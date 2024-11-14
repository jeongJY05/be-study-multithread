package thread.lecture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SemaphoreBasic {

    /**
     * SemaphoreBasic
     * permit and authoring. it could restrict the number of users.
     * Unlike the lock that is able to allow only one thread, semaphore can allow to multiple threads.
     * 
     * - Semaphore dosn't have a notion of resource owner thread
     * - Same thread can acquire the permit to resource multiple times.
     * - Semaphore can be released by any thread even that thread haven't acquired it.
     */
    public void execute() {
        int numberOfThreads = 10; //or any number you'd like 
 
        List<Thread> threads = new ArrayList<>();
    
        Barrier barrier = new Barrier(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            threads.add(new Thread(new CoordinatedWorkRunner(barrier)));
        }
    
        for(Thread thread: threads) {
            thread.start();
        }
    }


    public class Barrier {
        private final int numberOfWorkers;
        private final Semaphore semaphore = new Semaphore(0);
        private int counter = 0;
        private final Lock lock = new ReentrantLock();
    
        public Barrier(int numberOfWorkers) {
            this.numberOfWorkers = numberOfWorkers;
        }
    
        public void waitForOthers() throws InterruptedException {
            lock.lock();
            boolean isLastWorker = false;
            try {
                counter++;
    
                if (counter == numberOfWorkers) {
                    isLastWorker = true;
                }
            } finally {
                lock.unlock();
            }
    
            if (isLastWorker) {
                semaphore.release(numberOfWorkers-1);
            } else {
                semaphore.acquire();          
            }
        }
    }
 
    public class CoordinatedWorkRunner implements Runnable {
        private final Barrier barrier;
    
        public CoordinatedWorkRunner(Barrier barrier) {
            this.barrier = barrier;
        }
    
        @Override
        public void run() {
            try {
                task();
            } catch (InterruptedException e) {
            }
        }
    
        private void task() throws InterruptedException {
            // Performing Part 1
            System.out.println(Thread.currentThread().getName() 
                    + " part 1 of the work is finished");
    
            barrier.waitForOthers();
    
            // Performing Part2
            System.out.println(Thread.currentThread().getName() 
                    + " part 2 of the work is finished");
        }
    }
}