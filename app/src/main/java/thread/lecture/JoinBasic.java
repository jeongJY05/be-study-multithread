package thread.lecture;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JoinBasic {
    
    public void execute() {
        List<Long> inputNumbers = Arrays.asList(0L, 3435L, 35435L, 2324L, 2456L, 23L, 2435L, 5566L);

        List<FactorialThread> threads = new ArrayList<>();
        for (Long inputNumber : inputNumbers) {
            threads.add(new FactorialThread(inputNumber));
        }

        for (FactorialThread thread : threads) {
            thread.setDaemon(true);
            thread.start();
        }

        // main thread will be wait for finishing other thread's work.
        for (FactorialThread thread : threads) {
            try {
                // maximum time to wait even there has an unexpected exception
                thread.join(2000);
                // even if you set the time limit, you still have to manage some interrupt.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < inputNumbers.size(); i++) {
            FactorialThread thread = threads.get(i);
            if(thread.isFinished()) {
                System.out.println("Factorial of " + inputNumbers.get(i) + " is end");
            } else {
                System.out.println("Factorial of " + inputNumbers.get(i) + " is still in progress");
            }
            
        }
    }

    private class FactorialThread extends Thread {
        private long inputNumber;
        private BigInteger result = BigInteger.ZERO;
        private boolean isFinished = false;

        public FactorialThread(long inputNumber) {
            this.inputNumber = inputNumber;
        }

        @Override
        public void run() {
            this.result = factorial(inputNumber);
            this.isFinished = true;
        }

        private BigInteger factorial(long n) {
            BigInteger temp = BigInteger.ONE;

            for(long i = n; i> 0; i--){
                temp = temp.multiply(new BigInteger(Long.toString(i)));
            }
            return temp;
        }

        public boolean isFinished() {
            return this.isFinished;
        }

        public BigInteger getResult() {

            return this.result;
        }
    }
}
