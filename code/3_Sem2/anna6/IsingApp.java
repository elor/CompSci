
package anna6;
import org.opensourcephysics.controls.*;
import org.opensourcephysics.frames.*;

/**
 * IsingApp simulates a two-dimensional Ising model.
 *
 * @author Jan Tobochnik, Wolfgang Christian, Harvey Gould
 * @version 1.0  revised 07/05/05
 */
public class IsingApp extends AbstractSimulation {
  Ising ising = new Ising();
  LatticeFrame displayFrame = new LatticeFrame("Ising Model");
  PlotFrame plotFrame = new PlotFrame("time", "E and M", "Ising model");;
  PlotFrame plot2= new PlotFrame("E","sigma2","Sigma 2");
  PlotFrame plot3= new PlotFrame("E","sigma m","Sigma m");

  public IsingApp() {
    plotFrame.setXYColumnNames(0, "mcs", "M", "magnetization");
    plotFrame.setXYColumnNames(1, "mcs", "E", "energy");
  }

  public void initialize() {
    ising.temperature = control.getDouble("temperature");
    ising.initialize(control.getInt("L"), displayFrame);
    double p=control.getDouble("Besetzungswahrscheinlichkeit");
    resetData();
    int L=control.getInt("L");
    L=L*L;
    for (int i=0;i<L;i++){
    	double r=Math.random();
    	displayFrame.setAtIndex(i, (r < p ? 1 : -1));
    }
    
    ising.updateMagnetization();
  }

  public void doStep() {
    ising.doOneMCStep();
    plotFrame.append(0, ising.mcs, ising.magnetization*1.0/ising.N);
    plotFrame.append(1, ising.mcs, ising.energy*1.0/ising.N);
    plot2.append(0,ising.mcs,ising.sigma2);
    plot3.append(0,ising.mcs,ising.sigmam);
  }

  public void stop() {
    double norm = 1.0/(ising.mcs*ising.N);
    control.println("mcs = "+ising.mcs);
    control.println("acceptance probability = "+ising.acceptedMoves*norm);
    control.println("<E> = "+ising.energyAccumulator*norm);
    control.println("specific heat = "+ising.specificHeat());
    control.println("<M> = "+ising.magnetizationAccumulator*norm);
    control.println("susceptibility = "+ising.susceptibility());
    control.println("Vorzeichenwechsel = "+ising.vzw);
    control.println("VZW (norm) = "+ising.vzw/(double)ising.mcs);
    control.println("Chi= "+ising.chi());
    control.println("Chiabs= "+ising.chiabs());
  }

  public void startRunning() {
    ising.temperature = control.getDouble("temperature");
    ising.w[8] = Math.exp(-8.0/ising.temperature); // other array elements never occur for H = 0
    ising.w[4] = Math.exp(-4.0/ising.temperature);
  }

  public void reset() {
    control.setValue("L", 32);
    control.setValue("Besetzungswahrscheinlichkeit",0.5);
    control.setAdjustableValue("temperature", Ising.criticalTemperature);
    enableStepsPerDisplay(true); // allow user to speed up simulation
  }

  public void resetData() {
    ising.resetData();
    plotFrame.clearData();
    plotFrame.repaint();
    control.clearMessages();
    plot2.clearData();
    plot2.repaint();
    plot3.clearData();
    plot3.repaint();
  }

  /**
* Returns an XML.ObjectLoader to save and load data for this program.
*
* LJParticle data can now be saved using the Save menu item in the control.
*
* @return the object loader
*/
  public static XML.ObjectLoader getLoader() {
    return new IsingLoader();
  }

  public static void main(String[] args) {
    SimulationControl control = SimulationControl.createApp(new IsingApp());
    control.addButton("resetData", "Reset Data");
  }
}
