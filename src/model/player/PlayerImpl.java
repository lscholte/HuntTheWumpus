package model.player;

import model.Direction;
import model.Event;
import model.Heading;
import model.ReadOnlyRoom;
import model.Room;

/**
 * Represents a player that can move
 * within a maze.
 * @author Liam Scholte
 *
 */
public class PlayerImpl implements ModelPlayer {
  
  private final Event fellIntoPitEvent;
  private final Event killedByWumpusEvent;
  private final Event takenByBatsEvent;
  private final Event dodgedBatsEvent;
  private final Event killedWumpusEvent;
  private final Event arrowMissedEvent;
  private final Event positionChangedEvent;
  
  //Player can never end up in a hallway
  private Room room;
  
  private int arrowCount;
  
  private boolean isAlive;
  
  private String name;
  
  
  /**
   * Constructs a player that can traverse a maze.
   * @param initialRoom the initial room of the player
   * @param arrowCount the number of arrows the player starts with
   * @throws IllegalArgumentException if the initial room
   *      is null
   */
  public PlayerImpl(String name, Room initialRoom, int arrowCount) throws IllegalArgumentException {
    if (initialRoom == null) {
      throw new IllegalArgumentException(
          "Initial room cannot be null");
    }
    if (initialRoom.isHallway()) {
      throw new IllegalArgumentException(
          "Player cannot be placed in a hallway");
    }
    
    if (arrowCount < 1) {
      throw new IllegalArgumentException(
          "Player must start with at least 1 arrow");
    }
    
    this.name = name;
    
    fellIntoPitEvent = new Event();
    killedByWumpusEvent = new Event();
    takenByBatsEvent = new Event();
    dodgedBatsEvent = new Event();
    killedWumpusEvent = new Event();
    arrowMissedEvent = new Event();
    positionChangedEvent = new Event();
    
    room = initialRoom;
    this.arrowCount = arrowCount;
    isAlive = true;
  }
  
  /**
   * Gets an event that is raised when the player
   * falls into a pit and dies.
   * @return an event
   */
  @Override
  public Event getFellIntoPitEvent() {
    return fellIntoPitEvent;
  }
  
  @Override
  public Event getKilledByWumpusEvent() {
    return killedByWumpusEvent;
  }
  
  @Override
  public Event getTakenByBatsEvent() {
    return takenByBatsEvent;
  }
  
  @Override
  public Event getDodgedBatsEvent() {
    return dodgedBatsEvent;
  }
  
  @Override
  public Event getKilledWumpusEvent() {
    return killedWumpusEvent;
  }
  
  @Override
  public Event getArrowMissedEvent() {
    return arrowMissedEvent;
  }
  
  @Override
  public Event getPositionChangedEvent() {
    return positionChangedEvent;
  }
  
  @Override
  public void shootArrow(Direction direction, int distance)
      throws IllegalArgumentException, IllegalStateException {
    if (arrowCount <= 0) {
      throw new IllegalStateException(
          "Player does not have any arrows");
    }
    if (distance < 1) {
      throw new IllegalArgumentException(
          "The arrow must travel at least 1 room");
    }
    
    int maxDistance = getMaxShootDistance();
    if (distance > maxDistance) {
      throw new IllegalArgumentException(
          String.format(
              "The arrow cannot be shot farther than %d rooms",
              maxDistance));
    }
    
    --arrowCount;
    
    ReadOnlyRoom nextRoom = room;
    while (distance > 0) {
      
      Heading heading = nextRoom.getNonHallNeighbour(direction);
      nextRoom = heading.getRoom();
      direction = heading.getDirection();
      if (nextRoom == null) {
        arrowMissedEvent.raise();
        return;
      }
      --distance;
    }
    
    if (nextRoom.hasWumpus()) {
      killedWumpusEvent.raise();
      return;
    }
    arrowMissedEvent.raise();
  }
  
  @Override
  public int getArrowCount() {
    return arrowCount;
  }
  
  @Override
  public int getMaxShootDistance() {
    return 5;
  }
  
  @Override
  public void move(Direction direction) throws IllegalStateException {
    if (!isAlive) {
      throw new IllegalStateException("The player is dead");
    }
    
    Room destinationRoom = room.visitNonHallNeighbour(direction).getMutableRoom();
    if (destinationRoom == null) {
      throw new IllegalStateException(
          "Unable to move to the " + direction.toString().toLowerCase());
    }
    room = destinationRoom;
    room.getAction().perform(this);
    positionChangedEvent.raise();
  }
  
  @Override
  public void setRoom(Room room) throws IllegalArgumentException, IllegalStateException {
    if (!isAlive) {
      throw new IllegalStateException("The player is dead");
    }
    if (room == null) {
      throw new IllegalArgumentException("Room cannot be null");
    }
    if (room.isHallway()) {
      throw new IllegalArgumentException("The room cannot be a hallway");
    }
    room.explore();
    this.room = room;
    this.room.getAction().perform(this);
  }
  
  @Override
  public Room getRoom() {
    return room;
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
  public String getName() {
    return name;
  }
}
