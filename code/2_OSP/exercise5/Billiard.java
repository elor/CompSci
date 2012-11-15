package exercise5;

import java.awt.Graphics;

import org.opensourcephysics.display.Drawable;
import org.opensourcephysics.display.DrawingPanel;

public class Billiard implements Drawable {
  double r, l;
  double x, y, px, py;
  double t;
  private double l2;

  /**
   * empty default constructor. Values are set using specific setters
   */
  public Billiard() {
  }

  /**
   * sets the billiard geometry and (x, y, px, py) phase space position where
   * (px, py) is normalized.
   * 
   * @param r
   *          radius of the caps
   * @param l
   *          length of the middle part
   * @param x
   *          x position
   * @param y
   *          y position
   * @param px
   *          x momentum
   * @param py
   *          y momentum
   */
  public void setProperties(double r, double l, double x, double y, double px,
      double py) {
    this.r = r;
    this.l = l;
    this.l2 = l / 2;
    this.x = x;
    this.y = y;
    this.px = px;
    this.py = py;

    normalize();
  }

  /**
   * Normalizes the momentum
   */
  public void normalize() {
    double length = Math.sqrt(px * px + py * py);

    if (length == 0.0) {
      px = 1.0;
      py = 0.0;
    }

    px /= length;
    py /= length;
  }

  /**
   * retrieves and returns the sector
   * 
   * @return sector (1, 2, 3 if inside, 0 if outside)
   */
  public int getSector() {
    if (x < -l2) {
      double dist2 = Math.pow(x + l2, 2) + Math.pow(y, 2);
      if (dist2 > r * r) {
        return 0;
      }
      return 1;
    } else if (x > l2) {
      if (y > r || y < -r) {
        return 0;
      }
      return 2;
    } else {
      double dist2 = Math.pow(x - l2, 2) + Math.pow(y, 2);
      if (dist2 > r * r) {
        return 0;
      }
      return 3;
    }
  }

  /**
   * Checks if the position is inside the 'billiard table'
   * 
   * @return true if the position (x, y) is inside, false otherwise
   */
  public Boolean isInside() {
    return getSector() != 0;
  }

  /**
   * draw the billiard table
   */
  @Override
  public void draw(DrawingPanel panel, Graphics g) {
    // TODO Auto-generated method stub

    int y1 = panel.yToPix(0);
    int x1 = panel.xToPix(-l / 2);
    int rx = panel.xToPix(-l / 2 + r) - x1;
    int ry = panel.yToPix(r) - y1;

    int x2, y2;

    // first sector
    System.out.println(x1 + ", " + y1 + ", " + rx + ", " + ry);
    g.drawArc(x1, y1, rx, ry, 90, 270);

    // second sector
    x1 = panel.xToPix(-l / 2);
    x2 = panel.xToPix(l / 2);
    y1 = panel.yToPix(r);
    y2 = panel.yToPix(-r);

    g.drawLine(x1, y1, x2, y1);
    g.drawLine(x1, y2, x2, y2);

    // third sector
    x1 = panel.xToPix(l / 2);
    y1 = panel.yToPix(0);
    rx = panel.xToPix(l / 2 + r) - x1;
    ry = panel.yToPix(r) - y1;

    g.drawArc(x1, y1, rx, ry, 270, 90);
  }

  public void doStep() {
    // TODO Auto-generated method stub
    
  }

  public double getTime() {
    return t;
  }
}
