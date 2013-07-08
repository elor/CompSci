package anna8;

public class Metropolis {
  Phi phi;
  double x;
  double delta;

  public Metropolis(Phi phi, double x0, double delta) {
    this.phi = phi;
    this.x = x0;
    this.delta = delta;
  }

  double step() {
    double deltaN = Math.random() * 2 * delta - delta;
    double xTrial = x + deltaN;
    double w = phi.phi2(xTrial) / phi.phi2(x);
    if (w >= 1) {
      x = xTrial;
    } else {
      double r = Math.random();
      if (r <= w) {
        x = xTrial;
      }
    }

    return x;
  }

}
