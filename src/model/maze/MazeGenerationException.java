package model.maze;

/**
 * Thrown when generation of a maze is unable to be completed.
 * @author Liam Scholte
 *
 */
@SuppressWarnings("serial")
public class MazeGenerationException extends Exception {
  
  /**
   * Constructs a MazeGenerationException with the
   * specified message.
   * @param message the exception message
   */
  public MazeGenerationException(String message) {
    super(message);
  }

}
