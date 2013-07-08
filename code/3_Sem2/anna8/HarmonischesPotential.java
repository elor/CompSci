package anna8;

public class HarmonischesPotential implements Potential {

  @Override
  public double V(double x) {

    return x * x / 2;
  }

}
