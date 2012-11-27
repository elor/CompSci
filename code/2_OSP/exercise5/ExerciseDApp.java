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

public class ExerciseDApp extends AbstractSimulation {
  PlotFrame frame = new PlotFrame("x", "y", "Billiard Table");
  PlotFrame plot = new PlotFrame("time", "fraction of remaining balls",
      "Decay ploy");
  Billiard billiard = new Billiard();
  List<Double> list = new LinkedList<Double>();

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
    // TODO Auto-generated method stub
    plot.clearData();
    Iterator<Double> it = list.iterator();
    int size = list.size();
    int i = size;
    double time;

    while (it.hasNext()) {
      --i;
      time = it.next();
      plot.append(0, time, (double) i / size);
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
    plot.setMessage("ball no. " + list.size()+1);
  }

  /**
   * Resets animation to a predefined state.
   */
  public void reset() {
    control.setValue("l", 1);
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
