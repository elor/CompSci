/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package anna1;

import org.opensourcephysics.controls.*;
import org.opensourcephysics.frames.*;

/**
 * FallingParticlePlotApp computes the time for a particle to fall to the ground
 * and plots the position as a funciton of time.
 * 
 * @author Wolfgang Christian, Jan Tobochnik, Harvey Gould
 * @version 1.0 05/07/05
 */
public class Uebung1App extends AbstractCalculation {
	PlotFrame plotFrame = new PlotFrame("t", "y", "Falling Ball");
	Kreis array[];
	double L = 1;
	int anz = 1;

	/**
	 * Calculates the trajectory of a falling particle and plots the position as
	 * a function of time.
	 */
	public void calculate() {
		plotFrame.setAutoclear(false); // data not cleared at beginning of each
										// calculation
		// gets initial conditions
		L = control.getDouble("L");
		plotFrame.setPreferredMinMax(0, L, 0, L);
		double Rho = control.getDouble("Rho");
		// sets initial conditions
		anz = (int) Math.round(L * L * Rho);
		array = new Kreis[anz];
		for (int i = 0; i < anz; i++) {
			double x = Math.random() * L;
			double y = Math.random() * L;
			array[i] = new Kreis(x, y, 10);
			plotFrame.addDrawable(array[i]);

		}
		control.println("Belegte Fläche: " + dichte());
	}

	public double dichte() {
		double a = 1;
		int n = 100000;
		int b = 0;
		for (int j = 1; j <= n; j++) {
			double u = Math.random() * L;
			double v = Math.random() * L;
			for (int i = 0; i < anz; i++) {
				double dx = Math.abs(u - array[i].getX());
				double dy = Math.abs(v - array[i].getY());
				double c = dx * dx + dy * dy;
				if (c < 0.25) {
					b = b + 1;
					break;
				}
			}
		}
		a = (double) b / (double) n;
		return (a);
	}

	/**
	 * Resets the program to its initial state.
	 */
	public void reset() {
		control.setValue("L", 30);
		control.setValue("Rho", 1.34);
	}

	/**
	 * Starts the Java application.
	 * 
	 * @param args
	 *            command line parameters
	 */
	public static void main(String[] args) { // sets up calculation control
												// structure using this class
		CalculationControl.createApp(new Uebung1App());
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
