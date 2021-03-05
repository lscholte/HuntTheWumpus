package view;

import controller.Features;
import controller.GameCreator;
import model.ReadOnlyGame;

/**
 * Represents a view for the Hunt the Wumpus game.
 * @author Liam Scholte
 *
 */
public interface View {
  
  /**
   * Refreshes the view to represent the current state of the game.
   */
  public void refresh();
  
  /**
   * Presents configuration options for the game.
   * @param gameCreator the creator that will create the game
   */
  public void presentConfiguration(GameCreator gameCreator);
  
  /**
   * Presents the game.
   * @param game the game to present
   * @param features the features of the game that are available
   */
  public void presentGame(ReadOnlyGame game, Features features);
}
