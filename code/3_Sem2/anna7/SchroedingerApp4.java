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
public class SchroedingerApp4 extends AbstractCalculation {
  private static final double eigenAccuracy = 1e-8;

  /**
   * The main method starts the Java application.
   * 
   * @param args
   *          [] the input parameters
   */
  public static void main(String[] args) {
    CalculationControl.createApp(new SchroedingerApp4(), args);
  }

  PlotFrame frame = new PlotFrame("x", "phi", "Wave function");

  SchroedingerParabola schroedinger = new SchroedingerParabola();

  /**
   * Constructs SchroedingerApp and sets plotting frame parameters.
   */
  public SchroedingerApp4() {
    frame.setConnected(true);
    frame.setMarkerShape(0, Dataset.NO_MARKER);
    frame.setLineColor(0, Color.BLACK);
  }

  int calcNodes(double e, boolean even) {
    schroedinger.energy = e;
    schroedinger.initialize();

    if (even) {
      schroedinger.setState(1, 0);
    } else {
      schroedinger.setState(0, 1);
    }

    schroedinger.solve();

    return schroedinger.nodes;
  }

  /**
   * Calculates the wave function.
   */
  public void calculate() {
    schroedinger.numberOfPoints = control.getInt("number of points");

    double xmaxmin = control.getDouble("xmaxmin");
    double xmaxmax = control.getDouble("xmaxmax");

    clearPlot();

    int nodesmin = control.getInt("nodesmin");
    int nodesmax = control.getInt("nodesmax");
    for (int nodes = nodesmin; nodes <= nodesmax; ++nodes) {
      schroedinger.xmax = xmaxmin + (xmaxmax - xmaxmin) * (nodes - nodesmin)
          / (nodesmax - nodesmin);
      getAndPlotEnergy(nodes, nodes + 1);
    }

    plotPotential();
  }

  private void clearPlot() {
    frame.clearData();
  }

  private void plotPotential() {
    double dx = schroedinger.getDx();

    for (double x = 0.0; x <= schroedinger.xmax; x += dx) {
      frame.append(0, x, schroedinger.evaluatePotential(x));
    }
  }

  /**
   * @param nodes
   * @param dataset
   */
  void getAndPlotEnergy(int nodes, int dataset) {
    double energy = eigenvalue(nodes, eigenAccuracy);

    // calcNodes(energy1, nodes % 2 == 0);
    schroedinger.normalize();

    schroedinger.shiftPhi(energy);

    frame.setMarkerShape(dataset, Dataset.NO_MARKER);
    frame.setPreferredMinMaxY(0.0, energy + 1.5);
    frame.append(dataset, schroedinger.x, schroedinger.phi);

    control.println("Energie bei " + nodes + " Knoten: " + energy);
  }

  public double eigenvalue(int nodes, double accuracy) {
    boolean even = nodes % 2 == 0;
    nodes /= 2;

    int nodesmin = 0;
    int nodesmax = 0;
    double emin = 1;
    double emax = 2;

    nodesmin = calcNodes(emin, even);
    nodesmax = calcNodes(emax, even);

    double factor = 0.9;

    // untere Grenze anpassen
    while (nodesmin != nodes) {
      while (nodesmin > nodes) {
        emax = emin;
        nodesmax = nodesmin;
        emin = emin * factor;
        nodesmin = calcNodes(emin, even);
      }
      while (nodesmin < nodes) {
        emin = emax;
        nodesmin = nodesmax;
        emax = emax / factor;
        nodesmax = calcNodes(emax, even);
      }

      factor = factor * 0.9;
    }

    // obere Grenze anpassen
    factor = 0.9;
    while (nodesmax != nodes + 1) {
      while (nodesmax > nodes + 1) {
        emax = emax * factor;
        nodesmax = calcNodes(emax, even);
      }
      while (nodesmax < nodes + 1) {
        emax = emax / factor;
        nodesmax = calcNodes(emax, even);
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
      nodesmid = calcNodes(emid, even);

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

  /**
   * Resets the calculation parameters.
   */
  public void reset() {
    control.setValue("xmaxmin", 3.5);
    control.setValue("xmaxmax", 5);
    control.setValue("number of points", 500);
    control.setValue("nodesmin", 0);
    control.setValue("nodesmax", 5);
  }
}