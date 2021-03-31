public class Triangle {

    public static double IsIntersect1(Vector3d vert0, Vector3d vert1, Vector3d vert2, Vector3d orig, Vector3d dir){
        Vector3d edge1 = vert1.getSubtraction(vert0);
        Vector3d edge2 = vert2.getSubtraction(vert0);
        Vector3d P = dir.cross(edge2);
        Vector3d T = orig.getSubtraction(vert0);
        Vector3d Q = T.cross(edge1);
        double det = edge1.getScalar(P);
        if(det<1e-8 && det>-1e-8) return -1;

        double u = T.getScalar(P)*1/det;
        if(u<0 || u>1) return -1;

        double v = dir.getScalar(Q)*1/det;
        if(v<0 || u+v>1) return -1;

        double t = 1-u-v;
        if(v<0 || u+v+t>1) return -1;

        double dist = edge2.getScalar(Q)*1/det;
        return dist;
    }


    public static boolean IsInTriangle (Vector3d P,Vector3d p0, Vector3d p1, Vector3d p2){
        double S = AreaTriangle(p0, p1, p2);
        double s1 = AreaTriangle(P, p0, p1);
        double s2 = AreaTriangle(P, p0, p2);
        double s3 = AreaTriangle(P, p1, p2);
        if (Math.abs(S-s1+s2+s3) < 1)
            return false;
        else
            return true;
    }

    public static double AreaTriangle (Vector3d p0, Vector3d p1, Vector3d p2){
        double d1 = p1.getSubtraction(p0).getModule();
        double d2 = p1.getSubtraction(p2).getModule();
        double d3 = p2.getSubtraction(p0).getModule();
        double p = (d1+d2+d3)/2;
        return Math.sqrt(p*(p-d1)*(p-d2)*(p-d3));
    }

}
