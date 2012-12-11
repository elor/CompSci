package exercise7;

import java.util.Random;

public class Reaction {
  int[] lattice;
  int time;
  Random rng = new Random();
  private boolean directInteraction = false;

  public boolean isDirectInteraction() {
    return directInteraction;
  }

  public void setDirectInteraction(boolean directInteraction) {
    this.directInteraction = directInteraction;
  }

  public void initialize(int size) {
    lattice = new int[size];
    for (int i = 0; i < length(); ++i) {
      set(i, 1);
    }
    // randomize();
    time = 0;
  }

  public void step() {
    int len = length();

    // remove steps that cancel out
    for (int i = 0; i < len; ++i) {
      switch (at(i)) {
      // We don't need this case, because the lower particle performs the same
      // check
      // case -1:
      // if (directInteraction && at(i - 1) == 1) {
      // set(i - 1, 0);
      // set(i, 0);
      // } else if (at(i - 2) == 1) {
      // set(i - 2, 0);
      // set(i, 0);
      // }
      //
      // break;
      case 1:
        if (directInteraction && at(i + 1) == -1) {
          set(i + 1, 0);
          set(i, 0);
        } else if (at(i + 2) == -1) {
          set(i + 2, 0);
          set(i, 0);
        }

        break;
      case 0:
        // do nothing
        break;
      }
    }

    // perform the resulting steps
    // use a new array for that
    int[] oldstate = lattice;
    lattice = new int[length()];
    for (int i = 0; i < len; ++i) {
      if (oldstate[i] != 0) {
        set(i + oldstate[i], 1);
      }
    }

    randomize();

    ++time;
  }

  private void randomize() {
    int len = length();
    // set the step direction
    for (int i = 0; i < len; ++i) {
      if (at(i) != 0) {
        set(i, (rng.nextDouble() < 0.5 ? -1 : 1));
      }
    }
  }

  private void set(int i, int value) {
    // the indexing is hack
    lattice[(i + length()) % length()] = value;
  }

  public int at(int i) {
    // the indexing still is a hack
    return lattice[(i + length()) % length()];
  }

  public int t() {
    return time;
  }

  public int n() {
    int num = 0;
    int len = length();

    for (int i = 0; i < len; ++i) {
      if (at(i) != 0) {
        ++num;
      }
    }

    return num;
  }

  public int length() {
    return lattice.length;
  }

  public int count() {
    int count = 0;

    for (int i = 0; i < length(); ++i) {
      if (at(i) != 0) {
        ++count;
      }
    }

    return count;
  }
}
