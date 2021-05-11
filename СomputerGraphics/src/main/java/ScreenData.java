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
    private static JLabel label;
    private BufferedImage image = null;
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

    public static void AddPointOnDisplay(int x, int y, Color color) {
        int _x = x;
        int _y = y;
        if (_y >= height / 2)
            _y = height / 2 - 1;
        if (_x >= width / 2)
            _x = width / 2 - 1;
        if (_y <= -height / 2)
            _y = -height / 2 + 1;
        if (_x < -width / 2)
            _x = -width / 2;
        int final_x = Zero.x + _x;
        int final_y = Zero.y + _y;

        pointsColor[Zero.x + _x][Zero.y + _y] = color;
    }

    public void CreateAndOpenImage() {
        file = new File("src/main/resources/picture.png");
        try {

            if (image == null) {
                image = ImageIO.read(file);

                for (int x = 0; x < image.getWidth(); x++)
                    for (int y = 0; y < image.getHeight(); y++) {
                        int a;
                        if (x < 0)
                            a = 0;
                        if (height - y < 0)
                            a = 1;
                        if (x >= 30)
                            a = 1;
                        if (height - y >= 30)
                            a = 1;
                        image.setRGB(x, y, pointsColor[x][height - y - 1].getRGB());
                    }
                /*
                for (int x = 14; x < 16; x++)
                    for (int y = 14; y < 16; y++) {
                        int a;
                        if (x < 0)
                            a = 0;
                        if (height - y < 0)
                            a = 1;
                        if (x >= 30)
                            a = 1;
                        if (height - y >= 30)
                            a = 1;
                        //image.setRGB(x, y, Color.BLACK.getRGB());
                    }
                */
                CreateFrameForImage(image);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to open image !");
        }
    }

    public static void CreateFrameForImage(BufferedImage image) {
        //if (frame == null) {
        frame = new JFrame();
        frame.setTitle("stained_image");
        frame.setSize(image.getWidth(), image.getHeight());
        frame.setLocation(500, 50);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        label = new JLabel();
        ImageIcon imageicon = new ImageIcon(image);
        label.setIcon(imageicon);
        frame.getContentPane().add(label, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
        //} else label.setIcon(new ImageIcon(image));
    }
}
