package de.bschandera.tryouts;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.routing.RoundRobinRouter;
import akka.util.Duration;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.singletonList;

public class MergesortThreadedWithAkka {
    private final int nrOfWorkers = 4;

    public static void main(String[] args) {
        new MergesortThreadedWithAkka().sort(nRandomNumbers(10));
    }

    private static List<Integer> nRandomNumbers(int n) {
        List<Integer> result = new ArrayList<>(n);
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            result.add(random.nextInt());
        }
        return result;
    }

    /**
     * Sort the given {@linkplain Integer}s with Mergesort algorithm.
     *
     * @param unsorted
     * @return All {@linkplain Integer}s in ascending order.
     */
    public List<Integer> sort(Iterable<Integer> unsorted) {
        // Create an Akka system
        ActorSystem system = ActorSystem.create("MergesortSystem");
        system.actorSelection("akka.tcp://app@10.100.100.234:2552/user/MergesortSystem/master");

        // create the result listener, which will print the result and shutdown the system
        final ActorRef listener = system.actorOf(new Props(Listener.class), "listener");

        // create the master
        ActorRef master = system.actorOf(new Props(new UntypedActorFactory() {
            @Override
            public Actor create() throws Exception {
                return new Master(nrOfWorkers, listener);
            }
        }), "master");

        // start the calculation
        master.tell(new SortPlease(unsorted));

        // todo how can I retrieve mergesort result?
        return Collections.emptyList();
    }

    public static class Listener extends UntypedActor {
        @Override
        public void onReceive(Object message) throws Exception {
            System.out.println(this.toString());
            if (message instanceof Sorted) {
                printOutResultData((Sorted) message);
                getContext().system().shutdown();
            } else {
                unhandled(message);
            }
        }

        private static void printOutResultData(Sorted message) {
            String firstNumbers = firstNumbersOf(message.getList());
            System.out.println(String.format("\n\tSorted list, first numbers:\t%s\n" +
                    "\tSize of result array:\t\t%s\n" +
                    "\tCalculation time:\t\t\t%s\n", firstNumbers, message.getList().size(), message.getDuration()));
        }

        private static String firstNumbersOf(Iterable<Integer> sorted) {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < 10 && i < Iterables.size(sorted); i++) {
                result.append(Iterables.get(sorted, i)).append(" ");
            }
            return result.toString();
        }

    }

    /* Besides the master actor there is always a master listener who's awaiting the result from this actor */
    private static class Master extends UntypedActor {
        private final long start = System.currentTimeMillis();
        private final ActorRef listener;
        private final ActorRef workerRouter;
        private final Stack<Merged> stack;

        private int originalSize;

        public Master(final int nrOfWorkers, ActorRef listener) {
            this.listener = listener;
            stack = new Stack<>();
            workerRouter = this.getContext().
                    actorOf(new Props(Worker.class).
                            withRouter(new RoundRobinRouter(nrOfWorkers)), "workerRouter");
        }

        @Override
        public void onReceive(Object message) throws Exception {
            System.out.println(this.toString());
            if (message instanceof SortPlease) {
                List<Integer> work = ((SortPlease) message).getWork();
                originalSize = work.size();
                sendMergeRequestsForEveryTwoNumbers(work);
            } else if (message instanceof Merged) {
                returnOrNextMerge((Merged) message);
            } else {
                unhandled(message);
            }
        }

        private void sendMergeRequestsForEveryTwoNumbers(List<Integer> work) {
            if (work.size() % 2 == 0) {
                for (int i = 0; i < work.size(); i = i + 2) {
                    workerRouter.tell(new MergePlease(singletonList(work.get(i)), singletonList(work.get(i + 1))), getSelf());
                }
            } else {
                for (int i = 0; i < work.size(); i = i + 2) {
                    workerRouter.tell(new MergePlease(singletonList(work.get(i)), Collections.<Integer>emptyList()), getSelf());
                }
            }
        }

        private void returnOrNextMerge(Merged message) {
            if (message.getList().size() == originalSize) {
                listener.tell(
                        new Sorted(message.getList(), Duration.create(System.currentTimeMillis() - start, TimeUnit.MILLISECONDS)),
                        getSelf());
                getContext().stop(getSelf());
            } else {
                if (!stack.isEmpty()) {
                    workerRouter.tell(new MergePlease(stack.pop().getList(), message.getList()), getSelf());
                } else {
                    stack.push(message);
                }
            }
        }

    }

    public static class Worker extends UntypedActor {
        @Override
        public void onReceive(Object message) throws Exception {
            System.out.println(this.toString());
            if (message instanceof MergePlease) {
                MergePlease mergePlease = (MergePlease) message;
                System.out.println("size of left list: " + mergePlease.getLeft().size());
                System.out.println("size of right list: " + mergePlease.getRight().size());
                System.out.println();
                List<Integer> result = merge(mergePlease.getLeft(), mergePlease.getRight());
                getSender().tell(new Merged(result), getSelf());
            } else {
                unhandled(message);
            }
        }

        private static List<Integer> merge(List<Integer> left, List<Integer> right) {
            // create new objects to allow element removal
            left = new ArrayList<>(left);
            right = new ArrayList<>(right);
            List<Integer> result = new ArrayList<>();

            while (left.size() > 0 && right.size() > 0) {
                if (left.get(0) <= right.get(0)) {
                    result.add(left.get(0));
                    left.remove(0);
                } else {
                    result.add(right.get(0));
                    right.remove(0);
                }
            }

            if (left.size() != 0) {
                for (Integer number : left) {
                    result.add(number);
                }
            } else {
                for (Integer number : right) {
                    result.add(number);
                }
            }
            return result;
        }

    }

    private static class SortPlease {
        private final List<Integer> work;

        public SortPlease(Iterable<Integer> work) {
            this.work = ImmutableList.copyOf(work);
        }

        public List<Integer> getWork() {
            return ImmutableList.copyOf(work);
        }
    }

    private static class MergePlease {
        final List<Integer> left;
        final List<Integer> right;

        public MergePlease(List<Integer> left, List<Integer> right) {
            this.left = ImmutableList.copyOf(left);
            this.right = ImmutableList.copyOf(right);
        }

        public List<Integer> getRight() {
            return ImmutableList.copyOf(right);
        }

        public List<Integer> getLeft() {
            return ImmutableList.copyOf(left);
        }

    }

    private static class Merged {
        final List<Integer> sortedList;

        public Merged(List<Integer> sortedList) {
            this.sortedList = ImmutableList.copyOf(sortedList);
        }

        public List<Integer> getList() {
            return ImmutableList.copyOf(sortedList);
        }

    }

    private static class Sorted {
        final List<Integer> sortedList;
        final Duration duration;

        public Sorted(List<Integer> sortedList, Duration duration) {
            this.sortedList = ImmutableList.copyOf(sortedList);
            this.duration = duration;
        }

        public List<Integer> getList() {
            return ImmutableList.copyOf(sortedList);
        }

        public Duration getDuration() {
            return duration;
        }

    }

}
