package de.bschandera.tryouts;

import java.util.List;

public interface MergesortStrategy {

	/**
	 * Sort the given {@linkplain java.lang.Integer}s with Mergesort algorithm.
	 * 
	 * @return All {@linkplain java.lang.Integer}s in ascending order.
	 */
	List<Integer> sort(Iterable<Integer> unsorted);

}
