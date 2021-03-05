package controller;

import model.Direction;
import model.Game;
import model.ReadOnlyGame;
import model.maze.MazeGenerationException;
import model.maze.ReadOnlyMaze;
import model.player.Player;
import model.player.ReadOnlyPlayer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import view.View;

import java.util.List;

/**
 * Tests the Controller class.
 * @author Liam Scholte
 *
 */
public class ControllerTest {
  
  private class MockView implements View {
    
    int refreshCallCount;
    int presentConfigurationCallCount;
    int presentGameCallCount;
    
    public MockView() {
      refreshCallCount = 0;
      presentConfigurationCallCount = 0;
      presentGameCallCount = 0;
    }

    @Override
    public void refresh() {
      ++refreshCallCount;
    }

    @Override
    public void presentConfiguration(GameCreator gameCreator) {
      ++presentConfigurationCallCount;
    }

    @Override
    public void presentGame(ReadOnlyGame game, Features features) {
      ++presentGameCallCount;
    }
    
  }
  
  private class MockGameFactory implements GameFactory {
    
    private Game game;
    
    public MockGameFactory(Game game) {
      this.game = game;
    }

    @Override
    public Game createGame(
        int rowCount,
        int colCount,
        boolean wraps,
        int batCount,
        int pitCount,
        int playerCount,
        int arrowCount,
        long seed) {
      return game;
    }
    
  }
  
  private class MockGame implements Game {
   
    private int moveCallCount;
    private int shootArrowCallCount;
    private int suicideCallCount;
    
    public MockGame() {
      moveCallCount = 0;
      shootArrowCallCount = 0;
      suicideCallCount = 0;
    }

    @Override
    public boolean isWinnable() {
      return true;
    }

    @Override
    public boolean isOver() {
      return false;
    }

    @Override
    public Player getWinner() throws IllegalStateException {
      return null;
    }

    @Override
    public List<ReadOnlyPlayer> getPlayers() {
      return null;
    }

    @Override
    public ReadOnlyPlayer getCurrentPlayer() {
      return null;
    }

    @Override
    public ReadOnlyMaze getMaze() {
      return null;
    }

    @Override
    public void move(Direction direction) throws IllegalStateException {
      ++moveCallCount;
    }

    @Override
    public void shootArrow(Direction direction, int distance) throws IllegalStateException {
      ++shootArrowCallCount;
    }

    @Override
    public void suicide() throws IllegalStateException {
      ++suicideCallCount;
    }
    
  }

  private MockView view;
  private MockGameFactory gameFactory;
  private MockGame game;
  
  /**
   * Initializes data for testing.
   */
  @Before
  public void setup() {
    game = new MockGame();
    view = new MockView();
    gameFactory = new MockGameFactory(game);
  }
  
  /**
   * Tests that a constructing a Controller with a
   * null view throws an IllegalArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNullView() {
    new Controller(null, gameFactory);
  }
  
  /**
   * Tests that a constructing a Controller with a
   * null game factory throws an IllegalArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNullGameFactory() {
    new Controller(view, null);
  }
  
  /**
   * Tests createGame uses the game factory to create a game.
   */
  @Test
  public void testCreateGame() throws IllegalArgumentException, MazeGenerationException {
    Controller controller = new Controller(view, gameFactory);
    
    controller.createGame(1, 1, true, 0, 0, 1, 1, 12345);
    
    Assert.assertEquals(1, view.presentGameCallCount);
    Assert.assertEquals(0, view.refreshCallCount);
    
    Assert.assertEquals(0, game.moveCallCount);
    Assert.assertEquals(0, game.shootArrowCallCount);
    Assert.assertEquals(0, game.suicideCallCount);
  }
  
  /**
   * Tests that move tells the game to move the current player then refresh the view.
   */
  @Test
  public void testMove() throws IllegalArgumentException, MazeGenerationException {
    Controller controller = new Controller(view, gameFactory);
    controller.createGame(1, 1, true, 0, 0, 1, 1, 12345);

    controller.move(Direction.NORTH);
    
    Assert.assertEquals(1, view.refreshCallCount);
    
    Assert.assertEquals(1, game.moveCallCount);
    Assert.assertEquals(0, game.shootArrowCallCount);
    Assert.assertEquals(0, game.suicideCallCount);
  }
  
  /**
   * Tests that move tells the game to shoot an arrow from the current player
   * then refresh the view.
   */
  @Test
  public void testShootArrow() throws IllegalArgumentException, MazeGenerationException {
    Controller controller = new Controller(view, gameFactory);
    controller.createGame(1, 1, true, 0, 0, 1, 1, 12345);

    controller.shootArrow(Direction.NORTH, 3);
    
    Assert.assertEquals(1, view.refreshCallCount);
    
    Assert.assertEquals(0, game.moveCallCount);
    Assert.assertEquals(1, game.shootArrowCallCount);
    Assert.assertEquals(0, game.suicideCallCount);
  }
  
  /**
   * Tests that move tells the game to kill the current player then refresh the view.
   */
  @Test
  public void testSuicide() throws IllegalArgumentException, MazeGenerationException {
    Controller controller = new Controller(view, gameFactory);
    controller.createGame(1, 1, true, 0, 0, 1, 1, 12345);
    
    controller.suicide();
    
    Assert.assertEquals(1, view.refreshCallCount);
    
    Assert.assertEquals(0, game.moveCallCount);
    Assert.assertEquals(0, game.shootArrowCallCount);
    Assert.assertEquals(1, game.suicideCallCount);
  }
  
  /**
   * Tests that start tells the view to present configuration options.
   */
  @Test
  public void testStart() {
    Controller controller = new Controller(view, gameFactory);
    
    controller.start();
    
    Assert.assertEquals(1, view.presentConfigurationCallCount);
    Assert.assertEquals(0, view.refreshCallCount);
  }
}
