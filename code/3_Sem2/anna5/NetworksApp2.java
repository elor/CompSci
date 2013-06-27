package anna5;
import org.opensourcephysics.controls.*;
import org.opensourcephysics.frames.*;

/**
 * NetworksApp simulates and displays a network model such as PreferentialAttachment.
 *
 * @author Jan Tobochnik, Wolfgang Christiann, Harvey Gould
 * @version 1.0  revised 06/24/05
 */
public class NetworksApp2 extends AbstractSimulation {
  PreferentialAttachment2 network = new PreferentialAttachment2();
  PlotFrame plot2= new PlotFrame("lnN","lnC(N)","Clustering Coefficient");
 

  public NetworksApp2() {
   
  }

  public void initialize() {
    network.N = control.getInt("Number of nodes");
    network.m = control.getInt("links per node");
    network.drawPositions = false;
    network.initialize();
  }

  public void reset() {
    control.setAdjustableValue("Number of nodes", 100);
    control.setAdjustableValue("links per node", 2);
    control.setAdjustableValue("draw?", true);
  }

  public void startRunning() {
    network.drawPositions = control.getBoolean("draw?");
  }

  public void doStep() {
	initialize();  
    for (int i=0;i<network.N;i++){
	  network.step();}

    plot2.append(0,Math.log(network.n),Math.log(network.coeffi()));
  }

  public void stop() {
    
  }

  public static void main(String args[]) {
    SimulationControl.createApp(new NetworksApp2());
  }
}