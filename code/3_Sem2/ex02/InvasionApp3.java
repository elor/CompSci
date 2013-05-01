package ex02;

//import java.awt.Color;
import java.util.PriorityQueue;
import java.util.Random;

import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.frames.PlotFrame;
import org.opensourcephysics.frames.Scalar2DFrame;

/**
 * @author elor
 * 
 */
public class InvasionApp3 extends AbstractSimulation {
  /**
   * main
   * 
   * @param args
   */
  public static void main(String args[]) {
    SimulationControl.createApp(new InvasionApp3());
  }

  /**
   * 
   */
  public InvasionApp3() {
  }

  PriorityQueue<Pair> border = new PriorityQueue<Pair>();
  int h = 0; // height
  double p[] = null; // probability array (2d)
  Boolean visited[] = null;
  int w = 0; // width
  Scalar2DFrame probFrame = new Scalar2DFrame("Probabilities");

  int maxH;
  double stepH;

  PlotFrame histPlot = new PlotFrame("r", "prob", "Another Amazing Plot");
  int histIndex;

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

    double rProb[] = new double[100];

    // abort on empty list
    for (int j = 0; j < steps; ++j) {

      int rVisited[] = new int[101];
      int rTotal[] = new int[101];

      refurbish();

      // count all probabilities
      for (int i = 0; i < p.length; ++i) {
        int bin = (int) Math.round(p[i] * 100);
        ++rTotal[bin];
      }

      while (true) {
        if (border.size() <= 0) {
          control.println("no border left");
          break;
        }

        // get lowest-probability cell on border
        int i = border.poll().value;

        // visit it
        // visited[i] = true;// already visited when adding to queue
        int bin = (int) Math.round(p[i] * 100);
        ++rVisited[bin];

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

      // apply to rProb

      for (int bin = 0; bin < 100; ++bin) {
        rProb[bin] += (double) rVisited[bin] / (double) rTotal[bin];
      }

      // lattice plot
      probFrame.setAll(p);
    }

    // apply to histPlot
    for (int bin = 0; bin < 100; ++bin) {
      histPlot.append(histIndex, bin / 100.0, rProb[bin] / 100);
    }
    ++histIndex;

    // set next size
    h = (int) Math.floor(h * stepH);
  }

  /**
   */
  public void refurbish() {
    Random rand = new Random();

    // init probabilities
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

    probFrame.setZRange(false, -1.0, 1.0);
    probFrame.setSize(800, 400);

    histPlot.clearData();
    histPlot.setConnected(true);
    histIndex = 0;
  }

  void resize() {
    w = 2 * h;
    p = new double[h * w];
    visited = new Boolean[h * w];
    probFrame.resizeGrid(w, h);
  }

  @Override
  public void reset() {
    control.setValue("start height", 20);
    control.setValue("end height", 1000);
    control.setValue("height step factor", 5);
    control.setAdjustableValue("simulations per height", 20);
  }
}
