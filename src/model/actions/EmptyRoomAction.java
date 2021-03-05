package model.actions;

import model.player.ModelPlayer;

/**
 * Represents an action that does nothing.
 * @author Liam Scholte
 *
 */
public class EmptyRoomAction implements RoomAction {

  @Override
  public boolean perform(ModelPlayer player) {
    //Do nothing
    return false;
  }

}
