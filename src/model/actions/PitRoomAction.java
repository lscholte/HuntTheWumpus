package model.actions;

import model.player.ModelPlayer;

/**
 * Represents an action that is performed on a
 * player entering a room with a pit.
 * @author Liam Scholte
 *
 */
public class PitRoomAction extends CompositeRoomAction {
    
  /**
   * Constructs a pit room action.
   * @param priorAction the action to perform
   *      prior to this room action
   */
  public PitRoomAction(RoomAction priorAction) {
    super(priorAction);
  }
  
  @Override
  public boolean perform(ModelPlayer player) {
    if (super.perform(player)) {
      return true;
    }
    player.kill();
    player.getFellIntoPitEvent().raise();
    return true;
  }

}
