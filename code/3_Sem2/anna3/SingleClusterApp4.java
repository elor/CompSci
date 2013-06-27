/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package anna3;

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
public class SingleClusterApp4 extends AbstractSimulation {
	SingleCluster cluster = new SingleCluster();
	// PlotFrame plotFrame = new PlotFrame("ln r", "ln M", "Mass distribution");
	LatticeFrame latticeFrame = new LatticeFrame("Percolation cluster");
	int steps = 0;
	Ant ants[];
	PlotFrame plot2 = new PlotFrame("p", "Ds/D", "Plot2");

	public void initialize() {
		latticeFrame.setIndexedColor(0, Color.BLACK); // not occupied or tested
		latticeFrame.setIndexedColor(1, Color.BLUE); // occupied
		latticeFrame.setIndexedColor(2, Color.GREEN); // perimeter or growth
														// site
		latticeFrame.setIndexedColor(-1, Color.YELLOW); // permanently not
		plot2.clearData(); // occupied
		plot2.setPreferredMinMax(0.5, 1.1, 0, 1.1);
		cluster.p = control.getDouble("pmin");		
		cluster.L = control.getInt("L");

		steps = 0;
	}

	public void createCluster() {

		cluster.initialize();
		while (true) {
			cluster.step();
			if (cluster.perimeterNumber == 0) {
				break;
			}
		}
		latticeFrame.setAll(cluster.site);

		ants = new Ant[control.getInt("Anz. ant")];

		for (int i = 0; i < ants.length; i++) {
			ants[i] = new Ant(cluster);

		}
	}

	public void doStep() {
		
		double D=0;
		int anzCluster = control.getInt("Anz. Cluster");
		int schritte = control.getInt("t");
		for (int j = 0; j < anzCluster; j++) {
			double ds = 0;
			createCluster();

			steps++;
			for (int t = 1; t <= schritte; t++) {
				double R2 = 0;
				for (int i = 0; i < ants.length; i++) {
					ants[i].step();
					R2 = R2 + ants[i].getR();
				}
				R2 = R2 / ants.length;
				ds = ds + R2 / t;
			}
			ds = ds / schritte;
			D=D+ds;
		}
		D=D/anzCluster;
		plot2.append(0, cluster.p, D);
		cluster.p=cluster.p+control.getDouble("pstep");
		if (cluster.p>= control.getDouble("pmax"))
		{control.calculationDone("Fertig!");}
	}

	// public void stopRunning() {
	// //plotFrame.clearData();
	// cluster.massDistribution();
	// double massEnclosed = 0;
	// int rPrint = 2;
	// for (int r = 2; r < cluster.L / 2; r++) {
	// massEnclosed += cluster.mass[r];
	// if (r == rPrint) { // use logarithmic scale
	// //plotFrame.append(0, Math.log(r), Math.log(massEnclosed));
	// rPrint *= 2;
	// }
	// }
	// //plotFrame.setVisible(true);
	// }

	public void reset() {
		control.setAdjustableValue("L", 101);
		control.setAdjustableValue("t", 500);
		control.setAdjustableValue("Anz. ant", 100);
		control.setAdjustableValue("Anz. Cluster", 20);
		control.setValue("pmax", 1);
		control.setValue("pmin",0.3);
		control.setValue("pstep", 0.05);
		setStepsPerDisplay(1);
		enableStepsPerDisplay(true);
	}

	public static void main(String[] args) {
		SimulationControl.createApp(new SingleClusterApp4());
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
