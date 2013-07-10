package rendezvous;

import java.awt.Color;
import java.awt.Graphics;

import org.opensourcephysics.display.Drawable;
import org.opensourcephysics.display.DrawingPanel;
import org.opensourcephysics.display.Trail;

public class Soyuz extends Trail implements Drawable {

  double altitude = 0.0;
  double angle = 0.0;
  double halfheight = 0.5;
  double halfwidth = 0.3;
  boolean drawOrbit = true;
  boolean drawTrail = true;

  public Soyuz() {
    setConnected(false); // indicator: has not been drawn yet
  }

  @Override
  public void draw(DrawingPanel panel, Graphics g) {
    int x, y, wx, wy;

    if (drawOrbit) {
      x = panel.xToPix(-altitude);
      y = panel.yToPix(altitude);
      wx = panel.xToPix(altitude) - x;
      wy = panel.yToPix(-altitude) - y;

      g.setColor(Color.BLACK);

      g.drawArc(x, y, wx, wy, 0, 360);
    }

    g.setColor(Color.DARK_GRAY);

    double posx = getX();
    double posy = getY();

    int nPoints = 3;

    double[] xRaw = new double[] { -halfheight, halfheight, -halfheight };
    double[] yRaw = new double[] { -halfwidth, 0.0, halfwidth };

    double[] xPos = new double[3];
    double[] yPos = new double[3];

    double cos = Math.cos(angle);
    double sin = Math.sin(angle);
    for (int i = 0; i < 3; ++i) {
      xPos[i] = xRaw[i] * cos + yRaw[i] * sin;
      yPos[i] = yRaw[i] * cos - xRaw[i] * sin;
    }

    int[] xPoints = new int[3];
    int[] yPoints = new int[3];

    // convert
    for (int i = 0; i < 3; ++i) {
      xPoints[i] = panel.xToPix(xPos[i] + posx);
      yPoints[i] = panel.yToPix(yPos[i] + posy);
    }

    g.fillPolygon(xPoints, yPoints, nPoints);

    super.draw(panel, g);
  }

  public void updateTrail() {
    if (drawTrail) {
      double posx = getX();
      double posy = getY();

      if (!isConnected()) {
        this.moveToPoint(posx, posy);
        setConnected(true);
      }
      this.addPoint(posx, posy);
    }
  }

  private double getY() {
    double posy = Math.cos(angle) * altitude;
    return posy;
  }

  private double getX() {
    double posx = Math.sin(angle) * altitude;
    return posx;
  }
}
