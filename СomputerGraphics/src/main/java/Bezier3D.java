import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class    Bezier3D {
    public Color colorCurve;
    public double[][] points;
    public int[][] BezierTable;
    public int[][] pointsResult;
    private double DELTA_W = 0.02;//кол-во точек на прямых(гладкость прямых)
    private double DELTA_U = 0.02;//кол-во прямых
    private int COUNT_W = (int)(1/DELTA_W);
    private int COUNT_U = (int)(1/DELTA_U);

    private int N;
    private int M;

    private int MAX_U=1;
    private int MAX_W=1;

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
        points = new double[COUNT_W * COUNT_U+1][4];
        pointsResult = new int[COUNT_W * COUNT_U+1][2];

        N = AllTabs.size() - 1;
        M = AllTabs.elementAt(1).tableModel.getRowCount();


        int l = 0;

        try {
            for (double u = 0; u <= MAX_U; u += DELTA_U) {
                for (double w = 0; w <= MAX_W; w += DELTA_W) {
                    points[l][0] = FindOneBezierCoordinate(AllTabs, 0, u, w);
                    System.out.print(points[l][0]);
                    System.out.print(" ");
                    points[l][1] = FindOneBezierCoordinate(AllTabs, 1, u, w);
                    System.out.print(points[l][1]);
                    System.out.print(" ");
                    points[l][2] = FindOneBezierCoordinate(AllTabs, 2, u, w);
                    System.out.print(points[l][2]);
                    System.out.print(" ");
                    System.out.println();
                    points[l][3] = 1;
                    l++;
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "All values should be integer!\nBlank cells and rows are not allowed!");
        }
    }

    public double FindOneBezierCoordinate(Vector<MyTable> AllTabs,int coordinate,double u, double w)
    {
        double Bij;
        double result = 0;
        double Jni;
        double Kmj;
        double multi;
        for (int i = 0;i < N;i++)
        {
            for(int j = 0;j < M;j++)
            {
                Bij = Integer.parseInt((String) AllTabs.elementAt(i+1).Table.getValueAt(j, coordinate));
                Jni = (getFactorial(N-1) / (getFactorial(i) * getFactorial(N - 1-i))) * Math.pow(u,i) * Math.pow(1 - u, N-1 - i);
                Kmj = (getFactorial(M-1) / (getFactorial(j) * getFactorial(M-1 - j))) * Math.pow(w, j) * Math.pow(1 - w , M-1 - j);
                multi = Bij * Jni * Kmj;
                result += multi;
            }
        }
        return result;
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
        int i;
        for(i=0;i<COUNT_U*COUNT_W-1;i++) {
            if((i)%COUNT_U!=COUNT_U-1)
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

