import javax.swing.*;
import java.awt.*;

public class Curve2D {
    public Point2D[] points;
    public Point2D[] pointsResult;
    public int M;

    public Curve2D(JTable Table) {
        M=Table.getRowCount();
        points = new Point2D[M];
        double[] P = new double[2];
        try {
            for(int i=0; i<M; i++) {
                P[0]=Integer.parseInt((String) Table.getValueAt(i, 0));
                P[1]=Integer.parseInt((String) Table.getValueAt(i, 1));
                points[i]=new Point2D();
                points[i].SetLocation(P);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Все значения должны быть целочисленные!\nНе должно быть пустых строк или ячеек!");
        }
    }

    public Point2D OnePointWithGeometricAlgorithm(double t) {
        Point2D[] R = new Point2D[M];
        System.out.println(t);
        for (int i = 0; i < M; i++)
            R[i] = points[i];
        for (int j = M - 1; j > 0; j--)
            for (int i = 0; i < j; i++) {
                R[i].x = R[i].x + t * (R[i + 1].x - R[i].x);
                R[i].y = R[i].y + t * (R[i + 1].y - R[i].y);
            }
        return R[0];
    }

    public void ResultPoints() {
        pointsResult=new Point2D[11];
        double t=0;
        for (int i=0; i < 11; t+=0.1, i++) {
            pointsResult[i] = OnePointWithGeometricAlgorithm(t);
            System.out.println("("+pointsResult[i].x+"  "+pointsResult[i].y+")");
        }
    }

    public void AddCurve2DOnDisplay2D() {
        for(int i=0;i<points.length-1;i++)
            Line2D.AddLineSigmentOnDisplayBresenham(pointsResult[i].x, pointsResult[i].y, pointsResult[i+1].x, pointsResult[i+1].y);
    }
}
