package de.bschandera.tryouts;

import com.google.common.collect.Lists;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class MergesortSimpleTest {

    @Test
    public void testSort() {
        MergesortStrategy lucy = MergesortSimple.getInstance();

        assertThat(lucy.sort(Lists.newArrayList(0))).isEqualTo(Lists.newArrayList(0));
        assertThat(lucy.sort(Lists.newArrayList(2, 1))).isEqualTo(Lists.newArrayList(1, 2));
        assertThat(lucy.sort(Lists.newArrayList(9, 8, 7, 6, 5, 4, 3, 2, 1, 0))).
                isEqualTo(Lists.newArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    }

}
