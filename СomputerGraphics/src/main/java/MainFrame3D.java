import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainFrame3D {
    private JFrame frame2D;
	private Display3D display;
	private JToolBar toolBar1;
	private JButton AddRow;
	private JButton DeleteRow;
	private JButton DrawIsometric;
    private JButton DrawDimetric;
	private DefaultTableModel tableModel;
	private JTable Table;
	private JScrollPane scroller; 
	private Figure3D figureRealTime;
    private Figure3D figureOriginal;
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
    private JToolBar toolBar2;
    public double deltaAngel = Math.PI/18;
    private double countRotateAngelX=0;
    private double countRotateAngelY=0;
    private int countRef=0;

    public void MakeAndShow() {


		frame2D =new JFrame("2D");
        frame2D.setBounds(450,50, 400, 450);
        frame2D.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        AddRow=new JButton(new ImageIcon("src/main/resources/icons/Add48x48.png"));
        DeleteRow=new JButton(new ImageIcon("src/main/resources/icons/Delete48x48.png"));
        DrawIsometric = new JButton(new ImageIcon("src/main/resources/icons/Rectangle48x48.png"));
        RotateRightX = new JButton(new ImageIcon("src/main/resources/icons/RotateRight48x48.png"));
        RotateLeftX = new JButton(new ImageIcon("src/main/resources/icons/RotateLeft48x48.png"));
        RotateRightY = new JButton(new ImageIcon("src/main/resources/icons/RotateRight48x48.png"));
        RotateLeftY = new JButton(new ImageIcon("src/main/resources/icons/RotateLeft48x48.png"));

        AddRow.setToolTipText("Add row");
        DeleteRow.setToolTipText("Delete row");
        DrawIsometric.setToolTipText("Draw isometric");
        RotateRightX.setToolTipText("Rotate right X");
        RotateLeftX.setToolTipText("Rotate left X");
        RotateRightY.setToolTipText("Rotate right Y");
        RotateLeftY.setToolTipText("Rotate left Y");

        toolBar1 = new JToolBar();
        toolBar1.add(AddRow);
        toolBar1.add(DeleteRow);
        toolBar1.add(DrawIsometric);
        frame2D.add(toolBar1, BorderLayout.NORTH);


        String[] headers = {"X1", "Y1", "Z1","X2", "Y2", "Z2"};
        String [][] data;
        data=new String[1][6];
        tableModel=new DefaultTableModel(data, headers);
        Table=new JTable(tableModel);
        Font font = new Font("Verdana", Font.PLAIN, 24);
        Font fontHeaders = new Font("Verdana", Font.CENTER_BASELINE, 16);
        JTableHeader tableHeader = Table.getTableHeader();
        tableHeader.setFont(fontHeaders);
        Table.setFont(font);
        Table.setAutoCreateRowSorter(true);
        Table.setRowHeight(Table.getRowHeight()+10);
        scroller=new JScrollPane(Table);
        frame2D.add(scroller, BorderLayout.CENTER);


        toolBar2 = new JToolBar();
        toolBar2.add(RotateLeftX);
        toolBar2.add(RotateRightX);
        toolBar2.add(RotateLeftY);
        toolBar2.add(RotateRightY);
        frame2D.add(toolBar2, BorderLayout.SOUTH);

        
        frame2D.setVisible(true);


        RotateLeftX.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    display.Clear();
                    figureRealTime = new Figure3D(Table);
                    try {
                        countRotateAngelX -=deltaAngel;
                        figureRealTime.Rotate( countRotateAngelX, countRotateAngelY);
                        figureRealTime.IsometricProjection();
                        display.AddCoordinateAxesIsometric(0, 0, Color.lightGray, Color.lightGray, Color.lightGray);
                        display.AddCoordinateAxesIsometric(countRotateAngelX, countRotateAngelY, Color.red, Color.green, Color.blue);
                        figureRealTime.AddFigureOnDisplay(Color.BLACK);
                        display.UpdateImage();
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "Figure cannot be rotated. Your figure is too big!");
                    }
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, "You need to draw figure first!");
                }
            }
        });

        RotateRightX.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    display.Clear();
                    figureRealTime = new Figure3D(Table);
                    try {
                        countRotateAngelX +=deltaAngel;
                        figureRealTime.Rotate( countRotateAngelX, countRotateAngelY);
                        figureRealTime.IsometricProjection();
                        display.AddCoordinateAxesIsometric(0, 0, Color.lightGray, Color.lightGray, Color.lightGray);
                        display.AddCoordinateAxesIsometric(countRotateAngelX, countRotateAngelY, Color.red, Color.green, Color.blue);
                        figureRealTime.AddFigureOnDisplay(Color.BLACK);
                        display.UpdateImage();
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "Figure cannot be rotated. Your figure is too big!");
                    }
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, "You need to draw figure first!");
                }
            }
        });

        RotateLeftY.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    display.Clear();
                    figureRealTime = new Figure3D(Table);
                    try {
                        countRotateAngelY -=deltaAngel;
                        figureRealTime.Rotate( countRotateAngelX, countRotateAngelY);
                        figureRealTime.IsometricProjection();
                        display.AddCoordinateAxesIsometric(0, 0, Color.lightGray, Color.lightGray, Color.lightGray);
                        display.AddCoordinateAxesIsometric(countRotateAngelX, countRotateAngelY, Color.red, Color.green, Color.blue);
                        figureRealTime.AddFigureOnDisplay(Color.BLACK);
                        display.UpdateImage();
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "Figure cannot be rotated. Your figure is too big!");
                    }
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, "You need to draw figure first!");
                }
            }
        });

        RotateRightY.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    display.Clear();
                    figureRealTime = new Figure3D(Table);
                    try {
                        countRotateAngelY +=deltaAngel;
                        figureRealTime.Rotate( countRotateAngelX, countRotateAngelY);
                        figureRealTime.IsometricProjection();
                        display.AddCoordinateAxesIsometric(0, 0, Color.lightGray, Color.lightGray, Color.lightGray);
                        display.AddCoordinateAxesIsometric(countRotateAngelX, countRotateAngelY, Color.red, Color.green, Color.blue);
                        figureRealTime.AddFigureOnDisplay(Color.black);
                        display.UpdateImage();
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "Figure cannot be rotated. Your figure is too big!");
                    }
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, "You need to draw figure first!");
                }
            }
        });




        DrawIsometric.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                display = new Display3D();
                display.AddCoordinateAxesIsometric(0,0, Color.red, Color.green, Color.blue);
            	figureOriginal = new Figure3D(Table);
                figureOriginal.IsometricProjection();
            	figureOriginal.AddFigureOnDisplay(Color.BLACK);
            	display.CreateAndOpenImage();
            }
        });




        AddRow.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                tableModel.addRow(new String[] {"","",""});
            }
        });

        DeleteRow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	int[] rows=Table.getSelectedRows();
    			if (rows.length>0) {
    				tableModel.removeRow(rows[0]);
    			}
    			else
    				JOptionPane.showMessageDialog(frame2D, "You must first select the rows.", "Failed to delete rows.", 0);
            }
        });
	}
	
	
}
