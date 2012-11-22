package exercise5;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import org.opensourcephysics.display.Drawable;
import org.opensourcephysics.display.DrawingPanel;

public class Billiard implements Drawable {
  double r, l;
  double state[];
  double t;
  private double l2;
  Random rand;

  /**
   * empty default constructor. Values are set using specific setters
   */
  public Billiard() {
    rand = new Random();
  }

  /**
   * sets the billiard geometry and (x, y, px, py) phase space position where
   * (px, py) is normalized.
   * 
   * @param r
   *          radius of the caps
   * @param l
   *          length of the middle part
   * @param balls
   *          number of balls
   */
  public void setProperties(double r, double l, int balls) {
    this.r = r;
    this.l = l;
    this.l2 = l / 2;

    state = new double[balls * 4 + 1];
  }

  /**
   * randomize all ball positions and
   */
  public void randomize() {
    int numballs = (state.length - 1) / 4;
    double x, y;

    for (int ball = 0; ball < numballs; ++ball) {
      do {
        x = rand.nextDouble() * (l + 2 * r) - (l2 + r);
        y = rand.nextDouble() * 2 * r - r;
        setBall(ball, x, y, rand.nextDouble() - 0.5, rand.nextDouble() - 0.5);
      } while (isInside(ball) == false);
    }

    // reset the time
    state[state.length - 1] = 0.0;
  }

  /**
   * set the properties of a ball
   * 
   * @param ball
   *          index of the ball (from 0)
   * @param x
   * @param y
   * @param vx
   * @param vy
   */
  public void setBall(int ball, double x, double y, double vx, double vy) {
    // get offset within state vector
    ball *= 4;

    // avoid segfaults
    if (ball > state.length - 5) {
      return;
    }

    // normalize
    double scale = Math.sqrt(vx * vx + vy * vy);
    if (scale <= 1e-9) {
      vx = 1.0;
      vy = 0.0;
    } else {
      vx /= scale;
      vy /= scale;
    }

    // apply
    state[ball] = x;
    state[ball + 1] = vx;
    state[ball + 2] = y;
    state[ball + 3] = vy;
  }

  /**
   * retrieves and returns the sector
   * 
   * @return sector (1, 2, 3 if inside, 0 if outside)
   */
  public int getSector(int ball) {
    ball *= 4;
    double x = state[ball];
    double y = state[ball + 2];

    // first sector (left)
    if (x < -l2) {
      double dist2 = Math.pow(x + l2, 2) + Math.pow(y, 2);
      if (dist2 > r * r) {
        return 0;
      }
      return 1;
    } else if (x <= l2) {
      if (y > r || y < -r) {
        return 0;
      }
      return 2;
    } else {
      double dist2 = Math.pow(x - l2, 2) + Math.pow(y, 2);
      if (dist2 > r * r) {
        return 0;
      }
      return 3;
    }
  }

  /**
   * Checks if the position is inside the 'billiard table'
   * 
   * @return true if the position (x, y) is inside, false otherwise
   */
  public Boolean isInside(int ball) {
    return getSector(ball) != 0;
  }

  /**
   * draw the billiard table
   */
  @Override
  public void draw(DrawingPanel panel, Graphics g) {
    // TODO Auto-generated method stub

    int y1 = panel.yToPix(r);
    int x1 = panel.xToPix(-l2 - r);
    int rx = Math.abs(panel.xToPix(2 * r) - panel.xToPix(0));
    int ry = Math.abs(panel.yToPix(2 * r) - panel.yToPix(0));

    int x2, y2;

    // first sector
    g.drawArc(x1, y1, rx, ry, 90, 180);

    // second sector
    x1 = panel.xToPix(-l2);
    x2 = panel.xToPix(l2);
    y1 = panel.yToPix(r);
    y2 = panel.yToPix(-r);

    g.drawLine(x1, y1, x2, y1);
    g.drawLine(x1, y2, x2, y2);

    // third sector
    x1 = panel.xToPix(l2 - r);
    y1 = panel.yToPix(r);
    rx = Math.abs(panel.xToPix(2 * r) - panel.xToPix(0));
    ry = Math.abs(panel.yToPix(2 * r) - panel.yToPix(0));

    g.drawArc(x1, y1, rx, ry, 270, 180);

    g.setColor(Color.blue);

    // draw balls
    for (int ball4 = 0; ball4 < state.length - 1; ball4 += 4) {
      x1 = panel.xToPix(state[ball4]);
      y1 = panel.yToPix(state[ball4 + 2]);
      x2 = panel.xToPix(state[ball4] + state[ball4 + 1]);
      y2 = panel.yToPix(state[ball4 + 2] + state[ball4 + 3]);

      g.fillOval(x1 - 5, y1 - 5, 10, 10);
      g.drawLine(x1, y1, x2, y2);
    }
  }

  public double veclength(double x, double y) {
    return Math.sqrt(x * x + y * y);
  }

  double solveQuadratic(double p, double q) {
    if (p * p / 4 - q < 0.0) {
      throw new RuntimeException(
          "Impossibru! solveQuadratic: Starting outside the circle");
    }

    // -p/2 - Math.sqrt() is invalid, because it's in the past (since you
    // entered the circle at some time)

    return -p / 2 + Math.sqrt(p * p / 4 - q);
  }

  double getBounceTime(int ball) {
    int ball4 = ball * 4;
    while (getSector(ball) == 0) {
      state[ball4] -= 1e-5 * state[ball4 + 1];
      state[ball4 + 2] -= 1e-5 * state[ball4 + 3];
    }
    int sector = getSector(ball);
    double time1 = 0.0, time2 = 0.0, timesum = 0.0;

    double x = state[ball4];
    double vx = state[ball4 + 1];
    double y = state[ball4 + 2];
    double vy = state[ball4 + 3];
    double x0;

    if (Math.abs(vx) < 1e5) {
      switch (sector) {
      case 1:
        x0 = x + l2; // x relative to the center of the circle
        return solveQuadratic(y * vy, x0 * x0 + y * y - r * r);
      case 2:
        if (vy > 0.0) {
          // top
          return (r - y) / vy;
        } else {
          return (-r - y) / vy;
          // bottom
        }
      case 3:
        x0 = x - l2; // x relative to the center of the circle
        return solveQuadratic(y * vy, x0 * x0 + y * y - r * r);
      }
    } else if (vx > 0) {
      // ball moves right
      switch (sector) {
      case 1:
        // time when the ball enters the second sector
        time1 = (-l2 - x) / vx; // always > 0.0;
        // time when the ball hits the circle. solve the quadratic equation to
        // obtain it
        x0 = x + l2; // x relative to the center of the circle
        time2 = solveQuadratic(y * vy + x0 * vx, x0 * x0 + y * y - r * r);
        if (time1 >= time2 && time2 > 0.0) {
          // bounced off the wall
          if (vx * x0 + vy * y > 0.0) {
            return time2;
          }
        }

        // advance to a valid starting point for the second section
        x += vx * time1;
        y += vy * time1;
        // store the time we already advanced
        timesum += time1;
        // fallthrough to the second section
      case 2:
        // time when the ball enters the third sector
        time1 = (l2 - x) / vx;

        // time when the ball hits the top or bottom line
        if (vy > 0.0) {
          // top
          time2 = (r - y) / vy;
        } else {
          time2 = (-r - y) / vy;
          // bottom
        }

        if (time1 >= time2) {
          return timesum + time2;
        }

        // advance to a valid starting point for the third section
        x += vx * time1;
        y += vy * time1;
        // store the time we already advanced
        timesum += time1;
        // fallthrough to the third section
      case 3:
        x0 = x - l2; // adjust x
        // time when the border of the circle is hit
        time1 = solveQuadratic(y * vy + x0 * vx, x0 * x0 + y * y - r * r);
        return timesum + time1;
      }
    } else {
      // ball moves left
      switch (sector) {
      case 3:
        // time when the ball enters the second sector
        time1 = (l2 - x) / vx; // always > 0.0;
        // time when the ball hits the circle. solve the quadratic equation to
        // obtain it
        x0 = x - l2; // x relative to the center of the circle
        time2 = solveQuadratic(y * vy + x0 * vx, x0 * x0 + y * y - r * r);
        if (time1 >= time2 && time2 > 0.0) {
          // bounced off the wall
          if (vx * x0 + vy * y > 0.0) {
            return time2;
          }
        }

        // advance to a valid starting point for the second section
        x += vx * time1;
        y += vy * time1;
        // store the time we already advanced
        timesum += time1;
        // fallthrough to the second section
      case 2:
        // time when the ball enters the third sector
        time1 = (-l2 - x) / vx;

        // time when the ball hits the top or bottom line
        if (vy > 0.0) {
          // top
          time2 = (r - y) / vy;
        } else {
          time2 = (-r - y) / vy;
          // bottom
        }

        if (time1 >= time2) {
          return timesum + time2;
        }

        // advance to a valid starting point for the third section
        x += vx * time1;
        y += vy * time1;
        // store the time we already advanced
        timesum += time1;
        // fallthrough to the third section
      case 1:
        x0 = x + l2; // adjust x
        // time when the border of the circle is hit
        time1 = solveQuadratic(y * vy + x0 * vx, x0 * x0 + y * y - r * r);
        return timesum + time1;
      }
    }
    return 0.0;
  }

  public void doStep() {
    int minball = 0;
    double minstep = 0.02;
    double mintime = (2 * r + l) / 1.0; // t=s/v with normalized v: initialize
                                        // with worst case time
    double time;

    int numballs = (state.length - 1) / 4;
    for (int ball = 0; ball < numballs; ++ball) {
      time = getBounceTime(0);
      if (time < mintime) {
        mintime = time;
        minball = ball;
      }
    }

    if (mintime < minstep) {
      advanceState(mintime);
      System.out.println(mintime);
      bounceBall(minball);
    } else {
      advanceState(minstep);
    }
  }

  private void advanceState(double timestep) {
    int numballs = (state.length - 1) / 4;
    int ball4;
    for (int ball = 0; ball < numballs; ++ball) {
      ball4 = ball * 4;
      state[ball4] += state[ball4 + 1] * timestep;
      state[ball4 + 2] += state[ball4 + 3] * timestep;
    }
    state[state.length - 1] += timestep;
  }

  private void bounceBall(int ball) {
    int ball4 = ball * 4;

    while (getSector(ball) == 0) {
      state[ball4] -= 1e-5 * state[ball4 + 1];
      state[ball4 + 2] -= 1e-5 * state[ball4 + 3];
    }

    double rx = state[ball4];
    double vx = state[ball4 + 1];
    double ry = state[ball4 + 2];
    double vy = state[ball4 + 3];
    double vr;

    switch (getSector(ball)) {
    case 1:
      // first sector: bounce off circle
      rx += l2;
      vr = -2 * (rx * vx + ry * vy) / (rx * rx + ry * ry);
      vx += rx * vr;
      vy += ry * vr;

      state[ball4 + 1] = vx;
      state[ball4 + 3] = vy;
      break;
    case 2:
      // second sector: invert the y movement
      state[ball4 + 3] = -vy;
      break;
    case 3:
      // third sector: bounce off circle
      rx -= l2;
      vr = -2 * (rx * vx + ry * vy) / (rx * rx + ry * ry);
      vx += rx * vr;
      vy += ry * vr;

      state[ball4 + 1] = vx;
      state[ball4 + 3] = vy;
      break;
    case 0:
      // outside: kill the program
      throw new RuntimeException("ball left space");
    }

    return;
  }

  public double getTime() {
    return t;
  }
}
