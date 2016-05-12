package vexpressed;

import static vexpressed.ExpressionType.BOOLEAN;
import static vexpressed.ExpressionType.DATE;
import static vexpressed.ExpressionType.DATE_TIME;
import static vexpressed.ExpressionType.DECIMAL;
import static vexpressed.ExpressionType.INTEGER;
import static vexpressed.ExpressionType.STRING;
import static vexpressed.ExpressionType.TIMESTAMP;
import static vexpressed.grammar.ExprParser.ArithmeticOpContext;
import static vexpressed.grammar.ExprParser.BooleanLiteralContext;
import static vexpressed.grammar.ExprParser.ComparisonOpContext;
import static vexpressed.grammar.ExprParser.IsNullContext;
import static vexpressed.grammar.ExprParser.LogicOpContext;
import static vexpressed.grammar.ExprParser.NullLiteralContext;
import static vexpressed.grammar.ExprParser.NumericLiteralContext;
import static vexpressed.grammar.ExprParser.OP_ADD;
import static vexpressed.grammar.ExprParser.OP_SUB;
import static vexpressed.grammar.ExprParser.ParensContext;
import static vexpressed.grammar.ExprParser.StringLiteralContext;
import static vexpressed.grammar.ExprParser.VariableContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import vexpressed.func.ExpressionFunctionTypeResolver;
import vexpressed.func.FunctionParameterDefinition;
import vexpressed.grammar.ExprBaseVisitor;
import vexpressed.grammar.ExprParser;
import vexpressed.vars.ExpressionVariableTypeResolver;

/** Validates the expression - resolver for variables is mandatory, for functions optional. */
public class ExpressionValidatorVisitor extends ExprBaseVisitor<ExpressionType> {

	private final ExpressionVariableTypeResolver variableTypeResolver;
	private ExpressionFunctionTypeResolver functionTypeResolver;

	public ExpressionValidatorVisitor(ExpressionVariableTypeResolver variableTypeResolver) {
		if (variableTypeResolver == null) {
			throw new IllegalArgumentException("Variable type resolver must be provided");
		}
		this.variableTypeResolver = variableTypeResolver;
	}

	public ExpressionValidatorVisitor withFunctionTypeResolver(
		ExpressionFunctionTypeResolver functionTypeResolver)
	{
		this.functionTypeResolver = functionTypeResolver;
		return this;
	}

	@Override
	public ExpressionType visitLogicOp(LogicOpContext ctx) {
		ExpressionType left = visitNotNull(ctx.expr(0));
		ExpressionType right = visitNotNull(ctx.expr(1));
		if (left != BOOLEAN || right != BOOLEAN) {
			throw new ExpressionException(
				"Both sides must be of type BOOLEAN for logic operation. Actual types: " +
					left + ", " + right);
		}
		return BOOLEAN;
	}

	@Override
	public ExpressionType visitArithmeticOp(ArithmeticOpContext ctx) {
		ExpressionType left = visitNotNull(ctx.expr(0));
		ExpressionType right = visitNotNull(ctx.expr(1));

		int operator = ctx.op.getType();
		if (left == DATE || left == DATE_TIME || left == TIMESTAMP) {
			if ((operator == OP_ADD || operator == OP_SUB)
				&& (right == STRING || right == INTEGER))
			{
				return left;
			} else {
				throw new ExpressionException("Arithmetic operation " + ctx.op.getText() +
					" not supported on temporal type " + left + " and " + right);
			}
		}

		if (left == STRING && operator == OP_ADD) {
			return left;
		}
		ExpressionType commonNumberType = commonNumberType(left, right);
		if (commonNumberType != null) {
			return commonNumberType;
		}

		throw new ExpressionException("Arithmetic operation " + ctx.op.getText() +
			" not supported for types " + left + " and " + right);
	}

	/**
	 * Returns common number type promoting from {@link ExpressionType#INTEGER} to
	 * {@link ExpressionType#DECIMAL} as needed. Returns null if both sides are not number.
	 */
	private ExpressionType commonNumberType(ExpressionType left, ExpressionType right) {
		if (left == INTEGER && right == INTEGER) {
			return INTEGER;
		}
		if (left == DECIMAL && right == DECIMAL
			|| left == INTEGER && right == DECIMAL
			|| left == DECIMAL && right == INTEGER)
		{
			return DECIMAL;
		}

		return null;
	}

	@Override
	public ExpressionType visitComparisonOp(ComparisonOpContext ctx) {
		ExpressionType left = visit(ctx.expr(0));
		ExpressionType right = visit(ctx.expr(1));

		if ((left == DECIMAL || left == INTEGER) && (right == DECIMAL || right == INTEGER)) {
			return BOOLEAN;
		}

		// Does not cover all the corner cases - this would require more than ExpressionType,
		// we need actual Java type too to find out whether it's comparable.
		if (left == null || right == null || left == right) {
			return BOOLEAN;
		}

		throw new ExpressionException(
			"Invalid comparison/relation operation between type " + left + " and " + right);
	}

	@Override
	public ExpressionType visitVariable(VariableContext ctx) {
		return variableTypeResolver.resolve(ctx.ID().getText());
	}

	@Override
	public ExpressionType visitStringLiteral(StringLiteralContext ctx) {
		return STRING;
	}

	@Override
	public ExpressionType visitBooleanLiteral(BooleanLiteralContext ctx) {
		return BOOLEAN;
	}

	@Override
	public ExpressionType visitNumericLiteral(NumericLiteralContext ctx) {
		try {
			//noinspection ResultOfMethodCallIgnored
			Integer.parseInt(ctx.NUMERIC_LITERAL().getText());
			return INTEGER;
		} catch (NumberFormatException e) {
			// here we exploit that whatever is parsed as numeric can fit into BigDecimal
			return DECIMAL;
		}
	}

	@Override
	public ExpressionType visitNullLiteral(NullLiteralContext ctx) {
		return null;
	}

	@Override
	public ExpressionType visitUnarySign(ExprParser.UnarySignContext ctx) {
		ExpressionType type = visitNotNull(ctx.expr());
		if (type != INTEGER && type != DECIMAL) {
			throw new ExpressionException(
				"Unary sign can be applied only to numbers, not to " + type);
		}
		return type;
	}

	@Override
	public ExpressionType visitParens(ParensContext ctx) {
		return visit(ctx.expr());
	}

	@Override
	public ExpressionType visitFunction(ExprParser.FunctionContext ctx) {
		String functionName = ctx.ID().getText();
		List<FunctionParameterDefinition> args = ctx.paramlist() != null
			? ctx.paramlist().funarg().stream()
			.map(funArgCtx -> new FunctionParameterDefinition(
				funArgCtx.ID() != null ? funArgCtx.ID().getText() : null,
				visitFunarg(funArgCtx)))
			.collect(Collectors.toList())
			: Collections.emptyList();

		return resolveFunctionType(functionName, args);
	}

	@Override
	public ExpressionType visitFunarg(ExprParser.FunargContext ctx) {
		return visit(ctx.expr());
	}

	@Override
	public ExpressionType visitInfixFunction(ExprParser.InfixFunctionContext ctx) {
		String functionName = ctx.ID().getText();
		List<FunctionParameterDefinition> args = new ArrayList<>();
		args.add(new FunctionParameterDefinition(null, visit(ctx.expr(0))));
		args.add(new FunctionParameterDefinition(null, visit(ctx.expr(1))));

		return resolveFunctionType(functionName, args);
	}

	private ExpressionType resolveFunctionType(
		String functionName, List<FunctionParameterDefinition> argDefs)
	{
		if (functionTypeResolver == null) {
			throw new ExpressionException("Cannot validate function " +
				functionName + " because no function executor was set.");
		}
		return functionTypeResolver.resolveType(functionName, argDefs);
	}

	@Override
	public ExpressionType visitIsNull(IsNullContext ctx) {
		return BOOLEAN;
	}

	@Override
	public ExpressionType visitResult(ExprParser.ResultContext ctx) {
		return visit(ctx.expr());
	}

	private ExpressionType visitNotNull(ExprParser.ExprContext expr) {
		ExpressionType result = visit(expr);
		if (result == null) {
			throw new ExpressionException(
				"Null value not allowed here: " + expr.toStringTree());
		}

		return result;
	}
}
