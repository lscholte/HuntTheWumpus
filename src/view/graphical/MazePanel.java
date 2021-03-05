package view.graphical;

import model.Direction;
import model.Position;
import model.ReadOnlyGame;
import model.ReadOnlyRoom;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * A panel for drawing the cave system for the Hunt the Wumpus game.
 * @author Liam Scholte
 *
 */
public class MazePanel extends JPanel {
  
  private static final long serialVersionUID = -4926651860810458888L;

  private static final int ROOM_DIMENSION = 50;
  
  private BufferedImage playerImage;
  private BufferedImage batsImage;
  private BufferedImage wumpusImage;
  private BufferedImage pitImage;
  
  private BufferedImage wumpusNearbyImage;
  private BufferedImage pitNearbyImage;
  
  private BufferedImage cave4Image;
  private BufferedImage cave3Image;
  private BufferedImage cave1Image;
  private BufferedImage cave0Image;
  
  private BufferedImage hallwayStraightImage;
  private BufferedImage hallwayCurvedImage;
  
  private ReadOnlyGame game;
    
  /**
   * Constructs a maze panel.
   * @param game the game to draw
   */
  public MazePanel(ReadOnlyGame game) {
    this.game = game;
    
    this.setBorder(BorderFactory.createEtchedBorder());
    this.setBackground(Color.WHITE);
    
    this.setPreferredSize(
        new Dimension(
            game.getMaze().getSize().width * ROOM_DIMENSION,
            game.getMaze().getSize().height * ROOM_DIMENSION));

    try {
      playerImage = ImageIO.read(new File("player.png")); 
      batsImage = ImageIO.read(new File("superbat.png"));
      wumpusImage = ImageIO.read(new File("wumpus.png"));
      pitImage = ImageIO.read(new File("pit.png"));
      
      wumpusNearbyImage = ImageIO.read(new File("wumpus-nearby.png"));
      pitNearbyImage = ImageIO.read(new File("pit-nearby.png"));

      
      cave4Image = ImageIO.read(new File("room-4.png")); 
      cave3Image = ImageIO.read(new File("room-3.png")); 
      cave1Image = ImageIO.read(new File("room-1.png")); 
      cave0Image = ImageIO.read(new File("room-0.png")); 

      hallwayStraightImage = ImageIO.read(new File("tunnel-straight.png")); 
      hallwayCurvedImage = ImageIO.read(new File("tunnel-curved.png")); 

    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
    
    game.getPlayers().forEach(player -> player.getPositionChangedEvent().addListener(() -> {
      revalidate();
      repaint();
    }));
  }
  
  
  @Override
  protected void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);
    
    Graphics2D graphics2d = (Graphics2D)graphics;
    graphics2d.drawRect(
        0,
        0,
        game.getMaze().getSize().width * ROOM_DIMENSION,
        game.getMaze().getSize().height * ROOM_DIMENSION);

    for (ReadOnlyRoom room : game.getMaze().getExploredRooms()) {
      int xPosition = room.getPosition().getX() * ROOM_DIMENSION;
      int yPosition = room.getPosition().getY() * ROOM_DIMENSION;
      Set<Direction> directions = room.getAvailableDirections();
      if (room.isHallway()) {
        if (directions.containsAll(EnumSet.of(Direction.NORTH, Direction.SOUTH))) {
          graphics2d.drawImage(
              getRotatedImage(hallwayStraightImage, 90),
              xPosition,
              yPosition,
              ROOM_DIMENSION,
              ROOM_DIMENSION,
              null);
        }
        else if (directions.containsAll(EnumSet.of(Direction.EAST, Direction.WEST))) {
          graphics2d.drawImage(
              hallwayStraightImage,
              xPosition,
              yPosition,
              ROOM_DIMENSION,
              ROOM_DIMENSION,
              null);
        }
        else if (directions.containsAll(EnumSet.of(Direction.EAST, Direction.SOUTH))) {
          graphics2d.drawImage(
              hallwayCurvedImage,
              xPosition,
              yPosition,
              ROOM_DIMENSION,
              ROOM_DIMENSION,
              null);
        }
        else if (directions.containsAll(EnumSet.of(Direction.WEST, Direction.SOUTH))) {
          graphics2d.drawImage(
              getRotatedImage(hallwayCurvedImage, 90),
              xPosition,
              yPosition,
              ROOM_DIMENSION,
              ROOM_DIMENSION,
              null);
        }
        else if (directions.containsAll(EnumSet.of(Direction.EAST, Direction.NORTH))) {
          graphics2d.drawImage(
              getRotatedImage(hallwayCurvedImage, -90),
              xPosition,
              yPosition,
              ROOM_DIMENSION,
              ROOM_DIMENSION,
              null);
        }
        else {
          graphics2d.drawImage(
              getRotatedImage(hallwayCurvedImage, 180),
              xPosition,
              yPosition,
              ROOM_DIMENSION,
              ROOM_DIMENSION,
              null);
        }
      }
      else {
        if (directions.size() == 4) {
          graphics2d.drawImage(
              cave4Image,
              xPosition,
              yPosition,
              ROOM_DIMENSION,
              ROOM_DIMENSION,
              null);          
        }
        else if (directions.size() == 3) {
          if (directions.containsAll(EnumSet.of(Direction.NORTH, Direction.EAST, Direction.WEST))) {
            graphics2d.drawImage(
                getRotatedImage(cave3Image, 180),
                xPosition,
                yPosition,
                ROOM_DIMENSION,
                ROOM_DIMENSION,
                null);
          }
          else if (directions.containsAll(
              EnumSet.of(Direction.NORTH, Direction.EAST, Direction.SOUTH))) {
            graphics2d.drawImage(
                getRotatedImage(cave3Image, -90),
                xPosition,
                yPosition,
                ROOM_DIMENSION,
                ROOM_DIMENSION,
                null);
          }
          else if (directions.containsAll(
              EnumSet.of(Direction.WEST, Direction.EAST, Direction.SOUTH))) {
            graphics2d.drawImage(
                cave3Image,
                xPosition,
                yPosition,
                ROOM_DIMENSION,
                ROOM_DIMENSION,
                null);
          }
          else {
            graphics2d.drawImage(
                getRotatedImage(cave3Image, 90),
                xPosition,
                yPosition,
                ROOM_DIMENSION,
                ROOM_DIMENSION,
                null);
          }
        }
        else if (directions.size() == 1) {
          if (directions.containsAll(EnumSet.of(Direction.EAST))) {
            graphics2d.drawImage(
                cave1Image,
                xPosition,
                yPosition,
                ROOM_DIMENSION,
                ROOM_DIMENSION,
                null);          
          }
          else if (directions.containsAll(EnumSet.of(Direction.SOUTH))) {
            graphics2d.drawImage(
                getRotatedImage(cave1Image, 90),
                xPosition,
                yPosition,
                ROOM_DIMENSION,
                ROOM_DIMENSION,
                null);
          }
          else if (directions.containsAll(EnumSet.of(Direction.WEST))) {
            graphics2d.drawImage(
                getRotatedImage(cave1Image, 180),
                xPosition,
                yPosition,
                ROOM_DIMENSION,
                ROOM_DIMENSION,
                null);
          }
          else if (directions.containsAll(EnumSet.of(Direction.NORTH))) {
            graphics2d.drawImage(
                getRotatedImage(cave1Image, -90),
                xPosition,
                yPosition,
                ROOM_DIMENSION,
                ROOM_DIMENSION,
                null);
          }
        }
        else {
          graphics2d.drawImage(
              cave0Image,
              xPosition,
              yPosition,
              ROOM_DIMENSION,
              ROOM_DIMENSION,
              null);
        }
      }
    
      //Draw the hazards in the current room (wumpus/pit/bat)
      if (room.hasWumpus()) {
        graphics2d.drawImage(
            wumpusImage,
            xPosition,
            yPosition,
            ROOM_DIMENSION,
            ROOM_DIMENSION,
            null);
      }
      
      if (room.hasPit()) {
        graphics2d.drawImage(
            pitImage,
            xPosition,
            yPosition,
            ROOM_DIMENSION,
            ROOM_DIMENSION,
            null);
      }
      
      if (room.hasBats()) {
        graphics2d.drawImage(
            batsImage,
            xPosition,
            yPosition,
            ROOM_DIMENSION,
            ROOM_DIMENSION,
            null);
      }
      
      //Draw warnings for hazards in nearby rooms (nearby wumpus/pit)
      if (room.isWumpusNearby()) {
        graphics2d.drawImage(
            wumpusNearbyImage,
            xPosition,
            yPosition,
            ROOM_DIMENSION,
            ROOM_DIMENSION,
            null);
      }
      if (room.isPitNearby()) {
        graphics2d.drawImage(
            pitNearbyImage,
            xPosition,
            yPosition,
            ROOM_DIMENSION,
            ROOM_DIMENSION,
            null);
      }
    }
    
    //Draw the players in the current positions
    game.getPlayers().forEach(
        player ->
            graphics2d.drawImage(
                playerImage,
                player.getRoom().getPosition().getX() * ROOM_DIMENSION,
                player.getRoom().getPosition().getY() * ROOM_DIMENSION,
                ROOM_DIMENSION, 
                ROOM_DIMENSION,
                null));
  }
  
  /**
   * Converts a point in a drawable area to a cave position in the
   * maze that this view represents.
   * @param point a position in the drawable area
   * @return the corresponding cave position
   */
  public Position getRoomPositionFromDrawingPosition(Point point) {
    return new Position(point.x / ROOM_DIMENSION, point.y / ROOM_DIMENSION);
  }
  
  private BufferedImage getRotatedImage(BufferedImage inputImage, int degrees) {
    double rotationRequired = Math.toRadians(degrees);
    double locationX = cave1Image.getWidth() / 2;
    double locationY = cave1Image.getHeight() / 2;
    AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
    
    return op.filter(inputImage, null);
  }

}
