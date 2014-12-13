package de.bschandera.tryouts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.google.common.collect.Maps;

public class Adventure {
	// 1.000.000 random numbers are too much for my HP notebook. 
	private static final Logger LOG = Logger.getLogger(Adventure.class);
	private static final Map<Integer, List<Integer>> randomNumbers = Maps.newHashMap();

	private final MergesortStrategy simpleLucy = new MergesortSimple();
	private final MergesortStrategy shizoLucy = new MergesortThreadedDouble();

	@BeforeClass
	public static void setup() {
		// System stats
		LOG.info("Number of threads/cores (c): " + Runtime.getRuntime().availableProcessors());
		LOG.info("CREATE LISTS WITH 4k, 16k, 32k, 64k RANDOM NUMBERS");
		randomNumbers.put(4000, nRandomNumbers(4000));
		randomNumbers.put(16000, nRandomNumbers(16000));
		randomNumbers.put(32000, nRandomNumbers(32000));
		randomNumbers.put(64000, nRandomNumbers(64000));
		LOG.info("SORT THESE NUMBERS IN MULTIPLE RERUNS EACH");
	}

	private static List<Integer> nRandomNumbers(int m) {
		List<Integer> result = new ArrayList<>(m);
		Random random = new Random();
		for (int i = 0; i < m; i++) {
			result.add(random.nextInt());
		}
		return result;
	}

	@Rule
	public TestRule benchmarkRun = new BenchmarkRule();

	/*
	 * ONE THREAD
	 */
	@Test
	public void sort4000Numbers1Thread() {
		//		LOG.info("(n): 4.000");
		simpleLucy.sort(randomNumbers.get(4000));
	}

	@Test
	public void sort16000Numbers1Thread() {
		//		LOG.info("(n): 16.000");
		simpleLucy.sort(randomNumbers.get(16000));
	}

	@Test
	public void sort32000Numbers1Thread() {
		//		LOG.info("(n): 32.000");
		simpleLucy.sort(randomNumbers.get(32000));
	}

	@Test
	public void sort64000Numbers1Thread() {
		//		LOG.info("(n): 64.000");
		simpleLucy.sort(randomNumbers.get(64000));
	}

	/*
	 * TWO THREADS
	 */
	@Test
	public void sort4000Numbers2Threads() {
		//		LOG.info("(n): 4.000");
		shizoLucy.sort(randomNumbers.get(4000));
	}

	@Test
	public void sort16000Numbers2Threads() {
		//		LOG.info("(n): 16.000");
		shizoLucy.sort(randomNumbers.get(16000));
	}

	@Test
	public void sort32000Numbers2Threads() {
		//		LOG.info("(n): 32.000");
		shizoLucy.sort(randomNumbers.get(32000));
	}

	@Test
	public void sort64000Numbers2Threads() {
		//		LOG.info("(n): 64.000");
		shizoLucy.sort(randomNumbers.get(64000));
	}

}
