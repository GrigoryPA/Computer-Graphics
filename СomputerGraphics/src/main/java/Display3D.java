import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Display3D {
    public static Color[][] display;
    public static Color[][] aliasedDisplay;
    public static int widthVirtualDisplay = 2880;
    public static int heightVirtualDisplay = 1624;
    public static int widthImage = 1440;
    public static int heightImage = 812;
    public static int widthDelta = (widthVirtualDisplay - widthImage) / 2;
    public static int heightDelta = (heightVirtualDisplay - heightImage) / 2;
    public static int[] ZeroZero = {widthVirtualDisplay / 2, heightVirtualDisplay / 2};
    public static JFrame frame;
    public static JLabel label;
    public static BufferedImage image;
    public static ImageIcon imageicon;
    public static File file = new File("src/main/resources/canvasfullhd.jpg");

    public Display3D() {
        display = new Color[widthVirtualDisplay][heightVirtualDisplay];
        for (int i = 0; i < widthVirtualDisplay; i++)
            for (int j = 0; j < heightVirtualDisplay; j++) {
                display[i][j] = new Color(255, 255, 200);
            }
        return;
    }

    public static void Clear() {
        for (int i = 0; i < widthVirtualDisplay; i++)
            for (int j = 0; j < heightVirtualDisplay; j++) {
                display[i][j] = new Color(255, 255, 200);
            }
    }

    public static void AddPointOnDisplay(double x, double y, Color color) {
        display[ZeroZero[0] + (int) x][ZeroZero[1] + (int) y] = color;
    }

    public static void AddPointOnDisplayRT(int x, int y, Color color) {
        display[x + widthDelta][y + heightDelta] = color;
    }

    public void AddCoordinateAxesIsometric(double rotateX, double rotateY, Color colorX, Color colorY, Color colorZ) {
        Figure3D AxisX = new Figure3D(new double[][]{{0, 0, 0}, {250, 0, 0}});
        Figure3D AxisY = new Figure3D(new double[][]{{0, 0, 0}, {0, 250, 0}});
        Figure3D AxisZ = new Figure3D(new double[][]{{0, 0, 0}, {0, 0, 250}});

        AxisX.Rotate(rotateX, rotateY);
        AxisY.Rotate(rotateX, rotateY);
        AxisZ.Rotate(rotateX, rotateY);

        AxisX.IsometricProjection();
        AxisX.AddFigureOnDisplay(colorX);
        AxisY.IsometricProjection();
        AxisY.AddFigureOnDisplay(colorY);
        AxisZ.IsometricProjection();
        AxisZ.AddFigureOnDisplay(colorZ);
    }


    public void CreateAndOpenImage() {
        try {
            image = ImageIO.read(file);

            for (int x = 0; x < image.getWidth(); x++)
                for (int y = 0; y < image.getHeight(); y++) {
                    image.setRGB(x, y, display[x + widthDelta][heightVirtualDisplay - y - 1 - heightDelta].getRGB());
                }
            CreateFrameForImage(image);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to open image !");
        }

    }

    public void CreateAndOpenImage(boolean aliasing) {
        try {
            image = ImageIO.read(file);

            if (aliasing) {
                this.SSAO();
                widthDelta = widthDelta / 2;
                heightDelta = heightDelta / 2;
            }
            int a = image.getHeight();
            int b = image.getWidth();
            for (int x = 0; x < image.getWidth(); x++)
                for (int y = 0; y < image.getHeight(); y++) {
                    if (aliasing) {
                        image.setRGB(x, y, aliasedDisplay[x][heightVirtualDisplay/2 - y].getRGB());

                        if (y == 800)
                            a = 0;
                    }
                    else {

                        image.setRGB(x, y, display[x + widthDelta][heightVirtualDisplay - y - 1 - heightDelta].getRGB());
                    }
                }
            CreateFrameForImage(image);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to open image !");
        }

    }

    public void SSAO() {
        aliasedDisplay = new Color[widthVirtualDisplay / 2 + 1][heightVirtualDisplay / 2 + 1];
        for (int j = 0; j < heightVirtualDisplay - 2; j += 2) {
            for (int i = 0; i < widthVirtualDisplay - 2; i += 2) {
                int r = (display[i][j].getRed() + display[i + 1][j].getRed()
                        + display[i][j + 1].getRed() + display[i + 1][j + 1].getRed()) / 4;
                int g = (display[i][j].getGreen() + display[i + 1][j].getGreen()
                        + display[i][j + 1].getGreen() + display[i + 1][j + 1].getGreen()) / 4;
                int b = (display[i][j].getBlue() + display[i + 1][j].getBlue()
                        + display[i][j + 1].getBlue() + display[i + 1][j + 1].getBlue()) / 4;
                aliasedDisplay[i / 2][j / 2] = new Color(r, g, b);
            }
        }
    }

    public void UpdateImage() {
        for (int x = 0; x < image.getWidth(); x++)
            for (int y = 0; y < image.getHeight(); y++) {
                image.setRGB(x, y, display[x + widthDelta][heightVirtualDisplay - y - 1 - heightDelta].getRGB());
            }
        //imageicon.setImage(image);
        label.updateUI();
    }

    public void UpdateImage(boolean aliasing) {
        for (int x = 0; x < image.getWidth(); x++)
            for (int y = 0; y < image.getHeight(); y++) {
                if (aliasing)
                    image.setRGB(x, y, aliasedDisplay[x + widthDelta][heightVirtualDisplay - y - 1 - heightDelta].getRGB());
                else
                    image.setRGB(x, y, display[x + widthDelta][heightVirtualDisplay - y - 1 - heightDelta].getRGB());
            }
        //imageicon.setImage(image);
        label.updateUI();
    }

    public static void CreateFrameForImage(BufferedImage image) {
        if (frame == null) {
            frame = new JFrame();
            frame.setTitle("stained_image");
            frame.setSize(image.getWidth(), image.getHeight());
            frame.setLocation(500, 50);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            label = new JLabel();
            imageicon = new ImageIcon(image);
            label.setIcon(imageicon);
            frame.getContentPane().add(label, BorderLayout.CENTER);
            frame.setLocationRelativeTo(null);
            frame.pack();
            frame.setVisible(true);
        } else label.setIcon(new ImageIcon(image));
    }
}
