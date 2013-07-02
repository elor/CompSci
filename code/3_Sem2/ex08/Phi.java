package ex08;

import org.opensourcephysics.numerics.Function;

public class Phi implements Function {
  double lambda = 1.0;

  double phi(double x) {
    return Math.exp(-lambda * (x * x));
  }

  double phi2(double x) {
    return Math.exp(-2 * lambda * (x * x));
  }

  double d2(double x) {
    return (4 * lambda * lambda * x * x - 2 * lambda) * phi(x);
  }

  @Override
  public double evaluate(double x) {
    return phi2(x);
  }
}
