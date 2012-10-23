package ode;

import org.opensourcephysics.controls.*;
import org.opensourcephysics.frames.*;
import org.opensourcephysics.numerics.*;

public class ParticleSimulationApp extends AbstractSimulation {

  private ParticleODE frictionparticle;
  private ParticleODE spaceparticle;
  private AbstractODESolver frictionsolver;
  private AbstractODESolver spacesolver;
  private PlotFrame plot;

  public ParticleSimulationApp() {
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    SimulationControl.createApp(new ParticleSimulationApp());
  }

  @Override
  public void reset() {
    control.setValue("Starthoehe", 10.0);
    control.setValue("Startgeschwindigkeit", 30.0);
    control.setValue("Startwinkel", 0.0);
    control.setAdjustableValue("Zeitschrittweite", 0.01);
    control.setValue("C1", 0.0);
    control.setValue("C2", 0.7);
    control.setValue("Masse", 7.0);
    enableStepsPerDisplay(true);
    setStepsPerDisplay(10);
  }

  public void print() {
    control.println(spaceparticle.toString() + "\t"
        + frictionparticle.toString());
  }

  // control.println("Simulation stopped");

  @Override
  protected void doStep() {
    double dt = control.getDouble("Zeitschrittweite");

    if (spaceparticle.getState()[2] >= 0.0) {
      spacesolver.setStepSize(dt);
      spacesolver.step();
      plot.append(0, spaceparticle.getState()[0], spaceparticle.getState()[2]);
    }

    if (frictionparticle.getState()[2] >= 0.0) {
      frictionsolver.setStepSize(dt);
      frictionsolver.step();
      plot.append(1, frictionparticle.getState()[0],
          frictionparticle.getState()[2]);
    }
    print();

    if (frictionparticle.getState()[2] < 0.0) {
      control.calculationDone("Balls hit ground");
    }
  }

  @Override
  public void initialize() {
    double x0 = 0.0;
    double y0 = control.getDouble("Starthoehe");
    double v = control.getDouble("Startgeschwindigkeit");
    double angle = Math.toRadians(control.getDouble("Startwinkel"));
    double vx = Math.cos(angle) * v;
    double vy = Math.sin(angle) * v;
    double c1 = control.getDouble("C1");
    double c2 = control.getDouble("C2");
    double mass = control.getDouble("Masse");
    spaceparticle = new ParticleODE(x0, vx, y0, vy, 0.0, 0.0, mass, 9.81);
    frictionparticle = new ParticleODE(x0, vx, y0, vy, c1, c2, mass, 9.81);
    spacesolver = new Verlet(spaceparticle);
    frictionsolver = new Verlet(frictionparticle);
    control.println("Simulation started");
    plot = new PlotFrame("t (s)", "y (m)", "Hoehenzeitverlauf");
    plot.setConnected(true);
  }
}
