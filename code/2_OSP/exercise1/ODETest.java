package exercise1;

import org.opensourcephysics.numerics.*;

/**
 * ODE Test
 * 
 * @author elor
 */
public class ODETest {

  /**
   * @param args
   */
  public static void main(String[] args) {
    ODE ode = new ParticleODE(0, 5, 10, 0, 0, 0, 1, 9.81);
    AbstractODESolver solver = new Verlet(ode);

    System.out.println(ode);
    while (ode.getState()[2] >= 0.0) {
      solver.step();
      System.out.println(ode);
    }
  }

}
