import java.util.concurrent.atomic.AtomicInteger;

public class MyThread implements Runnable {

    public RayTracing3D RT;
    public int i0;
    public int i1;


    public MyThread(RayTracing3D _RT, int _i0, int _i1) {
        RT = _RT;
        i0 = _i0;
        i1 = _i1;
    }

    @Override
    public void run() {
        for (AtomicInteger j = new AtomicInteger(0); !j.weakCompareAndSetAcquire(RT.height, j.get()) ; j.incrementAndGet()) {//по y
            for (AtomicInteger i = new AtomicInteger(i0); !(i.weakCompareAndSetAcquire(i1+1,i.get())); i.incrementAndGet()) {//по х
                RT.RenderOnePixel(i.get(), j.get());
            }
        }
    }
}
