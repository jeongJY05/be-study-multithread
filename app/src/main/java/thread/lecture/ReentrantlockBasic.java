package thread.lecture;

import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;
import java.util.concurrent.locks.Lock;

public class ReentrantlockBasic {

    // ReentrantLock support many convinent features.
    public void execute() {
    }

    private class PriceUpdater extends Thread {
        PricesContainer container;
        Random random = new Random();

        public PriceUpdater(PricesContainer pricesContainer) {
            this.container = pricesContainer;
        }

        @Override
        public void run() {
            container.getLockObject().lock();

            try {
                container.setBitcoinPrice(random.nextInt(20000));
                container.setEtherPrice(random.nextInt(2000));
                container.setLitecoinPrice(random.nextInt(500));
                container.setBitcoinCashPrice(random.nextInt(5000));
                container.setRipplePrice(random.nextDouble());
            } finally {
                container.getLockObject().unlock();
            }

            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Virtual coin container
     */
    private class PricesContainer {
        private Lock lockObject = new ReentrantLock();
        private double bitcoinPrice;
        private double etherPrice;
        private double litecoinPrice;
        private double bitcoinCashPrice;
        private double ripplePrice;

        public double getBitcoinPrice() {
            return bitcoinPrice;
        }
        public double getEtherPrice() {
            return etherPrice;
        }
        public double getLitecoinPrice() {
            return litecoinPrice;
        }
        public double getBitcoinCashPrice() {
            return bitcoinCashPrice;
        }
        public double getRipplePrice() {
            return ripplePrice;
        }
        public Lock getLockObject() {
            return lockObject;
        }
        public void setBitcoinPrice(double bitcoinPrice) {
            this.bitcoinPrice = bitcoinPrice;
        }
        public void setEtherPrice(double etherPrice) {
            this.etherPrice = etherPrice;
        }
        public void setLitecoinPrice(double litecoinPrice) {
            this.litecoinPrice = litecoinPrice;
        }
        public void setBitcoinCashPrice(double bitcoinCashPrice) {
            this.bitcoinCashPrice = bitcoinCashPrice;
        }
        public void setRipplePrice(double ripplePrice) {
            this.ripplePrice = ripplePrice;
        }
    }

}
