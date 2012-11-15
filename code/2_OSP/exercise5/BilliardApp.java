/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package exercise5;

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
public class BilliardApp extends AbstractSimulation {
  PlotFrame frame = new PlotFrame("x (AU)", "y (AU)", "Planet Program");
  Billiard billiard = new Billiard();
  
  /**
   * Constructs the PlanetApp.
   */
  public BilliardApp() {
    frame.addDrawable(billiard);
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
      billiard.doStep(); // advances time
    }

//    double time = billiard.getTime();
  }

  /**
   * Initializes the animation using the values in the control.
   */
  public void initialize() {
    double r = control.getDouble("r");
    double l = control.getDouble("l");
    double x = control.getDouble("x");
    double y = control.getDouble("y");
    double px = control.getDouble("px");
    double py = control.getDouble("py");
    billiard.setProperties(r, l, x, y, px, py);
//    frame.setMessage("t=0");
  }

  /**
   * Resets animation to a predefined state.
   */
  public void reset() {
    control.setValue("r", 1);
    control.setValue("l", 2);
    control.setValue("x", 0.1);
    control.setValue("y", 1);
    control.setValue("px", 1);
    control.setValue("py", 0);
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
    SimulationControl.createApp(new BilliardApp());
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
