package model.actions;

import model.Direction;
import model.Event;
import model.Room;
import model.actions.EmptyRoomAction;
import model.player.ModelPlayer;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the EmptyRoomAction class.
 * @author Liam Scholte
 *
 */
public class EmptyRoomActionTest {
  
  private class MockPlayer implements ModelPlayer {

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
   * Tests that the room action does nothing and
   * is not a terminal action.
   */
  @Test
  public void testPerformAction() {
    EmptyRoomAction action = new EmptyRoomAction();
    
    ModelPlayer player = new MockPlayer();
    boolean result = action.perform(player);
    
    Assert.assertFalse(result);
  }

}
