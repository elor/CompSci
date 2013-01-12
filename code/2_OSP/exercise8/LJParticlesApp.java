/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package exercise8;

import org.opensourcephysics.controls.*;
import org.opensourcephysics.frames.*;

/**
 * LJParticlesApp simulates a two-dimensional system of interacting particles
 * via the Lennard-Jones potential.
 * 
 * @author Jan Tobochnik, Wolfgang Christian, Harvey Gould
 * @version 1.0 revised 03/28/05, 3/29/05
 */
public class LJParticlesApp extends AbstractSimulation {

  /**
   * Returns an XML.ObjectLoader to save and load data for this program.
   * 
   * LJParticle data can now be saved using the Save menu item in the control.
   * 
   * @return the object loader
   */
  public static XML.ObjectLoader getLoader() {
    return new LJParticlesLoader();
  }

  /**
   * Starts the Java application.
   * 
   * @param args
   *          command line parameters
   */
  public static void main(String[] args) {
    SimulationControl control = SimulationControl
        .createApp(new LJParticlesApp());
    control.addButton("resetData", "Reset Data");
    control.addButton("scale", "scale");
  }

  LJParticles md = new LJParticles();
  // HistogramFrame xVelocityHistogram = new HistogramFrame("vx", "H(vx)",
  // "Velocity histogram");
  DisplayFrame display = new DisplayFrame("x", "y", "Lennard-Jones system");

  PlotFrame ratioData = new PlotFrame("time", "PV/NT", "1/k");
  PlotFrame energyData = new PlotFrame("time", "E_tot", "total energy");
  PlotFrame pressureData = new PlotFrame("time", "PA/NkT", "pressure");
  PlotFrame temperatureData = new PlotFrame("time", "temperature",
      "temperature");
  int accpos = 0;
  Boolean accInitiating = true;
  double[] Eacc = new double[100];
  double[] Tacc = new double[100];
  double[] Pacc = new double[100];
  private double Eaccdelta;
  private double Paccdelta;
  private double Taccdelta;

  private double Eaccmean;
  private double Paccmean;
  private double Taccmean;

  /**
   * empty constructor
   */
  public LJParticlesApp() {
  }

  private void accumulate(double E, double P, double T) {
    Eacc[accpos] = E;
    Pacc[accpos] = P;
    Tacc[accpos] = T;

    ++accpos;

    if (accpos == Tacc.length) {
      accpos = 0;
      accInitiating = false;
    }

    double Emean = 0.0;
    double Pmean = 0.0;
    double Tmean = 0.0;

    for (double e : Eacc) {
      Emean += e;
    }
    for (double p : Pacc) {
      Pmean += p;
    }
    for (double t : Tacc) {
      Tmean += t;
    }

    int num = (accInitiating ? accpos : Tacc.length);

    Emean /= num;
    Pmean /= num;
    Tmean /= num;

    double weight = 0.001;
    double weight_old = 1.0 - weight;

    Eaccdelta = weight_old * Eaccdelta + weight * (Eaccmean - Emean) / Emean;
    Eaccmean = Emean;

    Paccdelta = weight_old * Paccdelta + weight * (Paccmean - Pmean) / Pmean;
    Paccmean = Pmean;

    Taccdelta = weight_old * Taccdelta + weight * (Taccmean - Tmean) / Tmean;
    Taccmean = Tmean;
  }

  /**
   * Does a simulation step and appends data to the views.
   */
  public void doStep() {
    double E, P, T;

    do {
      // md.step(xVelocityHistogram);
      md.step();
      E = md.getEnergy();
      P = md.getPressure();
      T = md.getTemperature();

      accumulate(E, P, T);

    } while (!isStatic());

    ratioData.append(0, md.t, Paccmean * md.Lx * md.Ly / (md.N * Taccmean));
    energyData.append(0, md.t, Eaccmean);
    pressureData.append(0, md.t, Paccmean);
    temperatureData.append(0, md.t, Taccmean);
  }

  /**
   * Initializes the model by reading the number of particles.
   */
  public void initialize() {
    md.nx = control.getInt("nx"); // number of particles per row
    md.ny = control.getInt("ny"); // number of particles per column
    md.initialKineticEnergy = control
        .getDouble("initial kinetic energy per particle");
    md.Lx = control.getDouble("Lx");
    md.Ly = control.getDouble("Ly");
    md.initialConfiguration = control.getString("initial configuration");
    md.dt = control.getDouble("dt");
    md.initialize();
    display.addDrawable(md);
    display.setPreferredMinMax(0, md.Lx, 0, md.Ly); // assumes vmax =
                                                    // 2*initalTemp and bin
                                                    // width = Vmax/N
                                                    // xVelocityHistogram.setBinWidth(2
                                                    // * md.initialKineticEnergy
                                                    // / md.N);

    resetAccumulated();
  }

  private boolean isStatic() {
    if (accInitiating) {
      return false;
    }

    double tolerance = 0.0005;
    return Math.abs(Paccdelta) < tolerance && Math.abs(Taccdelta) < tolerance;
  }

  /**
   * Resets the LJ model to its default state.
   */
  public void reset() {
    control.setValue("nx", 8);
    control.setValue("ny", 8);
    control.setAdjustableValue("Lx", 12.0);
    control.setAdjustableValue("Ly", 12.0);
    control.setValue("initial kinetic energy per particle", 2.0);
    control.setAdjustableValue("dt", 0.01);
    control.setValue("initial configuration", "rectangular");
    control.setAdjustableValue("scale ratio", 0.95);
    enableStepsPerDisplay(true);
    super.setStepsPerDisplay(10); // draw configurations every 10 steps
    display.setSquareAspect(true); // so particles will appear as circular disks
  }

  private void resetAccumulated() {
    accpos = 0;
    accInitiating = true;

    Eaccdelta = Eaccmean = Paccdelta = Paccmean = Taccdelta = Taccmean = 0.0;
  }

  /**
   * Resets the LJ model and the data graphs.
   * 
   * This method is invoked using a custom button.
   */
  public void resetData() {
    md.resetAverages();
//    GUIUtils.clearDrawingFrameData(false); // clears old data from the plot
                                           // frames
  }

  /**
   * scale the system to the ratio entered in the control panel
   */
  public void scale() {
    double ratio = control.getDouble("scale ratio");
    md.scale(ratio);
    accInitiating = true;
    accpos = 0;
  }

  /**
   * Reads adjustable parameters before the program starts running.
   */
  public void startRunning() {
    md.dt = control.getDouble("dt");
    double Lx = control.getDouble("Lx");
    double Ly = control.getDouble("Ly");
    if ((Lx != md.Lx) || (Ly != md.Ly)) {
      md.Lx = Lx;
      md.Ly = Ly;
      md.computeAcceleration();
      display.setPreferredMinMax(0, Lx, 0, Ly);
      resetData();
    }
  }

  /**
   * Prints the LJ model's data after the simulation has stopped.
   */
  public void stop() {
    control.println("Density = " + decimalFormat.format(md.rho));
    control.println("Number of time steps = " + md.steps);
    control.println("Time step dt = " + decimalFormat.format(md.dt));
    control.println("<T>= " + decimalFormat.format(md.getMeanTemperature()));
    control.println("<E> = " + decimalFormat.format(md.getMeanEnergy()));
    control.println("Heat capacity = "
        + decimalFormat.format(md.getHeatCapacity()));
    control.println("<PA/NkT> = " + decimalFormat.format(md.getMeanPressure()));
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
