package model;

/**
 * Represents a mutable current location along with
 * a direction of travel.
 * @author Liam Scholte
 *
 */
public class MutableHeading extends Heading {
  
  private Room room;

  /**
   * Constructs a Heading.
   * @param room the current location
   * @param direction the direction of travel
   */
  public MutableHeading(Room room, Direction direction) {
    super(room, direction);
    this.room = room;
  }

  /**
   * Gets the current location.
   * @return the current location
   */
  public Room getMutableRoom() {
    return room;
  }
}
