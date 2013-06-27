package anna2;

//package org.opensourcephysics.sip.ch13.invasion;
import org.opensourcephysics.controls.*;
import org.opensourcephysics.frames.*;
/**
 * InvasionApp models a cluster formed by invasion percolation on a lattice.
 * 
 * @author Jan Tobochnik, Wolfgang Christian, Harvey Gould
 * @version 1.0 revised 06/21/05
 */
public class Invasion2App extends AbstractCalculation {
	 PlotFrame plotFrame = new PlotFrame("lnL", "lnM", "Plot");
	 
	public void initialize() {
			control.setValue("Ly", 10);
	}

	public void reset() {
		control.setValue("Ly", 10);
	}

	public static void main(String[] args) {
		// set up animation control structure using this class
		CalculationControl.createApp(new Invasion2App());
	}

	@Override
	public void calculate() {
		double[][] feld = new double[2][10];
		for (int i = 1; i <= 10; i++) {
			int Ly = control.getInt("Ly");
			
			Ly = Ly * i;
			int n;
			double sum = 0;

			for (int j = 0; j < 20; j++) {
				Invasion model = new Invasion(Ly);
				while (model.ok) {
					model.step();
				}
				n = model.getn();
				sum = sum + (double) n;

			}
			double M = sum / 20;
			control.println("Dichte: " + M);
			feld[1][i - 1] = Math.log(M);
			feld[0][i-1]=Math.log(Ly);
		}

	
		for (int i= 0; i<10; i++){
			plotFrame.append(0, feld[0][i], feld[1][i]);
		}

	}
}