package ex02;

//import java.awt.Color;
import java.util.PriorityQueue;
import java.util.Random;

import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.frames.Scalar2DFrame;

/**
 * @author elor
 * 
 */
public class InvasionApp extends AbstractSimulation {
  /**
   * main
   * 
   * @param args
   */
  public static void main(String args[]) {
    SimulationControl.createApp(new InvasionApp());
  }

  PriorityQueue<Pair> border = new PriorityQueue<Pair>();
  int h = 0; // height
  double p[] = null; // probability array (2d)
  Boolean visited[] = null;
  int w = 0; // width
  Scalar2DFrame probFrame = new Scalar2DFrame("Probabilities");

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
    int steps = control.getInt("steps per display");
    // abort on empty list

    for (; steps > 0; --steps) {

      int n = 0; // number of occupied cells in center square
      refurbish();

      while (true) {
        if (border.size() <= 0) {
          control.println("no border left");
          break;
        }

        // get lowest-probability cell on border
        int i = border.poll().value;

        // visit it
        // visited[i] = true;// already visited when adding to queue

        // add unvisited neighbors to cell
        addNeighbors(i);

        // paint it pink
        p[i] = -1;

        int x = getX(i);

        // is in center
        if (x >= w / 4 || x < 3 * w / 4) {
          ++n;
        }

        if (x == w - 1) {
          // control.println("right end reached");
          break;
        }
      }

      // plot
      probFrame.setAll(p);

      // print
      double rho = (double) n / (double) (h * h);
      control.println("central density: " + rho);
    }
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

    probFrame.setAll(p);
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
    h = control.getInt("height");
    w = 2 * h;
    p = new double[h * w];
    visited = new Boolean[h * w];

    // arrays are filled in doStep

    probFrame.clearData();
    probFrame.setZRange(false, -1.0, 1.0);
    probFrame.resizeGrid(w, h);
    probFrame.setSize(800, 400);
  }

  @Override
  public void reset() {
    control.setValue("height", 1234);
    control.setAdjustableValue("steps per display", 1);
  }
}
