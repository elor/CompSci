/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package exercise3;

import java.awt.*;
import org.opensourcephysics.display.*;
import org.opensourcephysics.numerics.*;

/**
 * Planet2 models two interacting planets in the presence of a central inverse
 * square law force.
 * 
 * @author Wolfgang Christian, Jan Tobochnik, Harvey Gould
 * @version 1.0
 */
public class Planet3 implements Drawable, ODE {
  // GM in units of (AU)^3/(yr)^2
  final static double GM = 4 * Math.PI * Math.PI;
  final static double GM1 = 0.001 * GM; // inner planet
  double[] state = new double[5];
  ODESolver odeSolver = new RK45MultiStep(this);
  Mass masssun1 = new Mass();
  Mass masssun2 = new Mass();
  Mass mass1 = new Mass();

  /**
   * Steps the time using an ode solver.
   */
  public void doStep() {
    odeSolver.step();
    mass1.setXY(state[0], state[2]);
  }

  /**
   * Draws the three bodies.
   * 
   * @param panel
   * @param g
   */
  public void draw(DrawingPanel panel, Graphics g) {
    mass1.draw(panel, g);
    masssun1.draw(panel, g);
    masssun2.draw(panel, g);
  }

  /**
   * Initializes the positions and velocities with the given state.
   * 
   * @param initState
   */
  void initialize(double[] initState) {
    System.arraycopy(initState, 0, state, 0, initState.length);
    mass1.clear(); // clears data from the old trail
    mass1.setXY(state[0], state[2]);
    masssun1.setXY(-1, 0);
    masssun2.setXY(1, 0);
  }

  /**
   * Gets the rate using the given state.
   * 
   * Values in the rate array are overwritten.
   * 
   * @param state
   *          the state
   * @param rate
   *          the resulting rate
   */
  public void getRate(double[] state, double[] rate) {
    // state[]: x1, vx1, y1, vy1, x2, vx2, y2, vy2, t
    double dx1 = state[0] - 1;
    double dx2 = state[0] + 1;
    double r1Squared = (dx1 * dx1) + (state[2] * state[2]); // r1
    double r2Squared = (dx2 * dx2) + (state[2] * state[2]); // r2
    double r1Cubed = r1Squared * Math.sqrt(r1Squared);
    double r2Cubed = r2Squared * Math.sqrt(r2Squared);

    // squared
    rate[0] = state[1]; // x1 rate
    rate[2] = state[3]; // y1 rate
    rate[1] = -GM * (dx1 / r1Cubed + dx2 / r2Cubed);
    rate[3] = -GM * (state[2] / r1Cubed + state[2] / r2Cubed);
    rate[4] = 1; // time rate
  }

  /**
   * Gets the state: x1, vx1, y1, vy1, x2, vx2, y2, vy2, t.
   * 
   * @return the state
   */
  public double[] getState() {
    return state;
  }

  /**
   * Class Mass
   */
  class Mass extends Circle {
    Trail trail = new Trail();

    /**
     * Draws the mass
     * 
     * @param panel
     * @param g
     */
    public void draw(DrawingPanel panel, Graphics g) {
      trail.draw(panel, g);
      super.draw(panel, g);
    }

    /**
     * Clears the trail.
     * 
     */
    void clear() {
      trail.clear();
    }

    /**
     * Sets the postion and adds to the trail.
     * 
     * @param x
     * @param y
     */
    public void setXY(double x, double y) {
      super.setXY(x, y);
      trail.addPoint(x, y);
    }
  }

  // public double getAMom(int i) {
  // double L = -1.0;
  // switch (i) {
  // case 0:
  // L = GM1 * (state[0] * state[3] - state[2] * state[1]);
  // break;
  // case 1:
  // L = GM2 * (state[4] * state[7] - state[6] * state[5]);
  // break;
  // }
  //
  // return L;
  // }

  /**
   * @return time
   */
  public double getTime() {
    return state[4];
  }

  /**
   * @return kinetic energy
   */
  public double getKineticEnergy() {
    return GM1 * (state[1] * state[1] + state[3] * state[3]) / 2;
  }

  /**
   * @return potential energy
   */
  public double getPotentialEnergy() {
    double V1 = -1.0; // potential energy relative to sun
    double V2 = -1.0; // potential energy relative to the other planet

    double dx1 = state[0] - 1.0;
    double dx2 = state[0] + 1.0;
    double dy = state[2];

    V1 = -GM * GM1 / (Math.sqrt(dx1 * dx1 + dy * dy));
    V2 = -GM * GM1 / (Math.sqrt(dx2 * dx2 + dy * dy));

    return V1 + V2;
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
