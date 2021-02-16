import java.awt.*;

public class Line3D {

	
	public static int sign (int x) {
		return (x > 0) ? 1 : (x < 0) ? -1 : 0;
	}
	
	public static void AddLineSigmentOnDisplayBresenham(int xstart, int ystart, int xend, int yend, Color color) {
		int x, y, dx, dy, incx, incy, pdx, pdy, es, el, err;


		dx = xend - xstart;
		dy = yend - ystart;

		incx = sign(dx);
		incy = sign(dy);

		if (dx < 0) dx = -dx;
		if (dy < 0) dy = -dy;

		if (dx > dy){
			pdx = incx;	pdy = 0;
			es = dy;	el = dx;
		}
		else{
			pdx = 0;	
			pdy = incy;
			es = dx;	
			el = dy;
		}

		x = xstart;
		y = ystart;
		err = el/2;
		Display3D.AddPointOnDisplay(x,y,color);
		
		for (int t = 0; t < el; t++){
			err -= es;
			if (err < 0){
				err += el;
				x += incx;
				y += incy;
			}
			else{
				x += pdx;
				y += pdy;
			}
			Display3D.AddPointOnDisplay(x,y,color);
		}
	}
}
