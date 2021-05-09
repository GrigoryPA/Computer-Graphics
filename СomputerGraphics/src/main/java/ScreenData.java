import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ScreenData {
    static int width;
    static int height;
    static Color[][] pointsColor;
    static Point Zero;
    private static JFrame frame;
    private BufferedImage image;
    public static File file;

    public ScreenData(int width, int height, Color[][] pointsColor) {
        this.width = width;
        this.height = height;
        this.pointsColor = pointsColor;
        Zero = new Point(width / 2, height / 2);
    }

    public ScreenData(int width, int height) {
        this.width = width;
        this.height = height;
        this.pointsColor = new Color[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                this.pointsColor[i][j] = Color.WHITE;
            }
        }
        Zero = new Point(width / 2, height / 2);
    }

    public ScreenData(ScreenData s) {
        this.width = s.width;
        this.height = s.height;
        this.pointsColor = s.pointsColor;
    }

    public static void AddPointOnDisplay(double x, double y, Color color) {
        pointsColor[Zero.x + (int) x][Zero.y + (int) y] = color;
    }

    public void CreateAndOpenImage() {
        file = new File("src/main/resources/canvas.png");
        try {
            System.out.println(file.getAbsolutePath());
            image = ImageIO.read(file);

            for (int x = 0; x < image.getWidth(); x++)
                for (int y = 0; y < image.getHeight(); y++) {
                    image.setRGB(x, y, pointsColor[x][height - y - 1].getRGB());
                }
            CreateFrameForImage(image);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to open image !");
        }
    }

    public static void CreateFrameForImage(BufferedImage image) {
        JLabel label = new JLabel();
        if (frame == null) {
            frame = new JFrame();
            frame.setTitle("stained_image");
            frame.setSize(image.getWidth(), image.getHeight());
            frame.setLocation(500, 50);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            label = new JLabel();
            ImageIcon imageicon = new ImageIcon(image);
            label.setIcon(imageicon);
            frame.getContentPane().add(label, BorderLayout.CENTER);
            frame.setLocationRelativeTo(null);
            frame.pack();
            frame.setVisible(true);
        } else label.setIcon(new ImageIcon(image));
    }
}
