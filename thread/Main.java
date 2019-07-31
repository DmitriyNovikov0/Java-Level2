package thread;

public class Main {
    static final int SIZE = 10000000;
    static final int H = SIZE / 2;
    static float[] arr = new float[SIZE];

    public static void main(String[] args) {
        m1();
        m2();
    }

    static void m1(){
        long a = System.currentTimeMillis();
        for (int i = 0; i < arr.length; i++){
            arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        System.out.println(System.currentTimeMillis() - a);
    }

    static void m2(){
        float[] a1 = new float[H];
        float[] a2 = new float[H];

        long a = System.currentTimeMillis();
        System.arraycopy(arr, 0, a1, 0, H);
        System.arraycopy(arr, H, a2, 0, H);

        Thread t1 = new Thread(() -> {
            for(int i = 0; i < a1.length; i++){
                a1[i] = (float)(a1[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });

        Thread t2 = new Thread(() -> {
            for(int i = 0; i < a2.length; i++){
                a2[i] = (float)(a2[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });

        t1.start();
        t2.start();

//ожидаю завершения работы потоков
        if(t1.isAlive()){
            try {
                t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (t2.isAlive()){
            try {
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.arraycopy(a1, 0, arr, 0, H);
        System.arraycopy(a2, 0, arr, H, H);

        System.out.println(System.currentTimeMillis() - a);
    }

}
