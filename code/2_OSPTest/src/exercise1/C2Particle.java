package exercise1;

public class C2Particle extends Particle {

  double dt;
  double c2;
  double m;
  private double g;

  public C2Particle(double x0, double y0, double vx0, double vy0, double mass,
      double c2, double timestep, double gravity) {
    super(x0, y0, vx0, vy0);

    m = mass;
    this.c2 = c2;
    dt = timestep;
    g = gravity;
  }

  @Override
  public void step() {
    x += vx * dt;
    vx -= c2 * getV() * vx * dt / m;
    y += vy * dt;
    vy -= getG() * dt + c2 * getV() * vy * dt / m;
    t += dt;
  }

  @Override
  double getG() {
    return g;
  }

  @Override
  public void setTimestep(double dt) {
    this.dt = dt;
  }

}
