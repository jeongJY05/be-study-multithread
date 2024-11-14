package thread.lecture;

import java.util.Random;

public class VolatileBasic {

    /**
     * which operations are atomic?
     * 1. All reference and assignment are atomic
     *   Object b = new Object();
     *   Object a = b             <--- this!!
     *     - refering b is not only atomic, but assign to a is also atomic.
     * 
     * 2. All assignments to primitive types are safe.
     *    Except long and double;
     *    - read from or writing to thoes type : int, short, byte, float, char, boolean
     *    - placing volatile keyword to long or double make also make it atomic.
     * 
     * 3. Classes in java.util.concurrent.atomic
     * 
     */
    public void execute() {
        Metrics metric = new Metrics();

        Thread printer = new MetricsPrinter(metric);


        Thread businessLogic1 = new BusinessLogic(metric);
        Thread businessLogic2 = new BusinessLogic(metric);

        businessLogic1.start();
        businessLogic2.start();
        printer.start();
    }

    private class MetricsPrinter extends Thread {
        private Metrics metric;

        public MetricsPrinter(Metrics metric) {
            this.metric = metric;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                double currentAverage = metric.getAverage();

                System.out.println("Current Average is" + currentAverage);
            }

        }
    }

    private class BusinessLogic extends Thread {
        Metrics metric;
        private Random random = new Random();
        
        public BusinessLogic(Metrics metric) {
            this.metric = metric;
        }

        @Override
        public void run() {
            while(true) {
                long start = System.currentTimeMillis();

                try {
                    Thread.sleep(random.nextInt(10));
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                long end = System.currentTimeMillis();

                metric.addSample(end-start);
            }
        }

    }
    private class Metrics {
        private long count = 0;
        private volatile double average = 0.0;

        public synchronized void addSample(long sample) {
            double currentSum = average * count;
            count++;
            average = (currentSum + sample) / count;
        }

        public double getAverage() {
            return average;
        }

    }

}
