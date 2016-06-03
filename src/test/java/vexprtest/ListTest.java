package vexprtest;

import static org.assertj.core.api.Assertions.assertThat;
import static vexpressed.meta.ExpressionType.OBJECT;

import java.util.List;

import org.testng.annotations.Test;

/** @noinspection unchecked */
public class ListTest extends TestBase {

	@Test
	public void listExpressionTypeIsObject() {
		assertThat(check("[]")).isEqualTo(OBJECT);
		assertThat(check("[1]")).isEqualTo(OBJECT);
		assertThat(check("[1, 'mixed']")).isEqualTo(OBJECT);
	}

	@Test
	public void emptyListExpressionReturnsEmptyList() {
		Object result = eval("[]");
		assertThat(result).isNotNull()
			.isInstanceOf(List.class);
		assertThat((List) result).hasSize(0);
	}

	@Test
	public void listOfOneExpressionTest() {
		Object result = eval("[1]");
		assertThat(result).isNotNull()
			.isInstanceOf(List.class);
		assertThat((List) result).hasSize(1)
			.containsExactly(1);
	}

	@Test
	public void listOfTwoExpressionTest() {
		Object result = eval("[1,2]");
		assertThat(result).isNotNull()
			.isInstanceOf(List.class);
		assertThat((List) result).hasSize(2)
			.containsExactly(1, 2);
	}

	@Test
	public void listOfMorePreservesOrder() {
		Object result = eval("[4,1,2]");
		assertThat(result).isNotNull()
			.isInstanceOf(List.class);
		assertThat((List) result).containsExactly(4, 1, 2);
	}

	@Test
	public void listKeepsDuplicates() {
		Object result = eval("[4,1,2, 1, 4]");
		assertThat(result).isNotNull()
			.isInstanceOf(List.class);
		assertThat((List) result).containsExactly(4, 1, 2, 1, 4);
	}
}
