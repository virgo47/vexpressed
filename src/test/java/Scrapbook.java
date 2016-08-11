import static vexpressed.core.VariableResolver.NULL_VARIABLE_RESOLVER;

import vexpressed.VexpressedUtils;
import vexpressed.core.FunctionExecutor;
import vexpressed.core.VariableResolver;
import vexpressed.meta.ExpressionType;
import vexpressed.support.ExpressionFunction;
import vexpressed.support.FunctionMapper;
import vexpressed.support.FunctionParam;
import vexpressed.support.VariableBinding;
import vexpressed.support.VariableMapper;

import java.util.Collection;

public class Scrapbook {

	public static void main(String[] args) {
		// simple, no variables or functions
		System.out.println("5+6 = " + VexpressedUtils.eval("5+6", NULL_VARIABLE_RESOLVER, null));

		eval("2+2^2^3/16", NULL_VARIABLE_RESOLVER, null);
		eval("2 + ((2^(2^3)) / 16)", NULL_VARIABLE_RESOLVER, null);

		// variable
		eval("a * 3", var -> 2, null);

		// static map-like variable mapping with VariableBinding
		eval("a * b", new VariableBinding().add("a", 3).add("b", 6), null);

		// variable mapper for type Rectangle
		VariableMapper<Rectangle> rectangleMapper = new VariableMapper<Rectangle>()
			.define("a", ExpressionType.INTEGER, r -> r.a)
			.define("b", ExpressionType.INTEGER, r -> r.b)
			.finish();
		// and now use the same mapper with the same expression for two different objects
		eval("a * b", rectangleMapper.resolverFor(new Rectangle(3, 4)), null);
		eval("a * b", rectangleMapper.resolverFor(new Rectangle(2, 5)), null);

		// dummy function demo (does not recognize function name and always just sums up the inputs)
		eval("fun(1, 2, 3) + anyotherfun(4, 5)", NULL_VARIABLE_RESOLVER,
			(name, numbers) -> numbers.stream().mapToInt(o -> (Integer) o.value).sum());

		// using FunctionMapper
		FunctionMapper functionMapper = new FunctionMapper()
			.scanForFunctions(Scrapbook.class);
		eval("sum([1, 2, 3, 4])", NULL_VARIABLE_RESOLVER, functionMapper.executor());
		eval("sum(nums: [1, 2, 3, 4])", NULL_VARIABLE_RESOLVER, functionMapper.executor());

		// mixing variables and function with implicit access to variables
		// This time we map non-annotated function, first is name of the function (in expression)
		// then the implementing class/object (delegate), name of the method and then method
		// parameter types. VariableResolver parameter is technical and not counted.
		functionMapper = new FunctionMapper().registerFunction(
			"isLargerThan", Scrapbook.class, "areaLargerThan", VariableResolver.class, int.class);

		VariableResolver varResolver = rectangleMapper.resolverFor(new Rectangle(3, 4));
		// function is used only with one argument, the area we compare the actual area with
		eval("isLargerThan(10)", varResolver, functionMapper.executor(varResolver));
		varResolver = rectangleMapper.resolverFor(new Rectangle(3, 2));
		eval("isLargerThan(10)", varResolver, functionMapper.executor(varResolver));
	}

	private static void eval(String expression,
		VariableResolver varResolver, FunctionExecutor functionExecutor)
	{
		System.out.println(expression + " = " +
			VexpressedUtils.eval(expression, varResolver, functionExecutor));
	}

	@ExpressionFunction
	public static Integer sum(@FunctionParam(name = "nums") Collection<Integer> ints) {
		return ints.stream().mapToInt(i -> i).sum();
	}

	public static boolean areaLargerThan(VariableResolver variableResolver, int threshold) {
		int a = (int) variableResolver.resolve("a");
		int b = (int) variableResolver.resolve("b");
		return a * b > threshold;
	}
}

class Rectangle {
	public int a;
	public int b;

	public Rectangle(int a, int b) {
		this.a = a;
		this.b = b;
	}
}
