/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package exercise5;

import org.opensourcephysics.controls.*;
import org.opensourcephysics.frames.*;

public class BilliardApp extends AbstractSimulation {
  PlotFrame frame = new PlotFrame("x (AU)", "y (AU)", "Billiard Table");
  Billiard billiard;
  double state[];

  /**
   * Constructor 
   */
  public BilliardApp() {
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

    // double time = billiard.getTime();
  }

  /**
   * Initializes the animation using the values in the control.
   */
  public void initialize() {
    double r = control.getDouble("r");
    double l = control.getDouble("l");
    int balls = control.getInt("balls");
    double holesize = control.getDouble("hole size");
    
    frame.clearDrawables();
    
    billiard = new Billiard(control);
    frame.addDrawable(billiard);
    billiard.setProperties(r, l, holesize, balls);
    billiard.randomize();
    // frame.setMessage("t=0");
  }

  /**
   * Resets animation to a predefined state.
   */
  public void reset() {
    control.setValue("r", 1);
    control.setValue("l", 2);
    control.setValue("balls", 1);
    control.setValue("hole size", -1);
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
    SimulationControl.createApp(new BilliardApp());
  }
}
