package ex08;

public class Lambdinator {

  protected static final double m = 1;
  protected Phi phi; // remember to set the lambda value
  protected Potential pot; // remember to set the potential
  protected Metropolis metropolis;
  protected double eSum;
  protected int eSteps;

  public Lambdinator(Phi phi, Potential pot) {
    this.phi = phi;
    this.pot = pot;
  }

  public double calculate(int steps) {
    for (int i = 0; i < steps; ++i) {
      double x = metropolis.step();

      eSum += getEnergy(x);
      ++eSteps;
    }

    double eMean = eSum / eSteps;

    return eMean;
  }

  private double getEnergy(double x) {
    double p = phi.phi(x);
    return (-phi.d2(x) / 2 * m + pot.V(x) * p) / p;
  }

  public void reset(double delta, int equilibrationSteps) {
    metropolis = new Metropolis(phi, 0.0, delta);

    eSum = 0;
    eSteps = 0;

    for (int i = 0; i < equilibrationSteps; ++i) {
      metropolis.step();
    }
  }
}
