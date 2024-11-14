package thread.lecture;

import java.util.Random;

public class LockingStrategeBasic {

    /**
     * Types of locking
     *  1. Fine-grained Locking
     *    - multiple Lockings for one resource unit.
     *    - it is easy to occur a deadlock
     *  2. Coarse Grainded Locking
     *    - single Locking for whole one resource unit.
     * 
     * Deadlock
     *  - stuck because of waiting for each other.
     *  - condition
     *    - Mutual Exclusion: only one thread can have exclusive access to a resource
     *    - Hold and wait: more than one threads is holding a resource and waiting for another resource.
     *    - Non-preemptive allocation: It is not allowed to take away the resource from other thread.
     *    - Circular wait : chain of at least two thread wait for each other.
     * 
     *  - solution
     *    - avoid circular wait by enforcing the order. get same locking order within multiple method.
     *    - watchdog : deadlock detection
     *      : writing onto register on every step in thread and function.
     *        Watchdock monitors it's update span, and detect the deadlock status.  
     *    - thread interrupt
     *      : stop the thread in deadlock.
     *    - tryLock operation 
     *      : check the state of lock before locking
     */

     public void execute() {
        Intersection intersection = new Intersection();
        // InventoryCounter inventoryCounter = new InventoryCounterMethod();
        Thread trainAThread = new Thread(new TrainA(intersection));
        Thread trainBThread = new Thread(new TrainB(intersection));

        trainAThread.start();
        trainBThread.start();
    }

    public class TrainA implements Runnable {
        private Intersection intersection;
        private Random random = new Random();

        public TrainA(Intersection intersection) {
            this.intersection = intersection;
        }

        @Override
        public void run() {
           long sleepTime = random.nextInt(50);

            while (true) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
                intersection.takeRoadA(); 
            }
        }
    }

    public class TrainB implements Runnable {
        private Intersection intersection;
        private Random random = new Random();

        public TrainB(Intersection intersection) {
            this.intersection = intersection;
        }

        @Override
        public void run() {
           while (true) {
                long sleepTime = random.nextInt(50);

            try {
                Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                intersection.takeRoadB();    
           }
        }
    }

    public class Intersection {
        private Object roadA = new Object();
        private Object roadB = new Object();

        public void takeRoadA() {
            synchronized(roadA) {
                System.out.println("Road A is locked by thread" + Thread.currentThread().getName());

                synchronized(roadB) {
                    System.out.println("Train is passing through road A");
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }    
            }
        }

        public void takeRoadB() {
            synchronized(roadB) {
                System.out.println("Road B is locked by thread" + Thread.currentThread().getName());

                synchronized(roadA) {
                    System.out.println("Train is passing through road B");
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }    
            }
        }
    }

}
