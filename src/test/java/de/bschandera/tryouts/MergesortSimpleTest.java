package de.bschandera.tryouts;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class MergesortSimpleTest {

	private static MergesortSimple lucy;

	@Before
	public void setup() {
		lucy = new MergesortSimple();
	}

	@Test
	public void testSort() {
		assertThat(lucy.sort(Lists.newArrayList(0))).isEqualTo(Lists.newArrayList(0));
		assertThat(lucy.sort(Lists.newArrayList(2, 1))).isEqualTo(Lists.newArrayList(1, 2));
		assertThat(lucy.sort(Lists.newArrayList(9, 8, 7, 6, 5, 4, 3, 2, 1, 0))).isEqualTo(
				Lists.newArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
	}

	@Test
	public void testSortWithBorders() {
		assertThat(lucy.sort(Lists.newArrayList(0, Integer.MAX_VALUE, Integer.MIN_VALUE))).isEqualTo(
				Lists.newArrayList(Integer.MIN_VALUE, 0, Integer.MAX_VALUE));
	}

	@Test
	public void testSortWithEgalSequence() {
		assertThat(lucy.sort(Lists.newArrayList(7, 5, 3, 5))).isEqualTo(Lists.newArrayList(3, 5, 5, 7));
	}

}
