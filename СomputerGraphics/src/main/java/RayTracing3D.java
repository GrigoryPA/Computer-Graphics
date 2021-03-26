import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class RayTracing3D {
    public static Vector3d camera = new Vector3d(0,0,0);
    public static int dirZ = 1;
    public static double fov = Math.PI/2;
    public static Vector<Sphere3D> AllSpheres;
    public static Vector<Light3D> AllLights;
    public static Sphere3D OneSphere;
    public static Light3D OneLight;
    private static int maxDepth=2;

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
                Vector3d dir = (new Vector3d(x , y , dirZ )).normalize();

                Vector3d color_kef = CalculateSpherePixelColor(camera, dir, 0);

                double max_color_kef = Math.max(color_kef.x, Math.max(color_kef.y, color_kef.z));
                if(max_color_kef>1)
                    color_kef = color_kef.getVectorScaled(1./max_color_kef);
                Color pixelColor = new Color((int)(255*Math.max(0, Math.min(1,color_kef.x))),
                        (int)(255*Math.max(0, Math.min(1,color_kef.y))),
                        (int)(255*Math.max(0, Math.min(1,color_kef.z))));
                if(pixelColor!=null)
                    display.AddPointOnDisplayRT(i,height-j,pixelColor);
            }
        }
    }


    public static Vector3d CalculateSpherePixelColor(Vector3d orig, Vector3d dir, int depth){
        SceneIntersect scene = new SceneIntersect(orig,dir,AllSpheres);

        if (/*depth<=maxDepth &&*/ scene.isIntersect) {
            /*Vector3d reflect_dir_norm = reflect(dir, scene.N).normalize();
            Vector3d N_1 = scene.N.getVectorScaled(0.001);
            Vector3d reflect_orig = reflect_dir_norm.getScalar(scene.N) < 0 ?
                    scene.hit.getSubtraction(N_1) :
                    scene.hit.getAddition(N_1);
            Vector3d reflect_color = CalculateSpherePixelColor(
                    reflect_orig,
                    reflect_dir_norm,
                    depth+1);
            */
            double diffuse_light_intensity = 0,
                    specular_light_intensity = 0;

            for (int i=0; i<AllLights.size(); i++) {
                Vector3d light_dir = (AllLights.elementAt(i).position.getSubtraction(scene.hit)).normalize();
                /*double light_dist = light_dir.getModule();

                Vector3d shadow_orig = (light_dir.getScalar(scene.N) < 0) ? scene.hit.getSubtraction(N_1) :  scene.hit.getAddition(N_1); // checking if the point lies in the shadow of the lights[i]

                SceneIntersect shadow = new SceneIntersect(
                        shadow_orig,
                        light_dir,
                        AllSpheres);
                if (shadow.isIntersect){
                    double shadow_l = shadow.hit.getSubtraction(shadow_orig).getModule();
                    if(shadow_l<light_dist)
                        continue;
                }
                */
                diffuse_light_intensity += AllLights.elementAt(i).intensity * Math.max(0, light_dir.getScalar(scene.N));
                specular_light_intensity += Math.pow(
                        Math.max(0, reflect(light_dir,scene.N).getScalar(dir)),
                        scene.material.specularExponent)
                        *AllLights.elementAt(i).intensity;

            }

            Vector3d result_0 = scene.material.color.getVectorScaled(diffuse_light_intensity * scene.material.albedo[0]);
            Vector3d result_1 = (new Vector3d(1.,1.,1.)).getVectorScaled(specular_light_intensity * scene.material.albedo[1]);
            //Vector3d result_2 = reflect_color.getVectorScaled(scene.material.albedo[2]);
            return  result_0.getAddition(result_1/*.getAddition(result_2)*/);// sphere color//resultColor
        }
        return new Vector3d(0.1,1,0.1);
    }


    public static Vector3d reflect(Vector3d i, Vector3d n) {
        double I_N=i.getScalar(n);
        return i.getSubtraction(n.getVectorScaled(2.f*I_N));
    }

}
