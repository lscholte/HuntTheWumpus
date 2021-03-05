package maze;

import model.Direction;
import model.Position;
import model.Room;
import model.actions.BatsRoomAction;
import model.actions.EmptyRoomAction;
import model.actions.PitRoomAction;
import model.actions.WumpusRoomAction;
import model.maze.MazeGenerationException;
import model.maze.MazeImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

/**
 * Unit tests the Room class.
 * @author Liam Scholte
 *
 */
public class RoomTest {
  
  protected Room room;
  
  protected final Position expectedPosition;
  
  /**
   * Sets up common objects for testing.
   */
  public RoomTest() {
    expectedPosition = new Position(1, 2);
  }
  
  /**
   * Initializes data for testing.
   */
  @Before
  public void setup() {
    room = new Room(expectedPosition);
  }
  
  /**
   * Tests that a room is constructed
   * with null adjacent rooms.
   */
  @Test
  public void testConstructor() {
    Assert.assertNull(room.getNeighbour(Direction.NORTH));
    Assert.assertNull(room.getNeighbour(Direction.SOUTH));
    Assert.assertNull(room.getNeighbour(Direction.EAST));
    Assert.assertNull(room.getNeighbour(Direction.WEST));
    Assert.assertEquals(expectedPosition, room.getPosition());
  }
  
  /**
   * Tests that setNorth sets the room to the north.
   */
  @Test
  public void testSetNorth() {
    Room otherRoom = new Room(expectedPosition);
    room.setNeighbour(Direction.NORTH, otherRoom);
    
    Assert.assertSame(otherRoom, room.getNeighbour(Direction.NORTH));
    Assert.assertNull(room.getNeighbour(Direction.SOUTH));
    Assert.assertNull(room.getNeighbour(Direction.EAST));
    Assert.assertNull(room.getNeighbour(Direction.WEST));
    
    Assert.assertSame(room, otherRoom.getNeighbour(Direction.SOUTH));
    Assert.assertNull(otherRoom.getNeighbour(Direction.NORTH));
    Assert.assertNull(otherRoom.getNeighbour(Direction.WEST));
    Assert.assertNull(otherRoom.getNeighbour(Direction.EAST));
    
    room.setNeighbour(Direction.NORTH, null);
    
    Assert.assertNull(room.getNeighbour(Direction.NORTH));
    Assert.assertNull(room.getNeighbour(Direction.SOUTH));
    Assert.assertNull(room.getNeighbour(Direction.EAST));
    Assert.assertNull(room.getNeighbour(Direction.WEST));
    
    Assert.assertNull(otherRoom.getNeighbour(Direction.NORTH));
    Assert.assertNull(otherRoom.getNeighbour(Direction.SOUTH));
    Assert.assertNull(otherRoom.getNeighbour(Direction.EAST));
    Assert.assertNull(otherRoom.getNeighbour(Direction.WEST));
  }

  /**
   * Tests that setSouth sets the room to the south.
   */
  @Test
  public void testSetSouth() {
    Room otherRoom = new Room(expectedPosition);
    room.setNeighbour(Direction.SOUTH, otherRoom);
    
    Assert.assertSame(otherRoom, room.getNeighbour(Direction.SOUTH));
    Assert.assertNull(room.getNeighbour(Direction.NORTH));
    Assert.assertNull(room.getNeighbour(Direction.EAST));
    Assert.assertNull(room.getNeighbour(Direction.WEST));

    Assert.assertSame(room, otherRoom.getNeighbour(Direction.NORTH));
    Assert.assertNull(otherRoom.getNeighbour(Direction.SOUTH));
    Assert.assertNull(otherRoom.getNeighbour(Direction.WEST));
    Assert.assertNull(otherRoom.getNeighbour(Direction.EAST));
    
    room.setNeighbour(Direction.SOUTH, null);
    
    Assert.assertNull(room.getNeighbour(Direction.NORTH));
    Assert.assertNull(room.getNeighbour(Direction.SOUTH));
    Assert.assertNull(room.getNeighbour(Direction.EAST));
    Assert.assertNull(room.getNeighbour(Direction.WEST));
    
    Assert.assertNull(otherRoom.getNeighbour(Direction.NORTH));
    Assert.assertNull(otherRoom.getNeighbour(Direction.SOUTH));
    Assert.assertNull(otherRoom.getNeighbour(Direction.EAST));
    Assert.assertNull(otherRoom.getNeighbour(Direction.WEST));
  }
  
  /**
   * Tests that setEast sets the room to the east.
   */
  @Test
  public void testSetEast() {
    Room otherRoom = new Room(expectedPosition);
    room.setNeighbour(Direction.EAST, otherRoom);
    
    Assert.assertSame(otherRoom, room.getNeighbour(Direction.EAST));
    Assert.assertNull(room.getNeighbour(Direction.NORTH));
    Assert.assertNull(room.getNeighbour(Direction.SOUTH));
    Assert.assertNull(room.getNeighbour(Direction.WEST));
    
    Assert.assertSame(room, otherRoom.getNeighbour(Direction.WEST));
    Assert.assertNull(otherRoom.getNeighbour(Direction.SOUTH));
    Assert.assertNull(otherRoom.getNeighbour(Direction.NORTH));
    Assert.assertNull(otherRoom.getNeighbour(Direction.EAST));
    
    room.setNeighbour(Direction.EAST, null);
    
    Assert.assertNull(room.getNeighbour(Direction.NORTH));
    Assert.assertNull(room.getNeighbour(Direction.SOUTH));
    Assert.assertNull(room.getNeighbour(Direction.EAST));
    Assert.assertNull(room.getNeighbour(Direction.WEST));
    
    Assert.assertNull(otherRoom.getNeighbour(Direction.NORTH));
    Assert.assertNull(otherRoom.getNeighbour(Direction.SOUTH));
    Assert.assertNull(otherRoom.getNeighbour(Direction.EAST));
    Assert.assertNull(otherRoom.getNeighbour(Direction.WEST));
  }
  
  /**
   * Tests that setWest sets the room to the west.
   */
  @Test
  public void testSetWest() {
    Room otherRoom = new Room(expectedPosition);
    room.setNeighbour(Direction.WEST, otherRoom);
    
    Assert.assertSame(otherRoom, room.getNeighbour(Direction.WEST));
    Assert.assertNull(room.getNeighbour(Direction.NORTH));
    Assert.assertNull(room.getNeighbour(Direction.SOUTH));
    Assert.assertNull(room.getNeighbour(Direction.EAST));
    
    Assert.assertSame(room, otherRoom.getNeighbour(Direction.EAST));
    Assert.assertNull(otherRoom.getNeighbour(Direction.SOUTH));
    Assert.assertNull(otherRoom.getNeighbour(Direction.NORTH));
    Assert.assertNull(otherRoom.getNeighbour(Direction.WEST));
    
    room.setNeighbour(Direction.WEST, null);
    
    Assert.assertNull(room.getNeighbour(Direction.NORTH));
    Assert.assertNull(room.getNeighbour(Direction.SOUTH));
    Assert.assertNull(room.getNeighbour(Direction.EAST));
    Assert.assertNull(room.getNeighbour(Direction.WEST));
    
    Assert.assertNull(otherRoom.getNeighbour(Direction.NORTH));
    Assert.assertNull(otherRoom.getNeighbour(Direction.SOUTH));
    Assert.assertNull(otherRoom.getNeighbour(Direction.EAST));
    Assert.assertNull(otherRoom.getNeighbour(Direction.WEST));
  }
  
  /**
   * Tests that setting neighbours to null multiple
   * times does not throw any exceptions.
   */
  @Test
  public void testSetNeighbourToNullWhenAlreadyNull() {
    room.setNeighbour(Direction.NORTH, null);
    room.setNeighbour(Direction.SOUTH, null);
    room.setNeighbour(Direction.EAST, null);
    room.setNeighbour(Direction.WEST, null);
    
    Assert.assertNull(room.getNeighbour(Direction.NORTH));
    Assert.assertNull(room.getNeighbour(Direction.SOUTH));
    Assert.assertNull(room.getNeighbour(Direction.EAST));
    Assert.assertNull(room.getNeighbour(Direction.WEST));
  }
  
  /**
   * Tests that only rooms with exactly 2
   * connecting rooms are identified as hallways.
   */
  @Test
  public void testIsHallway() {
    Room otherRoom = new Room(expectedPosition);

    Assert.assertFalse(room.isHallway());
    Assert.assertFalse(otherRoom.isHallway());
    
    room.setNeighbour(Direction.NORTH, otherRoom);
    Assert.assertFalse(room.isHallway());
    Assert.assertFalse(otherRoom.isHallway());
    
    room.setNeighbour(Direction.SOUTH, otherRoom);
    Assert.assertTrue(room.isHallway());
    Assert.assertTrue(otherRoom.isHallway());
    
    room.setNeighbour(Direction.EAST, otherRoom);
    Assert.assertFalse(room.isHallway());
    Assert.assertFalse(otherRoom.isHallway());
    
    room.setNeighbour(Direction.WEST, otherRoom);
    Assert.assertFalse(room.isHallway());
    Assert.assertFalse(otherRoom.isHallway());
    
    room.setNeighbour(Direction.NORTH, null);
    Assert.assertFalse(room.isHallway());
    Assert.assertFalse(otherRoom.isHallway());
    
    room.setNeighbour(Direction.SOUTH, null);
    Assert.assertTrue(room.isHallway());
    Assert.assertTrue(otherRoom.isHallway());
    
    room.setNeighbour(Direction.EAST, null);
    Assert.assertFalse(room.isHallway());
    Assert.assertFalse(otherRoom.isHallway());
    
    room.setNeighbour(Direction.WEST, null);
    Assert.assertFalse(room.isHallway());
    Assert.assertFalse(otherRoom.isHallway());
  }
  
  /**
   * Tests that the room action by default is
   * an empty room action.
   */
  @Test
  public void testGetRoomAction() {
    Assert.assertNotNull(room.getAction());
    Assert.assertTrue(room.getAction() instanceof EmptyRoomAction);
  }
  
  /**
   * Tests that a wumpus can be added and that
   * the room action is updated.
   */
  @Test
  public void testAddWumpus() {
    Assert.assertFalse(room.hasWumpus());
    
    room.addWumpus();
    
    Assert.assertTrue(room.hasWumpus());    
    Assert.assertTrue(room.getAction() instanceof WumpusRoomAction);
  }
  
  /**
   * Tests that only one wumpus can be added to the room.
   */
  @Test(expected = IllegalStateException.class)
  public void testAddMultipleWumpuses() {    
    room.addWumpus();
    room.addWumpus();    
  }
  
  /**
   * Tests that a pit can be added and that
   * the room action is updated.
   */
  @Test
  public void testAddPit() {
    Assert.assertFalse(room.hasPit());
    
    room.addPit();
    
    Assert.assertTrue(room.hasPit());    
    Assert.assertTrue(room.getAction() instanceof PitRoomAction);
  }
  
  /**
   * Tests that only one pit can be added to the room.
   */
  @Test(expected = IllegalStateException.class)
  public void testAddMultiplePits() {    
    room.addPit();
    room.addPit();    
  }
  
  /**
   * Tests that bats can be added and that
   * the room action is updated.
   */
  @Test
  public void testAddBats() throws MazeGenerationException {
    Assert.assertFalse(room.hasBats());
    
    room.addBats(new MazeImpl(1, 1, 0, 0, false, new Random(1)));
    
    Assert.assertTrue(room.hasBats());    
    Assert.assertTrue(room.getAction() instanceof BatsRoomAction);
  }
  
  /**
   * Tests that bats can only be added to the room once.
   */
  @Test(expected = IllegalStateException.class)
  public void testAddMultipleBats() throws MazeGenerationException {    
    room.addBats(new MazeImpl(1, 1, 0, 0, false, new Random(1)));
    room.addBats(new MazeImpl(1, 1, 0, 0, false, new Random(1)));
  }
  
  /**
   * Tests that a non-null maze must be specified when
   * adding bats to the room.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddBatsNullMaze() {
    room.addBats(null);
  }
  
  /**
  * Tests that the room can properly detect a nearby pit.
  */
  @Test
  public void testIsPitNearby() {
    room.setNeighbour(Direction.NORTH, new Room(expectedPosition));
    room.visitNeighbour(Direction.NORTH).addPit();
    
    Assert.assertTrue(room.isPitNearby());
  }
  
  /**
  * Tests that the room can properly detect a nearby wumpus.
  */
  @Test
  public void testIsWumpusNearby() {
    room.setNeighbour(Direction.NORTH, new Room(expectedPosition));
    room.visitNeighbour(Direction.NORTH).addWumpus();
    
    Assert.assertTrue(room.isWumpusNearby());
  }
  
  /**
  * Tests that the room can properly detect nearby bats.
  */
  @Test
  public void testAreBatsNearby() throws MazeGenerationException {
    room.setNeighbour(Direction.NORTH, new Room(expectedPosition));
    room.visitNeighbour(Direction.NORTH).addBats(new MazeImpl(1, 1, 0, 0, false, new Random(1)));
    
    Assert.assertTrue(room.areBatsNearby());
  }
  
  /**
  * Tests that the room can detect all nearby hazards if multiple exist.
  */
  @Test
  public void testAreAllNearbyInSameRoom() throws MazeGenerationException {
    room.setNeighbour(Direction.NORTH, new Room(expectedPosition));
    room.visitNeighbour(Direction.NORTH).addPit();
    room.visitNeighbour(Direction.NORTH).addWumpus();
    room.visitNeighbour(Direction.NORTH).addBats(new MazeImpl(1, 1, 0, 0, false, new Random(1)));
    
    Assert.assertTrue(room.isPitNearby());
    Assert.assertTrue(room.isWumpusNearby());
    Assert.assertTrue(room.areBatsNearby());    
  }
  
  /**
  * Tests that the room can detect nearby hazards through hallways.
  */
  @Test
  public void testAreAllNearbyThroughHallway() throws MazeGenerationException {
    Room roomTwoToNorth = new Room(expectedPosition);
    roomTwoToNorth.addPit();
    roomTwoToNorth.addWumpus();
    roomTwoToNorth.addBats(new MazeImpl(5, 5, 1, 1, false, new Random(1)));
     
    room.setNeighbour(Direction.NORTH, new Room(expectedPosition));
    room.visitNeighbour(Direction.NORTH).setNeighbour(Direction.NORTH, roomTwoToNorth);
     
    Assert.assertTrue(room.isPitNearby());
    Assert.assertTrue(room.isWumpusNearby());
    Assert.assertTrue(room.areBatsNearby());
  }
  
  /**
  * Tests that the room properly detects no nearby hazards.
  */
  @Test
  public void testAreAllNotNearby() throws MazeGenerationException {
    room.setNeighbour(Direction.NORTH, new Room(expectedPosition));

    Assert.assertFalse(room.isPitNearby());
    Assert.assertFalse(room.isWumpusNearby());
    Assert.assertFalse(room.areBatsNearby());
  }
}
