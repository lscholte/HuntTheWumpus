package model;

import model.maze.ReadOnlyMaze;
import model.player.ReadOnlyPlayer;

import java.util.List;

/**
 * Represents an unmodifable game of Hunt the Wumpus.
 * The functionality provided will only retrieve the
 * current state of the game.
 * @author Liam Scholte
 *
 */
public interface ReadOnlyGame {
      
  /**
   * Determines if the game, in its current state, is
   * winnable for the player, regardless of how improbable
   * a win might be. Note that a winnable state may rely
   * on random chance, meaning that even with perfect play
   * by the player, a win is not guaranteed but is
   * possible nonetheless. If the player has already won or lost,
   * then isWinnable will return that result.
   * @return {@code true} if there is some sequence of moves
   *      that could result in the player winning,
   *      {@code false} otherwise
   * @throws IllegalStateException if the game has not started
   */
  public boolean isWinnable() throws IllegalStateException;
  
  /**
   * Determines if the game is over.
   * @return {@code true} if the game is over,
   *      {@code false} otherwise
   * @throws IllegalStateException if the game has not started
   */
  public boolean isOver() throws IllegalStateException;
  
  /**
   * Determines which player won the game.
   * @return the player that won the game or null if there
   *      was no winner
   * @throws IllegalStateException if the game has not started or
   *      the game is already over
   */  
  public ReadOnlyPlayer getWinner() throws IllegalStateException;
  
  /**
   * Gets all players in the game.
   * @return the players in the game
   */  
  public List<ReadOnlyPlayer> getPlayers();
  
  /**
   * Gets the player whose turn it currently is.
   * @return the current player
   */
  public ReadOnlyPlayer getCurrentPlayer();
  
  /**
   * Gets the game's maze.
   * @return the game's maze
   */
  public ReadOnlyMaze getMaze();
}
