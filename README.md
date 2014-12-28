psychic-thread-adventure
=================
What's the difference between sorting different counts of numbers using multiple threads and sorting them single threaded, in terms of time needed to sort the numbers?

This project discovers how threading influences performance of simple algorithms like Mergesort algorithm. Besides that the effort will be documented what needs to be done to make a program run with multiple threads w/o using frameworks. Maybe some tests will be done using Java 8 later on.

Inspired by Robert C. Martin's (et al.) Clean Code, 2009, Pearson Education, Inc, chapter on concurrency.

Additional Information
=================

**Micro Benchmarking libraries**
* http://labs.carrotsearch.com/junit-benchmarks-tutorial.html#turning-junit4-tests-into-benchmarks
* (not used) https://code.google.com/p/caliper/source/browse/tutorial/Tutorial.java
* (not used) http://perf4j.codehaus.org/devguide.html

**Concurrency and Distribution Framework**
* http://akka.io/

Adventure Planning
=================
To run the measuring on your machine, please run `Adventure.java` as JUnit test.

---

**Threading constellations**
* without threading, i.e., one execution path, no branching
  * consider different `n` and different machines
* few threads (fix number of threads)
  * consider different #Threads, different `n` and different machines
* dynamically created threads according to high/low `n`
  * consider different `n` and different machines
* (probably) M. written in functional style, oriented towards [this article](http://stackoverflow.com/questions/24855746/understanding-when-and-how-to-use-java-8-lambdas)

* different number of threads
  * #Threads 2
  * #Threads 3
  * #Threads 4
  * #Threads 8
* different `n`s
  * 4.000,
  * 16.000,
  * 32.000 and
* on different machines

Results
=================
NOTE: Merge sort's average case **performance** is `O(n log n)`

NOTE: Limits of concurrency gains: The runtime is limited by parts of the task which can be performed in parallel. The theoretical possible performance gain can be calculated by the following rule which is referred to as **Amdahl's Law**.
> If F is the percentage of the program which can not run in parallel and N is the number of processes, then the maximum performance gain is 
> `1 / (F + ((1-F)/n))` (thank you [vogella](http://www.vogella.com/tutorials/JavaConcurrency/article.html#concurrency_amdahl))

*2014-12-12/13 #2 - My HP machine (2 cores AMD Sempron, 3GB RAM)*
```
Adventure.sort4000Numbers1Thread: [measured 10 out of 15 rounds, threads: 1 (sequential)]
 round: 0.04 [+- 0.01], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+- 0.00], GC.calls: 11, GC.time: 0.02, time.total: 0.67, time.warmup: 0.24, time.bench: 0.44
```
```
Adventure.sort4000Numbers2Threads: [measured 10 out of 15 rounds, threads: 1 (sequential)]
 round: 0.02 [+- 0.01], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+- 0.00], GC.calls: 10, GC.time: 0.02, time.total: 0.28, time.warmup: 0.10, time.bench: 0.19
```
```
Adventure.sort16000Numbers1Thread: [measured 10 out of 15 rounds, threads: 1 (sequential)]
 round: 0.79 [+- 0.21], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+- 0.00], GC.calls: 46, GC.time: 0.09, time.total: 11.83, time.warmup: 3.86, time.bench: 7.96
```
```
Adventure.sort16000Numbers2Threads: [measured 10 out of 15 rounds, threads: 1 (sequential)]
 round: 0.24 [+- 0.02], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+- 0.00], GC.calls: 49, GC.time: 0.13, time.total: 3.63, time.warmup: 1.21, time.bench: 2.43
```
```
Adventure.sort32000Numbers1Thread: [measured 10 out of 15 rounds, threads: 1 (sequential)]
 round: 7.11 [+- 1.96], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+- 0.00], GC.calls: 103, GC.time: 0.21, time.total: 106.37, time.warmup: 35.30, time.bench: 71.07
```
```
Adventure.sort32000Numbers2Threads: [measured 10 out of 15 rounds, threads: 1 (sequential)]
 round: 1.78 [+- 0.48], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+- 0.00], GC.calls: 99, GC.time: 0.29, time.total: 24.51, time.warmup: 6.72, time.bench: 17.79
```
```
Adventure.sort64000Numbers1Thread: [measured 10 out of 15 rounds, threads: 1 (sequential)]
 round: 32.24 [+- 9.77], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+- 0.00], GC.calls: 220, GC.time: 0.60, time.total: 481.48, time.warmup: 159.11, time.bench: 322.37
```
```
Adventure.sort64000Numbers2Threads: [measured 10 out of 15 rounds, threads: 1 (sequential)]
 round: 14.94 [+- 2.25], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+- 0.00], GC.calls: 222, GC.time: 0.75, time.total: 223.85, time.warmup: 74.43, time.bench: 149.42
```

*2014-12-12/13 #1 - My HP machine (2 cores AMD Sempron, 3GB RAM)*
```
AdventureTest.sort4000Numbers1Thread: [measured 10 out of 15 rounds, threads: 1 (sequential)]
 round: 0.04 [+- 0.01], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+- 0.00], GC.calls: 11, GC.time: 0.02, time.total: 0.63, time.warmup: 0.20, time.bench: 0.42
```
```
AdventureTest.sort4000Numbers2Threads: [measured 10 out of 15 rounds, threads: 1 (sequential)]
 round: 0.02 [+- 0.01], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+- 0.00], GC.calls: 11, GC.time: 0.02, time.total: 0.38, time.warmup: 0.17, time.bench: 0.20
```
```
AdventureTest.sort16000Numbers1Thread: [measured 10 out of 15 rounds, threads: 1 (sequential)]
 round: 0.75 [+- 0.02], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+- 0.00], GC.calls: 48, GC.time: 0.08, time.total: 11.26, time.warmup: 3.77, time.bench: 7.49
```
```
AdventureTest.sort16000Numbers2Threads: [measured 10 out of 15 rounds, threads: 1 (sequential)]
 round: 0.26 [+- 0.05], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+- 0.00], GC.calls: 51, GC.time: 0.20, time.total: 3.88, time.warmup: 1.36, time.bench: 2.52
```

*2014-12-10 - My HP machine (2 cores AMD Sempron, 3GB RAM)*
```
AdventureTest.sort4000Numbers1Thread: [measured 10 out of 15 rounds, threads: 1 (sequential)]
 round: 0.04 [+- 0.01], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+- 0.00], GC.calls: 12, GC.time: 0.01, time.total: 0.62, time.warmup: 0.20, time.bench: 0.42
```
```
AdventureTest.sort16000Numbers1Thread: [measured 10 out of 15 rounds, threads: 1 (sequential)]
 round: 0.77 [+- 0.09], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+- 0.00], GC.calls: 51, GC.time: 0.07, time.total: 11.78, time.warmup: 4.05, time.bench: 7.73
```

Effort Notes
=================
- Find out what part of the programm ist really cost expensive
- change your mind. Even the programm flow has to be in a parallel manner. Dont wait for one thread to finish before calling another one.
- Cope with the slightly different way of handling results that are calculated by different threads.
- separate application logic from thread logic
