import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class RayTracing3D {
    public static double[] camera = new double[]{0,0,0};
    public static int dirZ = 1;
    public static double fov = Math.PI/2;
    public static Vector<Sphere3D> AllSpheres;
    public static Vector<Light3D> AllLights;
    public static Sphere3D OneSphere;
    public static Light3D OneLight;
    private static double[] hit, N;
    private static Material3D material;
    private static double[] shadow_pt, shadow_N;
    private static Material3D shadowMaterial;
    private static int maxDepth=1;

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
                Color pixelColor = CalculateSpherePixelColor(camera, dir, 0);
                if(pixelColor!=null)
                    display.AddPointOnDisplayRT(i,height-j,pixelColor);
            }
        }
    }


    public static Color CalculateSpherePixelColor(double[] orig, double[] dir, int depth){
        if (depth<=maxDepth && SceneIntersect(orig, dir)) {
            double diffuse_light_intensity = 0,
                    specular_light_intensity = 0;

            double[] reflect_dir = reflect(dir, N);
            double reflect_dir_l = Math.sqrt(reflect_dir[0] * reflect_dir[0] + reflect_dir[1] * reflect_dir[1] + reflect_dir[2] * reflect_dir[2]);
            double[] reflect_dir_norm = new double[]{reflect_dir[0]/reflect_dir_l, reflect_dir[1]/reflect_dir_l, reflect_dir[2]/reflect_dir_l};
            double[] N_1 = {N[0]*0.001, N[1]*0.001, N[2]*0.001};
            double[] Hit_N_1 = {hit[0] - N_1[0], hit[1] - N_1[1], hit[2] - N_1[2]};
            double[] Hit_N_2 = {hit[0] + N_1[0], hit[1] + N_1[1], hit[2] + N_1[2]};
            double reflect_dir_N = reflect_dir_norm[0]*N[0] + reflect_dir_norm[1]*N[1] + reflect_dir_norm[2]*N[2];
            double[] reflect_orig = reflect_dir_N < 0 ? Hit_N_1 : Hit_N_2;

            for (int i=0; i<AllLights.size(); i++) {
                double LDirx = AllLights.elementAt(i).position[0] - hit[0];
                double LDiry = AllLights.elementAt(i).position[1] - hit[1];
                double LDirz = AllLights.elementAt(i).position[2] - hit[2];
                double LDirl = Math.sqrt(LDirx * LDirx + LDiry * LDiry + LDirz * LDirz);
                double[] LDir = new double[]{LDirx / LDirl, LDiry / LDirl, LDirz / LDirl};
                double LDirN = LDir[0]*N[0] + LDir[1]*N[1] + LDir[2]*N[2];

                double[] ref = reflect(LDir, N);
                double ref_dir = ref[0]*dir[0] + ref[1]*dir[1] +  ref[2]*dir[2];

                double LDist = LDirl;
                double[] shadow_orig = (LDirN < 0) ? Hit_N_1 : Hit_N_2; // checking if the point lies in the shadow of the lights[i]
                if (ShadowSceneIntersect(shadow_orig, LDir)){
                    double[] shadow_pt_orig = {shadow_pt[0] - shadow_orig[0], shadow_pt[1] - shadow_orig[1], shadow_pt[2] - shadow_orig[2]};
                    double shadow_l = Math.sqrt(shadow_pt_orig[0] * shadow_pt_orig[0] + shadow_pt_orig[1] * shadow_pt_orig[1] + shadow_pt_orig[2] * shadow_pt_orig[2]);
                    if(shadow_l<LDist)
                        continue;
                }

                diffuse_light_intensity += AllLights.elementAt(i).intensity  * Math.max(0, LDirN);
                specular_light_intensity += Math.pow(Math.max(0, ref_dir), material.specularExponent)*AllLights.elementAt(i).intensity;
            }

            Color reflect_color = CalculateSpherePixelColor(reflect_orig, reflect_dir_norm, depth+1);
            reflect_color = reflect_color==null ? Color.BLACK : reflect_color;
            Color resultColor = new Color((int)(Math.min(255,material.color.getRed() * diffuse_light_intensity * material.albedo[0] + specular_light_intensity * material.albedo[1] + reflect_color.getRed()*material.albedo[2])),
                    (int)(Math.min(255,material.color.getGreen() * diffuse_light_intensity * material.albedo[0] + specular_light_intensity * material.albedo[1] + reflect_color.getGreen()*material.albedo[2])),
                    (int)(Math.min(255,material.color.getBlue() * diffuse_light_intensity * material.albedo[0] + specular_light_intensity * material.albedo[1] + reflect_color.getBlue()*material.albedo[2])));
            return resultColor;// sphere color//resultColor
        }
        return null;
    }


    public static boolean SceneIntersect(double[] orig, double[] dir) {
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

    public static boolean ShadowSceneIntersect(double[] orig, double[] dir) {
        double spheres_dist = Double.MAX_VALUE;

        for (int i = 0; i < AllSpheres.size(); i++) {
            double dist_i = AllSpheres.elementAt(i).IsIntersect(orig, dir);
            if (dist_i!=-1 && dist_i < spheres_dist) {
                spheres_dist = dist_i;
                shadow_pt = new double[]{ orig[0] + dir[0] * dist_i, orig[1] + dir[1] * dist_i, orig[2] + dir[2] * dist_i};
                double Nx = shadow_pt[0] - AllSpheres.elementAt(i).center[0];
                double Ny = shadow_pt[1] - AllSpheres.elementAt(i).center[1];
                double Nz = shadow_pt[2] - AllSpheres.elementAt(i).center[2];
                double Nl = Math.sqrt(Nx * Nx + Ny * Ny + Nz * Nz);
                shadow_N = new double[]{Nx / Nl, Ny / Nl, Nz / Nl};
                shadowMaterial = AllSpheres.elementAt(i).material;
            }
        }
        return spheres_dist != Double.MAX_VALUE;
    }

    public static double[] reflect(double[] i, double[] n) {
        double I_N=i[0]*n[0] + i[1]*n[1] + i[2]*n[2];
        return new double[]{i[0] - n[0]*2*(I_N),
                i[1] - n[1]*2*(I_N),
                i[2] - n[2]*2*(I_N)};
    }

}
