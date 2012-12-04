/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package functiondraw;

import java.awt.Color;

import org.opensourcephysics.controls.*;
import org.opensourcephysics.display.FunctionDrawer;
import org.opensourcephysics.frames.*;
import org.opensourcephysics.numerics.Function;
import org.opensourcephysics.numerics.ParsedFunction;

/**
 * Simulates random walkers in one dimension
 * 
 * @author Jan Tobochnik, Wolfgang Christian, Harvey Gould
 * @version 1.0 revised 04/21/05
 */
public class FunctionApp extends AbstractCalculation {
  PlotFrame plotFrame = new PlotFrame("x", "y", "function");

  /**
   * Sets column names for data table
   */
  public FunctionApp() {
  }

  public void reset() {
    control.setValue("f(t)", "sin(pi*t/10)");
    control.setValue("a", 0.1);
    control.setValue("b", -0.5);
    control.setValue("c", 1);
    control.setValue("delta", 0.1);
    control.setValue("N", 200);
  }

  /**
   * Starts the Java application.
   * 
   * @param args
   *          command line parameters
   */
  public static void main(String[] args) {
    CalculationControl.createApp(new FunctionApp());
  }

  @Override
  public void calculate() {
    plotFrame.clearDrawables();

    String fstr = control.getString("f(t)");
    double a = control.getDouble("a");
    double b = control.getDouble("b");
    double c = control.getDouble("c");

    int N = control.getInt("N");

    
    try {
      Function f2 = new ParsedFunction(fstr, "t");
      FunctionDrawer fd2 = new FunctionDrawer(f2);
      fd2.initialize(-10, 10, N, true);
      fd2.setColor(Color.blue);
      plotFrame.addDrawable(fd2);
    } catch (Exception e) {
      control.println("function '" + fstr + "' couldn't be parsed: " + e);
    }
    
    
    Function f1 = new QuadraticPolynomial(a, b, c);
    FunctionDrawer fd1 = new FunctionDrawer(f1);
    fd1.initialize(-10, 10, N, false);
    plotFrame.addDrawable(fd1);
    
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
