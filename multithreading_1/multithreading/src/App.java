public class App {
    public static void main(String[] args) throws Exception {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("We are now in thread: " + Thread.currentThread().getName());
                System.out.println("Current thread priority is " + Thread.currentThread().getPriority());
                throw new RuntimeException("Intentional Exception thrown!");
            }
        });

        thread.setName("Misbehaving thread");
        thread.setPriority(Thread.MAX_PRIORITY);

        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println(
                        "A critical error happened in thread " + t.getName() + " the error is " + e.getMessage());
            }
        });

        System.out.println("We are in thread: " + Thread.currentThread().getName() + " before starting a new thread");
        thread.start();
        System.out.println("We are in thread: " + Thread.currentThread().getName() + " after starting a new thread");

        Thread.sleep(10000);
    }
}
