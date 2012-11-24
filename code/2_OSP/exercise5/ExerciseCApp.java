/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package exercise5;

import java.awt.Color;

import org.opensourcephysics.controls.*;
import org.opensourcephysics.frames.*;

public class ExerciseCApp extends AbstractSimulation {
  PlotFrame frame = new PlotFrame("x", "y", "Billiard Table");
  Billiard billiard;
  Boolean inverted = false;

  /**
   * Constructor
   */
  public ExerciseCApp() {
    frame.setPreferredMinMax(-2, 2, -2, 2);
    frame.setSquareAspect(true);
    frame.setConnected(true);
    frame.setLineColor(1, Color.white);
  }

  @Override
  public void start() {
    super.start();

    // invert ball movement if stopped and started again
    if (billiard.getTime() != 0.0) {
      double[] ball = billiard.getBall(0);
      billiard.setBall(0, ball[0], ball[2], -ball[1], -ball[3]);
      inverted = true;
    }
  }

  /**
   * Steps the time.
   */
  public void doStep() {
    int steps = control.getInt("calculations per step");
    for (int i = 0; i < steps; i++) { // do 5 steps between screen draws
      billiard.doStep(); // advances time
    }

    appendBall(0);
    frame.setMessage("t=" + billiard.getTime());
  }

  /**
   * Initializes the animation using the values in the control.
   */
  public void initialize() {
    double l = control.getDouble("l");

    frame.clearDrawables();

    billiard = new Billiard(control);
    frame.addDrawable(billiard);
    billiard.setProperties(1, l, 0.0, 1);
    billiard.randomize();

    frame.clearData();
    inverted = false;
    appendBall(0);

    frame.setMessage("t=" + billiard.getTime());
  }

  private void appendBall(int ball) {
    double state[] = billiard.getBall(ball);
    if (inverted) {
      ball += billiard.getNumBalls();
    }
    frame.append(ball, state[0], state[2]);
  }

  /**
   * Resets animation to a predefined state.
   */
  public void reset() {
    control.setValue("l", 1);
    control.setAdjustableValue("calculations per step", 1);
  }

  /**
   * Start Java application.
   * 
   * @param args
   *          command line parameters
   */
  public static void main(String[] args) {
    SimulationControl.createApp(new ExerciseCApp());
  }
}
