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
  Reaction reaction = new Reaction();
  PlotFrame posPlot = new PlotFrame("x", "step", "Positions");
  PlotFrame countPlot = new PlotFrame("log(t)", "log(count)", "Counts (loglog)");
  HistogramFrame nearestHist = new HistogramFrame("r", "P(r)", "Nearest Neighbor Distance");
  private int steps;

  /**
   * Sets column names for data table
   */
  public ReactionApp() {
    countPlot.setConnected(true);
  }

  /**
   * Gets parameters and initializes model
   */
  public void initialize() {
    int n = control.getInt("N");
    reaction.setDirectInteraction(control.getBoolean("direct interaction"));
    reaction.initialize(n);
    countPlot.clearData();
    nearestHist.clearData();
    steps = 1;
    posPlot.setPreferredMinMax(-1, n, -2, 2);
    updatePlots();
  }

  /**
   * Does one walker at a time
   */
  public void doStep() {
    for (int i = 0; i < steps; ++i) {
      reaction.step();
    }
    
    steps *= 2;

    int t = reaction.t();

    updatePlots();
    posPlot.setMessage(t + " steps");
  }

  private void updatePlots() {
    // Position Plot
    posPlot.clearData();
    for (int i = 0; i < reaction.length(); ++i) {
      if (reaction.at(i) != 0) {
        posPlot.append(0, i, reaction.at(i));
      }
    }

    // Count Plot
    countPlot.append(0, Math.log(reaction.t()), Math.log(reaction.count()));
    
    // NN Histogram
    nearestHist.clearData();
    int[] distances = reaction.getNearestNeighborDistances();
    
    for (int d : distances) {
      nearestHist.append(d);
    }
  }

  /**
   * Resets to default values
   */
  public void reset() {
    control.setValue("N", 5000);
    control.setValue("direct interaction", false);
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
