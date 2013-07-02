package ex08;

import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.frames.PlotFrame;

public class VariationApp extends AbstractSimulation {

  Phi phi; // remember to set the lambda value
  Potential pot; // remember to set the potential
  PlotFrame ePlot = new PlotFrame("lambda", "<E_L>",
      "time development of <E_L>");
  Lambdinator lambdi;
  private int step;

  public VariationApp() {
  }

  @Override
  public void doStep() {
    double lambdamin = control.getDouble("lambda min");
    double lambdamax = control.getDouble("lambda max");
    int lambdasteps = control.getInt("lambda steps");
    double delta = control.getDouble("metropolis delta");
    int equiSteps = control.getInt("equilibration Steps");
    int steps = control.getInt("energy simulation steps");

    phi.lambda = lambdamin + (lambdamax - lambdamin) * step / lambdasteps;
    lambdi.reset(delta, equiSteps);

    double eMean = lambdi.calculate(steps);
    ePlot.append(0, phi.lambda, eMean);

    ++step;
    if (step == lambdasteps) {
      control.calculationDone("lambdamax reached");
    }
  }

  public void initialize() {
    phi = new Phi();

    String potType = control.getString("potential type");
    if (potType.compareTo("harmonic") == 0) {
      pot = new HarmonicPotential();
    } else if (potType.compareTo("hydrogen") == 0) {
      pot = new HydrogenPotential(control.getDouble("hydrogen: e"));
    } else if (potType.compareTo("anharmonic") == 0) {
      pot = new AnharmonicPotential(control.getDouble("anharmonic: b"));
    }
    lambdi = new Lambdinator(phi, pot);

    step = 0;
    ePlot.clearData();
  }

  @Override
  public void reset() {
    control.setValue("lambda min", 0.1);
    control.setValue("lambda max", 1.0);
    control.setValue("lambda steps", 100);
    control.setValue("metropolis delta", 0.1);
    control.setValue("equilibration Steps", 50000);
    control.setValue("potential type", "harmonic");
    control.setValue("anharmonic: b", 1);
    control.setValue("hydrogen: e", 1);
    control.setValue("energy simulation steps", 100000);
  }

  public static void main(String[] args) {
    SimulationControl.createApp(new VariationApp(), args);
  }
}
