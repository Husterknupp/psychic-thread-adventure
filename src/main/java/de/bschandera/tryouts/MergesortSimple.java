package de.bschandera.tryouts;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.sf.qualitycheck.Check;

import java.util.LinkedList;
import java.util.List;

/**
 * Implement the Mergesort algorithm without using threaded programing.
 */
public class MergesortSimple implements MergesortStrategy {

    private static MergesortSimple INSTANCE = new MergesortSimple();

    private MergesortSimple() {
    }

    public static MergesortSimple getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Integer> sort(Iterable<Integer> unsorted) {
        Check.noNullElements(unsorted, "unsorted");

        int n = Iterables.size(unsorted);
        if (n <= 1) {
            return ImmutableList.copyOf(unsorted);
        } else {
            List<Integer> copy = Lists.newLinkedList(unsorted);
            return merge(sort(copy.subList(0, n / 2)), sort(copy.subList(n / 2, n)));
        }
    }

    private List<Integer> merge(List<Integer> left, List<Integer> right) {

        left = new LinkedList<>(left);
        right = new LinkedList<>(right);
        List<Integer> result = new LinkedList<>();

        while (left.size() > 0 && right.size() > 0) {
            if (left.get(0) <= right.get(0)) {
                result.add(left.get(0));
                left.remove(0);
            } else {
                result.add(right.get(0));
                right.remove(0);
            }
        }

        if (left.size() != 0) {
            for (Integer number : left) {
                result.add(number);
            }
        } else {
            for (Integer number : right) {
                result.add(number);
            }
        }

        return result;

    }

}
