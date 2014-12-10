psychic-thread-adventure
========================
What's the difference between sorting different counts of numbers using multiple threads and sorting them single threaded, in terms of time needed to sort the numbers?

This project discovers how threading influences performance of simple algorithms like Mergesort algorithm. Besides that the effort will be documented what needs to be done to make a program run with multiple threads w/o using frameworks. Maybe some tests will be done using Java 8 later on.

Inspired by Robert C. Martin's (et al.) Clean Code, 2009, Pearson Education, Inc, chapter on concurrency.

Micro Benchmarking libraries
-----------------
* https://code.google.com/p/caliper/source/browse/tutorial/Tutorial.java
* http://perf4j.codehaus.org/devguide.html

*Results*
TeX These constellations with different `n`s and on different machines
Mergesort w/o threading
M. with few threads (fix number of threads)
M. with dynamically created threads (no fixed number)
(probably) M. written in functional style, oriented towards http://stackoverflow.com/questions/24855746/understanding-when-and-how-to-use-java-8-lambdas
