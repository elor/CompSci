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

  static DiskolationApp disko = null;

  /**
   * main
   * 
   * @param args
   */
  public static void main(String args[]) {
    CalculationControl sim = null;
    disko = new DiskolationApp();
    sim = CalculationControl.createApp(disko);
    sim.addButton("paintitblack", "paint it black");
    sim.addButton("colorify", "colorify");
  }

  /**
   * 
   */
  public static void colorify() {
    disko.colorAll();
  }

  /**
   * 
   */
  public static void paintitblack() {
    disko.clearColors();
  }

  List<Color> colors = new ArrayList<Color>();
  PlotFrame diskPlot = new PlotFrame("x", "y", "Perkolation");
  List<Disk> disks = new ArrayList<Disk>();
  double L;
  int nextColor;

  Random random = new Random();

  /**
   * Creates the PercolationApp and sets the colors for lattice.
   */
  public DiskolationApp() {
    diskPlot.setInteractiveMouseHandler(this);
    colors.add(Color.RED);
    colors.add(Color.BLUE);
    colors.add(Color.GREEN);
    colors.add(Color.MAGENTA);
    colors.add(Color.CYAN);
    colors.add(Color.GRAY);
    colors.add(Color.ORANGE);
    colors.add(Color.PINK);
    colors.add(Color.YELLOW);
  }

  double approxCoverage(int iterations) {
    int hit = 0;
    Random rand = new Random();

    for (int i = 0; i < iterations; ++i) {
      double x = rand.nextDouble() * L;
      double y = rand.nextDouble() * L;

      for (Disk d : disks) {
        if (d.isInside(x, y)) {
          ++hit;
          break;
        }
      }
    }

    return (double) hit / (double) iterations;
  }

  /**
   * Create some disks
   */
  public void calculate() {
    L = control.getDouble("Lattice size");

    diskPlot.setPreferredMinMax(-0.5, L + 0.5, -0.5, L + 0.5);

    int numdisks = control.getInt("number of disks");
    String type = control.getString("placement type");

    double radius = control.getDouble("radius");

    if (Disk.getR() <= radius) {
      clearClusters();
    }

    Disk.setR(radius);

    if (numdisks > 0) {
      if (!(type.equals("grid") || type.equals("random") || type
          .equals("nooverlap"))) {
        return;
      }

      // occupy lattice sites with probability p
      for (int i = 0; i < numdisks; i++) {
        double x = -1;
        double y = -1;
        Boolean valid = false;

        int failsleft = 100000;
        while (!valid) {
          valid = true;
          if (type.equals("grid")) {
            x = random.nextInt((int) L + 1);
            y = random.nextInt((int) L + 1);

            for (Disk d : disks) {
              if (d.isInside(x, y)) {
                valid = false;
                break;
              }
            }
          } else if (type.equals("random")) {
            x = random.nextDouble() * L;
            y = random.nextDouble() * L;
          } else if (type.equals("nooverlap")) {
            x = random.nextDouble() * L;
            y = random.nextDouble() * L;
            for (Disk d : disks) {
              if (d.overlap(x, y)) {
                valid = false;
                break;
              }
            }
          }

          if (!valid) {
            --failsleft;
            if (failsleft <= 0) {
              break;
            }
          }

        }

        if (!valid) {
          // there's no valid configuration for a new disk
          control.println("No valid space for a new disk left");
          break;
        }

        Disk disk = new Disk(x, y);
        disks.add(disk);
        diskPlot.addDrawable(disk);
      }
    }

    extendClusters();

    diskPlot.repaint(); // display lattice with colored cluster

    printInfo();
  }

  /**
   * clear all cluster values
   */
  void clearClusters() {
    for (Disk d : this.disks) {
      d.setRoot(null);
    }
  }

  /**
   * calculate and apply clusters
   */
  void extendClusters() {
    for (int i = 1; i < disks.size(); ++i) {
      Disk disk = disks.get(i);
      Disk root = disk.getRoot();
      for (int j = 0; j < i; ++j) {
        Disk d = disks.get(j);

        if (disk.overlap(d)) {
          if (root != disk) {
            // compare sizes
            if (root.size() < d.getRoot().size()) {
              root.setRoot(d.getRoot());
            } else {
              d.getRoot().setRoot(root);
            }
          } else {
            disk.setRoot(d.getRoot());
          }

          root = disk.getRoot();
        }
      }
    }
  }

  double getDensity() {
    double r = Disk.getR();
    int n = disks.size();

    // calculate accumulated area of all disks divided by total area
    return Math.PI * r * r * n / (L * L);
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

    if (panel.getMouseAction() == InteractivePanel.MOUSE_CLICKED) {
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
        mouseDisk.setColor(colors.get(nextColor)); // color cluster to which
                                                   // site belongs
        if (++nextColor == colors.size()) {
          nextColor = 0;
        }

        diskPlot.repaint(); // display lattice with colored cluster
      }
    }
  }

  void clearColors() {
    for (Disk d : disks) {
      if (d.isRoot()) {
        d.setColor(Color.BLACK);
      }
    }
    diskPlot.repaint();
  }

  void colorAll() {
    for (Disk d : disks) {
      if (d.isRoot() && d.getColor() == Color.BLACK) {
        d.setColor(colors.get(nextColor));
        if (++nextColor == colors.size()) {
          nextColor = 0;
        }
      }
    }

    diskPlot.repaint();
  }

  void printInfo() {
    double coverage = approxCoverage(100000);
    double density = getDensity();
    int num = disks.size();

    control.println("Disks: " + num + ", Coverage: " + coverage + ", Density: "
        + density);
  }

  public void reset() {
    control.setValue("placement type", "random");
    control.setValue("Lattice size", 10.0);
    control.setValue("number of disks", 50);
    control.setValue("radius", 0.5);

    // reset disks
    disks.clear();
    diskPlot.clearDrawables();

    nextColor = 0;
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
