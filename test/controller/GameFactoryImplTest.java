package controller;

import model.Game;
import model.Position;
import model.Room;
import model.maze.Maze;
import model.maze.MazeGenerationException;
import model.maze.MazeImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Tests the GameFactoryImpl class.
 * @author Liam Scholte
 *
 */
public class GameFactoryImplTest {
  
  private Position roomPosition = new Position(1, 2);
  private Maze maze;
  
  /**
   * Initializes data for testing.
   */
  @Before
  public void setup() throws MazeGenerationException {
    roomPosition = new Position(1, 2);
    maze = new MazeImpl(1, 1, 0, 0, false, new Random(1));

  }
  
  /**
   * Performs some sanity checks to have reasonable confidence that createGame
   * is creating a game with the correct parameters.
   */
  @Test
  public void test() throws IllegalArgumentException, MazeGenerationException {
    GameFactoryImpl gameFactory = new GameFactoryImpl();
    
    Game game = gameFactory.createGame(5, 10, true, 2, 3, 2, 1, 12345);
    
    Assert.assertEquals(5, game.getMaze().getSize().height);
    Assert.assertEquals(10, game.getMaze().getSize().width);
    Assert.assertEquals(2, game.getPlayers().size());
    game.getPlayers().forEach(player -> Assert.assertEquals(1, player.getArrowCount()));
  }
  
  /**
   * Tests that getStartingRooms with a null list
   * throws an IllegalArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetStartingRoomNullRooms() {
    GameFactoryImpl.getStartingRoom(null);
  }
  
  /**
   * Tests that getStartingRooms with an empty list
   * throws an IllegalArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetStartingRoomEmptyRooms() {
    GameFactoryImpl.getStartingRoom(new ArrayList<Room>());
  }
  
  /**
   * Tests that getStartingRoom correctly picks an empty room.
   */
  @Test
  public void testGetStartingRoomPicksEmptyRoom() {    
    Room roomA = new Room(roomPosition);
    roomA.addWumpus();
    
    Room roomB = new Room(roomPosition);
    roomB.addBats(maze);
    
    Room roomC = new Room(roomPosition);
    roomC.addPit();
    
    Room roomD = new Room(roomPosition);
    
    List<Room> rooms = new ArrayList<Room>();
    rooms.add(roomA);
    rooms.add(roomB);
    rooms.add(roomC);
    rooms.add(roomD);

    Assert.assertEquals(roomD, GameFactoryImpl.getStartingRoom(rooms));
  }

  /**
   * Tests that getStartingRoom correctly picks a room with bats
   * when no empty room exists.
   */
  @Test
  public void testGetStartingRoomPicksBatsRoom() {    
    Room roomA = new Room(roomPosition);
    roomA.addWumpus();
    
    Room roomB = new Room(roomPosition);
    roomB.addBats(maze);
    
    Room roomC = new Room(roomPosition);
    roomC.addPit();
    
    Room roomD = new Room(roomPosition);
    roomD.addPit();
    
    List<Room> rooms = new ArrayList<Room>();
    rooms.add(roomA);
    rooms.add(roomB);
    rooms.add(roomC);
    rooms.add(roomD);

    Assert.assertEquals(roomB, GameFactoryImpl.getStartingRoom(rooms));
  }
  
  /**
   * Tests that getStartingRoom correctly picks any room when no
   * empty room or room with bats only exists.
   */
  @Test
  public void testGetStartingRoomPicksAnyRoom() {    
    Room roomA = new Room(roomPosition);
    roomA.addWumpus();
    
    Room roomB = new Room(roomPosition);
    roomB.addBats(maze);
    roomB.addPit();
    
    Room roomC = new Room(roomPosition);
    roomC.addPit();
    
    Room roomD = new Room(roomPosition);
    roomD.addPit();
    
    List<Room> rooms = new ArrayList<Room>();
    rooms.add(roomA);
    rooms.add(roomB);
    rooms.add(roomC);
    rooms.add(roomD);

    Room selectedRoom = GameFactoryImpl.getStartingRoom(rooms);
    Assert.assertNotNull(selectedRoom);
    Assert.assertTrue(rooms.contains(selectedRoom));
  }

}
