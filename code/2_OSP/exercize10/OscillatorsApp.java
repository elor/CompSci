/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package exercize10;

import org.opensourcephysics.controls.*;
import org.opensourcephysics.frames.FFTFrame;
import org.opensourcephysics.frames.PlotFrame;

/**
 * OscillatorsApp displays a system of coupled oscillators in a drawing panel.
 * 
 * The separation between oscillators is one in the current model.
 * 
 * @author Wolfgang Christian, Jan Tobochnik, Harvey Gould
 * @version 1.0
 */
public class OscillatorsApp extends AbstractSimulation {
  /**
   * Creates the oscillator program from the command line
   * 
   * @param args
   */
  public static void main(String[] args) {
    SimulationControl.createApp(new OscillatorsApp());
  }

  PlotFrame posFrame = new PlotFrame("Position", "Displacement", "Oscillators");
  FFTFrame fftFrame = new FFTFrame("omega", "Amplitude", "title");
  Oscillators oscillators;

  double dt;
  private double[] fftData;
  private double maxTime = 51.2;
  private int fftStepsLeft;

  /**
   * @param data
   * @return max value within data
   */
  public double max(double[] data) {
    double ret = data[0];
    for (int i = 1; i < data.length; ++i) {
      if (data[i] > ret) {
        ret = data[i];
      }
    }

    return ret;
  }

  /**
   * @param data
   * @return min value within data
   */
  public double min(double[] data) {
    double ret = data[0];
    for (int i = 1; i < data.length; ++i) {
      if (data[i] < ret) {
        ret = data[i];
      }
    }

    return ret;
  }

  /**
   * Does a time step
   */
  public void doStep() {
    int steps = control.getInt("steps per display");
    for (int i = 0; i < steps; ++i) {
      oscillators.step(dt); // advance the state by dt

      if (fftStepsLeft != 0) {
        fftData[fftData.length - fftStepsLeft] = oscillators.getBluePos();
        --fftStepsLeft;

        if (fftStepsLeft == 0) {
          fftFrame.doFFT(fftData, 0, oscillators.getTime());

          control.calculationDone(maxTime + "reached");
        }
      }
    }

    posFrame.setMessage("t = " + decimalFormat.format(oscillators.getTime()));
  }

  @Override
  public void start() {
    String bc = control.getString("boundary conditions");
    oscillators.setBC(bc);
  }

  /**
   * Initializes the simulation by creating a system of oscillators.
   */
  public void initialize() {
    int mode = control.getInt("mode");
    int N = control.getInt("number of particles");
    String positioning = control.getString("initial positioning");
    String bc = control.getString("boundary conditions");
    dt = control.getDouble("dt");

    oscillators = new Oscillators(positioning, mode, N, 5, bc);

    posFrame.setPreferredMinMax(0, N + 1, -1.5, 1.5);
    posFrame.clearDrawables(); // remove old oscillators
    posFrame.setSquareAspect(false);
    posFrame.addDrawable(oscillators);

    fftStepsLeft = (int) (maxTime / dt);
    fftData = new double[2 * fftStepsLeft];
    fftFrame.clearData();
    fftFrame.setPreferredMinMaxX(-2, 2);
  }

  /**
   * Resets the oscillator program to its default values.
   */
  public void reset() {
    control.setValue("number of particles", 20);
    control.setValue("initial positioning", "random");
    control.setValue("mode", 1);
    control.setValue("dt", 0.005);
    control.setAdjustableValue("boundary conditions", "fixed");
    control.setAdjustableValue("steps per display", 1000);
    initialize();
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
