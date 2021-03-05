package model;

/**
 * Represents a direction of travel.
 * @author Liam Scholte
 *
 */
public enum Direction {
  NORTH, SOUTH, EAST, WEST;
  
  /**
   * Gets the direction that is opposite of
   * {@code this} direction.
   * @return The opposite direction
   */
  public Direction getOpposite() {
    switch (this) {
      case NORTH: return SOUTH;
      case SOUTH: return NORTH;
      case EAST: return WEST;
      case WEST: return EAST;
      default: return null;
    }
  }
}
