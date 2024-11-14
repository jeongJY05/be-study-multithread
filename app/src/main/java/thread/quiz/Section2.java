package thread.quiz;

import java.math.BigInteger;

public class Section2 {

    public BigInteger calculateResult(BigInteger base1, BigInteger power1, BigInteger base2, BigInteger power2) {
        BigInteger result;
        PowerCalculatingThread threadx = new PowerCalculatingThread(base1, power1);
        PowerCalculatingThread thready = new PowerCalculatingThread(base2, power2);

        // defence the exceptional case
        threadx.setDaemon(true);
        thready.setDaemon(true);

        // start the thread
        threadx.start();
        thready.start();

        // join the thread.
        try {
            threadx.join(2000);
            thready.join(2000);
        } catch (InterruptedException e) {
            // nothing
        }

        result = threadx.getResult().add(thready.getResult());

        System.out.println(result.intValue());

        return result;
    }

    private static class PowerCalculatingThread extends Thread {
        private BigInteger result = BigInteger.ONE;
        private BigInteger base;
        private BigInteger power;
        
        public PowerCalculatingThread(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }
    
        @Override
        public void run() {
            BigInteger temp = BigInteger.ONE;
            for (BigInteger i = BigInteger.ONE; power.compareTo(i) > 0; i = i.add(BigInteger.ONE)) {
                temp = temp.multiply(base);
            }
            result = temp;
        }
    
        public BigInteger getResult() { return result; }
    }
}