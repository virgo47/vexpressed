// Generated from C:/work/workspace/vexpressed/src/main/java/vexpressed/grammar\Expr.g4 by ANTLR 4.5.1
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
		STRING_LITERAL=24, SPACES=25, COMMENT=26, LINE_COMMENT=27, UNEXPECTED_CHAR=28;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "OP_LT", "OP_GT", "OP_LE", "OP_GE", "OP_EQ", 
		"OP_NE", "OP_AND", "OP_OR", "OP_ADD", "OP_SUB", "OP_MUL", "OP_DIV", "OP_MOD", 
		"K_IS", "K_NOT", "K_NULL", "BOOLEAN_LITERAL", "ID", "NUMERIC_LITERAL", 
		"STRING_LITERAL", "SPACES", "COMMENT", "LINE_COMMENT", "UNEXPECTED_CHAR", 
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
		"STRING_LITERAL", "SPACES", "COMMENT", "LINE_COMMENT", "UNEXPECTED_CHAR"
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\36\u0170\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64"+
		"\t\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\3\2\3\2\3\3\3\3\3\4\3\4\3\5"+
		"\3\5\3\6\3\6\3\6\3\6\5\6~\n\6\3\7\3\7\3\7\3\7\5\7\u0084\n\7\3\b\3\b\3"+
		"\b\3\b\3\b\5\b\u008b\n\b\3\t\3\t\3\t\3\t\3\t\5\t\u0092\n\t\3\n\3\n\3\n"+
		"\3\n\3\n\5\n\u0099\n\n\5\n\u009b\n\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\3\13\5\13\u00a8\n\13\3\f\3\f\3\f\3\f\3\f\3\f\5\f\u00b0"+
		"\n\f\3\r\3\r\3\r\3\r\3\r\5\r\u00b7\n\r\3\16\3\16\3\17\3\17\3\20\3\20\3"+
		"\21\3\21\3\22\3\22\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3"+
		"\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3"+
		"\26\5\26\u00dc\n\26\3\27\3\27\7\27\u00e0\n\27\f\27\16\27\u00e3\13\27\3"+
		"\30\6\30\u00e6\n\30\r\30\16\30\u00e7\3\30\3\30\7\30\u00ec\n\30\f\30\16"+
		"\30\u00ef\13\30\5\30\u00f1\n\30\3\30\3\30\5\30\u00f5\n\30\3\30\6\30\u00f8"+
		"\n\30\r\30\16\30\u00f9\5\30\u00fc\n\30\3\30\3\30\6\30\u0100\n\30\r\30"+
		"\16\30\u0101\3\30\3\30\5\30\u0106\n\30\3\30\6\30\u0109\n\30\r\30\16\30"+
		"\u010a\5\30\u010d\n\30\5\30\u010f\n\30\3\31\3\31\3\31\3\31\7\31\u0115"+
		"\n\31\f\31\16\31\u0118\13\31\3\31\3\31\3\32\3\32\3\32\3\32\3\33\3\33\3"+
		"\33\3\33\7\33\u0124\n\33\f\33\16\33\u0127\13\33\3\33\3\33\3\33\3\33\3"+
		"\33\3\34\3\34\3\34\3\34\7\34\u0132\n\34\f\34\16\34\u0135\13\34\3\34\3"+
		"\34\3\35\3\35\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3\"\3\"\3#\3#\3$\3$\3%\3"+
		"%\3&\3&\3\'\3\'\3(\3(\3)\3)\3*\3*\3+\3+\3,\3,\3-\3-\3.\3.\3/\3/\3\60\3"+
		"\60\3\61\3\61\3\62\3\62\3\63\3\63\3\64\3\64\3\65\3\65\3\66\3\66\3\67\3"+
		"\67\38\38\3\u0125\29\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27"+
		"\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33"+
		"\65\34\67\359\36;\2=\2?\2A\2C\2E\2G\2I\2K\2M\2O\2Q\2S\2U\2W\2Y\2[\2]\2"+
		"_\2a\2c\2e\2g\2i\2k\2m\2o\2\3\2#\6\2&&C\\aac|\b\2&&\60\60\62;C\\aac|\4"+
		"\2--//\3\2))\5\2\13\r\17\17\"\"\4\2\f\f\17\17\3\2\62;\4\2CCcc\4\2DDdd"+
		"\4\2EEee\4\2FFff\4\2GGgg\4\2HHhh\4\2IIii\4\2JJjj\4\2KKkk\4\2LLll\4\2M"+
		"Mmm\4\2NNnn\4\2OOoo\4\2PPpp\4\2QQqq\4\2RRrr\4\2SSss\4\2TTtt\4\2UUuu\4"+
		"\2VVvv\4\2WWww\4\2XXxx\4\2YYyy\4\2ZZzz\4\2[[{{\4\2\\\\||\u0172\2\3\3\2"+
		"\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17"+
		"\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2"+
		"\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3"+
		"\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3"+
		"\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\3q\3\2\2\2\5"+
		"s\3\2\2\2\7u\3\2\2\2\tw\3\2\2\2\13}\3\2\2\2\r\u0083\3\2\2\2\17\u008a\3"+
		"\2\2\2\21\u0091\3\2\2\2\23\u009a\3\2\2\2\25\u00a7\3\2\2\2\27\u00af\3\2"+
		"\2\2\31\u00b6\3\2\2\2\33\u00b8\3\2\2\2\35\u00ba\3\2\2\2\37\u00bc\3\2\2"+
		"\2!\u00be\3\2\2\2#\u00c0\3\2\2\2%\u00c2\3\2\2\2\'\u00c5\3\2\2\2)\u00c9"+
		"\3\2\2\2+\u00db\3\2\2\2-\u00dd\3\2\2\2/\u010e\3\2\2\2\61\u0110\3\2\2\2"+
		"\63\u011b\3\2\2\2\65\u011f\3\2\2\2\67\u012d\3\2\2\29\u0138\3\2\2\2;\u013a"+
		"\3\2\2\2=\u013c\3\2\2\2?\u013e\3\2\2\2A\u0140\3\2\2\2C\u0142\3\2\2\2E"+
		"\u0144\3\2\2\2G\u0146\3\2\2\2I\u0148\3\2\2\2K\u014a\3\2\2\2M\u014c\3\2"+
		"\2\2O\u014e\3\2\2\2Q\u0150\3\2\2\2S\u0152\3\2\2\2U\u0154\3\2\2\2W\u0156"+
		"\3\2\2\2Y\u0158\3\2\2\2[\u015a\3\2\2\2]\u015c\3\2\2\2_\u015e\3\2\2\2a"+
		"\u0160\3\2\2\2c\u0162\3\2\2\2e\u0164\3\2\2\2g\u0166\3\2\2\2i\u0168\3\2"+
		"\2\2k\u016a\3\2\2\2m\u016c\3\2\2\2o\u016e\3\2\2\2qr\7*\2\2r\4\3\2\2\2"+
		"st\7+\2\2t\6\3\2\2\2uv\7.\2\2v\b\3\2\2\2wx\7<\2\2x\n\3\2\2\2yz\5S*\2z"+
		"{\5c\62\2{~\3\2\2\2|~\7>\2\2}y\3\2\2\2}|\3\2\2\2~\f\3\2\2\2\177\u0080"+
		"\5I%\2\u0080\u0081\5c\62\2\u0081\u0084\3\2\2\2\u0082\u0084\7@\2\2\u0083"+
		"\177\3\2\2\2\u0083\u0082\3\2\2\2\u0084\16\3\2\2\2\u0085\u0086\5S*\2\u0086"+
		"\u0087\5E#\2\u0087\u008b\3\2\2\2\u0088\u0089\7>\2\2\u0089\u008b\7?\2\2"+
		"\u008a\u0085\3\2\2\2\u008a\u0088\3\2\2\2\u008b\20\3\2\2\2\u008c\u008d"+
		"\5I%\2\u008d\u008e\5E#\2\u008e\u0092\3\2\2\2\u008f\u0090\7@\2\2\u0090"+
		"\u0092\7?\2\2\u0091\u008c\3\2\2\2\u0091\u008f\3\2\2\2\u0092\22\3\2\2\2"+
		"\u0093\u0094\5E#\2\u0094\u0095\5]/\2\u0095\u009b\3\2\2\2\u0096\u0098\7"+
		"?\2\2\u0097\u0099\7?\2\2\u0098\u0097\3\2\2\2\u0098\u0099\3\2\2\2\u0099"+
		"\u009b\3\2\2\2\u009a\u0093\3\2\2\2\u009a\u0096\3\2\2\2\u009b\24\3\2\2"+
		"\2\u009c\u009d\5W,\2\u009d\u009e\5E#\2\u009e\u00a8\3\2\2\2\u009f\u00a0"+
		"\5W,\2\u00a0\u00a1\5E#\2\u00a1\u00a2\5]/\2\u00a2\u00a8\3\2\2\2\u00a3\u00a4"+
		"\7#\2\2\u00a4\u00a8\7?\2\2\u00a5\u00a6\7>\2\2\u00a6\u00a8\7@\2\2\u00a7"+
		"\u009c\3\2\2\2\u00a7\u009f\3\2\2\2\u00a7\u00a3\3\2\2\2\u00a7\u00a5\3\2"+
		"\2\2\u00a8\26\3\2\2\2\u00a9\u00aa\5=\37\2\u00aa\u00ab\5W,\2\u00ab\u00ac"+
		"\5C\"\2\u00ac\u00b0\3\2\2\2\u00ad\u00ae\7(\2\2\u00ae\u00b0\7(\2\2\u00af"+
		"\u00a9\3\2\2\2\u00af\u00ad\3\2\2\2\u00b0\30\3\2\2\2\u00b1\u00b2\5Y-\2"+
		"\u00b2\u00b3\5_\60\2\u00b3\u00b7\3\2\2\2\u00b4\u00b5\7~\2\2\u00b5\u00b7"+
		"\7~\2\2\u00b6\u00b1\3\2\2\2\u00b6\u00b4\3\2\2\2\u00b7\32\3\2\2\2\u00b8"+
		"\u00b9\7-\2\2\u00b9\34\3\2\2\2\u00ba\u00bb\7/\2\2\u00bb\36\3\2\2\2\u00bc"+
		"\u00bd\7,\2\2\u00bd \3\2\2\2\u00be\u00bf\7\61\2\2\u00bf\"\3\2\2\2\u00c0"+
		"\u00c1\7\'\2\2\u00c1$\3\2\2\2\u00c2\u00c3\5M\'\2\u00c3\u00c4\5a\61\2\u00c4"+
		"&\3\2\2\2\u00c5\u00c6\5W,\2\u00c6\u00c7\5Y-\2\u00c7\u00c8\5c\62\2\u00c8"+
		"(\3\2\2\2\u00c9\u00ca\5W,\2\u00ca\u00cb\5e\63\2\u00cb\u00cc\5S*\2\u00cc"+
		"\u00cd\5S*\2\u00cd*\3\2\2\2\u00ce\u00cf\5c\62\2\u00cf\u00d0\5_\60\2\u00d0"+
		"\u00d1\5e\63\2\u00d1\u00d2\5E#\2\u00d2\u00dc\3\2\2\2\u00d3\u00dc\5c\62"+
		"\2\u00d4\u00d5\5G$\2\u00d5\u00d6\5=\37\2\u00d6\u00d7\5S*\2\u00d7\u00d8"+
		"\5a\61\2\u00d8\u00d9\5E#\2\u00d9\u00dc\3\2\2\2\u00da\u00dc\5G$\2\u00db"+
		"\u00ce\3\2\2\2\u00db\u00d3\3\2\2\2\u00db\u00d4\3\2\2\2\u00db\u00da\3\2"+
		"\2\2\u00dc,\3\2\2\2\u00dd\u00e1\t\2\2\2\u00de\u00e0\t\3\2\2\u00df\u00de"+
		"\3\2\2\2\u00e0\u00e3\3\2\2\2\u00e1\u00df\3\2\2\2\u00e1\u00e2\3\2\2\2\u00e2"+
		".\3\2\2\2\u00e3\u00e1\3\2\2\2\u00e4\u00e6\5;\36\2\u00e5\u00e4\3\2\2\2"+
		"\u00e6\u00e7\3\2\2\2\u00e7\u00e5\3\2\2\2\u00e7\u00e8\3\2\2\2\u00e8\u00f0"+
		"\3\2\2\2\u00e9\u00ed\7\60\2\2\u00ea\u00ec\5;\36\2\u00eb\u00ea\3\2\2\2"+
		"\u00ec\u00ef\3\2\2\2\u00ed\u00eb\3\2\2\2\u00ed\u00ee\3\2\2\2\u00ee\u00f1"+
		"\3\2\2\2\u00ef\u00ed\3\2\2\2\u00f0\u00e9\3\2\2\2\u00f0\u00f1\3\2\2\2\u00f1"+
		"\u00fb\3\2\2\2\u00f2\u00f4\5E#\2\u00f3\u00f5\t\4\2\2\u00f4\u00f3\3\2\2"+
		"\2\u00f4\u00f5\3\2\2\2\u00f5\u00f7\3\2\2\2\u00f6\u00f8\5;\36\2\u00f7\u00f6"+
		"\3\2\2\2\u00f8\u00f9\3\2\2\2\u00f9\u00f7\3\2\2\2\u00f9\u00fa\3\2\2\2\u00fa"+
		"\u00fc\3\2\2\2\u00fb\u00f2\3\2\2\2\u00fb\u00fc\3\2\2\2\u00fc\u010f\3\2"+
		"\2\2\u00fd\u00ff\7\60\2\2\u00fe\u0100\5;\36\2\u00ff\u00fe\3\2\2\2\u0100"+
		"\u0101\3\2\2\2\u0101\u00ff\3\2\2\2\u0101\u0102\3\2\2\2\u0102\u010c\3\2"+
		"\2\2\u0103\u0105\5E#\2\u0104\u0106\t\4\2\2\u0105\u0104\3\2\2\2\u0105\u0106"+
		"\3\2\2\2\u0106\u0108\3\2\2\2\u0107\u0109\5;\36\2\u0108\u0107\3\2\2\2\u0109"+
		"\u010a\3\2\2\2\u010a\u0108\3\2\2\2\u010a\u010b\3\2\2\2\u010b\u010d\3\2"+
		"\2\2\u010c\u0103\3\2\2\2\u010c\u010d\3\2\2\2\u010d\u010f\3\2\2\2\u010e"+
		"\u00e5\3\2\2\2\u010e\u00fd\3\2\2\2\u010f\60\3\2\2\2\u0110\u0116\7)\2\2"+
		"\u0111\u0115\n\5\2\2\u0112\u0113\7)\2\2\u0113\u0115\7)\2\2\u0114\u0111"+
		"\3\2\2\2\u0114\u0112\3\2\2\2\u0115\u0118\3\2\2\2\u0116\u0114\3\2\2\2\u0116"+
		"\u0117\3\2\2\2\u0117\u0119\3\2\2\2\u0118\u0116\3\2\2\2\u0119\u011a\7)"+
		"\2\2\u011a\62\3\2\2\2\u011b\u011c\t\6\2\2\u011c\u011d\3\2\2\2\u011d\u011e"+
		"\b\32\2\2\u011e\64\3\2\2\2\u011f\u0120\7\61\2\2\u0120\u0121\7,\2\2\u0121"+
		"\u0125\3\2\2\2\u0122\u0124\13\2\2\2\u0123\u0122\3\2\2\2\u0124\u0127\3"+
		"\2\2\2\u0125\u0126\3\2\2\2\u0125\u0123\3\2\2\2\u0126\u0128\3\2\2\2\u0127"+
		"\u0125\3\2\2\2\u0128\u0129\7,\2\2\u0129\u012a\7\61\2\2\u012a\u012b\3\2"+
		"\2\2\u012b\u012c\b\33\3\2\u012c\66\3\2\2\2\u012d\u012e\7\61\2\2\u012e"+
		"\u012f\7\61\2\2\u012f\u0133\3\2\2\2\u0130\u0132\n\7\2\2\u0131\u0130\3"+
		"\2\2\2\u0132\u0135\3\2\2\2\u0133\u0131\3\2\2\2\u0133\u0134\3\2\2\2\u0134"+
		"\u0136\3\2\2\2\u0135\u0133\3\2\2\2\u0136\u0137\b\34\3\2\u01378\3\2\2\2"+
		"\u0138\u0139\13\2\2\2\u0139:\3\2\2\2\u013a\u013b\t\b\2\2\u013b<\3\2\2"+
		"\2\u013c\u013d\t\t\2\2\u013d>\3\2\2\2\u013e\u013f\t\n\2\2\u013f@\3\2\2"+
		"\2\u0140\u0141\t\13\2\2\u0141B\3\2\2\2\u0142\u0143\t\f\2\2\u0143D\3\2"+
		"\2\2\u0144\u0145\t\r\2\2\u0145F\3\2\2\2\u0146\u0147\t\16\2\2\u0147H\3"+
		"\2\2\2\u0148\u0149\t\17\2\2\u0149J\3\2\2\2\u014a\u014b\t\20\2\2\u014b"+
		"L\3\2\2\2\u014c\u014d\t\21\2\2\u014dN\3\2\2\2\u014e\u014f\t\22\2\2\u014f"+
		"P\3\2\2\2\u0150\u0151\t\23\2\2\u0151R\3\2\2\2\u0152\u0153\t\24\2\2\u0153"+
		"T\3\2\2\2\u0154\u0155\t\25\2\2\u0155V\3\2\2\2\u0156\u0157\t\26\2\2\u0157"+
		"X\3\2\2\2\u0158\u0159\t\27\2\2\u0159Z\3\2\2\2\u015a\u015b\t\30\2\2\u015b"+
		"\\\3\2\2\2\u015c\u015d\t\31\2\2\u015d^\3\2\2\2\u015e\u015f\t\32\2\2\u015f"+
		"`\3\2\2\2\u0160\u0161\t\33\2\2\u0161b\3\2\2\2\u0162\u0163\t\34\2\2\u0163"+
		"d\3\2\2\2\u0164\u0165\t\35\2\2\u0165f\3\2\2\2\u0166\u0167\t\36\2\2\u0167"+
		"h\3\2\2\2\u0168\u0169\t\37\2\2\u0169j\3\2\2\2\u016a\u016b\t \2\2\u016b"+
		"l\3\2\2\2\u016c\u016d\t!\2\2\u016dn\3\2\2\2\u016e\u016f\t\"\2\2\u016f"+
		"p\3\2\2\2\35\2}\u0083\u008a\u0091\u0098\u009a\u00a7\u00af\u00b6\u00db"+
		"\u00e1\u00e7\u00ed\u00f0\u00f4\u00f9\u00fb\u0101\u0105\u010a\u010c\u010e"+
		"\u0114\u0116\u0125\u0133\4\2\3\2\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}