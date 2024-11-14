package thread.lecture;

import java.math.BigInteger;

public class InterruptBasic {
    
    public void execute() {
        /**
         * Interrupt Case
         * 1. thrown InterruptedException
         * 2. Interrupt signal explicity  
         */

        // Thread 1 : Intrrupt a Thread by throwing InterruptedException
        Thread thread1 = new Thread(new BlockingTask());
        thread1.start();
        thread1.interrupt();

        // // Thread2: Interrupt a Thread by adding interrupt signal explicity
        // Thread thread2 = new Thread(new LongComputationTask(new BigInteger("2000000000"), new BigInteger("100000000000")));
        // thread2.start();
        // thread2.interrupt(); // Do not interrupt successfully

        // Thread3: With Daemon, we don't need to interrupt sign explictiy
        // Thread thread3 = new Thread(new DaemonTask(new BigInteger("2000000000"), new BigInteger("100000000000")));
        // thread3.setDaemon(true);
        // thread3.start();
        // try {
        //     thread3.sleep(100);
        // } catch (InterruptedException e) {
        // }
        // thread3.interrupt(); // Do not interrupt successfully
    }

    private class BlockingTask implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(50000);
            } catch (InterruptedException e) {
                // System.out.println("Exting blocking thread");
            }
        }
    }

    private abstract class LongComputationTask implements Runnable {
        private BigInteger base;
        private BigInteger power;

        public LongComputationTask(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            System.out.println(base + "^" + power+" = " + pow(base, power));
        }

        public BigInteger pow(BigInteger base, BigInteger power) {
            BigInteger result = BigInteger.ONE;
            for(BigInteger i = BigInteger.ZERO; i.compareTo(power)!=0; i=i.add(BigInteger.ONE)) {
                if(Thread.currentThread().interrupted()) {
                    System.out.println("Prematurely interrupted computation");
                    return BigInteger.ZERO;
                }
                result = result.multiply(base);
            }
            return result;
        }
    }

    private class DaemonTask extends LongComputationTask {

        public DaemonTask(BigInteger base, BigInteger power) {
            super(base, power);
        }

        @Override
        public BigInteger pow(BigInteger base, BigInteger power) {
            BigInteger result = BigInteger.ONE;
            for(BigInteger i = BigInteger.ZERO; i.compareTo(power)!=0; i=i.add(BigInteger.ONE)) {
                result = result.multiply(base);
            }
            return result;
        }
    }
}
