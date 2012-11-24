/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package exercise5;

import java.util.Random;

import org.opensourcephysics.controls.*;
import org.opensourcephysics.frames.*;

public class ExerciseBApp extends AbstractSimulation {
  PlotFrame frame = new PlotFrame("x", "y", "Billiard Table");
  PlotFrame lyaplot = new PlotFrame("t", "L", "Lyapunov exponent");
  Billiard billiard;

  double deltas0 = 0.0;

  /**
   * Constructor
   */
  public ExerciseBApp() {
    frame.setPreferredMinMax(-2, 2, -2, 2);
    frame.setSquareAspect(true);
    frame.setConnected(true);
  }

  /**
   * Steps the time.
   */
  public void doStep() {
    int steps = control.getInt("calculations per step");
    for (int i = 0; i < steps; i++) { // do 5 steps between screen draws
      billiard.doStep(); // advances time
    }

    frame.setMessage("t=" + billiard.getTime());

    lyapunovUpdate();
  }

  private void lyapunovUpdate() {
    double deltas = getDeltaS(billiard.getBall(0), billiard.getBall(1));
    double time = billiard.getTime();

    lyaplot.append(0, time, Math.log(deltas));
  }

  /**
   * Initializes the animation using the values in the control.
   */
  public void initialize() {
    double l = control.getDouble("l");

    frame.clearDrawables();

    billiard = new Billiard(control);
    frame.addDrawable(billiard);
    billiard.setProperties(1, l, 0.0, 2);
    randomize();
    
    lyaplot.clearData();
    lyapunovUpdate();
    
    frame.setMessage("t=" + billiard.getTime());
  }

  private void randomize() {
    Random rand = new Random();

    double vx = rand.nextDouble() - 0.5;
    double vy = rand.nextDouble() - 0.5;

    billiard.setBall(0, 0.0, 0.0, vx, vy);
    vx += 1e-5;
    vy -= 1e-5;
    billiard.setBall(1, 0.0, 0.0, vx, vy);

    deltas0 = getDeltaS(billiard.getBall(0), billiard.getBall(1));
  }

  private double getDeltaS(double[] ball, double[] ball2) {
    return Math.sqrt(billiard
        .veclength2(ball[0] - ball2[0], ball[2] - ball2[2])
        + billiard.veclength2(ball[1] - ball2[1], ball[3] - ball2[3]));
  }

  /**
   * Resets animation to a predefined state.
   */
  public void reset() {
    control.setValue("l", 1);
    control.setAdjustableValue("calculations per step", 1);
    initialize();
  }

  /**
   * Start Java application.
   * 
   * @param args
   *          command line parameters
   */
  public static void main(String[] args) {
    SimulationControl.createApp(new ExerciseBApp());
  }
}
