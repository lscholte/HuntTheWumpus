package view.text;

import controller.Features;
import controller.GameCreator;
import model.Direction;
import model.Event;
import model.Heading;
import model.Position;
import model.ReadOnlyGame;
import model.ReadOnlyRoom;
import model.maze.MazeGenerationException;
import model.maze.ReadOnlyMaze;
import model.player.Player;
import model.player.ReadOnlyPlayer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Tests the TextView class.
 * @author Liam Scholte
 *
 */
public class TextViewTest {
  
  private class MockGameCreator implements GameCreator {
    
    private int createGameCallCount;
    
    public MockGameCreator() {
      createGameCallCount = 0;
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
      ++createGameCallCount;
    }
  }
  
  private class MockGameCreatorThatThrowsIllegalArgumentException implements GameCreator {
    
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
      throw new IllegalArgumentException("Failed to generate a maze because the test case said so");
    }
  }
  
  private class MockGameCreatorThatThrowsMazeGenerationException implements GameCreator {
    
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
      throw new MazeGenerationException("Failed to generate a maze because the test case said so");
    }
  }
  
  private class MockRoom implements ReadOnlyRoom {
    
    boolean pitNearby;
    boolean wumpusNearby;
    
    public MockRoom(boolean pitNearby, boolean wumpusNearby) {
      this.pitNearby = pitNearby;
      this.wumpusNearby = wumpusNearby;
    }

    @Override
    public boolean isHallway() {
      return false;
    }

    @Override
    public int getExitCount() {
      return 3;
    }

    @Override
    public boolean isExplored() {
      return true;
    }

    @Override
    public Position getPosition() {
      return new Position(1, 2);
    }

    @Override
    public boolean hasBats() {
      return false;
    }

    @Override
    public boolean hasPit() {
      return false;
    }

    @Override
    public boolean hasWumpus() {
      return false;
    }

    @Override
    public boolean areBatsNearby() {
      return true;
    }

    @Override
    public boolean isPitNearby() {
      return pitNearby;
    }

    @Override
    public boolean isWumpusNearby() {
      return wumpusNearby;
    }

    @Override
    public Set<Direction> getAvailableDirections() {
      return EnumSet.of(Direction.NORTH, Direction.EAST, Direction.WEST);
    }

    @Override
    public ReadOnlyRoom getNeighbour(Direction direction) {
      return null;
    }

    @Override
    public Heading getNonHallNeighbour(Direction direction) {
      return null;
    }
    
  }
  
  private class MockPlayer implements ReadOnlyPlayer {
    
    private Event fellIntoPitEvent;
    private Event killedByWumpusEvent;
    private Event takenByBatsEvent;
    private Event dodgedBatsEvent;
    private Event killedWumpusEvent;
    private Event arrowMissedEvent;
    private Event positionChangedEvent;
    
    public MockPlayer() {
      fellIntoPitEvent = new Event();
      killedByWumpusEvent = new Event();
      takenByBatsEvent = new Event();
      dodgedBatsEvent = new Event();
      killedWumpusEvent = new Event();
      arrowMissedEvent = new Event();
      positionChangedEvent = new Event();
    }

    @Override
    public Event getFellIntoPitEvent() {
      return fellIntoPitEvent;
    }

    @Override
    public Event getKilledByWumpusEvent() {
      return killedByWumpusEvent;
    }

    @Override
    public Event getTakenByBatsEvent() {
      return takenByBatsEvent;
    }

    @Override
    public Event getDodgedBatsEvent() {
      return dodgedBatsEvent;
    }

    @Override
    public Event getKilledWumpusEvent() {
      return killedWumpusEvent;
    }

    @Override
    public Event getArrowMissedEvent() {
      return arrowMissedEvent;
    }

    @Override
    public Event getPositionChangedEvent() {
      return positionChangedEvent;
    }

    @Override
    public int getArrowCount() {
      return 3;
    }

    @Override
    public int getMaxShootDistance() {
      return 5;
    }

    @Override
    public boolean isAlive() {
      return true;
    }

    @Override
    public ReadOnlyRoom getRoom() {
      return new MockRoom(true, true);
    }

    @Override
    public String getName() {
      return "Player 1";
    }
    
  }
  
  private class MockGame implements ReadOnlyGame {

    private List<ReadOnlyPlayer> players;
    
    public MockGame() {
      players = new ArrayList<ReadOnlyPlayer>();
      players.add(new MockPlayer());
    }
    
    @Override
    public boolean isWinnable() throws IllegalStateException {
      return false;
    }

    @Override
    public boolean isOver() throws IllegalStateException {
      return false;
    }

    @Override
    public Player getWinner() throws IllegalStateException {
      return null;
    }

    @Override
    public List<ReadOnlyPlayer> getPlayers() {
      return players;
    }

    @Override
    public ReadOnlyPlayer getCurrentPlayer() {
      return players.get(0);
    }

    @Override
    public ReadOnlyMaze getMaze() {
      return null;
    }
  }
  
  private class MockFeatures implements Features {

    @Override
    public void move(Direction direction) {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void shootArrow(Direction direction, int distance) {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void suicide() {
      // TODO Auto-generated method stub
      
    }
    
  }
  
  private Readable input;
  private Appendable output;
  
  /**
   * Initializes data for testing.
   */
  @Before
  public void setup() {
    input = new StringReader("");
    output = new StringWriter();
  }
  
  /**
   * Tests that a TextView cannot be constructed with a null input.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNullInput() {
    new TextView(null, output);
  }
  
  /**
   * Tests that a TextView cannot be constructed with a null output.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNullOutput() {
    new TextView(input, null);
  }
  
  /**
   * Tests that when all questions are successfully answered, the view
   * passes the information to the controller to create a game.
   */
  @Test
  public void testPresentConfiguration() {
    final String expectedOutput =
        "Enter the number of rows" + System.lineSeparator()
        + "Enter the number of columns" + System.lineSeparator()
        + "Does the maze wrap? (yes/no)" + System.lineSeparator()
        + "Enter the number of bats" + System.lineSeparator()
        + "Enter the number of pits" + System.lineSeparator()
        + "Enter the number of players" + System.lineSeparator()
        + "Enter the number of arrows per player" + System.lineSeparator()
        + "Enter the random generation seed" + System.lineSeparator();

    
    final String inputText = "5 5 yes 2 3 1 1 12345";
    input = new StringReader(inputText);
    
    TextView view = new TextView(input, output);
    
    MockGameCreator gameCreator = new MockGameCreator();
    view.presentConfiguration(gameCreator);
    
    Assert.assertEquals(expectedOutput, output.toString());
    Assert.assertEquals(1, gameCreator.createGameCallCount);
  }
  
  /**
   * Tests that if the input stream runs out then the view does not continue
   * prompting questions.
   */
  @Test
  public void testPresentConfigurationInputRunsOut() {
    final String expectedOutput =
        "Enter the number of rows" + System.lineSeparator()
        + "Enter the number of columns" + System.lineSeparator()
        + "Does the maze wrap? (yes/no)" + System.lineSeparator()
        + "Enter the number of bats" + System.lineSeparator()
        + "Input ran out" + System.lineSeparator();

    
    final String inputText = "5 5 yes";
    input = new StringReader(inputText);
    
    TextView view = new TextView(input, output);
    
    MockGameCreator gameCreator = new MockGameCreator();
    view.presentConfiguration(gameCreator);
    
    Assert.assertEquals(expectedOutput, output.toString());
    Assert.assertEquals(0, gameCreator.createGameCallCount);
  }
  
  /**
   * Tests that entering invalid inputs causes the question to be repeated.
   */
  @Test
  public void testPresentConfigurationInvalidInputRepeatsQuestion() {
    final String expectedOutput =
        "Enter the number of rows" + System.lineSeparator()
        + "Enter the number of columns" + System.lineSeparator()
        + "Invalid input" + System.lineSeparator()
        + "Enter the number of columns" + System.lineSeparator()
        + "Does the maze wrap? (yes/no)" + System.lineSeparator()
        + "Invalid input" + System.lineSeparator()
        + "Does the maze wrap? (yes/no)" + System.lineSeparator()
        + "Enter the number of bats" + System.lineSeparator()
        + "Input ran out" + System.lineSeparator();

    
    final String inputText = "5 bogus 5 maybe yes";
    input = new StringReader(inputText);
    
    TextView view = new TextView(input, output);
    
    MockGameCreator gameCreator = new MockGameCreator();
    view.presentConfiguration(gameCreator);
    
    Assert.assertEquals(expectedOutput, output.toString());
    Assert.assertEquals(0, gameCreator.createGameCallCount);
  }
  
  /**
   * Tests that when all questions are successfully answered, the view
   * passes the information to the controller to create a game. When there is
   * an IllegalArgumentException, the exception's message is displayed and then the
   * configuration is repeated.
   */
  @Test
  public void testPresentConfigurationGameCreationThrowsIllegalArgumentException() {
    final String expectedOutput =
        "Enter the number of rows" + System.lineSeparator()
        + "Enter the number of columns" + System.lineSeparator()
        + "Does the maze wrap? (yes/no)" + System.lineSeparator()
        + "Enter the number of bats" + System.lineSeparator()
        + "Enter the number of pits" + System.lineSeparator()
        + "Enter the number of players" + System.lineSeparator()
        + "Enter the number of arrows per player" + System.lineSeparator()
        + "Enter the random generation seed" + System.lineSeparator()
        + "Failed to create the game." + System.lineSeparator()
        + "Failed to generate a maze because the test case said so" + System.lineSeparator()
        + "Enter the number of rows" + System.lineSeparator()
        + "Input ran out" + System.lineSeparator();

    
    final String inputText = "5 5 yes 2 3 1 1 12345";
    input = new StringReader(inputText);
    
    TextView view = new TextView(input, output);
    
    MockGameCreatorThatThrowsIllegalArgumentException gameCreator =
        new MockGameCreatorThatThrowsIllegalArgumentException();
    view.presentConfiguration(gameCreator);
    
    Assert.assertEquals(expectedOutput, output.toString());
  }
  
  /**
   * Tests that when all questions are successfully answered, the view
   * passes the information to the controller to create a game. When there is
   * a MazeGenerationException, a particular message pops up and then the configuration
   * is repeated.
   */
  @Test
  public void testPresentConfigurationGameCreationThrowsMazeGenerationException() {
    final String expectedOutput =
        "Enter the number of rows" + System.lineSeparator()
        + "Enter the number of columns" + System.lineSeparator()
        + "Does the maze wrap? (yes/no)" + System.lineSeparator()
        + "Enter the number of bats" + System.lineSeparator()
        + "Enter the number of pits" + System.lineSeparator()
        + "Enter the number of players" + System.lineSeparator()
        + "Enter the number of arrows per player" + System.lineSeparator()
        + "Enter the random generation seed" + System.lineSeparator()
        + "A random maze could not be generated." + System.lineSeparator()
        + "Try again or consider specifying larger maze dimensions and/or fewer pits or bats."
        + System.lineSeparator()
        + "Enter the number of rows" + System.lineSeparator()
        + "Input ran out" + System.lineSeparator();

    
    final String inputText = "5 5 yes 2 3 1 1 12345";
    input = new StringReader(inputText);
    
    TextView view = new TextView(input, output);
    
    MockGameCreatorThatThrowsMazeGenerationException gameCreator =
        new MockGameCreatorThatThrowsMazeGenerationException();
    view.presentConfiguration(gameCreator);
    
    Assert.assertEquals(expectedOutput, output.toString());
  }

  /**
   * Tests that the game presents the correct text UI.
   */
  @Test
  public void testPresentGame() {
    final String expectedOutput =
        "Starting Hunt the Wumpus..." + System.lineSeparator()
        + System.lineSeparator()
        + "Current Player: Player 1" + System.lineSeparator()
        + "Player 1 is in cave at position (1, 2)" + System.lineSeparator()
        + "Player 1 has 3 arrows remaining" + System.lineSeparator()
        + "Player 1 smells a wumpus nearby" + System.lineSeparator()
        + "Player 1 feels the breeze of a pit nearby" + System.lineSeparator()
        + "Available directions: north, east, west" + System.lineSeparator()
        + "Input ran out" + System.lineSeparator()
        + "The game is unfinished" + System.lineSeparator();
    
    TextView view = new TextView(input, output);
    view.presentGame(new MockGame(), new MockFeatures());
    
    Assert.assertEquals(expectedOutput, output.toString());
  }
  
  /**
   * Tests that entering an invalid direction displays an error.
   */
  @Test
  public void testPresentGameMoveInvalidDirection() {
    final String expectedOutput =
        "Starting Hunt the Wumpus..." + System.lineSeparator()
        + System.lineSeparator()
        + "Current Player: Player 1" + System.lineSeparator()
        + "Player 1 is in cave at position (1, 2)" + System.lineSeparator()
        + "Player 1 has 3 arrows remaining" + System.lineSeparator()
        + "Player 1 smells a wumpus nearby" + System.lineSeparator()
        + "Player 1 feels the breeze of a pit nearby" + System.lineSeparator()
        + "Available directions: north, east, west" + System.lineSeparator()
        + "'bogus' is not a direction" + System.lineSeparator()
        + System.lineSeparator()
        + "Current Player: Player 1" + System.lineSeparator()
        + "Player 1 is in cave at position (1, 2)" + System.lineSeparator()
        + "Player 1 has 3 arrows remaining" + System.lineSeparator()
        + "Player 1 smells a wumpus nearby" + System.lineSeparator()
        + "Player 1 feels the breeze of a pit nearby" + System.lineSeparator()
        + "Available directions: north, east, west" + System.lineSeparator()
        + "Input ran out" + System.lineSeparator()
        + "The game is unfinished" + System.lineSeparator();
    
    final String inputText = "move bogus";
    input = new StringReader(inputText);
    
    TextView view = new TextView(input, output);
    view.presentGame(new MockGame(), new MockFeatures());
    
    Assert.assertEquals(expectedOutput, output.toString());
  }
  
  /**
   * Tests that entering an unrecognized command displays an error.
   */
  @Test
  public void testPresentGameUnrecognizedCommand() {
    final String expectedOutput =
        "Starting Hunt the Wumpus..." + System.lineSeparator()
        + System.lineSeparator()
        + "Current Player: Player 1" + System.lineSeparator()
        + "Player 1 is in cave at position (1, 2)" + System.lineSeparator()
        + "Player 1 has 3 arrows remaining" + System.lineSeparator()
        + "Player 1 smells a wumpus nearby" + System.lineSeparator()
        + "Player 1 feels the breeze of a pit nearby" + System.lineSeparator()
        + "Available directions: north, east, west" + System.lineSeparator()
        + "Unrecognized command" + System.lineSeparator()
        + System.lineSeparator()
        + "Current Player: Player 1" + System.lineSeparator()
        + "Player 1 is in cave at position (1, 2)" + System.lineSeparator()
        + "Player 1 has 3 arrows remaining" + System.lineSeparator()
        + "Player 1 smells a wumpus nearby" + System.lineSeparator()
        + "Player 1 feels the breeze of a pit nearby" + System.lineSeparator()
        + "Available directions: north, east, west" + System.lineSeparator()
        + "Input ran out" + System.lineSeparator()
        + "The game is unfinished" + System.lineSeparator();
    
    final String inputText = "garbage";
    input = new StringReader(inputText);
    
    TextView view = new TextView(input, output);
    view.presentGame(new MockGame(), new MockFeatures());
    
    Assert.assertEquals(expectedOutput, output.toString());
  }
}
