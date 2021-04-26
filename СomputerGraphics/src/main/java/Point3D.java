import java.awt.*;

public class Point3D{
    protected double x;
    protected double y;
    protected double z;
    protected double k;

    Point3D(double x, double y, double z, double k) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.k = k;
    }

    Point3D(Point3D p) {
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
        this.k = p.k;
    }
}
