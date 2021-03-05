package model.actions;

import model.player.ModelPlayer;

/**
 * Represents an action that is performed on a
 * player entering a room with a wumpus.
 * @author Liam Scholte
 *
 */
public class WumpusRoomAction extends CompositeRoomAction {
    
  /**
   * Constructs a wumpus room action.
   * @param priorAction the action to perform
   *      prior to this room action
   */
  public WumpusRoomAction(RoomAction priorAction) {
    super(priorAction);
  }

  @Override
  public boolean perform(ModelPlayer player) {
    if (super.perform(player)) {
      return true;
    }
    player.kill();
    player.getKilledByWumpusEvent().raise();
    return true;
  }

}
