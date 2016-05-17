package vexprtest;

import static org.assertj.core.api.Assertions.assertThat;
import static vexpressed.meta.ExpressionType.OBJECT;

import java.util.Set;

import org.testng.annotations.Test;

/** @noinspection unchecked */
public class SetTest extends TestBase {

	@Test
	public void setExpressionTypeIsObject() {
		assertThat(check("[]")).isEqualTo(OBJECT);
		assertThat(check("[1]")).isEqualTo(OBJECT);
		assertThat(check("[1, 'mixed']")).isEqualTo(OBJECT);
	}

	@Test
	public void emptySetExpressionReturnsEmptySet() {
		Object result = eval("[]");
		assertThat(result).isNotNull()
			.isInstanceOf(Set.class);
		assertThat((Set) result).hasSize(0);
	}

	@Test
	public void setOfOneExpressionTest() {
		Object result = eval("[1]");
		assertThat(result).isNotNull()
			.isInstanceOf(Set.class);
		assertThat((Set) result).hasSize(1)
			.containsExactly(1);
	}

	@Test
	public void setOfTwoExpressionTest() {
		Object result = eval("[1,2]");
		assertThat(result).isNotNull()
			.isInstanceOf(Set.class);
		assertThat((Set) result).hasSize(2)
			.containsExactly(1, 2);
	}

	@Test
	public void setOfMorePreservesOrder() {
		Object result = eval("[4,1,2]");
		assertThat(result).isNotNull()
			.isInstanceOf(Set.class);
		assertThat((Set) result).containsExactly(4, 1, 2);
	}

	@Test
	public void setRemovesDuplicates() {
		Object result = eval("[4,1,2, 1, 4]");
		assertThat(result).isNotNull()
			.isInstanceOf(Set.class);
		assertThat((Set) result).containsExactly(4, 1, 2);
	}
}
