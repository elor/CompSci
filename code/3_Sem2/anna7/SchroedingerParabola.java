package anna7;

import org.opensourcephysics.numerics.ODE;
import org.opensourcephysics.numerics.ODESolver;
import org.opensourcephysics.numerics.RK45MultiStep;

/**
 * Schroedinger solves the time independent Schroedinger equation
 * 
 * @author Wolfgang Christian, Jan Tobochnik, Harvey Gould
 * @version 1.0 revised 03/23/05
 */
public class SchroedingerParabola implements ODE {
  private static final double stepThreshold = 1.0e5;
  double energy = 0;
  public int nodes;
  int numberOfPoints;
  double[] phi;
  double[] state = new double[3]; // state = phi, dphi/dx, x
  double[] x;
  double xmax; // maximum x value
  ODESolver solver = new RK45MultiStep(this);

  /**
   * Evaluates the potential using a step at x=0. Change this function to model
   * other qm systems.
   * 
   * @param x
   * @return
   */
  public double evaluatePotential(double x) {
    return x * x / 2;
  }

  /**
   * @return
   */
  public double getDx() {
    return xmax / (numberOfPoints - 1);
  }

  /**
   * Gets the rate using the given state.
   * 
   * @param state
   * @param rate
   */
  public void getRate(double[] state, double[] rate) {
    rate[0] = state[1];
    rate[1] = 2.0 * (-energy + evaluatePotential(state[2])) * state[0];
    rate[2] = 1.0;
  }

  /**
   * Gets the state. The state for the ode solver: phi, dphi/dx, x.
   * 
   * @return the state
   */
  public double[] getState() {
    return state;
  }

  /**
   * Constructs a simple quantum mechanical model. The potential enegy is coded
   * into the evaluate method.
   * 
   * @param numberOfPoints
   *          number of grid points
   * @param a
   *          half interval
   */
  public void initialize() {
    phi = new double[numberOfPoints];
    x = new double[numberOfPoints];
    solver.setStepSize(getDx());
    nodes = 0;
  }

  /**
   * normalize the function in place
   */
  public void normalize() {
    // integrate to get quotient (squared)
    double dx = getDx();
    double quotient2 = 0.0;
    for (double p : phi) {
      quotient2 += p * p * dx;
    }

    double factor = 1 / Math.sqrt(quotient2 * 2);

    // scale phi
    for (int i = 0; i < phi.length; ++i) {
      phi[i] *= factor;
    }
  }

  public void setState(double phi, double dphidx) {
    state[0] = phi; // initial phi
    state[1] = dphidx; // nonzero initial dphi/dx
    state[2] = 0; // initial value of x
  }

  /**
   * Solves the Schroedinger equation and stores the result in the phi array.
   * 
   * @param energy
   * @return
   */
  void solve() {
    for (int i = 0; i < numberOfPoints; i++) { // zeros wavefunction
      phi[i] = 0;
    }

    for (int i = 0; i < numberOfPoints; i++) {
      phi[i] = state[0]; // stores wavefunction
      x[i] = state[2];
      solver.step(); // steps Schroedinger equation
      if (Math.abs(state[0]) > stepThreshold) { // checks for diverging solution
        break; // leave the loop
      }

      if ((state[0] >= 0 && phi[i] < 0)
          || (state[0] < 0 && phi[i] >= 0 && i != 0)) {
        nodes++;
      }
    }
  }

  public void shiftPhi(double energy) {
    for (int i = 0; i < phi.length; ++i) {
      phi[i] += energy;
    }
  }
}