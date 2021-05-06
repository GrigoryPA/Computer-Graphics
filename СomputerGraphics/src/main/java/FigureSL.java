import javax.swing.*;

public class FigureSL {
    public Segment[] Polyhedron;
    public Rectangle[] Points;
    public FigureSL(JTable SegmentTable, JTable RectangleTable) {
        //colorFigure=color;
        try {
            Polyhedron = new Segment[SegmentTable.getRowCount()];
            for (int i = 0; i < SegmentTable.getRowCount(); i++) {
                Point point1 = new Point();
                point1.x = Integer.parseInt((String) SegmentTable.getValueAt(i, 0));
                point1.y = Integer.parseInt((String) SegmentTable.getValueAt(i, 1));
                Point point2 = new Point();
                point2.x = Integer.parseInt((String) SegmentTable.getValueAt(i, 2));
                point2.y = Integer.parseInt((String) SegmentTable.getValueAt(i, 3));
                Polyhedron[i] = new Segment(point1, point2);
                Polyhedron[i].i = i;
            }
            Points = new Rectangle();
            Points.BottomLeft.x = Integer.parseInt((String) RectangleTable.getValueAt(0, 0));
            Points.BottomLeft.y = Integer.parseInt((String) RectangleTable.getValueAt(0, 1));
            Points.TopRight.x = Integer.parseInt((String) RectangleTable.getValueAt(0, 2));
            Points.TopRight.y = Integer.parseInt((String) RectangleTable.getValueAt(0, 3));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "All values should be integer!\nBlank cells and rows are not allowed!");
        }
    }

    public void AddFigure2DOnDisplayCS(Color colorFigure) {
        Line2D.clear();
        Color fillColor = new Color(250, 250,250);
        Line2D.AddRectangleOnDisplayBresenham(Points, colorFigure, fillColor);
        for (int i = 0; i < Polyhedron.length; i++)
            Line2D.AddLineSigmentOnDisplayBresenham(Polyhedron[i], colorFigure);
    }

    public void DrawCohenSutherland(Color colorFigure, Color colorAlternative) {
        Line2D.clear();
        Color fillColor = new Color(250, 250,250);
        Line2D.AddRectangleOnDisplayBresenham(Points, colorFigure, fillColor);
        CohenSutherland2D cohenSutherland2D = new CohenSutherland2D();
        for (int i = 0; i < Polyhedron.length; i++) {
            if (cohenSutherland2D.CohenSutherland(Points, Polyhedron[i]))
                Line2D.AddLineSigmentOnDisplayBresenham(Polyhedron[i], colorFigure);
            else {
                Line2D.AddLineSigmentOnDisplayBresenham(Polyhedron[i].point1, Polyhedron[i]._point1, colorAlternative);
                Line2D.AddLineSigmentOnDisplayBresenham(Polyhedron[i]._point1, Polyhedron[i]._point2, Color.RED);
                Line2D.AddLineSigmentOnDisplayBresenham(Polyhedron[i].point2, Polyhedron[i]._point2, colorAlternative);
            }
        }
    }
}