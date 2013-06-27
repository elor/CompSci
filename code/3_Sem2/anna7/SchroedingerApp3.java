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
public class SchroedingerApp3 extends AbstractCalculation {
  private static final double eigenAccuracy = 1e-8;
  PlotFrame frame = new PlotFrame("x", "phi", "Wave function");
  SchroedingerDistortion schroedinger = new SchroedingerDistortion();

  /**
   * Constructs SchroedingerApp and sets plotting frame parameters.
   */
  public SchroedingerApp3() {
    frame.setConnected(true);
    frame.setMarkerShape(0, Dataset.NO_MARKER);
    frame.setMarkerShape(1, Dataset.NO_MARKER);
    frame.setMarkerShape(2, Dataset.NO_MARKER);
    frame.setLineColor(2, Color.BLACK);

    frame.setPreferredMinMaxY(-1.5, 1.5);

  }

  private void clearPlot() {
    frame.clearData();
  }

  private void plotPotential() {
    double dx = schroedinger.getDx();

    for (double x = schroedinger.xmin; x <= schroedinger.xmax; x += dx) {
      frame.append(2, x, schroedinger.evaluatePotential(x));
    }
  }

  /**
   * Calculates the wave function.
   */
  public void calculate() {
    double[] phiMit, phiOhne;

    schroedinger.stepHeight = control.getDouble("step height (Vb)");
    schroedinger.stepHalfWidth = control.getDouble("step halfwidth (b)");
    schroedinger.xmax = control.getDouble("system halfwidth (a)");
    schroedinger.xmin = -schroedinger.xmax;
    schroedinger.numberOfPoints = control.getInt("number of points");

    double energy1 = eigenvalue(control.getInt("nodes"), eigenAccuracy);

    calcNodes(energy1);
    schroedinger.normalize();

    phiMit = schroedinger.phi;

    clearPlot();

    frame.append(0, schroedinger.x, schroedinger.phi);

    plotPotential();

    schroedinger.stepHeight = 0;

    double energy2 = eigenvalue(control.getInt("nodes"), eigenAccuracy);
    calcNodes(energy2);
    schroedinger.normalize();

    phiOhne = schroedinger.phi;

    frame.append(1, schroedinger.x, schroedinger.phi);

    double dx = (schroedinger.xmax - schroedinger.xmin)
        / (schroedinger.numberOfPoints - 1);

    double overlap = overlapIntegral(phiMit, phiOhne, dx);

    control.clearMessages();
    control.println("Energie mit: " + energy1);
    control.println("Energie ohne: " + energy2);
    control.println("overlap: " + overlap);
  }

  double overlapIntegral(double[] phiMit, double[] phiOhne, double dx) {
    if (phiMit.length != phiOhne.length || dx <= 0.0) {
      System.err.println("Warning: overlapIntegral(): invalid arguments");
    }

    double sum = 0;
    for (int i = 0; i < phiMit.length; ++i) {
      sum += dx * phiMit[i] * phiOhne[i];
    }

    return sum;
  }

  int calcNodes(double e) {
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

    // control.println("emin: " + emin + ", emax: " + emax);
    // control.println("nodesmin: " + nodesmin + ", nodesmax: " + nodesmax);

    return (emin + emax) / 2;
  }

  /**
   * Resets the calculation parameters.
   */
  public void reset() {
    control.setValue("system halfwidth (a)", 1);
    control.setValue("step halfwidth (b)", 0.1);
    control.setValue("step height (Vb)", 10);
    control.setValue("number of points", 500);
    control.setValue("phi'", 1);
    control.setValue("nodes", 0);
  }

  /**
   * The main method starts the Java application.
   * 
   * @param args
   *          [] the input parameters
   */
  public static void main(String[] args) {
    CalculationControl.createApp(new SchroedingerApp3(), args);
  }
}