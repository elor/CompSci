/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package ex03;

import org.opensourcephysics.controls.*;
import org.opensourcephysics.frames.*;
import java.awt.Color;

/**
 * Creates percolation cluster with probability p and computes mass
 * distribution.
 * 
 * @author Jan Tobochnik, Wolfgang Christian, Harvey Gould
 * @version 1.0 revised 06/21/05
 */
public class SingleClusterApp3 extends AbstractSimulation {
  SingleCluster cluster = new SingleCluster();
  LatticeFrame latticeFrame = new LatticeFrame("Percolation cluster");
  PlotFrame plot = new PlotFrame("p", "Ds", "think of a title");

  Ant ants[];

  public void initialize() {
    latticeFrame.setIndexedColor(0, Color.BLACK); // not occupied or tested
    latticeFrame.setIndexedColor(1, Color.BLUE); // occupied
    latticeFrame.setIndexedColor(2, Color.GREEN); // perimeter or growth site
    latticeFrame.setIndexedColor(-1, Color.YELLOW); // permanently not occupied

    plot.clearData();
    double pmin = control.getDouble("pmin");
    double pmax = control.getDouble("pmax");
    plot.setPreferredMinMax(pmin, pmax, 0.0, 1.1);
    cluster.L = control.getInt("L");
    cluster.p = control.getDouble("pmin");
  }

  /**
   * 
   */
  void createCluster() {
    cluster.initialize();
    while (cluster.perimeterNumber != 0) {
      cluster.step();
    }
    latticeFrame.setAll(cluster.site);

    ants = new Ant[control.getInt("number of ants")];

    for (int i = 0; i < ants.length; ++i) {
      ants[i] = new Ant(cluster);
    }
  }

  public void doStep() {
    int maxstep = control.getInt("max number of steps");
    int numclusters = control.getInt("clusters per step");

    double Dacc = 0.0;

    for (int c = 0; c < numclusters; ++c) {
      double Ds = 0.0;
      createCluster();

      for (int t = 1; t <= maxstep; ++t) {
        double R2 = 0.0;
        for (int i = 0; i < ants.length; ++i) {
          ants[i].step();
          R2 += ants[i].getR2();
        }

        R2 /= ants.length;

        Ds += R2 / t;
      }

      Ds /= maxstep;
      Dacc += Ds;
    }

    Dacc /= numclusters;
    plot.append(0, cluster.p, Dacc);

    cluster.p += control.getDouble("pstep");

    if (cluster.p > control.getDouble("pmax")) {
      control.calculationDone("pmax reached");
    }
  }

  public void reset() {
    control.setValue("L", 101);
    control.setValue("pmin", 0.5);
    control.setAdjustableValue("pmax", 1.0);
    control.setAdjustableValue("pstep", 0.05);
    control.setAdjustableValue("number of ants", 100);
    control.setAdjustableValue("max number of steps", 500);
    control.setAdjustableValue("clusters per step", 50);

    setStepsPerDisplay(1);
    enableStepsPerDisplay(true);
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    SimulationControl.createApp(new SingleClusterApp3());
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
