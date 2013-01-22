/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package exercize10;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import org.opensourcephysics.display.*;
import org.opensourcephysics.numerics.ODE;
import org.opensourcephysics.numerics.ODESolver;
import org.opensourcephysics.numerics.RK4;

/**
 * Oscillators models the numeric solution of a chain of oscillators with fixed
 * end points.
 * 
 * Students should implement the ODE interface to complete the exercise in
 * "An Introduction to Computer Simulation Methods."
 * 
 * @author Wolfgang Christian, Jan Tobochnik, Harvey Gould
 * @version 1.0
 */
public class Oscillators implements Drawable, ODE {
  OscillatorsMode normalMode;
  Circle circle = new Circle();
  double[] state; // displacement
  ODESolver solver;
  private String bc;
  private double forceOmega;
  private double forceAmplitude;
  private double frictionGamma;
  private int lowMassId2;

  /**
   * Constructs a chain of coupled oscillators in the given mode and number of
   * oscillators.
   * 
   * @param positioning
   *          initial positioning. Must be one of "mode", "random" and "pulse".
   * @param mode
   *          initial oscillation mode
   * @param N
   *          number of particles without ghost images
   * @param lowMassId
   *          id of the particle with reduced mass (1/4 with respect to the
   *          other particles)
   * @param bc
   *          boundary conditions
   */
  public Oscillators(String positioning, int mode, int N, int lowMassId,
      String bc) {
    this.state = new double[2 * (N + 2) + 1]; // includes the two ends of the
                                              // chain
    this.bc = bc;
    this.lowMassId2 = lowMassId*2;

    if (positioning.equals("mode")) {
      int correction = 0;
      int offset = 0;

      if (this.bc.equals("periodic")) {
        correction = 1;
      } else if (this.bc.equals("free")) {
        correction = 2;
        offset = 1;
      }

      this.normalMode = new OscillatorsMode(mode, N - correction);
      if (this.bc.equals("free")) {
        normalMode.setPhase(Math.PI / 2);
      }
      for (int i = offset, n = getNum(); i < n; ++i) {
        // initial displacement
        state[2 * i] = normalMode.evaluate(i - offset);
      }
    } else if (positioning.equals("random")) {
      // initialize on random positions
      Random rand = new Random();
      for (int i = 2; i < getNum2() - 2; i += 2) {
        state[i] = rand.nextDouble() - 0.5;
      }
    } else if (positioning.equals("pulse")) {
      if (state.length < 12) {
        throw new RuntimeException(
            "Too few particles for positioning type 'pulse'. You need at least 5");
      }

      int offset = 76;
      state[offset + 2] = 0.2;
      state[offset + 4] = 0.6;
      state[offset + 6] = 1.0;
      state[offset + 8] = 0.6;
      state[offset + 10] = 0.2;
    }

    solver = new RK4(this);
  }

  /**
   * @return number of atoms
   */
  public int getNum() {
    return (state.length - 1) / 2;
  }

  /**
   * @return number of atoms * 2
   */
  public int getNum2() {
    return state.length - 1;
  }

  private void applyBoundaries(double[] state) {
    int l = state.length;

    if (bc.equals("fixed")) {
      // ghost particles don't move
      state[0] = 0.0;
      state[l - 2] = 0.0;
    } else if (bc.equals("periodic")) {
      // ghost particles follow periodic images
      state[0] = state[l - 4];
      state[l - 2] = state[2];
    } else if (bc.equals("free")) {
      // ghost particles follow their respective ends of the chain
      state[0] = state[2];
      state[l - 2] = state[l - 4];
    } else if (bc.equals("free")) {
      throw new RuntimeException("Unknown boundary condition:" + bc);
    }
  }

  /**
   * Draws the oscillators
   * 
   * @param drawingPanel
   * @param g
   */
  public void draw(DrawingPanel drawingPanel, Graphics g) {
    applyBoundaries(state);

    for (int i = 0, n = getNum2(); i < n; i += 2) {
      if (i == lowMassId2) {
        circle.color = Color.BLUE;
      } else {
        circle.color = Color.RED;
      }
      circle.setXY(i / 2, state[i]);
      circle.draw(drawingPanel, g);
    }
  }

  /**
   * Returns the externally applied force
   * 
   * @param time
   * @return the force (same for every particle)
   */
  private double getForce(double time) {
    return forceAmplitude * Math.sin(forceOmega * time);
  }

  @Override
  public void getRate(double[] state, double[] rate) {
    applyBoundaries(state);

    double force = getForce(state[state.length - 1]);

    for (int i = 2; i < getNum2() - 2; i += 2) {
      rate[i] = state[i + 1];
      rate[i + 1] = (state[i - 2] + state[i + 2] - 2 * state[i]) - state[i + 1]
          * frictionGamma + force;
      if (i == lowMassId2) {
        rate[i + 1] *= 4;
      }
    }

    rate[getNum2()] = 1.0; // time
  }

  @Override
  public double[] getState() {
    return state;
  }

  /**
   * @return numerically calculated positions of the particles for the current
   *         time
   */
  public double[] numericalPositions() {
    double[] pos = new double[state.length / 2];

    for (int i = 0; i < getNum(); ++i) {
      pos[i] = state[2 * i];
    }

    return pos;
  }

  /**
   * set Boundary Conditions on the fly
   * 
   * @param bc
   *          boundary conditions. See source code of applyBoundaries() for
   *          details
   */
  public void setBC(String bc) {
    this.bc = bc;
  }

  /**
   * @return the number of particles, excluding ghost images
   */
  public int size() {
    return state.length / 2 - 2;
  }

  /**
   * Steps the time using the given time step.
   * 
   * @param dt
   */
  public void step(double dt) {
    solver.setStepSize(dt);
    solver.step();
  }

  /**
   * @return current time
   */
  public double getTime() {
    return state[getNum2()];
  }

  /**
   * @return position of the blue ball
   */
  public double getBluePos() {
    return state[lowMassId2];
  }
}

/*
 * Open Source Physics software is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License (GPL) as
 * published by the Free Software Foundation; either version 2 of the License,
 * or(at your option) any later version.
 * 
 * Code that uses any portion of the code in the org.opensourcephysics package
 * or any subpackage (subdirectory) of this package must must also be be
 * released under the GNU GPL license.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this; if not, write to the Free Software Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston MA 02111-1307 USA or view the license online at
 * http://www.gnu.org/copyleft/gpl.html
 * 
 * Copyright (c) 2007 The Open Source Physics project
 * http://www.opensourcephysics.org
 */
