package exercise1;

import java.awt.Color;
import java.awt.Graphics;

import org.opensourcephysics.display.Drawable;
import org.opensourcephysics.display.DrawingPanel;
import org.opensourcephysics.numerics.ODE;

public class ParticleODE implements Drawable, ODE {
  double[] state;
  double g, c1, c2, m;

  public ParticleODE(double x0, double vx0, double y0, double vy0, double c1, double c2, double m, double g) {
    this.g = g;
    this.c1 = c1;
    this.c2 = c2;
    this.m = m;
    
    state = new double[5];
    state[0] = x0;
    state[1] = vx0;
    state[2] = y0;
    state[3] = vy0;
    state[4] = 0.0;
  }

  @Override
  public double[] getState() {
    return state;
  }

  @Override
  public void getRate(double[] state, double[] rate) {
    double v = Math.sqrt(state[1]*state[1] + state[3]*state[3]);
    rate[0] = state[1];
    rate[1] = -c1*state[1]/m - c2*v*state[1]/m;
    rate[2] = state[3];
    rate[3] = -g - c1*state[3]/m - c2*v*state[3]/m;
    rate[4] = 1.0;
  }

  @Override
  public String toString() {
    return "[" + state[0] + ", " + state[1] + ", " + state[2] + ", " + state[3]
        + ", " + state[4] + "]";
  }

  @Override
  public void draw(DrawingPanel panel, Graphics g) {
    int x = panel.xToPix(state[0]);
    int y = panel.yToPix(state[2]);
    
    g.setColor(Color.blue);
    g.fillArc(x, y, 5, 5, 0, 360);
  }

}
