package model.maze;

import model.ReadOnlyRoom;

import java.awt.Dimension;
import java.util.List;

/**
 * Represents a maze of rooms.
 * @author Liam Scholte
 *
 */
public interface ReadOnlyMaze {
  
  /**
   * Gets all non-hallway rooms in the maze.
   * @return all non-hallway rooms in the maze
   */
  public List<ReadOnlyRoom> getRooms();
  
  /**
   * Gets all rooms that have been explored in the maze.
   * @return all rooms that have been explored in the maze
   */
  public List<ReadOnlyRoom> getExploredRooms();
  
  /**
   * Gets the dimensions of the maze.
   * @return the dimensions of the maze
   */
  public Dimension getSize();
}
