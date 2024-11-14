package thread.lecture;

public class ConditionBasic {

    // way to make our function atomic?
    // only atomic actions could operate successfully in this class.
    // answer is this: make a critical section, in other word, using Locking.
    /**
     * Synchronized : Locking mechanism
     *  - In one object, all of method having synchronize keyword shares the usage status.
     * 
     *  1. method level
     *    - If one thread wants to use one of the synchronized method, 
     *      should wait until other thread finishes processing synchronized method.
     * 
     *  2. section level
     *    - Making an object to be act as Lock.
     *    - Within the object, synchronized section can be used by only one thread.
     *    - It is more flexible than method level
     */
    public void execute() {
        InventoryCounter inventoryCounter = new InventoryCounterSectionLevel();
        // InventoryCounter inventoryCounter = new InventoryCounterMethod();
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

    // 1. method level
    private static class InventoryCounterMethod implements InventoryCounter {
        private int items = 0;

        public synchronized void increment() {
            items++;
        }

        public synchronized void decrement() {
            items--;
        }

        public synchronized int getItems() {
            return items;
        }
    }

    // 2. section level
    private static class InventoryCounterSectionLevel implements InventoryCounter {
        private int items = 0;

        Object lock = new Object();

        public void increment() {
            synchronized(this.lock) {
                items++;
            }
        }

        public void decrement() {
            synchronized(this.lock) {
                items--;
            }
        }

        public int getItems() {
            synchronized(this.lock) {
                return items;
            }
        }
    }

    public interface InventoryCounter {
        public abstract void increment();
        public abstract void decrement();
        public abstract int getItems();
    }

}
