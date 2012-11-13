/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package exercise3;

import org.opensourcephysics.controls.*;
import org.opensourcephysics.frames.*;

/**
 * Planet2App models and displays two interacting planets in the presence of a
 * central inverse square law force.
 * 
 * This application demonstrates:
 * <ol>
 * <li>how to use the ODE interface.</li>
 * <li>how to use the ODESolver interface.</li>
 * <li>how to use the Animation control to run and single-step a differential
 * equation.</li>
 * </ol>
 * Students should test other ODESolvers in the Numerics package.
 * 
 * @author Wolfgang Christian, Jan Tobochnik, Harvey Gould
 * @version 1.0
 */
public class TwinStarApp extends AbstractSimulation {
  PlotFrame frame = new PlotFrame("x (AU)", "y (AU)", "Planet Program");
  PlotFrame energy = new PlotFrame("time", "energy",
      "Energy (r=kin, g=pot, b=tot");
  Planet3 planet3 = new Planet3();

  /**
   * Constructs the PlanetApp.
   */
  public TwinStarApp() {
    frame.addDrawable(planet3);
    frame.setPreferredMinMax(-2, 2, -2, 2);
    frame.setSquareAspect(true);
    frame.setConnected(true);
  }

  /**
   * Steps the time.
   */
  public void doStep() {
    int steps = control.getInt("calculations per step");
    for (int i = 0; i < steps; i++) { // do 5 steps between screen draws
      planet3.doStep(); // advances time
    }

    double time = planet3.getTime();

    // Angular Momentum
    // double amom1 = planet2.getAMom(0);
    // double amom2 = planet2.getAMom(1);
    // double amomt = amom1 + amom2;
    // amom.append(0, time, amom1);
    // amom.append(1, time, amom2);
    // amom.append(2, time, amomt);

    // Energy
    double T = planet3.getKineticEnergy();
    double V = planet3.getPotentialEnergy();
    double E = T + V;

    energy.append(0, time, T);
    energy.append(1, time, V);
    energy.append(2, time, E);

  }

  /**
   * Initializes the animation using the values in the control.
   */
  public void initialize() {
    planet3.odeSolver.setStepSize(control.getDouble("dt"));
    double x = control.getDouble("x");
    double y = control.getDouble("y");
    double vx = control.getDouble("vx");
    double vy = control.getDouble("vy");
    planet3.initialize(new double[] { x, vx, y, vy, 0 });
    frame.setMessage("t=0");
    // amom.clearData();
    energy.clearData();
  }

  /**
   * Resets animation to a predefined state.
   */
  public void reset() {
    control.setValue("x", 0.1);
    control.setValue("y", 1);
    control.setValue("vx", 0);
    control.setValue("vy", 0);
    control.setValue("dt", 0.01); // initial step size
    control.setAdjustableValue("calculations per step", 5);
    initialize();
  }

  /**
   * Start Java application.
   * 
   * @param args
   *          command line parameters
   */
  public static void main(String[] args) {
    SimulationControl.createApp(new TwinStarApp());
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
