import java.util.Vector;

public class SceneIntersect {
    public Vector3d hit;
    public Vector3d N;
    public Material3D material;
    public boolean isIntersect=false;
    private Vector3d nY = new Vector3d(0,1,0);
    private  Vector3d nX = new Vector3d(1,0,0);
    private  Vector3d nZ = new Vector3d(0,0,1);


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

        d = -(orig.getScalar(nX) + 5) / dir.getScalar(nX);
        pt = orig.getAddition((dir.getVectorScaled(d)));
        if(Math.abs(pt.y)<10 && pt.z>10 && pt.z<30
                && d>0 && d<spheres_dist){
            spheres_dist = d;
            hit = pt;
            N = nX;
            material = new Material3D(1);
        }
        */




        /*Vector3d p0 = new Vector3d(-40,-5,30);
        Vector3d p1 = new Vector3d(-20,-5,30);
        Vector3d p2 = new Vector3d(-30,20,30);
        double checkerboard_dist = Double.MAX_VALUE;
        //if(Math.abs(dir.y)>0.001){

        Vector3d edge1 = p1.getSubtraction(p0);
        Vector3d edge2 = p2.getSubtraction(p0);
        Vector3d pvec = dir.cross(edge2);
        double det = edge1.getScalar(pvec);

        Vector3d tvec = orig.getSubtraction(p0);
        double u = tvec.getScalar(pvec);

        Vector3d qvec = tvec.cross(edge1);
        double v = dir.getScalar(qvec);

        double tnear = edge2.getScalar(qvec)*(1./det);
        double d = tnear;
            //double d = -(orig.y+5)/dir.y;//плоскость шахматной доски задана как y=-4
            Vector3d pt = orig.getAddition((dir.getVectorScaled(d)));
            if(d>0  &&
                    d<spheres_dist){
                checkerboard_dist = d;
                hit = pt;
                N = new Vector3d(0,1,0);
                material = /*(((int)(.8*hit.x+1000) + (int)(.8*hit.z))&1) == 0
                        ? new Material3D(3) :  new Material3D(2);
            }*/
        //}

        //double triangle_dist = Double.MAX_VALUE;
        Vector3d p0 = new Vector3d(10,-5,10);
        Vector3d p1 = new Vector3d(0,-5,10);
        Vector3d p2 = new Vector3d(-10,-5,30);
        double dist_tr1 = Triangle.IsIntersect1(p0, p1, p2, orig, dir);
        if (dist_tr1>0 && dist_tr1 < spheres_dist) {
            spheres_dist = dist_tr1;
            hit = orig.getAddition(dir.getVectorScaled(dist_tr1));
            N = new Vector3d(0,1,0);
            //N = (new Vector3d(edge1.y * edge2.z, edge1.z * edge2.x, edge1.x * edge2.y)).normalize();
            material = new Material3D(Material3D.MaterialType.MIRROR);
        }
        /*
        double triangle_dist2 = Double.MAX_VALUE;
        Vector3d p3 = new Vector3d(0,-5,10);
        Vector3d p4 = new Vector3d(20,-5,10);
        Vector3d p5 = new Vector3d(10,-5,30);
        double dist_tr2 = Triangle.IsIntersect2(p3, p4, p5, orig, dir);
        if (dist_tr2!=0 && dist_tr2 < spheres_dist) {
            triangle_dist = dist_tr2;
            hit = orig.getAddition((dir.getVectorScaled(triangle_dist)));
            //Vector3d edge1 = p1.getSubtraction(p0);
            //Vector3d edge2 = p2.getSubtraction(p0);
            N = new Vector3d(0,1,0);
            //N = (new Vector3d(edge1.y * edge2.z, edge1.z * edge2.x, edge1.x * edge2.y)).normalize();
            material = new Material3D(2);
        }
        */


        isIntersect = spheres_dist < 1000;
        //isIntersect = spheres_dist < 1000;
    }
}
