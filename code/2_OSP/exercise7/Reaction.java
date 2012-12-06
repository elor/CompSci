package exercise7;

import java.util.Random;

public class Reaction {
  int[] lattice;
  int time;
  Random rng = new Random();

  public void initialize(int size) {
    lattice = new int[size];
    time = 0;
  }

  public void step() {
    int len = length();
    
    // set the step direction
    for (int i = 0; i < len; ++i) {
      if (lattice[i] != 0)
      {
        lattice[i] = (rng.nextDouble() < 0.5 ? -1 : 1);
      }
    }

    // TODO: perform the steps
    // remove steps that cancel out
    for (int i = 0; i < len; ++i) {
      switch() {
      case -1:
        if ()
        break;
      case 1:
        break;
      case 0:
        break;
      }
    }
    

    ++time;
  }

  public int t() {
    return time;
  }

  public int n() {
    int num = 0;
    int len = length();
    
    for (int i = 0; i < len; ++i) {
      if (lattice[i] != 0) {
        ++num;
      }
    }

    return num;
  }

  private int length() {
    return lattice.length;
  }
}
