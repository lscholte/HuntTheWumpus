package model;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the Direction enum.
 * @author Liam Scholte
 *
 */
public class DirectionTest {
  
  /**
   * Tests that the opposite of north is south.
   */
  @Test
  public void testGetOppositeOfNorth() {
    Assert.assertEquals(Direction.SOUTH, Direction.NORTH.getOpposite());
  }

  /**
   * Tests that the opposite of south is north.
   */
  @Test
  public void testGetOppositeOfSouth() {
    Assert.assertEquals(Direction.NORTH, Direction.SOUTH.getOpposite());
  }
  
  /**
   * Tests that the opposite of east is west.
   */
  @Test
  public void testGetOppositeOfEast() {
    Assert.assertEquals(Direction.WEST, Direction.EAST.getOpposite());
  }
  
  /**
   * Tests that the opposite of west is east.
   */
  @Test
  public void testGetOppositeOfWest() {
    Assert.assertEquals(Direction.EAST, Direction.WEST.getOpposite());
  }
}
