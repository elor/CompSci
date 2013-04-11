/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package ex01;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.opensourcephysics.display.*;
import org.opensourcephysics.controls.*;
import org.opensourcephysics.frames.PlotFrame;

/**
 * PercolationApp displays site percolation clusters.
 * 
 * Click on a cluster to select and change its color.
 * 
 * @author Jan Tobochnik, Wolfgang Christian, Harvey Gould, Kipton Barros
 * @version 1.1 revised 05/18/05
 */
public class DiskolationApp extends AbstractCalculation implements
    InteractiveMouseHandler {
  PlotFrame diskPlot = new PlotFrame("Percolation", "x", "y");
  List<Disk> disks = new ArrayList<Disk>();
  Random random = new Random();
  double L;

  /**
   * Creates the PercolationApp and sets the colors for lattice.
   */
  public DiskolationApp() {
  }

  /**
   * @param panel
   *          InteractivePanel
   * @param evt
   *          MouseEvent
   */
  // uses mouse click events to identify an occupied site and identify its
  // cluster
  public void handleMouseAction(InteractivePanel panel, MouseEvent evt) {
    panel.handleMouseAction(panel, evt);
    if (panel.getMouseAction() == InteractivePanel.MOUSE_PRESSED) {
      Disk mouseDisk = null;

      double x = panel.getMouseX();
      double y = panel.getMouseY();
      for (Disk disk : this.disks) {
        if (disk.isInside(x, y)) {
          mouseDisk = disk;
          break;
        }
      }

      // test if a valid site was clicked (index non-negative),
      // and if site is occupied, but not yet cluster colored (value -1).
      if (mouseDisk != null) {
        mouseDisk.setColor(Color.red); // color cluster to which site belongs
        diskPlot.repaint(); // display lattice with colored cluster
      }
    }
  }

  /**
   * Create some disks
   */
  public void calculate() {
    L = control.getDouble("Lattice size");
    int numdisks = control.getInt("number of disks");
    String type = control.getString("placement type");

    if (type != "grid" && type != "random" && type != "nooverlap") {
      return;
    }

    // reset disks
    disks.clear();
    diskPlot.clearDrawables();

    // occupy lattice sites with probability p
    for (int i = 0; i < numdisks; i++) {
      Disk disk = null;

      if (type == "grid") {
        disk = new Disk(random.nextInt((int) L), random.nextInt((int) L));
      } else if (type == "random") {
        disk = new Disk(random.nextDouble() * L, random.nextDouble() * L);
      } else if (type == "nooverlap") {
        // TODO implement
      }

      if (disk != null) {
        disks.add(disk);
        diskPlot.addDrawable(disk);
      } else {
      }
    }

    diskPlot.repaint(); // display lattice with colored cluster
  }

  public void reset() {
    control.setValue("placement type", "grid");
    control.setValue("Lattice size", 10.0);
    control.setValue("number of disks", 10);
    calculate();
  }

  /**
   * main func
   * 
   * @param args
   */
  public static void main(String args[]) {
    CalculationControl.createApp(new DiskolationApp());
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
