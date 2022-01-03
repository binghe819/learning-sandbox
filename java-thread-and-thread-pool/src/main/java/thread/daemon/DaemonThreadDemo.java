package thread.daemon;

public class DaemonThreadDemo implements Runnable{

    static boolean autoSave = false;

    public static void main(String[] args){
        Thread th = new Thread(new DaemonThreadDemo());
        th.setDaemon(true); // 데몬스레드 설정. ( 이 부분이 없으면 종료되지 않는다. )
        th.start();

        for(int i = 1; i <= 10; i++){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println(i);

            if(i == 5)
                autoSave = true;
        }

        System.out.println("프로그램을 종료합니다.");
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(3*1000); // 3초마다
            } catch (InterruptedException e) { e.printStackTrace(); }

            if (autoSave){
                autoSave();
            }
        }
    }

    public void autoSave() {
        System.out.println("작업파일이 자동저장되었습니다.");
    }
}
