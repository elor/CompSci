package fallingparticle;

/**
 * falling Particle for an Euler integrator
 * 
 * @author elor
 */
public class EulerParticle extends Particle {

  private double v0;
  private double y0;
  double dt;
  private double g;

  /**
   * Constructor
   * 
   * @param y0
   *          initial y position
   * @param v0
   *          initial y velocity
   * @param timestep
   *          timestep
   */
  public EulerParticle(double y0, double v0, double timestep) {
    super(y0, v0);

    this.y0 = y0;
    this.v0 = v0;
    dt = timestep;
    g = 9.81;
  }

  /**
   * alternative constructor
   * 
   * @param y0
   *          initial y position
   * @param v0
   *          initial y velocity
   * @param timestep
   *          timestep
   * @param gravity
   *          gravity in m/s^2
   */
  public EulerParticle(double y0, double v0, double timestep, double gravity) {
    super(y0, v0);

    this.y0 = y0;
    this.v0 = v0;
    dt = timestep;
    g = gravity;
  }

  @Override
  public void step() {
    y += v * dt;
    v += -getG() * dt;
    t += dt;
  }

  @Override
  public double analyticalPosition() {
    return -0.5 * getG() * t * t + v0 * t + y0;
  }

  @Override
  public double analyticalVelocity() {
    return -getG() * t + v0;
  }

  @Override
  double getG() {
    return g;
  }

}
