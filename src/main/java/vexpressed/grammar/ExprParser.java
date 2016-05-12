// Generated from C:/work/vexpressed/src/main/java/vexpressed/grammar\Expr.g4 by ANTLR 4.5.1
package vexpressed.grammar;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ExprParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, OP_LT=5, OP_GT=6, OP_LE=7, OP_GE=8, OP_EQ=9, 
		OP_NE=10, OP_AND=11, OP_OR=12, OP_ADD=13, OP_SUB=14, OP_MUL=15, OP_DIV=16, 
		OP_MOD=17, K_IS=18, K_NOT=19, K_NULL=20, BOOLEAN_LITERAL=21, ID=22, NUMERIC_LITERAL=23, 
		STRING_LITERAL=24, SPACES=25, COMMENT=26, LINE_COMMENT=27, CUSTOM_OP=28, 
		UNEXPECTED_CHAR=29;
	public static final int
		RULE_result = 0, RULE_expr = 1, RULE_paramlist = 2, RULE_funarg = 3;
	public static final String[] ruleNames = {
		"result", "expr", "paramlist", "funarg"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'('", "')'", "','", "':'", null, null, null, null, null, null, 
		null, null, "'+'", "'-'", "'*'", "'/'", "'%'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, "OP_LT", "OP_GT", "OP_LE", "OP_GE", "OP_EQ", 
		"OP_NE", "OP_AND", "OP_OR", "OP_ADD", "OP_SUB", "OP_MUL", "OP_DIV", "OP_MOD", 
		"K_IS", "K_NOT", "K_NULL", "BOOLEAN_LITERAL", "ID", "NUMERIC_LITERAL", 
		"STRING_LITERAL", "SPACES", "COMMENT", "LINE_COMMENT", "CUSTOM_OP", "UNEXPECTED_CHAR"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Expr.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ExprParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ResultContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ResultContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_result; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprVisitor ) return ((ExprVisitor<? extends T>)visitor).visitResult(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ResultContext result() throws RecognitionException {
		ResultContext _localctx = new ResultContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_result);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(8);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ParensContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ParensContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprVisitor ) return ((ExprVisitor<? extends T>)visitor).visitParens(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NullLiteralContext extends ExprContext {
		public TerminalNode K_NULL() { return getToken(ExprParser.K_NULL, 0); }
		public NullLiteralContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprVisitor ) return ((ExprVisitor<? extends T>)visitor).visitNullLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class InfixFunctionContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode ID() { return getToken(ExprParser.ID, 0); }
		public InfixFunctionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprVisitor ) return ((ExprVisitor<? extends T>)visitor).visitInfixFunction(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ComparisonOpContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode OP_LT() { return getToken(ExprParser.OP_LT, 0); }
		public TerminalNode OP_GT() { return getToken(ExprParser.OP_GT, 0); }
		public TerminalNode OP_EQ() { return getToken(ExprParser.OP_EQ, 0); }
		public TerminalNode OP_NE() { return getToken(ExprParser.OP_NE, 0); }
		public TerminalNode OP_LE() { return getToken(ExprParser.OP_LE, 0); }
		public TerminalNode OP_GE() { return getToken(ExprParser.OP_GE, 0); }
		public ComparisonOpContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprVisitor ) return ((ExprVisitor<? extends T>)visitor).visitComparisonOp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UnarySignContext extends ExprContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public UnarySignContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprVisitor ) return ((ExprVisitor<? extends T>)visitor).visitUnarySign(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CustomOpContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode CUSTOM_OP() { return getToken(ExprParser.CUSTOM_OP, 0); }
		public CustomOpContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprVisitor ) return ((ExprVisitor<? extends T>)visitor).visitCustomOp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StringLiteralContext extends ExprContext {
		public TerminalNode STRING_LITERAL() { return getToken(ExprParser.STRING_LITERAL, 0); }
		public StringLiteralContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprVisitor ) return ((ExprVisitor<? extends T>)visitor).visitStringLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FunctionContext extends ExprContext {
		public ParamlistContext params;
		public TerminalNode ID() { return getToken(ExprParser.ID, 0); }
		public ParamlistContext paramlist() {
			return getRuleContext(ParamlistContext.class,0);
		}
		public FunctionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprVisitor ) return ((ExprVisitor<? extends T>)visitor).visitFunction(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IsNullContext extends ExprContext {
		public Token not;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode K_IS() { return getToken(ExprParser.K_IS, 0); }
		public TerminalNode K_NULL() { return getToken(ExprParser.K_NULL, 0); }
		public TerminalNode K_NOT() { return getToken(ExprParser.K_NOT, 0); }
		public IsNullContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprVisitor ) return ((ExprVisitor<? extends T>)visitor).visitIsNull(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class VariableContext extends ExprContext {
		public TerminalNode ID() { return getToken(ExprParser.ID, 0); }
		public VariableContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprVisitor ) return ((ExprVisitor<? extends T>)visitor).visitVariable(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LogicOpContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode OP_AND() { return getToken(ExprParser.OP_AND, 0); }
		public TerminalNode OP_OR() { return getToken(ExprParser.OP_OR, 0); }
		public LogicOpContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprVisitor ) return ((ExprVisitor<? extends T>)visitor).visitLogicOp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ArithmeticOpContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode OP_MUL() { return getToken(ExprParser.OP_MUL, 0); }
		public TerminalNode OP_DIV() { return getToken(ExprParser.OP_DIV, 0); }
		public TerminalNode OP_MOD() { return getToken(ExprParser.OP_MOD, 0); }
		public TerminalNode OP_ADD() { return getToken(ExprParser.OP_ADD, 0); }
		public TerminalNode OP_SUB() { return getToken(ExprParser.OP_SUB, 0); }
		public ArithmeticOpContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprVisitor ) return ((ExprVisitor<? extends T>)visitor).visitArithmeticOp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BooleanLiteralContext extends ExprContext {
		public TerminalNode BOOLEAN_LITERAL() { return getToken(ExprParser.BOOLEAN_LITERAL, 0); }
		public BooleanLiteralContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprVisitor ) return ((ExprVisitor<? extends T>)visitor).visitBooleanLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NumericLiteralContext extends ExprContext {
		public TerminalNode NUMERIC_LITERAL() { return getToken(ExprParser.NUMERIC_LITERAL, 0); }
		public NumericLiteralContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprVisitor ) return ((ExprVisitor<? extends T>)visitor).visitNumericLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 2;
		enterRecursionRule(_localctx, 2, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(28);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				_localctx = new UnarySignContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(11);
				((UnarySignContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==OP_ADD || _la==OP_SUB) ) {
					((UnarySignContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(12);
				expr(11);
				}
				break;
			case 2:
				{
				_localctx = new StringLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(13);
				match(STRING_LITERAL);
				}
				break;
			case 3:
				{
				_localctx = new BooleanLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(14);
				match(BOOLEAN_LITERAL);
				}
				break;
			case 4:
				{
				_localctx = new NumericLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(15);
				match(NUMERIC_LITERAL);
				}
				break;
			case 5:
				{
				_localctx = new NullLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(16);
				match(K_NULL);
				}
				break;
			case 6:
				{
				_localctx = new FunctionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(17);
				match(ID);
				setState(18);
				match(T__0);
				setState(20);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << OP_ADD) | (1L << OP_SUB) | (1L << K_NULL) | (1L << BOOLEAN_LITERAL) | (1L << ID) | (1L << NUMERIC_LITERAL) | (1L << STRING_LITERAL))) != 0)) {
					{
					setState(19);
					((FunctionContext)_localctx).params = paramlist();
					}
				}

				setState(22);
				match(T__1);
				}
				break;
			case 7:
				{
				_localctx = new VariableContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(23);
				match(ID);
				}
				break;
			case 8:
				{
				_localctx = new ParensContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(24);
				match(T__0);
				setState(25);
				expr(0);
				setState(26);
				match(T__1);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(56);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(54);
					switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
					case 1:
						{
						_localctx = new ArithmeticOpContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(30);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(31);
						((ArithmeticOpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << OP_MUL) | (1L << OP_DIV) | (1L << OP_MOD))) != 0)) ) {
							((ArithmeticOpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(32);
						expr(11);
						}
						break;
					case 2:
						{
						_localctx = new ArithmeticOpContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(33);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(34);
						((ArithmeticOpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==OP_ADD || _la==OP_SUB) ) {
							((ArithmeticOpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(35);
						expr(10);
						}
						break;
					case 3:
						{
						_localctx = new ComparisonOpContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(36);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(37);
						((ComparisonOpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << OP_LT) | (1L << OP_GT) | (1L << OP_LE) | (1L << OP_GE) | (1L << OP_EQ) | (1L << OP_NE))) != 0)) ) {
							((ComparisonOpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(38);
						expr(9);
						}
						break;
					case 4:
						{
						_localctx = new LogicOpContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(39);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(40);
						((LogicOpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==OP_AND || _la==OP_OR) ) {
							((LogicOpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(41);
						expr(7);
						}
						break;
					case 5:
						{
						_localctx = new InfixFunctionContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(42);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(43);
						match(ID);
						setState(44);
						expr(5);
						}
						break;
					case 6:
						{
						_localctx = new CustomOpContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(45);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(46);
						match(CUSTOM_OP);
						setState(47);
						expr(4);
						}
						break;
					case 7:
						{
						_localctx = new IsNullContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(48);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(49);
						match(K_IS);
						setState(51);
						_la = _input.LA(1);
						if (_la==K_NOT) {
							{
							setState(50);
							((IsNullContext)_localctx).not = match(K_NOT);
							}
						}

						setState(53);
						match(K_NULL);
						}
						break;
					}
					} 
				}
				setState(58);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class ParamlistContext extends ParserRuleContext {
		public List<FunargContext> funarg() {
			return getRuleContexts(FunargContext.class);
		}
		public FunargContext funarg(int i) {
			return getRuleContext(FunargContext.class,i);
		}
		public ParamlistContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_paramlist; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprVisitor ) return ((ExprVisitor<? extends T>)visitor).visitParamlist(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParamlistContext paramlist() throws RecognitionException {
		ParamlistContext _localctx = new ParamlistContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_paramlist);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(59);
			funarg();
			setState(64);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(60);
				match(T__2);
				setState(61);
				funarg();
				}
				}
				setState(66);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunargContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode ID() { return getToken(ExprParser.ID, 0); }
		public FunargContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funarg; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprVisitor ) return ((ExprVisitor<? extends T>)visitor).visitFunarg(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunargContext funarg() throws RecognitionException {
		FunargContext _localctx = new FunargContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_funarg);
		try {
			setState(71);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(67);
				expr(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(68);
				match(ID);
				setState(69);
				match(T__3);
				setState(70);
				expr(0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 1:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 10);
		case 1:
			return precpred(_ctx, 9);
		case 2:
			return precpred(_ctx, 8);
		case 3:
			return precpred(_ctx, 6);
		case 4:
			return precpred(_ctx, 4);
		case 5:
			return precpred(_ctx, 3);
		case 6:
			return precpred(_ctx, 7);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\37L\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3"+
		"\27\n\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\37\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\66\n\3\3"+
		"\3\7\39\n\3\f\3\16\3<\13\3\3\4\3\4\3\4\7\4A\n\4\f\4\16\4D\13\4\3\5\3\5"+
		"\3\5\3\5\5\5J\n\5\3\5\2\3\4\6\2\4\6\b\2\6\3\2\17\20\3\2\21\23\3\2\7\f"+
		"\3\2\r\16Y\2\n\3\2\2\2\4\36\3\2\2\2\6=\3\2\2\2\bI\3\2\2\2\n\13\5\4\3\2"+
		"\13\3\3\2\2\2\f\r\b\3\1\2\r\16\t\2\2\2\16\37\5\4\3\r\17\37\7\32\2\2\20"+
		"\37\7\27\2\2\21\37\7\31\2\2\22\37\7\26\2\2\23\24\7\30\2\2\24\26\7\3\2"+
		"\2\25\27\5\6\4\2\26\25\3\2\2\2\26\27\3\2\2\2\27\30\3\2\2\2\30\37\7\4\2"+
		"\2\31\37\7\30\2\2\32\33\7\3\2\2\33\34\5\4\3\2\34\35\7\4\2\2\35\37\3\2"+
		"\2\2\36\f\3\2\2\2\36\17\3\2\2\2\36\20\3\2\2\2\36\21\3\2\2\2\36\22\3\2"+
		"\2\2\36\23\3\2\2\2\36\31\3\2\2\2\36\32\3\2\2\2\37:\3\2\2\2 !\f\f\2\2!"+
		"\"\t\3\2\2\"9\5\4\3\r#$\f\13\2\2$%\t\2\2\2%9\5\4\3\f&\'\f\n\2\2\'(\t\4"+
		"\2\2(9\5\4\3\13)*\f\b\2\2*+\t\5\2\2+9\5\4\3\t,-\f\6\2\2-.\7\30\2\2.9\5"+
		"\4\3\7/\60\f\5\2\2\60\61\7\36\2\2\619\5\4\3\6\62\63\f\t\2\2\63\65\7\24"+
		"\2\2\64\66\7\25\2\2\65\64\3\2\2\2\65\66\3\2\2\2\66\67\3\2\2\2\679\7\26"+
		"\2\28 \3\2\2\28#\3\2\2\28&\3\2\2\28)\3\2\2\28,\3\2\2\28/\3\2\2\28\62\3"+
		"\2\2\29<\3\2\2\2:8\3\2\2\2:;\3\2\2\2;\5\3\2\2\2<:\3\2\2\2=B\5\b\5\2>?"+
		"\7\5\2\2?A\5\b\5\2@>\3\2\2\2AD\3\2\2\2B@\3\2\2\2BC\3\2\2\2C\7\3\2\2\2"+
		"DB\3\2\2\2EJ\5\4\3\2FG\7\30\2\2GH\7\6\2\2HJ\5\4\3\2IE\3\2\2\2IF\3\2\2"+
		"\2J\t\3\2\2\2\t\26\36\658:BI";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}