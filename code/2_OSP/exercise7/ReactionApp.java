/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package exercise7;

import org.opensourcephysics.controls.*;
import org.opensourcephysics.frames.*;

/**
 * Simulates random walkers in one dimension
 * 
 * @author Jan Tobochnik, Wolfgang Christian, Harvey Gould
 * @version 1.0 revised 04/21/05
 */
public class ReactionApp extends AbstractSimulation {
  Reaction walker = new Reaction();
  PlotFrame plotFrame = new PlotFrame("x", "y", "Bees");
  PlotFrame meanPlot = new PlotFrame("time", "<x>, <y>", "Means");
  PlotFrame varPlot = new PlotFrame("time", "sigmax2,sigmay2,<R2>", "Variances");
  PlotFrame logLogVarPlot = new PlotFrame("time", "sigmax2,sigmay2,<R2>",
      "LogLog Variances");

  /**
   * Sets column names for data table
   */
  public ReactionApp() {
    plotFrame.addDrawable(walker);

    meanPlot.setConnected(true);
    varPlot.setConnected(true);
    logLogVarPlot.setConnected(true);
  }

  /**
   * Gets parameters and initializes model
   */
  public void initialize() {
    walker.initialize(control.getInt("N"));
    meanPlot.clearData();
    varPlot.clearData();
  }

  public double xMean() {
    int num = walker.n();
    double x = 0.0;

    for (int i = 0; i < num; ++i) {
      x += walker.x(i);
    }

    return x / num;
  }

  public double yMean() {
    int num = walker.n();
    double y = 0.0;

    for (int i = 0; i < num; ++i) {
      y += walker.y(i);
    }

    return y / num;
  }

  /**
   * Does one walker at a time
   */
  public void doStep() {
    int steps = control.getInt("steps per display");
    for (int i = 0; i < steps; ++i) {
      walker.step();
    }

    int t = walker.t();

    double xm = xMean();
    double ym = yMean();
    double x2 = xVar(xm);
    double y2 = yVar(ym);
    meanPlot.append(0, t, xm);
    meanPlot.append(1, t, ym);
    varPlot.append(0, t, x2);
    varPlot.append(1, t, y2);
    varPlot.append(2, t, x2 + y2);

    logLogVarPlot.append(0, Math.log(t), Math.log(x2));
    logLogVarPlot.append(1, Math.log(t), Math.log(y2));
    logLogVarPlot.append(2, Math.log(t), Math.log(x2 + y2));

    plotFrame.setMessage(t + " steps");
  }

  private double xVar(double xm) {
    int num = walker.n();
    double x2 = 0.0;
    double x;

    for (int i = 0; i < num; ++i) {
      x = walker.x(i);
      x2 += x * x;
    }
    return x2 / num - xm * xm;
  }

  private double yVar(double ym) {
    int num = walker.n();
    double y2 = 0.0;
    double y;

    for (int i = 0; i < num; ++i) {
      y = walker.y(i);
      y2 += y * y;
    }
    return y2 / num - ym * ym;
  }

  /**
   * Plots data when user stops the simulation.
   */
  public void stopRunning() {
    plotFrame.clearData();
    for (int t = 0; t <= walker.n(); t++) {
      // double xbar = walker.xAccum[t]*1.0/trials;
      // double x2bar = walker.xSquaredAccum[t]*1.0/trials;
      // plotFrame.append(0, 1.0*t, xbar);
      // plotFrame.append(1, 1.0*t, x2bar-xbar*xbar);
    }
    plotFrame.repaint();
  }

  /**
   * Resets to default values
   */
  public void reset() {
    control.setValue("N", 100);
    control.setAdjustableValue("steps per display", 10);
  }

  /**
   * Starts the Java application.
   * 
   * @param args
   *          command line parameters
   */
  public static void main(String[] args) {
    SimulationControl.createApp(new ReactionApp());
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
