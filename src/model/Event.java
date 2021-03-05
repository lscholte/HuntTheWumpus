package model;

import java.util.LinkedList;
import java.util.List;

/**
 * An event that will invoke behaviours associated with
 * one or more listeners when the event is raised.
 * @author Liam Scholte
 *
 */
public class Event {
    
  private List<Runnable> listeners;
  
  /**
   * Constructs an event with no listeners.
   */
  public Event() {
    listeners = new LinkedList<Runnable>();
  }
  
  /**
   * Adds a Runnable that is invoked upon {@code this}
   * event being raised.
   * @param runnable the Runnable to invoke
   * @throws IllegalArgumentException if the runnable is null
   */
  public void addListener(Runnable runnable) throws IllegalArgumentException {
    if (runnable == null) {
      throw new IllegalArgumentException(
          "The runnable cannot be null");
    }
    listeners.add(runnable);
  }
  
  /**
   * Raises this event, invoking all attached listeners.
   */
  public void raise() {
    listeners.forEach(runnable -> runnable.run());
  }

}
