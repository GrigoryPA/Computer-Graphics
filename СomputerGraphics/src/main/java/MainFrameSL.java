import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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
    private Map<Integer, Tab> tabs = new HashMap<>();

    public void MakeAndShow() {
        frame2D = new JFrame("Scan Line");
        frame2D.setBounds(50, 50, 800, 450);
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
        toolBar1.add(addPoly);
        toolBar1.add(deletePoly);
        toolBar1.add(addRow);
        toolBar1.add(deleteRow);
        toolBar1.add(random);
        toolBar1.add(draw);
        frame2D.add(toolBar1, BorderLayout.NORTH);
        frame2D.add(tabbedPane, BorderLayout.CENTER);

        frame2D.setVisible(true);

        addPoly.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createTab();
            }
        });

        deletePoly.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedTab = tabbedPane.getSelectedIndex();
                tabbedPane.remove(selectedTab);
                int dataId = Integer.parseInt(tabbedPane.getTitleAt(selectedTab));
                tabs.remove(dataId);
            }
        });

        addRow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedTab = tabbedPane.getSelectedIndex();
                int dataId = Integer.parseInt(tabbedPane.getTitleAt(selectedTab));
                tabs.get(dataId).Points.Data = new String[1][tabs.get(dataId).Points.Headers.length];
            }
        });

        deleteRow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedTab = tabbedPane.getSelectedIndex();
                int dataId = Integer.parseInt(tabbedPane.getTitleAt(selectedTab));
                int[] rows = tabs.get(dataId).Points.Table.getSelectedRows();
                if (rows.length > 0) {
                    tabs.get(dataId).Points.TableModel.removeRow(rows[0]);
                } else
                    JOptionPane.showMessageDialog(frame2D, "You must first select the rows.", "Failed to delete rows.", 0);
            }
        });

        draw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ScreenData screen = new ScreenData(700, 700);
                //displayCS.AddCoordinateAxes();
                figureOriginal = new FigureSL(tabs, screen);
                figureOriginal.DrawScanLine();
                //figureOriginal.Add;
                screen.CreateAndOpenImage();
                figureRealTime = figureOriginal;
            }
        });

        random.addActionListener(new ActionListener() { // у меня плохая архитектура программы
            public void actionPerformed(ActionEvent e) { // я тоже самое делаю в другом классе
                tabs.clear();
                tabbedPane.removeAll();
                for (int i = 0; i < getRandomInteger(3, 6); i++) {
                    createTab();
                }

                double A = 0, B = 0, C = 0, D = 0;
                for (Map.Entry<Integer, Tab> entry : tabs.entrySet()) { // генерация плоскости

                    int min = -300, max = 300;
                    int nOfPointsToGen = getRandomInteger(3, 3);
                    int[] x = new int[nOfPointsToGen];
                    int[] y = new int[nOfPointsToGen];
                    double[] z = new double[nOfPointsToGen];
                    for (int i = 0, retry = 0; i < nOfPointsToGen; i++) { // генерация точек
                        if (retry == 0) {
                            A = getNotZeroDouble(-25, 25) / 5;
                            B = getNotZeroDouble(-25, 25) / 5;
                            C = getNotZeroDouble(-25, 25) / 5;
                            D = getRandomDouble(-100, 100);
                            retry = 20;
                            i = 0;
                        }
                        do {
                            x[i] = getRandomInteger(min, max);
                            y[i] = getRandomInteger(min, max);
                            z[i] = (D - (A * x[i]) - (B * y[i])) / C;
                            retry--;
                            if ((z[i] > 500) || (z[i] < -500))
                                retry--;
                        } while (((z[i] > 500) || (z[i] < -500)) && (retry == 0));
                    }
                    entry.getValue().Points.TableModel.removeRow(0);
                    String[][] a = new String[nOfPointsToGen][2];
                    for (int j = 0; j < nOfPointsToGen; j++) {
                        a[j][0] = String.valueOf(x[j]);
                        a[j][1] = String.valueOf(y[j]);
                        entry.getValue().Points.TableModel.addRow(a[j]);
                    }
                    String[] b = new String[7];
                    b[0] = String.format("%.2f", A);
                    b[1] = String.format("%.2f", B);
                    b[2] = String.format("%.2f", C);
                    b[3] = String.format("%.2f", D);
                    b[4] = String.valueOf(getRandomInteger(0, 255));
                    b[5] = String.valueOf(getRandomInteger(0, 255));
                    b[6] = String.valueOf(getRandomInteger(0, 255));
                    entry.getValue().Poly.TableModel.removeRow(0);
                    entry.getValue().Poly.TableModel.addRow(b);
                }
            }
        });
    }

    private static final AtomicInteger tabCounter = new AtomicInteger();

    void createTab() {
        Integer tabId = tabCounter.incrementAndGet();
        String tabName = tabId.toString();

        JPanel panel = new JPanel(new GridLayout(2, 1));
        tabbedPane.addTab(tabName, panel);

        tabs.put(tabId, new Tab(new String[]{"A", "B", "C", "D", "R", "G", "B"},
                new String[]{"X", "Y"}));

        panel.add(tabs.get(tabId).Poly.Scroller);
        panel.add(tabs.get(tabId).Points.Scroller);

        frame2D.setVisible(true);
    }

    public Integer getRandomInteger(int min, int max) {
        double r = Math.random();
        int d = max - min;
        return (int) ((r * d) + min);
    }

    public Double getRandomDouble(int min, int max) {
        return ((Math.random() * (max - min)) + min);
    }

    private Double getNotZeroDouble(int min, int max) {
        double A;
        do {
            A = getRandomDouble(min, max);
        } while (A == 0);
        return A;
    }
}

class Tab {
    Table Poly;
    Table Points;

    Tab(String[] PolyHeaders, String[] PointsHeaders) {
        Poly = new Table(PolyHeaders);
        Points = new Table(PointsHeaders);
    }
}

class Table {
    String[] Headers;
    String[][] Data;
    DefaultTableModel TableModel;
    JTable Table;
    JTableHeader TableHeader;
    JScrollPane Scroller;

    Table(String[] Headers) {
        Font font = new Font("Verdana", Font.PLAIN, 24);
        Font fontHeaders = new Font("Verdana", Font.CENTER_BASELINE, 16);

        this.Headers = Headers; //{"A", "B", "C", "D", "R", "G", "B"};
        Data = new String[1][this.Headers.length];
        TableModel = new DefaultTableModel(Data, Headers);
        Table = new JTable(TableModel);
        TableHeader = Table.getTableHeader();
        TableHeader.setFont(fontHeaders);
        Table.setFont(font);
        Table.setAutoCreateRowSorter(true);
        Table.setRowHeight(Table.getRowHeight() + 10);
        Scroller = new JScrollPane(Table);
    }
}