import java.util.Vector;

public class SceneIntersect {
    public Vector3d hit;
    public Vector3d N;
    public Material3D material = new Material3D(1);
    public boolean isIntersect=false;


    public  SceneIntersect(Vector3d orig, Vector3d dir, Vector<Sphere3D> AllSpheres) {
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

        double checkerboard_dist = Double.MAX_VALUE;
        if(Math.abs(dir.y)>0.001){
            double d = -(orig.y+4)/dir.y;//плоскость шахматной доски задана как y=-4
            Vector3d pt = orig.getAddition((dir.getVectorScaled(d)));
            if(d>0 && Math.abs(pt.x)<100 &&
                    pt.z>0 && pt.z<1000 &&
                    d<spheres_dist){
                checkerboard_dist = d;
                hit = pt;
                N = new Vector3d(0,1,0);
                material = new Material3D(3);
            }

        }

        isIntersect = Math.min(spheres_dist, checkerboard_dist) < 1000;
    }
}
