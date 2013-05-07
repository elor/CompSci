/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package ex04;

import org.opensourcephysics.frames.*;
import org.opensourcephysics.controls.*;
import java.awt.Color;
import java.util.BitSet;

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
  BitSet state;
  int size = 16;
  private BitSet stayAlive;
  private BitSet birth;

  /**
   * Constructs the LifeApp.
   */
  public LifeApp() {
    latticeFrame.setToggleOnClick(true, 0, 1);
    latticeFrame.setIndexedColor(0, Color.RED);
    latticeFrame.setIndexedColor(1, Color.BLUE);
  }

  /**
   * Initialzie the game of life.
   * 
   * @param size
   */
  public void initCells(int size) {
    this.size = size;
    state = new BitSet(size * size);
    stayAlive = new BitSet(size * size);
    birth = new BitSet(size * size);
  }

  /**
   * Clears all cells.
   */
  public void clear() {
    latticeFrame.setAll(new byte[size][size]);
    latticeFrame.repaint();
  }

  /**
   * Sets the default parameters in the control.
   */
  public void reset() {
    control.println("Click in drawingPanel to toggle life.");
    control.setValue("grid size", 16);
    control.setValue("steps per display", 1);
    initCells(16);
  }

  /**
   * Set the default parameters in the control.
   */
  public void initialize() {
    initCells(control.getInt("grid size"));
  }

  private int calcNeighborsPeriodic(int i) {
    return calcNeighborsPeriodic(getRow(i), getCol(i));
  }

  /**
   * Calculate the number of neighbors in a periodic lattice.
   */
  private int calcNeighborsPeriodic(int row, int col) {
    int neighbors = (getCell(row, col) ? -1 : 0); // do not count self

    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        if (getCell(row + i, col + j)) {
          ++neighbors;
        }
      }
    }

    return neighbors;
  }

  private boolean getCell(int row, int col) {
    return getCell(getIndex(row, col));
  }

  private int getIndex(int row, int col) {
    return ((row + size) % size) * size + (col + size) % size;
  }

  private int getRow(int i) {
    return (i / size + size) % size;
  }

  private int getCol(int i) {
    return (i + size) % size;
  }

  private boolean getCell(int i) {
    return state.get(i);
  }

  /**
   * Step the Lattice by one generation.
   */
  public void doStep() {
    int steps = control.getInt("steps per display");
    for (int j = 0; j < steps; ++j) {

      stayAlive.clear();
      birth.clear();
      for (int i = 0; i < state.length(); ++i) {
        switch (calcNeighborsPeriodic(i)) {
        case 3:
          birth.set(i);
        case 2:
          stayAlive.set(i);
          break;
        default:
          break;
        }
      }

      // apply
      // first: stay alive
      state.and(stayAlive);
      // second: birth
      state.or(birth);
    }

    for (int i = 0; i < state.length(); ++i) {
      latticeFrame.setAtIndex(i, state.get(i) ? 1 : 0);
    }
  }

  /* --------------- application target --------------- */
  public static void main(String[] args) {
    OSPControl control = SimulationControl.createApp(new LifeApp());
    control.addButton("clear", "Clear"); // optional custom action
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
