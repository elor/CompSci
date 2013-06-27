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
public class SingleClusterApp5 extends AbstractSimulation {
	SingleCluster cluster = new SingleCluster();
	//PlotFrame plotFrame = new PlotFrame("ln r", "ln M", "Mass distribution");
	LatticeFrame latticeFrame = new LatticeFrame("Percolation cluster");
	Scalar2DFrame bild =new Scalar2DFrame("x","y", "Zahl der Ameisen");
	int steps;
	Ant2 ants[];
	PlotFrame plot = new PlotFrame("t","R2","Abstand");

	public void initialize() {
		latticeFrame.setIndexedColor(0, Color.BLACK); // not occupied or tested
		latticeFrame.setIndexedColor(1, Color.BLUE); // occupied
		latticeFrame.setIndexedColor(2, Color.GREEN); // perimeter or growth
														// site
		latticeFrame.setIndexedColor(-1, Color.YELLOW); // permanently not
														// occupied
		cluster.L = control.getInt("L");
		cluster.p = control.getDouble("p");

		cluster.initialize();
		latticeFrame.setAll(cluster.site);
		while (true) {
			cluster.step();
			if (cluster.perimeterNumber == 0) {
				break;
			}
		}
		latticeFrame.setAll(cluster.site);
		
		steps=0;
		bild.clearData();
		bild.setZRange(false, 0, 100);
		ants = new Ant2[control.getInt("Anz. ant")];
		
		for (int i=0; i< ants.length; i++)
		{ants[i]= new Ant2(cluster);
			
		}
		
		plot.clearData();
	}

	public void doStep() {
		double R2=0;
		steps++;
		double [][] antcount =new double[cluster.L+2][cluster.L+2];
		for (int i=0; i< ants.length; i++)
		{ants[i].step();
		antcount[ants[i].x][ants[i].y]++;
		R2=R2+ants[i].getR();
		}
		R2=R2/ants.length;
		bild.setAll(antcount);
		plot.append(0, steps, R2);
	
		if(steps> control.getInt("t"))
		{control.calculationDone("Fertig!");}
	}

//	public void stopRunning() {
//		//plotFrame.clearData();
//		cluster.massDistribution();
//		double massEnclosed = 0;
//		int rPrint = 2;
//		for (int r = 2; r < cluster.L / 2; r++) {
//			massEnclosed += cluster.mass[r];
//			if (r == rPrint) { // use logarithmic scale
//				//plotFrame.append(0, Math.log(r), Math.log(massEnclosed));
//				rPrint *= 2;
//			}
//		}
//		//plotFrame.setVisible(true);
//	}

	public void reset() {
		control.setValue("L", 101);
		control.setValue("p", 0.7);
		control.setValue("t", 500);
		control.setValue("Anz. ant",100);
		setStepsPerDisplay(1);
		enableStepsPerDisplay(true);
	}

	public static void main(String[] args) {
		SimulationControl.createApp(new SingleClusterApp5());
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
