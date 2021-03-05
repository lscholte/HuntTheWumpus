package model;

/**
 * A game of Hunt the Wumpus with the actions that can be performed.
 * @author Liam Scholte
 *
 */
public interface Game extends ReadOnlyGame {
  
  /**
   * Moves the player whose turn it currently is in the specified direction.
   * @param direction the direction to move the player
   * @throws IllegalStateException if the game is over
   */
  public void move(Direction direction) throws IllegalStateException;
  
  /**
   * Shoots an arrow from the player whose turn it currently is
   * in the specified direction.
   * @param direction the direction to shoot
   * @param distance the number of caves to shoot
   * @throws IllegalStateException if the game is over
   */
  public void shootArrow(Direction direction, int distance) throws IllegalStateException;
  
  /**
   * Kills the player whose turn it currently is.
   * @throws IllegalStateException if the game is over
   */
  public void suicide() throws IllegalStateException;

}
