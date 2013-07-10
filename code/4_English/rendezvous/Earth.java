package rendezvous;

import java.awt.Color;
import java.awt.Graphics;

import org.opensourcephysics.display.Drawable;
import org.opensourcephysics.display.DrawingPanel;

public class Earth implements Drawable {

  double radius = 1.0;

  public Earth(double radius) {
    this.radius = radius;
  }

  @Override
  public void draw(DrawingPanel panel, Graphics g) {
    int x = panel.xToPix(-radius);
    int y = panel.yToPix(radius);
    int wx = panel.xToPix(radius) - x;
    int wy = panel.yToPix(-radius) - y;

    g.setColor(Color.BLUE);

    g.fillArc(x, y, wx, wy, 0, 360);
  }

}
