import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Curve2D {
    public Color colorCurve;
    public int[][] points;
    public int[][] pointsResult;
    public int M;
    private int COUNT_T= 101;
    private double DELTA_T= 0.01;

    public Curve2D(JTable Table, Color color) {
        colorCurve=color;
        M=Table.getRowCount();
        points = new int[M][2];
        try {
            for(int i=0; i<M; i++) {
                points[i][0]=Integer.parseInt((String) Table.getValueAt(i, 0));
                points[i][1]=Integer.parseInt((String) Table.getValueAt(i, 1));
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "All values should be integer!\nBlank cells and rows are not allowed");
        }
    }

    public int[] OnePointWithGeometricAlgorithm(double t) {
        double[][] R = new double[M][2];
        for (int i = 0; i < M; i++){
            R[i][0] = points[i][0]; R[i][1] = points[i][1];
        }

        for (int j = M - 1; j > 0; j--)
            for (int i = 0; i < j; i++) {
                R[i][0] = (R[i][0] + t * (R[i + 1][0] - R[i][0]));
                R[i][1] = (R[i][1] + t * (R[i + 1][1] - R[i][1]));
            }


        int[] res = new int[2];
            res[0]=(int)R[0][0];
            res[1]=(int)R[0][1];
        return res;
    }

    public void ResultPoints() {
        pointsResult=new int[COUNT_T][2];
        double t=0;
        for (int i=0; i < COUNT_T; t+=DELTA_T, i++) {
            pointsResult[i] = OnePointWithGeometricAlgorithm(t);
        }
    }

    public void AddCurve2DOnDisplay2D() {
        for(int i=0;i<pointsResult.length-1;i++)//рисуем курву
            Line2D.AddLineSigmentOnDisplayBresenham(pointsResult[i][0],
                    pointsResult[i][1],
                    pointsResult[i + 1][0],
                    pointsResult[i + 1][1], colorCurve);
        for(int i=0;i<points.length;i++) {//рисуем ключевые точки
            Display2D.AddPointOnDisplay(points[i][0], points[i][1], Color.RED);
            Display2D.AddPointOnDisplay(points[i][0], points[i][1]+1, Color.RED);
            Display2D.AddPointOnDisplay(points[i][0], points[i][1]-1, Color.RED);
            Display2D.AddPointOnDisplay(points[i][0]+1, points[i][1], Color.RED);
            Display2D.AddPointOnDisplay(points[i][0]-1, points[i][1], Color.RED);


        }


    }
}
