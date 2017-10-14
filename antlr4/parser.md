# Tree

## Tree

```java
public interface Tree {
	Tree getParent();
	Object getPayload();
	Tree getChild(int i);
	int getChildCount();
	String toStringTree();
}
```

Note: **getPayLoad** returns whatever objects represents the data at this node.

## SyntaxTree

```java
public interface SyntaxTree extends Tree {
	Interval getSourceInterval();
}
```

It returns a Interval indicating the first and last token associate with this 
subtree


## ParseTree

The tree of `RuleContext` objects

* Internal nodes
* Rule invocation


```java
public interface ParseTree extends SyntaxTree {
	// the following methods narrow the return type; they are not additional methods
	@Override
	ParseTree getParent();
	@Override
	ParseTree getChild(int i);


	void setParent(RuleContext parent);

	<T> T accept(ParseTreeVisitor<? extends T> visitor);

	/** Return the combined text of all leaf nodes. Does not get any
	 *  off-channel tokens (if any) so won't return whitespace and
	 *  comments if they are sent to parser on hidden channel.
	 */
	String getText();

	String toStringTree(Parser parser);
}
```

## RuleNode

```java
public interface RuleNode extends ParseTree {
	RuleContext getRuleContext();
}
```

## RuleContext

* A rule context is a record of a **single rule invocation**.
* Form a stack of context objects using the parent pointer. 
* The root node always has a null pointer and `invokingState` of -1

Upon entry to parsing:

The first invoked rule function creates a context object and makes it
the root of the parse tree. 

A subclass specialized for that rule: 

```java
 public final SContext s() throws RecognitionException {
     SContext _localctx = new SContext(_ctx, getState()); <-- create new node
     enterRule(_localctx, 0, RULE_s);                     <-- push it
     ...
     exitRule();                                          <-- pop back to _localctx
     return _localctx;
 }
 ```

 A subsequent rule invocation from the start rule `s` pushes a new context object for `r`
 whose parent points at `s` and use inovking state is the state with `r` emanating as 
 edge label. 

 `invokingState` -- form a stack of rule indication states (root has -1)

 If we invoke start symbol `s` then `r1` which calls `r2`:

```
SContext[-1]   <- root node (bottom of the stack)
R1Context[p]   <- p in rule s called r1
R2Context[q]   <- q in rule r1 called r2
```

```java
public class RuleContext implements RuleNode {
	public static final ParserRuleContext EMPTY = new ParserRuleContext();

	/** What context invoked this rule? */
	public RuleContext parent;

	/** What state invoked the rule associated with this context?
	 *  The "return address" is the followState of invokingState
	 *  If parent is null, this should be -1 this context object represents
	 *  the start rule.
	 */
	public int invokingState = -1;

	public RuleContext() {}

	public RuleContext(RuleContext parent, int invokingState) {
		this.parent = parent;
		//if ( parent!=null ) System.out.println("invoke "+stateNumber+" from "+parent);
		this.invokingState = invokingState;
	}

	public int depth() {
        // num of parents to the root
	}

	/** A context is empty if there is no invoking state; meaning nobody called
	 *  current context.
	 */
	public boolean isEmpty() {
		return invokingState == -1;
	}

	// satisfy the ParseTree / SyntaxTree interface

	@Override
	public Interval getSourceInterval() {
		return Interval.INVALID;
	}

	@Override
	public RuleContext getRuleContext() { return this; }

	@Override
	public RuleContext getParent() { return parent; }

	@Override
	public RuleContext getPayload() { return this; }

	/** Return the combined text of all child nodes. 
	 */
	@Override
	public String getText() {
		if (getChildCount() == 0) {
			return "";
		}

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < getChildCount(); i++) {
			builder.append(getChild(i).getText());
		}

		return builder.toString();
	}

	public int getRuleIndex() { return -1; }

	/** For rule associated with this parse tree internal node, return
	 *  the outer alternative number used to match the input. Default
	 *  implementation does not compute nor store this alt num. Create
	 *  a subclass of ParserRuleContext with backing field and set
	 *  option contextSuperClass.
	 *  to set it.
	 *
	 *  @since 4.5.3
	 */
	public int getAltNumber() { return ATN.INVALID_ALT_NUMBER; }

	/** Set the outer alternative number for this context node. Default
	 *  implementation does nothing to avoid backing field overhead for
	 *  trees that don't need it.  Create
     *  a subclass of ParserRuleContext with backing field and set
     *  option contextSuperClass.
	 *
	 *  @since 4.5.3
	 */
	public void setAltNumber(int altNumber) { }

	/** @since 4.7. {@see ParseTree#setParent} comment */
	@Override
	public void setParent(RuleContext parent) {
		this.parent = parent;
	}

	@Override
	public ParseTree getChild(int i) {
		return null;
	}

	@Override
	public int getChildCount() {
		return 0;
	}

	@Override
	public <T> T accept(ParseTreeVisitor<? extends T> visitor) { return visitor.visitChildren(this); }

    // omit to string methods ...

}
```

## ParserRuleContext

A rule invocation record for parsing

Subclasses made for each rule and grammer track the parameters, return values,
locals and labels specific to that rule. 

```java
public class ParserRuleContext extends RuleContext {
	public List<ParseTree> children;

	public Token start, stop;

	/**
	 * The exception that forced this rule to return. If the rule successfully
	 * completed, this is {@code null}.
	 */
	public RecognitionException exception;

	public ParserRuleContext() { }

	/** COPY a ctx (I'm deliberately not using copy constructor) to avoid
	 *  confusion with creating node with parent. Does not copy children
	 *  (except error leaves).
	 */
	public void copyFrom(ParserRuleContext ctx) {
		this.parent = ctx.parent;
		this.invokingState = ctx.invokingState;

		this.start = ctx.start;
		this.stop = ctx.stop;

		// copy any error nodes to alt label node
		if ( ctx.children!=null ) {
			this.children = new ArrayList<>();
			// reset parent pointer for any error nodes
			for (ParseTree child : ctx.children) {
				if ( child instanceof ErrorNode ) {
					addChild((ErrorNode)child);
				}
			}
		}
	}

	public ParserRuleContext(ParserRuleContext parent, int invokingStateNumber) {
		super(parent, invokingStateNumber);
	}

	// Double dispatch methods for listeners

	public void enterRule(ParseTreeListener listener) { }
	public void exitRule(ParseTreeListener listener) { }

	/** Add a parse tree node to this as a child.  Works for
	 *  internal and leaf nodes. Does not set parent link;
	 *  other add methods must do that. Other addChild methods
	 *  call this.
	 *
	 *  We cannot set the parent pointer of the incoming node
	 *  because the existing interfaces do not have a setParent()
	 *  method and I don't want to break backward compatibility for this.
	 *
	 *  @since 4.7
	 */
	public <T extends ParseTree> T addAnyChild(T t) {
		if ( children==null ) children = new ArrayList<>();
		children.add(t);
		return t;
	}

	public RuleContext addChild(RuleContext ruleInvocation) {
		return addAnyChild(ruleInvocation);
	}

	/** Add a token leaf node child and force its parent to be this node. */
	public TerminalNode addChild(TerminalNode t) {
		t.setParent(this);
		return addAnyChild(t);
	}

	/** Add an error node child and force its parent to be this node.
	 *
	 * @since 4.7
	 */
	public ErrorNode addErrorNode(ErrorNode errorNode) {
		errorNode.setParent(this);
		return addAnyChild(errorNode);
	}

	/** Add a child to this node based upon matchedToken. It
	 *  creates a TerminalNodeImpl rather than using
	 *  {@link Parser#createTerminalNode(ParserRuleContext, Token)}. I'm leaving this
     *  in for compatibility but the parser doesn't use this anymore.
	 */
	@Deprecated
	public TerminalNode addChild(Token matchedToken) {
		TerminalNodeImpl t = new TerminalNodeImpl(matchedToken);
		addAnyChild(t);
		t.setParent(this);
		return t;
	}

	/** Add a child to this node based upon badToken.  It
	 *  creates a ErrorNodeImpl rather than using
	 *  {@link Parser#createErrorNode(ParserRuleContext, Token)}. I'm leaving this
	 *  in for compatibility but the parser doesn't use this anymore.
	 */
	@Deprecated
	public ErrorNode addErrorNode(Token badToken) {
		ErrorNodeImpl t = new ErrorNodeImpl(badToken);
		addAnyChild(t);
		t.setParent(this);
		return t;
	}

	/** Used by enterOuterAlt to toss out a RuleContext previously added as
	 *  we entered a rule. If we have # label, we will need to remove
	 *  generic ruleContext object.
	 */
	public void removeLastChild() {
		if ( children!=null ) {
			children.remove(children.size()-1);
		}
	}

	@Override
	/** Override to make type more specific */
	public ParserRuleContext getParent() {
		return (ParserRuleContext)super.getParent();
	}

	@Override
	public ParseTree getChild(int i) {
		return children!=null && i>=0 && i<children.size() ? children.get(i) : null;
	}

	public <T extends ParseTree> T getChild(Class<? extends T> ctxType, int i) {
		if ( children==null || i < 0 || i >= children.size() ) {
			return null;
		}

		int j = -1; // what element have we found with ctxType?
		for (ParseTree o : children) {
			if ( ctxType.isInstance(o) ) {
				j++;
				if ( j == i ) {
					return ctxType.cast(o);
				}
			}
		}
		return null;
	}

	public TerminalNode getToken(int ttype, int i) {
		if ( children==null || i < 0 || i >= children.size() ) {
			return null;
		}

		int j = -1; // what token with ttype have we found?
		for (ParseTree o : children) {
			if ( o instanceof TerminalNode ) {
				TerminalNode tnode = (TerminalNode)o;
				Token symbol = tnode.getSymbol();
				if ( symbol.getType()==ttype ) {
					j++;
					if ( j == i ) {
						return tnode;
					}
				}
			}
		}

		return null;
	}

	public List<TerminalNode> getTokens(int ttype) {
		if ( children==null ) {
			return Collections.emptyList();
		}

		List<TerminalNode> tokens = null;
		for (ParseTree o : children) {
			if ( o instanceof TerminalNode ) {
				TerminalNode tnode = (TerminalNode)o;
				Token symbol = tnode.getSymbol();
				if ( symbol.getType()==ttype ) {
					if ( tokens==null ) {
						tokens = new ArrayList<TerminalNode>();
					}
					tokens.add(tnode);
				}
			}
		}

		if ( tokens==null ) {
			return Collections.emptyList();
		}

		return tokens;
	}

	public <T extends ParserRuleContext> T getRuleContext(Class<? extends T> ctxType, int i) {
		return getChild(ctxType, i);
	}

	public <T extends ParserRuleContext> List<T> getRuleContexts(Class<? extends T> ctxType) {
		if ( children==null ) {
			return Collections.emptyList();
		}

		List<T> contexts = null;
		for (ParseTree o : children) {
			if ( ctxType.isInstance(o) ) {
				if ( contexts==null ) {
					contexts = new ArrayList<T>();
				}

				contexts.add(ctxType.cast(o));
			}
		}

		if ( contexts==null ) {
			return Collections.emptyList();
		}

		return contexts;
	}

	@Override
	public int getChildCount() { return children!=null ? children.size() : 0; }

	@Override
	public Interval getSourceInterval() {
		if ( start == null ) {
			return Interval.INVALID;
		}
		if ( stop==null || stop.getTokenIndex()<start.getTokenIndex() ) {
			return Interval.of(start.getTokenIndex(), start.getTokenIndex()-1); // empty
		}
		return Interval.of(start.getTokenIndex(), stop.getTokenIndex());
	}

	/**
	 * Get the initial token in this context.
	 * Note that the range from start to stop is inclusive, so for rules that do not consume anything
	 * (for example, zero length or error productions) this token may exceed stop.
	 */
	public Token getStart() { return start; }
	/**
	 * Get the final token in this context.
	 * Note that the range from start to stop is inclusive, so for rules that do not consume anything
	 * (for example, zero length or error productions) this token may precede start.
	 */
	public Token getStop() { return stop; }

	/** Used for rule context info debugging during parse-time, not so much for ATN debugging */
	public String toInfoString(Parser recognizer) {
		List<String> rules = recognizer.getRuleInvocationStack(this);
		Collections.reverse(rules);
		return "ParserRuleContext"+rules+"{" +
			"start=" + start +
			", stop=" + stop +
			'}';
	}
}
```

# Parser

