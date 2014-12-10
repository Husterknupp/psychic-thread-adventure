package de.bschandera.tryouts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

public class AdventureTest {
	// 1.000.000 random numbers are too much for my HP notebook. 

	private static final Logger LOG = Logger.getLogger(AdventureTest.class);
	private MergesortStrategy lucy = new MergesortSimple();

	@BeforeClass
	public static void setup() {
		// System stats
		LOG.info("Number of threads/cores (c)");
		LOG.info("(c): " + Runtime.getRuntime().availableProcessors());
	}

	// Actual Sorting
	@Test
	public void sort100Numbers1Thread() {
		lucy.sort(nRandomNumbers(100));
	}

	@Test
	public void sort10000Numbers1Thread() {
		lucy.sort(nRandomNumbers(10000));
	}

	@Test
	public void sort100000Numbers1Thread() {
		lucy.sort(nRandomNumbers(100000));
	}

	private static List<Integer> nRandomNumbers(int m) {
		List<Integer> result = new ArrayList<>(m);
		Random random = new Random();
		for (int i = 0; i < m; i++) {
			result.add(random.nextInt());
		}
		return result;
	}

}
