public class App2 {

    public static void main(String[] args) {
        Thread thread = new NewThread();
        thread.start();
    }

    private static class NewThread extends Thread {

        @Override
        public void run() {
            System.out.println("Hello from " + this.getName());
            System.out.println("My priority is " + this.getPriority());
        }
    }

}
