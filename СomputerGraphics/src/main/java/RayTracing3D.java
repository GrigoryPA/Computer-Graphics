import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class RayTracing3D {
    public static int[] orig = new int[]{0,0,0};
    public static int dirZ = 1;
    public static double fov = Math.PI/2;
    public static Vector<Sphere3D> AllSpheres;
    public static Sphere3D OneSphere;

    static void RenderSpheres(JTable Table, Display3D display) {
        int width =  Display3D.widthImage;
        int height = Display3D.heightImage;
        AllSpheres = new Vector<Sphere3D>();

        for(int i=0; i<Table.getRowCount(); i++) {
            OneSphere = new Sphere3D(
                    Integer.parseInt((String) Table.getValueAt(i, 0)),
                    Integer.parseInt((String) Table.getValueAt(i, 1)),
                    Integer.parseInt((String) Table.getValueAt(i, 2)),
                    Integer.parseInt((String) Table.getValueAt(i, 3)),
                    Integer.parseInt((String) Table.getValueAt(i, 4)),
                    Integer.parseInt((String) Table.getValueAt(i, 5)),
                    Integer.parseInt((String) Table.getValueAt(i, 6)));
            AllSpheres.add(OneSphere);
        }

        for (int j = 0; j<height; j++) {//по y
            for (int i = 0; i<width; i++) {//по х
                double x = (2 * (i + 0.5) / width - 1) * Math.tan(fov / 2) * width / height;
                double y = -(2 * (j + 0.5) / height - 1) * Math.tan(fov / 2);
                double l = Math.sqrt(x * x + y * y + dirZ * dirZ);
                double[] dir = {x / l, y / l, dirZ / l};
                double sphere_dist = Double.MAX_VALUE;
                for (int q = 0; q < AllSpheres.size(); q++) {
                    if (AllSpheres.elementAt(q).IsIntersect(orig, dir, sphere_dist) == true)
                        display.AddPointOnDisplayRT(i, j, AllSpheres.elementAt(q).material.color); // sphere color
                }
            }
        }
    }
}
