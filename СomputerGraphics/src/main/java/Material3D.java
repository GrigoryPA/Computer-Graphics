import java.awt.*;

public class Material3D {
    public Color color;
    public double[] albedo = new double[]{1,0};
    public double specularExponent;

    public Material3D(Color color1, double[] alb, double specular){
        color = color1;
        albedo=alb;
        specularExponent=specular;
    }

    public Material3D(){
    }
}
