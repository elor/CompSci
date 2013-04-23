package ex02;

/**
 * @author elor
 */
public class Pair implements Comparable<Pair> {

  /**
   * 
   */
  public double key;
  /**
   * 
   */
  public int value;

  /**
   * @param key
   * @param value
   */
  public Pair(double key, int value) {
    super();
    this.key = key;
    this.value = value;
  }

  @Override
  public int compareTo(Pair o) {
    return Double.compare(key, o.key);
  }

}
