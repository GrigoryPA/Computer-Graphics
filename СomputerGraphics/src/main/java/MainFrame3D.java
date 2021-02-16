import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainFrame3D {
    private JFrame frame2D;
	private Display2D display;
	private JToolBar toolBar1;
	private JButton AddRow;
	private JButton DeleteRow;
	private JButton Draw;
	private DefaultTableModel tableModel;
	private JTable Table;
	private JScrollPane scroller; 
	private Figure2D figureRealTime;
    private Figure2D figureOriginal;
    private Curve2D curve;
	private JButton ScaleUp;
    private JButton ScaleDown;
    private JButton RotateRight;
    private JButton RotateLeft;
    private JButton ReflexionX;
    private JButton ReflexionY;
    private JButton Bezier;    private JToolBar toolBar2;
    public double deltaAngel = Math.PI/18;
    private double countRotateAngel=0;
    private int countRef=0;

    public void MakeAndShow() {


		frame2D =new JFrame("2D");
        frame2D.setBounds(450,50, 400, 450);
        frame2D.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        AddRow=new JButton(new ImageIcon("src/main/resources/icons/Add48x48.png"));
        DeleteRow=new JButton(new ImageIcon("src/main/resources/icons/Delete48x48.png"));
        Draw = new JButton(new ImageIcon("src/main/resources/icons/Rectangle48x48.png"));
        ScaleUp= new JButton(new ImageIcon("src/main/resources/icons/ScaleUp48x48.png"));
        ScaleDown= new JButton(new ImageIcon("src/main/resources/icons/ScaleDown48x48.png"));
        RotateRight = new JButton(new ImageIcon("src/main/resources/icons/RotateRight48x48.png"));
        RotateLeft= new JButton(new ImageIcon("src/main/resources/icons/RotateLeft48x48.png"));
        ReflexionX = new JButton(new ImageIcon("src/main/resources/icons/ReflectionX48x48.png"));
        ReflexionY= new JButton(new ImageIcon("src/main/resources/icons/ReflectionY48x48.png"));
        Bezier= new JButton(new ImageIcon("src/main/resources/icons/Bezier48x48.png"));

        AddRow.setToolTipText("Add row");
        DeleteRow.setToolTipText("Delete row");
        Draw.setToolTipText("Draw");
        ScaleUp.setToolTipText("Scale up");
        ScaleDown.setToolTipText("Scale down");
        RotateRight.setToolTipText("Rotate right");
        RotateLeft.setToolTipText("Rotate left");
        ReflexionX.setToolTipText("Reflexion X");
        ReflexionY.setToolTipText("Reflexion Y");
        Bezier.setToolTipText("Draw curve Bezier");

        toolBar1 = new JToolBar();
        toolBar1.add(AddRow);
        toolBar1.add(DeleteRow);
        toolBar1.add(Draw);
        toolBar1.add(Bezier);
        frame2D.add(toolBar1, BorderLayout.NORTH);


        String[] headers = {"X", "Y", "Z"};
        String [][] data;
        data=new String[1][3];
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
        toolBar2.add(RotateLeft);
        toolBar2.add(RotateRight);
        toolBar2.add(ScaleUp);
        toolBar2.add(ScaleDown);
        toolBar2.add(ReflexionX);
        toolBar2.add(ReflexionY);
        frame2D.add(toolBar2, BorderLayout.SOUTH);

        
        frame2D.setVisible(true);

        ReflexionX.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    display.Clear();
                    display.AddCoordinateAxes();
                    try {
                        figureRealTime.Reflexion( 2);
                        figureRealTime.AddFigure2DOnDisplay2D();
                        display.UpdateImage();
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "Figure cannot be reflected. Your figure is too big!");
                    }
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, "You need to draw figure first!");
                }
            }
        });

        ReflexionY.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            try {
                display.Clear();
                display.AddCoordinateAxes();
                try {
                    figureRealTime.Reflexion( 1);
                    figureRealTime.AddFigure2DOnDisplay2D();
                    display.UpdateImage();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, "Figure cannot be reflected. Your figure is too big!");
                }
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(null, "You need to draw figure first!");
            }
        }
        });

        RotateLeft.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    display.Clear();
                    display.AddCoordinateAxes();
                    figureRealTime = new Figure2D(Table);
                    try {
                        countRotateAngel +=-1*deltaAngel;
                        figureRealTime.Rotate( countRotateAngel);
                        figureRealTime.AddFigure2DOnDisplay2D();
                        display.UpdateImage();
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "Figure cannot be rotated. Your figure is too big!");
                    }
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, "You need to draw figure first!");
                }
            }
        });

        RotateRight.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    display.Clear();
                    display.AddCoordinateAxes();
                    figureRealTime = new Figure2D(Table);
                    try {
                        countRotateAngel +=1*deltaAngel;
                        figureRealTime.Rotate( countRotateAngel);
                        figureRealTime.AddFigure2DOnDisplay2D();
                        display.UpdateImage();
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "Figure cannot be rotated. Your figure is too big!");
                    }
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, "You need to draw figure first!");
                }
            }
        });


        AddRow.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                tableModel.addRow(new String[] {"",""});
            }
        });
        
        Draw.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                display = new Display2D();
                display.AddCoordinateAxes();
            	figureOriginal = new Figure2D(Table);
            	figureOriginal.AddFigure2DOnDisplay2D();
            	display.CreateAndOpenImage();
            	figureRealTime=figureOriginal;
            }
        });

        Bezier.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                display = new Display2D();
                display.AddCoordinateAxes();
                curve = new Curve2D(Table);
                curve.ResultPoints();
                curve.AddCurve2DOnDisplay2D();
                display.CreateAndOpenImage();
            }
        });
        
        ScaleUp.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    display.Clear();
                    display.AddCoordinateAxes();
                    try {
                        figureRealTime.ScaleUp(1);
                        figureRealTime.AddFigure2DOnDisplay2D();
                        display.UpdateImage();
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "Figure cannot be scaled up. Your figure is too big!");
                    }
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, "You need to draw figure first!");
                }
            }
        });

        ScaleDown.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    display.Clear();
                    display.AddCoordinateAxes();
                            figureRealTime.ScaleUp(-1);
                            figureRealTime.AddFigure2DOnDisplay2D();
                            display.UpdateImage();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, "You need to draw figure first!");
                }
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
