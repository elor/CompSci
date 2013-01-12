/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package exercise5;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.opensourcephysics.controls.*;
import org.opensourcephysics.frames.*;

/**
 * App for exercise 5d
 * @author elor
 */
public class ExerciseDApp extends AbstractSimulation {
  PlotFrame frame = new PlotFrame("x", "y", "Billiard Table");
  PlotFrame plot = new PlotFrame("time", "fraction of remaining balls",
      "Decay ploy");
  Billiard billiard = new Billiard();
  List<Double> list = new LinkedList<Double>();
  private double tau;

  /**
   * Constructor
   */
  public ExerciseDApp() {
    frame.setPreferredMinMax(-2, 2, -2, 2);
    frame.setSquareAspect(true);
    frame.setConnected(true);

    frame.addDrawable(billiard);

    plot.setConnected(true);
  }

  /**
   * Steps the time.
   */
  public void doStep() {
    int steps = control.getInt("calculations per step");
    for (int i = 0; i < steps; i++) { // do 5 steps between screen draws
      billiard.doStep(); // advances time

      if (billiard.getNumBalls() == 0) {
        insertTime(billiard.getTime());
        updatePlot();
        resetBilliard();
      }
    }

    setMessages();
  }

  private void updatePlot() {
    plot.clearData();
    Iterator<Double> it = list.iterator();
    int size = list.size();
    int i = size;
    double time = 0.0;

    tau = 0.0; // characteristical time
    int charfraction = size - (int) (size / Math.exp(1));

    while (it.hasNext()) {
      --i;
      time = it.next();
      plot.append(0, time, (double) i / size);

      if (charfraction == 0) {
        tau = time;
      }
      --charfraction;
    }
    
    // exp plot
    
    for (double t = 0.0; t <= time; t += time/20) {
      plot.append(1, t, Math.exp(-t/tau));
    }
  }

  /**
   * Initializes the animation using the values in the control.
   */
  public void initialize() {

    resetBilliard();

    setMessages();
    list.clear();

    plot.clearData();
    plot.append(0, billiard.getTime(), 1.0);
  }

  private void resetBilliard() {
    double l = control.getDouble("l");
    billiard.setProperties(1, l, 0.02, 1);
    billiard.randomize();
  }

  private void setMessages() {
    frame.setMessage("t=" + billiard.getTime());
    plot.setMessage("ball no. " + (list.size() + 1) + ", tau=" + tau);
  }

  /**
   * Resets animation to a predefined state.
   */
  public void reset() {
    control.setValue("l", 1);
    control.setAdjustableValue("calculations per step", 100000);
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

  private void insertTime(double d) {
    if (list.isEmpty()) {
      list.add(d);
      return;
    }

    Iterator<Double> it = list.iterator();
    double v;
    int pos = 0;

    do {
      v = it.next();
      if (v < d) {
        ++pos;
      } else {
        break;
      }
    } while (it.hasNext());

    list.add(pos, d);
  }

}
