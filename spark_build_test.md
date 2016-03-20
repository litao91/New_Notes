# Build Spark
## Build with maven
### Basic
```shell
build/mvn -Pyarn -Phadoop-2.6 -Dhadoop.verson=2.6.0 -DskipTests clean package
```

Incremental
```shell
build/mvn -Pyarn -Phadoop-2.6 -Dhadoop.verson=2.6.0 -DskipTests package
```

### Build a Runnable Distribution

```shell
./make-distribution.sh --name custom-spark --tgz -Psparkr -Phadoop-2.6 -Phive -Phive-thriftserver -Pyarn
```

### Setting up Maven's Memory Usage

```shell
export MAVEN_OPTS="-Xmx2g -XX:MaxPermSize=512M -XX:ReservedCodeCacheSize=512m"
```

### Build with JDBC support
```shell
mvn -Pyarn -Phadoop-2.6 -Dhadoop.version=2.6.0 -Phive -Phive-thriftserver -DskipTests clean package
```

### Build submodules individually
For example:
```
mvn -pl :spark-streaming_2.10 clean install
```
Where `spark-streaming_2.10` is the `artifactId` specified in the
`streaming/pom.xml` file

### Continuous Compilation
Incremental and continuous compilation. E.g.,
```
mvn scala:cc
```
Should run continuous compilation (i.e., wait for changes)

The full flow for running continuous-compilation:
```
$ mvn install
$ cd core
$ mvn scala:cc
```

## Build with SBT
```
build\sbt -Pyarn -Phadoop-2.6 assembly
```


## Reducing Build Times
```shell
$ build/sbt clean assembly # Create a normal assembly
$ ./bin/spark-shell # Use spark with the normal assembly
$ export SPARK_PREPEND_CLASSES=true
$ ./bin/spark-shell # Now it's using compiled classes
# ... do some local development ... #
$ build/sbt compile
# ... do some local development ... #
$ build/sbt compile
$ unset SPARK_PREPEND_CLASSES
$ ./bin/spark-shell # Back to normal, using Spark classes from the assembly jar
 
# You can also use ~ to let sbt do incremental builds on file changes without running a new sbt session every time
$ build/sbt ~compile
```



# Run Test
Note that we need to run package first, the build test sequence
```shell
mvn -Pyarn -Phadoop-2.6 -DskipTests -Phive -Phive-thriftserver clean package
mvn -Pyarn -Phadoop-2.6 -Phive -Phive-thriftserver test
```

SBT:
```
build/sbt -Pyarn -Phadoop-2.6 -Phive -Phive-thriftserver test
build/sbt -Pyarn -Phadoop-2.6 -Phive -Phive-thriftserver core/test
build/sbt -Pyarn -Phadoop-2.3 -Phive -Phive-thriftserver "test-only org.apache.spark.repl.ReplSuite"
```



## Running Individual Tests
```
# sbt
$ build/sbt "test-only org.apache.spark.io.CompressionCodecSuite"
$ build/sbt "test-only org.apache.spark.io.*"
 
# Maven, run Scala test
$ build/mvn test -DwildcardSuites=org.apache.spark.io.CompressionCodecSuite -Dtest=none
$ build/mvn test -DwildcardSuites=org.apache.spark.io.* -Dtest=none
 
# The above will work, but will take time to iterate through each project.  If you want
# to only run tests in one subproject, first run "install", then use "-pl <project>"
# with the tests
$ build/mvn <options> install
$ build/mvn <other options> -pl org.apache.spark:spark-hive_2.11 test -DwildcardSuites=org.apache.spark.sql.hive.execution.HiveTableScanSuite -Dtest=none
 
# Maven, run Java test
$ build/mvn test -DwildcardSuites=none -Dtest=org.apache.spark.streaming.JavaAPISuite
```
