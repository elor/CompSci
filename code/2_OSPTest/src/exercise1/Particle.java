package exercise1;

abstract public class Particle {
  double x, y, vx, vy, t;

  public double getY() {
    return y;
  }

  public double getV() {
    return Math.sqrt(vx*vx+vy*vy);
  }

  public double getT() {
    return t;
  }

  public Particle(double x0, double y0, double vx0, double vy0) {
    x = x0;
    y = y0;
    vx = vx0;
    vy = vy0;
  }

  abstract double getG();

  abstract public void step();

  @Override
  public String toString() {
    return "t=" + t + ", pos=[" + x + ", " + y + "], v=[" + vx + ", " + vy + "] (" + getV() + ")";
  }

  public void print() {
    System.out.println(this);
  }

  public abstract void setTimestep(double dt);
}
