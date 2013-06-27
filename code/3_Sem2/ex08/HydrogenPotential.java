package ex08;

public class HydrogenPotential implements Potential {
  double e;

  public HydrogenPotential(double e) {
    this.e = e;
  }

  @Override
  public double V(double x) {
    return e * e / x;
  }

}
