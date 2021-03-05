package model.maze;

import model.Room;

import java.util.List;

/**
 * A maze in a Hunt the Wumpus game providing
 * mutating functionality.
 * @author Liam Scholte
 *
 */
public interface Maze extends ReadOnlyMaze {
  
  /**
   * Gets all rooms in the maze in a mutable form.
   * @return all rooms in the maze
   */
  public List<Room> getMutableRooms();
}
