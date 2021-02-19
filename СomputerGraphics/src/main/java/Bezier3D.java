import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class    Bezier3D {
    public Color colorCurve;
    public double[][] points;
    public int[][] BezierTable;
    public int[][] pointsResult;
    private double DELTA_W = 0.5;
    private double DELTA_U = 0.5;
    private int COUNT_W = 101;

    private int N;
    private int M;

    private int MAX_U;
    private int MAX_W;

    public double[][] Isometric = {{0.707107, -0.408248, 0.577353, 0},
            {0, 0.816497, 0.577345, 0},
            {-0.707107, -0.408248, 0.577353, 0},
            {0, 0, 0, 1}};
    public double[][] ProjectionOnXOY = {{1, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 1}};

    /*
    public double[][] TestMatrix = {
            {-15,0, 15,1},
            {-5, 5, 15,1},
            {5, 5, 15,1},
            {, , ,}};
*/
    public Bezier3D(Vector<MyTable> AllTabs) {
        points = new double[2000][4];
        pointsResult = new int[2000][2];

        N = AllTabs.size() - 1;
        M = AllTabs.elementAt(1).tableModel.getRowCount();

        MAX_U = N;
        MAX_W = M;

        //indOneBezierCoordinate( ,0.5,0.5)

        for ( int i = 1; i < AllTabs.size();i++)
        {
            try {
            for (int j = 0;i < M;j++)
            {
                for (double u = 0; u < MAX_U;u += DELTA_U)
                {
                    for (double w = 0; w < MAX_W;w += DELTA_W)
                    {
                        points[j][0] = FindOneBezierCoordinate(Integer.parseInt((String) AllTabs.elementAt(i).Table.getValueAt(j, 0)),u,w);
                        System.out.print(points[j][0]);
                        System.out.print(" ");
                        points[j][1] = FindOneBezierCoordinate(Integer.parseInt((String) AllTabs.elementAt(i).Table.getValueAt(j, 1)),u,w);
                        System.out.print(points[j][1]);
                        System.out.print(" ");
                        points[j][2] = FindOneBezierCoordinate(Integer.parseInt((String) AllTabs.elementAt(i).Table.getValueAt(j, 2)),u,w);
                        System.out.print(points[j][2]);
                        System.out.print(" ");
                        System.out.println(" ");
                        points[j][3] = 1;
                    }
                }
            }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "All values should be integer!\nBlank cells and rows are not allowed!");
            }
        }
        /*
        M=Table.getRowCount();
         = new int[M][2];
        try {
            for(int i=0; i<M; i++) {
                points[i][0]=Integer.parseInt((String) Table.getValueAt(i, 0));
                points[i][1]=Integer.parseInt((String) Table.getValueAt(i, 1));
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "All values should be integer!\nBlank cells and rows are not allowed");
        }

         */
    }

    public double FindOneBezierCoordinate(double b, double u, double w)
    {
        double coordinate = 0;
        double Jni;
        double Kmj;
        double multi;
        for (int i = 0;i < N;i++)
        {
            for(int j = 0;j < M;j++)
            {
                Jni = (getFactorial(N) / (getFactorial(i) * getFactorial(N - i))) * Math.pow(u,i) * Math.pow(1 - u, N - i);
                Kmj = (getFactorial(M) / (getFactorial(j) * getFactorial(M - j))) * Math.pow(w, j) * Math.pow(1 - w , M - j);
                multi = b * Jni * Kmj;
                coordinate += multi;
            }
        }
        return coordinate;
    }

    public void IsometricProjection() {
        double[][] M = points;
        M = Multiply(M,Isometric);
        M = Multiply(M,ProjectionOnXOY);
        for(int i=0;i<M.length;i++) {
            pointsResult[i][0] = (int) M[i][0];
            pointsResult[i][1] = (int) M[i][1];
        }
    }

    public void AddFigureOnDisplay(Color colorFigure) {
        //Line3D.AddLineSigmentOnDisplayBresenham(pointsResult[0][0], pointsResult[0][1],pointsResult[pointsResult.length-1][0], pointsResult[pointsResult.length-1][1]);
        for(int i=0;i<pointsResult.length-1;i+=2) {
            Line3D.AddLineSigmentOnDisplayBresenham(pointsResult[i][0], pointsResult[i][1], pointsResult[i + 1][0], pointsResult[i + 1][1], colorFigure);
        }
    }

    public double[][] Multiply(double[][] A, double[][] B) {
        double[][] c=new double[A.length][B[0].length];
        for (int i=0; i<A.length; ++i)
            for (int j=0; j<B[0].length; ++j)
                c[i][j] = (int)(A[i][0] * B[0][j] + A[i][1] * B[1][j] + A[i][2] * B[2][j] + A[i][3] * B[3][j]) ;
        return c;
    }

    public static int getFactorial(int f) {
        if (f <= 1) {
            return 1;
        }
        else {
            return f * getFactorial(f - 1);
        }
    }
}

