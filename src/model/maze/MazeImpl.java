package model.maze;

import model.Direction;
import model.Position;
import model.ReadOnlyRoom;
import model.Room;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A randomized maze both in terms of room layout
 * as well as the layout of entities such as pits
 * within the maze.
 * @author Liam Scholte
 *
 */
public class MazeImpl implements Maze {
  
  private final int rows;
  private final int cols;
  private final int roomCount;
         
  private List<Room> allRooms;
  private List<Room> nonHallwayRooms;
  
  private Random random;
  
  /**
   * Constructs a randomized maze.
   * @param rows the number of rows in the maze
   * @param cols the number of columns in the maze
   * @param batCount the number of bats in the maze
   * @param pitCount the number of pits in the maze
   * @param wraps whether or not the rooms on the edge of the
   *      maze wrap to the opposite side
   * @throws IllegalArgumentException if rows or columns is not positive
   * @throws IllegalArgumentException if the number of bats or pits is negative
   * @throws IllegalArgumentException if dimensions of the maze could not possibly
   *      support all the required entities (pits, bats, and wumpus)
   * @throws MazeGenerationException if the maze generation is unable to create
   *      a random maze with the required number of non-hallway rooms
   */
  public MazeImpl(
      int rows,
      int cols,
      int batCount,
      int pitCount,
      boolean wraps,
      Random random) throws IllegalArgumentException, MazeGenerationException {
    if (rows < 1 || cols < 1) {
      throw new IllegalArgumentException(
          "Number of rows and columns must both be positive");
    }
    
    if (batCount < 0 || pitCount < 0) {
      throw new IllegalArgumentException(
          "Number of pits and bats must not be negative");
    }
    
    int minRooms = Math.max(1 + pitCount, batCount);    
    if (minRooms > rows * cols) {
      throw new IllegalArgumentException(
          "The dimensions of the maze do not support the required number of rooms");
    }
    
    this.rows = rows;
    this.cols = cols;
    this.roomCount = rows * cols;
    this.random = random;
        
    generateMaze(wraps, minRooms);
    
    addRoomEntities(batCount, pitCount);
    
  }
  
  @Override
  public List<ReadOnlyRoom> getRooms() {
    return nonHallwayRooms.stream().collect(Collectors.toList());
  }
  
  @Override
  public List<Room> getMutableRooms() {
    return nonHallwayRooms;
  }
  
  @Override
  public List<ReadOnlyRoom> getExploredRooms() {
    return allRooms.stream().filter(room -> room.isExplored()).collect(Collectors.toList());
  }
  
  @Override
  public Dimension getSize() {
    return new Dimension(cols, rows);
  }
  
  /**
   * Gets the number of remaining walls for an NxM perfect maze.
   * @param rows the number of rows
   * @param cols the number of columns
   * @return the number of walls in a perfect maze of this size
   */
  private static int getPerfectMazeEdgeCount(int rows, int cols) {
    int totalEdges = getInitialEdgeCount(rows, cols);
    return totalEdges - rows * cols + 1;
  }
  
  private static int getInitialEdgeCount(int rows, int cols) {
    return (cols - 1) * rows + (rows - 1) * cols;
  }
  
  private Room[] generateRooms(GraphNode[] nodes) {    
    Room[] rooms = new Room[nodes.length];
    int i = 0;
    for (GraphNode node : nodes) {
      rooms[i] = new Room(new Position(node.x, node.y));
      ++i;
    }
    
    return rooms;
  }
  
  private GraphNode[] generateNodes() {
    GraphNode[] nodes = new GraphNode[rows * cols];
    int index = 0;
    for (int y = 0; y < rows; ++y) {
      for (int x = 0; x < cols; ++x) {
        nodes[index] = new GraphNode(index, x, y);
        ++index;
      }
    } 
    return nodes;
  }
  
  private List<GraphEdge> generateEdges(GraphNode[] nodes) {
    int initialEdgeCount = getInitialEdgeCount(rows, cols);
    List<GraphEdge> edges = new ArrayList<GraphEdge>(initialEdgeCount);
    for (int x = 0; x < cols - 1; ++x) {
      for (int y = 0; y < rows; ++y) {
        GraphNode nodeA = nodes[getRoomIndex(y, x)];
        GraphNode nodeB = nodes[getRoomIndex(y, x + 1)];
        edges.add(new GraphEdge(nodeA, nodeB));
      }
    }
    for (int x = 0; x < cols; ++x) {
      for (int y = 0; y < rows - 1; ++y) {
        GraphNode nodeA = nodes[getRoomIndex(y, x)];
        GraphNode nodeB = nodes[getRoomIndex(y + 1, x)];
        edges.add(new GraphEdge(nodeA, nodeB));
      }
    }
    
    return edges;
  }
  
  private void connectRooms(Room roomA, Room roomB, GraphEdge edge) {
    if (edge.nodeA.x < edge.nodeB.x) {
      roomA.setNeighbour(Direction.EAST, roomB);
    }
    else if (edge.nodeA.x > edge.nodeB.x) {
      roomA.setNeighbour(Direction.WEST, roomB);
    }
    else if (edge.nodeA.y < edge.nodeB.y) {
      roomA.setNeighbour(Direction.SOUTH, roomB);
    }
    else {
      roomA.setNeighbour(Direction.NORTH, roomB);
    }
  }
  
  private void wrapRooms(Room[] rooms) {
    for (int i = 0; i < cols; ++i) {
      Room northRoom = rooms[i];
      Room southRoom = rooms[i + (rows - 1) * cols];
      
      northRoom.setNeighbour(Direction.NORTH, southRoom);
      southRoom.setNeighbour(Direction.SOUTH, northRoom);
    }
    
    for (int i = 0; i <= (rows - 1) * cols; i += cols) {
      Room westRoom = rooms[i];
      Room eastRoom = rooms[i + (cols - 1)];
      
      westRoom.setNeighbour(Direction.WEST, eastRoom);
      eastRoom.setNeighbour(Direction.EAST, westRoom);
    }
  }
  
  private void addRoomEntities(int batCount, int pitCount) {
    //Generate a sequence of numbers from [0, N) and then shuffle the sequence
    List<Integer> roomIndices =
        IntStream
            .range(0, nonHallwayRooms.size())
            .boxed()
            .collect(Collectors.toList());
    Collections.shuffle(roomIndices, random);
    
    //Add the bats to random rooms
    for (int i = 0; i < batCount; ++i) {
      nonHallwayRooms.get(roomIndices.get(i)).addBats(this);
    }
    
    Collections.shuffle(roomIndices, random);
    
    //Add the wumpus to a random room
    Room room = nonHallwayRooms.get(roomIndices.get(0));
    room.addWumpus();
    
    //Add the pits to random rooms
    for (int i = 1; i < 1 + pitCount; ++i) {
      room = nonHallwayRooms.get(roomIndices.get(i));
      room.addPit();
    }
    
  }
  
  private void generateMaze(boolean wraps, int minRooms) throws MazeGenerationException {    
    GraphNode[] nodes = generateNodes();
    List<GraphEdge> edges = generateEdges(nodes);
    
    Collections.shuffle(edges, random);
    
    //Gives each node its own tree to start
    DisjointSetForest trees = new DisjointSetForest(roomCount);
    
    
    Room[] rooms = generateRooms(nodes);
    
    //Start by creating a perfect maze by creating a spanning tree
    //by joining trees together until there is one giant tree
    List<GraphEdge> savedEdges = new ArrayList<GraphEdge>();
    while (!edges.isEmpty() && trees.size() > 1) {

      //Remove the last edge, which is essentially a random
      //edge because the list has been shuffled
      GraphEdge edge = edges.remove(edges.size() - 1);

      //Look for the tree that contains node A
      //and the tree that contains node B.
      int a = trees.find(edge.nodeA.index);
      int b = trees.find(edge.nodeB.index);

      //If nodes A and B are not in same tree
      if (a != b) {
        //Combines the trees into one
        trees.union(a, b);
        
        Room roomA = rooms[edge.nodeA.index];
        Room roomB = rooms[edge.nodeB.index];
        
        connectRooms(roomA, roomB, edge);
      }
      else {
        //Both nodes are already in the same set of nodes, so
        //connecting these rooms would create a non-perfect maze
        savedEdges.add(edge);
      }
    }
    
    //Copy over any remaining edges to saved edges
    for (GraphEdge edge : edges) {
      savedEdges.add(edge);
    }
    
    //Remove remaining edges until the desired number of remaining
    //walls is reached
    while (savedEdges.size() > getPerfectMazeEdgeCount(rows, cols)) {
      GraphEdge edge = savedEdges.remove(savedEdges.size() - 1);
            
      Room roomA = rooms[edge.nodeA.index];
      Room roomB = rooms[edge.nodeB.index];
            
      connectRooms(roomA, roomB, edge);
    }
    
    //If wrapping was specified,
    //then attach each outside room to it's
    //corresponding opposite room
    if (wraps) {
      wrapRooms(rooms);      
    }
    
    allRooms = new ArrayList<Room>();
    
    Set<Room> tunnels = new HashSet<Room>();
    Set<Room> caves = new LinkedHashSet<Room>();
    for (Room room : rooms) {
      if (room.isHallway()) {
        tunnels.add(room);
      }
      else {
        caves.add(room);
      }
      allRooms.add(room);
    }
    
    Iterator<GraphEdge> edgeIterator = savedEdges.iterator();
    while (caves.size() < minRooms && edgeIterator.hasNext()) {
      GraphEdge edge = edgeIterator.next();
      Room roomA = rooms[edge.nodeA.index];
      Room roomB = rooms[edge.nodeB.index];
      
      boolean shouldConnectRooms = false;
      if (roomA.isHallway() && roomB.getExitCount() != 1) {
        caves.add(roomA);
        tunnels.remove(roomA);
        shouldConnectRooms = true;
      }
      
      if (roomB.isHallway() && roomA.getExitCount() != 1) {
        caves.add(roomB);
        tunnels.remove(roomB);
        shouldConnectRooms = true;
      }
      
      if (shouldConnectRooms) {
        connectRooms(roomA, roomB, edge);
        edgeIterator.remove();
      }
    }
    
    if (caves.size() < minRooms) {
      throw new MazeGenerationException(
          "A maze with the required number of rooms could not be generated");
    }

    nonHallwayRooms = new ArrayList<Room>(caves);
  }
  
  private int getRoomIndex(int row, int col) { 
    return row * cols + col;
  }
  
  /**
   * A helper class that represents a node in a graph.
   * @author Liam Scholte
   *
   */
  private static class GraphNode {
    private final int index;
    private final int x;
    private final int y;
    
    public GraphNode(int index, int x, int y) {
      this.index = index;
      this.x = x;
      this.y = y;
    }
  }
  
  /**
   * A helper class that represents an edge between nodes
   * in a graph.
   * @author Liam Scholte
   *
   */
  private static class GraphEdge {
    private final GraphNode nodeA;
    private final GraphNode nodeB;
    
    public GraphEdge(GraphNode a, GraphNode b) {
      nodeA = a;
      nodeB = b;
    }      
  }
  
  /**
   * A helper class for storing nodes in sets.
   * It represents a forest of trees when building
   * up a spanning tree using Kruskal's algorithm.
   * @author Liam Scholte
   *
   */
  private static class DisjointSetForest {

    private final int[] parent;
    private final int[] rank;
    private int numSets;

    public DisjointSetForest(int n) {
      numSets = n;
      parent = new int[n];
      rank = new int[n];
      for (int i = 0; i < n; ++i) {
        makeSet(i);
      }
    }

    public final int size() {
      return numSets;
    }

    public final int find(int x) {
      if (parent[x] == x) {
        return x;
      }
      return find(parent[x]);
    }

    public final void union(int x, int y) {
      int xRoot = find(x);
      int yRoot = find(y);

      if (xRoot == yRoot) {
        //x and y are already in same set, so we don't need to union anything
        return;
      }

      --numSets;

      //Adds a tree of nodes to another
      //tree of nodes, creating a larger tree
      if (rank[xRoot] < rank[yRoot]) {
        parent[xRoot] = yRoot;
      }
      else if (rank[xRoot] > rank[yRoot]) {
        parent[yRoot] = xRoot;
      }
      else {
        parent[yRoot] = xRoot;
        ++rank[xRoot];
      }
    }
    
    private void makeSet(int x) {
      parent[x] = x;
      rank[x] = 0;
    }
  }

}
