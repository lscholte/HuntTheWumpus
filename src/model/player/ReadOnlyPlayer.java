package model.player;

import model.Event;
import model.ReadOnlyRoom;


/**
 * Represents an unmodifiable player
 * in a Hunt the Wumpus game. The player's state
 * will not change by invoking any methods
 * in the ReadOnlyPlayer.
 * @author Liam Scholte
 *
 */
public interface ReadOnlyPlayer {
  
  /**
   * Gets an event that is raised when the player
   * falls into a pit and dies.
   * @return an event
   */
  public Event getFellIntoPitEvent();
  
  /**
   * Gets an event that is raised when the player
   * falls is killed by a wumpus.
   * @return an event
   */
  public Event getKilledByWumpusEvent();
  
  /**
   * Gets an event that is raised when the player
   * is taken by bats.
   * @return an event
   */
  public Event getTakenByBatsEvent();
  
  /**
   * Gets an event that is raised when the player
   * dodges bats attempting to take the player.
   * @return an event
   */
  public Event getDodgedBatsEvent();
  
  /**
   * Gets an event that is raised when the player
   * has killed a wumpus.
   * @return an event
   */
  public Event getKilledWumpusEvent();
  
  /**
   * Gets an event that is raised when the player's
   * arrow has missed a wumpus.
   * @return an event
   */
  public Event getArrowMissedEvent();
  
  /**
   * Gets an event that is raised when the player's
   * position changes.
   * @return an event
   */
  public Event getPositionChangedEvent();
  
  /**
   * Gets the number of arrows the shooter has remaining.
   * @return the number of arrows
   */
  public int getArrowCount();
  
  /**
   * Gets the farthest number of rooms this entity
   * can shoot arrows.
   * @return the farthest number of rooms that
   *      an arrow can be shot
   */
  public int getMaxShootDistance();
  
  /**
   * Determines whether or not the player is still alive.
   * @return {@code true} if the player is alive,
   *      {@code false} otherwise
   */
  public boolean isAlive();
  
  /**
   * Gets the current room of the player.
   * @return the current room of the player
   */
  public ReadOnlyRoom getRoom();
  
  /**
   * Gets the name of the player.
   * @return the name of the player
   */
  public String getName();

}
