package anna2;

//package org.opensourcephysics.sip.ch13.invasion;
import org.opensourcephysics.controls.*;

/**
 * InvasionApp models a cluster formed by invasion percolation on a lattice.
 * 
 * @author Jan Tobochnik, Wolfgang Christian, Harvey Gould
 * @version 1.0 revised 06/21/05
 */
public class Invasion3App extends AbstractCalculation {

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

