package de.bschandera.tryouts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.google.common.collect.Maps;

public class AdventureTest {
	// 1.000.000 random numbers are too much for my HP notebook. 
	private static final String _4_000 = "4.000";
	private static final String _16_000 = "16.000";
	private static final String _64_000 = "64.000";
	private static final Logger LOG = Logger.getLogger(AdventureTest.class);
	private static final Map<String, List<Integer>> randomNumbers = Maps.newHashMap();

	private final MergesortStrategy lucy = new MergesortSimple();

	@BeforeClass
	public static void setup() {
		// System stats
		LOG.info("Number of threads/cores (c): " + Runtime.getRuntime().availableProcessors());
		LOG.info("CREATE LISTS WITH 4k, 16, 64k RANDOM NUMBERS");
		randomNumbers.put(_4_000, nRandomNumbers(4000));
		randomNumbers.put(_16_000, nRandomNumbers(16000));
		randomNumbers.put(_64_000, nRandomNumbers(64000));
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

	@Test
	public void sort4000Numbers1Thread() {
		LOG.info("(n): 4.000");
		lucy.sort(randomNumbers.get(_4_000));
	}

	@Test
	public void sort16000Numbers1Thread() {
		LOG.info("(n): 16.000");
		lucy.sort(randomNumbers.get(_16_000));
	}

	@Ignore("too long")
	@Test
	public void sort64Numbers1Thread() {
		LOG.info("(n): 64.000");
		lucy.sort(randomNumbers.get(_64_000));
	}

}
