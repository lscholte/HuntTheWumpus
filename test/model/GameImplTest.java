package model;

import model.maze.Maze;
import model.maze.MazeGenerationException;
import model.maze.MazeImpl;
import model.maze.ReadOnlyMaze;
import model.player.ModelPlayer;
import model.player.PlayerImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Tests the GameImpl class.
 * @author Liam Scholte
 *
 */
public class GameImplTest {
  
  private Position roomPosition;
  
  private List<ModelPlayer> onePlayerList;
  
  /**
   * Initializes data for testing.
   */
  @Before
  public void setup() {
    roomPosition = new Position(1, 2); 
    
    onePlayerList = new ArrayList<ModelPlayer>();
    onePlayerList.add(new PlayerImpl("Player 1", new Room(roomPosition), 3));
  }

  /**
   * Tests that a game cannot be constructed with a
   * null maze.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNullMaze() {
    new GameImpl(null, onePlayerList);
  }

  /**
   * Tests that a game cannot be constructed with a
   * null player list.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNullPlayer() throws MazeGenerationException {
    new GameImpl(new MazeImpl(5, 5, 5, 5, false, new Random(1)), null);
  }
  
  /**
   * Tests that when the player kills the wumpus, the game is over.
   */
  @Test
  public void testIsOverPlayerKilledWumpus() {
    List<Room> rooms = new ArrayList<Room>();
    Room room = new Room(roomPosition);
    rooms.add(room);
    
    Room otherRoom = new Room(roomPosition);
    rooms.add(otherRoom);
    
    room.setNeighbour(Direction.EAST, otherRoom);
    otherRoom.addWumpus();
        
    ReadOnlyMaze maze = new ReadOnlyMaze() {

      @Override
      public List<ReadOnlyRoom> getRooms() {
        return rooms.stream().collect(Collectors.toList());
      }

      @Override
      public List<ReadOnlyRoom> getExploredRooms() {
        return null;
      }

      @Override
      public Dimension getSize() {
        return null;
      }
    };
    
    ModelPlayer player = new PlayerImpl("Player", rooms.get(0), 2);
    List<ModelPlayer> playerList = new ArrayList<ModelPlayer>();
    playerList.add(player);

    GameImpl game = new GameImpl(maze, playerList);
    
    Assert.assertFalse(game.isOver());
    
    player.shootArrow(Direction.EAST, 1);
    
    Assert.assertTrue(game.isOver());
    Assert.assertEquals(player, game.getWinner());
    Assert.assertTrue(game.isWinnable());
  }
  
  /**
   * Tests that when the player kills the wumpus, the game is over.
   */
  @Test
  public void testIsOverPlayerRunsOutOfArrows() {
    List<Room> rooms = new ArrayList<Room>();
    Room room = new Room(roomPosition);
    rooms.add(room);
    
    Room otherRoom = new Room(roomPosition);
    rooms.add(otherRoom);
    
    room.setNeighbour(Direction.EAST, otherRoom);
    otherRoom.addWumpus();
        
    ReadOnlyMaze maze = new ReadOnlyMaze() {

      @Override
      public List<ReadOnlyRoom> getRooms() {
        return rooms.stream().collect(Collectors.toList());
      }

      @Override
      public List<ReadOnlyRoom> getExploredRooms() {
        return null;
      }

      @Override
      public Dimension getSize() {
        return null;
      }
    };

    ModelPlayer player = new PlayerImpl("Player", rooms.get(0), 1);
    List<ModelPlayer> playerList = new ArrayList<ModelPlayer>();
    playerList.add(player);

    GameImpl game = new GameImpl(maze, playerList);
    
    Assert.assertFalse(game.isOver());
    
    player.shootArrow(Direction.WEST, 5);
    
    Assert.assertTrue(game.isOver());
    Assert.assertNull(game.getWinner());
    Assert.assertFalse(game.isWinnable());
  }
  
  /**
   * Tests that when the player kills the wumpus, the game is over.
   */
  @Test
  public void testIsOverPlayedKilled() {
    List<Room> rooms = new ArrayList<Room>();
    Room room = new Room(roomPosition);
    rooms.add(room);
    
    Room otherRoom = new Room(roomPosition);
    rooms.add(otherRoom);
    
    room.setNeighbour(Direction.EAST, otherRoom);
    otherRoom.addWumpus();
        
    ReadOnlyMaze maze = new ReadOnlyMaze() {

      @Override
      public List<ReadOnlyRoom> getRooms() {
        return rooms.stream().collect(Collectors.toList());
      }

      @Override
      public List<ReadOnlyRoom> getExploredRooms() {
        return null;
      }

      @Override
      public Dimension getSize() {
        return null;
      }
    };
    
    ModelPlayer player = new PlayerImpl("Player", rooms.get(0), 2);
    List<ModelPlayer> playerList = new ArrayList<ModelPlayer>();
    playerList.add(player);

    GameImpl game = new GameImpl(maze, playerList);
    
    Assert.assertFalse(game.isOver());
    
    player.kill();
    
    Assert.assertTrue(game.isOver());
    Assert.assertNull(game.getWinner());
    Assert.assertFalse(game.isWinnable());
  }
  
  /**
   * Tests that calling playerWon while the game is not yet over
   * throws an IllegalStateException.
   */
  @Test(expected = IllegalStateException.class)
  public void testPlayerWonWhileGameInProgress() {
    List<Room> rooms = new ArrayList<Room>();
    Room room = new Room(roomPosition);
    rooms.add(room);
    
    Room otherRoom = new Room(roomPosition);
    rooms.add(otherRoom);
    
    room.setNeighbour(Direction.EAST, otherRoom);
    otherRoom.addWumpus();
        
    ReadOnlyMaze maze = new ReadOnlyMaze() {

      @Override
      public List<ReadOnlyRoom> getRooms() {
        return rooms.stream().collect(Collectors.toList());
      }

      @Override
      public List<ReadOnlyRoom> getExploredRooms() {
        return null;
      }

      @Override
      public Dimension getSize() {
        return null;
      }
    };
    
    ModelPlayer player = new PlayerImpl("Player", rooms.get(0), 2);
    List<ModelPlayer> playerList = new ArrayList<ModelPlayer>();
    playerList.add(player);

    GameImpl game = new GameImpl(maze, playerList);
    
    game.getWinner();
  }
  
  /**
   * Tests that a game where the nearest safe room to shoot a wumpus
   * is farther than the player can shoot makes the game unwinnable.
   */
  @Test
  public void testIsWinnableWumpusTooFarToShoot() {

    List<Room> rooms = new ArrayList<Room>();
    Room room = new Room(roomPosition);
    rooms.add(room);
    for (int i = 1; i <= 5; ++i) {
      Room newRoom = new Room(roomPosition);
      rooms.add(newRoom);
      
      Room sideRoom = new Room(roomPosition);
      rooms.add(sideRoom);
      
      room.setNeighbour(Direction.EAST, newRoom);
      newRoom.setNeighbour(Direction.SOUTH, sideRoom);
      newRoom.addPit();
      room = newRoom;
    }
    Room newRoom = new Room(roomPosition);
    rooms.add(newRoom);
    
    room.setNeighbour(Direction.EAST, newRoom);
    newRoom.addWumpus();
    
    ReadOnlyMaze maze = new ReadOnlyMaze() {

      @Override
      public List<ReadOnlyRoom> getRooms() {
        return rooms.stream().collect(Collectors.toList());
      }

      @Override
      public List<ReadOnlyRoom> getExploredRooms() {
        return null;
      }

      @Override
      public Dimension getSize() {
        return null;
      }
    };
    
    ModelPlayer player = new PlayerImpl("Player", rooms.get(0), 3);
    List<ModelPlayer> playerList = new ArrayList<ModelPlayer>();
    playerList.add(player);

    GameImpl game = new GameImpl(maze, playerList);
    
    Assert.assertFalse(game.isWinnable());
  }
  
  /**
   * Tests that a game where the nearest safe room to shoot a wumpus
   * is within the distance that the player can shoot makes the game winnable.
   */
  @Test
  public void testConstructorWumpusNearEnoughToShoot() {

    List<Room> rooms = new ArrayList<Room>();
    Room room = new Room(roomPosition);
    rooms.add(room);
    for (int i = 1; i <= 5; ++i) {
      Room newRoom = new Room(roomPosition);
      rooms.add(newRoom);
      
      Room sideRoom = new Room(roomPosition);
      rooms.add(sideRoom);
      
      room.setNeighbour(Direction.EAST, newRoom);
      newRoom.setNeighbour(Direction.SOUTH, sideRoom);
      newRoom.addPit();
      room = newRoom;
    }
    Room newRoom = new Room(roomPosition);
    rooms.add(newRoom);
    
    room.setNeighbour(Direction.EAST, newRoom);
    newRoom.addWumpus();
    
    ReadOnlyMaze maze = new ReadOnlyMaze() {

      @Override
      public List<ReadOnlyRoom> getRooms() {
        return rooms.stream().collect(Collectors.toList());
      }

      @Override
      public List<ReadOnlyRoom> getExploredRooms() {
        return null;
      }

      @Override
      public Dimension getSize() {
        return null;
      }
    };
    
    ModelPlayer player = new PlayerImpl("Player", rooms.get(0), 3);
    List<ModelPlayer> playerList = new ArrayList<ModelPlayer>();
    playerList.add(player);

    GameImpl game = new GameImpl(maze, playerList);
    
    Assert.assertFalse(game.isWinnable());
  }
  
  /**
   * Tests that when a wumpus is protected by a pit around a corner
   * (no path for arrow to travel) makes the game unwinnable.
   */
  @Test
  public void testIsWinnableWumpusShieldedByPit() {

    List<Room> rooms = new ArrayList<Room>();
    Room roomA = new Room(roomPosition);
    rooms.add(roomA);
    
    Room roomB = new Room(roomPosition);
    rooms.add(roomB);
    
    Room roomC = new Room(roomPosition);
    rooms.add(roomC);
    
    Room roomD = new Room(roomPosition);
    rooms.add(roomD);
    
    roomB.setNeighbour(Direction.WEST, roomA);
    roomB.setNeighbour(Direction.EAST, roomC);
    roomB.setNeighbour(Direction.SOUTH, roomD);

    roomB.addPit();
    roomD.addWumpus();
    
    ReadOnlyMaze maze = new ReadOnlyMaze() {

      @Override
      public List<ReadOnlyRoom> getRooms() {
        return rooms.stream().collect(Collectors.toList());
      }

      @Override
      public List<ReadOnlyRoom> getExploredRooms() {
        return null;
      }

      @Override
      public Dimension getSize() {
        return null;
      }
    };
    
    ModelPlayer player = new PlayerImpl("Player", roomA, 3);
    List<ModelPlayer> playerList = new ArrayList<ModelPlayer>();
    playerList.add(player);

    GameImpl game = new GameImpl(maze, playerList);

    Assert.assertFalse(game.isWinnable());
  }
  
  /**
   * Tests that when a wumpus is protected by a pit with bats around a corner
   * (no path for arrow to travel) makes the game unwinnable because the bats
   * do cannot take the player to a location that could shoot the wumpus.
   */
  @Test
  public void testIsWinnableWumpusShieldedByPitBatsDontHelp() {

    List<Room> rooms = new ArrayList<Room>();
    Room roomA = new Room(roomPosition);
    rooms.add(roomA);
    
    Room roomB = new Room(roomPosition);
    rooms.add(roomB);
    
    Room roomC = new Room(roomPosition);
    rooms.add(roomC);
    
    Room roomD = new Room(roomPosition);
    rooms.add(roomD);
    
    roomB.setNeighbour(Direction.WEST, roomA);
    roomB.setNeighbour(Direction.EAST, roomC);
    roomB.setNeighbour(Direction.SOUTH, roomD);
    
    Maze maze = new Maze() {

      @Override
      public List<ReadOnlyRoom> getRooms() {
        return rooms.stream().collect(Collectors.toList());
      }

      @Override
      public List<ReadOnlyRoom> getExploredRooms() {
        return null;
      }

      @Override
      public Dimension getSize() {
        return null;
      }

      @Override
      public List<Room> getMutableRooms() {
        return null;
      }
    };
    
    roomB.addBats(maze);
    roomB.addPit();
    roomD.addWumpus();
    
    ModelPlayer player = new PlayerImpl("Player", roomA, 3);
    List<ModelPlayer> playerList = new ArrayList<ModelPlayer>();
    playerList.add(player);

    GameImpl game = new GameImpl(maze, playerList);

    Assert.assertFalse(game.isWinnable());
  }
  
  /**
   * Tests that a game with a location that can shoot the wumpus but the
   * location is inaccessible to the player makes the game unwinnable.
   */
  @Test
  public void testIsWinnableWumpusNotShieldedButRoomInaccessible() {

    List<Room> rooms = new ArrayList<Room>();
    Room roomA = new Room(roomPosition);
    rooms.add(roomA);
    
    Room roomB = new Room(roomPosition);
    rooms.add(roomB);
    
    Room roomC = new Room(roomPosition);
    rooms.add(roomC);
    
    Room roomD = new Room(roomPosition);
    rooms.add(roomD);
    
    Room roomE = new Room(roomPosition);
    rooms.add(roomE);
    
    roomB.setNeighbour(Direction.WEST, roomA);
    roomB.setNeighbour(Direction.EAST, roomC);
    roomB.setNeighbour(Direction.SOUTH, roomD);
    roomB.setNeighbour(Direction.NORTH, roomE);
    
    roomB.addPit();
    roomD.addWumpus();
    
    ModelPlayer player = new PlayerImpl("Player", roomA, 3);
    List<ModelPlayer> playerList = new ArrayList<ModelPlayer>();
    playerList.add(player);
    
    ReadOnlyMaze maze = new ReadOnlyMaze() {

      @Override
      public List<ReadOnlyRoom> getRooms() {
        return rooms.stream().collect(Collectors.toList());
      }

      @Override
      public List<ReadOnlyRoom> getExploredRooms() {
        return null;
      }

      @Override
      public Dimension getSize() {
        return null;
      }
    };

    GameImpl game = new GameImpl(maze, playerList);

    Assert.assertFalse(game.isWinnable());
  }
  
  /**
   * Tests that a game with a location that can shoot the wumpus but the
   * location and the location is accessible due to bats makes the game winnable.
   */
  @Test
  public void testIsWinnableDueToRoomAccessibleByBats() {

    List<Room> rooms = new ArrayList<Room>();
    Room roomA = new Room(roomPosition);
    rooms.add(roomA);
    
    Room roomB = new Room(roomPosition);
    rooms.add(roomB);
    
    Room roomC = new Room(roomPosition);
    rooms.add(roomC);
    
    Room roomD = new Room(roomPosition);
    rooms.add(roomD);
    
    Room roomE = new Room(roomPosition);
    rooms.add(roomE);
    
    roomB.setNeighbour(Direction.WEST, roomA);
    roomB.setNeighbour(Direction.EAST, roomC);
    roomB.setNeighbour(Direction.SOUTH, roomD);
    roomB.setNeighbour(Direction.NORTH, roomE);
    
    Maze maze = new Maze() {

      @Override
      public List<ReadOnlyRoom> getRooms() {
        return rooms.stream().collect(Collectors.toList());
      }

      @Override
      public List<ReadOnlyRoom> getExploredRooms() {
        return null;
      }

      @Override
      public Dimension getSize() {
        return null;
      }

      @Override
      public List<Room> getMutableRooms() {
        return null;
      }
    };
    
    roomB.addBats(maze);
    roomB.addPit();
    roomD.addWumpus();
    
    ModelPlayer player = new PlayerImpl("Player", roomA, 3);
    List<ModelPlayer> playerList = new ArrayList<ModelPlayer>();
    playerList.add(player);

    GameImpl game = new GameImpl(maze, playerList);

    Assert.assertTrue(game.isWinnable());
  }
  
  /**
   * Tests that calling start moves the player into the starting room,
   * performing the actions associated with that room.
   */
  @Test
  public void testStartCausesPlayerToMoveIntoStartRoom() throws MazeGenerationException {
    ModelPlayer player = new PlayerImpl("Player", new Room(roomPosition), 3);
    List<ModelPlayer> playerList = new ArrayList<ModelPlayer>();
    playerList.add(player);
    
    boolean[] actionPerformed = new boolean[1];
    player.getKilledByWumpusEvent().addListener(() -> actionPerformed[0] = true);
    
    player.getRoom().addWumpus();
    
    Maze maze = new MazeImpl(5, 5, 0, 0, false, new Random(1));
    
    new GameImpl(maze, playerList);
       
    Assert.assertTrue(actionPerformed[0]);
  }
  

  
  /**
   * Tests that checking if the player won before the game has started
   * throws an IllegalStateException.
   */
  @Test(expected = IllegalStateException.class)
  public void testPlayerWonThrowsIfGameNotStarted() throws MazeGenerationException {
    ModelPlayer player = new PlayerImpl("Player", new Room(roomPosition), 3);
    List<ModelPlayer> playerList = new ArrayList<ModelPlayer>();
    playerList.add(player);
    
    Maze maze = new MazeImpl(5, 5, 0, 0, false, new Random(1));
    
    GameImpl game = new GameImpl(maze, playerList);
    
    game.getWinner();
  }
  
  /**
   * Tests that the current player changes after a player moves.
   */
  @Test
  public void testGetCurrentPlayerChangesAfterMoving() throws MazeGenerationException {    
    List<Room> rooms = new ArrayList<Room>();
    Room roomA = new Room(roomPosition);
    rooms.add(roomA);
    
    Room roomB = new Room(roomPosition);
    rooms.add(roomB);
    
    Room roomC = new Room(roomPosition);
    rooms.add(roomC);
    
    Room roomD = new Room(roomPosition);
    rooms.add(roomD);
    
    Room roomE = new Room(roomPosition);
    rooms.add(roomE);
    
    roomB.setNeighbour(Direction.WEST, roomA);
    roomB.setNeighbour(Direction.EAST, roomC);
    roomB.setNeighbour(Direction.SOUTH, roomD);
    roomB.setNeighbour(Direction.NORTH, roomE);
    
    roomD.addWumpus();
    
    ModelPlayer player1 = new PlayerImpl("Player 1", roomA, 3);
    ModelPlayer player2 = new PlayerImpl("Player 2", roomA, 3);
    List<ModelPlayer> playerList = new ArrayList<ModelPlayer>();
    playerList.add(player1);
    playerList.add(player2);
        
    Maze maze = new Maze() {

      @Override
      public List<ReadOnlyRoom> getRooms() {
        return rooms.stream().collect(Collectors.toList());
      }

      @Override
      public List<ReadOnlyRoom> getExploredRooms() {
        return null;
      }

      @Override
      public Dimension getSize() {
        return null;
      }

      @Override
      public List<Room> getMutableRooms() {
        return null;
      }
    };
    
    GameImpl game = new GameImpl(maze, playerList);
    
    Assert.assertEquals(player1, game.getCurrentPlayer());
    game.move(Direction.EAST);
    Assert.assertEquals(player2, game.getCurrentPlayer());
    game.move(Direction.EAST);
    Assert.assertEquals(player1, game.getCurrentPlayer());
  }
  
  /**
   * Tests that the current player changes after a player shoots an arrow.
   */
  @Test
  public void testGetCurrentPlayerChangesAfterShootingArrow() throws MazeGenerationException {    
    List<Room> rooms = new ArrayList<Room>();
    Room roomA = new Room(roomPosition);
    rooms.add(roomA);
    
    Room roomB = new Room(roomPosition);
    rooms.add(roomB);
    
    Room roomC = new Room(roomPosition);
    rooms.add(roomC);
    
    Room roomD = new Room(roomPosition);
    rooms.add(roomD);
    
    Room roomE = new Room(roomPosition);
    rooms.add(roomE);
    
    roomB.setNeighbour(Direction.WEST, roomA);
    roomB.setNeighbour(Direction.EAST, roomC);
    roomB.setNeighbour(Direction.SOUTH, roomD);
    roomB.setNeighbour(Direction.NORTH, roomE);
   
    roomD.addWumpus();
    
    ModelPlayer player1 = new PlayerImpl("Player 1", roomA, 3);
    ModelPlayer player2 = new PlayerImpl("Player 2", roomA, 3);
    List<ModelPlayer> playerList = new ArrayList<ModelPlayer>();
    playerList.add(player1);
    playerList.add(player2);
        
    Maze maze = new Maze() {

      @Override
      public List<ReadOnlyRoom> getRooms() {
        return rooms.stream().collect(Collectors.toList());
      }

      @Override
      public List<ReadOnlyRoom> getExploredRooms() {
        return null;
      }

      @Override
      public Dimension getSize() {
        return null;
      }

      @Override
      public List<Room> getMutableRooms() {
        return null;
      }
    };
    
    GameImpl game = new GameImpl(maze, playerList);
    
    Assert.assertEquals(player1, game.getCurrentPlayer());
    game.shootArrow(Direction.EAST, 1);
    Assert.assertEquals(player2, game.getCurrentPlayer());
    game.shootArrow(Direction.EAST, 1);
    Assert.assertEquals(player1, game.getCurrentPlayer());
  }
  
  /**
   * Tests that the current player does not change to a dead player.
   */
  @Test
  public void testGetCurrentPlayerDoesNotReturnDeadPlayer() throws MazeGenerationException {    
    List<Room> rooms = new ArrayList<Room>();
    Room roomA = new Room(roomPosition);
    rooms.add(roomA);
    
    Room roomB = new Room(roomPosition);
    rooms.add(roomB);
    
    Room roomC = new Room(roomPosition);
    rooms.add(roomC);
    
    Room roomD = new Room(roomPosition);
    rooms.add(roomD);
    
    Room roomE = new Room(roomPosition);
    rooms.add(roomE);
    
    roomB.setNeighbour(Direction.WEST, roomA);
    roomB.setNeighbour(Direction.EAST, roomC);
    roomB.setNeighbour(Direction.SOUTH, roomD);
    roomB.setNeighbour(Direction.NORTH, roomE);
    
    roomD.addWumpus();
    
    ModelPlayer player1 = new PlayerImpl("Player 1", roomA, 3);
    ModelPlayer player2 = new PlayerImpl("Player 2", roomA, 3);
    List<ModelPlayer> playerList = new ArrayList<ModelPlayer>();
    playerList.add(player1);
    playerList.add(player2);
    
    Maze maze = new Maze() {

      @Override
      public List<ReadOnlyRoom> getRooms() {
        return rooms.stream().collect(Collectors.toList());
      }

      @Override
      public List<ReadOnlyRoom> getExploredRooms() {
        return null;
      }

      @Override
      public Dimension getSize() {
        return null;
      }

      @Override
      public List<Room> getMutableRooms() {
        return null;
      }
    };
        
    GameImpl game = new GameImpl(maze, playerList);
    
    Assert.assertEquals(player1, game.getCurrentPlayer());
    game.suicide();
    Assert.assertEquals(player2, game.getCurrentPlayer());
    game.move(Direction.EAST);
    Assert.assertEquals(player2, game.getCurrentPlayer());
  }
}
