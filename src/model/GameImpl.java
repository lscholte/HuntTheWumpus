package model;

import model.maze.ReadOnlyMaze;
import model.player.ModelPlayer;
import model.player.Player;
import model.player.ReadOnlyPlayer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * An implementation of the Hunt the Wumpus game that
 * supports exactly 1 wumpus, an arbitrary number of pits,
 * and an arbitrary number of bats.
 * @author Liam Scholte
 *
 */
public class GameImpl implements Game {
    
  private Player winner;
  
  private Queue<ModelPlayer> playerQueue;
  private List<Player> allPlayers;
  
  private ReadOnlyMaze maze;
    
  //Currently only 1 wumpus is allowed.
  //Allowing more introduces extra complications such as
  // * Determing if game is winnable. There is a corner case where
  //   the game may only be winnable if wumpuses are killed in certain order
  // * Need to allow wumpus to be removed from the game and that the game
  //   will continue if there are more wumpuses remaining
  private boolean wumpusAlive;
  
  /**
   * Constructs a game of Hunt the Wumpus.
   * @param maze the maze of rooms to use for the game
   * @param players the players in the game
   *      to randomly assign the bats, pits, and wumpus
   *      to rooms in the maze
   * @throws IllegalArgumentException if either the maze or player is null
   */
  public GameImpl(ReadOnlyMaze maze, List<ModelPlayer> players)
      throws IllegalArgumentException {
    if (maze == null) {
      throw new IllegalArgumentException("Maze must not be null");
    }
    if (players == null) {
      throw new IllegalArgumentException("Player must not be null");
    }
        
    playerQueue = new LinkedList<ModelPlayer>(players);
    allPlayers = new ArrayList<Player>(players);
    this.maze = maze;    
    wumpusAlive = true;

    //Invoked when a player kills the wumpus.
    //This is used to determine the winner of the game
    playerQueue.forEach(p -> p.getKilledWumpusEvent().addListener(() -> {
      winner = p;
      wumpusAlive = false;
    }));
    
    playerQueue.forEach(player -> player.setRoom(player.getRoom()));
  }
  
  @Override
  public boolean isWinnable() throws IllegalStateException {
    ModelPlayer player = playerQueue.peek();
    if (isOver()) {
      player.equals(getWinner());
    }
   
    if (!player.isAlive() || player.getArrowCount() <= 0) {
      return false;
    }
    
    //With multiple wumpus, there is technically an edge case where a game is winnable
    //only if wumpuses are killed in a specific order to free up a space
    //for the player to stand safely
    
    ReadOnlyRoom wumpusRoom =
        maze
            .getRooms()
            .stream()
            .filter(room -> room.hasWumpus())
            .findFirst()
            .get();
    
    //Find all locations in which a player could safely stand and shoot an arrow to kill the wumpus
    Set<ReadOnlyRoom> shootableRooms = new HashSet<ReadOnlyRoom>();
    getAllRoomsShootable(shootableRooms, wumpusRoom);
      
    //Find all locations accessible from player's current position
    //(accessible superbat makes every room accessible)
    Set<ReadOnlyRoom> accessibleRoomsByPlayer = new HashSet<ReadOnlyRoom>();
    getAllRoomsAccessibleToPlayer(accessibleRoomsByPlayer, player.getRoom());
      
    //Check if there is any room that the player can access AND shoot the wumpus from
    accessibleRoomsByPlayer.retainAll(shootableRooms);
    return !accessibleRoomsByPlayer.isEmpty();
  }
  
  @Override
  public boolean isOver() throws IllegalStateException {
    boolean anyPlayersThatCanKill =
        playerQueue
            .stream()
            .anyMatch(player -> player.isAlive() && player.getArrowCount() > 0);
    return !wumpusAlive || !anyPlayersThatCanKill;
  }
  
  @Override
  public Player getWinner() {
    if (!isOver()) {
      throw new IllegalStateException(
          "The game is not over yet");
    }
    return winner;
  }
  
  @Override
  public List<ReadOnlyPlayer> getPlayers() {
    return new ArrayList<ReadOnlyPlayer>(allPlayers);
  }
  
  @Override
  public ReadOnlyPlayer getCurrentPlayer() {
    return playerQueue.peek();
  }
  
  @Override
  public ReadOnlyMaze getMaze() {
    return maze;
  }

  @Override
  public void move(Direction direction) {
    if (isOver()) {
      throw new IllegalStateException("The game is over");
    }
    getNextAlivePlayer().move(direction);
    playerQueue.add(playerQueue.remove());
  }

  @Override
  public void shootArrow(Direction direction, int distance) {
    if (isOver()) {
      throw new IllegalStateException("The game is over");
    }
    getNextAlivePlayer().shootArrow(direction, distance);
    playerQueue.add(playerQueue.remove());
  }
  
  @Override
  public void suicide() {
    if (isOver()) {
      throw new IllegalStateException("The game is over");
    }
    getNextAlivePlayer().kill();
    playerQueue.remove();
  }
  
  private ModelPlayer getNextAlivePlayer() {
    while (!playerQueue.isEmpty()) {
      ModelPlayer player = playerQueue.peek();
      if (player.isAlive()) {
        return player;
      }
      playerQueue.remove();
    }
    
    //Expectation here is that methods calling this should never reach this because
    //this implies that the game is over
    return null;
  }
  
  private void getAllRoomsAccessibleToPlayer(
      Set<ReadOnlyRoom> accessibleRooms,
      ReadOnlyRoom startingRoom) {    
    accessibleRooms.add(startingRoom);    
    for (Direction direction : Direction.values()) {
      ReadOnlyRoom roomToCheck = startingRoom.getNonHallNeighbour(direction).getRoom();
      if (roomToCheck != null) {
        if (!accessibleRooms.contains(roomToCheck)) {
          if (roomToCheck.hasBats()) {
            //Everything is accessible. Early out
            accessibleRooms.addAll(maze.getRooms());
            return;
          }
          else if (!roomToCheck.hasWumpus() && !roomToCheck.hasPit()) {
            getAllRoomsAccessibleToPlayer(accessibleRooms, roomToCheck);            
          }
        }
      }
    }
  }
  
  private void getAllRoomsShootable(
      Set<ReadOnlyRoom> shootableRooms,
      ReadOnlyRoom startingRoom) {
    for (Direction initialDirection : Direction.values()) {
      Direction direction = initialDirection;
      ReadOnlyRoom room = startingRoom;
      for (int i = 0; i < playerQueue.peek().getMaxShootDistance(); ++i) {
        Heading heading = room.getNonHallNeighbour(direction);
        room = heading.getRoom();
        direction = heading.getDirection();
        
        if (room == null) {
          break;
        }
        
        if (!room.hasPit() && !room.hasWumpus()) {
          shootableRooms.add(room);          
        }
      }

    }
  }
}
