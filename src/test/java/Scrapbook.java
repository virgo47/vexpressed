import static vexpressed.core.VariableResolver.NULL_VARIABLE_RESOLVER;

import vexpressed.VexpressedUtils;
import vexpressed.core.FunctionExecutor;
import vexpressed.core.VariableResolver;
import vexpressed.meta.ExpressionType;
import vexpressed.support.VariableMapper;

public class Scrapbook {

	public static void main(String[] args) {
		// simple, no variables or functions
		System.out.println("5+6 = " + VexpressedUtils.eval("5+6", NULL_VARIABLE_RESOLVER, null));

		eval("2+2^2^3/16", NULL_VARIABLE_RESOLVER, null);
		eval("2 + ((2^(2^3)) / 16)", NULL_VARIABLE_RESOLVER, null);

		// variable
		eval("a * 3", var -> 2, null);

		// variable mapper for type Rectangle
		VariableMapper<Rectangle> rectangleMapper = new VariableMapper<Rectangle>()
			.define("a", ExpressionType.INTEGER, o -> o.a)
			.define("b", ExpressionType.INTEGER, o -> o.b)
			.finish();
		// and now use the same mapper with the same expression for two different objects
		eval("a * b", rectangleMapper.resolverFor(new Rectangle(3, 4)), null);
		eval("a * b", rectangleMapper.resolverFor(new Rectangle(2, 5)), null);
	}

	private static void eval(String expression,
		VariableResolver varResolver, FunctionExecutor functionExecutor)
	{
		System.out.println(expression + " = " +
			VexpressedUtils.eval(expression, varResolver, functionExecutor));
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
