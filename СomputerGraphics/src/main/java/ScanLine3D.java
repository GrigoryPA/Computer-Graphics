import java.awt.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/*
    1. Создать таблицы многоугольников и ребёр
        1.1. Создать таблицу многоугольников
            1.1.1. Вычислить коэффициенты A, B, C, D уравнения плоскости и Color грани.
        1.2. Создать таблицу ребёр
            1.2.1. Занести в неё все негоризонтальные рёбра Y_min != Y_max.
                   Y_min, X_min, Y_max, X_max, tg_alpha (= delta_Y / delta_X).
    2.
        2.1. Сортировка ребёр по возрастанию Y_min и объединение в таблицу групповой сортировки.
             Эта таблица содержит столько строк, сколько строк имеет дисплей. Может иметь пустые строки.
        2.2. Если Y_min равны, дополнительно отсортировать по убыванию tg_alpha.
    3.
        3.1. Формирование таблицы активных рёбер, для которых Y_tek = Y_min_i
    4. Обработка ТАР
        4.1.
            4.1.1. Считывание параметров первоого ребра. Определение соответствующего ребру i номер многоугольника в ТМ,
                   и его цвет.
            4.1.2. Поднимается флаг этого многоугольника.
            4.1.3. В соответствии с этими данными определяется цвет сканирующего луча — луч подсвечивается соответствующим
             цветом при значении x_min_i.
            4.1.4. Количество поднятых флагов N активных многоугольников устанавливается равным 1, N = 1.
        4.2.
            4.2.1. Отвратительно написанный алгоритм, честное слово.
*/
public class ScanLine3D {
    Vector<Polyhedron> PolyhedronTable;
    Vector<Edge> EdgeTable;
    Map<Double, ArrayList<Edge>> GroupSortingTable;
    ArrayList<Edge> ActiveEdgesTable;
    double minKey = Double.MIN_VALUE;

    ScanLine3D(Vector<Polyhedron> polyhedronTable, Vector<Edge> edgeTable) {
        PolyhedronTable = polyhedronTable;
        for (Polyhedron polyhedron: PolyhedronTable) {
            polyhedron.createEdges();
        }
        EdgeTable = combineEdges();
        GroupSortingTable = sortEdges();
    }

    void ScanLine() {
        ActiveEdgesTable = cloneList(GroupSortingTable.get(minKey));

        Edge currentEdge = ActiveEdgesTable.get(0);
        Polyhedron currentPolyhedron = getOwnerPolyhedron(currentEdge);
        Color currentColor = currentPolyhedron.color;
        int N = 1; // number of active polyhedrons



        AET_Step();

    }

    Polyhedron getOwnerPolyhedron(Edge edge) {
        for (Polyhedron polyhedron: PolyhedronTable) {
            if (polyhedron.polyhedronId == edge.ownerPolyhedronId)
                return polyhedron;
        }
        return null;
    }

    void AET_Step() {

    }

    Vector<Edge> combineEdges() {
        Vector<Edge> combinedEdges = null;

        for (Polyhedron polyhedron: PolyhedronTable) {
            combinedEdges.addAll(polyhedron.edges);
        }

        return  combinedEdges;
    }

    Map<Double, ArrayList<Edge>> sortEdges() {
        Map<Double, ArrayList<Edge>> groupedEdges = new HashMap<>();

        for (Edge edge: EdgeTable) {
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
    {
        A = 0;
        B = 0;
        C = 0;
        D = 0;
    }
    Color color = Color.BLACK;
    Vector<Point3D> points;
    Vector<Edge> edges;

    public Polyhedron(double a, double b, double c, double d, Color color) {
        A = a;
        B = b;
        C = c;
        D = d;
        this.color = color;
        polyhedronId = polyhedronCounter.incrementAndGet();
    }

    Vector<Point3D> createPoints(Polyhedron p, int n) { // генерация в точек в плоскости
        // нужен метод, который отсортирует точки в плоскости по часовой стрелке, иначе бред
        int min = 100, max = 600;

        for (int i = 0; i < n; i++) {
            double x = getRandomNumber(min, max);
            double y = getRandomNumber(min, max);
            double z = (p.D - (p.A * x) - (p.B * y)) / p.C;
            points.add(new Point3D(x, y, z));
        }
        return points;
    }

    void createEdges() {

        for (int i = 0; i < points.size() - 2; i++) {
            double x_min = points.get(i).x;
            double y_min = points.get(i).y;
            double x_max = points.get(i + 1).x;
            double y_max = points.get(i + 1).y;
            if (y_min != y_max)
                edges.add(new Edge(x_min, y_min, x_max, y_max, this.polyhedronId));
        }
        double x_min = points.get(points.size() - 1).x;
        double y_min = points.get(points.size() - 1).y;
        double x_max = points.get(0).x;
        double y_max = points.get(0).y;
        if (y_min != y_max)
            edges.add(new Edge(x_min, y_min, x_max, y_max, this.polyhedronId));
    }

    public Integer getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}

class Edge {
    int ownerPolyhedronId;
    double x_min, y_min;
    double x_max, y_max;
    double tg_alpha;

    public Edge(double x_min, double y_min, double x_max, double y_max, int ownerPolyhedronId) {
        this.ownerPolyhedronId = ownerPolyhedronId;
        this.x_min = x_min;
        this.y_min = y_min;
        this.x_max = x_max;
        this.y_max = y_max;
        this.tg_alpha = (y_max - y_min) / (x_max - x_min);
    }

    public Edge(Edge edge) {
        this.ownerPolyhedronId = edge.ownerPolyhedronId;
        this.x_min = edge.x_min;
        this.y_min = edge.y_min;
        this.x_max = edge.x_max;
        this.y_max = edge.y_max;
        this.tg_alpha = edge.tg_alpha;
    }

}

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

