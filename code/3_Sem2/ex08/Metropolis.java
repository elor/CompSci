package ex08;

public class Metropolis {
  protected Phi phi;
  protected double x;
  public double delta;
  int rejections;
  int steps;

  public Metropolis(Phi phi, double x0, double delta0) {
    this.phi = phi;
    // 0
    this.x = x0;
    this.delta = delta0;
    clear();
  }

  public void clear() {
    rejections = 0;
    steps = 0;
  }

  public double step() {
    // 1
    double xnew = x + Math.random() * 2 * delta - delta;

    // 2
    double w = phi.phi2(xnew) / phi.phi2(x);

    // 3
    if (w >= 1) {
      x = xnew;
    } else if (Math.random() <= w) {
      x = xnew;
    } else {
      ++rejections;
    }

    ++steps;

    return x;
  }

  public double getX() {
    return x;
  }
}
