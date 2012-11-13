package fallingparticle;

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
    control.setValue("Anfangsposition", 10.0);
    control.setValue("Anfangsgeschwindigkeit", 0.0);
    control.setValue("Zeitschrittweite", 0.01);

  }

  public void print() {
    control.println("t=" + particle.getT() + ", y=" + particle.getY() + " ("
        + particle.analyticalPosition() + "), v=" + particle.getV() + " ("
        + particle.analyticalVelocity() + ")");
  }

  @Override
  public void calculate() {
    particle = new EulerParticle(control.getDouble("Anfangsposition"),
        control.getDouble("Anfangsgeschwindigkeit"),
        control.getDouble("Zeitschrittweite"));
    
    control.println("Simulation started");
    print();

    while (particle.getY() > 0.0) {
      particle.step();
    }

    control.println("Simulation stopped");
    print();

  }

}
