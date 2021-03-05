package maze;

import model.Direction;
import model.Position;
import model.ReadOnlyRoom;
import model.maze.Maze;
import model.maze.MazeGenerationException;
import model.maze.MazeImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

/**
 * Unit tests the Maze class.
 * @author Liam Scholte
 *
 */
public class MazeTest {
  
  private Random random;
  
  /**
   * Initializes data for testing.
   */
  @Before
  public void setup() {
    random = new Random(1);
  }
  
  /**
   * Tests that a maze with 0 rows throws an
   * IllegalArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructor0Rows() throws MazeGenerationException {
    new MazeImpl(0, 1, 0, 0, false, random);
  }
  
  /**
   * Tests that a maze with 0 columns throws an
   * IllegalArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructor0Columns() throws MazeGenerationException {
    new MazeImpl(1, 0, 0, 0, false, random);
  }
  
  /**
   * Tests that a maze with negative amount of bats
   * throws an IllegalArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNegativeBatCount() throws MazeGenerationException {
    new MazeImpl(1, 1, -1, 0, false, random);
  }

  /**
   * Tests that a maze with negative amount of pits
   * throws an IllegalArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNegativePitCount() throws MazeGenerationException {
    new MazeImpl(1, 1, 0, -1, false, random);
  }
  
  /**
   * Tests constructing a 1x1 maze that does not wrap.
   */
  @Test
  public void testConstructor1x1NonWrapping() throws MazeGenerationException {
    Maze maze = new MazeImpl(1, 1, 0, 0, false, random);
        
    Assert.assertEquals(1, maze.getRooms().size());
    
    ReadOnlyRoom room = maze.getRooms().get(0);
    Assert.assertEquals(new Position(0, 0), room.getPosition());
    Assert.assertNull(room.getNeighbour(Direction.NORTH));
    Assert.assertNull(room.getNeighbour(Direction.SOUTH));
    Assert.assertNull(room.getNeighbour(Direction.EAST));
    Assert.assertNull(room.getNeighbour(Direction.WEST));
  }
  
  /**
   * Tests constructing a 1x1 maze that does wrap.
   */
  @Test
  public void testConstructor1x1Wrapping() throws MazeGenerationException {
    Maze maze = new MazeImpl(1, 1, 0, 0, true, random);
    
    Assert.assertEquals(1, maze.getRooms().size());
    
    ReadOnlyRoom room = maze.getRooms().get(0);
    Assert.assertEquals(new Position(0, 0), room.getPosition());
    Assert.assertSame(room, room.getNeighbour(Direction.NORTH));
    Assert.assertSame(room, room.getNeighbour(Direction.SOUTH));
    Assert.assertSame(room, room.getNeighbour(Direction.EAST));
    Assert.assertSame(room, room.getNeighbour(Direction.WEST));
  }
}
