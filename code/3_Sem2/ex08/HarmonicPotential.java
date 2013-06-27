package ex08;

public class HarmonicPotential implements Potential {

  @Override
  public double V(double x) {
    return x * x / 2;
  }

}
