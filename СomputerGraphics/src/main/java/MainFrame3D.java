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
	private JButton Draw;
	private DefaultTableModel tableModel;
	private JTable Table;
	private JScrollPane scroller; 
	private Figure3D figureRealTime;
    private Figure3D figureOriginal;
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

        AddRow.setToolTipText("Add row");
        DeleteRow.setToolTipText("Delete row");
        Draw.setToolTipText("Draw");

        toolBar1 = new JToolBar();
        toolBar1.add(AddRow);
        toolBar1.add(DeleteRow);
        toolBar1.add(Draw);
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
        frame2D.add(toolBar2, BorderLayout.SOUTH);

        
        frame2D.setVisible(true);




        
        Draw.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                display = new Display3D();
                display.AddCoordinateAxes();
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
