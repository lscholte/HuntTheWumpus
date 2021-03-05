package controller;

import model.Game;
import model.GameImpl;
import model.Room;
import model.maze.Maze;
import model.maze.MazeGenerationException;
import model.maze.MazeImpl;
import model.player.ModelPlayer;
import model.player.PlayerImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * A factory that creates instances of the Hunt the Wumpus game and positions
 * the players in safe room if available.
 * @author Liam Scholte
 *
 */
public class GameFactoryImpl implements GameFactory {
  
  @Override
  public Game createGame(
      int rowCount,
      int colCount,
      boolean wraps,
      int batCount,
      int pitCount,
      int playerCount,
      int arrowCount,
      long seed)
      throws IllegalArgumentException, MazeGenerationException {
    Random random = new Random(seed);
    Maze maze = new MazeImpl(rowCount, colCount, batCount, pitCount, wraps, random);
    
    List<ModelPlayer> players = new ArrayList<ModelPlayer>();
    for (int i = 1; i <= playerCount; ++i) {
      players.add(
          new PlayerImpl(
              "Player " + i,
              getStartingRoom(maze.getMutableRooms()),
              arrowCount));        
    }      
    return new GameImpl(maze, players);
  }
  
  /**
   * Returns a room for the player to start in. Preference is
   * given to rooms that have no wumpus, pits, nor bats. If no
   * such room exists, then preference is given to rooms with
   * neither a wumpus nor bats. If no such room exists, then
   * any room may be selected effectively guaranteeing the player's
   * death due to the hazard in the room.
   * @param allRooms all possible rooms to pick from
   * @return a safe room if one exists, otherwise any room
   * @throws IllegalArgumentException if the list of rooms is null or empty
   */
  public static Room getStartingRoom(List<Room> allRooms) throws IllegalArgumentException {
    if (allRooms == null || allRooms.isEmpty()) {
      throw new IllegalArgumentException();
    }
    
    List<Room> hazardFreeRooms = 
        allRooms
            .stream()
            .filter(room -> !room.hasWumpus() && !room.hasPit())
            .collect(Collectors.toList());
    
    Optional<Room> possibleStartRoom =
        hazardFreeRooms.stream().filter(room -> !room.hasBats()).findAny();
    if (!possibleStartRoom.isPresent()) {
      possibleStartRoom = hazardFreeRooms.stream().findAny();
      if (!possibleStartRoom.isPresent()) {
        possibleStartRoom = allRooms.stream().findAny();
      }
    }
    
    return possibleStartRoom.get();
  }
}
