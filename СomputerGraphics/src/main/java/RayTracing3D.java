import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class RayTracing3D {
    private Vector3d camera = new Vector3d(0,0,0);
    private int dirZ = 1;
    private double fov = Math.PI/2;
    public  Vector<Sphere3D> AllSpheres;
    public  Vector<Light3D> AllLights;
    public  Vector<TriangleModel> AllTriangleModels;
    public  Sphere3D OneSphere;
    public  Light3D OneLight;
    public  TriangleModel OneTriangleModel;
    private  Vector3d BackgroundColor = new Vector3d(0.3,0.5,0.4);
    private  int maxDepth=4;
    public int width;
    public int height;
    public Display3D Display;
    private int threadsAmount = 8;

    public RayTracing3D(JTable TableSpheres, JTable TableLights, Display3D _display){
        Display = _display;
        width =  Display.widthImage;
        height = Display.heightImage;
        AllSpheres = new Vector<Sphere3D>();
        AllLights = new Vector<Light3D>();
        AllTriangleModels = new Vector<TriangleModel>();

        for(int i=0; i<TableSpheres.getRowCount(); i++) {
            OneSphere = new Sphere3D(TableSpheres, i);
            AllSpheres.add(OneSphere);
        }

        for(int i=0; i<TableLights.getRowCount(); i++) {
            OneLight = new Light3D(TableLights, i);
            AllLights.add(OneLight);
        }

        //OneTriangleModels = new TriangleModel("model.txt", MaterialType.STEEL);
        //OneTriangleModel = new TriangleModel(MaterialType.STEEL);
        OneTriangleModel = new TriangleModel( "src/main/resources/3d/duck.obj",
                MaterialType.STEEL);
        //ScaleModel(1);
        OneTriangleModel.MoveModel(-5,3, 15);
        AllTriangleModels.add(OneTriangleModel);
        OneTriangleModel = new TriangleModel(new Vector3d(-50,-30,50),
                new Vector3d(0,50,50),
                new Vector3d(50,-30,50),
                MaterialType.REDWOOD);
        AllTriangleModels.add(OneTriangleModel);
    }

    public void RenderScene(Display3D display) {

        /*
        MyThread r1 = new MyThread(this, 0,width/threadsAmount-1);
        MyThread r2 = new MyThread(this, width/threadsAmount,width/threadsAmount*2-1);
        MyThread r3 = new MyThread(this, width/threadsAmount*2,width/threadsAmount*3-1);
        MyThread r4 = new MyThread(this, width/threadsAmount*3,width/threadsAmount*4-1);
        MyThread r5 = new MyThread(this, width/threadsAmount*4,width/threadsAmount*5-1);
        MyThread r6 = new MyThread(this, width/threadsAmount*5,width/threadsAmount*6-1);
        MyThread r7 = new MyThread(this, width/threadsAmount*6,width/threadsAmount*7-1);
        MyThread r8 = new MyThread(this, width/threadsAmount*7,width-1);

        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        Thread t3 = new Thread(r3);
        Thread t4 = new Thread(r4);
        Thread t5 = new Thread(r5);
        Thread t6 = new Thread(r6);
        Thread t7 = new Thread(r7);
        Thread t8 = new Thread(r8);

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t7.start();
        t8.start();

        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
            t6.join();
            t7.join();
            t8.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */
        /*
        for (int j = 0; j < height; ++j) {//по y
            for (int i = 0; i < width; ++i) {//по х
                RenderOnePixel(i, j);
            }
        }
        */

    }




    public void RenderOnePixel( int i, int j){
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
            Display.AddPointOnDisplayRT(i,height-j,pixelColor);
    }


    public  Vector3d CalculateSpherePixelColor(Vector3d orig, Vector3d dir, int depth){
        SceneIntersect scene = new SceneIntersect(orig,dir,AllSpheres,AllTriangleModels);

        if (depth<=maxDepth && scene.isIntersect) {
            Vector3d reflect_dir = reflect(dir, scene.N);
            Vector3d refract_dir = refract(dir, scene.N, scene.material.refractive).normalize();
            Vector3d reflect_orig = reflect_dir.getScalar(scene.N) < 0 ?
                    scene.hit.getSubtraction(scene.N.getVectorScaled(0.001)) :
                    scene.hit.getAddition(scene.N.getVectorScaled(0.001));
            Vector3d refract_orig = refract_dir.getScalar(scene.N) < 0 ?
                    scene.hit.getSubtraction(scene.N.getVectorScaled(0.001)) :
                    scene.hit.getAddition(scene.N.getVectorScaled(0.001));
            Vector3d reflect_color = CalculateSpherePixelColor(
                    reflect_orig,
                    reflect_dir,
                    depth+1);
            Vector3d refract_color = CalculateSpherePixelColor(
                    refract_orig,
                    refract_dir,
                    depth+1);

            double diffuse_light_intensity = 0,
                    specular_light_intensity = 0;

            for (int i=0; i<AllLights.size(); i++) {
                Vector3d light_dir = (AllLights.elementAt(i).position.getSubtraction(scene.hit)).normalize();
                double light_dist = (AllLights.elementAt(i).position.getSubtraction(scene.hit)).getModule();

                Vector3d shadow_orig = (light_dir.getScalar(scene.N) < 0) ?
                        scene.hit.getSubtraction(scene.N.getVectorScaled(0.001)) :
                        scene.hit.getAddition(scene.N.getVectorScaled(0.001)); // checking if the point lies in the shadow of the lights[i]

                SceneIntersect shadow = new SceneIntersect(
                        shadow_orig,
                        light_dir,
                        AllSpheres,
                        AllTriangleModels);
                if (shadow.isIntersect){
                    double shadow_l = (shadow.hit.getSubtraction(shadow_orig)).getModule();
                    if(shadow_l<light_dist)
                        continue;
                }

                diffuse_light_intensity += AllLights.elementAt(i).intensity * Math.max(0, light_dir.getScalar(scene.N));
                specular_light_intensity += Math.pow(
                        Math.max(0, reflect(light_dir,scene.N).getScalar(dir)),
                        scene.material.specularExponent)
                        *AllLights.elementAt(i).intensity;

            }

            Vector3d result_0 = scene.material.color.getVectorScaled(diffuse_light_intensity * scene.material.albedo[0]);
            Vector3d result_1 = (new Vector3d(1.,1.,1.)).getVectorScaled(specular_light_intensity * scene.material.albedo[1]);
            Vector3d result_2 = reflect_color.getVectorScaled(scene.material.albedo[2]);
            Vector3d result_3 = refract_color.getVectorScaled(scene.material.albedo[3]);
            return  result_0.getAddition(result_1.getAddition(result_2.getAddition(result_3)));// sphere color//resultColor
        }
        return BackgroundColor;
    }


    public  Vector3d reflect(Vector3d i, Vector3d n) {
        double I_N=i.getScalar(n);
        return i.getSubtraction(n.getVectorScaled(2.f*I_N));
    }

    public  Vector3d refract(Vector3d i, Vector3d n, double etaT) {
        double etaI = 1, buf;
        double cosi = -Math.max(-1, Math.min(1,i.getScalar(n)));
        Vector3d n_new = n;
        if(cosi<0){
            cosi = -cosi;
            buf=etaT; etaT=etaI; etaI=buf;
            n_new = n.getVectorScaled(-1);
        }

        double eta = etaI/etaT;
        double k = 1 - eta*eta*(1 - cosi*cosi);
        return k<0 ?
                new Vector3d(0,0,0) :
                (i.getVectorScaled(eta)).getAddition(n_new.getVectorScaled(eta*cosi - Math.sqrt(k)));
    }

}
