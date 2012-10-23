package exercise1;

public class C1Particle extends Particle {

  double dt;
  double c1;
  double m;
  private double g;

  public C1Particle(double x0, double y0, double vx0, double vy0, double mass,
      double c1, double timestep, double gravity) {
    super(x0, y0, vx0, vy0);

    m = mass;
    this.c1 = c1;
    dt = timestep;
    g = gravity;
  }

  @Override
  public void step() {
    x += vx * dt;
    vx += -c1 * vx / m * dt;
    y += vy * dt;
    vy += -getG() * dt + -c1 * vy / m * dt;
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
