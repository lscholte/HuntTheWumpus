package model;

/**
 * Represents a current location along with
 * a direction of travel.
 * @author Liam Scholte
 *
 */
public class Heading {
  
  private ReadOnlyRoom room;
  private Direction direction;
  
  /**
   * Constructs a Heading.
   * @param room the current location
   * @param direction the direction of travel
   */
  public Heading(ReadOnlyRoom room, Direction direction) {
    this.room = room;
    this.direction = direction;
  }
  
  /**
   * Gets the current location.
   * @return the current location
   */
  public ReadOnlyRoom getRoom() {
    return room;
  }
  
  /**
   * Gets the direction of travel.
   * @return the direction of travel
   */
  public Direction getDirection() {
    return direction;
  }

}
