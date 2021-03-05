package controller;

import model.maze.MazeGenerationException;

/**
 * Creates instances of a Hunt the Wumpus game.
 * @author Liam Scholte
 *
 */
public interface GameCreator {
  
  /**
   * Create a Hunt the Wumpus game.
   * @param rowCount the number of rows in the game's maze
   * @param colCount the number of columns in the game's maze
   * @param wraps whether or not the game's maze wraps at the edges
   * @param batCount the number of caves with bats
   * @param pitCount the number of caves with pits
   * @param playerCount the number of players in the game
   * @param arrowCount the number of arrows each player starts with
   * @param seed a random seed for the game
   * @throws IllegalArgumentException if any of the parameters are invalid
   * @throws MazeGenerationException if a maze for the game cannot be generated
   */
  public void createGame(
      int rowCount,
      int colCount,
      boolean wraps,
      int batCount,
      int pitCount,
      int playerCount,
      int arrowCount,
      long seed)
      throws IllegalArgumentException, MazeGenerationException;

}
