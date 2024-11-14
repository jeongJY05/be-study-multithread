package thread.lecture;

public class ThreadBasic {
    
    public void execute() {
    // Thread 1: Basic
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("We are now in thread: " + Thread.currentThread().getName());
                System.out.println("Current thread priority is : " + Thread.currentThread().getPriority());
            }
        });

        thread.setName("New Worker Thread");

        thread.setPriority(Thread.MAX_PRIORITY);

        System.out.println("We are in thread: " + Thread.currentThread().getName() + " before starting a new thread");
        thread.start();
        System.out.println("We are in thread: " + Thread.currentThread().getName() + " after starting a new thread");

    // Thread 2: Exception Handling
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("We are now in thread: " + Thread.currentThread().getName());
                throw new RuntimeException("Intential Exception");
            }
        });
        thread2.setName("Misbehaving thread");

        thread2.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("A critical error happend in thread " + t.getName()
                        + " the error is " + e.getMessage());
            }
        });
        thread2.start();

    // Thread 3: define Class method
        Thread thread3 = new NewThread();
        thread3.setName("new Class thread");

        thread3.start();
    }

    private class NewThread extends Thread {
        @Override
        public void run() {
            System.out.println("Hello from : " + Thread.currentThread().getName());
        }
    }
}
