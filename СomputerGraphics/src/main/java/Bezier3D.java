import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class    Bezier3D {
    public double[][] points;
    public int[][] pointsResult;
    private int COUNT_W = 21;//кол-во точек на прямых(гладкость прямых)
    private int COUNT_U = 11;//кол-во прямых
    private double DELTA_W = 0.05;
    private double DELTA_U = 0.1;
    private boolean flagSecondDraw = false;

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
        int buf=0;
        points = new double[COUNT_W * COUNT_U*2][4];
        pointsResult = new int[COUNT_W * COUNT_U*2][2];
        try {
        N = AllTabs.size() - 1;
        M = AllTabs.elementAt(1).tableModel.getRowCount();
            int l = 0;
            double w = 0, u = 0;

            for (int j = 0; j < COUNT_U; u+=DELTA_U, j++) {
                for (int i = 0; i < COUNT_W; w+=DELTA_W, i++, l++) {
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
                }
                w=0;
            }
            u=0;

            flagSecondDraw = true;
            buf = N; N = M; M = buf;
            for (int j = 0; j < COUNT_U; u+=DELTA_U, j++) {
                for (int i = 0; i < COUNT_W; w+=DELTA_W, i++, l++) {
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
                }
                w=0;
            }
            u=0;
            buf = N; N = M; M = buf;
            flagSecondDraw = false;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "All values should be integer!\nBlank cells and rows are not allowed!");
        }
    }

    public Bezier3D(Bezier3D original) {

        points = original.points;
        pointsResult = original.pointsResult;

        DELTA_U = original.DELTA_U ;
        DELTA_W = original.COUNT_W;

        COUNT_U = original.COUNT_U;
        COUNT_W = original.COUNT_W;

        MAX_U = original.MAX_U;
        MAX_W = original.MAX_W;

        N = original.N;
        M = original.M;

    }

    public double FindOneBezierCoordinate(Vector<MyTable> AllTabs,int coordinate,double u, double w)
    {
        double Bij;
        double result = 0;
        double Jni;
        double Kmj;
        double multi;
        for (int i = 0;i < N;i++) {
            for(int j = 0;j < M;j++) {
               if(!flagSecondDraw)
                   Bij = Integer.parseInt((String) AllTabs.elementAt(i+1).Table.getValueAt(j, coordinate));
               else
                   Bij = Integer.parseInt((String) AllTabs.elementAt(j+1).Table.getValueAt(i, coordinate));
                Jni = (getFactorial(N-1) / (getFactorial(i) * getFactorial(N - 1 - i))) * Math.pow(u,i) * Math.pow(1 - u, N-1 - i);
                Kmj = (getFactorial(M-1) / (getFactorial(j) * getFactorial(M - 1 - j))) * Math.pow(w, j) * Math.pow(1 - w , M-1 - j);
                multi = Bij * Jni * Kmj;
                result += multi;
            }
        }
        return result;
    }

    public void IsometricProjection() {
        double[][] X = points;
        X = Multiply(X,Isometric);
        X = Multiply(X,ProjectionOnXOY);
        for(int i=0;i<X.length;i++) {
            pointsResult[i][0] = (int) X[i][0];
            pointsResult[i][1] = (int) X[i][1];
        }
    }

    public void AddFigureOnDisplay(Color colorFigure) {
        //Line3D.AddLineSigmentOnDisplayBresenham(pointsResult[0][0], pointsResult[0][1],pointsResult[pointsResult.length-1][0], pointsResult[pointsResult.length-1][1]);
        int i;
        for(i=0;i<pointsResult.length;i++) {
            if((i)%COUNT_W!=COUNT_W-1)
                Line3D.AddLineSigmentOnDisplayBresenham(pointsResult[i][0],
                                                        pointsResult[i][1],
                                                        pointsResult[i + 1][0],
                                                        pointsResult[i + 1][1],
                                                        colorFigure);
        }
    }

    public void Rotate( double rotateX, double rotateY) {
        double[][] b = {{Math.cos(rotateY), Math.sin(rotateX)*Math.sin(rotateY),  -Math.sin(rotateY)*Math.cos(rotateX),0},
                {0,                 Math.cos(rotateX),             Math.sin(rotateX), 0},
                {Math.sin(rotateY),        -Math.sin(rotateX)*Math.cos(rotateY), Math.cos(rotateX)*Math.cos(rotateY),0},
                {0,                 0,                      0, 1}};
        points=Multiply(points, b);
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

