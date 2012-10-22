package exercise1;

import org.opensourcephysics.controls.*;

public class ParticleCalcApp extends AbstractCalculation {

  private Particle particle;

  public ParticleCalcApp() {
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    CalculationControl.createApp(new ParticleCalcApp());
  }

  @Override
  public void reset() {
    control.setValue("Starthoehe", 10.0);
    control.setValue("Startgeschwindigkeit", 0.0);
    control.setValue("Startwinkel", 0.0);
    control.setValue("Zeitschrittweite", 0.01);
    control.setValue("C", 0.7);
    control.setValue("Masse", 7.0);
  }

  public void print() {
    control.println(particle.toString());
  }

  @Override
  public void calculate() {
    double x0 = 0.0;
    double y0 = control.getDouble("Starthoehe");
    double v = control.getDouble("Startgeschwindigkeit");
    double angle = Math.toRadians(control.getDouble("Startwinkel"));
    double vx = Math.cos(angle) * v;
    double vy = Math.sin(angle) * v;
    double dt = control.getDouble("Zeitschrittweite");
    double c = control.getDouble("C");
    double mass = control.getDouble("Masse");
    particle = new C1Particle(x0, y0, vx, vy, mass, c, dt, 9.81);

    control.println("Simulation started");
    print();

    while (particle.getY() >= 0.0) {
      particle.step();
    }

    control.println("Simulation stopped");
    print();

  }

}
