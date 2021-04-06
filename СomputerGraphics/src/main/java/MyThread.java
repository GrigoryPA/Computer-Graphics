public class MyThread extends Thread{
    public RayTracing3D RT;
    public int i;
    public int j;
    static public int semaforik = 4;

    public MyThread(RayTracing3D _RT, int _i, int _j){
        RT=_RT;
        i=_i;
        j=_j;
    }



    public void run() {
        --semaforik;
        RT.RenderOnePixel(i, j);
        ++semaforik;
    }
}
