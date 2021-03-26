import javax.swing.*;
import java.awt.*;

public class Sphere3D {
    public Vector3d center;
    public int radius = 20;
    public Material3D material;

    public Sphere3D(int x, int y, int z, int d, int R, int G, int B){
        center = new Vector3d (x,y,z);
        radius = d;
        material = new Material3D(new Color(R,G,B), new double[]{1,0}, 1);
    }

    public Sphere3D(JTable table, int row){
        center = new Vector3d (Integer.parseInt((String) table.getValueAt(row, 0)),
                Integer.parseInt((String) table.getValueAt(row, 1)),
                Integer.parseInt((String) table.getValueAt(row, 2)));

        radius = Integer.parseInt((String) table.getValueAt(row, 3));

        material = new Material3D(Integer.parseInt((String) table.getValueAt(row, 4)));
    }
    //определеяем перечекает ли луч выходящий из orig в направлении dir нашу сферу
    //решаем квадратное уравнние пересечения луча и сферы
    //если дескриминант больше нуля, то есть два пересечения, если нулю - одно пересечение
    public double IsIntersect(Vector3d orig, Vector3d dir){
        double t0;
        Vector3d L = center.getSubtraction(orig);//
        double tca = L.getScalar(dir);//
        double d2 = L.getScalar(L) - tca*tca;//
        if (d2 > radius*radius) return -1;
        double thc = Math.sqrt(radius*radius - d2);
        t0 = tca - thc;
        double t1 = tca + thc;
        if (t0 < 0) t0 = t1;
        if (t0 < 0) return -1;
        return t0;
    }



}
