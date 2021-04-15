package Lab4;

import java.awt.geom.Point2D;

public class CohenSutherland2D {

    private enum Position {
        LEFT (1),
        RIGHT (2),
        BOT (4),
        TOP (8);

        private final int code;
        Position(int code) {
            this.code = code;
        }

        public int code()   { return code; }
    }

    private Point2D.Double points[];

    int CohenSutherland(Rectangle rectangle, Point2D.Double a, Point2D.Double b) {
        int code_a, code_b; /* коды концов отрезка */
        int code; // код новой точки
        Point2D.Double c; /* одна из точек */

        code_a = getCode(rectangle, a);
        code_b = getCode(rectangle, b);

        /* пока одна из точек отрезка вне прямоугольника */
        while ((code_a | code_b) != 0) {
            /* если обе точки с одной стороны прямоугольника, то отрезок не пересекает прямоугольник */
            if ((code_a & code_b) != 0)
                return -1;

            /* выбираем точку c с ненулевым кодом */
            if (code_a != 0) {
                code = code_a;
                c = a;
            } else {
                code = code_b;
                c = b;
            }

		/* если c левее r, то передвигаем c на прямую x = r->x_min
		   если c правее r, то передвигаем c на прямую x = r->x_max */
            if ((code & Position.LEFT.code()) != 0) {
                c.y += (a.y - b.y) * (rectangle.x_min - c.x) / (a.x - b.x);
                c.x = rectangle.x_min;
            } else if ((code & Position.RIGHT.code()) != 0) {
                c.y += (a.y - b.y) * (rectangle.x_max - c.x) / (a.x - b.x);
                c.x = rectangle.x_max;
            }/* если c ниже r, то передвигаем c на прямую y = r->y_min
		    если c выше r, то передвигаем c на прямую y = r->y_max */
            else if ((code & Position.BOT.code()) != 0) {
                c.x += (a.x - b.x) * (rectangle.y_min - c.y) / (a.y - b.y);
                c.y = rectangle.y_min;
            } else if ((code & Position.TOP.code()) != 0) {
                c.x += (a.x - b.x) * (rectangle.y_max - c.y) / (a.y - b.y);
                c.y = rectangle.y_max;
            }

            /* обновляем код */
            if (code == code_a) {
                a = c;
                code_a = getCode(rectangle, a);
            } else {
                b = c;
                code_b = getCode(rectangle, b);
            }
        }

        /* оба кода равны 0, следовательно обе точки в прямоугольнике */
        return 0;
    }

    int getCode (Rectangle rectangle, Point2D.Double point) {
        int position = 0;
        if (point.x < rectangle.x_min)
            position += Position.LEFT.code();
        if (point.x < rectangle.x_max)
            position += Position.RIGHT.code();
        if (point.y < rectangle.y_min)
            position += Position.BOT.code();
        if (point.y < rectangle.y_max)
            position += Position.TOP.code();
        return position;
    }
}

class Rectangle {
    protected double x_min, y_min, x_max, y_max;
}