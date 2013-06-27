
package anna7;
import org.opensourcephysics.controls.*;
import org.opensourcephysics.display.*;
import org.opensourcephysics.frames.*;

/**
 * SchroedingerApp displays a solution to the time-independent Schroedinger equation.
 *
 * @author Wolfgang Christian, Jan Tobochnik, Harvey Gould
 * @version 1.0  revised 03/23/05
 */
public class SchroedingerApp1 extends AbstractCalculation {
  PlotFrame frame = new PlotFrame("x", "phi", "Wave function");
  SchroedingerStep schroedinger = new SchroedingerStep();

  /**
   * Constructs SchroedingerApp and sets plotting frame parameters.
   */
  public SchroedingerApp1() {
    frame.setConnected(0, true);
    frame.setMarkerShape(0, Dataset.NO_MARKER);
  }

  /**
   * Calculates the wave function.
   */
  public void calculate() {
    schroedinger.xmin = control.getDouble("xmin");
    schroedinger.xmax = control.getDouble("xmax");
    schroedinger.stepHeight = control.getDouble("step height at x = 0");
    schroedinger.numberOfPoints = control.getInt("number of points");
    schroedinger.energy = control.getDouble("energy");
    schroedinger.initialize();
    schroedinger.setState(control.getDouble("phi"), control.getDouble("phi'"));
    schroedinger.solve();
    frame.append(0, schroedinger.x, schroedinger.phi);
  }
  

  /**
   * Resets the calculation parameters.
   */
  public void reset() {
    control.setValue("xmin", -10);
    control.setValue("xmax", 10);
    control.setValue("step height at x = 0", 3);
    control.setValue("number of points", 500);
    control.setValue("phi", 1);
    control.setValue("phi'", 0);
    control.setValue("energy", 1);
  }

  /**
   * The main method starts the Java application.
   * @param args 
   * @param args[]  the input parameters
   */
  public static void main(String[] args) {
    CalculationControl.createApp(new SchroedingerApp1(), args);
  }
}