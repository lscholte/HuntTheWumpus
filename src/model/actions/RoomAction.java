package model.actions;

import model.player.ModelPlayer;

/**
 * Represents an action that can be performed on
 * a player.
 * @author Liam Scholte
 *
 */
public interface RoomAction {
  
  /**
   * Performs an action on a {@code Player}.
   * @param player the recipient of this action.
   * @return {@code true} if this action is terminal
   *      (i.e. no other action can be performed),
   *      otherwise {@code false} if any followup actions
   *      may occur
   * @throws IllegalArgumentException is the player is invalid
   */
  public boolean perform(ModelPlayer player) throws IllegalArgumentException;

}
