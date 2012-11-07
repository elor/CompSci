package fallingparticle;

abstract public class Particle {
  double y, v, t;

  public double getY() {
    return y;
  }

  public double getV() {
    return v;
  }

  public double getT() {
    return t;
  }

  public Particle(double y0, double v0) {
    y = y0;
    v = v0;
  }

  abstract double getG();

  abstract public void step();

  abstract public double analyticalPosition();

  abstract public double analyticalVelocity();

  public void print() {
    System.out.println("t=" + t + ", y=" + y + " (" + analyticalPosition()
        + "), v=" + v + " (" + analyticalVelocity() + ")");
  }
}
