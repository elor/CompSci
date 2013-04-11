package ex01;

import java.awt.Color;
import java.awt.Graphics;

import org.opensourcephysics.display.Drawable;
import org.opensourcephysics.display.DrawingPanel;

/**
 * Disk class
 * 
 * @author elor
 * 
 */
public class Disk implements Drawable {
  double x; // x pos
  double y; // y pos
  static double r = 0.5;
  static double r2 = r * r;
  Disk parent;
  Color color = Color.black;

  /**
   * constructor
   * 
   * @param x
   * @param y
   */
  public Disk(double x, double y) {
    this.x = x;
    this.y = y;
  }

  /**
   * perform an overlap test
   * 
   * @param disk
   * @return whether the disks overlap ever so slightly
   */
  public Boolean overlap(Disk disk) {
    double x2 = disk.x - this.x;
    double y2 = disk.y - this.y;

    x2 *= x2;
    y2 *= y2;

    return x2 + y2 <= 4 * Disk.r2;
  }

  /**
   * perform inside test
   * 
   * @param x
   * @param y
   * @return whether (x, y) is inside the disk
   */
  public Boolean isInside(double x, double y) {
    double x2 = this.x - x;
    double y2 = this.y - y;

    x2 *= x2;
    y2 *= y2;

    return x2 + y2 <= Disk.r2;
  }

  /**
   * traverses the tree recursively and returns the root associated with the
   * current cluster
   * 
   * @return
   */
  public Disk getRoot() {
    if (this.parent == null) {
      return this;
    }

    return this.parent.getRoot();
  }

  /**
   * set color of the whole cluster
   * 
   * @param color
   */
  public void setColor(Color color) {
    getRoot().color = color;
  }

  /**
   * set radius for all disks
   * 
   * @param r
   *          new radius
   */
  public static void setR(double r) {
    Disk.r = r;
    Disk.r2 = r * r;
  }

  @Override
  public void draw(DrawingPanel panel, Graphics g) {
    // TODO draw disk

    int px = panel.xToPix(this.x);
    int py = panel.yToPix(this.y);

    int wx = panel.xToPix(Disk.r);
    int wy = panel.yToPix(Disk.r);

    g.fillArc(px, py, wx, wy, 0, 360);
  }
}
