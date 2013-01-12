package exercise2;

import java.awt.Color;
import java.awt.Graphics;

import org.opensourcephysics.display.Drawable;
import org.opensourcephysics.display.DrawingPanel;
import org.opensourcephysics.numerics.ODE;

/**
 * Pendulum ODE class
 * 
 * @author elor
 */
public class PendulumODE implements Drawable, ODE {
  double[] state;
  double g, l;

  /**
   * constructor
   * 
   * @param theta0
   *          initial angular position
   * @param omega0
   *          initial angular velocity
   * @param l
   *          length of pendulum
   * @param g
   *          gravity
   */
  public PendulumODE(double theta0, double omega0, double l, double g) {
    this.g = g;
    this.l = l;

    state = new double[3];
    state[0] = theta0;
    state[1] = omega0;
    state[2] = 0.0;
  }

  @Override
  public double[] getState() {
    return state;
  }

  @Override
  public void getRate(double[] state, double[] rate) {
    rate[0] = state[1];
    rate[1] = -g * Math.sin(state[0]) / l;
    // rate[1] = -g*state[0]/l;
    rate[2] = 1.0;
  }

  @Override
  public String toString() {
    return "[" + state[0] + ", " + state[1] + ", " + state[2] + ", " + state[3]
        + ", " + state[4] + "]";
  }

  @Override
  public void draw(DrawingPanel panel, Graphics g) {
    int x = panel.xToPix(state[0]);
    int y = panel.yToPix(state[1]);

    g.setColor(Color.red);
    g.fillArc(x, y, 5, 5, 0, 360);
  }

}
