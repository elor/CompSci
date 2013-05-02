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
public class SingleClusterApp1 extends AbstractSimulation {
  SingleCluster cluster = new SingleCluster();
  LatticeFrame latticeFrame = new LatticeFrame("Percolation cluster");
  Scalar2DFrame bild = new Scalar2DFrame("x", "y", "Zahl der Ameisen");
  double[][] antcount = null;
  PlotFrame r2Plot = new PlotFrame("t", "<R^2>", "mean squared distance");

  int steps;
  Ant ants[];

  public void initialize() {
    latticeFrame.setIndexedColor(0, Color.BLACK); // not occupied or tested
    latticeFrame.setIndexedColor(1, Color.BLUE); // occupied
    latticeFrame.setIndexedColor(2, Color.GREEN); // perimeter or growth site
    latticeFrame.setIndexedColor(-1, Color.YELLOW); // permanently not occupied
    cluster.L = control.getInt("L");
    cluster.p = control.getDouble("p");
    cluster.initialize();
    latticeFrame.setAll(cluster.site);

    while (cluster.perimeterNumber != 0) {
      cluster.step();
    }
    latticeFrame.setAll(cluster.site);

    steps = 0;

    ants = new Ant[control.getInt("number of ants")];

    for (int i = 0; i < ants.length; ++i) {
      ants[i] = new Ant(cluster);
    }

    bild.clearData();
    // bild.setZRange(false, -2, 10);

    r2Plot.clearData();

    antcount = new double[cluster.L + 2][cluster.L + 2];
  }

  public void doStep() {
    double R2 = 0.0;

    ++steps;

    for (int i = 0; i < ants.length; ++i) {
      ants[i].step();
      R2 += ants[i].getR2();
      antcount[ants[i].x][ants[i].y]++;
    }

    R2 /= ants.length;
    r2Plot.append(0, steps, R2);

    bild.setAll(antcount);

    int maxstep = control.getInt("max number of steps");
    if (steps > maxstep) {
      control.calculationDone("max step reached");
    }
  }

  public void reset() {
    control.setValue("L", 101);
    control.setValue("p", 0.7);
    control.setValue("number of ants", 100);
    control.setValue("max number of steps", 500);

    setStepsPerDisplay(10);
    enableStepsPerDisplay(true);
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    SimulationControl.createApp(new SingleClusterApp1());
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
