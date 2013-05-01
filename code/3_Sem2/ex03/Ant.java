package ex03;

import java.util.Random;

/**
 * @author elor
 * 
 *         An Ant
 */
public class Ant {
  /**
   * x position
   */
  public int x;
  /**
   * y position
   */
  public int y;
  SingleCluster cluster;
  static Random rand = new Random();

  /**
   * @param cluster
   *          reference to the underlying cluster
   */
  public Ant(SingleCluster cluster) {
    setCluster(cluster);
  }

  /**
   * @param cluster
   */
  public void setCluster(SingleCluster cluster) {
    this.cluster = cluster;
    y = x = cluster.L / 2 + 1;
  }

  /**
   * perform a single step
   */
  public void step() {
    // randomly pick neighbor position
    int nx = x;
    int ny = y;

    switch (rand.nextInt(4)) {
    case 0:
      ++nx;
      break;
    case 1:
      --nx;
      break;
    case 2:
      ++ny;
      break;
    case 3:
      --ny;
      break;
    }

    // verify that it's available
    if (cluster.site[nx][ny] == 1) {
      // walk there
      x = nx;
      y = ny;
    }
  }

  /**
   * @return squared distance from the center
   */
  public int getR2() {
    int dx = x - cluster.L / 2 + 1;
    int dy = y - cluster.L / 2 + 1;

    return dx * dx + dy * dy;
  }

}
