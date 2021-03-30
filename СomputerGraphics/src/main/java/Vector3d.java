public class Vector3d {
    public double x, y, z;

    public Vector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3d() {
    }

    public double getModule() {
        return Math.sqrt(x*x + y*y + z*z);
    }

    public Vector3d getAddition(Vector3d vector) {
        return new Vector3d(x + vector.x, y + vector.y, z + vector.z);
    }

    public Vector3d getSubtraction(Vector3d vector) {
        return new Vector3d(x - vector.x, y - vector.y, z - vector.z);
    }

    public double getScalar(Vector3d vector) {
        return x * vector.x + y * vector.y + z * vector.z;
    }

    public Vector3d getVector(Vector3d vector) {
        return new Vector3d(y * vector.z - z * vector.y, z * vector.x - x * vector.z, x * vector.y - y * vector.x);
    }

    public Vector3d getVectorScaled(double k) {
        return new Vector3d(x*k, y*k, z*k);
    }

    public Vector3d normalize() {
        double L = this.getModule();
        return new Vector3d(x/L, y/L, z/L);
    }

    public Vector3d cross(Vector3d v) {
        return new Vector3d(y*v.z - z*v.y, z*v.x - x*v.z, x*v.y - y*v.x);
    }

    @Override
    public String toString() {
        return "{" + x + "; " + y + "; " + z + "}";
    }

}