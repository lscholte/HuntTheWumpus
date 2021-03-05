package model.actions;

import model.Direction;
import model.Event;
import model.Room;
import model.player.ModelPlayer;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the WumpusRoomAction class.
 * @author Liam Scholte
 *
 */
public class WumpusRoomActionTest {
  
  private class MockPlayer implements ModelPlayer {
    
    private boolean isAlive;
    private Event event;
    
    public MockPlayer() {
      isAlive = true;
      event = new Event();
    }

    @Override
    public Event getFellIntoPitEvent() {
      return null;
    }

    @Override
    public Event getKilledByWumpusEvent() {
      return event;
    }

    @Override
    public Event getTakenByBatsEvent() {
      return null;
    }

    @Override
    public Event getDodgedBatsEvent() {
      return null;
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
      isAlive = false;
    }

    @Override
    public boolean isAlive() {
      return isAlive;
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
   * Tests that the room action kills the player.
   */
  @Test
  public void testPerform() {
    WumpusRoomAction action = new WumpusRoomAction(new EmptyRoomAction());
    ModelPlayer player = new MockPlayer();
    
    boolean[] eventRaised = new boolean[1];
    player.getKilledByWumpusEvent().addListener(() -> eventRaised[0] = true);
    
    boolean result = action.perform(player);
    
    Assert.assertTrue(result);
    Assert.assertFalse(player.isAlive());
    Assert.assertTrue(eventRaised[0]);
  }

}
