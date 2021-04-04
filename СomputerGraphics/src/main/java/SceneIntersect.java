import java.util.Vector;

public class SceneIntersect {
    public Vector3d hit;
    public Vector3d N;
    public Material3D material;
    public boolean isIntersect=false;
    private Vector3d nY = new Vector3d(0,1,0);
    private  Vector3d nX = new Vector3d(1,0,0);
    private  Vector3d nZ = new Vector3d(0,0,1);


    public  SceneIntersect(Vector3d orig, Vector3d dir, Vector<Sphere3D> AllSpheres, Vector<TriangleModel> AllTriangleModels) {
        double spheres_dist = Double.MAX_VALUE;

        for (int i = 0; i < AllSpheres.size(); i++) {
            double dist_i = AllSpheres.elementAt(i).IsIntersect(orig, dir);
            if (dist_i!=-1 && dist_i < spheres_dist) {
                spheres_dist = dist_i;
                hit = orig.getAddition(dir.getVectorScaled(dist_i));
                N = (hit.getSubtraction(AllSpheres.elementAt(i).center)).normalize();
                material = AllSpheres.elementAt(i).material;
            }
        }

        for (int i = 0; i < AllTriangleModels.size(); i++) {
            double dist_i = AllTriangleModels.elementAt(i).IsIntersect(orig, dir);
            if (dist_i>0 && dist_i < spheres_dist) {
                spheres_dist = dist_i;
                hit = orig.getAddition(dir.getVectorScaled(dist_i));
                N = AllTriangleModels.elementAt(i).oneTriangle.normal;
                material = AllTriangleModels.elementAt(i).material;
            }
        }



        isIntersect = spheres_dist < 1000;
    }
}
