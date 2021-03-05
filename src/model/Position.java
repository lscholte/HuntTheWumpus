package model;

import java.util.Objects;

/**
 * Represents a position in an XY-plane.
 * @author Liam Scholte
 *
 */
public final class Position {
  
  private final int x;
  private final int y;
  
  /**
   * Constructs a position.
   * @param x the X coordinate
   * @param y the Y coordinate
   */
  public Position(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Gets the X coordinate.
   * @return the Y coordinate
   */
  public int getX() {
    return x;
  }
  
  /**
   * Gets the Y coordinate.
   * @return the Y coordinate
   */
  public int getY() {
    return y;
  }
  
  @Override
  public String toString() {
    return String.format("(%d, %d)", x, y);
  }
  
  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Position)) {
      return false;
    }
    
    if (this == other) {
      return true;
    }
    
    Position otherPosition = (Position)other;
    
    return x == otherPosition.x && y == otherPosition.y;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }
}
