package ex02;

import java.awt.Color;
import java.util.PriorityQueue;
import java.util.Random;

import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.frames.LatticeFrame;
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
  LatticeFrame lattice = new LatticeFrame("Visited Cells");
  Scalar2DFrame probFrame = new Scalar2DFrame("Probabilities");

  /**
   * @param j
   */
  public void addBorder(int j) {
    if (!visited[j]) {
      // add to border
      border.add(new Pair(p[j], j));
      // visit it to avoid second consideration
      visited[j] = true;
    }
  }

  void addNeighbors(int i) {
    int x = getX(i);
    int y = getY(i);

    int j;

    j = getId(x - 1, y);
    if (j >= 0 && !visited[j]) {
      addBorder(j);
    }
    j = getId(x + 1, y);
    if (j >= 0 && !visited[j]) {
      addBorder(j);
    }
    j = getId(x, y - 1);
    if (j >= 0 && !visited[j]) {
      addBorder(j);
    }
    j = getId(x, y + 1);
    if (j >= 0 && !visited[j]) {
      addBorder(j);
    }
  }

  @Override
  protected void doStep() {
    // abort on empty list
    if (border.size() <= 0) {
      control.calculationDone("no border left");
    }

    // get lowest-probability cell on border
    int i = border.poll().value;

    // visit it
    // visited[i] = true;// already visited when adding to queue

    // add unvisited neighbors to cell
    addNeighbors(i);

    // paint it black
    int x = getX(i);
    int y = getY(i);

    control.println("Zelle " + i + " ( " + x + ", " + x + " ) besucht");

    lattice.setValue(x, y, 1);
    p[i] = -1;
    probFrame.setAll(p);
  }

  int getId(int x, int y) {
    if (x < 0 || x >= w) {
      return -1;
    }

    return x + (y % h) * w;
  }

  double getP(int i) {
    return getP(getX(i), getY(i));
  }

  double getP(int x, int y) {
    if (x < 0 || x >= w) {
      return 1.0;
    }

    return p[getId(x, y)];
  }

  int getX(int i) {
    return i % h;
  }

  int getY(int i) {
    return (i / w) % h;
  }

  @Override
  public void initialize() {
    h = control.getInt("height");
    w = 2 * h;
    Random rand = new Random();
    p = new double[h * w];
    visited = new Boolean[h * w];

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

    lattice.clearData();
    lattice.resizeLattice(w, h);
    lattice.setIndexedColor(0, Color.blue);
    lattice.setIndexedColor(1, Color.red);

    // initialize with 0 (unvisited)
    for (int i = 0; i < h * w; ++i) {
      int x = getX(i);
      int y = getY(i);

      lattice.setValue(x, y, 0);
    }

    probFrame.clearData();
    probFrame.setZRange(false, -1.0, 1.0);
    probFrame.resizeGrid(w, h);
    probFrame.setAll(p);
    probFrame.setSquareAspect(true);
  }

  @Override
  public void reset() {
    control.setValue("height", 20);
  }

}
