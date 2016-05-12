// Generated from C:/work/vexpressed/src/main/java/vexpressed/grammar\Expr.g4 by ANTLR 4.5.1
package vexpressed.grammar;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ExprLexer extends Lexer {
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
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "OP_LT", "OP_GT", "OP_LE", "OP_GE", "OP_EQ", 
		"OP_NE", "OP_AND", "OP_OR", "OP_ADD", "OP_SUB", "OP_MUL", "OP_DIV", "OP_MOD", 
		"K_IS", "K_NOT", "K_NULL", "BOOLEAN_LITERAL", "ID", "NUMERIC_LITERAL", 
		"STRING_LITERAL", "SPACES", "COMMENT", "LINE_COMMENT", "CUSTOM_OP", "UNEXPECTED_CHAR", 
		"DIGIT", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", 
		"N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
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


	public ExprLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Expr.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\37\u0177\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64"+
		"\t\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\3\2\3\2\3\3\3\3\3\4\3"+
		"\4\3\5\3\5\3\6\3\6\3\6\3\6\5\6\u0080\n\6\3\7\3\7\3\7\3\7\5\7\u0086\n\7"+
		"\3\b\3\b\3\b\3\b\3\b\5\b\u008d\n\b\3\t\3\t\3\t\3\t\3\t\5\t\u0094\n\t\3"+
		"\n\3\n\3\n\3\n\3\n\5\n\u009b\n\n\5\n\u009d\n\n\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\3\13\3\13\3\13\5\13\u00aa\n\13\3\f\3\f\3\f\3\f\3\f\3\f"+
		"\5\f\u00b2\n\f\3\r\3\r\3\r\3\r\3\r\5\r\u00b9\n\r\3\16\3\16\3\17\3\17\3"+
		"\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\25\3"+
		"\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3"+
		"\26\3\26\3\26\5\26\u00de\n\26\3\27\3\27\7\27\u00e2\n\27\f\27\16\27\u00e5"+
		"\13\27\3\30\6\30\u00e8\n\30\r\30\16\30\u00e9\3\30\3\30\7\30\u00ee\n\30"+
		"\f\30\16\30\u00f1\13\30\5\30\u00f3\n\30\3\30\3\30\5\30\u00f7\n\30\3\30"+
		"\6\30\u00fa\n\30\r\30\16\30\u00fb\5\30\u00fe\n\30\3\30\3\30\6\30\u0102"+
		"\n\30\r\30\16\30\u0103\3\30\3\30\5\30\u0108\n\30\3\30\6\30\u010b\n\30"+
		"\r\30\16\30\u010c\5\30\u010f\n\30\5\30\u0111\n\30\3\31\3\31\3\31\3\31"+
		"\7\31\u0117\n\31\f\31\16\31\u011a\13\31\3\31\3\31\3\32\3\32\3\32\3\32"+
		"\3\33\3\33\3\33\3\33\7\33\u0126\n\33\f\33\16\33\u0129\13\33\3\33\3\33"+
		"\3\33\3\33\3\33\3\34\3\34\3\34\3\34\7\34\u0134\n\34\f\34\16\34\u0137\13"+
		"\34\3\34\3\34\3\35\6\35\u013c\n\35\r\35\16\35\u013d\3\36\3\36\3\37\3\37"+
		"\3 \3 \3!\3!\3\"\3\"\3#\3#\3$\3$\3%\3%\3&\3&\3\'\3\'\3(\3(\3)\3)\3*\3"+
		"*\3+\3+\3,\3,\3-\3-\3.\3.\3/\3/\3\60\3\60\3\61\3\61\3\62\3\62\3\63\3\63"+
		"\3\64\3\64\3\65\3\65\3\66\3\66\3\67\3\67\38\38\39\39\3\u0127\2:\3\3\5"+
		"\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21"+
		"!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37=\2?"+
		"\2A\2C\2E\2G\2I\2K\2M\2O\2Q\2S\2U\2W\2Y\2[\2]\2_\2a\2c\2e\2g\2i\2k\2m"+
		"\2o\2q\2\3\2$\6\2&&C\\aac|\b\2&&\60\60\62;C\\aac|\4\2--//\3\2))\5\2\13"+
		"\r\17\17\"\"\4\2\f\f\17\17\n\2##%(,-/\61<B`a~~\u0080\u0080\3\2\62;\4\2"+
		"CCcc\4\2DDdd\4\2EEee\4\2FFff\4\2GGgg\4\2HHhh\4\2IIii\4\2JJjj\4\2KKkk\4"+
		"\2LLll\4\2MMmm\4\2NNnn\4\2OOoo\4\2PPpp\4\2QQqq\4\2RRrr\4\2SSss\4\2TTt"+
		"t\4\2UUuu\4\2VVvv\4\2WWww\4\2XXxx\4\2YYyy\4\2ZZzz\4\2[[{{\4\2\\\\||\u017a"+
		"\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2"+
		"\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2"+
		"\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2"+
		"\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2"+
		"\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3"+
		"\2\2\2\3s\3\2\2\2\5u\3\2\2\2\7w\3\2\2\2\ty\3\2\2\2\13\177\3\2\2\2\r\u0085"+
		"\3\2\2\2\17\u008c\3\2\2\2\21\u0093\3\2\2\2\23\u009c\3\2\2\2\25\u00a9\3"+
		"\2\2\2\27\u00b1\3\2\2\2\31\u00b8\3\2\2\2\33\u00ba\3\2\2\2\35\u00bc\3\2"+
		"\2\2\37\u00be\3\2\2\2!\u00c0\3\2\2\2#\u00c2\3\2\2\2%\u00c4\3\2\2\2\'\u00c7"+
		"\3\2\2\2)\u00cb\3\2\2\2+\u00dd\3\2\2\2-\u00df\3\2\2\2/\u0110\3\2\2\2\61"+
		"\u0112\3\2\2\2\63\u011d\3\2\2\2\65\u0121\3\2\2\2\67\u012f\3\2\2\29\u013b"+
		"\3\2\2\2;\u013f\3\2\2\2=\u0141\3\2\2\2?\u0143\3\2\2\2A\u0145\3\2\2\2C"+
		"\u0147\3\2\2\2E\u0149\3\2\2\2G\u014b\3\2\2\2I\u014d\3\2\2\2K\u014f\3\2"+
		"\2\2M\u0151\3\2\2\2O\u0153\3\2\2\2Q\u0155\3\2\2\2S\u0157\3\2\2\2U\u0159"+
		"\3\2\2\2W\u015b\3\2\2\2Y\u015d\3\2\2\2[\u015f\3\2\2\2]\u0161\3\2\2\2_"+
		"\u0163\3\2\2\2a\u0165\3\2\2\2c\u0167\3\2\2\2e\u0169\3\2\2\2g\u016b\3\2"+
		"\2\2i\u016d\3\2\2\2k\u016f\3\2\2\2m\u0171\3\2\2\2o\u0173\3\2\2\2q\u0175"+
		"\3\2\2\2st\7*\2\2t\4\3\2\2\2uv\7+\2\2v\6\3\2\2\2wx\7.\2\2x\b\3\2\2\2y"+
		"z\7<\2\2z\n\3\2\2\2{|\5U+\2|}\5e\63\2}\u0080\3\2\2\2~\u0080\7>\2\2\177"+
		"{\3\2\2\2\177~\3\2\2\2\u0080\f\3\2\2\2\u0081\u0082\5K&\2\u0082\u0083\5"+
		"e\63\2\u0083\u0086\3\2\2\2\u0084\u0086\7@\2\2\u0085\u0081\3\2\2\2\u0085"+
		"\u0084\3\2\2\2\u0086\16\3\2\2\2\u0087\u0088\5U+\2\u0088\u0089\5G$\2\u0089"+
		"\u008d\3\2\2\2\u008a\u008b\7>\2\2\u008b\u008d\7?\2\2\u008c\u0087\3\2\2"+
		"\2\u008c\u008a\3\2\2\2\u008d\20\3\2\2\2\u008e\u008f\5K&\2\u008f\u0090"+
		"\5G$\2\u0090\u0094\3\2\2\2\u0091\u0092\7@\2\2\u0092\u0094\7?\2\2\u0093"+
		"\u008e\3\2\2\2\u0093\u0091\3\2\2\2\u0094\22\3\2\2\2\u0095\u0096\5G$\2"+
		"\u0096\u0097\5_\60\2\u0097\u009d\3\2\2\2\u0098\u009a\7?\2\2\u0099\u009b"+
		"\7?\2\2\u009a\u0099\3\2\2\2\u009a\u009b\3\2\2\2\u009b\u009d\3\2\2\2\u009c"+
		"\u0095\3\2\2\2\u009c\u0098\3\2\2\2\u009d\24\3\2\2\2\u009e\u009f\5Y-\2"+
		"\u009f\u00a0\5G$\2\u00a0\u00aa\3\2\2\2\u00a1\u00a2\5Y-\2\u00a2\u00a3\5"+
		"G$\2\u00a3\u00a4\5_\60\2\u00a4\u00aa\3\2\2\2\u00a5\u00a6\7#\2\2\u00a6"+
		"\u00aa\7?\2\2\u00a7\u00a8\7>\2\2\u00a8\u00aa\7@\2\2\u00a9\u009e\3\2\2"+
		"\2\u00a9\u00a1\3\2\2\2\u00a9\u00a5\3\2\2\2\u00a9\u00a7\3\2\2\2\u00aa\26"+
		"\3\2\2\2\u00ab\u00ac\5? \2\u00ac\u00ad\5Y-\2\u00ad\u00ae\5E#\2\u00ae\u00b2"+
		"\3\2\2\2\u00af\u00b0\7(\2\2\u00b0\u00b2\7(\2\2\u00b1\u00ab\3\2\2\2\u00b1"+
		"\u00af\3\2\2\2\u00b2\30\3\2\2\2\u00b3\u00b4\5[.\2\u00b4\u00b5\5a\61\2"+
		"\u00b5\u00b9\3\2\2\2\u00b6\u00b7\7~\2\2\u00b7\u00b9\7~\2\2\u00b8\u00b3"+
		"\3\2\2\2\u00b8\u00b6\3\2\2\2\u00b9\32\3\2\2\2\u00ba\u00bb\7-\2\2\u00bb"+
		"\34\3\2\2\2\u00bc\u00bd\7/\2\2\u00bd\36\3\2\2\2\u00be\u00bf\7,\2\2\u00bf"+
		" \3\2\2\2\u00c0\u00c1\7\61\2\2\u00c1\"\3\2\2\2\u00c2\u00c3\7\'\2\2\u00c3"+
		"$\3\2\2\2\u00c4\u00c5\5O(\2\u00c5\u00c6\5c\62\2\u00c6&\3\2\2\2\u00c7\u00c8"+
		"\5Y-\2\u00c8\u00c9\5[.\2\u00c9\u00ca\5e\63\2\u00ca(\3\2\2\2\u00cb\u00cc"+
		"\5Y-\2\u00cc\u00cd\5g\64\2\u00cd\u00ce\5U+\2\u00ce\u00cf\5U+\2\u00cf*"+
		"\3\2\2\2\u00d0\u00d1\5e\63\2\u00d1\u00d2\5a\61\2\u00d2\u00d3\5g\64\2\u00d3"+
		"\u00d4\5G$\2\u00d4\u00de\3\2\2\2\u00d5\u00de\5e\63\2\u00d6\u00d7\5I%\2"+
		"\u00d7\u00d8\5? \2\u00d8\u00d9\5U+\2\u00d9\u00da\5c\62\2\u00da\u00db\5"+
		"G$\2\u00db\u00de\3\2\2\2\u00dc\u00de\5I%\2\u00dd\u00d0\3\2\2\2\u00dd\u00d5"+
		"\3\2\2\2\u00dd\u00d6\3\2\2\2\u00dd\u00dc\3\2\2\2\u00de,\3\2\2\2\u00df"+
		"\u00e3\t\2\2\2\u00e0\u00e2\t\3\2\2\u00e1\u00e0\3\2\2\2\u00e2\u00e5\3\2"+
		"\2\2\u00e3\u00e1\3\2\2\2\u00e3\u00e4\3\2\2\2\u00e4.\3\2\2\2\u00e5\u00e3"+
		"\3\2\2\2\u00e6\u00e8\5=\37\2\u00e7\u00e6\3\2\2\2\u00e8\u00e9\3\2\2\2\u00e9"+
		"\u00e7\3\2\2\2\u00e9\u00ea\3\2\2\2\u00ea\u00f2\3\2\2\2\u00eb\u00ef\7\60"+
		"\2\2\u00ec\u00ee\5=\37\2\u00ed\u00ec\3\2\2\2\u00ee\u00f1\3\2\2\2\u00ef"+
		"\u00ed\3\2\2\2\u00ef\u00f0\3\2\2\2\u00f0\u00f3\3\2\2\2\u00f1\u00ef\3\2"+
		"\2\2\u00f2\u00eb\3\2\2\2\u00f2\u00f3\3\2\2\2\u00f3\u00fd\3\2\2\2\u00f4"+
		"\u00f6\5G$\2\u00f5\u00f7\t\4\2\2\u00f6\u00f5\3\2\2\2\u00f6\u00f7\3\2\2"+
		"\2\u00f7\u00f9\3\2\2\2\u00f8\u00fa\5=\37\2\u00f9\u00f8\3\2\2\2\u00fa\u00fb"+
		"\3\2\2\2\u00fb\u00f9\3\2\2\2\u00fb\u00fc\3\2\2\2\u00fc\u00fe\3\2\2\2\u00fd"+
		"\u00f4\3\2\2\2\u00fd\u00fe\3\2\2\2\u00fe\u0111\3\2\2\2\u00ff\u0101\7\60"+
		"\2\2\u0100\u0102\5=\37\2\u0101\u0100\3\2\2\2\u0102\u0103\3\2\2\2\u0103"+
		"\u0101\3\2\2\2\u0103\u0104\3\2\2\2\u0104\u010e\3\2\2\2\u0105\u0107\5G"+
		"$\2\u0106\u0108\t\4\2\2\u0107\u0106\3\2\2\2\u0107\u0108\3\2\2\2\u0108"+
		"\u010a\3\2\2\2\u0109\u010b\5=\37\2\u010a\u0109\3\2\2\2\u010b\u010c\3\2"+
		"\2\2\u010c\u010a\3\2\2\2\u010c\u010d\3\2\2\2\u010d\u010f\3\2\2\2\u010e"+
		"\u0105\3\2\2\2\u010e\u010f\3\2\2\2\u010f\u0111\3\2\2\2\u0110\u00e7\3\2"+
		"\2\2\u0110\u00ff\3\2\2\2\u0111\60\3\2\2\2\u0112\u0118\7)\2\2\u0113\u0117"+
		"\n\5\2\2\u0114\u0115\7)\2\2\u0115\u0117\7)\2\2\u0116\u0113\3\2\2\2\u0116"+
		"\u0114\3\2\2\2\u0117\u011a\3\2\2\2\u0118\u0116\3\2\2\2\u0118\u0119\3\2"+
		"\2\2\u0119\u011b\3\2\2\2\u011a\u0118\3\2\2\2\u011b\u011c\7)\2\2\u011c"+
		"\62\3\2\2\2\u011d\u011e\t\6\2\2\u011e\u011f\3\2\2\2\u011f\u0120\b\32\2"+
		"\2\u0120\64\3\2\2\2\u0121\u0122\7\61\2\2\u0122\u0123\7,\2\2\u0123\u0127"+
		"\3\2\2\2\u0124\u0126\13\2\2\2\u0125\u0124\3\2\2\2\u0126\u0129\3\2\2\2"+
		"\u0127\u0128\3\2\2\2\u0127\u0125\3\2\2\2\u0128\u012a\3\2\2\2\u0129\u0127"+
		"\3\2\2\2\u012a\u012b\7,\2\2\u012b\u012c\7\61\2\2\u012c\u012d\3\2\2\2\u012d"+
		"\u012e\b\33\3\2\u012e\66\3\2\2\2\u012f\u0130\7\61\2\2\u0130\u0131\7\61"+
		"\2\2\u0131\u0135\3\2\2\2\u0132\u0134\n\7\2\2\u0133\u0132\3\2\2\2\u0134"+
		"\u0137\3\2\2\2\u0135\u0133\3\2\2\2\u0135\u0136\3\2\2\2\u0136\u0138\3\2"+
		"\2\2\u0137\u0135\3\2\2\2\u0138\u0139\b\34\3\2\u01398\3\2\2\2\u013a\u013c"+
		"\t\b\2\2\u013b\u013a\3\2\2\2\u013c\u013d\3\2\2\2\u013d\u013b\3\2\2\2\u013d"+
		"\u013e\3\2\2\2\u013e:\3\2\2\2\u013f\u0140\13\2\2\2\u0140<\3\2\2\2\u0141"+
		"\u0142\t\t\2\2\u0142>\3\2\2\2\u0143\u0144\t\n\2\2\u0144@\3\2\2\2\u0145"+
		"\u0146\t\13\2\2\u0146B\3\2\2\2\u0147\u0148\t\f\2\2\u0148D\3\2\2\2\u0149"+
		"\u014a\t\r\2\2\u014aF\3\2\2\2\u014b\u014c\t\16\2\2\u014cH\3\2\2\2\u014d"+
		"\u014e\t\17\2\2\u014eJ\3\2\2\2\u014f\u0150\t\20\2\2\u0150L\3\2\2\2\u0151"+
		"\u0152\t\21\2\2\u0152N\3\2\2\2\u0153\u0154\t\22\2\2\u0154P\3\2\2\2\u0155"+
		"\u0156\t\23\2\2\u0156R\3\2\2\2\u0157\u0158\t\24\2\2\u0158T\3\2\2\2\u0159"+
		"\u015a\t\25\2\2\u015aV\3\2\2\2\u015b\u015c\t\26\2\2\u015cX\3\2\2\2\u015d"+
		"\u015e\t\27\2\2\u015eZ\3\2\2\2\u015f\u0160\t\30\2\2\u0160\\\3\2\2\2\u0161"+
		"\u0162\t\31\2\2\u0162^\3\2\2\2\u0163\u0164\t\32\2\2\u0164`\3\2\2\2\u0165"+
		"\u0166\t\33\2\2\u0166b\3\2\2\2\u0167\u0168\t\34\2\2\u0168d\3\2\2\2\u0169"+
		"\u016a\t\35\2\2\u016af\3\2\2\2\u016b\u016c\t\36\2\2\u016ch\3\2\2\2\u016d"+
		"\u016e\t\37\2\2\u016ej\3\2\2\2\u016f\u0170\t \2\2\u0170l\3\2\2\2\u0171"+
		"\u0172\t!\2\2\u0172n\3\2\2\2\u0173\u0174\t\"\2\2\u0174p\3\2\2\2\u0175"+
		"\u0176\t#\2\2\u0176r\3\2\2\2\36\2\177\u0085\u008c\u0093\u009a\u009c\u00a9"+
		"\u00b1\u00b8\u00dd\u00e3\u00e9\u00ef\u00f2\u00f6\u00fb\u00fd\u0103\u0107"+
		"\u010c\u010e\u0110\u0116\u0118\u0127\u0135\u013d\4\2\3\2\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}