package model.actions;

import model.Room;
import model.maze.Maze;
import model.player.ModelPlayer;

import java.util.List;
import java.util.Random;

/**
 * Represents an action that is performed on a
 * player entering a room with bats.
 * @author Liam Scholte
 *
 */
public class BatsRoomAction extends CompositeRoomAction {
  
  private final Random random;
  private final Maze maze;
  
  /**
   * Constructs a bats room action.
   * @param maze The maze of rooms where the bats
   *      may transport a player to
   * @param random a random number generator
   * @param priorAction the action to perform before
   *      this action
   */
  public BatsRoomAction(Maze maze, Random random, RoomAction priorAction) {
    super(priorAction);    
    this.random = random;
    this.maze = maze;
  }

  @Override
  public boolean perform(ModelPlayer player) throws IllegalArgumentException {
    if (super.perform(player)) {
      return true;
    }
    
    if (random.nextBoolean()) {
      player.getTakenByBatsEvent().raise();
      
      List<Room> rooms = maze.getMutableRooms();
      Room destinationRoom = rooms.get(random.nextInt(rooms.size()));
      player.setRoom(destinationRoom);
      return true;
    }
    player.getDodgedBatsEvent().raise();
    return false;
  }

}
