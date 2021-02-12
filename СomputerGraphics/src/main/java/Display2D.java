import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class Display2D {
	protected static int[][] display; 
	protected static int N=700; 
	protected static int M=500;
	private static int[] ZeroZero= {350,250};
	private static JFrame frame;
	private static JLabel label;
	private static BufferedImage image;
	private static ImageIcon imageicon;
	private static Color Color0 = new Color(0, 0, 0);//background
	private static Color Color1 = new Color(255, 255, 255);//axis
	private static Color Color2 = new Color(50, 255, 50);//for draw
	private static File file = new File("src/main/resources/convas.png");
	
	public  Display2D() {
		display=new int[N][M];
		for(int i=0;i<N;i++)
			for(int j=0;j<M;j++) {
				display[i][j]=0;
			}
		return;
	}

	public void Clear() {
		for(int i=0;i<N;i++)
			for(int j=0;j<M;j++) {
				display[i][j]=0;
			}
	}
	
	public static void AddPointOnDisplay(int x, int y, int color) {
		display[ZeroZero[0]+x][ZeroZero[1]+y]=color;
	}
	
	public void AddCoordinateAxes() {
		for(int i=0;i<N;i++) {
			for(int j=0;j<M;j++) {
				if(i==ZeroZero[0] || j==ZeroZero[1]) display[i][j]=1;
				if((i==ZeroZero[0]-1||i==ZeroZero[0]-2) && j%10==0) display[i][j]=1;
				if((j==ZeroZero[1]-1||j==ZeroZero[1]-2) && i%10==0) display[i][j]=1;
			}
		}
	}
	
	
	public void CreateAndOpenImage() {
		try {
			image = ImageIO.read(file);

			for(int x=0;x<image.getWidth();x++) 
				for(int y=0;y<image.getHeight();y++) {
					if(display[x][M-y-1]==0)image.setRGB(x, y, Color0.getRGB());
					if(display[x][M-y-1]==1)image.setRGB(x, y, Color1.getRGB());
					if(display[x][M-y-1]==2)image.setRGB(x, y, Color2.getRGB());
			}
			CreateFrameForImage(image);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Не удалось открыть изображение!");
		}
		
	}

	public void UpdateImage() {
			for(int x=0;x<image.getWidth();x++)
				for(int y=0;y<image.getHeight();y++) {
					if(display[x][M-y-1]==0)image.setRGB(x, y, Color0.getRGB());
					if(display[x][M-y-1]==1)image.setRGB(x, y, Color1.getRGB());
					if(display[x][M-y-1]==2)image.setRGB(x, y, Color2.getRGB());
				}
				//imageicon.setImage(image);
				label.updateUI();
	}
	
	
	public static void CreateFrameForImage(BufferedImage image){
	   if(frame==null){
	       frame=new JFrame();
	       frame.setTitle("stained_image");
	       frame.setSize(image.getWidth(), image.getHeight());
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
