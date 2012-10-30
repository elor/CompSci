package exercise2;

import java.util.Random;

import org.opensourcephysics.controls.*;
import org.opensourcephysics.frames.*;
import org.opensourcephysics.numerics.*;

public class PendulumSimulationApp extends AbstractSimulation {

  private PendulumODE pendulae[];
  private AbstractODESolver solvers[];
  private PlotFrame plot;
  private PlotFrame areaplot;

  public PendulumSimulationApp() {
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    SimulationControl.createApp(new PendulumSimulationApp());
  }

  public double calcArea() {
    double A = 0.0;
    int max = pendulae.length - 1;

    for (int i = 0; i < max; ++i) {
      A += pendulae[i].getState()[1] * pendulae[i + 1].getState()[0]
          - pendulae[i + 1].getState()[1] * pendulae[i].getState()[0];
    }

    A += pendulae[0].getState()[1] * pendulae[max].getState()[0]
        - pendulae[max].getState()[1] * pendulae[0].getState()[0];

    return A / 2;
  }

  @Override
  public void reset() {
    control.setValue("Maximalwinkel (rad)", 0.1 * Math.PI);
    control.setValue("Maximalgeschwindigkeit (rad/s)", 2.0);
    control.setValue("Pendellaenge (m)", 0.1);
    control.setValue("Anzahl", 1000);
    control.setValue("Zufaellig", false);
    control.setAdjustableValue("Zeitschrittweite", 0.01);
    enableStepsPerDisplay(true);
    setStepsPerDisplay(10);
  }

  // control.println("Simulation stopped");

  @Override
  protected void doStep() {
    double dt = control.getDouble("Zeitschrittweite");

    for (int i = 0; i < solvers.length; ++i) {
      solvers[i].setStepSize(dt);
      solvers[i].step();
    }

    areaplot.append(0, pendulae[0].getState()[2], calcArea());
  }

  @Override
  public void initialize() {
    double thetamax = control.getDouble("Maximalwinkel (rad)");
    double omegamax = control.getDouble("Maximalgeschwindigkeit (rad/s)");
    double length = control.getDouble("Pendellaenge (m)");
    int num = control.getInt("Anzahl");
    Boolean random = control.getBoolean("Zufaellig");

    if (areaplot == null) {
      areaplot = new PlotFrame("t", "A", "area over time");
      areaplot.setConnected(true);
    }
    areaplot.clearData();

    if (plot == null) {
      plot = new PlotFrame("theta", "omega", "phase space");
    }
    plot.clearDrawables();
    plot.setPreferredMinMax(-0.15 * Math.PI, 0.15 * Math.PI, -3, 3);

    pendulae = new PendulumODE[num];
    solvers = new Verlet[num];
    Random rand = new Random();
    for (int i = 0; i < num; ++i) {
      double theta = 0.0;
      double omega = 0.0;
      if (random) {
        theta = (rand.nextDouble() * 2.0 - 1.0) * thetamax;
        omega = (rand.nextDouble() * 2.0 - 1.0) * omegamax;
      } else {
        int quadrant = (4 * i) / num;
        double pos = new Double((4 * i) - quadrant * num) / num;
        switch (quadrant) {
        case 0:
          theta = thetamax * 0.5;
          omega = (pos * omegamax + omegamax) * 0.5;
          break;
        case 1:
          theta = (pos * thetamax + thetamax) * 0.5;
          omega = omegamax;
          break;
        case 2:
          theta = thetamax;
          omega = ((1 - pos) * omegamax + omegamax) * 0.5;
          break;
        case 3:
          theta = ((1 - pos) * thetamax + thetamax) * 0.5;
          omega = omegamax * 0.5;
        }
      }

      pendulae[i] = new PendulumODE(theta, omega, length, 9.81);
      solvers[i] = new Verlet(pendulae[i]);
      plot.addDrawable(pendulae[i]);
    }
    control.println("Simulation started");
  }
}
