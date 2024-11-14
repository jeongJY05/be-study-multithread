package thread.lecture;

public class DataConditionBasic {

    /**
     * Race condition?
     * What we have learn in ConditionBasic.
     * -> The non atomic operation performed on the shared resource   
     * Condition when multiple threads are accessing a shared resource.
     * At least one thread is modifying the resource
     * The timing of thread's scheduling may caus incorrect results.
     * 
     * Data condition
     * -> It happens because CPU reorganize the task itself to maximize the processing performance.
     * Unlikely race condition, it happens only one thread modifies the resource.
     * 
     * Solutions
     *  1. Synchronization of methods withch modify shared variables
     *  2. Declaration of shared variables with the volatile keyword.
     *   - volatile keyword solves all data races by guaanteeing order.
     */
    public void execute() {
        SharedClass sheardClass = new SharedClass();
        Thread thread1 = new Thread(() -> {
            for(int i=0;i< 1000; i++) {
                sheardClass.increment();
            }
        });

        Thread thread2 = new Thread(() -> {
            for(int i=0;i< 1000; i++) {
                sheardClass.checkForDataRace();
            }
        });

        thread1.start();
        thread2.start();
    }

    private class SharedClass {
        private volatile int x = 0;
        private volatile int y = 0;

        public void increment() {
            x++;
            y++;
        }

        public void checkForDataRace() {
            if(y>x) {
                System.out.println("y > x - Data Race is detacted");
            }
        }

    }

}
