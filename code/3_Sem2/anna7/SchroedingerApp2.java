package anna7;

import java.awt.Color;

import org.opensourcephysics.controls.*;
import org.opensourcephysics.display.*;
import org.opensourcephysics.frames.*;

/**
 * SchroedingerApp displays a solution to the time-independent Schroedinger
 * equation.
 * 
 * @author Wolfgang Christian, Jan Tobochnik, Harvey Gould
 * @version 1.0 revised 03/23/05
 */
public class SchroedingerApp2 extends AbstractCalculation {
  private static final double nodesAccuracy = 1e-8;
  PlotFrame frame = new PlotFrame("x", "phi", "Wave function");
  SchroedingerStep schroedinger = new SchroedingerStep();

  /**
   * Constructs SchroedingerApp and sets plotting frame parameters.
   */
  public SchroedingerApp2() {
    frame.setConnected(true);
    frame.setMarkerShape(0, Dataset.NO_MARKER);
    frame.setLineColor(0, Color.BLACK);
  }

  /**
   * Calculates the wave function.
   */
  public void calculate() {
    int nodesmin = control.getInt("nodesmin");
    int nodesmax = control.getInt("nodesmax");

    clearPlot();

    for (int nodes = nodesmin; nodes <= nodesmax; ++nodes) {
      getAndPlotEnergies(nodes);
    }

    plotPotential();
  }

  private void clearPlot() {
    frame.clearData();
  }

  private void plotPotential() {
    double dx = schroedinger.getDx();

    for (double x = schroedinger.xmin; x <= schroedinger.xmax; x += dx) {
      frame.append(0, x, schroedinger.evaluatePotential(x));
    }
  }

  /**
   * @param nodes
   */
  void getAndPlotEnergies(int nodes) {
    double energy = eigenvalue(nodes, nodesAccuracy);
    calcNodes(energy);
    schroedinger.normalize();

    frame.setPreferredMinMaxY(0.0, energy + 2.0);
    frame.setMarkerShape(nodes + 1, Dataset.NO_MARKER);
    schroedinger.shiftPhi(energy);
    frame.append(nodes + 1, schroedinger.x, schroedinger.phi);
  }

  int calcNodes(double e) {
    schroedinger.xmin = control.getDouble("xmin");
    schroedinger.xmax = control.getDouble("xmax");
    schroedinger.stepHeight = control.getDouble("step height at x = 0");
    schroedinger.numberOfPoints = control.getInt("number of points");
    schroedinger.energy = e;
    schroedinger.initialize();
    schroedinger.setState(0, control.getDouble("phi'"));
    schroedinger.solve();

    return schroedinger.nodes;
  }

  public double eigenvalue(int nodes, double accuracy) {
    int nodesmin = 0;
    int nodesmax = 0;
    double emin = 1;
    double emax = 2;

    nodesmin = calcNodes(emin);
    nodesmax = calcNodes(emax);

    double factor = 0.9;

    // untere Grenze anpassen
    while (nodesmin != nodes) {
      while (nodesmin > nodes) {
        emax = emin;
        nodesmax = nodesmin;
        emin = emin * factor;
        nodesmin = calcNodes(emin);
      }
      while (nodesmin < nodes) {
        emin = emax;
        nodesmin = nodesmax;
        emax = emax / factor;
        nodesmax = calcNodes(emax);
      }

      factor = factor * 0.9;
    }

    // obere Grenze anpassen
    factor = 0.9;
    while (nodesmax != nodes + 1) {
      while (nodesmax > nodes + 1) {
        emax = emax * factor;
        nodesmax = calcNodes(emax);
      }
      while (nodesmax < nodes + 1) {
        emax = emax / factor;
        nodesmax = calcNodes(emax);
      }

      factor = factor * 0.9;
    }

    // control.println("emin: " + emin + ", emax: " + emax);
    // control.println("nodesmin: " + nodesmin + ", nodesmax: " + nodesmax);

    // Eigenenergie binär suchen
    double emid;
    double nodesmid;
    while (emax - emin > accuracy) {
      emid = (emin + emax) / 2;
      nodesmid = calcNodes(emid);

      if (nodesmid == nodesmin) {
        emin = emid;
      }
      if (nodesmid == nodesmax) {
        emax = emid;
      }
      if (nodesmin == nodesmax) {
        // ERROR. consider the search complete
        break;
      }
    }

    control.println("emin: " + emin + ", emax: " + emax);
    control.println("nodesmin: " + nodesmin + ", nodesmax: " + nodesmax);

    return (emin + emax) / 2;
  }

  /**
   * Resets the calculation parameters.
   */
  public void reset() {
    control.setValue("xmin", -1);
    control.setValue("xmax", 1);
    control.setValue("step height at x = 0", 1);
    control.setValue("number of points", 500);
    control.setValue("phi'", 1);
    control.setValue("nodesmin", 0);
    control.setValue("nodesmax", 5);
  }

  /**
   * The main method starts the Java application.
   * 
   * @param args
   *          [] the input parameters
   */
  public static void main(String[] args) {
    CalculationControl.createApp(new SchroedingerApp2(), args);
  }
}