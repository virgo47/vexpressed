package com.virgo47.vexpressed.core;

import static java.util.stream.Collectors.toCollection;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.virgo47.vexpressed.grammar.ExprBaseVisitor;
import com.virgo47.vexpressed.grammar.ExprParser;
import com.virgo47.vexpressed.meta.ExpressionType;

/**
 * Evaluates the expression. It is virtually useless without {@link #variableResolver} set -
 * use {@link #withVariableResolver(VariableResolver)} to do so. Similarly {@link
 * #functionExecutor} can be set with {@link #withFunctionExecutor(FunctionExecutor)} when needed.
 * <p>
 * It is not thread-safe as it likely uses {@link VariableResolver} specific for one-off evaluation
 */
public class ExpressionCalculatorVisitor extends ExprBaseVisitor {

	public static final int DEFAULT_MAX_SCALE = 15;
	public static final int DEFAULT_MAX_RESULT_SCALE = 6;

	/** Minimal scale for {@link ExpressionType#DECIMAL}, to make it obvious. */
	public static final int DEFAULT_MIN_RESULT_SCALE = 1;

	private int maxScale = DEFAULT_MAX_SCALE;
	private int maxResultScale = DEFAULT_MAX_RESULT_SCALE;
	private int roundingMode = BigDecimal.ROUND_HALF_UP;

	private VariableResolver variableResolver = VariableResolver.NULL_VARIABLE_RESOLVER;
	private FunctionExecutor functionExecutor = FunctionExecutor.NULL_FUNCTION_EXECUTOR;

	public ExpressionCalculatorVisitor withVariableResolver(VariableResolver variableResolver) {
		this.variableResolver = variableResolver;
		return this;
	}

	public ExpressionCalculatorVisitor withFunctionExecutor(FunctionExecutor functionExecutor) {
		this.functionExecutor = functionExecutor;
		return this;
	}

	/** Maximum BigDecimal scale used during computations. */
	public ExpressionCalculatorVisitor withMaxScaleForIntermediateResults(int maxScale) {
		this.maxScale = maxScale;
		return this;
	}

	/** Maximum BigDecimal scale for result. */
	public ExpressionCalculatorVisitor withMaxResultScale(int maxResultScale) {
		this.maxResultScale = maxResultScale;
		return this;
	}

	@Override
	public Object visitLogicNot(ExprParser.LogicNotContext ctx) {
		return !(boolean) visitNotNull(ctx.expr());
	}

	@Override
	public Boolean visitLogicOp(ExprParser.LogicOpContext ctx) {
		boolean left = (boolean) visitNotNull(ctx.left);

		switch (ctx.op.getType()) {
			case ExprParser.OP_AND:
				return left && booleanRightSide(ctx);
			case ExprParser.OP_OR:
				return left || booleanRightSide(ctx);
			default:
				throw new ExpressionException("Unknown operator " + ctx.op);
		}
	}

	private boolean booleanRightSide(ExprParser.LogicOpContext ctx) {
		return (boolean) visitNotNull(ctx.right);
	}

	@Override
	public Object visitArithmeticOp(ExprParser.ArithmeticOpContext ctx) {
		Object left = visitNotNull(ctx.left);
		if (left instanceof Temporal) {
			return temporalArithmetic(ctx, (Temporal) left);
		} else if (left instanceof String) {
			return stringArithmetic(ctx, (String) left);
		} else {
			return numberArithmetic(ctx, (Number) left);
		}
	}

	private static final Pattern TEMPORAL_AMOUNT_PATTERN = Pattern.compile("(\\d+)([ymwd])?");

	private Object temporalArithmetic(ExprParser.ArithmeticOpContext ctx, Temporal left) {
		Object right = visitNotNull(ctx.right);
		long amount;
		TemporalUnit unit;

		if (right instanceof Number) {
			amount = ((Number) right).longValue();
			unit = ChronoUnit.DAYS;
		} else {
			Matcher matcher = TEMPORAL_AMOUNT_PATTERN.matcher(((String) right).toLowerCase());
			if (matcher.matches()) {
				amount = Long.parseLong(matcher.group(1));
				unit = parseUnit(matcher.group(2));
			} else {
				throw new ExpressionException("Cannot parse temporal amount: " + right);
			}
		}

		switch (ctx.op.getType()) {
			case ExprParser.OP_ADD:
				return left.plus(amount, unit);
			case ExprParser.OP_SUB:
				return left.minus(amount, unit);
			default:
				throw new ExpressionException(
					"Unknown operator " + ctx.op + ", or not supported for temporal types");
		}
	}

	private Object stringArithmetic(ExprParser.ArithmeticOpContext ctx, String left) {
		Object right = visitNotNull(ctx.right);

		switch (ctx.op.getType()) {
			case ExprParser.OP_ADD:
				return left.concat(right.toString());
			default:
				throw new ExpressionException(
					"Unknown operator " + ctx.op + ", or not supported for temporal types");
		}
	}

	private TemporalUnit parseUnit(String unitGroup) {
		if (unitGroup != null && unitGroup.length() > 0) {
			switch (unitGroup.charAt(0)) {
				case 'y':
					return ChronoUnit.YEARS;
				case 'm':
					return ChronoUnit.MONTHS;
				case 'w':
					return ChronoUnit.WEEKS;
			}
		}
		return ChronoUnit.DAYS;
	}

	private Object numberArithmetic(ExprParser.ArithmeticOpContext ctx, Number left) {
		Number right = (Number) visitNotNull(ctx.right);
		if (left instanceof BigDecimal && right instanceof BigDecimal) {
			return bigDecimalArithmetic(ctx, (BigDecimal) left, (BigDecimal) right);
		} else if (left instanceof BigDecimal) {
			return bigDecimalArithmetic(ctx, (BigDecimal) left, new BigDecimal(right.toString()));
		} else if (right instanceof BigDecimal) {
			return bigDecimalArithmetic(ctx, new BigDecimal(left.toString()), (BigDecimal) right);
		}
		return integerArithmetic(ctx, left.intValue(), right.intValue());
	}

	private Number bigDecimalArithmetic(
		ExprParser.ArithmeticOpContext ctx, BigDecimal left, BigDecimal right)
	{
		switch (ctx.op.getType()) {
			case ExprParser.OP_ADD:
				return left.add(right);
			case ExprParser.OP_SUB:
				return left.subtract(right);
			case ExprParser.OP_MUL:
				return left.multiply(right);
			case ExprParser.OP_DIV:
				return divide(left, right);
			case ExprParser.OP_IDIV:
				return narrowNumberResult(left.divideToIntegralValue(right).longValue());
			case ExprParser.OP_REMAINDER:
				return left.remainder(right);
			case ExprParser.OP_POW:
				return right.scale() > 0
					? BigDecimal.valueOf(Math.pow(left.doubleValue(), right.doubleValue()))
					: left.pow(right.intValue());
			default:
				throw new ExpressionException("Unknown operator " + ctx.op);
		}
	}

	/** Left side is made long to prevent overflows. */
	private Number integerArithmetic(ExprParser.ArithmeticOpContext ctx, long left, int right) {
		switch (ctx.op.getType()) {
			case ExprParser.OP_ADD:
				return narrowNumberResult(left + right);
			case ExprParser.OP_SUB:
				return narrowNumberResult(left - right);
			case ExprParser.OP_MUL:
				return narrowNumberResult(left * right);
			case ExprParser.OP_DIV:
				return narrowNumberResult(divide(new BigDecimal(left), new BigDecimal(right)));
			case ExprParser.OP_IDIV:
				return narrowNumberResult(left / right);
			case ExprParser.OP_REMAINDER:
				return narrowNumberResult(left % right);
			case ExprParser.OP_POW:
				return narrowNumberResult(BigInteger.valueOf(left).pow(right));
			default:
				throw new ExpressionException("Unknown operator " + ctx.op);
		}
	}

	private BigDecimal divide(BigDecimal left, BigDecimal right) {
		return left.divide(right, maxScale, roundingMode).stripTrailingZeros();
	}

	private Number narrowNumberResult(Number result) {
		return (Number) narrowDownNumberTypes(result);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Boolean visitComparisonOp(ExprParser.ComparisonOpContext ctx) {
		Comparable left = (Comparable) visit(ctx.left);
		Comparable right = (Comparable) visit(ctx.right);
		int operator = ctx.op.getType();
		if (left == null || right == null) {
			// TODO do we want to throw when operator is not EQ/NE?
			return left == null && right == null && operator == ExprParser.OP_EQ
				|| (left != null || right != null) && operator == ExprParser.OP_NE;
		}
		// if one side is integer and the other BigDecimal, we want to unify it to BigDecimal
		if (left instanceof BigDecimal && right instanceof Integer) {
			right = new BigDecimal(right.toString());
		}
		if (right instanceof BigDecimal && left instanceof Integer) {
			left = new BigDecimal(left.toString());
		}

		int comp = left.compareTo(right);
		switch (operator) {
			case ExprParser.OP_EQ:
				return comp == 0;
			case ExprParser.OP_NE:
				return comp != 0;
			case ExprParser.OP_GT:
				return comp > 0;
			case ExprParser.OP_LT:
				return comp < 0;
			case ExprParser.OP_GE:
				return comp >= 0;
			case ExprParser.OP_LE:
				return comp <= 0;
			default:
				throw new ExpressionException("Unknown operator " + ctx.op);
		}
	}

	@Override
	public Object visitVariable(ExprParser.VariableContext ctx) {
		String variableName = ctx.ID().getText();
		if (variableResolver == null) {
			throw new ExpressionException(
				"Cannot resolve variable " + variableName + " - no variable resolver provided!");
		}
		Object value = variableResolver.resolve(variableName);
		return narrowDownNumberTypes(value);
	}

	@Override
	public String visitStringLiteral(ExprParser.StringLiteralContext ctx) {
		String text = ctx.STRING_LITERAL().getText();
		text = text.substring(1, text.length() - 1)
			.replaceAll("''", "'");
		return text;
	}

	@Override
	public Boolean visitBooleanLiteral(ExprParser.BooleanLiteralContext ctx) {
		return ctx.BOOLEAN_LITERAL().getText().toLowerCase().charAt(0) == 't';
	}

	@Override
	public Number visitNumericLiteral(ExprParser.NumericLiteralContext ctx) {
		String text = ctx.NUMERIC_LITERAL().getText();
		return stringToNumber(text.replaceAll("_", ""));
	}

	private Number stringToNumber(String text) {
		try {
			if (text.indexOf('.') == -1) {
				return new Integer(text);
			}
		} catch (NumberFormatException e) {
			// ignored, we will just try BigDecimal
		}
		BigDecimal bigDecimal = new BigDecimal(text);

		return bigDecimal.scale() < 0
			? bigDecimal.setScale(0, roundingMode)
			: bigDecimal;
	}

	@Override
	public Object visitNullLiteral(ExprParser.NullLiteralContext ctx) {
		return null;
	}

	@Override
	public Number visitUnarySign(ExprParser.UnarySignContext ctx) {
		Object expr = visitNotNull(ctx.expr());
		if (!(expr instanceof Number)) {
			throw new ExpressionException(
				"Unary sign can be applied only to numbers, not to: " + expr);
		}

		Number result = (Number) expr;
		boolean unaryMinus = ctx.op.getText().equals("-");
		return unaryMinus
			? (result instanceof BigDecimal ? ((BigDecimal) result).negate() : -result.intValue())
			: result;
	}

	@Override
	public Object visitParens(ExprParser.ParensContext ctx) {
		return visit(ctx.expr());
	}

	private final List<FunctionArgument> EMPTY_PARAMS = Collections.emptyList();

	@Override
	public Object visitFunction(ExprParser.FunctionContext ctx) {
		String functionName = ctx.ID().getText();
		List<FunctionArgument> params = ctx.paramlist() != null
			? ctx.paramlist().funarg().stream()
			.map(this::visitFunarg)
			.collect(Collectors.toList())
			: EMPTY_PARAMS;

		return executeFunction(functionName, params);
	}

	@Override
	public FunctionArgument visitFunarg(ExprParser.FunargContext ctx) {
		String argName = ctx.ID() != null ? ctx.ID().getText() : null;
		return new FunctionArgument(argName, visit(ctx.expr()));
	}

	@Override
	public Object visitInfixFunction(ExprParser.InfixFunctionContext ctx) {
		String functionName = ctx.ID().getText();

		return executeFunction(functionName, Arrays.asList(
			new FunctionArgument(visit(ctx.left)),
			new FunctionArgument(visit(ctx.right))));
	}

	@Override
	public Object visitCustomOp(ExprParser.CustomOpContext ctx) {
		String optext = ctx.op.getText();
		System.out.println("\nOPTEXT = " + optext);
		return super.visitCustomOp(ctx);
	}

	private Object executeFunction(String functionName, List<FunctionArgument> params) {
		if (functionExecutor == null) {
			throw new ExpressionException(
				"Cannot execute function " + functionName + " - no function executor provided!");
		}
		Object result = functionExecutor.execute(functionName, params);
		return narrowDownNumberTypes(result);
	}

	private Object narrowDownNumberTypes(Object value) {
		// directly supported types and null
		if (value == null
			|| value instanceof Integer || value instanceof BigDecimal
			|| value instanceof String || value instanceof Boolean)
		{
			return value;
		}

		if (value instanceof Number) {
			return stringToNumber(value.toString());
		}

		return value;
	}

	@Override
	public Object visitListConstructor(ExprParser.ListConstructorContext ctx) {
		return ctx.listExpr() != null
			? ctx.listExpr().expr().stream()
			.map(this::visit)
			.collect(toCollection(ArrayList::new))
			: Collections.emptyList();
	}

	@Override
	public Object visitResult(ExprParser.ResultContext ctx) {
		Object result = visit(ctx.expr());
		if (result instanceof BigDecimal) {
			BigDecimal bdResult = (BigDecimal) result;
			if (bdResult.scale() > maxResultScale) {
				result = bdResult.setScale(maxResultScale, roundingMode);
			} else if (bdResult.scale() == 0) {
				result = bdResult.setScale(DEFAULT_MIN_RESULT_SCALE, roundingMode);
			}
		}
		return result;
	}

	private Object visitNotNull(ExprParser.ExprContext expr) {
		Object result = visit(expr);
		if (result == null) {
			throw new ExpressionException(
				"Null value not allowed here: " + expr.toStringTree());
		}

		return result;
	}
}
