package view.graphical;

import controller.Features;
import controller.GameCreator;
import model.Direction;
import model.Position;
import model.ReadOnlyGame;
import model.maze.MazeGenerationException;
import model.player.ReadOnlyPlayer;
import view.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.stream.IntStream;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

/**
 * Represents a graphical representation of the Hunt the Wumpus game.
 * This view presents the game as a series of caves or hallways that appear
 * as they become explored.
 * @author Liam Scholte
 *
 */
public class GraphicalView extends JFrame implements View {
  
  private static final long serialVersionUID = -8909564980994804649L;
  
  private JLayeredPane layeredPane;
  private GuiInterfacePanel gui;
  
  private ReadOnlyGame game;
  
  private JPanel panel;
    
  /**
   * Constructs a graphical view of a Hunt the Wumpus game.
   */
  public GraphicalView() {
    setTitle("Hunt the Wumpus");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    panel = new JPanel();
    add(panel);
    
    setSize(600, 400);
    setVisible(true);
  }

  @Override
  public void refresh() {
    if (game.getCurrentPlayer() != null) {
      gui.currentPlayerLabel.setText(
          String.format("<html><b>%s</b></html>", game.getCurrentPlayer().getName()));    
      gui.arrowCountLabel.setText("Arrows Remaining: " + game.getCurrentPlayer().getArrowCount()); 
    }
    
    if (game.isOver()) {
      JLabel label;
      ReadOnlyPlayer winner = game.getWinner();
      if (winner != null) {
        label = new JLabel(generateHtmlString(winner.getName() + " won!", 36, Color.GREEN));
      }
      else {
        label = new JLabel(generateHtmlString("Game Over!", 36, Color.RED));
      }
      label.setSize(600, 400);
      label.setHorizontalAlignment(SwingConstants.CENTER);
      label.setVerticalAlignment(SwingConstants.CENTER);
      layeredPane.add(label, BorderLayout.CENTER, 0);
      
      gui.setFocusable(false);
    }
    revalidate();
  }
  
  @Override
  public void presentConfiguration(GameCreator gameCreator) {
    panel.removeAll();
    
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    
    SpinnerModel rowCountModel =  new SpinnerNumberModel(5, 1, 100, 1);
    SpinnerModel colCountModel = new SpinnerNumberModel(5, 1, 100, 1);
    JComboBox<Boolean> wrapComboBox = new JComboBox<Boolean>(new Boolean[] { true, false});
    SpinnerModel batCountModel = new SpinnerNumberModel(2, 0, 100 * 100, 1);
    SpinnerModel pitCountModel = new SpinnerNumberModel(3, 0, 100 * 100, 1);
    SpinnerModel playerCountModel = new SpinnerNumberModel(1, 1, 2, 1);
    SpinnerModel arrowCountModel = new SpinnerNumberModel(1, 1, 5, 1);
    SpinnerModel seedModel = new SpinnerNumberModel(12345, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
    
    {
      JPanel rowPanel = new JPanel();
      JLabel label = new JLabel("Number of Rows");
      JSpinner spinner = new JSpinner(rowCountModel);
      
      rowPanel.add(label);
      rowPanel.add(spinner);
      
      panel.add(rowPanel);      
    }
    
    {
      JPanel rowPanel = new JPanel();
      JLabel label = new JLabel("Number of Columns");
      JSpinner spinner = new JSpinner(colCountModel);
      
      rowPanel.add(label);
      rowPanel.add(spinner);
      
      panel.add(rowPanel);
    }
    
    {
      JPanel rowPanel = new JPanel();
      JLabel label = new JLabel("Wrapping Maze");
      
      wrapComboBox.setFocusable(false);
      
      rowPanel.add(label);
      rowPanel.add(wrapComboBox);
      
      panel.add(rowPanel);
    }
    
    {
      JPanel rowPanel = new JPanel();
      JLabel label = new JLabel("Number of Bats");
      JSpinner spinner = new JSpinner(batCountModel);
      
      rowPanel.add(label);
      rowPanel.add(spinner);
      
      panel.add(rowPanel);
    }
    
    
    {
      JPanel rowPanel = new JPanel();
      JLabel label = new JLabel("Number of Pits");
      JSpinner spinner = new JSpinner(pitCountModel);
      
      rowPanel.add(label);
      rowPanel.add(spinner);
      
      panel.add(rowPanel);
    }
    
    
    {
      JPanel rowPanel = new JPanel();
      JLabel label = new JLabel("Number of Players");
      JSpinner spinner = new JSpinner(playerCountModel);
      
      rowPanel.add(label);
      rowPanel.add(spinner);
      
      panel.add(rowPanel);
    }
    
    {
      JPanel rowPanel = new JPanel();
      JLabel label = new JLabel("Number of Arrows");
      JSpinner spinner = new JSpinner(arrowCountModel);
      
      rowPanel.add(label);
      rowPanel.add(spinner);
      
      panel.add(rowPanel);
    }
    
    
    {
      JPanel rowPanel = new JPanel();
      JLabel label = new JLabel("Seed");
      JSpinner spinner = new JSpinner(seedModel);
      
      rowPanel.add(label);
      rowPanel.add(spinner);
      
      panel.add(rowPanel);
    }
    
    JLabel errorLabel = new JLabel();
        
    JButton startGameButton = new JButton("Start Game");
    startGameButton.setFocusable(true);
    startGameButton.addActionListener(e -> {
      String formattedErrorText = "";
      try {
        gameCreator.createGame(
            (int)rowCountModel.getValue(),
            (int)colCountModel.getValue(),
            (boolean)wrapComboBox.getSelectedItem(),
            (int)batCountModel.getValue(),
            (int)pitCountModel.getValue(),
            (int)playerCountModel.getValue(),
            (int)arrowCountModel.getValue(),
            (int)seedModel.getValue());
      }
      catch (IllegalArgumentException exception) {
        String errorText = "Failed to create the game."
            + exception.getMessage();
        formattedErrorText = generateHtmlString(errorText, 3, Color.RED);
      }
      catch (MazeGenerationException exception) {
        String errorText = "A random maze could not be generated."
            + "Try again with a different seed or consider specifying larger "
            + "maze dimensions and/or fewer pits or bats.";
        formattedErrorText = generateHtmlString(errorText, 3, Color.RED);
      }
      errorLabel.setText(formattedErrorText);
      revalidate();
    });
    
    panel.add(startGameButton);
    panel.add(errorLabel);

    add(panel);
    
    revalidate();
  }
  
  @Override
  public void presentGame(ReadOnlyGame game, Features features) {    
    panel.removeAll();
    
    this.game = game;
    
    layeredPane = new JLayeredPane();
    layeredPane.setLayout(new BorderLayout());
        
    gui = new GuiInterfacePanel(game);
    gui.setSize(600, 350);
    
    layeredPane.add(gui, BorderLayout.CENTER);
    
    gui.hintButton.addActionListener(e -> {
      String message;
      if (game.isWinnable()) {
        message = "The game is winnable for " + game.getCurrentPlayer().getName();
      }
      else {
        message = "The game is not winnable for " + game.getCurrentPlayer().getName();
      }
      JOptionPane.showMessageDialog(this, message, "Hint", JOptionPane.PLAIN_MESSAGE);
    });
    
    gui.suicideButton.addActionListener(e -> {
      try {
        features.suicide();
      }
      catch (Exception exception) {
        //Do nothing
      }
    });

    gui.shootButton.addActionListener(e -> {
      try {
        features.shootArrow(
            (Direction)gui.directionComboBox.getSelectedItem(),
            (int)gui.distanceComboBox.getSelectedItem());
      }
      catch (Exception exception) {
        //Do nothing
      }
    });

    gui.addKeyListener(new KeyListener() {

      @Override
      public void keyTyped(KeyEvent e) {
        //Do nothing
      }
  
      @Override
      public void keyPressed(KeyEvent e) {
        //Do nothing
      }
  
      @Override
      public void keyReleased(KeyEvent e) {
        try {
          switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_KP_RIGHT:
              features.move(Direction.EAST);
              break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_KP_LEFT:
              features.move(Direction.WEST);
              break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_KP_UP:
              features.move(Direction.NORTH);
              break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_KP_DOWN:
              features.move(Direction.SOUTH);
              break;
            default:
              break;
          } 
        }
        catch (Exception exception) {
          //Do nothing
        }
      }
    });
    
    gui.mazePanel.addMouseListener(new MouseListener() {

      @Override
      public void mouseClicked(MouseEvent e) {
        //Do nothing
      }

      @Override
      public void mouseEntered(MouseEvent e) {
        //Do nothing
      }

      @Override
      public void mouseExited(MouseEvent e) {
        //Do nothing
      }

      @Override
      public void mousePressed(MouseEvent e) {
        //Do nothing
      }

      @Override
      public void mouseReleased(MouseEvent e) {        
        Position currentPosition =
            game.getCurrentPlayer().getRoom().getPosition();
        Position targetRoomPosition =
            gui.mazePanel.getRoomPositionFromDrawingPosition(e.getPoint());
        
        try {
          if (targetRoomPosition.equals(
              new Position(currentPosition.getX() + 1, currentPosition.getY()))) {
            features.move(Direction.EAST);
          }
          else if (targetRoomPosition.equals(
              new Position(currentPosition.getX() - 1, currentPosition.getY()))) {
            features.move(Direction.WEST);
          }
          else if (targetRoomPosition.equals(
              new Position(currentPosition.getX(), currentPosition.getY() + 1))) {
            features.move(Direction.SOUTH);
          }
          else if (targetRoomPosition.equals(
              new Position(currentPosition.getX(), currentPosition.getY() - 1))) {
            features.move(Direction.NORTH);
          } 
        }
        catch (Exception exception) {
          //Do nothing
        }
      }
      
    });

    panel.add(layeredPane);
    gui.setFocusable(true);
    gui.requestFocusInWindow();
    
    refresh();
  }
  
  private String generateHtmlString(String text, int fontSize, Color fontColor) {
    return String.format(
        "<html><font size=\"%d\" color=\"#%06X\">%s</font></html>",
        fontSize,
        fontColor.getRGB() & 0x00FFFFFF,
        text);
  }
  
  private class GuiInterfacePanel extends JPanel {
            
    private static final long serialVersionUID = 5470558224106029260L;
    
    private JLabel currentPlayerLabel;
    private JLabel arrowCountLabel;
        
    private JButton shootButton;
    private JComboBox<Integer> distanceComboBox;
    private JComboBox<Direction> directionComboBox;
    
    private JButton hintButton;
    private JButton suicideButton;
    
    private MazePanel mazePanel;
      
    public GuiInterfacePanel(ReadOnlyGame game) {
            
      
      setLayout(new BorderLayout());
      mazePanel = new MazePanel(game);
      
      JPanel p = new JPanel(new GridBagLayout());
      p.add(mazePanel);
      
      add(new JScrollPane(p), BorderLayout.CENTER);
        
      currentPlayerLabel = new JLabel();
      arrowCountLabel = new JLabel();
      
      shootButton = new JButton("Shoot Arrow");
      shootButton.setFocusable(false);
      
      directionComboBox = new JComboBox<Direction>(Direction.values());
      directionComboBox.setFocusable(false);

      Integer[] possibleDistances =
          IntStream
              .range(1, game.getCurrentPlayer().getMaxShootDistance() + 1)
              .boxed()
              .toArray(Integer[]::new);
      
      distanceComboBox = new JComboBox<Integer>(possibleDistances);
      distanceComboBox.setFocusable(false);
            
      JPanel buttonsPanel = new JPanel();
      buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
      
      buttonsPanel.add(currentPlayerLabel);
      
      JPanel shootPanel = new JPanel();
      shootPanel.add(shootButton);
      shootPanel.add(distanceComboBox);
      shootPanel.add(directionComboBox);
      shootPanel.add(arrowCountLabel);
      
      buttonsPanel.add(shootPanel);
      
      hintButton = new JButton("Hint");
      hintButton.setFocusable(false);
      
      suicideButton = new JButton("Give Up");
      suicideButton.setFocusable(false);
      
      buttonsPanel.add(hintButton);
      buttonsPanel.add(suicideButton);
      
      add(buttonsPanel, BorderLayout.SOUTH);
      
      setFocusable(true);
    }
  }
}
