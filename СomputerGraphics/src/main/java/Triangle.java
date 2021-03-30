public class Triangle {

    public static double IsIntersect1(Vector3d vert0, Vector3d vert1, Vector3d vert2, Vector3d orig, Vector3d dir){
        Vector3d edge1 = vert1.getSubtraction(vert0);
        Vector3d edge2 = vert2.getSubtraction(vert0);
        Vector3d pvec = dir.cross(edge2);
        double det = edge1.getScalar(pvec);
        if(det<1e-8 && det>-1e-8) return 0;

        Vector3d tvec = orig.getSubtraction(vert0);
        double u = tvec.getScalar(pvec)*1/det;
        if(u<0 || u>1) return 0;

        Vector3d qvec = tvec.cross(edge1);
        double v = dir.getScalar(qvec)*1/det;
        if(v<0 || u+v>1) return 0;

        return edge2.getScalar(qvec)*1/det;
    }

    public static double IsIntersect2(Vector3d vert0, Vector3d vert1, Vector3d vert2, Vector3d orig, Vector3d dir){
        Vector3d edge1 = vert1.getSubtraction(vert0);
        Vector3d edge2 = vert2.getSubtraction(vert0);
        Vector3d pvec = dir.cross(edge2);
        double det = edge1.getScalar(pvec);
        if(det<1e-3 && det>-1e-3) return 0;

        Vector3d tvec = orig.getSubtraction(vert0);
        double u = tvec.getScalar(pvec);
        if(u<0 || u>det) return 0;

        Vector3d qvec = tvec.cross(edge1);
        double v = dir.getScalar(qvec);
        if(v<0 || u+v>det) return 0;

        double tnear = edge2.getScalar(qvec)*(1./det);
        if(tnear>1e-3 || tnear<-1e-3)
            return tnear;
        else
            return 0;
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
