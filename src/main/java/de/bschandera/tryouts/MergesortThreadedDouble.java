package de.bschandera.tryouts;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * Implement Mergesort algorithm. Use one main thread and two subtasks for sorting. Unsorted vector will be split into
 * two parts which in turn will be sorted in a single thread each. The last merge will be done in one single thread.
 */
public class MergesortThreadedDouble implements MergesortStrategy {
	private static final class MyCallable implements Callable<List<Integer>> {
		private final Collection<Integer> unsortedNumbers;

		public MyCallable(Collection<Integer> unsortedNumbers) {
			this.unsortedNumbers = unsortedNumbers;
		}

		@Override
		public List<Integer> call() throws Exception {
			return new MergesortSimple().sort(this.unsortedNumbers);
		}

	}

	private static final Logger LOG = Logger.getLogger(MergesortThreadedDouble.class);

	@Override
	public List<Integer> sort(Iterable<Integer> unsorted) {
		final int n = Iterables.size(unsorted);
		final List<Integer> part1 = Lists.newArrayList(unsorted).subList(0, n / 2);
		final List<Integer> part2 = Lists.newArrayList(unsorted).subList(n / 2, n);

		ExecutorService executor = Executors.newFixedThreadPool(2);
		Future<List<Integer>> result1 = executor.submit(new MyCallable(part1));
		Future<List<Integer>> result2 = executor.submit(new MyCallable(part2));

		try {
			return MergesortSimple.merge(result1.get(), result2.get());
		} catch (InterruptedException | ExecutionException e) {
			LOG.error("One part could not be sorted.");
			LOG.error("That's the reason: ", e);
			return Collections.emptyList();
		}
	}

}
