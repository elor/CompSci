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
public class LJParticlesAppTvsE extends AbstractSimulation {

  public static class Accumulator {
    int accpos;
    Boolean accInitiating;
    double[] Eacc;
    double[] Tacc;
    double[] Pacc;
    double Eaccdelta;
    double Paccdelta;
    double Taccdelta;
    double Eaccmean;

    public double E() {
      return Eaccmean;
    }

    public double P() {
      return Paccmean;
    }

    public double T() {
      return Taccmean;
    }

    void accumulate(double E, double P, double T) {
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

    boolean isStatic() {
      if (accInitiating) {
        return false;
      }
    
      double tolerance = 0.0005;
      return Math.abs(Paccdelta) < tolerance
          && Math.abs(Taccdelta) < tolerance;
    }

    void reset() {
      accpos = 0;
      accInitiating = true;
    
      Eaccdelta = Eaccmean = Paccdelta = Paccmean = Taccdelta = Taccmean = 0.0;
    }

    public double Paccmean;
    public double Taccmean;

    public Accumulator(int accpos, Boolean accInitiating, double[] eacc,
        double[] tacc, double[] pacc) {
      this.accpos = accpos;
      this.accInitiating = accInitiating;
      Eacc = eacc;
      Tacc = tacc;
      Pacc = pacc;
    }
  }

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
    // SimulationControl control = SimulationControl
    SimulationControl.createApp(new LJParticlesAppTvsE());
  }

  LJParticles md = new LJParticles();
  // HistogramFrame xVelocityHistogram = new HistogramFrame("vx", "H(vx)",
  // "Velocity histogram");
  DisplayFrame display = new DisplayFrame("x", "y", "Lennard-Jones system");

  Accumulator acc = new Accumulator(0, true, new double[100], new double[100],
      new double[100]);
  private double Ttarget;
  private PlotFrame TvsE = new PlotFrame("E", "T", "T(E)");
  private PlotFrame CvPlot = new PlotFrame("t", "Cv", "Cv(t)");

  public LJParticlesAppTvsE() {
  }

  /**
   * Does a simulation step and appends data to the views.
   */
  public void doStep() {
    /*
     * TODO modify to calculate T(E)
     */

    double Tmax = control.getDouble("Tmax");
    double Tmin = control.getDouble("Tmin");
    int steps = control.getInt("steps");
    if (steps < 1) {
      steps = 1;
    }
    double Tstep = (Tmax - Tmin) / steps;

    // if Tmax is exceeded, stop the simulation
    if (Ttarget > Tmax) {
      control.calculationDone("target temperature reached");
    }

    // relax 50 time units
    double stoptime = md.t + 50;
    double rescaletime = md.t + 2.0;
    while (md.t < stoptime) {
      md.step();
      // rescale temperature every 2 time units
      if (md.t > rescaletime) {
        md.tempScale(Ttarget);
        rescaletime += 2.0;
      }
    }

    // relax until equilibrium is reached
    acc.reset();
    do {
      md.step();
      acc.accumulate(md.getEnergy(), md.getPressure(), md.getTemperature());

      // rescale temperature every 2 time units
      if (md.t > rescaletime) {
        md.tempScale(Ttarget);
        rescaletime += 2.0;
      }
    } while (!acc.isStatic());

    // average total energy and temperature over 10 time units
    stoptime = md.t + 10;
    double Eacc = 0.0;
    double Tacc = 0.0;
    int count = 0;
    while (md.t < stoptime) {
      md.step();

      Eacc += md.getEnergy();
      Tacc += md.getTemperature();
      ++count;
    }

    // append T(E) to plot frame

    TvsE.append(0, Eacc / count, Tacc / count);
    CvPlot.append(0, md.t, md.getHeatCapacity());

    // increase target temperature by (Tmax - Tmin) / Tsteps end step
    Ttarget += Tstep;

  }

  /**
   * Initializes the model by reading the number of particles.
   */
  public void initialize() {
    md.nx = control.getInt("nx"); // number of particles per row
    md.ny = control.getInt("ny"); // number of particles per column
    md.initialKineticEnergy = 2.0;
    md.Lx = control.getDouble("Lx");
    md.Ly = control.getDouble("Ly");
    md.initialConfiguration = "rectangular";
    md.dt = control.getDouble("dt");
    md.initialize();
    display.addDrawable(md);
    display.setPreferredMinMax(0, md.Lx, 0, md.Ly); // assumes vmax =
                                                    // 2*initalTemp and bin
                                                    // width = Vmax/N
                                                    // xVelocityHistogram.setBinWidth(2
                                                    // * md.initialKineticEnergy
                                                    // / md.N);

    acc.reset();
  }

  /**
   * Resets the LJ model to its default state.
   */
  public void reset() {
    control.setValue("nx", 8);
    control.setValue("ny", 8);
    control.setValue("Lx", 12.0);
    control.setValue("Ly", 12.0);
    control.setAdjustableValue("dt", 0.01);
    control.setAdjustableValue("Tmin", 1.0);
    control.setAdjustableValue("Tmax", 1.2);
    control.setAdjustableValue("steps", 100);
    display.setSquareAspect(true); // so particles will appear as circular disks
  }

  /**
   * Resets the LJ model and the data graphs.
   * 
   * This method is invoked using a custom button.
   */
  public void resetData() {
    md.resetAverages();
  }

  /**
   * Reads adjustable parameters before the program starts running.
   */
  public void startRunning() {
    md.dt = control.getDouble("dt");
    double Lx = control.getDouble("Lx");
    double Ly = control.getDouble("Ly");
    Ttarget = control.getDouble("Tmin");
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
