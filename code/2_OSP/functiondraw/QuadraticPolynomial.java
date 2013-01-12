package functiondraw;

import org.opensourcephysics.numerics.Function;

/**
 * Quadratic polynomial class for function plotting
 * @author elor
 */
public class QuadraticPolynomial implements Function {
  double a, b, c;
  
  /**
   * constructor
   * 
   * @param a
   * @param b
   * @param c
   */
  public QuadraticPolynomial(double a, double b, double c) {
    this.a = a;
    this.b = b;
    this.c = c;
  }

  @Override
  public double evaluate(double x) {
    return a*x*x + b*x + c;
  }

}
