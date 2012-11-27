package exercise5;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import org.opensourcephysics.display.Drawable;
import org.opensourcephysics.display.DrawingPanel;

public class Billiard implements Drawable {
  double r, l;
  double state[];
  private double l2;
  Random rand;
  private double r2;
  private double h2;

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
  public void setProperties(double r, double l, double holesize, int balls) {
    this.r = r;
    this.r2 = r * r;
    this.l = l;
    this.l2 = l / 2;
    this.h2 = holesize / 2;

    state = new double[balls * 4 + 1];
  }

  /**
   * randomize all ball positions and
   */
  public void randomize() {
    int numballs = (state.length - 1) / 4;
    double x, y, vx, vy;

    for (int ball = 0; ball < numballs; ++ball) {
      do {
        x = rand.nextDouble() * (l + 2 * r) - (l2 + r);
        y = rand.nextDouble() * 2 * r - r;
        vx = rand.nextDouble() - 0.5;
        vy = rand.nextDouble() - 0.5;

        setBall(ball, x, y, vx, vy);
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
    int ball4 = ball * 4;
    double x = state[ball4];
    double y = state[ball4 + 2];

    // first sector (left)
    if (x < -l2) {
      double dist2 = Math.pow(x + l2, 2) + Math.pow(y, 2);
      if (dist2 > r2) {
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
      if (dist2 > r2) {
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

    g.setColor(Color.black);

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

    // hole on the right end of the third sector

    if (h2 > 0.0) {
      x1 = panel.xToPix(l2 + r) - 5;
      x2 = Math.abs(panel.xToPix(l2 + r) - x1);
      y1 = panel.yToPix(h2);
      y2 = Math.abs(y1 - panel.yToPix(-h2));
      g.fillRect(x1, y1, x2, y2);
    }

    g.setColor(Color.blue);

    // draw balls
    for (int ball4 = 0; ball4 < state.length - 1; ball4 += 4) {
      x1 = panel.xToPix(state[ball4]);
      y1 = panel.yToPix(state[ball4 + 2]);
      x2 = panel.xToPix(state[ball4] + state[ball4 + 1] * 0.1);
      y2 = panel.yToPix(state[ball4 + 2] + state[ball4 + 3] * 0.1);

      g.fillOval(x1 - 5, y1 - 5, 10, 10);
      g.drawLine(x1, y1, x2, y2);
    }
  }

  public double veclength(double x, double y) {
    return Math.sqrt(x * x + y * y);
  }

  public double getBounceTime(int ball) {
    int ball4 = ball * 4;
    int sector = getApproxSector(ball);
    double time1 = 0.0, time2 = 0.0, timesum = 0.0;

    double x = state[ball4];
    double vx = state[ball4 + 1];
    double y = state[ball4 + 2];
    double vy = state[ball4 + 3];
    double x0, rdotv, secdist;

    if (vx > 0) {
      // ball moves right
      switch (sector) {
      case 1:
        x0 = x + l2; // x relative to the center of the circle

        // time when the ball enters the second sector
        if (vx == 0) {
          // exorbitantly large value compared to the y velocity
          time1 = 5 * r;
        } else {
          time1 = -x0 / vx;
        }

        // time when the ball hits the circle using a secant method
        // get distance between secant and center
        rdotv = x0 * vx + y * vy;
        secdist = veclength(x0 - rdotv * vx, y - rdotv * vy);
        // get time until the circle is reached. This equates to the distance
        // between the current position on the secant and the circle, which can
        // be calculated using the distance to the secant and the radius of the
        // circle using pythagoras' law.
        time2 = Math.sqrt(r2 - secdist * secdist) - rdotv;
        // time2 = solveQuadratic(y * vy + x0 * vx, x0 * x0 + y * y - r2);

        // + time2);

        if (time1 >= time2) {
          // bounced off the wall
          return time2;
        }

        // advance to a valid starting point for the second section
        x += vx * time1;
        y += vy * time1;
        // store the time we already advanced
        timesum += time1;
        // fallthrough to the second section
      case 2:
        // time when the ball enters the third sector
        if (vx == 0) {
          // exorbitantly large value compared to the y velocity
          time1 = 5 * r;
        } else {
          time1 = (l2 - x) / vx;
        }

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
        // time when the ball hits the circle using a secant method
        x0 = x - l2; // adjust x

        // get distance between secant and center
        rdotv = x0 * vx + y * vy;
        secdist = veclength(x0 - rdotv * vx, y - rdotv * vy);
        // get time until the circle is reached. This equates to the distance
        // between the current position on the secant and the circle, which can
        // be calculated using the distance to the secant and the radius of the
        // circle using pythagoras' law.
        time1 = Math.sqrt(r2 - secdist * secdist) - rdotv;
        // time1 = solveQuadratic(y * vy + x0 * vx, x0 * x0 + y * y - r2);

        return timesum + time1;
      }
    } else {
      // ball moves left
      switch (sector) {
      case 3:
        // time when the ball enters the second sector
        if (vx == 0) {
          // exorbitantly large value compared to the y velocity
          time1 = 5 * r;
        } else {
          time1 = (l2 - x) / vx; // always > 0.0;
        }

        // time when the ball hits the circle using a secant method
        x0 = x - l2; // x relative to the center of the circle

        // get distance between secant and center
        rdotv = x0 * vx + y * vy;
        secdist = veclength(x0 - rdotv * vx, y - rdotv * vy);
        // get time until the circle is reached. This equates to the distance
        // between the current position on the secant and the circle, which can
        // be calculated using the distance to the secant and the radius of the
        // circle using pythagoras' law.
        time2 = Math.sqrt(r2 - secdist * secdist) - rdotv;
        // time2 = solveQuadratic(y * vy + x0 * vx, x0 * x0 + y * y - r2);

        if (time1 >= time2) {
          // bounced off the wall
          return time2;
        }

        // advance to a valid starting point for the second section
        x += vx * time1;
        y += vy * time1;
        // store the time we already advanced
        timesum += time1;
        // fallthrough to the second section
      case 2:
        // time when the ball enters the third sector
        if (vx == 0) {
          // exorbitantly large value compared to the y velocity
          time1 = 5 * r;
        } else {
          time1 = (-l2 - x) / vx;
        }

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
        // time when the ball hits the circle using a secant method
        x0 = x + l2; // x relative to the center of the circle

        // get distance between secant and center
        rdotv = x0 * vx + y * vy;
        secdist = veclength(x0 - rdotv * vx, y - rdotv * vy);
        // get time until the circle is reached. This equates to the distance
        // between the current position on the secant and the circle, which can
        // be calculated using the distance to the secant and the radius of the
        // circle using pythagoras' law.
        time1 = Math.sqrt(r2 - secdist * secdist) - rdotv;
        // time1 = solveQuadratic(y * vy + x0 * vx, x0 * x0 + y * y - r2);

        return timesum + time1;
      }
    }

    throw new RuntimeException("getBounceTime: ball isn't inside the space");
  }

  public void doStep() {
    if (state.length == 1) {
      return;
    }

    int minball = 0;
    double mintime = (2 * r + l) / 1.0; // t=s/v with normalized v: initialize
                                        // with worst case time
    double time;

    int numballs = (state.length - 1) / 4;
    for (int ball = 0; ball < numballs; ++ball) {
      time = getBounceTime(ball);

      if (time < mintime) {
        mintime = time;
        minball = ball;
      }
    }

    advanceState(mintime);
    bounceBall(minball);
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

    double rx = state[ball4];
    double vx = state[ball4 + 1];
    double ry = state[ball4 + 2];
    double vy = state[ball4 + 3];
    double vr;

    switch (getApproxSector(ball)) {
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

      // remove ball if it enters the hole
      if (ry < h2 && ry > -h2) {
        double oldstate[] = state;
        state = new double[state.length - 4];
        int offset = 0;
        for (int i = 0; i < state.length; ++i) {
          if (i == ball4) {
            offset = 4;
          }
          state[i] = oldstate[i + offset];
        }

        break;
      }

      break;
    case 0:
      // outside: kill the program
      throw new RuntimeException("bounceBall: Ball left space");
    }

    return;
  }

  public double getTime() {
    return state[state.length - 1];
  }

  public double[] getBall(int ball) {
    double ballstate[] = new double[4];
    int ball4 = ball * 4;

    for (int i = 0; i < 4; ++i) {
      ballstate[i] = state[ball4 + i];
    }

    return ballstate;
  }

  public int getNumBalls() {
    if (state == null) {
      return 0;
    }
    return (state.length - 1) / 4;
  }

  /**
   * retrieves and returns the sector
   * 
   * @return sector (1, 2, 3 if inside, 0 if outside)
   */
  public int getApproxSector(int ball) {
    int ball4 = ball * 4;
    double x = state[ball4];

    // first sector (left)
    if (x < -l2) {
      return 1;
    } else if (x <= l2) {
      return 2;
    } else {
      return 3;
    }
  }

  public double veclength2(double x, double y) {
    return x * x + y * y;
  }
}
