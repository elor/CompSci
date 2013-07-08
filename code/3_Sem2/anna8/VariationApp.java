package anna8;

import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.frames.PlotFrame;

public class VariationApp extends AbstractSimulation {

  Phi phi = new Phi();
  HarmonischesPotential pot;
  PlotFrame plot = new PlotFrame("lambda", "E", "Amazing Plot");
  double lambda;
  double deltalambda;

  @Override
  protected void doStep() {
    int relax = control.getInt("relax");
    int n = control.getInt("n");

    plot.append(0, lambda, energieVonLambda(lambda, relax, n));
    lambda += deltalambda;
  }

  public double energieVonLambda(double lambda, int relax, int n) {
    double delta = control.getDouble("Delta");
    phi.lambda = lambda;

    Metropolis metropolis = new Metropolis(phi, 0, delta);
    for (int i = 0; i < relax; i++) {
      metropolis.step();
    }

    double Esum = 0;
    for (int i = 0; i < n; i++) {
      Esum += getEnergy(metropolis.step());
    }
    return Esum / (double)n;
  }

  @Override
  public void initialize() {
    plot.clearData();
    pot = new HarmonischesPotential();
    lambda = control.getDouble("Lambda");
    deltalambda = control.getDouble("deltalambda");
  }

  public double getEnergy(double x) {
    double p = phi.phi(x);
    return (-phi.phiAbl(x) / 2 + pot.V(x) * p) / p;
  }

  @Override
  public void reset() {
    control.setValue("Lambda", 0.1);
    control.setValue("deltalambda", 0.05);
    control.setValue("Delta", 0.1);
    control.setValue("relax", 50000);
    control.setValue("n", 100000);

    enableStepsPerDisplay(true);
  }

  public static void main(String[] args) {
    SimulationControl.createApp(new VariationApp(), args);
  }

}
