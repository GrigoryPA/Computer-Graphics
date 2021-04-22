import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrameCS extends MainFrame2D {
    private JFrame frame2D;
    private DisplayCS displayCS;
    private FigureCS figureRealTime;
    private FigureCS figureOriginal;
    private Curve2D curve;
    public double deltaAngel = Math.PI / 18;
    private double countRotateAngel = 0;
    private int countRef = 0;

    public void MakeAndShow() {
        frame2D = new JFrame("Cohen-Sutherland");
        frame2D.setBounds(50, 50, 400, 450);
        frame2D.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton addRow = new JButton(new ImageIcon("src/main/resources/icons/Add48x48.png"));
        JButton deleteRow = new JButton(new ImageIcon("src/main/resources/icons/Delete48x48.png"));
        JButton draw = new JButton(new ImageIcon("src/main/resources/icons/Rectangle48x48.png"));
        JButton random = new JButton(new ImageIcon("src/main/resources/icons/Bezier48x48.png"));
        JButton check = new JButton(new ImageIcon("src/main/resources/icons/DrawBezierPlane48x48.png"));

        addRow.setToolTipText("Add row");
        deleteRow.setToolTipText("Delete row");
        draw.setToolTipText("Draw");
        random.setToolTipText("Create random segments");
        check.setToolTipText("Run Cohen-Sutherland algorithm");

        JToolBar toolBar1 = new JToolBar();
        toolBar1.add(addRow);
        toolBar1.add(deleteRow);
        toolBar1.add(random);
        toolBar1.add(draw);
        toolBar1.add(check);
        frame2D.add(toolBar1, BorderLayout.NORTH);

        Font font = new Font("Verdana", Font.PLAIN, 24);
        Font fontHeaders = new Font("Verdana", Font.CENTER_BASELINE, 16);

        String[] rectangleHeaders = {"X1", "Y1", "X2", "Y2"};
        String[][] rectangleData;
        rectangleData = new String[1][4];
        DefaultTableModel rectangleTableModel = new DefaultTableModel(rectangleData, rectangleHeaders);
        JTable rectangleTable = new JTable(rectangleTableModel);
        JTableHeader rectangleTableHeader = rectangleTable.getTableHeader();
        rectangleTableHeader.setFont(fontHeaders);
        rectangleTable.setFont(font);
        rectangleTable.setAutoCreateRowSorter(true);
        rectangleTable.setRowHeight(rectangleTable.getRowHeight() + 10);

        String[] segmentHeaders = {"X1", "Y1", "X2", "Y2"};
        String[][] segmentData;
        segmentData = new String[1][4];
        DefaultTableModel segmentTableModel = new DefaultTableModel(segmentData, segmentHeaders);
        JTable segmentTable = new JTable(segmentTableModel);
        JTableHeader segmentTableHeader = segmentTable.getTableHeader();
        segmentTableHeader.setFont(fontHeaders);
        segmentTable.setFont(font);
        segmentTable.setAutoCreateRowSorter(true);
        segmentTable.setRowHeight(segmentTable.getRowHeight() + 10);


        JScrollPane rectangleScroller = new JScrollPane(rectangleTable);
        JScrollPane segmentScroller = new JScrollPane(segmentTable);
        JPanel panel = new JPanel(new GridLayout(2,1));
        panel.add(rectangleScroller);
        panel.add(segmentScroller);
        frame2D.add(panel, BorderLayout.CENTER);

        frame2D.setVisible(true);

        addRow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                segmentTableModel.addRow(new String[]{"", "", "", ""});
            }
        });

        deleteRow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] rows = segmentTable.getSelectedRows();
                if (rows.length > 0) {
                    segmentTableModel.removeRow(rows[0]);
                } else
                    JOptionPane.showMessageDialog(frame2D, "You must first select the rows.", "Failed to delete rows.", 0);
            }
        });

        draw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayCS = new DisplayCS(new Color(217, 217, 217));
                //displayCS.AddCoordinateAxes();
                figureOriginal = new FigureCS(segmentTable, rectangleTable);
                figureOriginal.AddFigure2DOnDisplayCS(Color.black);
                displayCS.CreateAndOpenImage();
                figureRealTime = figureOriginal;
            }
        });

       check.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayCS = new DisplayCS();
                //displayCS.AddCoordinateAxes();
                figureOriginal = new FigureCS(segmentTable, rectangleTable);
                figureOriginal.DrawCohenSutherland(Color.black, Color.blue);
                displayCS.CreateAndOpenImage();
                //displayCS.UpdateImage();
                figureRealTime = figureOriginal;
            }
        });

        random.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                /* Для тестов
                rectangleTableModel.removeRow(0);
                rectangleTableModel.addRow(new String[]{"-200", "-150", "200", "150"});

                int a = segmentTableModel.getRowCount();
                for (int i = 0; i < a; i++) {
                    segmentTableModel.removeRow(0);
                    int b = segmentTableModel.getRowCount();
                }
                segmentTableModel.addRow(new String[]{"-40", "-15", "220", "220"});
                //rectangleTableModel.addRow(new String[]{"-200", "-150", "200", "150"});
                */
                rectangleTableModel.removeRow(0);
                rectangleTableModel.addRow(new String[]{"-200", "-150", "200", "150"});

                int a = segmentTableModel.getRowCount();
                for (int i = 0; i < a; i++) {
                    segmentTableModel.removeRow(0);
                    int b = segmentTableModel.getRowCount();
                }
                for (int i = 0; i < 10; i++) {
                    String[] temp = new String[4];
                    for (int j = 0; j < temp.length; j += 2) {
                        temp[j] = getRandomNumber(-350, 350).toString();
                        temp[j+1] = getRandomNumber(-250, 250).toString();
                    }
                    segmentTableModel.addRow(temp);
                }
            }
        });
    }

    public Integer getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}

