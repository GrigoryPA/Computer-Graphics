import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;


public class MainFrame3D {
    private JFrame frame;
	private Display3D display;
	private JToolBar toolBar1;
	private JButton AddRow;
	private JButton DeleteRow;
	private JButton DrawIsometric;
    private JButton DrawDimetric;
    private JButton AddRowForBezier;
    private JButton DeleteRowForBezier;
    private JButton DrawBezierPlane;
    private JTabbedPane tablePanel;
	private DefaultTableModel tableModel;
	private JTable Table;
	private JLabel MainLabel;
    private Vector<MyTable>  AllTabs = new Vector<MyTable>();
    private MyTable OneTab;
    private Vector TableModels = new Vector();
	private JScrollPane scroller; 
	private Figure3D figureRealTime;
    private Figure3D figureOriginal;
    private Bezier3D bezierPlaneOriginal;
    private Bezier3D bezierPlaneRealTime;
    private Curve2D curve;
	private JButton ScaleUp;
    private JButton ScaleDown;
    private JButton RotateRightX;
    private JButton RotateLeftX;
    private JButton RotateRightY;
    private JButton RotateLeftY;
    private JButton ReflexionX;
    private JButton ReflexionY;
    private JButton Bezier;
    private JButton DrawRayTracing;
    private JToolBar toolBar2;
    public double deltaAngel = Math.PI/18;
    private double countRotateAngelX=0;
    private double countRotateAngelY=0;
    private int countRef=0;
    private int BezierRowsAmount = 1;
    private boolean bOnScreen;//false - ������, true - ����������� �����

    public void MakeAndShow() {


		frame =new JFrame("2D");
        frame.setBounds(450,50, 500, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        AddRow=new JButton(new ImageIcon("src/main/resources/icons/Add48x48.png"));
        DeleteRow=new JButton(new ImageIcon("src/main/resources/icons/Delete48x48.png"));
        DrawIsometric = new JButton(new ImageIcon("src/main/resources/icons/Rectangle48x48.png"));
        AddRowForBezier = new JButton(new ImageIcon("src/main/resources/icons/AddRowForBezier48x48.png"));
        DeleteRowForBezier = new JButton(new ImageIcon("src/main/resources/icons/DeleteRowForBezier48x48.png"));
        DrawBezierPlane = new JButton(new ImageIcon("src/main/resources/icons/DrawBezierPlane48x48.png"));
        RotateRightX = new JButton(new ImageIcon("src/main/resources/icons/RotateRight48x48.png"));
        RotateLeftX = new JButton(new ImageIcon("src/main/resources/icons/RotateLeft48x48.png"));
        RotateRightY = new JButton(new ImageIcon("src/main/resources/icons/RotateRight48x48.png"));
        RotateLeftY = new JButton(new ImageIcon("src/main/resources/icons/RotateLeft48x48.png"));
        DrawRayTracing = new JButton(new ImageIcon("src/main/resources/icons/RotateLeft48x48.png"));


        AddRow.setToolTipText("Add row");
        DeleteRow.setToolTipText("Delete row");
        DrawIsometric.setToolTipText("Draw isometric");
        AddRowForBezier.setToolTipText("Add row for bezier plane");
        DeleteRowForBezier.setToolTipText("Delete row for bezier plane");
        DrawBezierPlane.setToolTipText("Draw bezier plane");
        RotateRightX.setToolTipText("Rotate right X");
        RotateLeftX.setToolTipText("Rotate left X");
        RotateRightY.setToolTipText("Rotate right Y");
        RotateLeftY.setToolTipText("Rotate left Y");
        DrawRayTracing.setToolTipText("Draw with ray tracing");

        toolBar1 = new JToolBar();
        toolBar1.add(AddRow);
        toolBar1.add(DeleteRow);
        toolBar1.add(DrawIsometric);
        toolBar1.add(AddRowForBezier);
        toolBar1.add(DeleteRowForBezier);
        toolBar1.add(DrawBezierPlane);
        toolBar1.add(DrawRayTracing);
        frame.add(toolBar1, BorderLayout.NORTH);


        String[] headers = {"X1", "Y1", "Z1","X2", "Y2", "Z2"};
        OneTab = new MyTable(headers, "Main", 1);
        AllTabs.add(OneTab);
        tablePanel = new JTabbedPane();
        tablePanel.add(OneTab.scroller,"Main", 0);
        frame.add(tablePanel, BorderLayout.CENTER);

        toolBar2 = new JToolBar();
        toolBar2.add(RotateLeftX);
        toolBar2.add(RotateRightX);
        toolBar2.add(RotateLeftY);
        toolBar2.add(RotateRightY);
        frame.add(toolBar2, BorderLayout.SOUTH);

        
        frame.setVisible(true);

        AddRowForBezier.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String[] headers = {"X", "Y", "Z"};
                int defaultrowscount;
                if (BezierRowsAmount == 1) defaultrowscount = 1;
                else {
                    defaultrowscount = AllTabs.elementAt(1).tableModel.getRowCount();
                }
                String TableName = BezierRowsAmount + " row";
                OneTab = new MyTable(headers, TableName, defaultrowscount);
                AllTabs.add(OneTab);
                tablePanel.add(OneTab.scroller, OneTab.tableName, BezierRowsAmount);
                BezierRowsAmount++;
            }
        });

        DeleteRowForBezier.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (BezierRowsAmount == 1)
                {
                    return;
                }
                tablePanel.remove(tablePanel.getComponentAt(BezierRowsAmount - 1));
                AllTabs.remove(BezierRowsAmount - 1);
                BezierRowsAmount--;
            }
        });



        RotateLeftX.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                RotateObject(0,0);//������ �������� 0 - X���, 1 - Y���. ������ �������� 0 - �����, 1 - ������
            }
        });

        RotateRightX.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                RotateObject(0, 1);//������ �������� 0 - X���, 1 - Y���. ������ �������� 0 - �����, 1 - ������
            }
        });

        RotateLeftY.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                RotateObject(1,0);//������ �������� 0 - X���, 1 - Y���. ������ �������� 0 - �����, 1 - ������
                }
        });

        RotateRightY.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                RotateObject(1,1);//������ �������� 0 - X���, 1 - Y���. ������ �������� 0 - �����, 1 - ������
            }
        });

        DrawIsometric.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                countRotateAngelX=0;
                countRotateAngelY=0;
                display = new Display3D();
                display.AddCoordinateAxesIsometric(0,0, Color.red, Color.green, Color.blue);
            	figureOriginal = new Figure3D(AllTabs.elementAt(0).Table);
                figureOriginal.IsometricProjection();
            	figureOriginal.AddFigureOnDisplay(Color.BLACK);
            	display.CreateAndOpenImage();
            	bOnScreen = false;
            }
        });

        DrawBezierPlane.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                countRotateAngelX=0;
                countRotateAngelY=0;
                display = new Display3D();
                display.AddCoordinateAxesIsometric(0,0,Color.RED, Color.GREEN, Color.BLUE);
                bezierPlaneOriginal = new Bezier3D(AllTabs);
                bezierPlaneOriginal.IsometricProjection();
                bezierPlaneOriginal.AddFigureOnDisplay(Color.BLACK, Color.MAGENTA);
                display.CreateAndOpenImage();
                bOnScreen = true;
                }
        });


        AddRow.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {

                int c = getCurTab();

                if (c == 0)
                {
                    AllTabs.elementAt(c).tableModel.addRow(new String[] {"","","","","",""});
                }
                else {
                    for (int i = 1;i < AllTabs.size();i++) {
                        AllTabs.elementAt(i).tableModel.addRow(new String[]{"", "", ""});
                    }
                }
            }
        });

        DeleteRow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int c = getCurTab();
            	int[] rows=AllTabs.elementAt(c).Table.getSelectedRows();

    			if (rows.length>0) {

    			    if (c == 0) {
                        AllTabs.elementAt(c).tableModel.removeRow(rows[0]);
                    }
    			    else
                    {
                        for (int i = 1; i < AllTabs.size();i++)
                        {
                            AllTabs.elementAt(i).tableModel.removeRow(rows[0]);
                        }
                    }
    			}
    			else
    				JOptionPane.showMessageDialog(frame, "You must first select the rows.", "Failed to delete rows.", 0);
            }
        });
	}

	public void RotateObject(int coordinate, int direction) {

        try {
            try {
                if (coordinate == 0) {//��������� ����� ���� � ���� ������
                    if (direction == 0) {
                        countRotateAngelX -= deltaAngel;
                    } else {
                        countRotateAngelX += deltaAngel;
                    }
                } else {
                    if (direction == 0) {
                        countRotateAngelY -= deltaAngel;
                    } else {
                        countRotateAngelY += deltaAngel;
                    }
                }
                //������ ������ ��� �����������
                if (bOnScreen == false) {
                        display.Clear();
                        figureRealTime = new Figure3D(Table);
                        figureRealTime.Rotate(countRotateAngelX, countRotateAngelY);
                        figureRealTime.IsometricProjection();
                        display.AddCoordinateAxesIsometric(0, 0, Color.red, Color.green, Color.blue);
                        figureRealTime.AddFigureOnDisplay(Color.black);
                        display.UpdateImage();
                }
                else
                    {
                        display.Clear();
                        bezierPlaneRealTime = new Bezier3D(bezierPlaneOriginal);
                        bezierPlaneRealTime.Rotate(countRotateAngelX, countRotateAngelY);
                        bezierPlaneRealTime.IsometricProjection();
                        display.AddCoordinateAxesIsometric(0, 0, Color.red, Color.green, Color.blue);
                        bezierPlaneRealTime.AddFigureOnDisplay(Color.BLACK, Color.MAGENTA);
                        display.UpdateImage();
                    }
                }
            catch (Exception e1) {
                JOptionPane.showMessageDialog(null, "Figure cannot be rotated. Your figure is too big!");
            }
        }
        catch (Exception e1) {
            JOptionPane.showMessageDialog(null, "You need to draw figure first!");
        }
    }

    public int getCurTab() {
        int returnTab = 10;
        for (int i = 0; i < AllTabs.size(); i++) {
            if (AllTabs.elementAt(i).Table.isShowing() == true)
                returnTab = i;
        }
        return returnTab;
    }
	
}
