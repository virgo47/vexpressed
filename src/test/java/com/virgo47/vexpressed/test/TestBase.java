package com.virgo47.vexpressed.test;

import com.virgo47.vexpressed.VexpressedUtils;
import com.virgo47.vexpressed.core.FunctionExecutor;
import com.virgo47.vexpressed.core.VariableResolver;
import com.virgo47.vexpressed.meta.ExpressionType;
import com.virgo47.vexpressed.validation.FunctionTypeResolver;
import com.virgo47.vexpressed.validation.VariableTypeResolver;
import org.testng.annotations.BeforeMethod;

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

	public Object evalAsObject(String expression) {
		return VexpressedUtils.eval(expression, variableResolver, functionExecutor);
	}

	public <RT> RT eval(String expression) {
		return VexpressedUtils.eval(expression, variableResolver, functionExecutor);
	}

	public ExpressionType check(String expression) {
		return VexpressedUtils.check(expression, variableTypeResolver, functionTypeResolver);
	}
}
