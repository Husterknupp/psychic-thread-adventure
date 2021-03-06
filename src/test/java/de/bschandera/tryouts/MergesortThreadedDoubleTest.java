package de.bschandera.tryouts;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class MergesortThreadedDoubleTest {

	MergesortThreadedDouble lucy;

	@Before
	public void setup() {
		lucy = new MergesortThreadedDouble();
	}

	@Test
	public void testSort() {
		assertThat(lucy.sort(Lists.newArrayList(0))).isEqualTo(Lists.newArrayList(0));
		assertThat(lucy.sort(Lists.newArrayList(2, 1, 0))).isEqualTo(Lists.newArrayList(0, 1, 2));
		assertThat(lucy.sort(Lists.newArrayList(100, 10000, 10, 0))).isEqualTo(Lists.newArrayList(0, 10, 100, 10000));
		assertThat(lucy.sort(Lists.newArrayList(1, -1, 0))).isEqualTo(Lists.newArrayList(-1, 0, 1));
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
