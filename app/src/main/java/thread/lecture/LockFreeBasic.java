package thread.lecture;

import java.util.concurrent.atomic.AtomicInteger;

public class LockFreeBasic {

    /**
     * Available problem using Locks
     * 
     * 1. Deadlocks
     * 2. Slow Critical Section
     *      - All threads become as slow as the slowest thread.
     * 3. Priority Inversion
     *      - Lower priority thread could excuted sooner than Higher priority.
     * 4. Kill Tolerance
     *      - If resource has not being released by any reason, all thread will be hanging.
     * 5. Performance
     *      - context switch cost. overhead happens.
     * 
     * Lock Free solution : Make operation atomic.
     *  - java.util.concurrent.atomic : offering some specific implementation.
     *  - ex. AtomicInteger
     *  - It makes calculation like 'a = a++; ' atomic.
     *    but there still race condition between 2 seperate atomic operation.
     */
    public void execute() {
        InventoryCounter inventoryCounter = new InventoryCounter();
        Thread increase = new IncrementThread(inventoryCounter);
        Thread decrese = new DecrementThread(inventoryCounter);

        try {
            increase.start();
            decrese.start();
            increase.join();
            decrese.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("We currently have " + inventoryCounter.getItems() + " items");
    }

    private class IncrementThread extends Thread {
        private InventoryCounter inventoryCounter;
        
        public IncrementThread(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            for(int i = 0; i< 10000; i++) {
                inventoryCounter.increment();
            }
        }

    }

    private class DecrementThread extends Thread {
        private InventoryCounter inventoryCounter;
        
        public DecrementThread(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            for(int i = 0; i< 10000; i++) {
                inventoryCounter.decrement();
            }
        }
    }

    private static class InventoryCounter {
        private AtomicInteger items = new AtomicInteger(0);

        public void increment() {
            items.incrementAndGet();
        }

        public void decrement() {
            items.decrementAndGet();
        }

        public int getItems() {
            return items.get();
        }
    }

}