package fallingparticle;

public class ParticleApp {

  /**
   * @param args
   */
  public static void main(String[] args) {
    Particle particle = new EulerParticle(10, 0, 0.01);

    System.out.println("Simulation initiated");
    particle.print();

    while (particle.getY() > 0.0) {
      particle.step();
//      particle.print();
    }

    System.out.println("Simulation finished");
    particle.print();

  }

}
