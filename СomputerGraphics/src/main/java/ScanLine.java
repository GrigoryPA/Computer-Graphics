import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class ScanLine {

    ScreenData screen;
    Vector<Polyhedron> PolyhedronTable;
    Vector<Edge> EdgeTable;
    Map<Double, ArrayList<Edge>> GroupSortingTable;
    ArrayList<Edge> ActiveEdgesTable;
    double minKey = Double.MIN_VALUE;

    ScanLine(Vector<Polyhedron> polyhedronTable, Vector<Edge> edgeTable, ScreenData screen) {
        PolyhedronTable = polyhedronTable;
        this.screen = screen;
        for (Polyhedron polyhedron : PolyhedronTable) {
            polyhedron.createEdges();
        }
        EdgeTable = combineEdges();
        GroupSortingTable = sortEdges();
    }

    void ScanLine() {
        ActiveEdgesTable = cloneList(GroupSortingTable.get(minKey));

        Edge currentEdge = ActiveEdgesTable.get(0);
        int currentPolyhedronId = currentEdge.ownerPolyhedronId;
        Polyhedron currentPolyhedron = getOwnerPolyhedron(currentEdge);
        Color currentColor = currentPolyhedron.color;
        int N = 1; // number of active polyhedrons
        int prevPolyhedronId = currentPolyhedronId;

        for (int i = 0; i < ActiveEdgesTable.size(); i++) {
            currentEdge = ActiveEdgesTable.get(i);
            if (prevPolyhedronId == currentEdge.ownerPolyhedronId) {
                putPoints();
                currentColor = null;
                N = 0;
            } else {
                // x = x_min_j
                N += 1;

            }
        }

        AET_Step();

    }

    void putPoints(int start, int end, int y, Color currentColor) {
        for (int x = start; x < end; x++) {
            screen.pointsColor[x][y] = currentColor;
        }
    }

    Polyhedron getOwnerPolyhedron(Edge edge) {
        for (Polyhedron polyhedron : PolyhedronTable) {
            if (polyhedron.polyhedronId == edge.ownerPolyhedronId)
                return polyhedron;
        }
        return null;
    }

    void AET_Step() {

    }

    Vector<Edge> combineEdges() {
        Vector<Edge> combinedEdges = null;

        for (Polyhedron polyhedron : PolyhedronTable) {
            combinedEdges.addAll(polyhedron.edges);
        }

        return combinedEdges;
    }

    Map<Double, ArrayList<Edge>> sortEdges() {
        Map<Double, ArrayList<Edge>> groupedEdges = new HashMap<>();

        for (Edge edge : EdgeTable) {
            if (!groupedEdges.containsKey(edge.y_min)) {
                groupedEdges.put(edge.y_min, new ArrayList<>());
            }
            groupedEdges.get(edge.y_min).add(edge); // Надо сортировать по tg alpha
            if (groupedEdges.get(edge.y_min).size() > 1) {
                groupedEdges.get(edge.y_min).sort(new tanEdgeComparator());
            }
            if (minKey > edge.y_min)
                minKey = edge.y_min;
        }

        return groupedEdges;
    }

    public static ArrayList<Edge> cloneList(ArrayList<Edge> original) {
        ArrayList<Edge> clonedList = new ArrayList<>(original.size());
        for (Edge element : original) {
            clonedList.add(new Edge(element));
        }
        return clonedList;
    }
}

class Polyhedron {
    private static final AtomicInteger polyhedronCounter = new AtomicInteger();

    int polyhedronId;
    double A, B, C, D;
    int topScanString;

    {
        A = 0;
        B = 0;
        C = 0;
        D = 0;
        topScanString = 0;
    }

    Color color = Color.BLACK;
    Vector<Point3D> points;
    ArrayList<Edge> edges;


    public Polyhedron(double a, double b, double c, double d, Color color) {
        A = a;
        B = b;
        C = c;
        D = d;

        generatePoints(getRandomNumber(3, 6));
        createEdges();
        this.color = color;
        polyhedronId = polyhedronCounter.incrementAndGet();
    }

    Vector<Point3D> generatePoints(int n) { // генерация в точек в плоскости
        // нужен метод, который отсортирует точки в плоскости по часовой стрелке, иначе бред
        int min = 100, max = 600;

        for (int i = 0; i < n; i++) {
            double x;
            double y;
            double z;
            do {
                x = getRandomNumber(min, max);
                y = getRandomNumber(min, max);
                z = (D - (A * x) - (B * y)) / C;
            } while ((z > 400) || (z < -400));
            points.add(new Point3D(x, y, z));
        }
        return points;
    }

    void createEdges() {
        for (int i = 0; i < points.size() - 2; i++) {
            Point3D start = points.get(i);
            Point3D end = points.get(i + 1);
            edges.add(new Edge(start, end, this.polyhedronId));
        }
        Point3D start = points.get(points.size() - 1);
        Point3D end = points.get(0);
        edges.add(new Edge(start, end, this.polyhedronId));
    }

    int getGlobalMinimumZ(Edge e) {
        int min = ;
        if () {
            int z =
        } else {

        }

        return z;
    }

    public Integer getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}

class Edge {
    int ownerPolyhedronId;
    Point3D start, end;
    //double tg_alpha;

    public Edge(Point3D start, Point3D end, int ownerPolyhedronId) {
        this.ownerPolyhedronId = ownerPolyhedronId;
        this.start = new Point3D(start);
        this.end = new Point3D(end);
    }

    public Edge(Edge edge) {
        this.ownerPolyhedronId = edge.ownerPolyhedronId;
        this.start = new Point3D(edge.start);
        this.end = new Point3D(edge.end);
    }

}
/*
class tanEdgeComparator implements Comparator<Edge> {
    @Override
    public int compare(Edge o1, Edge o2) {
        if (o1.tg_alpha-o2.tg_alpha > 0)
            return 1;
        if (o1.tg_alpha-o2.tg_alpha < 0)
            return -1;
        return 0;
    }
}
 */