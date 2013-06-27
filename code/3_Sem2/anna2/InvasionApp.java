package anna2;

//package org.opensourcephysics.sip.ch13.invasion;
import org.opensourcephysics.controls.*;

/**
 * InvasionApp models a cluster formed by invasion percolation on a lattice.
 * 
 * @author Jan Tobochnik, Wolfgang Christian, Harvey Gould
 * @version 1.0 revised 06/21/05
 */
public class InvasionApp extends AbstractCalculation {

	public void reset() {
		control.setValue("Ly", 20);
	}

	public static void main(String[] args) {
		// set up animation control structure using this class
		CalculationControl
				.createApp(new InvasionApp());
	}

	@Override
	public void calculate() {
		int Ly = control.getInt("Ly");
		int n;
		double sum = 0;


		for (int j = 0; j < 20; j++) {
			Invasion model = new Invasion(Ly);
			while (model.ok) {
				model.step();
			}
			n = model.getn();
			 sum = sum + (double) n / ((double) Ly * Ly);
			

		}
		double M = sum / 20;
		control.println("Dichte: " + M);
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
