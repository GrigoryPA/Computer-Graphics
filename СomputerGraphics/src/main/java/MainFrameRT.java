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
        frame = new JFrame("RayTracing");
        frame.setBounds(50, 50, 450, 450);
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

        String[] headersS = {"X", "Y", "Z", "Radius", "Material"};
        SphereTab = new MyTable(headersS, "Spheres", 0);
        /*SphereTab.tableModel.setValueAt("-3",0,0);
        SphereTab.tableModel.setValueAt("0",0,1);
        SphereTab.tableModel.setValueAt("16",0,2);
        SphereTab.tableModel.setValueAt("2",0,3);
        SphereTab.tableModel.setValueAt("1",0,4);

        SphereTab.tableModel.setValueAt("-1",1,0);
        SphereTab.tableModel.setValueAt("-2",1,1);
        SphereTab.tableModel.setValueAt("12",1,2);
        SphereTab.tableModel.setValueAt("2",1,3);
        SphereTab.tableModel.setValueAt("4",1,4);

        SphereTab.tableModel.setValueAt("2",2,0);
        SphereTab.tableModel.setValueAt("-1",2,1);
        SphereTab.tableModel.setValueAt("18",2,2);
        SphereTab.tableModel.setValueAt("3",2,3);
        SphereTab.tableModel.setValueAt("2",2,4);

        SphereTab.tableModel.setValueAt("7",3,0);
        SphereTab.tableModel.setValueAt("5",3,1);
        SphereTab.tableModel.setValueAt("18",3,2);
        SphereTab.tableModel.setValueAt("4",3,3);
        SphereTab.tableModel.setValueAt("3",3,4);*/
        AllTabs.add(SphereTab);
        String[] headersL = {"X", "Y", "Z", "Intensity"};
        LightTab = new MyTable(headersL, "Lights", 4);
        LightTab.tableModel.setValueAt("100",0,0);
        LightTab.tableModel.setValueAt("50",0,1);
        LightTab.tableModel.setValueAt("70",0,2);
        LightTab.tableModel.setValueAt("10",0,3);
        LightTab.tableModel.setValueAt("-100",1,0);
        LightTab.tableModel.setValueAt("50",1,1);
        LightTab.tableModel.setValueAt("70",1,2);
        LightTab.tableModel.setValueAt("10",1,3);
        LightTab.tableModel.setValueAt("0",2,0);
        LightTab.tableModel.setValueAt("50",2,1);
        LightTab.tableModel.setValueAt("170",2,2);
        LightTab.tableModel.setValueAt("5",2,3);
        LightTab.tableModel.setValueAt("0",3,0);
        LightTab.tableModel.setValueAt("50",3,1);
        LightTab.tableModel.setValueAt("-30",3,2);
        LightTab.tableModel.setValueAt("10",3,3);
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
                    RayTracing3D RT = new RayTracing3D(AllTabs.elementAt(0).Table, AllTabs.elementAt(1).Table, display);
                    RT.RenderScene(display);
                    display.CreateAndOpenImage(true);
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
