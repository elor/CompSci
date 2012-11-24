/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package exercise5;

import org.opensourcephysics.controls.*;
import org.opensourcephysics.frames.*;

public class ExerciseDApp extends AbstractSimulation {
  PlotFrame frame = new PlotFrame("x", "y", "Billiard Table");
  PlotFrame plot = new PlotFrame("time", "fraction of remaining balls",
      "Decay ploy");
  Billiard billiard;
  private int initialballs;

  /**
   * Constructor
   */
  public ExerciseDApp() {
    frame.setPreferredMinMax(-2, 2, -2, 2);
    frame.setSquareAspect(true);
    frame.setConnected(true);

    plot.setConnected(true);
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

    plot.append(0, billiard.getTime(), (double) billiard.getNumBalls()
        / initialballs);

    if (billiard.getNumBalls() == 0) {
      control.calculationDone("no balls left");
    }
  }

  /**
   * Initializes the animation using the values in the control.
   */
  public void initialize() {
    double l = control.getDouble("l");
    int balls = control.getInt("balls");
    initialballs = balls;

    frame.clearDrawables();

    billiard = new Billiard(control);
    frame.addDrawable(billiard);
    billiard.setProperties(1, l, 0.02, balls);
    billiard.randomize();
    
    frame.setMessage("t=" + billiard.getTime());
    
    plot.clearData();
    plot.append(0, billiard.getTime(), 1.0);
  }

  /**
   * Resets animation to a predefined state.
   */
  public void reset() {
    control.setValue("l", 1);
    control.setValue("balls", 10000);
    control.setAdjustableValue("calculations per step", 100);
    initialize();
  }

  /**
   * Start Java application.
   * 
   * @param args
   *          command line parameters
   */
  public static void main(String[] args) {
    SimulationControl.createApp(new ExerciseDApp());
  }
}
