package com.virgo47.vexpressed.test.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.virgo47.vexpressed.core.UnknownVariable;
import com.virgo47.vexpressed.support.VariableBinding;

import java.util.HashMap;
import java.util.Map;

import com.virgo47.vexpressed.test.TestBase;
import org.testng.annotations.Test;

public class VariableBindingTest extends TestBase {

	@Test
	public void throwsUnknownVariableWhenEmpty() {
		assertThatThrownBy(() -> new VariableBinding().resolve("undefined"))
			.isInstanceOf(UnknownVariable.class);
	}

	@Test
	public void throwsUnknownVariableForUndefinedVariable() {
		VariableBinding binding = new VariableBinding()
			.add("var", "value");

		assertThatThrownBy(() -> binding.resolve("undefined"))
			.isInstanceOf(UnknownVariable.class);
	}

	@Test
	public void returnsDefinedNonNullValue() {
		VariableBinding binding = new VariableBinding()
			.add("var", "value");

		assertThat(binding.resolve("var")).isEqualTo("value");
	}

	@Test
	public void returnsDefinedNullValue() {
		VariableBinding binding = new VariableBinding()
			.add("var", null);

		assertThat(binding.resolve("var")).isNull();
	}

	@Test
	public void returnsAllValuesFromProvidedMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("str", "String");
		map.put("int", 1);

		VariableBinding binding = new VariableBinding()
			.addAll(map);

		assertThat(binding.resolve("str")).isEqualTo("String");
		assertThat(binding.resolve("int")).isEqualTo(1);
	}

	@Test
	public void canCombineMapAndDirectlyDefinedVariables() {
		Map<String, Object> map = new HashMap<>();
		map.put("str", null);

		VariableBinding binding = new VariableBinding()
			.addAll(map)
			.add("directly", 1);

		assertThat(binding.resolve("str")).isNull();
		assertThat(binding.resolve("directly")).isEqualTo(1);
	}
}
