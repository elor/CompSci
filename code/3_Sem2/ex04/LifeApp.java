/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package ex04;

import org.opensourcephysics.frames.*;
import org.opensourcephysics.controls.*;
import org.opensourcephysics.display.InteractiveMouseHandler;
import org.opensourcephysics.display.InteractivePanel;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.BitSet;
import java.util.Random;

/**
 * LifeApp implements the "Game of Life" invented by John Conway and popularized
 * by Martin Gardner in his Mathemtatical Recreations column in Scientific
 * American. (October 1970)
 * 
 * @author Wolfgang Christian, Jan Tobochnik, Harvey Gould
 * @version 1.0 revised 06/24/05
 */
public class LifeApp extends AbstractSimulation implements
    InteractiveMouseHandler {
  /* --------------- application target --------------- */
  /**
   * @param args
   */
  public static void main(String[] args) {
    OSPControl control = SimulationControl.createApp(new LifeApp());
    control.addButton("clear", "Clear"); // optional custom action
    control.addButton("randomize", "Randomize"); // optional custom action
  }

  private BitSet changed;
  private BitSet curBirth;
  private BitSet curKill;
  LatticeFrame latticeFrame = new LatticeFrame("Game of Life");
  private BitSet nextBirth;
  private BitSet nextKill;
  byte[] numNeighbors;
  Random rand = new Random();

  int size = 16;

  BitSet state;

  /**
   * Constructs the LifeApp.
   */
  public LifeApp() {
    latticeFrame.setInteractiveMouseHandler(this);
    latticeFrame.setIndexedColor(0, Color.WHITE);
    latticeFrame.setIndexedColor(1, Color.BLACK);
  }

  // private int calcNeighborsPeriodic(int i) {
  // return calcNeighborsPeriodic(getRow(i), getCol(i));
  // }
  //
  // /**
  // * Calculate the number of neighbors in a periodic lattice.
  // */
  // private int calcNeighborsPeriodic(int row, int col) {
  // int neighbors = (getCell(row, col) ? -1 : 0); // do not count self
  //
  // for (int i = -1; i <= 1; i++) {
  // for (int j = -1; j <= 1; j++) {
  // if (getCell(row + i, col + j)) {
  // ++neighbors;
  // }
  // }
  // }
  //
  // return neighbors;
  // }

  /**
   * @param i
   * @param j
   */
  void applyNeighborhoodChanges(int i) {
    switch (numNeighbors[i]) {
    case 2:
      // stay alive
      nextBirth.clear(i);
      nextKill.clear(i);
      break;
    case 3:
      // give birth
      nextBirth.set(i);
      nextKill.clear(i);
      break;
    default:
      // die
      nextBirth.clear(i);
      nextKill.set(i);
      break;
    }
  }

  /**
   * Clears all cells.
   */
  public void clear() {
    latticeFrame.setAll(new byte[size][size]);
    state.clear();
    curKill.clear();
    curBirth.clear();
    nextKill.clear();
    nextBirth.clear();
    numNeighbors = new byte[size * size];
    latticeFrame.repaint();
  }

  /**
   * @param i
   */
  void clearCell(int i) {
    if (state.get(i)) {
      state.clear(i);
      changed.flip(i);
      reduceNeighbors(i);
    }
  }

  void clearCell(int row, int col) {
    clearCell(getIndex(row, col));
  }

  /**
   * Step the Lattice by one generation.
   */
  public void doStep() {
    int steps = control.getInt("steps per display");
    // swap
    BitSet tmpSet;

    changed.clear();

    for (int j = 0; j < steps; ++j) {
      tmpSet = curBirth;
      curBirth = nextBirth;
      nextBirth = tmpSet;
      nextBirth.clear();

      tmpSet = curKill;
      curKill = nextKill;
      nextKill = tmpSet;
      nextKill.clear();

      // stayAlive.clear();
      // birth.clear();
      // for (int i = 0; i < size * size; ++i) {
      // switch (calcNeighborsPeriodic(i)) {
      // case 3:
      // birth.set(i);
      // // control.println("birth: " + i);
      // break;
      // case 2:
      // stayAlive.set(i);
      // // control.println("alive: " + i);
      // break;
      // default:
      // break;
      // }
      // }

      // // apply
      // // first: stay alive
      // state.and(stayAlive);
      // // second: birth
      // state.or(birth);
      // updateLattice();

      int i = curKill.nextSetBit(0);
      while (i != -1) {
        clearCell(i);
        try {
          i = curKill.nextSetBit(i + 1);
        } catch (Exception e) {
          i = -1;
        }
      }

      i = curBirth.nextSetBit(0);
      while (i != -1) {
        setCell(i);
        try {
          i = curBirth.nextSetBit(i + 1);
        } catch (Exception e) {
          i = -1;
        }
      }
    }

    updateLattice();
  }

  void flipCell(int i) {
    if (state.get(i)) {
      state.clear(i);
      changed.flip(i);
      reduceNeighbors(i);
    } else {
      state.set(i);
      changed.flip(i);
      increaseNeighbors(i);
    }
  }

  void flipCell(int row, int col) {
    flipCell(getIndex(row, col));
  }

  private boolean getCell(int i) {
    return state.get(i);
  }

  @SuppressWarnings("unused")
  private boolean getCell(int row, int col) {
    return getCell(getIndex(row, col));
  }

  private int getCol(int i) {
    return (i + size) % size;
  }

  private int getIndex(int row, int col) {
    return ((row + size) % size) * size + ((col + size) % size);
  }

  private int getRow(int i) {
    return (i / size + size) % size;
  }

  @Override
  public void handleMouseAction(InteractivePanel panel, MouseEvent evt) {
    panel.handleMouseAction(panel, evt);

    if (panel.getMouseAction() == InteractivePanel.MOUSE_CLICKED) {
      int x = (int) panel.getMouseX();
      int y = (int) panel.getMouseY();

      flipCell(y, x);
    }
  }

  private void increaseNeighbors(int i) {
    int row = getRow(i);
    int col = getCol(i);
    int j;
    for (int x = -1; x <= 1; ++x) {
      for (int y = -1; y <= 1; ++y) {
        if (x != 0 || y != 0) {
          j = getIndex(row + y, col + x);
          ++numNeighbors[j];
          applyNeighborhoodChanges(j);
        }
      }
    }
  }

  /**
   * Initialize the game of life.
   * 
   * @param size
   */
  public void initCells(int size) {
    this.size = size;
    state = new BitSet(size * size);
    curKill = new BitSet(size * size);
    curBirth = new BitSet(size * size);
    nextKill = new BitSet(size * size);
    nextBirth = new BitSet(size * size);
    numNeighbors = new byte[size * size];
    changed = new BitSet(size * size);

    clear();
  }

  /**
   * Set the default parameters in the control.
   */
  public void initialize() {
    initCells(control.getInt("grid size"));

    setCell(7, 7);
    setCell(8, 7);
    setCell(9, 7);
    setCell(9, 8);
    setCell(8, 9);

    updateLattice();
  }

  /**
   * 
   */
  public void randomize() {
    clear();

    double p = control.getDouble("p");
    for (int i = 0; i < size * size; ++i) {
      if (rand.nextDouble() < p) {
        setCell(i);
      }
    }

    latticeFrame.repaint();
  }

  private void reduceNeighbors(int i) {
    int row = getRow(i);
    int col = getCol(i);
    int j;
    for (int x = -1; x <= 1; ++x) {
      for (int y = -1; y <= 1; ++y) {
        if (x != 0 || y != 0) {
          j = getIndex(row + y, col + x);
          --numNeighbors[j];
          applyNeighborhoodChanges(j);
        }
      }
    }
  }

  /**
   * Sets the default parameters in the control.
   */
  public void reset() {
    control.println("Click in drawingPanel to toggle life.");
    control.setValue("grid size", 16);
    control.setAdjustableValue("steps per display", 1);
    control.setAdjustableValue("p", 0.5);
    initCells(16);
  }

  /**
   * @param i
   */
  void setCell(int i) {
    if (!state.get(i)) {
      state.set(i);
      changed.flip(i);
      increaseNeighbors(i);
    }
  }

  void setCell(int row, int col) {
    setCell(getIndex(row, col));
  }

  /**
   * 
   */
  void updateLattice() {
    int i = changed.nextSetBit(0);
    while (i != -1) {
      latticeFrame.setAtIndex(i, state.get(i) ? 1 : 0);
//      try {
        i = changed.nextSetBit(i + 1);
//      } catch (Exception e) {
//        i = -1;
//      }
    }

    latticeFrame.repaint();
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
