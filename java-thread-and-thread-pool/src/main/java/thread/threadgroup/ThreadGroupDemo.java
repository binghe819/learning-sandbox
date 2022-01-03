package thread.threadgroup;

public class ThreadGroupDemo {

    public static void main(String[] args) {
        Runnable printName = new PrintName();

        ThreadGroup thGroup = new ThreadGroup("Parent Thread Group");

        Thread th1 = new Thread(thGroup, printName, "th1");
        th1.start();
        Thread th2 = new Thread(thGroup, printName, "th2");
        th2.start();
        Thread th3 = new Thread(thGroup, printName, "th3");
        th3.start();

        System.out.println("activeCount : " + thGroup.activeCount());
    }

    static class PrintName implements Runnable {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getThreadGroup().getName());
        }
    }
}
