package model.player;

import model.Room;

/**
 * Represents a Player with functionality intended
 * to be used solely within the Hunt the Wumpus model.
 * @author Liam Scholte
 *
 */
public interface ModelPlayer extends Player {

  /**
   * Gets the player's current room.
   * @return the current room
   */
  public Room getRoom();
  
  /**
   * Moves the player to a specified room.
   * @param room the room that the player will
   *      be moved to.
   * @throws IllegalArgumentException if the room is null or a hallway
   * @throws IllegalStateException if the player cannot teleport
   */
  public void setRoom(Room room) throws IllegalArgumentException, IllegalStateException;
    
}