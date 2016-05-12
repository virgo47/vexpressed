package vexpressed;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import vexpressed.grammar.ExprLexer;
import vexpressed.grammar.ExprParser;

public final class ExpressionUtils {

	private static final ANTLRErrorListener ERROR_LISTENER = new ExceptionThrowingErrorListener();

	public static ParseTree createParseTree(String expression) {
		ANTLRInputStream input = new ANTLRInputStream(expression);
		ExprLexer lexer = new ExprLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		ExprParser parser = new ExprParser(tokens);
		parser.removeErrorListeners();
		parser.addErrorListener(ERROR_LISTENER);
		return parser.result();
	}

	private static class ExceptionThrowingErrorListener extends BaseErrorListener {
		@Override
		public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
			int line, int col, String msg, RecognitionException e)
		{
			throw new ExpressionException(
				"Expression parse failed at " + line + ':' + col + " - " + msg +
					underlinedError(recognizer, (Token) offendingSymbol, line, col));
		}

		private String underlinedError(
			Recognizer recognizer, Token offendingToken, int line, int col)
		{
			StringBuilder sb = new StringBuilder();
			CommonTokenStream tokens =
				(CommonTokenStream) recognizer.getInputStream();
			String input = tokens.getTokenSource().getInputStream().toString();
			String[] lines = input.split("\n");
			String errorLine = lines[line - 1];
			sb.append('\n').append(errorLine);

			int start = offendingToken.getStartIndex();
			int stop = offendingToken.getStopIndex();
			if (start > stop) {
				return sb.toString();
			}

			sb.append('\n');
			for (int i = 0; i < col; i++) sb.append(' ');
			if (start >= 0 && stop >= 0) {
				for (int i = start; i <= stop; i++) sb.append('^');
			}
			return sb.toString();
		}
	}
}
