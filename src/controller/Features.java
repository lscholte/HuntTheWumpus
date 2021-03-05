package controller;

import model.Direction;

/**
 * Represents a series of features that the Hunt the Wumpus game supports.
 * @author Liam Scholte
 *
 */
public interface Features {
  
  /**
   * Moves the current player in the specified direction.
   * @param direction the direction to move
   */
  public void move(Direction direction);
  
  /**
   * Shoots an arrow from the current player in the specified direction.
   * @param direction the direction to shoot an arrow
   * @param distance the number of caves for the arrow to travel
   */
  public void shootArrow(Direction direction, int distance);
  
  /**
   * Kills the current player.
   */
  public void suicide();
}
