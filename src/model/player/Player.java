package model.player;

import model.Direction;

/**
 * Represents a player in a game of Hunt the Wumpus.
 * @author Liam Scholte
 *
 */
public interface Player extends ReadOnlyPlayer {
  
  /**
   * Moves the player to a location in the specified
   * direction if such a location is accessible from the
   * current location. Otherwise, the player remains
   * at the current location.
   * @param direction the direction to move towards
   * @throws IllegalStateException if the entity is in a state
   *      where movement is not allowable
   */
  public void move(Direction direction) throws IllegalStateException;
  
  /**
   * Shoots an arrow in the specified direction
   * for the specified distance.
   * @param direction the direction to shoot
   * @param distance the amount of non-hallway rooms
   *      the arrow will travels
   * @throws IllegalArgumentException if distance is not positive
   * @throws IllegalStateException if an arrow cannot be fired
   */
  public void shootArrow(Direction direction, int distance)
      throws IllegalArgumentException, IllegalStateException;
  
  /**
   * Kills the player. Subsequent method invocations
   * on {@code this} player may result in an
   * IllegalStateException.
   */
  public void kill();
   
}
