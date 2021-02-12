import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class Figure2D {
	public int[][] points;
	public double sinDelta = 0.173648;//sin(10`)
	public double cosDelta = 0.984808;//cos(10`)
	
	public Figure2D(JTable Table) {
		points = new int[Table.getRowCount()][2];
		try {
			for(int i=0; i<Table.getRowCount(); i++) {
						points[i][0]=Integer.parseInt((String) Table.getValueAt(i, 0));
						points[i][1]=Integer.parseInt((String) Table.getValueAt(i, 1));
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Все значения должны быть целочисленные!\nНе должно быть пустых строк или ячеек!");
		}
	}
	
	public void AddFigure2DOnDisplay2D() {
		Line2D.AddLineSigmentOnDisplayBresenham(points[0],points[points.length-1]);
		for(int i=0;i<points.length-1;i++)
			Line2D.AddLineSigmentOnDisplayBresenham(points[i],points[i+1]);
	}

	public void ScaleUp(JTextField searchSpace) {
		double[][] b = new double[2][2];
		try {
			int K = Integer.parseInt(searchSpace.getText());
			if(K>=0) {
				b[0][0]=K; b[0][1]=0;
				b[1][0]=0; b[1][1]=K;
				points=Multiply(b);
			}
			else
				JOptionPane.showMessageDialog(null, "Все значени ядолжны быть целочисленные!\nНе должно быть пустых строк или ячеек!");
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Все значени ядолжны быть целочисленные!\nНе должно быть пустых строк или ячеек!");
		}
	}

	public void Rotate(int clock) {
		double[][] b = new double[2][2];
				b[0][0]= cosDelta; b[0][1]=clock*sinDelta;
				b[1][0]=-(clock*sinDelta); b[1][1]=cosDelta;
				points=Multiply(b);
				for (int i=0; i<points.length; ++i)
					System.out.println(points[i][0]+"; "+points[i][1]);
		}
	
	public int[][] Multiply(double[][] b) {
		int[][] c=new int[points.length][2];
		for (int i=0; i<points.length; ++i)
		    for (int j=0; j<2; ++j)
		    	c[i][j] = (int)(points[i][0] * b[0][j] + points[i][1] * b[1][j]) ;
		return c;
	}
}
