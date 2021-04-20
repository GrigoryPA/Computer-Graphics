import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DisplayCS extends Display2D {

    public DisplayCS() {
        display = new Color[N][M];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < M; j++) {
                display[i][j] = new Color(217, 217, 217);
            }
        return;
    }

    public DisplayCS(Color color) {
        back = color;
        display = new Color[N][M];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < M; j++) {
                display[i][j] = back;
            }
        return;
    }
}
