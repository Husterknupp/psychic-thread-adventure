package de.bschandera.tryouts;

import java.util.List;

public abstract class MergesortStrategy {

	/**
	 * Sort the given {@linkplain java.lang.Integer}s with Mergesort algorithm.
	 * 
	 * @return All {@linkplain java.lang.Integer}s in ascending order.
	 */
	abstract List<Integer> sort(Iterable<Integer> unsorted);

	public static MergesortStrategy mergesortSimple() {
		return MergesortSimple.getInstance();
	}

}
