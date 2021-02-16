import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Display3D {
	protected static Color[][] display;
	protected static int N=700;
	protected static int M=500;
	private static int[] ZeroZero= {350,250};
	private static JFrame frame;
	private static JLabel label;
	private static BufferedImage image;
	private static ImageIcon imageicon;
	private static File file = new File("src/main/resources/convas.png");

	public Display3D() {
		display=new Color[N][M];
		for(int i=0;i<N;i++)
			for(int j=0;j<M;j++) {
				display[i][j]=new Color(255,255, 200);
			}
		return;
	}

	public void Clear() {
		for(int i=0;i<N;i++)
			for(int j=0;j<M;j++) {
				display[i][j]=new Color(255,255, 200);
			}
	}
	
	public static void AddPointOnDisplay(double x, double y, Color color) {
		display[ZeroZero[0]+(int)x][ZeroZero[1]+(int)y]=color;
	}
	
	public void AddCoordinateAxes() {
		Figure3D AxisX = new Figure3D(new double[][]{{0,0,0},{250,0,0}}, new Color(255,0,0));
		AxisX.IsometricProjection();
		AxisX.AddFigureOnDisplay();
		Figure3D AxisY = new Figure3D(new double[][]{{0,0,0},{0,250,0}}, new Color(0,255,0));
		AxisY.IsometricProjection();
		AxisY.AddFigureOnDisplay();
		Figure3D AxisZ = new Figure3D(new double[][]{{0,0,0},{0,0,250}}, new Color(0,0,255));
		AxisZ.IsometricProjection();
		AxisZ.AddFigureOnDisplay();
	}
	
	
	public void CreateAndOpenImage() {
		try {
			image = ImageIO.read(file);

			for(int x=0;x<image.getWidth();x++) 
				for(int y=0;y<image.getHeight();y++) {
					image.setRGB(x, y, display[x][M-y-1].getRGB());
			}
			CreateFrameForImage(image);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Failed to open image !");
		}
		
	}

	public void UpdateImage() {
			for(int x=0;x<image.getWidth();x++)
				for(int y=0;y<image.getHeight();y++) {
					image.setRGB(x, y, display[x][M-y-1].getRGB());
				}
				//imageicon.setImage(image);
				label.updateUI();
	}
	
	
	public static void CreateFrameForImage(BufferedImage image){
	   if(frame==null){
	       frame=new JFrame();
	       frame.setTitle("stained_image");
	       frame.setSize(image.getWidth(), image.getHeight());
		   frame.setLocation(500, 50);
	       frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	       label=new JLabel();
	       imageicon=new ImageIcon(image);
	       label.setIcon(imageicon);
	       frame.getContentPane().add(label,BorderLayout.CENTER);
	       frame.setLocationRelativeTo(null);
	       frame.pack(); 
	       frame.setVisible(true);
	   }else label.setIcon(new ImageIcon(image));
	}
}
