package exercise2;

import java.util.Random;

import org.opensourcephysics.controls.*;
import org.opensourcephysics.frames.*;
import org.opensourcephysics.numerics.*;

/**
 * Pendulum App using Simulation class
 * 
 * @author elor
 */
public class PendulumSimulationApp extends AbstractSimulation {

  private PendulumODE pendulae[];
  private AbstractODESolver solvers[];
  private PlotFrame plot;
  private PlotFrame areaplot;

  /**
   * empty constructor
   */
  public PendulumSimulationApp() {
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    SimulationControl.createApp(new PendulumSimulationApp());
  }

  /**
   * calculate area of all pendulae in phase space
   * 
   * @return area
   */
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
    control.setValue("Minimalwinkel (rad)", 0.0);
    control.setValue("Minimalgeschwindigkeit (rad/s)", 0.0);
    control.setValue("Maximalwinkel (rad)", 1.0);
    control.setValue("Maximalgeschwindigkeit (rad/s)", 1.0);
    control.setValue("Pendellaenge (m)", 1.0);
    control.setValue("Anzahl", 1000);
    control.setValue("Zufaellig", false);
    control.setAdjustableValue("Zeitschrittweite", 0.001);
    enableStepsPerDisplay(true);
    setStepsPerDisplay(100);
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
    double thetamin = control.getDouble("Minimalwinkel (rad)");
    double omegamin = control.getDouble("Minimalgeschwindigkeit (rad/s)");
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
    plot.setPreferredMinMax(-(thetamax + thetamin) * 1.5,
        (thetamax + thetamin) * 1.5, -(omegamin + omegamax) * 1.5,
        (omegamin + omegamax) * 1.5);

    pendulae = new PendulumODE[num];
    solvers = new AbstractODESolver[num];
    Random rand = new Random();
    for (int i = 0; i < num; ++i) {
      double theta = 0.0;
      double omega = 0.0;
      if (random) {
        theta = rand.nextDouble() * thetamax;
        omega = rand.nextDouble() * omegamax;
      } else {
        int quadrant = (4 * i) / num;
        double pos = new Double((4 * i) - quadrant * num) / num;
        switch (quadrant) {
        case 0:
          theta = thetamin;
          omega = omegamin + pos * omegamax;
          break;
        case 1:
          theta = thetamin + pos * thetamax;
          omega = omegamin + omegamax;
          break;
        case 2:
          theta = thetamin + thetamax;
          omega = omegamin + (1 - pos) * omegamax;
          break;
        case 3:
          theta = thetamin + (1 - pos) * thetamax;
          omega = omegamin;
        }
      }

      pendulae[i] = new PendulumODE(theta, omega, length, 9.81);
      solvers[i] = new RK4(pendulae[i]);
      plot.addDrawable(pendulae[i]);
    }
    control.println("Simulation started");
  }
}
