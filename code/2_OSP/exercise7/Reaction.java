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
    int l = length();

    for (int i = 0; i < l; ++i) {
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

  public int[] getNearestNeighborDistances() {
    // Create return array of length n (atom count)
    // find first cell
    // until you reach the end:
    // find next cell
    // calculate distance between last and next cell
    // write distance to next position in array
    // if less than current distance:
    // write distance to current position in array
    // get distance between last and first element
    // apply both to first and last position in array if lower

    // current: 0 to n-1
    // cell: 0 to l-1
    // firstcell: 0 to n-1
    // lastcell: 0 to n-1

    int n = n();
    if (n <= 1) {
      return new int[0];
    }

    int l = length();
    int[] distances = new int[n];

    int current = 0;
    int lastcell = 0;
    int firstcell = 0;

    // locate first cell
    int cell = 0;
    for (; at(cell) == 0; ++cell)
      ;

    firstcell = lastcell = cell;
    ++cell;

    // instantiate the first element to an absurdly high value
    distances[0] = l;

    for (; cell < l; ++cell) {
      if (at(cell) != 0) {
        int dist = cell - lastcell;
        distances[current + 1] = dist;
        if (distances[current] > dist) {
          distances[current] = dist;
        }

        lastcell = cell;
        ++current;
      }
    }

    // apply periodic boundary conditions
    int dist = l + firstcell - lastcell;
    if (distances[0] > dist) {
      distances[0] = dist;
    }
    if (distances[n - 1] > dist) {
      distances[n - 1] = dist;
    }

    return distances;
  }
}
