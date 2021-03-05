package model;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the Event class.
 * @author Liam Scholte
 *
 */
public class EventTest {
  
  /**
   * Tests that calling addListener with a null
   * Runnable throws an IllegalArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddNullListener() {
    Event event = new Event();
    
    event.addListener(null);
  }
  
  /**
   * Tests that calling raise with no listeners does nothing.
   */
  @Test
  public void testRaiseNoListeners() {
    Event event = new Event();
    
    try {
      event.raise();
    }
    catch (Exception e) {
      Assert.fail(e.getMessage());
    }
  }

  /**
   * Tests that calling raise invokes all of the listeners
   * attached to the event.
   */
  @Test
  public void testListenerInvokedWhenEventRaised() {
    Event event = new Event();
    
    boolean[] invoked = new boolean[3];
    event.addListener(() -> invoked[0] = true);
    event.addListener(() -> invoked[1] = true);
    event.addListener(() -> invoked[2] = true);
    
    Assert.assertFalse(invoked[0]);
    Assert.assertFalse(invoked[1]);
    Assert.assertFalse(invoked[2]);
    
    event.raise();
    
    Assert.assertTrue(invoked[0]);
    Assert.assertTrue(invoked[1]);
    Assert.assertTrue(invoked[2]);
  }
}
