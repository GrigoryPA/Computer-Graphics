import java.awt.*;
import java.util.Vector;

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

    void ScanLine(Vector<Polyhedron> PolyhedronTable, Vector<Edge> edgeTable) {

    }

}

class Polyhedron {
    double A, B, C, D;
    Color color;

    public Polyhedron(double a, double b, double c, double d, Color color) {
        A = a;
        B = b;
        C = c;
        D = d;
        this.color = color;
    }
}

class Edge {
    double x_min, y_min;
    double x_max, y_max;
    double tg_alpha;

    public Edge(double x_min, double y_min, double x_max, double y_max) {
        this.x_min = x_min;
        this.y_min = y_min;
        this.x_max = x_max;
        this.y_max = y_max;
        this.tg_alpha = (y_max - y_min) / (x_max - x_min);
    }
}
