import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * simple list test
 * 
 * @author elor
 */
public class ListTest {

  List<Double> list = new LinkedList<Double>();

  /**
   * @param args
   */
  public static void main(String[] args) {
    ListTest test = new ListTest();
    test.run();

  }

  private void run() {
    insert(1.0);
    insert(2.0);
    insert(1.5);
    insert(0.0);

    print();
  }

  private void print() {
    Iterator<Double> it = list.iterator();

    while (it.hasNext()) {
      double v = it.next();
      System.out.println(v);
    }
  }

  private void insert(double d) {
    if (list.isEmpty()) {
      list.add(d);
      return;
    }

    Iterator<Double> it = list.iterator();
    double v;
    int pos = 0;

    do {
      v = it.next();
      if (v < d) {
        ++pos;
      } else {
        break;
      }
    } while (it.hasNext());

    list.add(pos, d);
  }
}
