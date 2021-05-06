import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class MainFrameSL extends MainFrame3D {
    private JFrame frame2D;
    private Display3D display3D;
    private FigureSL figureRealTime;
    private FigureSL figureOriginal;
    private Curve2D curve;
    public double deltaAngel = Math.PI / 18;
    private double countRotateAngel = 0;
    private int countRef = 0;

    JTabbedPane tabbedPane = new JTabbedPane();
    private Vector<>

    public void MakeAndShow() {
        frame2D = new JFrame("Scan Line");
        frame2D.setBounds(50, 50, 400, 450);
        frame2D.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton addPoly = new JButton("Add poly");
        JButton deletePoly = new JButton("Delete poly");
        JButton addRow = new JButton("Add row");
        JButton deleteRow = new JButton("Delete row");
        JButton draw = new JButton("Draw");
        JButton random = new JButton("Random");

        addRow.setToolTipText("Add row");
        deleteRow.setToolTipText("Delete row");
        draw.setToolTipText("Draw");
        random.setToolTipText("Create random segments");

        JToolBar toolBar1 = new JToolBar();
        toolBar1.add(addRow);
        toolBar1.add(deleteRow);
        toolBar1.add(random);
        toolBar1.add(draw);
        frame2D.add(toolBar1, BorderLayout.NORTH);

        Font font = new Font("Verdana", Font.PLAIN, 24);
        Font fontHeaders = new Font("Verdana", Font.CENTER_BASELINE, 16);

        String[] PolyHeaders = {"A", "B", "C", "D", "R", "G", "B"};
        String[][] PolyData;
        PolyData = new String[1][7];
        DefaultTableModel PolyTableModel = new DefaultTableModel(PolyData, PolyHeaders);
        JTable PolyTable = new JTable(PolyTableModel);
        JTableHeader PolyTableHeader = PolyTable.getTableHeader();
        PolyTableHeader.setFont(fontHeaders);
        PolyTable.setFont(font);
        PolyTable.setAutoCreateRowSorter(true);
        PolyTable.setRowHeight(PolyTable.getRowHeight() + 10);

        String[] pointsHeaders = {"X", "Y"};
        String[][] pointsData;
        pointsData = new String[1][2];
        DefaultTableModel PointsTableModel = new DefaultTableModel(pointsData, pointsHeaders);
        JTable PointsTable = new JTable(PointsTableModel);
        JTableHeader PointsTableHeader = PointsTable.getTableHeader();
        PointsTableHeader.setFont(fontHeaders);
        PointsTable.setFont(font);
        PointsTable.setAutoCreateRowSorter(true);
        PointsTable.setRowHeight(PointsTable.getRowHeight() + 10);


        JScrollPane PolyScroller = new JScrollPane(PolyTable);
        JScrollPane PointsScroller = new JScrollPane(PointsTable);
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(PolyScroller);
        panel.add(PointsScroller);
        frame2D.add(tabbedPane, BorderLayout.CENTER);

        frame2D.setVisible(true);

        addRow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PointsTableModel.addRow(new String[]{"", "", "", ""});
            }
        });

        deleteRow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] rows = PointsTable.getSelectedRows();
                if (rows.length > 0) {
                    PointsTableModel.removeRow(rows[0]);
                } else
                    JOptionPane.showMessageDialog(frame2D, "You must first select the rows.", "Failed to delete rows.", 0);
            }
        });

        draw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                display3D = new DisplayCS(new Color(217, 217, 217));
                //displayCS.AddCoordinateAxes();
                figureOriginal = new FigureSL(PointsTable, PolyTable);
                figureOriginal.AddFigure2DOnDisplayCS(Color.black);
                display3D.CreateAndOpenImage();
                figureRealTime = figureOriginal;
            }
        });

        check.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                display3D = new DisplayCS();
                //displayCS.AddCoordinateAxes();
                figureOriginal = new FigureSL(PointsTable, PolyTable);
                figureOriginal.DrawCohenSutherland(Color.black, Color.blue);
                display3D.CreateAndOpenImage();
                //displayCS.UpdateImage();
                figureRealTime = figureOriginal;
            }
        });

        random.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                /* ??? ??????
                PolyTableModel.removeRow(0);
                PolyTableModel.addRow(new String[]{"-200", "-150", "200", "150"});
                int a = PointsTableModel.getRowCount();
                for (int i = 0; i < a; i++) {
                    PointsTableModel.removeRow(0);
                    int b = PointsTableModel.getRowCount();
                }
                PointsTableModel.addRow(new String[]{"-40", "-15", "220", "220"});
                //PolyTableModel.addRow(new String[]{"-200", "-150", "200", "150"});
                */
                PolyTableModel.removeRow(0);
                PolyTableModel.addRow(new String[]{"-200", "-150", "200", "150"});

                int a = PointsTableModel.getRowCount();
                for (int i = 0; i < a; i++) {
                    PointsTableModel.removeRow(0);
                    int b = PointsTableModel.getRowCount();
                }
                for (int i = 0; i < 10; i++) {
                    String[] temp = new String[4];
                    for (int j = 0; j < temp.length; j += 2) {
                        temp[j] = getRandomNumber(-350, 350).toString();
                        temp[j + 1] = getRandomNumber(-250, 250).toString();
                    }
                    PointsTableModel.addRow(temp);
                }
            }
        });
    }

    void createTab() {

        Font font = new Font("Verdana", Font.PLAIN, 24);
        Font fontHeaders = new Font("Verdana", Font.CENTER_BASELINE, 16);

        String[] PolyHeaders = {"A", "B", "C", "D", "R", "G", "B"};
        String[][] PolyData;
        PolyData = new String[1][7];
        DefaultTableModel PolyTableModel = new DefaultTableModel(PolyData, PolyHeaders);
        JTable PolyTable = new JTable(PolyTableModel);
        JTableHeader PolyTableHeader = PolyTable.getTableHeader();
        PolyTableHeader.setFont(fontHeaders);
        PolyTable.setFont(font);
        PolyTable.setAutoCreateRowSorter(true);
        PolyTable.setRowHeight(PolyTable.getRowHeight() + 10);

        String[] pointsHeaders = {"X", "Y"};
        String[][] pointsData;
        pointsData = new String[1][2];
        DefaultTableModel PointsTableModel = new DefaultTableModel(pointsData, pointsHeaders);
        JTable PointsTable = new JTable(PointsTableModel);
        JTableHeader PointsTableHeader = PointsTable.getTableHeader();
        PointsTableHeader.setFont(fontHeaders);
        PointsTable.setFont(font);
        PointsTable.setAutoCreateRowSorter(true);
        PointsTable.setRowHeight(PointsTable.getRowHeight() + 10);


        JScrollPane PolyScroller = new JScrollPane(PolyTable);
        JScrollPane PointsScroller = new JScrollPane(PointsTable);
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(PolyScroller);
        panel.add(PointsScroller);

        tabbedPane.addTab("1", panel);
        frame2D.setVisible(true);
    }

    public Integer getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}