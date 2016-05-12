package vexprtest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static vexpressed.ExpressionType.DECIMAL;
import static vexpressed.ExpressionType.OBJECT;
import static vexpressed.ExpressionType.STRING;

import java.util.Iterator;
import java.util.Set;

import org.assertj.core.api.Condition;
import org.testng.annotations.Test;
import vexpressed.vars.UnknownVariable;
import vexpressed.vars.VariableDefinition;
import vexpressed.vars.VariableMapper;

public class VariableMapperTest {

	@Test
	public void variableMapperResolvesVariableValues() {
		VariableMapper<Object> variables = new VariableMapper<>()
			.define("x", DECIMAL, o -> 5)
			.define("s", STRING, o -> "str");

		assertThat(variables.resolveVariable("x", null)).isEqualTo(5);
		assertThat(variables.resolveVariable("s", null)).isEqualTo("str");
	}

	@Test
	public void simpleVariableMapperReturnsInfoAboutItsVariables() {
		VariableMapper<Object> variables = new VariableMapper<>()
			.define("x", DECIMAL, o -> 5)
			.define("s", STRING, o -> "");

		Set<VariableDefinition> varInfo = variables.variableInfo();
		assertThat(varInfo).hasSize(2);
		Iterator<VariableDefinition> iterator = varInfo.iterator();
		VariableDefinition var1 = iterator.next();
		assertThat(var1.name).isEqualTo("s");
		assertThat(var1.type).isEqualTo(STRING);
		VariableDefinition var2 = iterator.next();
		assertThat(var2.name).isEqualTo("x");
		assertThat(var2.type).isEqualTo(DECIMAL);
	}

	@Test
	public void variableMapperResolvesVariableValuesUsingDelegate() {
		VariableMapper<Object> delegate = new VariableMapper<>()
			.define("s", STRING, o -> "str");
		VariableMapper<Object> variables = new VariableMapper<>()
			.define("x", DECIMAL, o -> 5)
			.addDelegate(delegate, o -> o);

		assertThat(variables.resolveVariable("x", null)).isEqualTo(5);
		assertThat(variables.resolveVariable("s", null)).isEqualTo("str");
	}

	@Test
	public void variableMapperIncludesDelegateInVariableInformation() {
		VariableMapper<Object> delegate = new VariableMapper<>()
			.define("s", STRING, o -> "");
		VariableMapper<Object> variables = new VariableMapper<>()
			.define("x", DECIMAL, o -> 5)
			.addDelegate(delegate, o -> o);

		Set<VariableDefinition> varInfo = variables.variableInfo();
		assertThat(varInfo).hasSize(2);
		Iterator<VariableDefinition> iterator = varInfo.iterator();
		VariableDefinition var1 = iterator.next();
		assertThat(var1.name).isEqualTo("s");
		assertThat(var1.type).isEqualTo(STRING);
		VariableDefinition var2 = iterator.next();
		assertThat(var2.name).isEqualTo("x");
		assertThat(var2.type).isEqualTo(DECIMAL);
	}

	@Test
	public void variableMapperResolvesVariableValuesUsingMultipleDelegates() {
		VariableMapper<Object> delegate1 = new VariableMapper<>()
			.define("x", STRING, o -> "str1");
		VariableMapper<Object> delegate2 = new VariableMapper<>()
			.define("y", STRING, o -> "str2");
		VariableMapper<Object> variables = new VariableMapper<>()
			.addDelegate(delegate1, o -> o)
			.addDelegate(delegate2, o -> o);

		assertThat(variables.resolveVariable("x", null)).isEqualTo("str1");
		assertThat(variables.resolveVariable("y", null)).isEqualTo("str2");
		assertThatThrownBy(() -> variables.resolveVariable("z", null))
			.isInstanceOf(UnknownVariable.class)
			.has(new Condition<>(e -> ((UnknownVariable) e).variableName.equals("z"),
				"variableName must be filled"));
	}

	@Test
	public void variableMapperIncludesAllDelegatesInVariableInformation() {
		VariableMapper<Object> delegate1 = new VariableMapper<>()
			.define("x", STRING, o -> null);
		VariableMapper<Object> delegate2 = new VariableMapper<>()
			.define("y", OBJECT, o -> null);
		VariableMapper<Object> variables = new VariableMapper<>()
			.addDelegate(delegate1, o -> o)
			.addDelegate(delegate2, o -> o);

		Set<VariableDefinition> varInfo = variables.variableInfo();
		assertThat(varInfo).hasSize(2);
		Iterator<VariableDefinition> iterator = varInfo.iterator();
		VariableDefinition var1 = iterator.next();
		assertThat(var1.name).isEqualTo("x");
		assertThat(var1.type).isEqualTo(STRING);
		VariableDefinition var2 = iterator.next();
		assertThat(var2.name).isEqualTo("y");
		assertThat(var2.type).isEqualTo(OBJECT);
	}
}