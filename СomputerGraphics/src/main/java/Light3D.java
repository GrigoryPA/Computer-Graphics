import javax.swing.*;

public class Light3D {
    public double intensity;
    public Vector3d position;

    public Light3D(double I, int[] XYZ){
        intensity = I;
        position = new Vector3d(XYZ[0], XYZ[1], XYZ[2]);
    }

    public Light3D(JTable table, int row){
        position = new Vector3d(Integer.parseInt((String) table.getValueAt(row, 0)),
                Integer.parseInt((String) table.getValueAt(row, 1)),
                Integer.parseInt((String) table.getValueAt(row, 2)));
        intensity = Integer.parseInt((String) table.getValueAt(row, 3))/10;
    }
}
