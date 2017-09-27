package sample;

public class WaitForTarget implements Runnable {
    Main main;

    public WaitForTarget(Main main) {
        this.main = main;
    }

    @Override
    public void run() {
        while (!Cloning.foundTarget) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        main.markPath();
    }
}
