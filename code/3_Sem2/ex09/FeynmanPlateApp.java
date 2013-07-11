/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package ex09;

import java.awt.Color;

import org.opensourcephysics.controls.*;
import org.opensourcephysics.frames.PlotFrame;

/**
 * FeynmanPlateApp displays rigid body dynamics using quaternions.
 * 
 * @author Wolfgang Christian, Jan Tobochnik, Harvey Gould
 * @version 1.0 revised 06/05/2005
 */
public class FeynmanPlateApp extends AbstractSimulation {
  FeynmanPlate plate = new FeynmanPlate();
  PlotFrame anglePlot = new PlotFrame("t", "phi (around z-axis)",
      "angles of omega (broken, ignore me)");

  PlotFrame energyPlot = new PlotFrame("t", "E", "Energy");
  PlotFrame angMomPlot = new PlotFrame("t", "L", "Angular Momentum");
  PlotFrame omegaPlot = new PlotFrame("t", "omega", "angular velocity");

  /**
   * Initializes the simulation by reading parameters and passing them to the
   * rigid body model.
   */
  public void initialize() {
    energyPlot.clearData();
    angMomPlot.clearData();
    omegaPlot.clearData();

    boolean isBox = control.getBoolean("box");

    plate.setBox(isBox);

    plate.time = 0;
    plate.dt = control.getDouble("dt");
    plate.spaceL[0] = control.getDouble("Lx");
    plate.spaceL[1] = control.getDouble("Ly");
    plate.spaceL[2] = control.getDouble("Lz");

    double inertiaFactor = isBox ? 2 / 3. : 1.0;

    plate.setInertia(inertiaFactor, 2* inertiaFactor, 3 * inertiaFactor);

    // double[] boxSize = getBoxSizeFromInertia(new double[] { plate.I1,
    // plate.I2,
    // plate.I3 });
    // control.println("box size: [" + boxSize[0] + ", " + boxSize[1] + ", "
    // + boxSize[2] + "]");

    // initAnglePlot();
  }

  /**
   * just playing around
   * 
   * @param inertia
   * @return
   */
  double[] getBoxSizeFromInertia(double[] inertia) {
    double[] size = new double[] { 1, 0, 0 };

    size[1] = Math.sqrt(12 * inertia[2] - size[0] * size[0]);
    size[2] = Math.sqrt(12 * inertia[1] - size[0] * size[0]);

    return size;
  }

  /**
   * 
   */
  void initAnglePlot() {
    anglePlot.clearData();
    anglePlot.setConnected(true);
    anglePlot.setMarkerColor(0, Color.YELLOW);
    anglePlot.setLineColor(0, Color.YELLOW);
    anglePlot.setMarkerColor(1, Color.GREEN);
    anglePlot.setLineColor(1, Color.GREEN);
  }

  /**
   * Does an simulation step by advancing the time and updating the space view.
   */
  protected void doStep() {
    plate.advanceTime();

    // updateAnglePlot();

    energyPlot.append(0, plate.time, plate.getEnergy());
    angMomPlot.append(0, plate.time, plate.getAngularMomentum());
    omegaPlot.append(0, plate.time, plate.getAbsoluteOmega());
  }

  /**
   * 
   */
  void updateAnglePlot() {
    // FIXME fix angles
    double[] spaceOmega = plate.toBody.direct(new double[] { plate.wx,
        plate.wy, plate.wz });
    double angle = Math.atan2(spaceOmega[0], spaceOmega[1]);
    anglePlot.append(0, plate.time, angle);

    double[] spaceRight = plate.toBody.direct(new double[] { 1, 0, 0 });
    angle = Math.atan2(spaceRight[0], spaceRight[1]);
    anglePlot.append(1, plate.time, angle);
  }

  /**
   * Resets the simulation.
   */
  public void reset() {
    control.setValue("Lx", 0.1);
    control.setValue("Ly", 0.0);
    control.setValue("Lz", 1.0);
    control.setValue("dt", 0.1);
    control.setValue("box", false);
    enableStepsPerDisplay(true);
    initialize();
  }

  /**
   * Starts the Java application.
   * 
   * @param args
   *          command line parameters
   */
  public static void main(String[] args) {
    SimulationControl.createApp(new FeynmanPlateApp());
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
