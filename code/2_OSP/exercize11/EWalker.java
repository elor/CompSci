package exercize11;

import java.util.Random;

/**
 * @author elor
 * 
 */
public class EWalker {

  private double[][] field;
  private int size;
  Random rnd = new Random();
  private int[][][][] green; // (x, y), (x_l, y_l) => (start), (end)

  /**
   * @param size
   *          the size of the field
   * @param v
   */
  public EWalker(int size, double[] v) {
    this.size = size;

    initField(v);
  }

  /**
   * initializes the field
   * 
   * @param v
   */
  public void initField(double[] v) {

    field = new double[size][size];
    green = new int[size][size][size][size];

    int s = size - 1;
    for (int i = 1; i < s; ++i) {
      field[i][s] = v[0];
      field[s][i] = v[1];
      field[i][0] = v[2];
      field[0][i] = v[3];
    }
  }

  /**
   * @param maxsteps
   * @param x0
   * @param y0
   * @return
   */
  public double walk(int maxsteps, int x0, int y0) {
    int x = x0;
    int y = y0;

    for (int i = 0; i < maxsteps || maxsteps == -1; ++i) {
      if (field[x][y] != 0.0) {
        ++green[x0][y0][x][y];
        return field[x][y];
      }

      switch (rnd.nextInt(4)) {
      case 0:
        ++x;
        break;
      case 1:
        --x;
        break;
      case 2:
        ++y;
        break;
      case 3:
        --y;
        break;
      }
    }

    return 0.0;
  }
}
