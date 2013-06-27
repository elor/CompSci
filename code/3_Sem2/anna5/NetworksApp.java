package anna5;
import org.opensourcephysics.controls.*;
import org.opensourcephysics.frames.*;

/**
 * NetworksApp simulates and displays a network model such as PreferentialAttachment.
 *
 * @author Jan Tobochnik, Wolfgang Christiann, Harvey Gould
 * @version 1.0  revised 06/24/05
 */
public class NetworksApp extends AbstractSimulation {
  PreferentialAttachment network = new PreferentialAttachment();
  PlotFrame plot = new PlotFrame("ln s", "ln N(s)", "Degree Distribution");
  DisplayFrame display = new DisplayFrame("", "", "Network");

  public NetworksApp() {
    display.addDrawable(network);
    display.setPreferredMinMax(0, 100, 0, 100);
  }

  public void initialize() {
    network.N = control.getInt("Number of nodes");
    network.m = control.getInt("links per node");
    network.drawPositions = control.getBoolean("draw?");
    network.initialize();
  }

  public void reset() {
    control.setValue("Number of nodes", 100);
    control.setValue("links per node", 2);
    control.setAdjustableValue("draw?", true);
  }

  public void startRunning() {
    network.drawPositions = control.getBoolean("draw?");
  }

  public void doStep() {
    network.step();
    display.setMessage(network.numberOfCompletedNetworks+" completed networks "+network.n+" nodes in new network");
  }

  public void stop() {
    network.degreeDistribution(plot);
    plot.render();
  }

  public static void main(String args[]) {
    SimulationControl.createApp(new NetworksApp());
  }
}