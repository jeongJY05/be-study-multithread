package thread.lecture;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class ReentrantReadWriteLockBasic {
    /**
     * ReentrantReadWriteLock 
     * Provide the minimum locking that locks within reader threads but only to writer thread.
     * Reader Locking blocking the Writer thread to write in DB but allows other reader thread to refer.
     */
    private final int HIGHST_PRICE = 1000;
    public void execute() {
        InventoryDatabase db = new InventoryDatabase();

        // insert the prepared data
        Random random = new Random();
        for(int i = 0; i < 1000000; i++) {
            db.addItem(random.nextInt(HIGHST_PRICE));
        }

        Thread writer = new Thread(() -> {
            while(true) {
                db.addItem(random.nextInt(HIGHST_PRICE));
                db.removeItem(random.nextInt(HIGHST_PRICE));

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        writer.setDaemon(true);
        writer.start();

        int numberOfReaderThread = 3;
        List<Thread> readers = new ArrayList<>(6);
        for (int readerIndex = 0; readerIndex < numberOfReaderThread; readerIndex++) {
            Thread thread = new Thread(() -> {
                for (int i = 0; i < 1000000; i++) {
                    int upperBoundPrice = random.nextInt(HIGHST_PRICE);
                    int lowerBoundPrice = upperBoundPrice > 0 ?  random.nextInt(upperBoundPrice) : 0;
                    db.getNumberOfItemsInPriceRange(lowerBoundPrice, upperBoundPrice);
                }
            });
            thread.setDaemon(true);
            readers.add(thread);
        }

        long startReadingTime = System.currentTimeMillis();

        for (Thread reader : readers) {
            reader.start();
        }
        for (Thread reader : readers) {
            try {
                reader.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endReadingTime = System.currentTimeMillis();

        System.out.println(String.format("Reading took %d ms", endReadingTime - startReadingTime));
    }

    private class InventoryDatabase {
        private TreeMap<Integer, Integer> priceToCountMap = new TreeMap<>();
        private ReentrantLock lock = new ReentrantLock();
        private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        private Lock readLock = reentrantReadWriteLock.readLock();
        private Lock writeLock = reentrantReadWriteLock.writeLock();


        public int getNumberOfItemsInPriceRange(int lowerBound, int upperBound) {
           readLock.lock();
           try {
            Integer fromKey = priceToCountMap.ceilingKey(lowerBound);
            Integer toKey = priceToCountMap.floorKey(upperBound);

            if(fromKey == null || toKey == null) {
                return 0;
            }

            NavigableMap<Integer, Integer> rangePrices
             = priceToCountMap.subMap(fromKey, true, toKey, true);

            int sum = 0;
            for (int numberOfItemsForPrice : rangePrices.values()) {
                sum += numberOfItemsForPrice;                
            }
            return sum;
           } finally {
            readLock.unlock();
           }
        }

        public void addItem(int price) {
            writeLock.lock();
            try{
                Integer numberOfItemsForPrice = priceToCountMap.get(price);
                if(numberOfItemsForPrice == null ){
                    priceToCountMap.put(price, 1);
                } else {
                    priceToCountMap.put(price, numberOfItemsForPrice+1);
                }
            } finally {
                writeLock.unlock();
            }
        }

        public void removeItem(int price) {
            writeLock.lock();
            try {
                Integer numberOfItemsForPrice = priceToCountMap.get(price);
                if(numberOfItemsForPrice == null || numberOfItemsForPrice == 1 ){
                    priceToCountMap.remove(price);
                } else {
                    priceToCountMap.put(price, numberOfItemsForPrice - 1);
                }
            } finally {
                writeLock.unlock();
            }
        }
    }

}
