import java.awt.*;


public class Material3D {
    public Vector3d color;
    public double[] albedo = new double[]{1,0,0,0};
    //первое - способность материала передавать свой цвет
    //второе - способность материала отражать свет
    //третье - кеф передачи цвета отраженного как на зеркале
    //для норм зеркала рекомендую отключать цветопередачу, сделать норм отражение и зеркальность
    public double specularExponent;
    //степень отражения света
    public double refractive;

    public enum MaterialType
    {
        STEEL,
        REDWOOD,
        MIRROR,
        GLASS
    }


    public Material3D(Color color1, double[] alb, double specular){
        color = new Vector3d(color1.getRed(),
                color1.getGreen(),
                color1.getBlue());
        albedo=alb;
        specularExponent=specular;
    }

    public Material3D(MaterialType m) {
        switch (m){
            case STEEL://steel
                color = new Vector3d(0.4,0.4,0.3);
                albedo = new double[]{0.6,0.3,0.1,0};
                specularExponent=50;
                refractive = 1;
                break;
            case REDWOOD://red
                color = new Vector3d(0.3,0.1,0.1);
                albedo = new double[]{0.9,0.1,0,0};
                specularExponent=10;
                refractive = 1;
                break;
            case MIRROR://mirror
                color = new Vector3d(1,1,1);
                albedo = new double[]{0,10,0.8,0};
                specularExponent=1425;
                refractive = 1;
                break;
            case GLASS://glass
                color = new Vector3d(0.6,0.7,0.8);
                albedo = new double[]{0,0.5,0.1,0.8};
                specularExponent=125;
                refractive = 1.5;
                break;
        }
    }

    public Material3D(int m) {
        switch (m){
            case 1://steel
                color = new Vector3d(0.4,0.4,0.3);
                albedo = new double[]{0.6,0.3,0.1,0};
                specularExponent=50;
                refractive = 1;
                break;
            case 2://red
                color = new Vector3d(0.3,0.1,0.1);
                albedo = new double[]{0.9,0.1,0,0};
                specularExponent=10;
                refractive = 1;
                break;
            case 3://mirror
                color = new Vector3d(1,1,1);
                albedo = new double[]{0,10,0.8,0};
                specularExponent=1425;
                refractive = 1;
                break;
            case 4://glass
                color = new Vector3d(0.6,0.7,0.8);
                albedo = new double[]{0,0.5,0.1,0.8};
                specularExponent=125;
                refractive = 1.5;
                break;
        }
    }


}
