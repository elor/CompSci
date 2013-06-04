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
  static double r = 0.5;
  static double r2 = r * r;

  /**
   * get radius of all disks
   * 
   * @return Radius r
   */
  public static double getR() {
    return Disk.r;
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

  Color color = Color.black;
  Disk parent;

  double x; // x pos
  double y; // y pos

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

  @Override
  public void draw(DrawingPanel panel, Graphics g) {
    g.setColor(getRoot().color);

    int px = panel.xToPix(this.x - Disk.r);
    int py = panel.yToPix(this.y + Disk.r);

    int wx = panel.xToPix(x + Disk.r) - px;
    int wy = panel.yToPix(y - Disk.r) - py;

    g.fillArc(px, py, wx, wy, 0, 360);
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
   * overlap without using a Disk instance
   * 
   * @param x
   *          x pos of center
   * @param y
   *          y pos of center
   * @return true if they overlap, false otherwise
   */
  public boolean overlap(double x, double y) {
    double x2 = x - this.x;
    double y2 = y - this.y;

    x2 *= x2;
    y2 *= y2;

    return x2 + y2 <= 4 * Disk.r2;
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
   * set the root
   * 
   * @param root
   *          new root
   */
  public void setRoot(Disk root) {
    if (this.getRoot() != root) {
      this.parent = root;
    }
  }

  /**
   * @return size of cluster, i.e. number of children
   */
  public int size() {
    // todo return true number of children
    return 0;
  }

  /**
   * @return whether the disk is root
   */
  public boolean isRoot() {
    return this.parent == null;
  }

  /**
   * @return current color of the cluster the disk belongs to
   */
  public Color getColor() {
    return this.getRoot().color;
  }
}
