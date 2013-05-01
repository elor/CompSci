package ex02;

//import java.awt.Color;
import java.util.PriorityQueue;
import java.util.Random;

import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.frames.PlotFrame;

/**
 * @author elor
 * 
 */
public class InvasionApp4 extends AbstractSimulation {
  /**
   * main
   * 
   * @param args
   */
  public static void main(String args[]) {
    SimulationControl.createApp(new InvasionApp4());
  }

  /**
   * 
   */
  public InvasionApp4() {
  }

  PriorityQueue<Pair> border = new PriorityQueue<Pair>();
  int h = 0; // height
  double p[] = null; // probability array (2d)
  Boolean visited[] = null;
  int w = 0; // width

  int maxH;
  double stepH;

  PlotFrame timePlot = new PlotFrame("system height",
      "mean runtime in seconds", "runtimes");

  /**
   * @param j
   */
  public void addBorder(int j) {
    if (j >= 0 && !visited[j]) {
      // add to border
      border.add(new Pair(p[j], j));
      // visit it to avoid second consideration
      visited[j] = true;
    }
  }

  void addNeighbors(int i) {
    int x = getX(i);
    int y = getY(i);

    addBorder(getId(x - 1, y));
    addBorder(getId(x + 1, y));
    addBorder(getId(x, y - 1));
    addBorder(getId(x, y + 1));
  }

  @Override
  protected void doStep() {
    int steps = control.getInt("simulations per height");

    if (h > maxH) {
      control.calculationDone("max height reached");
      return;
    }

    resize();

    double timesum = 0;

    // abort on empty list
    for (int j = 0; j < steps; ++j) {

      refurbish();

      long startTime = System.nanoTime();
      while (true) {
        if (border.size() <= 0) {
          control.println("no border left");
          break;
        }

        // get lowest-probability cell on border
        int i = border.poll().value;

        // add unvisited neighbors to cell
        addNeighbors(i);

        // paint it pink
        p[i] = -1;

        int x = getX(i);

        if (x == w - 1) {
          // control.println("right end reached");
          break;
        }
      }

      long endTime = System.nanoTime();

      timesum += (endTime - startTime) * 1e-9;
    }

    timePlot.append(0, border.size(), timesum / steps);

    // set next size
    h = (int) Math.floor(h * stepH);
  }

  /**
   */
  public void refurbish() {
    Random rand = new Random();

    // initialize probabilities
    for (int i = 0; i < h * w; ++i) {
      p[i] = rand.nextDouble();
      visited[i] = false;
    }

    // initialize border (i.e. left border of system)
    border.clear();

    for (int i = 0; i < h; ++i) {
      int j = getId(0, i);
      addBorder(j);
    }
  }

  int getId(int x, int y) {
    if (x < 0 || x >= w) {
      return -1;
    }

    return x + (y % h) * w;
  }

  int getX(int i) {
    return i % w;
  }

  int getY(int i) {
    return (i / w) % h;
  }

  @Override
  public void initialize() {
    h = control.getInt("start height");
    maxH = control.getInt("end height");
    stepH = control.getDouble("height step factor");

    timePlot.clearData();
  }

  void resize() {
    w = 2 * h;
    p = new double[h * w];
    visited = new Boolean[h * w];
  }

  @Override
  public void reset() {
    control.setValue("start height", 20);
    control.setValue("end height", 1200);
    control.setValue("height step factor", 1.2);
    control.setAdjustableValue("simulations per height", 20);
  }
}
