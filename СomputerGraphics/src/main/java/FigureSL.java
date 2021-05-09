import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

public class FigureSL {
    ScanLine scanLine;
    ScreenData screen;
    NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
    Number number;

    public FigureSL(Map<Integer, Tab> tabs, ScreenData screen) {
        //colorFigure=color;
        try {
            Vector<Polyhedron> Polyhedrons = new Vector<>();
            for (Map.Entry<Integer, Tab> entry : tabs.entrySet()) {
                Table PolyTable = entry.getValue().Poly;
                Polyhedron Polyhedron;

                double A = parseDouble((String) PolyTable.Table.getValueAt(0, 0));
                double B = parseDouble((String) PolyTable.Table.getValueAt(0, 1));
                double C = parseDouble((String) PolyTable.Table.getValueAt(0, 2));
                double D = parseDouble((String) PolyTable.Table.getValueAt(0, 3));
                int r = Integer.parseInt((String) PolyTable.Table.getValueAt(0, 4));
                int g = Integer.parseInt((String) PolyTable.Table.getValueAt(0, 5));
                int b = Integer.parseInt((String) PolyTable.Table.getValueAt(0, 6));
                Color c = new Color(r, g, b);

                Table PointsTable = entry.getValue().Points;
                Point3D[] Points3D = new Point3D[PointsTable.Table.getRowCount()];
                for (int i = 0; i < PointsTable.Table.getRowCount(); i++) {
                    Point3D p = new Point3D();
                    p.x = parseDouble((String) PointsTable.Table.getValueAt(i, 0));
                    p.y = parseDouble((String) PointsTable.Table.getValueAt(i, 1));
                    Points3D[i] = p;
                }
                Polyhedron = new Polyhedron(A, B, C, D, c, Points3D);
                Polyhedrons.add(Polyhedron);
            }
            scanLine = new ScanLine(Polyhedrons, screen);
            this.screen = screen;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "All values should be integer!\nBlank cells and rows are not allowed!");
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "All values should be integer!\nBlank cells and rows are not allowed!");
        }
    }

    public void DrawScanLine() {
        scanLine.run();
    }

    private double parseDouble(String a) throws ParseException {
        number = format.parse(a);
        return number.doubleValue();
    }
}