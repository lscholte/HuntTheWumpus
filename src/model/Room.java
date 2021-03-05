package model;

import model.actions.BatsRoomAction;
import model.actions.EmptyRoomAction;
import model.actions.PitRoomAction;
import model.actions.RoomAction;
import model.actions.WumpusRoomAction;
import model.maze.Maze;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Represents a room within a maze.
 * 
 * @author Liam Scholte
 *
 */
public class Room implements ReadOnlyRoom {

  private final Position position;

  private Map<Direction, Room> roomMap;

  private RoomAction roomAction;

  private boolean hasPit;
  private boolean hasWumpus;
  private boolean hasBats;
  
  private boolean isExplored;

  /**
   * Constructs a room in a maze.
   * 
   * @param position the position of the room
   */
  public Room(Position position) {
    this.position = position;
    
    roomMap = new EnumMap<Direction, Room>(Direction.class);
    roomAction = new EmptyRoomAction();

    hasPit = false;
    hasWumpus = false;
    hasBats = false;    
    
    isExplored = false;
  }

  @Override
  public boolean isHallway() {
    return getExitCount() == 2;
  }
  
  @Override
  public int getExitCount() {
    return (int)roomMap.values().stream().filter(room -> room != null).count();
  }

  @Override
  public boolean isExplored() {
    return isExplored;
  }
  
  /**
   * Marks the room as explored.
   */
  public void explore() {
    isExplored = true;
  }

  @Override
  public Position getPosition() {
    return position;
  }
  
  @Override
  public boolean hasBats() {
    return hasBats;
  }
  
  @Override
  public boolean hasPit() {
    return hasPit;
  }

  @Override
  public boolean hasWumpus() {
    return hasWumpus;
  }
  
  @Override
  public boolean areBatsNearby() {
    return isSomethingNearby(roomToCheck -> roomToCheck.hasBats());
  }
  
  @Override
  public boolean isPitNearby() {
    return isSomethingNearby(roomToCheck -> roomToCheck.hasPit());
  }
  
  @Override
  public boolean isWumpusNearby() {
    return isSomethingNearby(roomToCheck -> roomToCheck.hasWumpus());
  }
  
  private boolean isSomethingNearby(Predicate<ReadOnlyRoom> predicate) {
    if (isHallway()) {
      return false;
    }
    for (Direction direction : getAvailableDirections()) {
      if (predicate.test(getNonHallNeighbour(direction, false).getRoom())) {
        return true;
      }
    }
    return false;
  }
  
  @Override
  public Set<Direction> getAvailableDirections() {
    Set<Direction> availableDirections = EnumSet.noneOf(Direction.class);
    for (Direction direction : Direction.values()) {
      if (getNeighbour(direction) != null) {
        availableDirections.add(direction);
      }
    }
    
    return availableDirections;
  }
  
  @Override
  public ReadOnlyRoom getNeighbour(Direction direction) {
    return getNeighbour(direction, false);
  }

  private Room getNeighbour(Direction direction, boolean shouldVisit) {
    Room neighbour = roomMap.get(direction);
    if (neighbour != null) {
      neighbour.isExplored |= shouldVisit;      
    }
    return neighbour;
  }
  
  @Override
  public Heading getNonHallNeighbour(Direction direction) {
    return getNonHallNeighbour(direction, false);
  }
  
  private MutableHeading getNonHallNeighbour(Direction direction, boolean shouldVisit) {
    Room nextRoom = getNeighbour(direction, shouldVisit);
    while (nextRoom != null && nextRoom.isHallway()) {   
      for (Direction directionToTry : Direction.values()) {
        if (directionToTry == direction.getOpposite()) {
          continue;
        }
        
        Room possibleNextRoom = nextRoom.getNeighbour(directionToTry, shouldVisit);
        if (possibleNextRoom != null) {
          nextRoom = possibleNextRoom;
          direction = directionToTry;
          break;
        }
      }
    }
    
    return new MutableHeading(nextRoom, direction); 
  }
  
  /**
   * Visits the room in the specified direction.
   * This will mark the room as explored.
   * 
   * @param direction the direction of the room
   * @return the room in the specified direction
   */
  public Room visitNeighbour(Direction direction) {
    return getNeighbour(direction, true);
  }
  
  /**
   * Visits the next non-hallway room in the specified direction.
   * This will mark each room along the traversed path as explored.
   * @param direction the direction to search for the next
   *      non-hallway room
   * @return a heading containing the room that was found
   *      and the direction that is currently being travelled
   *      upon entering that room. The room in the heading
   *      may be null if the intial specified direction has no
   *      neighbouring room.
   */
  public MutableHeading visitNonHallNeighbour(Direction direction) {
    return getNonHallNeighbour(direction, true);
  }

  /**
   * Sets the room in the specified direction.
   * 
   * @param direction the direction of the room
   * @param room the room to set
   */
  public void setNeighbour(Direction direction, Room room) {
    if (room == null) {
      Room otherRoom = roomMap.get(direction);
      if (otherRoom == null) {
        return;
      }
      roomMap.put(direction, null);
      otherRoom.roomMap.put(direction.getOpposite(), null);
    }
    else {
      roomMap.put(direction, room);
      room.roomMap.put(direction.getOpposite(), this); 
    }
  }

  /**
   * Gets the action associated with the room.
   * 
   * @return the action associated with the room
   */
  public RoomAction getAction() {
    return roomAction;
  }
  
  /**
   * Adds bats to this room.
   * @param maze the maze of possible rooms that
   *      the bats could teleport a player to
   * @throws IllegalStateException if the room already has bats
   * @throws IllegalArgumentException if the maze is null
   */
  public void addBats(Maze maze) throws IllegalStateException, IllegalArgumentException {
    if (hasBats) {
      throw new IllegalStateException("Room already has bats");
    }
    if (maze == null) {
      throw new IllegalArgumentException("Maze cannot be null");
    }
    hasBats = true;
    roomAction = new BatsRoomAction(maze, new Random(), roomAction);
  }

  /**
   * Adds a pit to this room.
   * @throws IllegalStateException if the room already has a pit
   */
  public void addPit() throws IllegalStateException {
    if (hasPit) {
      throw new IllegalStateException("Room already has a pit");
    }
    hasPit = true;
    roomAction = new PitRoomAction(roomAction);
  }

  /**
   * Adds a wumpus to this room.
   * @throws IllegalStateException if the room already has a wumpus
   */
  public void addWumpus() throws IllegalStateException {
    if (hasWumpus) {
      throw new IllegalStateException("Room already has a wumpus");
    }
    hasWumpus = true;
    roomAction = new WumpusRoomAction(roomAction);
  }
}
