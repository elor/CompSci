package ex08;

public class Phi {
  double lambda;

  double phi(double x) {
    return Math.exp(-lambda * (x * x));
  }

  double phi2(double x) {
    return Math.exp(-2 * lambda * (x * x));
  }

  double d2(double x) {
    return (4 * lambda * lambda * x * x - 2 * lambda) * phi(x);
  }
}
