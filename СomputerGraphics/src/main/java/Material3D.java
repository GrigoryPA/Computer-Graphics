import java.awt.*;

public class Material3D {
    public Color color;
    public double[] albedo = new double[]{1,0,0};
    //первое - способность материала передавать цвет
    //второе - способность материала отражать свет
    //третье - кеф передачи цвета отраженного как на зеркале
    //для норм зеркала рекомендую отключать цветопередачу, сделать норм отражение и зеркальность
    public double specularExponent;
    //степень отражения света

    public Material3D(Color color1, double[] alb, double specular){
        color = color1;
        albedo=alb;
        specularExponent=specular;
    }

    public Material3D(int m) {
        switch (m){
            case 1://steel
                color = new Color(40,40,50);
                albedo = new double[]{1,50,1};
                specularExponent=70;
                break;
            case 2://wood
                color = new Color(50,50,20);
                albedo = new double[]{1,0.5,0};
                specularExponent=10;
                break;
            case 3://mirror
                color = new Color(255,255,255);
                albedo = new double[]{0,100,10};
                specularExponent=1500;
                break;
        }
    }
}
