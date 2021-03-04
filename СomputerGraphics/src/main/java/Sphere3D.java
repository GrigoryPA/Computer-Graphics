import javax.swing.*;
import java.awt.*;

public class Sphere3D {
    public int[] center;
    public int radius = 20;
    public Material3D material;

    public Sphere3D(int x, int y, int z, int d, int R, int G, int B){
        center = new int[]{x,y,z};
        radius = d;
        material = new Material3D(new Color(R,G,B));
    }

    public Sphere3D(JTable table, int row){
        center = new int[]{Integer.parseInt((String) table.getValueAt(row, 0)),
                Integer.parseInt((String) table.getValueAt(row, 1)),
                Integer.parseInt((String) table.getValueAt(row, 2)),};

        radius = Integer.parseInt((String) table.getValueAt(row, 3));

        material = new Material3D(new Color(Integer.parseInt((String) table.getValueAt(row, 4)),
                Integer.parseInt((String) table.getValueAt(row, 5)),
                Integer.parseInt((String) table.getValueAt(row, 6))));
    }

    //определеяем перечекает ли луч выходящий из orig в направлении dir нашу сферу
    //решаем квадратное уравнние пересечения луча и сферы
    //если дескриминант больше нуля, то есть два пересечения, если нулю - одно пересечение
    public double IsIntersect(int[] orig, double[] dir){
        double t0;
        int[] L = new int[]{center[0]-orig[0], center[1]-orig[1], center[2]-orig[2]};//заменмть на разность векторов
        double tca = L[0]*dir[0] + L[1]*dir[1] + L[2]*dir[2];//заменить на скалярное умноженеи векторов
        double d2 = L[0]*L[0] + L[1]*L[1] + L[2]*L[2] - tca*tca;//скалярное умножение векторов
        if (d2 > radius*radius) return -1;
        double thc = Math.sqrt(radius*radius - d2);
        t0 = tca - thc;
        double t1 = tca + thc;
        if (t0 < 0) t0 = t1;
        if (t0 < 0) return -1;
        return t0;
    }



}
