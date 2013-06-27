package anna4;

import org.opensourcephysics.frames.*;
import org.opensourcephysics.controls.*;
import java.awt.Color;

/**
 * LifeApp implements the "Game of Life" invented by John Conway and popularized
 * by Martin Gardner in his Mathemtatical Recreations column in Scientific
 * American. (October 1970)
 * 
 * @author Wolfgang Christian, Jan Tobochnik, Harvey Gould
 * @version 1.0 revised 06/24/05
 */
public class LifeApp extends AbstractSimulation {
	LatticeFrame latticeFrame = new LatticeFrame("Game of Life");
	PlotFrame dichtePlot = new PlotFrame("rho(t+1)", "rho(t)", "Dichte");
	byte[][] newCells;
	int size = 32;
	int anz = 0;
	int anz2 = 0;

	/**
	 * Constructs the LifeApp.
	 */
	public LifeApp() {
		latticeFrame.setToggleOnClick(true, 0, 1);
		latticeFrame.setIndexedColor(0, Color.RED);
		latticeFrame.setIndexedColor(1, Color.BLUE);
		dichtePlot.setPreferredMinMax(0, 0.4, 0, 0.4);
	}

	/**
	 * Initialize the game of life.
	 */
	public void initCells(int size) {
		this.size = size;
		newCells = new byte[size][size];
		latticeFrame.setAll(newCells, 0, size, 0, size);
		double ws = control.getDouble("Besetzungswahrscheinlichkeit");
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				double a = Math.random();
				if (a <= ws) {
					latticeFrame.setValue(i, j, 1);
					anz++;
				}
			}
		}

	}

	/**
	 * Clears all cells.
	 */
	public void clear() {
		latticeFrame.setAll(new byte[size][size]);
		latticeFrame.repaint();
		dichtePlot.clearData();
	}

	/**
	 * Sets the default parameters in the control.
	 */
	public void reset() {
		control.println("Click in drawingPanel to toggle life.");
		control.setValue("grid size", 32);
		control.setValue("Besetzungswahrscheinlichkeit", 0.5);
		initCells(32);
	}

	/**
	 * Set the default parameters in the control.
	 */
	public void initialize() {
		initCells(control.getInt("grid size"));
	}

	/**
	 * Calculate the number of neighbors in a peridodic lattice.
	 */
	private int calcNeighborsPeriodic(int row, int col) {
		int neighbors = -latticeFrame.getValue(row, col); // do not count self
		row += size; // add the size so that the mod operator works for row=0
						// and col=0
		col += size;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				neighbors += latticeFrame.getValue((row + i) % size, (col + j)
						% size);
			}
		}
		return neighbors;
	}

	/**
	 * Step the Lattice by one generation.
	 */
	public void doStep() {
		anz = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				newCells[i][j] = 0;
			}
		}
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				switch (calcNeighborsPeriodic(i, j)) {
				case 0:
				case 1:
					newCells[i][j] = 0; // dies
					break;
				case 2:
					if ((newCells[i][j] = (byte) latticeFrame.getValue(i, j)) == 1) {
						anz++;
					}

					break;
				case 3:
					newCells[i][j] = 1; // condition for birth
					anz++;
					break;
				default:
					newCells[i][j] = 0; // dies of overcrowding if >3

				}
			}
		}
		latticeFrame.setAll(newCells);
		
		dichtePlot.append(0, (double) (anz2/(double)(size*size)), (double) (anz/(double)(size*size)));
		anz2 = anz;
	}

	/* --------------- application target --------------- */
	public static void main(String[] args) {
		OSPControl control = SimulationControl.createApp(new LifeApp());
		control.addButton("clear", "Clear"); // optional custom action
	}
}