package exercise1;

import org.opensourcephysics.controls.*;
import org.opensourcephysics.frames.*;

public class ParticleSimulationApp extends AbstractSimulation {

  private Particle frictionparticle;
  private Particle spaceparticle;
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
    control.setValue("Startgeschwindigkeit", 0.0);
    control.setValue("Startwinkel", 0.0);
    control.setAdjustableValue("Zeitschrittweite", 0.01);
    control.setValue("C", 0.7);
    control.setValue("Masse", 7.0);
    enableStepsPerDisplay(true);
    setStepsPerDisplay(10);
  }

  public void print() {
    control.println(spaceparticle.toString() + "\t" + frictionparticle.toString());
  }

  // control.println("Simulation stopped");

  @Override
  protected void doStep() {
    double dt = control.getDouble("Zeitschrittweite");
    spaceparticle.setTimestep(dt);
    frictionparticle.setTimestep(dt);
    spaceparticle.step();
    frictionparticle.step();
    print();
    plot.append(0, frictionparticle.getT(), frictionparticle.getY());
    plot.append(1, spaceparticle.getT(), spaceparticle.getY());
    
    if (frictionparticle.getY() < 0.0) {
      control.calculationDone("Balls hit ground");
    }
    
    plot.setMessage("v=" + frictionparticle.getV() + "|" + spaceparticle.getV());
  }

  @Override
  public void initialize() {
    double x0 = 0.0;
    double y0 = control.getDouble("Starthoehe");
    double v = control.getDouble("Startgeschwindigkeit");
    double angle = Math.toRadians(control.getDouble("Startwinkel"));
    double vx = Math.cos(angle) * v;
    double vy = Math.sin(angle) * v;
    double dt = control.getDouble("Zeitschrittweite");
    double c = control.getDouble("C");
    double mass = control.getDouble("Masse");
    spaceparticle = new C1Particle(x0, y0, vx, vy, mass, 0.0, dt, 9.81);
    frictionparticle = new C1Particle(x0, y0, vx, vy, mass, c, dt, 9.81);
    control.println("Simulation started");
    plot = new PlotFrame("t (s)", "y (m)", "Hoehenzeitverlauf");
    plot.setConnected(true);
    plot.append(0, spaceparticle.getT(), spaceparticle.getY());
    plot.append(1, frictionparticle.getT(), frictionparticle.getY());
  }
}
