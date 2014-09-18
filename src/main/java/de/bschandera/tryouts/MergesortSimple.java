package de.bschandera.tryouts;

import java.util.LinkedList;
import java.util.List;

import net.sf.qualitycheck.Check;

import org.apache.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * Implement the Mergesort algorithm without using threaded programing.
 */
public class MergesortSimple implements MergesortStrategy {
	private static MergesortSimple INSTANCE = new MergesortSimple();
	private static final Logger LOGGER = Logger.getLogger(MergesortSimple.class);

	private MergesortSimple() {
	}

	public static MergesortSimple getInstance() {
		LOGGER.info("Instance of " + MergesortSimple.class.getSimpleName() + " assigned.");
		return INSTANCE;
	}

	@Override
	public List<Integer> sort(Iterable<Integer> unsorted) {
		Check.noNullElements(unsorted, "unsorted");
		LOGGER.info("Sort " + Iterables.size(unsorted) + " numbers.");

		long start = System.nanoTime();
		List<Integer> result = sortH(Lists.newLinkedList(unsorted));
		LOGGER.info("Numbers are sorted, now. Duration in milliseconds.");
		LOGGER.info("(d): " + (System.nanoTime() - start) / 1000000);

		return result;
	}

	private List<Integer> sortH(List<Integer> unsorted) {
		int n = unsorted.size();
		if (n <= 1) {
			return ImmutableList.copyOf(unsorted);
		} else {
			return ImmutableList.copyOf(merge(sortH(unsorted.subList(0, n / 2)), sortH(unsorted.subList(n / 2, n))));
		}
	}

	private List<Integer> merge(List<Integer> left, List<Integer> right) {

		// create new objects to allow element removal
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
