import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class RayTracing3D {
    public static int[] orig = new int[]{0,0,0};
    public static int dirZ = 1;
    public static double fov = Math.PI/2;
    public static Vector<Sphere3D> AllSpheres;
    public static Vector<Light3D> AllLights;
    public static Sphere3D OneSphere;
    public static Light3D OneLight;
    private static double[] hit, N;
    private static Material3D material;

    static void RenderSpheres(JTable TableSpheres, JTable TableLights, Display3D display) {
        int width =  Display3D.widthImage;
        int height = Display3D.heightImage;
        AllSpheres = new Vector<Sphere3D>();
        AllLights = new Vector<Light3D>();

        for(int i=0; i<TableSpheres.getRowCount(); i++) {
            OneSphere = new Sphere3D(TableSpheres, i);
            AllSpheres.add(OneSphere);
        }

        for(int i=0; i<TableLights.getRowCount(); i++) {
            OneLight = new Light3D(TableLights, i);
            AllLights.add(OneLight);
        }

        for (int j = 0; j<height; j++) {//по y
            for (int i = 0; i<width; i++) {//по х
                double x = (2 * (i + 0.5) / width - 1) * Math.tan(fov / 2) * width / height;
                double y = -(2 * (j + 0.5) / height - 1) * Math.tan(fov / 2);
                double l = Math.sqrt(x * x + y * y + dirZ * dirZ);
                double[] dir = {x / l, y / l, dirZ / l};
                Color pixelColor = CalculateSpherePixelColor(dir);
                if(pixelColor!=null)
                    display.AddPointOnDisplayRT(i,j,pixelColor);
            }
        }
    }


    public static Color CalculateSpherePixelColor(double[] dir){
        if (SceneIntersect(orig, dir)) {
            double diffuse_light_intensity = 0;

            for (int i=0; i<AllLights.size(); i++) {
                double LDirx = AllLights.elementAt(i).position[0] - hit[0];
                double LDiry = AllLights.elementAt(i).position[1] - hit[1];
                double LDirz = AllLights.elementAt(i).position[2] - hit[2];
                double LDirl = Math.sqrt(LDirx * LDirx + LDiry * LDiry + LDirz * LDirz);
                double[] LDir = new double[]{LDirx / LDirl, LDiry / LDirl, LDirz / LDirl};
                double LDirN = LDir[0]*N[0] + LDir[1]*N[1] + LDir[2]*N[2];

                diffuse_light_intensity += AllLights.elementAt(i).intensity  * Math.max(0, LDirN);
            }
            Color resultColor = new Color((int)(Math.min(255,material.color.getRed() * diffuse_light_intensity)),
                    (int)(Math.min(255,material.color.getGreen() * diffuse_light_intensity)),
                    (int)(Math.min(255,material.color.getBlue() * diffuse_light_intensity)));
            return resultColor;// sphere color//resultColor
        }
        return null;
    }


    public static boolean SceneIntersect(int[] orig, double[] dir) {
        double spheres_dist = Double.MAX_VALUE;

        for (int i = 0; i < AllSpheres.size(); i++) {
            double dist_i = AllSpheres.elementAt(i).IsIntersect(orig, dir);
            if (dist_i!=-1 && dist_i < spheres_dist) {
                spheres_dist = dist_i;
                hit = new double[]{orig[0] + dir[0] * dist_i, orig[1] + dir[1] * dist_i, orig[2] + dir[2] * dist_i};
                double Nx = hit[0] - AllSpheres.elementAt(i).center[0];
                double Ny = hit[1] - AllSpheres.elementAt(i).center[1];
                double Nz = hit[2] - AllSpheres.elementAt(i).center[2];
                double Nl = Math.sqrt(Nx * Nx + Ny * Ny + Nz * Nz);
                N = new double[]{Nx / Nl, Ny / Nl, Nz / Nl};
                material = AllSpheres.elementAt(i).material;
            }
        }
        return spheres_dist != Double.MAX_VALUE;
    }
}
