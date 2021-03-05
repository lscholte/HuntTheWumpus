package model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the Position class.
 * @author Liam Scholte
 *
 */
public class PositionTest {
  
  private Position position;
  
  /**
   * Initializes data for testing.
   */
  @Before
  public void setup() {
    position = new Position(-5, 10);
  }
  
  /**
   * Tests that the position is correctly constructed.
   */
  @Test
  public void testConstructor() {
    Assert.assertEquals(-5, position.getX());
    Assert.assertEquals(10, position.getY());
  }
  
  /**
   * Tests that toString returns the correctly formatted string.
   */
  @Test
  public void testToString() {
    Assert.assertEquals("(-5, 10)", position.toString());
  }

}
