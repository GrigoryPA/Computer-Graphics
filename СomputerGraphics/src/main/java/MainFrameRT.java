import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class MainFrameRT {
    private JFrame frame;
    public Display3D display = new Display3D();
    private JToolBar toolBar1;
    private JButton AddRow;
    private JButton DeleteRow;
    private JButton DrawRayTracing;
    private JTabbedPane tablePanel;
    private Vector<MyTable> AllTabs = new Vector<MyTable>();
    private MyTable SphereTab;
    private MyTable LightTab;
    private Vector TableModels = new Vector();

    public void MakeAndShow() {
        frame = new JFrame("2D");
        frame.setBounds(450, 50, 500, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        AddRow = new JButton(new ImageIcon("src/main/resources/icons/Add48x48.png"));
        DeleteRow = new JButton(new ImageIcon("src/main/resources/icons/Delete48x48.png"));
        DrawRayTracing = new JButton(new ImageIcon("src/main/resources/icons/RotateLeft48x48.png"));

        AddRow.setToolTipText("Add row");
        DeleteRow.setToolTipText("Delete row");
        DrawRayTracing.setToolTipText("Draw with ray tracing");

        toolBar1 = new JToolBar();
        toolBar1.add(AddRow);
        toolBar1.add(DeleteRow);
        toolBar1.add(DrawRayTracing);
        frame.add(toolBar1, BorderLayout.NORTH);

        String[] headersS = {"X", "Y", "Z", "Radius", "R", "G", "B", "Specular"};
        SphereTab = new MyTable(headersS, "Spheres", 1);
        AllTabs.add(SphereTab);
        String[] headersL = {"X", "Y", "Z", "Intensity"};
        LightTab = new MyTable(headersL, "Lights", 1);
        AllTabs.add(LightTab);
        tablePanel = new JTabbedPane();
        tablePanel.add(SphereTab.scroller,"Spheres");
        tablePanel.add(LightTab.scroller,"Lights");
        frame.add(tablePanel, BorderLayout.CENTER);

        frame.setVisible(true);


        DrawRayTracing.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                //try {
                    display.Clear();
                    RayTracing3D.RenderSpheres(AllTabs.elementAt(0).Table, AllTabs.elementAt(1).Table, display);
                    display.CreateAndOpenImage();
                //}
                //catch(Exception e1){}
            }
        });

        AddRow.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {

                int c = getCurTab();
                    AllTabs.elementAt(c).tableModel.addRow(new String[AllTabs.elementAt(c).tableModel.getColumnCount()]);
            }
        });

        DeleteRow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int c = getCurTab();
                int[] rows=AllTabs.elementAt(c).Table.getSelectedRows();

                if (rows.length>0) {
                        AllTabs.elementAt(c).tableModel.removeRow(rows[0]);
                }
                else
                    JOptionPane.showMessageDialog(frame, "You must first select the rows.", "Failed to delete rows.", 0);
            }
        });
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
