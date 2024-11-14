package thread.lecture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionVariableBasic {

    /**
     * ConditionVariable
     * 
     * Inter-thread method
     * 1. Thread.interupt();
     *   - it makes the thread terminate.
     * 2. Thread.join();
     *   - it makes the A thread wait until counterpart thread(B) finish it's procss.
     *   - After finishing, the counterpart thread(B) wakes up the A thread. 
     * 3. Semaphore.
     *   - semaphore.acquire() makes the thread wait until semaphore being released
     *   - after semaphore.release() wakes up the threads which wait for the semaphore.
     * 
     * 4. Condition Variable
     *   - associated with a lock: thoese are used with lock.lock() and lock.unlock()
     *   - condition.signal(), condition.await(), condition.signalAll()
     * 
     * 5. Object Signalling
     *   - very similar to Condition variable
     *   - it used with synchronized(object) {}.
     *   - object.wait(), object.notify(), object.notifyAll();
     * 
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