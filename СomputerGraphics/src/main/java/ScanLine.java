import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Math.abs;

public class ScanLine {

    ScreenData screen;
    Vector<Polyhedron> PolyhedronTable;
    Vector<Edge> EdgeTable;
    Map<Integer, ArrayList<Polyhedron>> ScanGroupTable;
    ArrayList<Polyhedron> ActiveLinePolyTable;
    ArrayList<EdgeData> ActiveLineEdgeTable;
    Integer minKey = Integer.MIN_VALUE;

    ScanLine(Vector<Polyhedron> polyhedronTable, Vector<Edge> edgeTable, ScreenData screen) {
        PolyhedronTable = polyhedronTable;
        this.screen = screen;
        for (Polyhedron polyhedron : PolyhedronTable) {
            polyhedron.createEdges();
        }
        EdgeTable = combineEdges();
        //GroupSortingTable = sortEdges();
    }

    void ScanLine() {
        ActiveLinePolyTable = cloneList(ScanGroupTable.get(minKey));
        ActiveLineEdgeTable = createALET();

    }

    private ArrayList<EdgeData> createALET() {
        ArrayList<EdgeData> ActiveLineEdgeTable;


        return ActiveLineEdgeTable;
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

    Map<Integer, ArrayList<Polyhedron>> sortEdges() {
        Map<Integer, ArrayList<Polyhedron>> groupedPolyhedrons = new HashMap<>();

        for (Polyhedron polyhedron : PolyhedronTable) {
            if (!groupedPolyhedrons.containsKey(polyhedron.topScanString)) {
                groupedPolyhedrons.put(polyhedron.topScanString, new ArrayList<>());
            }
            groupedPolyhedrons.get(polyhedron.topScanString).add(polyhedron);
        }

        return groupedPolyhedrons;
    }

    /*
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
     */

    public static ArrayList<Polyhedron> cloneList(ArrayList<Polyhedron> original) {
        ArrayList<Polyhedron> clonedList = new ArrayList<>(original.size());
        for (Polyhedron element : original) {
            clonedList.add(new Polyhedron(element));
        }
        return clonedList;
    }

    class EdgeData {
        Edge edge;
        int x;
        double delta_x;
        int delta_y;
        int flag;

        EdgeData(Edge edge, int scanLine) {
            this.edge = edge;
            x = getIntersectionX(edge, scanLine);
            delta_x = getDeltaX(edge, scanLine);
            delta_y = abs((int) (edge.start.y - edge.end.y));
            flag = 0;
        }

        int getIntersectionX(Edge edge, int scanLine) {
            double a = (edge.start.y - edge.end.y) / (edge.start.x - edge.end.x);
            double b = edge.start.y - (a * edge.start.x);
            double c = 0;
            double d = scanLine;
            Double x = (d - c) / (a - b);
            return x.intValue();
        }

        double getDeltaX(Edge edge, int scanLine) {
            double a = (edge.start.y - edge.end.y) / (edge.start.x - edge.end.x);
            double b = edge.start.y - (a * edge.start.x);
            double delta_x = ((scanLine - b) / a) - ((scanLine + 1 - b) / a);
            return delta_x;
        }
    }
}

class Polyhedron {
    private static final AtomicInteger polyhedronCounter = new AtomicInteger();

    int polyhedronId;
    double A, B, C, D;
    int topScanString;
    int botScanString;
    int totalScanStrings;

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

        this.color = color;
        polyhedronId = polyhedronCounter.incrementAndGet();

        generatePoints(getRandomNumber(3, 6));
        createEdges();
        getTopScanString();
        getBotScanString();
        getTotalScanStrings();
    }

    public Polyhedron(Polyhedron p) {
        A = p.A;
        B = p.B;
        C = p.C;
        D = p.D;

        color = p.color;
        polyhedronId = p.polyhedronId;

        points = p.points;
        edges = p.edges;
        topScanString = p.topScanString;
        botScanString = p.botScanString;
        totalScanStrings = p.totalScanStrings;
    }

    Vector<Point3D> generatePoints(int n) { // генерация в точек в плоскости
        // нужен метод, который отсортирует точки в плоскости по часовой стрелке, иначе бред
        // однако если генерить только треугольники, то такой проблемы нет
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

    void getTopScanString() {
        topScanString = Integer.MIN_VALUE;
        for (Edge edge : edges) {
            if (edge.start.y > topScanString)
                topScanString = (int) edge.start.y;
        }
    }

    void getBotScanString() {
        botScanString = Integer.MAX_VALUE;
        for (Edge edge : edges) {
            if (edge.start.y < botScanString)
                botScanString = (int) edge.start.y;
        }
    }

    void getTotalScanStrings() {
        totalScanStrings = topScanString - botScanString + 1;
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