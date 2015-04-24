package de.bschandera.tryouts;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinRouter;
import akka.util.Duration;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.singletonList;

/**
 * That class must be run on every remote machine. Remote machines should be callable from a listener/master machine.
 */
public class MergesortThreadedWithAkkaRemote implements akka.kernel.Bootable {
    private final ActorSystem system = ActorSystem.create("MergesortSystem");

    @Override
    public void startup() {
        system.actorOf(new Props(Master.class), "master").tell("start", null);
    }

    @Override
    public void shutdown() {
        system.shutdown();
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
