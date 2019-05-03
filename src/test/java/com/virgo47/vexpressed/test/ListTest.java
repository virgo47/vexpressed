package com.virgo47.vexpressed.test;

import static com.virgo47.vexpressed.meta.ExpressionType.OBJECT;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.testng.annotations.Test;

public class ListTest extends TestBase {

	@Test
	public void listExpressionTypeIsObject() {
		assertThat(check("[]")).isEqualTo(OBJECT);
		assertThat(check("[1]")).isEqualTo(OBJECT);
		assertThat(check("[1, 'mixed']")).isEqualTo(OBJECT);
	}

	@Test
	public void emptyListExpressionReturnsEmptyList() {
		List<Object> result = eval("[]");
		assertThat(result).isNotNull()
			.isInstanceOf(List.class);
		assertThat(result).hasSize(0);
	}

	@Test
	public void listOfOneExpressionTest() {
		List<Object> result = eval("[1]");
		assertThat(result).isNotNull()
			.isInstanceOf(List.class);
		assertThat(result).hasSize(1)
			.containsExactly(1);
	}

	@Test
	public void listOfTwoExpressionTest() {
		List<Object> result = eval("[1,2]");
		assertThat(result).isNotNull()
			.isInstanceOf(List.class);
		assertThat(result).hasSize(2)
			.containsExactly(1, 2);
	}

	@Test
	public void listOfMorePreservesOrder() {
		List<Object> result = eval("[4,1,2]");
		assertThat(result).isNotNull()
			.isInstanceOf(List.class);
		assertThat(result).containsExactly(4, 1, 2);
	}

	@Test
	public void listKeepsDuplicates() {
		List<Object> result = eval("[4,1,2, 1, 4]");
		assertThat(result).isNotNull()
			.isInstanceOf(List.class);
		assertThat(result).containsExactly(4, 1, 2, 1, 4);
	}
}
