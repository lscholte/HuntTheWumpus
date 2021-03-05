package model.actions;

import model.Direction;
import model.Event;
import model.Room;
import model.maze.Maze;
import model.maze.MazeGenerationException;
import model.maze.MazeImpl;
import model.player.ModelPlayer;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * Tests the BatsRoomAction class.
 * @author Liam Scholte
 *
 */
public class BatsRoomActionTest {
  
  @SuppressWarnings("serial")
  private class RandomMock extends Random {
    
    private boolean next;
    
    public RandomMock(boolean seed) {
      next = seed;
    }
    
    @Override
    public boolean nextBoolean() {
      return next;
    }
  }
  
  private class MockPlayer implements ModelPlayer {
    
    private Event takenByBatsEvent;
    private Event dodgedBatsEvent;
    
    public MockPlayer() {
      takenByBatsEvent = new Event();
      dodgedBatsEvent = new Event();
    }

    @Override
    public Event getFellIntoPitEvent() {
      return null;
    }

    @Override
    public Event getKilledByWumpusEvent() {
      return null;
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
      return null;
    }

    @Override
    public Event getArrowMissedEvent() {
      return null;
    }

    @Override
    public Event getPositionChangedEvent() {
      return null;
    }

    @Override
    public void move(Direction direction) throws IllegalStateException {
      return;
    }

    @Override
    public void shootArrow(Direction direction, int distance)
        throws IllegalArgumentException, IllegalStateException {
      return;
    }

    @Override
    public int getArrowCount() throws IllegalStateException {
      return 0;
    }

    @Override
    public int getMaxShootDistance() {
      return 0;
    }

    @Override
    public void kill() {
      return;
    }

    @Override
    public boolean isAlive() {
      return false;
    }

    @Override
    public void setRoom(Room room) throws IllegalArgumentException, IllegalStateException {
      return;
    }

    @Override
    public Room getRoom() {
      return null;
    }

    @Override
    public String getName() {
      return null;
    }
    
  }
  
  /**
   * Tests that calling perform with a null player
   * throws an IllegalArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testPerformNullPlayer() {
    WumpusRoomAction action = new WumpusRoomAction(new EmptyRoomAction());
    
    action.perform(null);    
  }
  
  /**
   * Tests that a player can dodge bats.
   */
  @Test
  public void testPerformDodgedBats() throws MazeGenerationException {
    Maze maze = new MazeImpl(100, 100, 5, 5, true, new Random(1));
    
    BatsRoomAction action = new BatsRoomAction(maze, new RandomMock(false), new EmptyRoomAction());
    ModelPlayer player = new MockPlayer();
    
    boolean[] eventRaised = new boolean[2];
    player.getTakenByBatsEvent().addListener(() -> eventRaised[0] = true);
    player.getDodgedBatsEvent().addListener(() -> eventRaised[1] = true);
    
    boolean result = action.perform(player);
    
    Assert.assertFalse(result);
    Assert.assertFalse(eventRaised[0]);
    Assert.assertTrue(eventRaised[1]);
  }
  
  /**
   * Tests that a player can be taken by bats.
   */
  @Test
  public void testPerformTakenByBats() throws MazeGenerationException {
    Maze maze = new MazeImpl(100, 100, 5, 5, true, new Random(1));
    
    BatsRoomAction action = new BatsRoomAction(maze, new RandomMock(true), new EmptyRoomAction());
    ModelPlayer player = new MockPlayer();
    
    boolean[] eventRaised = new boolean[2];
    player.getTakenByBatsEvent().addListener(() -> eventRaised[0] = true);
    player.getDodgedBatsEvent().addListener(() -> eventRaised[1] = true);
    
    boolean result = action.perform(player);
    
    Assert.assertTrue(result);
    Assert.assertTrue(eventRaised[0]);
    Assert.assertFalse(eventRaised[1]);
  }

}
