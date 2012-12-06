package exercise6;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import org.opensourcephysics.display.Drawable;
import org.opensourcephysics.display.DrawingPanel;

public class Walker2D implements Drawable {
  double[] state;
  int time;
  Random rng = new Random();

  public void initialize(int walkers) {
    state = new double[walkers * 2 + 1];
    time = 0;
  }

  @Override
  public void draw(DrawingPanel panel, Graphics g) {
    g.setColor(Color.black);
    int num = n();
    int px, py;

    for (int i = 0; i < num; ++i) {
      px = panel.xToPix(x(i));
      py = panel.yToPix(y(i));

      g.fillArc(px - 2, py - 2, 4, 4, 0, 360);
    }
  }

  public void stepWalker(int walker) {
    int direction = rng.nextInt(4);

    switch (direction) {
    case 0: // up
      ++state[walker * 2 + 1];
      break;
    case 1: // right
      ++state[walker * 2];
      break;
    case 2: // down
      --state[walker * 2 + 1];
      break;
    case 3: // left
      --state[walker * 2];
      break;
    }
  }

  public void step() {
    int num = n();
    for (int i = 0; i < num; ++i) {
      stepWalker(i);
    }

    ++time;
  }

  public double x(int walker) {
    return state[walker * 2];
  }

  public double y(int walker) {
    return state[walker * 2 + 1];
  }

  public int t() {
    return time;
  }

  public int n() {
    return state.length / 2;
  }
}
