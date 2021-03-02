import java.awt.*;

public class RayTracing3D {
    public static int[] orig = new int[]{0,0,0};
    public static int dirZ = 1;

    static void RenderSphere(Sphere3D sphere, Display3D display) {
         double fov = Math.PI/2;
         int width =  Display3D.widthImage;
         int height = Display3D.heightImage;


        for (int j = 0; j<height; j++) {//по y
            for (int i = 0; i<width; i++) {//по х
                double x =  (2 * (i + 0.5)/width - 1) * Math.tan(fov/2) * width/height;
                double y = -(2*(j + 0.5)/height - 1)*Math.tan(fov/2.);
                double l = Math.sqrt(x*x + y*y + dirZ*dirZ);
                double[] dir = {x/l,y/l,dirZ/l};
                display.AddPointOnDisplayRT(i,j,Sphere3D.CalculateRayLandingPoint(orig, dir, sphere));
            }
        }
    }
}
