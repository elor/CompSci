package anna1;

import java.awt.Graphics;

import org.opensourcephysics.display.Circle;
import org.opensourcephysics.display.DrawingPanel;

public class Kreis extends Circle {

	public Kreis() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Kreis(double _x, double _y, int _r) {
		super(_x, _y, _r);
		// TODO Auto-generated constructor stub
	}

	public Kreis(double _x, double _y) {
		super(_x, _y);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(DrawingPanel panel, Graphics g) {
		// TODO Auto-generated method stub
		int px=panel.xToPix(x-0.5);
		int py=panel.yToPix(y+0.5);
		int pu=panel.xToPix(x+0.5)-px;
		int pv=panel.yToPix(y-0.5)-py;
		g.fillArc(px, py, pu, pv,0, 360);
	}

}
