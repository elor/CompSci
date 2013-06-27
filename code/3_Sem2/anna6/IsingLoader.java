
package anna6;
import org.opensourcephysics.controls.*;
import org.opensourcephysics.frames.*;
import org.opensourcephysics.display.GUIUtils;

/**
 * IsingLoader implements the ObjectLoader interface to load and store Ising model data.
 *
 * @author Wolfgang Christian, Jan Tobochnik, Harvey Gould
 * @version 1.0 revised 04/10/05
 */
public class IsingLoader implements XML.ObjectLoader {

  /**
   * Creates a LJParticlesApp object.
   *
   * @param control the xml control
   * @return a new object
   */
  public Object createObject(XMLControl element) {
    return new IsingApp();
  }

  /**
   * Saves data from the IsingApp model into the control.
   *
   * @param element XMLControl
   * @param obj Object
   */
  public void saveObject(XMLControl control, Object obj) {
    IsingApp model = (IsingApp) obj;
    // control.setValue("name", "ising_model");   // stores the data in the plot
    control.setValue("temperature", model.ising.temperature);
    control.setValue("mcs", model.ising.mcs);
    control.setValue("energy", model.ising.energy);
    control.setValue("energy_accumulator", model.ising.energyAccumulator);
    control.setValue("energy_squared_accumulator", model.ising.energySquaredAccumulator);
    control.setValue("magnetization", model.ising.magnetization);
    control.setValue("magnetization_accumulator", model.ising.magnetizationAccumulator);
    control.setValue("magnetization_squared_accumulator", model.ising.magnetizationSquaredAccumulator);
    control.setValue("accepted_moves", model.ising.acceptedMoves);
    control.setValue("spin", model.ising.lattice); // stores the spin lattice
    control.setValue("plot", model.plotFrame);     // stores the data in the plot
  }

  /**
   * Loads data from the control into the IsingApp model.
   *
   * @param element XMLControl
   * @param obj Object
   * @return Object
   */
  public Object loadObject(XMLControl control, Object obj) {
    // GUI has been loaded with the saved values; now restore the Ising model's state
    IsingApp model = (IsingApp) obj;
    model.initialize(); // reads values from the GUI into the Ising model
    model.ising.temperature = control.getDouble("temperature");
    model.ising.mcs = control.getInt("mcs");
    model.ising.energy = control.getInt("energy");
    model.ising.energyAccumulator = control.getDouble("energy_accumulator");
    model.ising.energySquaredAccumulator = control.getDouble("energy_squared_accumulator");
    model.ising.magnetization = control.getInt("magnetization");
    model.ising.magnetizationAccumulator = control.getDouble("magnetization_accumulator");
    model.ising.magnetizationSquaredAccumulator = control.getDouble("magnetization_squared_accumulator");
    model.ising.acceptedMoves = control.getInt("accepted_moves");
    model.ising.lattice = (LatticeFrame) control.getObject("spin");
    // transfering data from XML to an existing object is a two=step process
    // first: get an XMLControl containing the data
    XMLControl childControl = control.getChildControl("plot");
    // second: have the control load the data into the object
    childControl.loadObject(model.plotFrame);
    GUIUtils.repaintAnimatedFrames();
    return obj;
  }
}