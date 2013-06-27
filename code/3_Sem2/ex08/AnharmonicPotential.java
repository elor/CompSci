package ex08;

public class AnharmonicPotential implements Potential {

  public AnharmonicPotential(double b) {
    this.b = b;
  }

  public double b;

  @Override
  public double V(double x) {
    double x2 = x * x;
    return x2 / 2 + b * x2 * x2;
  }
}
