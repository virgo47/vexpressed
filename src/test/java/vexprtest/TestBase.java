package vexprtest;

import org.testng.annotations.BeforeMethod;
import vexpressed.VexpressedUtils;
import vexpressed.core.FunctionExecutor;
import vexpressed.core.VariableResolver;
import vexpressed.meta.ExpressionType;
import vexpressed.validation.FunctionTypeResolver;
import vexpressed.validation.VariableTypeResolver;

public class TestBase {

	protected VariableResolver variableResolver;
	protected FunctionExecutor functionExecutor;

	protected VariableTypeResolver variableTypeResolver;
	protected FunctionTypeResolver functionTypeResolver;

	@BeforeMethod
	public void init() {
		variableResolver = var -> null;
		functionExecutor = null;

		variableTypeResolver = var -> null;
		functionTypeResolver = null;
	}

	public Object eval(String expression) {
		return VexpressedUtils.eval(expression, variableResolver, functionExecutor);
	}

	public ExpressionType check(String expression) {
		return VexpressedUtils.check(expression, variableTypeResolver, functionTypeResolver);
	}
}
