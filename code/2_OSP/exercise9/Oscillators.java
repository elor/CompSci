/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package exercise9;

import java.awt.Graphics;
import org.opensourcephysics.display.*;
import org.opensourcephysics.numerics.ODE;
import org.opensourcephysics.numerics.ODESolver;
import org.opensourcephysics.numerics.RK4;

/**
 * Oscillators models the analytic soution of a chain of oscillators with fixed
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
  double[] modePos; // positions of the initial mode (== amplitudes)
  double time = 0;
  private double k;
  ODESolver solver;
  private String bc;

  /**
   * Constructs a chain of coupled oscillators in the given mode and number of
   * oscillators.
   * 
   * @param mode  initial oscillation mode
   * @param N number of particles without ghost images
   * @param k spring parameter (actually k/m)
   * @param bc  boundary conditions. Can be one of "fixed", "periodic" and "free"
   */
  public Oscillators(int mode, int N, double k, String bc) {
    this.state = new double[2 * (N + 2)]; // includes the two ends of the chain
    this.modePos = new double[N + 2];
    this.k = k;
    this.bc = bc;
    normalMode = new OscillatorsMode(mode, N);
    for (int i = 0, n = state.length / 2; i < n; ++i) {
      modePos[i] = state[2 * i] = normalMode.evaluate(i); // initial
                                                          // displacement
    }

    solver = new RK4(this);
  }

  /**
   * Draws the oscillators
   * 
   * @param drawingPanel
   * @param g
   */
  public void draw(DrawingPanel drawingPanel, Graphics g) {
    applyBoundaries(state);
    normalMode.draw(drawingPanel, g); // draw initial condition
    for (int i = 0, n = state.length; i < n; i += 2) {
      circle.setXY(i / 2, state[i]);
      circle.draw(drawingPanel, g);
    }
  }

  @Override
  public void getRate(double[] state, double[] rate) {
    applyBoundaries(state);

    rate[0] = rate[rate.length - 1] = 0.0;

    for (int i = 2; i < rate.length - 2; i += 2) {
      rate[i] = state[i + 1];
      rate[i + 1] = k * (state[i - 2] + state[i + 2] - 2 * state[i]);
    }
  }

  private void applyBoundaries(double[] state) {
    int l = state.length;

    if (bc == "fixed") {
      // ghost particles don't move
      state[0] = 0.0;
      state[l - 2] = 0.0;
    } else if (bc == "periodic") {
      // ghost particles follow periodic images
      state[0] = state[l - 4];
      state[l - 2] = state[2];
    } else if (bc == "free") {
      // ghost particles follow their respective ends of the chain
      state[0] = state[2];
      state[l - 2] = state[l - 4];
    } else {
      throw new RuntimeException("Unknown boundary condition.");
    }
  }

  @Override
  public double[] getState() {
    return state;
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
    time += dt;
    solver.setStepSize(dt);
    solver.step();
  }

  /**
   * @return analytically calculated particle positions for current time 
   */
  public double[] analyticalPositions() {
    double[] pos = new double[modePos.length];

    double phase = Math.cos(time * normalMode.omega);
    for (int i = 0; i < pos.length; ++i) {
      pos[i] = modePos[i] * phase;
    }

    return pos;
  }

  /**
   * @return numerically calculated positions of the particles for the current time
   */
  public double[] numericalPositions() {
    double[] pos = new double[state.length / 2];

    for (int i = 0; i < state.length / 2; ++i) {
      pos[i] = state[2 * i];
    }

    return pos;
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
