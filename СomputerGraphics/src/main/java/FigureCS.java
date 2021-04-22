import javax.swing.*;
import java.awt.*;

public class FigureCS {
    public Segment[] segments;
    public Rectangle rectangle;
    public FigureCS(JTable SegmentTable, JTable RectangleTable) {
        //colorFigure=color;
        try {
            segments = new Segment[SegmentTable.getRowCount()];
            for (int i = 0; i < SegmentTable.getRowCount(); i++) {
                Point point1 = new Point();
                point1.x = Integer.parseInt((String) SegmentTable.getValueAt(i, 0));
                point1.y = Integer.parseInt((String) SegmentTable.getValueAt(i, 1));
                Point point2 = new Point();
                point2.x = Integer.parseInt((String) SegmentTable.getValueAt(i, 2));
                point2.y = Integer.parseInt((String) SegmentTable.getValueAt(i, 3));
                segments[i] = new Segment(point1, point2);
                segments[i].i = i;
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
        Line2D.clear();
        Color fillColor = new Color(250, 250,250);
        Line2D.AddRectangleOnDisplayBresenham(rectangle, colorFigure, fillColor);
        for (int i = 0; i < segments.length; i++)
            Line2D.AddLineSigmentOnDisplayBresenham(segments[i], colorFigure);
     }

    public void DrawCohenSutherland(Color colorFigure, Color colorAlternative) {
        Line2D.clear();
        Color fillColor = new Color(250, 250,250);
        Line2D.AddRectangleOnDisplayBresenham(rectangle, colorFigure, fillColor);
        CohenSutherland2D cohenSutherland2D = new CohenSutherland2D();
        for (int i = 0; i < segments.length; i++) {
            if (cohenSutherland2D.CohenSutherland(rectangle, segments[i]))
                Line2D.AddLineSigmentOnDisplayBresenham(segments[i], colorFigure);
            else {
                Line2D.AddLineSigmentOnDisplayBresenham(segments[i].point1, segments[i]._point1, colorAlternative);
                Line2D.AddLineSigmentOnDisplayBresenham(segments[i]._point1, segments[i]._point2, Color.RED);
                Line2D.AddLineSigmentOnDisplayBresenham(segments[i].point2, segments[i]._point2, colorAlternative);
            }
        }
    }
}
