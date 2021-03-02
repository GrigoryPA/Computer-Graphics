import javax.swing.*;
import java.awt.*;

public class Sphere3D {
    public static int[] center;
    public static int radius = 20;

    public Sphere3D(int x, int y, int z, int r){
        center = new int[]{x,y,z};
        radius = r;
    }

    //определеяем перечекает ли луч выходящий из orig в направлении dir нашу сферу
    //решаем квадратное уравнние пересечения луча и сферы
    //если дескриминант больше нуля, то есть два пересечения, если нулю - одно пересечение
    static boolean IsIntersect(int[] orig, double[] dir, double t0) {
        int[] L = new int[]{center[0]-orig[0], center[1]-orig[1], center[2]-orig[2]};//заменмть на разность векторов
        double tca = L[0]*dir[0] + L[1]*dir[1] + L[2]*dir[2];//заменить на скалярное умноженеи векторов
        double d2 = L[0]*L[0] + L[1]*L[1] + L[2]*L[2] - tca*tca;//скалярное умножение векторов
        if (d2 > radius*radius) return false;
        double thc = Math.sqrt(radius*radius - d2);
        t0 = tca - thc;
        double t1 = tca + thc;
        if (t0 < 0) t0 = t1;
        if (t0 < 0) return false;
        return true;
    }



    static Color CalculateRayLandingPoint(int[] orig, double[] dir, Sphere3D sphere) {
        double sphere_dist = Double.MAX_VALUE;
        if (Sphere3D.IsIntersect(orig, dir, sphere_dist)==false)
            return Color.BLACK; // background color
        else
            return Color.CYAN; // Sphere color
    }

}
