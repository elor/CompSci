package anna8;

import org.opensourcephysics.numerics.Function;

public class Phi implements Function {
  double lambda;

  double phi(double x) {
    return Math.exp(-lambda * x * x);
  }

  double phi2(double x) {
    return Math.exp(-2 * lambda * x * x);
  }

  double phiAbl(double x) {
    return (4 * lambda * lambda * x * x - 2 * lambda) * phi(x);
  }

  @Override
  public double evaluate(double x) {
    return phi2(x);
  }
}
