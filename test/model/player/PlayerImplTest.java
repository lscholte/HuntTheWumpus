package model.player;

import model.Direction;
import model.Position;
import model.Room;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the PlayerImpl class.
 * 
 * @author Liam Scholte
 *
 */
public class PlayerImplTest {

  private Room roomWithoutNeighbours;
  private Room roomWithNeighbours;
  private Room roomToNorth;
  private Room roomToWest;

  private Position position;
  private String name;

  /**
   * Sets up common objects for testing.
   */
  @Before
  public void setup() {
    name = "Player";
    position = new Position(1, 2);

    roomWithoutNeighbours = new Room(position);

    roomToNorth = new Room(position);
    roomToWest = new Room(position);
    roomWithNeighbours = new Room(position);

    roomWithNeighbours.setNeighbour(Direction.NORTH, roomToNorth);
    roomWithNeighbours.setNeighbour(Direction.SOUTH, new Room(position));
    roomWithNeighbours.setNeighbour(Direction.EAST, new Room(position));
    roomWithNeighbours.setNeighbour(Direction.WEST, roomToWest);
  }

  /**
   * Tests that a player cannot be constructed with a null room.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithNullRoom() {
    new PlayerImpl(name, null, 1);
  }

  /**
   * Tests that a player cannot be constructed with 0 arrows.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithNoArrows() {
    new PlayerImpl(name, roomWithoutNeighbours, 0);
  }

  /**
   * Tests that constructing a player properly initializes the player.
   */
  @Test
  public void testConstructor() {
    PlayerImpl player = new PlayerImpl(name, roomWithoutNeighbours, 1);

    Assert.assertNotNull(player.getArrowMissedEvent());
    Assert.assertNotNull(player.getDodgedBatsEvent());
    Assert.assertNotNull(player.getFellIntoPitEvent());
    Assert.assertNotNull(player.getKilledByWumpusEvent());
    Assert.assertNotNull(player.getKilledWumpusEvent());
    Assert.assertNotNull(player.getTakenByBatsEvent());

    Assert.assertTrue(player.isAlive());

    Assert.assertEquals(1, player.getArrowCount());
  }

  /**
   * Tests that the player can correctly move between adjacent rooms.
   */
  @Test
  public void testMove() {
    PlayerImpl player = new PlayerImpl(name, roomWithNeighbours, 1);
            
    try {
      player.move(Direction.NORTH);
      player.move(Direction.SOUTH);
      
      player.move(Direction.SOUTH);
      player.move(Direction.NORTH);
      
      player.move(Direction.EAST);
      player.move(Direction.WEST);
      
      player.move(Direction.WEST);
      player.move(Direction.EAST);
    }
    catch (IllegalStateException e) {
      Assert.fail("Player should be able to move to each of these rooms");
    }
  }
  
  /**
  * Tests that the player cannot move in a direction with no room.
  */
  @Test(expected = IllegalStateException.class)
  public void testCannotMoveInDirection() {
    PlayerImpl player = new PlayerImpl(name, this.roomToNorth, 1);
    player.move(Direction.NORTH);
  }

  /**
   * Tests that the player cannot move when dead.
   */
  @Test(expected = IllegalStateException.class)
  public void testMoveWhenDead() {
    PlayerImpl player = new PlayerImpl(name, roomWithNeighbours, 1);

    player.kill();
    player.move(Direction.NORTH);
  }

  /**
   * Tests that the player can be killed.
   */
  @Test
  public void testKill() {
    PlayerImpl player = new PlayerImpl(name, roomWithNeighbours, 1);

    Assert.assertTrue(player.isAlive());

    player.kill();

    Assert.assertFalse(player.isAlive());
  }

  /**
   * Tests that the player cannot shoot an arrow with a distance of 0.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testShootArrowNoDistance() {
    PlayerImpl player = new PlayerImpl(name, roomWithNeighbours, 1);

    player.shootArrow(Direction.NORTH, 0);
  }

  /**
   * Tests that the player cannot shoot an arrow farther than it max shooting
   * distance.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testShootArrowTooFar() {
    PlayerImpl player = new PlayerImpl(name, roomWithNeighbours, 1);

    player.shootArrow(Direction.NORTH, 6);
  }

  /**
   * Tests that the player cannot shoot more arrows than it is holding.
   */
  @Test(expected = IllegalStateException.class)
  public void testShootTooManyArrows() {
    PlayerImpl player = new PlayerImpl(name, roomWithNeighbours, 1);

    player.shootArrow(Direction.NORTH, 1);

    Assert.assertEquals(0, player.getArrowCount());

    player.shootArrow(Direction.NORTH, 1);
  }

  /**
   * Tests that shooting an arrow past a wumpus does not kill the wumpus.
   */
  @Test
  public void testOvershootArrow() {
    Room roomTwoToNorth = new Room(position);

    roomToNorth.setNeighbour(Direction.NORTH, roomTwoToNorth);
    roomToNorth.setNeighbour(Direction.EAST, new Room(position));
    roomToNorth.addWumpus();

    boolean[] arrowHit = new boolean[1];
    boolean[] arrowMissed = new boolean[1];

    PlayerImpl player = new PlayerImpl(name, roomWithNeighbours, 1);
    player.getKilledWumpusEvent().addListener(() -> arrowHit[0] = true);
    player.getArrowMissedEvent().addListener(() -> arrowMissed[0] = true);

    player.shootArrow(Direction.NORTH, 2);

    Assert.assertFalse(arrowHit[0]);
    Assert.assertTrue(arrowMissed[0]);
  }

  /**
   * Tests that shooting an arrow that hits the wumpus kills the wumpus.
   */
  @Test
  public void testShootArrowHitWumpus() {
    Room roomTwoToNorth = new Room(position);
    roomTwoToNorth.addWumpus();

    roomToNorth.setNeighbour(Direction.NORTH, roomTwoToNorth);
    roomToNorth.setNeighbour(Direction.EAST, new Room(position));

    boolean[] arrowHit = new boolean[1];
    boolean[] arrowMissed = new boolean[1];

    PlayerImpl player = new PlayerImpl(name, roomWithNeighbours, 1);
    player.getKilledWumpusEvent().addListener(() -> arrowHit[0] = true);
    player.getArrowMissedEvent().addListener(() -> arrowMissed[0] = true);

    player.shootArrow(Direction.NORTH, 2);

    Assert.assertTrue(arrowHit[0]);
    Assert.assertFalse(arrowMissed[0]);
  }

  /**
   * Tests shooting an arrow through a tunnel that goes west then north then into
   * a room to the east. Then the arrow continues into a tunnel to the east that
   * leads north then west then back south to kill a wumpus.
   */
  @Test
  public void testShootCrookedArrowThatHitsWumpus() {
    Room roomTwoToNorth = new Room(position);

    roomToNorth.setNeighbour(Direction.NORTH, roomTwoToNorth);
    roomToNorth.addWumpus();

    roomToWest.setNeighbour(Direction.NORTH, new Room(position));
    roomToWest.visitNeighbour(Direction.NORTH).setNeighbour(Direction.EAST, roomToNorth);
    roomToNorth.setNeighbour(Direction.WEST, roomToWest.visitNeighbour(Direction.NORTH));

    roomToNorth.setNeighbour(Direction.EAST, new Room(position));
    roomToNorth.visitNeighbour(Direction.EAST).setNeighbour(Direction.NORTH, new Room(position));

    roomToNorth.visitNeighbour(Direction.EAST).visitNeighbour(Direction.NORTH)
        .setNeighbour(Direction.WEST, roomTwoToNorth);

    boolean[] arrowHit = new boolean[1];
    boolean[] arrowMissed = new boolean[1];

    PlayerImpl player = new PlayerImpl(name, roomWithNeighbours, 1);
    player.getKilledWumpusEvent().addListener(() -> arrowHit[0] = true);
    player.getArrowMissedEvent().addListener(() -> arrowMissed[0] = true);

    player.shootArrow(Direction.WEST, 2);

    Assert.assertTrue(arrowHit[0]);
    Assert.assertFalse(arrowMissed[0]);
  }

  /**
   * Tests shooting an arrow through a tunnel that goes west then north then into
   * a room to the east. Then the arrow continues into a tunnel to the east that
   * leads north then west into a room and then continues west into a wall.
   */
  @Test
  public void testShootCrookedArrowThatHitsWall() {

    roomToNorth.addWumpus();

    roomToWest.setNeighbour(Direction.NORTH, new Room(position));
    roomToWest.visitNeighbour(Direction.NORTH).setNeighbour(Direction.EAST, roomToNorth);
    roomToNorth.setNeighbour(Direction.WEST, roomToWest.visitNeighbour(Direction.NORTH));

    roomToNorth.setNeighbour(Direction.EAST, new Room(position));
    roomToNorth.visitNeighbour(Direction.EAST).setNeighbour(Direction.NORTH, new Room(position));

    Room roomTwoToNorth = new Room(position);
    roomToNorth.visitNeighbour(Direction.EAST).visitNeighbour(Direction.NORTH)
        .setNeighbour(Direction.WEST, roomTwoToNorth);

    boolean[] arrowHit = new boolean[1];
    boolean[] arrowMissed = new boolean[1];

    PlayerImpl player = new PlayerImpl(name, roomWithNeighbours, 1);
    player.getKilledWumpusEvent().addListener(() -> arrowHit[0] = true);
    player.getArrowMissedEvent().addListener(() -> arrowMissed[0] = true);

    player.shootArrow(Direction.WEST, 3);

    Assert.assertFalse(arrowHit[0]);
    Assert.assertTrue(arrowMissed[0]);
  }

  /**
   * Tests that the player cannot be placed in a null room.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testSetNullRoomTest() {
    PlayerImpl player = new PlayerImpl(name, roomWithNeighbours, 1);

    player.setRoom(null);
  }

  /**
   * Tests that the player cannot move rooms when dead.
   */
  @Test(expected = IllegalStateException.class)
  public void testSetRoomWhenDead() {
    PlayerImpl player = new PlayerImpl(name, roomWithNeighbours, 1);

    player.kill();
    player.setRoom(roomWithoutNeighbours);
  }

  /**
   * Tests that the player can successfully change rooms.
   */
  @Test
  public void testSetRoom() {
    PlayerImpl player = new PlayerImpl(name, roomWithNeighbours, 1);

    player.setRoom(roomWithoutNeighbours);
    
    Assert.assertEquals(roomWithoutNeighbours, player.getRoom());
  }
}
