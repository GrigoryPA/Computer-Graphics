import javax.swing.*;
import java.awt.*;

public class FigureCS {
    public Point[] SegmentPoints;
    public Rectangle rectangle;
    public FigureCS(JTable SegmentTable, JTable RectangleTable) {
        //colorFigure=color;
        try {
            SegmentPoints = new Point[SegmentTable.getRowCount()];
            for (int i = 0; i < SegmentTable.getRowCount(); i++) {
                SegmentPoints[i] = new Point();
                SegmentPoints[i].x = Integer.parseInt((String) SegmentTable.getValueAt(i, 0));
                SegmentPoints[i].y = Integer.parseInt((String) SegmentTable.getValueAt(i, 1));
            }
            rectangle = new Rectangle();
            rectangle.BottomLeft.x = Integer.parseInt((String) RectangleTable.getValueAt(0, 0));
            rectangle.BottomLeft.y = Integer.parseInt((String) RectangleTable.getValueAt(0, 1));
            rectangle.TopRight.x = Integer.parseInt((String) RectangleTable.getValueAt(0, 2));
            rectangle.TopRight.y = Integer.parseInt((String) RectangleTable.getValueAt(0, 3));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "All values should be integer!\nBlank cells and rows are not allowed!");
        }
    }

    public void AddFigure2DOnDisplayCS(Color colorFigure) {
        Line2D.AddLineSigmentOnDisplayBresenham(SegmentPoints[0], SegmentPoints[SegmentPoints.length - 1], colorFigure);
        for (int i = 0; i < SegmentPoints.length - 1; i++)
            Line2D.AddLineSigmentOnDisplayBresenham(SegmentPoints[i], SegmentPoints[i + 1], colorFigure);

        Line2D.AddRectangleOnDisplayBresenham(rectangle, colorFigure);
    }
}
