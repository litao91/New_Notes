```scala
  lazy val sessionState: SessionState = {
    parentSessionState
      .map(_.clone(this))
      .getOrElse {
        val state = SparkSession.instantiateSessionState(
          SparkSession.sessionStateClassName(sparkContext.conf),
          self)
        initialSessionOptions.foreach { case (k, v) => state.conf.setConfString(k, v) }
        state
      }
  }
```

```
  private def sessionStateClassName(conf: SparkConf): String = {
    conf.get(CATALOG_IMPLEMENTATION) match {
      case "hive" => HIVE_SESSION_STATE_BUILDER_CLASS_NAME
      case "in-memory" => classOf[SessionStateBuilder].getCanonicalName
    }
  }
```


```scala
  /**
   * Helper method to create an instance of `SessionState` based on `className` from conf.
   * The result is either `SessionState` or a Hive based `SessionState`.
   */
  private def instantiateSessionState(
      className: String,
      sparkSession: SparkSession): SessionState = {
    try {
      // invoke `new [Hive]SessionStateBuilder(SparkSession, Option[SessionState])`
      val clazz = Utils.classForName(className)
      val ctor = clazz.getConstructors.head
      ctor.newInstance(sparkSession, None).asInstanceOf[BaseSessionStateBuilder].build()
    } catch {
      case NonFatal(e) =>
        throw new IllegalArgumentException(s"Error while instantiating '$className':", e)
    }
  }
```

```scala
private[sql] class SessionState(
    sharedState: SharedState,
    val conf: SQLConf,
    val experimentalMethods: ExperimentalMethods,
    val functionRegistry: FunctionRegistry,
    val udfRegistration: UDFRegistration,
    catalogBuilder: () => SessionCatalog,
    val sqlParser: ParserInterface,
    analyzerBuilder: () => Analyzer,
    optimizerBuilder: () => Optimizer,
    val planner: SparkPlanner,
    val streamingQueryManager: StreamingQueryManager,
    val listenerManager: ExecutionListenerManager,
    resourceLoaderBuilder: () => SessionResourceLoader,
    createQueryExecution: LogicalPlan => QueryExecution,
    createClone: (SparkSession, SessionState) => SessionState) {
```

```scala
  /**
   * Build the [[SessionState]].
   */
  def build(): SessionState = {
    new SessionState(
      session.sharedState,
      conf,
      experimentalMethods,
      functionRegistry,
      udfRegistration,
      () => catalog,
      sqlParser,
      () => analyzer,
      () => optimizer,
      planner,
      streamingQueryManager,
      listenerManager,
      () => resourceLoader,
      createQueryExecution,
      createClone)
  }
}
```

Where

```scala
  protected def createQueryExecution: LogicalPlan => QueryExecution = { plan =>
    new QueryExecution(session, plan)
  }
```

Analyzer

```scala
// BaseSessionStateBuilder#analyzer
  protected def analyzer: Analyzer = new Analyzer(catalog, conf) {
    override val extendedResolutionRules: Seq[Rule[LogicalPlan]] =
      new FindDataSourceTable(session) +:
        new ResolveSQLOnFile(session) +:
        customResolutionRules

    override val postHocResolutionRules: Seq[Rule[LogicalPlan]] =
      PreprocessTableCreation(session) +:
        PreprocessTableInsertion(conf) +:
        DataSourceAnalysis(conf) +:
        customPostHocResolutionRules

    override val extendedCheckRules: Seq[LogicalPlan => Unit] =
      PreWriteCheck +:
        PreReadCheck +:
        HiveOnlyCheck +:
        customCheckRules
  }
```

```scala
// SessionState#analyzer
  lazy val analyzer: Analyzer = analyzerBuilder()
```


QueryExecution#assertAnalyzed

```scala
  def assertAnalyzed(): Unit = {
    // Analyzer is invoked outside the try block to avoid calling it again from within the
    // catch block below.
    analyzed
      sparkSession.sessionState.analyzer.checkAnalysis(analyzed)
  }
```

```scala
  lazy val analyzed: LogicalPlan = {
    SparkSession.setActiveSession(sparkSession)
    sparkSession.sessionState.analyzer.execute(logical)
  }
```

Analyzer#batches
```
  lazy val batches: Seq[Batch] = Seq(
    Batch("Hints", fixedPoint,
      new ResolveHints.ResolveBroadcastHints(conf),
      ResolveHints.RemoveAllHints),
    Batch("Simple Sanity Check", Once,
      LookupFunctions),
    Batch("Substitution", fixedPoint,
      CTESubstitution,
      WindowsSubstitution,
      EliminateUnions,
      new SubstituteUnresolvedOrdinals(conf)),
    Batch("Resolution", fixedPoint,
      ResolveTableValuedFunctions ::
      ResolveRelations ::
      ResolveReferences ::
      ResolveCreateNamedStruct ::
      ResolveDeserializer ::
      ResolveNewInstance ::
      ResolveUpCast ::
      ResolveGroupingAnalytics ::
      ResolvePivot ::
      ResolveOrdinalInOrderByAndGroupBy ::
      ResolveAggAliasInGroupBy ::
      ResolveMissingReferences ::
      ExtractGenerator ::
      ResolveGenerate ::
      ResolveFunctions ::
      ResolveAliases ::
      ResolveSubquery ::
      ResolveSubqueryColumnAliases ::
      ResolveWindowOrder ::
      ResolveWindowFrame ::
      ResolveNaturalAndUsingJoin ::
      ExtractWindowExpressions ::
      GlobalAggregates ::
      ResolveAggregateFunctions ::
      TimeWindowing ::
      ResolveInlineTables(conf) ::
      ResolveTimeZone(conf) ::
      TypeCoercion.typeCoercionRules ++
      extendedResolutionRules : _*),
    Batch("Post-Hoc Resolution", Once, postHocResolutionRules: _*),
    Batch("View", Once,
      AliasViewChild(conf)),
    Batch("Nondeterministic", Once,
      PullOutNondeterministic),
    Batch("UDF", Once,
      HandleNullInputsForUDF),
    Batch("FixNullability", Once,
      FixNullability),
    Batch("Subquery", Once,
      UpdateOuterReferences),
    Batch("Cleanup", fixedPoint,
      CleanupAliases)
  )
```


RuleExecutor#execute

```scala

  def execute(plan: TreeType): TreeType = {
    var curPlan = plan

    batches.foreach { batch =>
      val batchStartPlan = curPlan
      var iteration = 1
      var lastPlan = curPlan
      var continue = true

      // Run until fix point (or the max number of iterations as specified in the strategy.
      while (continue) {
        curPlan = batch.rules.foldLeft(curPlan) {
          case (plan, rule) =>
            val result = rule(plan)
            result
        }
        iteration += 1
        // max iteration reached
        if (iteration > batch.strategy.maxIterations) {
          continue = false
        }

        // fix point
        if (curPlan.fastEquals(lastPlan)) {
          continue = false
        }
        lastPlan = curPlan
      }

      if (!batchStartPlan.fastEquals(curPlan)) {
        logDebug(
          s"""
          |=== Result of Batch ${batch.name} ===
          |${sideBySide(batchStartPlan.treeString, curPlan.treeString).mkString("\n")}
        """.stripMargin)
      } else {
        logTrace(s"Batch ${batch.name} has no effect.")
      }
    }

    curPlan
  }
}
```
