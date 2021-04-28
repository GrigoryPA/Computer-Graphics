import java.awt.*;

public class ScreenData {
    int width;
    int height;
    Color[][] pointsColor;

    public ScreenData(int width, int height, Color[][] pointsColor) {
        this.width = width;
        this.height = height;
        this.pointsColor = pointsColor;
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

    }

    public ScreenData(ScreenData s) {
        this.width = s.width;
        this.height = s.height;
        this.pointsColor = s.pointsColor;
    }
}
