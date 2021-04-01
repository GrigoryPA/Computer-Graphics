import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class TriangleModel {
    public Vector<Triangle> triangles;
    public Material3D material;
    public double minDist_i;
    public Triangle oneTriangle;


    public TriangleModel(String fileName, MaterialType _materialType) {
        material = new Material3D(_materialType);
        try (FileReader file = new FileReader(fileName)) {
            int buf;

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TriangleModel(MaterialType _materialType) {
        material = new Material3D(_materialType);
        triangles = new Vector<Triangle>();
        Triangle t = new Triangle(new Vector3d(-10,-5,10),
                new Vector3d(0,-5,30),
                new Vector3d(10,-5,10));
        triangles.add(t);
    }

    public double IsIntersect(Vector3d orig, Vector3d dir){
        double dist_i;
        minDist_i = Double.MAX_VALUE;
        for(int i=0; i<triangles.size(); i++){
            dist_i = triangles.elementAt(i).IsIntersect(orig, dir);
            if(dist_i<minDist_i && dist_i!=-1){
                minDist_i=dist_i;
                oneTriangle=triangles.elementAt(i);
            }
        }
        return minDist_i;
    }

}
