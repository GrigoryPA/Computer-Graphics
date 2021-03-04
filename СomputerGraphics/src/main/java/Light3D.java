import javax.swing.*;

public class Light3D {
    public double intensity;
    public int[] position;

    public Light3D(double I, int[] XYZ){
        intensity = I;
        position = XYZ;
    }

    public Light3D(JTable table, int row){
        position = new int[]{Integer.parseInt((String) table.getValueAt(row, 0)),
                Integer.parseInt((String) table.getValueAt(row, 1)),
                Integer.parseInt((String) table.getValueAt(row, 2))};
        intensity = Integer.parseInt((String) table.getValueAt(row, 3));
    }
}
