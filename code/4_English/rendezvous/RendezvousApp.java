package rendezvous;

import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display.DrawingFrame;

public class RendezvousApp extends AbstractSimulation {
  DrawingFrame display = new DrawingFrame();
  int frame;
  int maxframe;

  public RendezvousApp() {
    display.setSquareAspect(true);
  }

  private Earth earth;
  private ISS iss;
  private Soyuz soyuz;
  private int phase;
  private double s1ISSTargetAngle;
  private double s1SoyuzTargetAltitude;
  private double s1SoyuzTargetAngle;
  private double s2TargetAngle;
  private double s3TargetAngle;

  MultiDrawer drawer = new MultiDrawer();

  @Override
  public void initialize() {
    display.setSize(control.getInt("display width"),
        control.getInt("display height"));

    earth = new Earth(control.getDouble("Earth radius"));
    iss = new ISS(control.getDouble("ISS altitude") + earth.radius);
    soyuz = new Soyuz();

    drawer.clear();
    drawer.add(earth);
    drawer.add(iss);
    drawer.add(soyuz);

    frame = 0;
    maxframe = control.getInt("frames");

    s1ISSTargetAngle = control.getDouble("Stage 1 ISS target angle");
    s1SoyuzTargetAngle = control.getDouble("Stage 1 Soyuz target angle");
    s1SoyuzTargetAltitude = control.getDouble("Stage 1 Soyuz target altitude");

    s2TargetAngle = control.getDouble("Stage 2 target angle");

    s3TargetAngle = control.getDouble("Stage 3 target angle");

    phase = control.getInt("phase");

    iss.altitude = control.getDouble("ISS altitude") + earth.radius;

    switch (phase) {
    case 1:
      soyuz.altitude = earth.radius;
      soyuz.drawOrbit = false;
      break;
    case 2:
      iss.angle = s1ISSTargetAngle;
      soyuz.angle = s1SoyuzTargetAngle;
      soyuz.altitude = earth.radius + s1SoyuzTargetAltitude;
      soyuz.drawTrail = false;
      break;
    case 3:
      iss.angle = s1ISSTargetAngle + s2TargetAngle;
      soyuz.altitude = earth.radius + s1SoyuzTargetAltitude;
      soyuz.angle = iss.angle;
      soyuz.drawOrbit = false;
      break;
    default:
      control.calculationDone("invalid phase");
      break;
    }
  }

  @Override
  protected void doStep() {
    double frac = frame / (double) maxframe;
    double carf = 1.0 - frac;

    switch (phase) {
    case 1: // launch phase
      iss.angle = s1ISSTargetAngle * frac;
      soyuz.angle = s1SoyuzTargetAngle * frac;
      soyuz.altitude = earth.radius + s1SoyuzTargetAltitude * frac;
      break;
    case 2: // phasing phase
      iss.angle = s1ISSTargetAngle + frac * s2TargetAngle;
      soyuz.angle = s1SoyuzTargetAngle * carf + frac
          * (s2TargetAngle + s1ISSTargetAngle);
      break;
    case 3: // rendezvous phase
      iss.angle = s1ISSTargetAngle + s2TargetAngle + frac * s3TargetAngle;
      soyuz.angle = iss.angle;
      soyuz.altitude = (earth.radius + s1SoyuzTargetAltitude) * carf + frac
          * iss.altitude;
      break;
    default:
      control.calculationDone("Invalid Phase: " + phase);
      setPhase(1);
    }

    soyuz.updateTrail();

    repaint();

    ++frame;

    if (frame > maxframe) {

      if (control.getBoolean("pause after each phase")) {
        control.calculationDone("Phase " + phase + " completed after " + frame
            + " frames");
        increasePhase();
      } else {
        increasePhase();
        initialize();
      }
    }

    try {
      Thread.sleep(1000, 0);
    } catch (InterruptedException e) {
      control.println("sleep failed");
    }
  }

  private void repaint() {
    display.clearDrawables();
    display.addDrawable(drawer);

    display.repaint();
  }

  private void increasePhase() {
    setPhase(phase + 1);
  }

  private void setPhase(int phase) {
    control.setValue("phase", phase);
  }

  @Override
  public void reset() {
    control.setValue("Earth radius", 1.0);
    control.setValue("ISS altitude", 7.0);
    control.setValue("Stage 1 ISS target angle", 3.0);
    control.setValue("Stage 1 Soyuz target angle", 2.5);
    control.setValue("Stage 1 Soyuz target altitude", 6.0);
    control.setValue("Stage 2 target angle", Math.PI * 4 - 3.0);
    control.setValue("Stage 3 target angle", Math.PI);
    setPhase(1);
    control.setValue("frames", 300);
    control.setValue("display width", 600);
    control.setValue("display height", 600);
    control.setAdjustableValue("pause after each phase", true);
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    SimulationControl.createApp(new RendezvousApp(), args);
  }
}
