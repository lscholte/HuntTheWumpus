package model.actions;

import model.player.ModelPlayer;

/**
 * Represents a room action that itself
 * can invoke another room action.
 * @author Liam Scholte
 *
 */
public class CompositeRoomAction implements RoomAction {
  
  private final RoomAction priorAction;
  
  /**
   * Constructs a composite room action.
   * @param priorAction the other action to perform
   */
  public CompositeRoomAction(RoomAction priorAction) {
    this.priorAction = priorAction;
  }

  @Override
  public boolean perform(ModelPlayer player) throws IllegalArgumentException {
    if (player == null) {
      throw new IllegalArgumentException(
          "Player must not be null");
    }
    return priorAction.perform(player);
  }
}
