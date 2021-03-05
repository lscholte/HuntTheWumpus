package controller;

import model.Direction;
import model.Game;
import model.maze.MazeGenerationException;
import view.View;


/**
 * The controller is responsible for performing a set of actions on a Game
 * and notifying a View of potential changes in the state of the game.
 * @author Liam Scholte
 *
 */
public class Controller implements GameCreator, Features {
  
  private View view;
  private GameFactory gameFactory;
  
  private Game game;

  /**
   * Constructs a controller connected to the specified view.
   * @param view the view
   * @throws IllegalArgumentException if any arguments are null
   */
  public Controller(View view, GameFactory gameFactory) throws IllegalArgumentException {
    if (view == null) {
      throw new IllegalArgumentException("View must not be null");
    }
    if (gameFactory == null) {
      throw new IllegalArgumentException("Game factory must not be null");
    }
    this.view = view;
    this.gameFactory = gameFactory;
  }
  
  @Override
  public void createGame(
      int rowCount,
      int colCount,
      boolean wraps,
      int batCount,
      int pitCount,
      int playerCount,
      int arrowCount,
      long seed)
      throws IllegalArgumentException, MazeGenerationException {
    game = gameFactory.createGame(
        rowCount,
        colCount,
        wraps,
        batCount,
        pitCount,
        playerCount,
        arrowCount,
        seed);    
    view.presentGame(game, this);    
  }
  
  @Override
  public void move(Direction direction) {
    game.move(direction);
    view.refresh();
  }
  
  @Override
  public void shootArrow(Direction direction, int distance) {
    game.shootArrow(direction, distance);
    view.refresh();
  }
  
  @Override
  public void suicide() {
    game.suicide();    
    view.refresh();
  }

  /**
   * Starts the controller, which instructs the view to
   * present configuration options for the game.
   */
  public void start() {
    view.presentConfiguration(this);
  }
}
