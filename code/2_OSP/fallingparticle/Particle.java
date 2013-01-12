package fallingparticle;

/**
 * abstract falling particle class
 * 
 * @author elor
 */
abstract public class Particle {
  double y, v, t;

  /**
   * @return y position
   */
  public double getY() {
    return y;
  }

  /**
   * @return y velocity
   */
  public double getV() {
    return v;
  }

  /**
   * @return time
   */
  public double getT() {
    return t;
  }

  /**
   * constructor
   * 
   * @param y0
   *          initial y position
   * @param v0
   *          initial y velocity
   */
  public Particle(double y0, double v0) {
    y = y0;
    v = v0;
  }

  abstract double getG();

  /**
   * perform a step
   */
  abstract public void step();

  /**
   * @return current analytical position of the particle
   */
  abstract public double analyticalPosition();

  /**
   * @return current analytical velocity of the particle
   */
  abstract public double analyticalVelocity();

  /**
   * write current state to stdout
   */
  public void print() {
    System.out.println("t=" + t + ", y=" + y + " (" + analyticalPosition()
        + "), v=" + v + " (" + analyticalVelocity() + ")");
  }
}
