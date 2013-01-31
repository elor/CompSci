package exercize11;

import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.controls.XML;
import org.opensourcephysics.frames.Scalar2DFrame;

/**
 * @author elor
 * 
 */
public class EWalkerApp extends AbstractSimulation {
  EWalker walker;
  int maxsteps;

  double[][] accumulatedPotField;
  double[][] potField;
  int[][] nField;
  int size;

  Scalar2DFrame frame = new Scalar2DFrame("Potential");
  double[] v;

  /**
   * @param args
   */
  public static void main(String[] args) {
    SimulationControl.createApp(new EWalkerApp());
  }

  @Override
  public void initialize() {
    size = control.getInt("field size");

    potField = new double[size][size];
    v = new double[4];
    v[0] = control.getDouble("V north");
    v[1] = control.getDouble("V east");
    v[2] = control.getDouble("V south");
    v[3] = control.getDouble("V west");

    initPotField(v);
    accumulatedPotField = new double[size][size];
    nField = new int[size][size];

    maxsteps = control.getInt("max steps per walk");
    walker = new EWalker(size, v);

    frame.clearData();
    double vmin = Math.min(Math.min(v[0], v[1]), Math.min(v[2], v[3]));
    double vmax = Math.max(Math.max(v[0], v[1]), Math.max(v[2], v[3]));
    frame.setZRange(false, vmin, vmax);
    frame.setAll(potField, 0, size, 0, size);
  }

  private void initPotField(double v[]) {
    int s = size - 1;

    for (int i = 1; i < s; ++i) {
      potField[i][s] = v[0];
      potField[s][i] = v[1];
      potField[i][0] = v[2];
      potField[0][i] = v[3];
    }

    potField[0][0] = 0.5 * (v[2] + v[3]);
    potField[0][s] = 0.5 * (v[0] + v[3]);
    potField[s][0] = 0.5 * (v[1] + v[2]);
    potField[s][s] = 0.5 * (v[0] + v[1]);
  }

  @Override
  public void reset() {
    control.setValue("V north", 10.0);
    control.setValue("V east", 5.0);
    control.setValue("V south", 10.0);
    control.setValue("V west", 5.0);
    control.setValue("field size", 8);
    control.setValue("max steps per walk", -1);
    control.setAdjustableValue("steps per display", 1);
  }

  @Override
  protected void doStep() {
    int steps = control.getInt("steps per display");
    for (int i = 0; i < steps; ++i) {
      for (int x = 1; x < size - 1; ++x) {
        for (int y = 1; y < size - 1; ++y) {
          double pot = walker.walk(maxsteps, x, y);

          ++nField[x][y];
          potField[x][y] = (accumulatedPotField[x][y] += pot) / nField[x][y];
        }
      }
    }

    frame.setAll(potField, 0, size, 0, size);
    frame.repaint();
  }

  public static XML.ObjectLoader getLoader() {
    return new GreenLoader();
  }
}
