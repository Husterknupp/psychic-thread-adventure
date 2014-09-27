package de.bschandera.tryouts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Adventure {
	private static final Logger LOG = Logger.getLogger(Adventure.class);

	public static void main(String[] args) {
		initializeLogger("log4j.properties");

		// System stats
		LOG.info("Number of threads/cores (c)");
		LOG.info("(c): " + Runtime.getRuntime().availableProcessors());

		// Actual Sorting
		sortMNumbersNTimes(MergesortStrategy.mergesortSimple(), 100, 300);
		sortMNumbersNTimes(MergesortStrategy.mergesortSimple(), 10000, 300);
		sortMNumbersNTimes(MergesortStrategy.mergesortSimple(), 100000, 300);

	}

	private static void initializeLogger(String configFile) {
		PropertyConfigurator.configure(Adventure.class.getResourceAsStream("/" + configFile));
		LOG.info("Logger successfully initialized");
	}

	private static void sortMNumbersNTimes(MergesortStrategy lucy, int m, int n) {
		for (int i = 0; i < n; i++) {
			lucy.sort(mRandomNumbers(m));
		}
	}

	private static List<Integer> mRandomNumbers(int m) {
		List<Integer> result = new ArrayList<>(m);
		Random random = new Random();
		for (int i = 0; i < m; i++) {
			result.add(random.nextInt());
		}
		return result;
	}

}
