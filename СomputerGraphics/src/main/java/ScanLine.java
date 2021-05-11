import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Math.abs;

public class ScanLine {
    ScreenData screen;
    Vector<Polyhedron> PolyhedronTable;
    //Vector<Edge> EdgeTable;
    Map<Integer, ArrayList<Polyhedron>> ScanGroupTable;
    ArrayList<Polyhedron> ActiveLinePolyTable;
    ArrayList<EdgeData> ActiveLineEdgeTable;
    ArrayList<EdgeData> ALET;
    Integer topLine = Integer.MIN_VALUE;
    private int botLine = Integer.MAX_VALUE;

    private static class visibleSegment {
        private int polyId;

        visibleSegment(int polyId) {
            this.polyId = polyId;
        }

        public int getPolyId() {
            return polyId;
        }
    }

    ScanLine(Vector<Polyhedron> polyhedronTable, ScreenData screen) {
        PolyhedronTable = polyhedronTable;
        this.screen = screen;
        for (Polyhedron polyhedron : PolyhedronTable) {
            polyhedron.createEdges();
        }
        createScanGroupTable();
    }

    void run() {
        // Алгоритм начинает работу с верхней строки
        ActiveLinePolyTable = new ArrayList<>();
        ActiveLineEdgeTable = new ArrayList<>();
        int a;
        for (int scanLine = topLine; scanLine > botLine /*-screen.height / 2*/; scanLine--) {
            System.out.println("");
            System.out.print("scanLine = " + scanLine + " |");
            addALPT(scanLine);
            addALET(scanLine);
            sortALET();
            processEdges(scanLine);
            correctALET(scanLine);
            correctALPT();
        }
    }

    private void addALPT(int scanLine) {
        if (ScanGroupTable.get(scanLine) != null)
            for (Polyhedron p : ScanGroupTable.get(scanLine)) {
                boolean alreadyContains = false;
                for (Polyhedron l : ActiveLinePolyTable) {
                    if (l.polyhedronId == p.polyhedronId)
                        alreadyContains = true;
                }
                if (alreadyContains == false)
                    ActiveLinePolyTable.add(p);
            }
    }

    private void addALET(Integer scanLine) {
        for (Polyhedron p : ActiveLinePolyTable) {
            for (Edge e : p.edges) {
                boolean contains = false;
                for (EdgeData ed : ActiveLineEdgeTable) {
                    if (ed.edge.equals(e))
                        contains = true;
                }
                if ((contains == false) && (isBetween((int) e.start.y, (int) e.end.y, scanLine)))
                    ActiveLineEdgeTable.add(new EdgeData(e, scanLine));
            }
        }
        int temp;
        if (scanLine == 0)
            temp = 1;

        for (int i = 0; i < ActiveLineEdgeTable.size(); i++) {
            EdgeData ed1 = ActiveLineEdgeTable.get(i);
            for (int j = 0; j < ActiveLineEdgeTable.size(); j++) {
                EdgeData ed2 = ActiveLineEdgeTable.get(j);
                double a = ed1.x;
                double b = ed2.x;
                boolean equal = false;
                if (isBetween(a + 0.01, a - 0.01, b))
                    equal = true;
                //if ((ed1.x == ed2.x) && !(ed1.equals(ed2))) {
                if ((equal) && !(ed1.equals(ed2))) {
                    if (ed1.used == true) {
                        if (ed2.used == true) {
                            ;
                        } else {
                            ActiveLineEdgeTable.remove(ed1);
                            i--;
                            j--;
                        }
                    } else if (ed2.used == true) {
                        if (ed1.used == true) {
                            ;
                        } else {
                            ActiveLineEdgeTable.remove(ed2);
                            i--;
                            j--;
                        }
                    }
                }
            }
        }
    }

    private void sortALET() {
        ActiveLineEdgeTable.sort(new xEdgeDataComparator());
    }

    public static boolean isBetween(int a, int b, int checkedInt) {
        return b > a ? checkedInt >= a && checkedInt <= b : checkedInt >= b && checkedInt <= a;
    }

    public static boolean isBetween(double a, double b, double checkedDouble) {
        return b > a ? checkedDouble >= a && checkedDouble <= b : checkedDouble >= b && checkedDouble <= a;
    }

    void processEdges(int scanLine) {
        int x_left = -screen.width / 2;
        int x_right = 0;
        int x_max = screen.width / 2;
        int polyCounter = 0;
        ALET = cloneList(ActiveLineEdgeTable);
        visibleSegment visibleSegment = new visibleSegment(-1);

        int i = 0;
        while (ALET.size() > i) {
            EdgeData current = null;
            EdgeData right = null;

            current = getEdgeData(ALET, i);
            i++;
            right = getRight(current, scanLine);
            //if (ALET.size() > 1)
            //   right = ALET.get(1);
            //ALET.remove(0);

            x_right = (int) Math.round(current.x);
            if (polyCounter == 0) {
                //видимый отрезок - фон
                visibleSegment.polyId = -1;
            } else {
                if (polyCounter > 1) {
                    visibleSegment.polyId = getPolyLeftZMax(scanLine, x_left).polyhedronId;
                } else {
                    visibleSegment.polyId = getActivePolys().get(0).polyhedronId; //current.getPolyByEdgeData().polyhedronId;
                }
            }
            // изменить признак активности многоугольника
            if (right != null) {
                if (right.flag == 0)
                    right.flag = 1;
                else
                    right.flag = 0;
            } else
                current.flag = 0;
            if ((right == null) || (right.flag == 0))
                polyCounter--;
            else
                polyCounter++;

            print(x_left, x_right, visibleSegment.polyId, scanLine);
            x_left = x_right;
        }

        if (x_left < x_max) {
            x_right = x_max;
            visibleSegment.polyId = -1;
            print(x_left, x_right, visibleSegment.polyId, scanLine);
        }
        return;
    }

    EdgeData getEdgeData(ArrayList<EdgeData> a, int i) {
        a.get(i).usedThisLine = true;
        return a.get(i);
    }

    private EdgeData getRight(EdgeData left, int scanLine) {
        EdgeData right = null;
        Polyhedron p = getOwnerPolyhedron(left.edge);
        Edge e_r = null;
        for (Edge e : p.edges) {
            if (!(e.equals(left.edge))) {
                boolean contains = false;
                for (int i = 0; i < ALET.size(); i++) {
                    if (ALET.get(i).edge.equals(e))
                        contains = true;
                }
                if (contains)
                    if ((isBetween((int) e.start.y, (int) e.end.y, scanLine)))
                        if (e_r == null)
                            e_r = e;
                        else {
                            double top_e_r;
                            double top_e;
                            if (e.start.y > e.end.y)
                                top_e = e.start.y;
                            else
                                top_e = e.end.y;
                            if (e_r.start.y > e_r.end.y)
                                top_e_r = e_r.start.y;
                            else
                                top_e_r = e_r.end.y;

                            if (top_e < top_e_r)
                                e_r = e;
                        }
            }
        }
        right = getEdgeDataByEdge(e_r);
        if (right != null) {
            if (right.usedThisLine == false)
                return right;
            else
                return null;
        } else
            return null;
    }

    private ArrayList<Polyhedron> getActivePolys() {
        ArrayList<Polyhedron> polyhedronArrayList = new ArrayList<>();
        for (EdgeData e : ALET) {
            if (e.flag == 1)
                polyhedronArrayList.add(e.getPolyByEdgeData());
        }
        return polyhedronArrayList;
    }

    private EdgeData getEdgeDataByEdge(Edge e) {
        for (EdgeData ed : ALET) {
            if (ed.edge.equals(e))
                return ed;
        }
        return null;
    }

    private void print(int start, int end, int polyId, int scanLine) {
        Color color;
        System.out.print(" " + start + " " + end + " " + polyId + " |");
        if (polyId == -1)
            color = new Color(255, 255, 255);
        else
            color = getPolyByIdALPT(polyId).color;
        for (int i = start; i <= end; i++) {
            screen.AddPointOnDisplay(i, scanLine, color);
        }
    }

    private Polyhedron getPolyLeftZMax(int scanLine, int x_left) { // to do
        Polyhedron r = null;
        int x = x_left;
        int y = scanLine;
        double z_max = -Double.MAX_VALUE;
        ArrayList<Polyhedron> ActivePolys = getActivePolys();
        for (Polyhedron p : ActivePolys) {
            double z = (p.D - (p.A * x) - (p.B * y)) / p.C;
            if (z > z_max) {
                z_max = z;
                r = p;
            }
        }
        return r;
    }

    private void correctALET(int scanLine) {
        for (int i = 0; i < ActiveLineEdgeTable.size(); i++) {
            EdgeData e = ActiveLineEdgeTable.get(i);
            e.getDelta_y(scanLine);
            if (e.delta_y < 0) {
                ActiveLineEdgeTable.remove(e);
                i--;
            }
            e.flag = 0;
            e.used = true;
            e.correctX();
        }
    }

    private void correctALPT() {
        for (Polyhedron p : ActiveLinePolyTable) {
            p.decreaseTotalScanStrings();
            if (p.totalScanStrings < 0)
                ActiveLinePolyTable.remove(p);
        }
    }

    void createScanGroupTable() {
        ScanGroupTable = new HashMap<>();
        topLine = Integer.MIN_VALUE;
        for (Polyhedron p : PolyhedronTable) {
            if (!ScanGroupTable.containsKey(p.topScanString)) { // Если для этой строки ранее не были найдены многоугольники
                ScanGroupTable.put(p.topScanString, new ArrayList<>()); // создать для неё список
            }
            ScanGroupTable.get(p.topScanString).add(p); // добавить многоугольник в список
            if (p.topScanString > topLine)
                topLine = p.topScanString;
            if (p.botScanString < botLine)
                botLine = p.botScanString;
        }
    }

    Polyhedron getOwnerPolyhedron(Edge edge) {
        for (Polyhedron polyhedron : PolyhedronTable) {
            if (polyhedron.polyhedronId == edge.ownerPolyhedronId)
                return polyhedron;
        }
        return null;
    }

    Polyhedron getPolyByIdALPT(int polyId) {
        for (Polyhedron p : ActiveLinePolyTable) {
            if (polyId == p.polyhedronId)
                return p;
        }
        return null;
    }

    public ArrayList<EdgeData> cloneList(ArrayList<EdgeData> original) {
        ArrayList<EdgeData> clonedList = new ArrayList<>(original.size());
        for (EdgeData element : original) {
            clonedList.add(new EdgeData(element));
        }
        return clonedList;
    }


    class EdgeData {
        Edge edge;
        double x;
        double delta_x;
        int delta_y;
        int flag;
        boolean used = false;
        boolean usedThisLine = false;

        EdgeData(Edge edge, int scanLine) {
            this.edge = edge;
            getIntersectionX(scanLine);
            getDelta_x(scanLine);
            getDelta_y(scanLine);
            flag = 0;
        }

        EdgeData(EdgeData edgeData) {
            this.edge = edgeData.edge;
            this.x = edgeData.x;
            this.delta_x = edgeData.delta_x;
            this.delta_y = edgeData.delta_y;
            this.flag = edgeData.flag;
        }

        void getIntersectionX(int scanLine) {
            double a = (edge.start.y - edge.end.y) / (edge.start.x - edge.end.x);
            double b = edge.start.y - (a * edge.start.x);
            double c = 0;
            double d = scanLine;
            Double _x = (d - b) / (a - c);

            x = truncDouble(_x);
            //System.out.println("getIntersectionX - x = " + x + " | Edge p1:" + "(" + edge.start.x + ", " + edge.start.y + ") p2:" + "(" + edge.end.x + ", " + edge.end.y + ")");
        }

        void getDelta_x(int scanLine) {
            double a = (edge.start.y - edge.end.y) / (edge.start.x - edge.end.x);
            double b = edge.start.y - (a * edge.start.x);
            delta_x = ((scanLine - b) / a) - ((scanLine + 1 - b) / a);
            delta_x = truncDouble(delta_x);
            //System.out.println("getDelta_X - x = " + delta_x + " | Edge p1:" + "(" + edge.start.x + ", " + edge.start.y + ") p2:" + "(" + edge.end.x + ", " + edge.end.y + ")");
        }

        void getDelta_y(int scanLine) {
            int top;
            int bot;
            if (edge.start.y > edge.end.y) {
                top = (int) edge.start.y;
                bot = (int) edge.end.y;
            } else {
                top = (int) edge.end.y;
                bot = (int) edge.start.y;
            }
            if (bot >= scanLine)
                delta_y = -1;
            else if (top > scanLine)
                delta_y = scanLine - bot;
            else
                delta_y = top - bot;
        }

        void correctX() {
            x += delta_x;
            x = truncDouble(x);
        }

        Polyhedron getPolyByEdgeData() {
            for (Polyhedron p : ActiveLinePolyTable) {
                if (p.edges.contains(this.edge))
                    return p;
            }
            return null;
        }

        private Double truncDouble(double d) {
            Double truncatedDouble = BigDecimal.valueOf(d)
                    .setScale(4, RoundingMode.HALF_UP)
                    .doubleValue();
            return truncatedDouble;
        }
    }

    class xEdgeDataComparator implements Comparator<EdgeData> {
        @Override
        public int compare(EdgeData o1, EdgeData o2) {
            if (o1.x - o2.x > 0)
                return 1;
            if (o1.x - o2.x < 0)
                return -1;
            return 0;
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

        generatePoints(3/*getRandomNumber(3, 6)*/);
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

    public Polyhedron(double a, double b, double c, double d, Color color, Point3D[] point3D) {
        A = a;
        B = b;
        C = c;
        D = d;

        this.color = color;
        polyhedronId = polyhedronCounter.incrementAndGet();

        generatePoints(point3D/*getRandomNumber(3, 6)*/);
        createEdges();
        getTopScanString();
        getBotScanString();
        getTotalScanStrings();
    }

    void generatePoints(int n) { // генерация в точек в плоскости
        // нужен метод, который отсортирует точки в плоскости по часовой стрелке, иначе бред
        // однако если генерить только треугольники, то такой проблемы нет
        int min = -200, max = 200;

        points = new Vector<>();

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
    }

    void generatePoints(Point3D[] point3D) { // генерация в точек в плоскости по данным из таблицы
        int min = -200, max = 200;

        points = new Vector<>();

        for (int i = 0; i < point3D.length; i++) {
            double x;
            double y;
            double z;

            x = point3D[i].x;
            y = point3D[i].y;
            z = (D - (A * x) - (B * y)) / C;
            points.add(new Point3D(x, y, z));
        }
    }

    void createEdges() {
        edges = new ArrayList<>();
        for (int i = 0; i < points.size() - 1; i++) {
            Point3D start = points.get(i);
            Point3D end = points.get(i + 1);
            edges.add(new Edge(start, end, this.polyhedronId));
        }
        Point3D start = points.get(points.size() - 1);
        Point3D end = points.get(0);
        edges.add(new Edge(start, end, this.polyhedronId));
    }

    private void getTopScanString() {
        topScanString = Integer.MIN_VALUE;
        for (Edge edge : edges) {
            if (edge.start.y > topScanString)
                topScanString = (int) edge.start.y;
        }
    }

    private void getBotScanString() {
        botScanString = Integer.MAX_VALUE;
        for (Edge edge : edges) {
            if (edge.start.y < botScanString)
                botScanString = (int) edge.start.y;
        }
    }

    private void getTotalScanStrings() {
        totalScanStrings = topScanString - botScanString + 1;
    }

    void decreaseTotalScanStrings() {
        topScanString--;
    }

    private Integer getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}

class Edge {
    int ownerPolyhedronId;
    Point3D start, end;

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