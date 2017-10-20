package com.virgo47.vexpressed;

import com.virgo47.vexpressed.core.ExpressionCalculatorVisitor;
import com.virgo47.vexpressed.core.ExpressionException;
import com.virgo47.vexpressed.core.FunctionExecutor;
import com.virgo47.vexpressed.core.VariableResolver;
import com.virgo47.vexpressed.grammar.ExprLexer;
import com.virgo47.vexpressed.grammar.ExprParser;
import com.virgo47.vexpressed.meta.ExpressionType;
import com.virgo47.vexpressed.validation.ExpressionValidatorVisitor;
import com.virgo47.vexpressed.validation.FunctionTypeResolver;
import com.virgo47.vexpressed.validation.VariableTypeResolver;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public final class VexpressedUtils {

	private static final ANTLRErrorListener ERROR_LISTENER = new ExceptionThrowingErrorListener();

	/**
	 * Evaluates the expression using provided {@link VariableResolver} and
	 * {@link FunctionExecutor}. This always parses the expression so it is not
	 * "production-ready" because it is better to cache the parse trees for the
	 * same expression.
	 *
	 * @param <RT> result type
	 */
	@SuppressWarnings("unchecked")
	public static <RT> RT eval(
		String expression, VariableResolver variableResolver, FunctionExecutor functionExecutor)
	{
		ParseTree parseTree = createParseTree(expression);
		ParseTreeVisitor visitor = new ExpressionCalculatorVisitor()
			.withVariableResolver(variableResolver)
			.withFunctionExecutor(functionExecutor);
		return (RT) visitor.visit(parseTree);
	}

	/**
	 * Parses expression and returns the {@link ParseTree} for further usage. This is
	 * slow operation compared to evaluation itself, so it is recommended to cache parse
	 * trees for repeated evaluations of the same expression.
	 */
	public static ParseTree createParseTree(String expression) {
		ExprLexer lexer = new ExprLexer(CharStreams.fromString(expression));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		ExprParser parser = new ExprParser(tokens);
		parser.removeErrorListeners();
		parser.addErrorListener(ERROR_LISTENER);
		return parser.result();
	}

	/**
	 * Checks validity of this expression and returns its {@link ExpressionType} when valid,
	 * otherwise throws exception. Does not cache parse tree, but for validation this may
	 * actually be more appropriate behavior as it is expected that many variations of the
	 * expression will be checked.
	 */
	public static ExpressionType check(String expression,
		VariableTypeResolver variableTypeResolver, FunctionTypeResolver functionTypeResolver)
	{
		ParseTree parseTree = createParseTree(expression);
		ExpressionValidatorVisitor visitor = new ExpressionValidatorVisitor(variableTypeResolver)
			.withFunctionTypeResolver(functionTypeResolver);
		return visitor.visit(parseTree);
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
