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

        /*
        double checkerboard_dist = Double.MAX_VALUE;
        double d = -(orig.getScalar(nY) + 5) / dir.getScalar(nY);
        Vector3d pt = orig.getAddition((dir.getVectorScaled(d)));
        if(Math.abs(pt.x)<10 && pt.z>pt.x+20 && pt.z<30
                && d>0 && d<spheres_dist){
            spheres_dist = d;
            hit = pt;
            N = nY;
            material = new Material3D(2);
        }

         */

        Vector3d p0 = new Vector3d(0,-5,10);
        Vector3d p1 = new Vector3d(10,-5,10);
        Vector3d p2 = new Vector3d(5,-5,30);
        Triangle tri = new Triangle(p0, p1, p2);
        double dist_tr1 = tri.IsIntersect(orig, dir);
        if (dist_tr1>0 && dist_tr1 < spheres_dist) {
            spheres_dist = dist_tr1;
            hit = orig.getAddition(dir.getVectorScaled(dist_tr1));
            N = new Vector3d(0,1,0);
            //N = (new Vector3d(edge1.y * edge2.z, edge1.z * edge2.x, edge1.x * edge2.y)).normalize();
            material = new Material3D(MaterialType.MIRROR);
        }

        isIntersect = spheres_dist < 1000;
    }
}
