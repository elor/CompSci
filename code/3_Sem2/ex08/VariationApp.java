package ex08;

import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.frames.HistogramFrame;

public class VariationApp extends AbstractSimulation {

  Phi phi = new Phi(); // remember to set the lambda value
  Potential pot; // remember to set the potential
  HistogramFrame histogram = new HistogramFrame("x", "n",
      "Metropolis Histogram");
  Metropolis metropolis;

  public VariationApp() {
    histogram.setBinWidth(1);
  }

  @Override
  protected void doStep() {
    double x = metropolis.step();
    histogram.append(x);
  }

  @Override
  public void initialize() {
    double x0 = control.getDouble("x0");
    double delta = control.getDouble("delta");
    metropolis = new Metropolis(phi, x0, delta);
  }

  @Override
  public void reset() {
    control.setValue("initial lambda", 0.5);
    control.setValue("delta", 1);
    control.setValue("x0", 0);
    enableStepsPerDisplay(true);
  }

  public static void main(String[] args) {
    SimulationControl.createApp(new VariationApp(), args);
  }
}
