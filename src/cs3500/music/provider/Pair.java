package cs3500.music.provider;

import java.util.Objects;

/**
 * A class for a 2-tuple.
 */
public class Pair<A, B> {
  private A first;
  private B second;

  /**
   * Construct a pair.
   *
   * @param first  the first item in the tuple.
   * @param second the second item in the tuple.
   */
  public Pair(A first, B second) {
    this.first = first;
    this.second = second;
  }

  /**
   * Get the first element in the tuple.
   *
   * @return the first element.
   */
  public A getFirst() {
    return this.first;
  }

  /**
   * Get the second element in the tuple.
   *
   * @return the second element in the tuple.
   */
  public B getSecond() {
    return this.second;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Pair) {
      Pair p = (Pair) other;
      return p.first.equals(this.first)
              && p.second.equals(this.second);
    }

    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.first, this.second);
  }
}
