package model;

import java.util.Set;

/**
 * Represents an unmodifiable room within a maze.
 * 
 * @author Liam Scholte
 *
 */
public interface ReadOnlyRoom  {

  /**
   * Determines if the room is a hallway. A hallway is a room that has exactly 2
   * exits/entrances.
   * 
   * @return {@code true} if the room is a hallway, {@code false} otherwise
   */
  public boolean isHallway();
  
  /**
   * Counts the number of exits this room has.
   * @return the number of exits.
   */
  public int getExitCount();

  /**
   * Determines if the room has been visited by a player.
   * @return {code true} if the room has been explored,
   *      {@code false} otherwise
   */
  public boolean isExplored();
  
  /**
   * Gets the position of the room.
   * @return the position of the room
   */
  public Position getPosition();
  
  /**
   * Determines if this room has bats.
   * @return {@code true} if the room has bats,
   *      {@code false} otherwise
   */
  public boolean hasBats();

  /**
   * Determines if this room has a pit.
   * @return {@code true} if the room has a pit,
   *      {@code false} otherwise
   */
  public boolean hasPit();

  /**
   * Determines if this room has a wumpus.
   * @return {@code true} if the room has a wumpus,
   *      {@code false} otherwise
   */
  public boolean hasWumpus();
  
  /**
   * Determines if bats are in a neighbouring room.
   * @return {@code true} if bats are in a neighbouring room,
   *      {@code false} otherwise
   */
  public boolean areBatsNearby();
  
  /**
   * Determines if a pit is in a neighbouring room.
   * @return {@code true} if a pit is in a neighbouring room,
   *      {@code false} otherwise
   */
  public boolean isPitNearby();
  
  /**
   * Determines if a wumpus is in a neighbouring room.
   * @return {@code true} if a wumpus is in a neighbouring room,
   *      {@code false} otherwise
   */
  public boolean isWumpusNearby();
  
  /**
   * Gets the directions of exits from this room.
   * @return a set of all directions containing exits from this room
   */
  public Set<Direction> getAvailableDirections();
  
  /**
   * Gets the neighbouring room in the specified direction.
   * This neighbour may or may not be a hallway.
   * @param direction the direction of the neighbour
   * @return an unmodifiable neighbouring room if it exists
   *      or null if no such room exists
   */
  public ReadOnlyRoom getNeighbour(Direction direction);

  /**
   * Gets a neighbouring non-hallway room in the specified direction.
   * @param direction the direction of the neighbour
   * @return a heading containing the room that was found
   *      and the direction that is currently being traveled
   *      upon entering that room. The room in the heading
   *      may be null if the initial specified direction has no
   *      neighbouring room.
   */
  public Heading getNonHallNeighbour(Direction direction);
}
