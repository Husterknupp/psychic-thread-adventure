psychic-thread-adventure
========================
What's the difference between sorting different counts of numbers using multiple threads and sorting them single threaded, in terms of time needed to sort the numbers?

This project discovers how threading influences performance of simple algorithms like Mergesort algorithm. Besides that the effort will be documented what needs to be done to make a program run with multiple threads w/o using frameworks. Maybe some tests will be done using Java 8 later on.

Inspired by Robert C. Martin's (et al.) Clean Code, 2009, Pearson Education, Inc, chapter on concurrency.

Micro Benchmarking libraries
-----------------
* http://labs.carrotsearch.com/junit-benchmarks-tutorial.html#turning-junit4-tests-into-benchmarks
* (not used) https://code.google.com/p/caliper/source/browse/tutorial/Tutorial.java
* (not used) http://perf4j.codehaus.org/devguide.html

Adventure Planning
=================
Threading constellations
* without threading, i.e., one execution path, no branching
  * consider different `n` and different machines
* few threads (fix number of threads)
  * consider different #Threads, different `n` and different machines
* dynamically created threads according to high/low `n`
  * consider different `n` and different machines
* (probably) M. written in functional style, oriented towards [this article](http://stackoverflow.com/questions/24855746/understanding-when-and-how-to-use-java-8-lambdas)

Run different constellations with
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
-----------------
NOTE: Merge sort's average case **performance** is `O(n log n)`

*2014-12-10 - My HP machine (2 cors AMD Sempron, 3GB RAM)*
```
AdventureTest.sort4000Numbers1Thread: [measured 10 out of 15 rounds, threads: 1 (sequential)]
 round: 0.04 [+- 0.01], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+- 0.00], GC.calls: 12, GC.time: 0.01, time.total: 0.62, time.warmup: 0.20, time.bench: 0.42
```
```
AdventureTest.sort16000Numbers1Thread: [measured 10 out of 15 rounds, threads: 1 (sequential)]
 round: 0.77 [+- 0.09], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+- 0.00], GC.calls: 51, GC.time: 0.07, time.total: 11.78, time.warmup: 4.05, time.bench: 7.73
```

Effort Notes
-----------------
- Find out what part of the programm ist really cost expensive
- change your mind. Even the programm flow has to be in a parallel manner. Dont wait for one thread to finish before calling another one.
- Cope with the slightly different way of handling results that are calculated by different threads.
- separate application logic from thread logic
