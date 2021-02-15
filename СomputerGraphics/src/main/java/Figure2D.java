import javax.swing.JOptionPane;
import javax.swing.JTable;

public class Figure2D {
	public int[][] points;
	public double countScale = 1;

	public Figure2D(JTable Table) {
		points = new int[Table.getRowCount()][2];
		try {
			for(int i=0; i<Table.getRowCount(); i++) {
						points[i][0]=Integer.parseInt((String) Table.getValueAt(i, 0));
						points[i][1]=Integer.parseInt((String) Table.getValueAt(i, 1));
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "All values should be integer!\nBlank cells and rows are not allowed!");
		}
	}
	
	public void AddFigure2DOnDisplay2D() {
		Line2D.AddLineSigmentOnDisplayBresenham(points[0][0], points[0][1],points[points.length-1][0], points[points.length-1][1]);
		for(int i=0;i<points.length-1;i++)
			Line2D.AddLineSigmentOnDisplayBresenham(points[i][0],points[i][1],points[i+1][0],points[i+1][1]);
	}

	public void ScaleUp(int updown) {
		double[][] b = new double[2][2];
		try {
			if(updown==-1 && countScale>=2) {
				countScale--;
				b[0][0] = countScale / (countScale + 1);  b[0][1] = 0;
				b[1][0] = 0;                              b[1][1] = countScale / (countScale + 1);
				points = Multiply(b);
			}
			else if(updown==1){
				countScale++;
				b[0][0] = countScale / (countScale - 1);  b[0][1] = 0;
				b[1][0] = 0;                              b[1][1] = countScale / (countScale - 1);
				points = Multiply(b);
			}
			System.out.println(countScale+"   "+updown);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null,
					"All values should be integer!\nBlank cells and rows are not allowed");
		}
	}

	public void Rotate( double countRotateAngel) {
		double[][] b = new double[2][2];
				b[0][0]= Math.cos(countRotateAngel); b[0][1]=Math.sin(countRotateAngel);
				b[1][0]=-(Math.sin(countRotateAngel)); b[1][1]=Math.cos(countRotateAngel);
				points=Multiply(b);
		}

	public void Reflexion( int axis) {
		double[][] b = new double[2][2];
			if(axis==2) {
				b[0][0] = -1; b[0][1] = 0;
				b[1][0] = 0; b[1][1] = 1;
			}
			else{
				b[0][0] = 1; b[0][1] = 0;
				b[1][0] = 0; b[1][1] = -1;
			}

		points=Multiply(b);
	}
	
	public int[][] Multiply(double[][] b) {
		int[][] c=new int[points.length][2];
		for (int i=0; i<points.length; ++i)
		    for (int j=0; j<2; ++j)
		    	c[i][j] = (int)(points[i][0] * b[0][j] + points[i][1] * b[1][j]) ;
		return c;
	}
}
