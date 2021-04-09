import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;
import java.util.Scanner;

public class TriangleModel {
    public Vector<Triangle> triangles;
    public Material3D material;
    public double minDist_i;
    public Triangle oneTriangle;
    private Vector3d[] v;
    private int[][] f;
    private int vcount = 0;
    private int fcount = 0;

    public TriangleModel(String fileName, MaterialType _materialType){
        material = new Material3D(_materialType);
        File file = new File(fileName);
        Scanner scanner = null;
        v = new Vector3d[256];
        f = new int[510][3];
        try {
            scanner = new Scanner(file);
            while(scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                String[] numbers = line.split(" ");
                //System.out.println(Arrays.toString(numbers));
                if ( numbers[0].equals("v"))
                {
                    v[vcount++] = new Vector3d(Float.parseFloat(String.valueOf(numbers[1])),
                            Float.parseFloat(String.valueOf(numbers[2])),
                            Float.parseFloat(String.valueOf(numbers[3])));
                }
                else if (numbers[0].equals("f"))
                {
                    f[fcount][0] = Integer.parseInt(String.valueOf(numbers[1]));
                    f[fcount][1] = Integer.parseInt(String.valueOf(numbers[2]));
                    f[fcount++][2] = Integer.parseInt(String.valueOf(numbers[3]));
                }
            }
            scanner.close();
            triangles = new Vector<Triangle>();
            for (int i = 0;i < fcount;i++)
            {
                Triangle t = new Triangle(v[f[i][0]-1], v[f[i][1]-1], v[f[i][2]-1]);
                triangles.add(t);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void ScaleModel(int koef)
    {
        for (int i = 0; i < triangles.size();i++) {
            triangles.elementAt(i).ScaleTriangle(   koef);
        }
    }

    public void MoveModel(double x, double y, double z)
    {
        Vector3d MoveVector = new Vector3d(x,y,z);
        for (int i = 0; i < triangles.size();i++) {
            triangles.elementAt(i).MoveTriangle(MoveVector);
        }
    }

    public TriangleModel(Vector3d v1, Vector3d v2, Vector3d v3,MaterialType _materialType) {
        material = new Material3D(_materialType);
        triangles = new Vector<Triangle>();
        Triangle t = new Triangle(v1, v2, v3);
        triangles.add(t);
    }

    //Задавать по часовой стрелке
    public TriangleModel(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, MaterialType _materialType) {
        material = new Material3D(_materialType);
        triangles = new Vector<Triangle>();
        triangles.add(new Triangle(v1, v2, v3));
        triangles.add(new Triangle(v3, v4, v1));
    }

    public TriangleModel(Vector3d v1, double height, double width, MaterialType _materialType) {
        material = new Material3D(_materialType);
        triangles = new Vector<Triangle>();
        triangles.add(new Triangle(v1, v1.getMoveY(height), v1.getMoveX(width).getMoveY(height)));
        triangles.add(new Triangle(v1.getMoveX(width).getMoveY(height), v1.getMoveX(width), v1));
    }

    public double IsIntersect(Vector3d orig, Vector3d dir){
        double dist_i;
        minDist_i = Double.MAX_VALUE;
        for(int i=0; i<triangles.size(); i++){
            dist_i = triangles.elementAt(i).IsIntersect(orig, dir);
            if(dist_i<minDist_i && dist_i>0){
                minDist_i=dist_i;
                oneTriangle=triangles.elementAt(i);
            }
        }
        return minDist_i;
    }

}
