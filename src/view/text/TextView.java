package view.text;

import controller.Features;
import controller.GameCreator;
import model.Direction;
import model.ReadOnlyGame;
import model.maze.MazeGenerationException;
import model.player.ReadOnlyPlayer;
import view.View;

import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Represents a text-based representation of the Hunt the Wumpus game.
 * User interaction with the game is achieved through a series of text
 * inputs and outputs.
 * @author Liam Scholte
 *
 */
public class TextView implements View {
  
  private ReadOnlyGame game;

  private Appendable output;  
  private Scanner scanner;
    
  /**
   * Constructs a text-based view of the game.
   * @param input a source of input commands
   * @param output the output where prompts and the state
   *      of the game will be written to
   * @throws IllegalArgumentException if any parameters
   *      are null
   */
  public TextView(Readable input, Appendable output) throws IllegalArgumentException {
    if (input == null) {
      throw new IllegalArgumentException(
          "Input must not be null");
    }
    if (output == null) {
      throw new IllegalArgumentException(
          "Output must not be null");
    }

    this.output = output;
    this.scanner = new Scanner(input);    
  }

  @Override
  public void refresh() {
    //Not used -- this view is synchronous
  }
  
  @Override
  public void presentConfiguration(GameCreator gameCreator) {
    Integer rowCount = null;
    Integer colCount = null;
    Boolean wraps = null;
    Integer batCount = null;
    Integer pitCount = null;
    Integer playerCount = null;
    Integer arrowCount = null;
    Integer seed = null;
    try {
      boolean gameGenerated = false;
      do {
        try {
          rowCount = askForInteger("Enter the number of rows");
          colCount = askForInteger("Enter the number of columns");
          wraps = askForBoolean("Does the maze wrap? (yes/no)");        
          batCount = askForInteger("Enter the number of bats");
          pitCount = askForInteger("Enter the number of pits");
          playerCount = askForInteger("Enter the number of players");
          arrowCount = askForInteger("Enter the number of arrows per player");
          seed = askForInteger("Enter the random generation seed");
        }
        catch (NoSuchElementException e) {
          output.append("Input ran out").append(System.lineSeparator());
          return;
        }
        
        try {
          gameCreator.createGame(
              rowCount,
              colCount,
              wraps,
              batCount,
              pitCount,
              playerCount,
              arrowCount,
              seed);
          gameGenerated = true;
        }
        catch (IllegalArgumentException e) {
          output
              .append("Failed to create the game.")
              .append(System.lineSeparator())
              .append(e.getMessage())
              .append(System.lineSeparator());
        }
        catch (MazeGenerationException e) {
          output
              .append("A random maze could not be generated.")
              .append(System.lineSeparator())
              .append(
                  "Try again or consider specifying larger maze "
                  + "dimensions and/or fewer pits or bats.")
              .append(System.lineSeparator());
        }
      }
      while (!gameGenerated);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }    
  }
  
  @Override
  public void presentGame(ReadOnlyGame game, Features features) {
    this.game = game;
    registerEventHandlers();

    Map<String, Runnable> commands = registerCommands(features);
    
    try {
      output.append("Starting Hunt the Wumpus...").append(System.lineSeparator());
      try {
        while (!game.isOver()) {
          ReadOnlyPlayer currentPlayer = game.getCurrentPlayer();
          output
              .append(System.lineSeparator())
              .append("Current Player: " + currentPlayer.getName())
              .append(System.lineSeparator())
              .append(String.format(
                  "%s is in cave at position %s",
                  currentPlayer.getName(),
                  currentPlayer.getRoom().getPosition()))
              .append(System.lineSeparator())      
              .append(currentPlayer.getName() + " has ")
              .append(Integer.toString(currentPlayer.getArrowCount()))
              .append(" arrows remaining")
              .append(System.lineSeparator());
      
          if (currentPlayer.getRoom().isWumpusNearby()) {
            output
                .append(currentPlayer.getName() + " smells a wumpus nearby")
                .append(System.lineSeparator());
          }
          if (currentPlayer.getRoom().isPitNearby()) {
            output
                .append(currentPlayer.getName() + " feels the breeze of a pit nearby")
                .append(System.lineSeparator());
          }
          
          String directions = currentPlayer
              .getRoom()
              .getAvailableDirections()
              .stream()
              .map(direction -> direction.toString().toLowerCase())
              .collect(Collectors.joining(", "));
          output
              .append("Available directions: " + directions)
              .append(System.lineSeparator());
          
          Runnable runnable = null;
          try {
            runnable = commands.get(scanner.next());
          }
          catch (NoSuchElementException e) {
            throw e;
          }
          catch (Exception e) {
            //Do nothing -- command will be null, so unrecognized
          }
        
          if (runnable == null) {
            output
                .append("Unrecognized command")
                .append(System.lineSeparator());
            continue;
          }
          
          try {
            runnable.run();
          }
          catch (Exception e) {
            output
                .append(e.getMessage())
                .append(System.lineSeparator());
          }
        } 
      }
      catch (NoSuchElementException e) {
        output.append("Input ran out").append(System.lineSeparator());
      }
      
      if (game.isOver()) {
        displayGameOver();
      }
      else {
        output.append("The game is unfinished").append(System.lineSeparator());  
      }
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  private int askForInteger(String message) throws IOException {
    Integer result = null;
    do {
      try {
        output.append(message).append(System.lineSeparator());
        result = scanner.nextInt();
      }
      catch (InputMismatchException e) {
        output.append("Invalid input").append(System.lineSeparator());
        scanner.next();
      }
    }
    while (result == null);
    return result;
  }
  
  private boolean askForBoolean(String message) throws IOException {
    Boolean result = null;
    do {
      output.append(message).append(System.lineSeparator());
      String resultString = scanner.next();
      if (resultString.equalsIgnoreCase("yes")) {
        result = true;
      }
      else if (resultString.equalsIgnoreCase("no")) {
        result = false;
      }
      else {
        output.append("Invalid input").append(System.lineSeparator());
      }
    }
    while (result == null);
    
    return result;
  }
  
  private void registerEventHandlers() {
    for (ReadOnlyPlayer player : game.getPlayers()) {
      
      player
          .getPositionChangedEvent()
          .addListener(wrapThrowingRunnable(
              () -> {
                output
                    .append(
                        String.format(
                            "%s has moved to cave at position %s",
                            player.getName(),
                            player.getRoom().getPosition().toString()))
                    .append(System.lineSeparator());
              }));
      
      player
          .getKilledByWumpusEvent()
          .addListener(wrapThrowingRunnable(
              () -> {
                output
                    .append(player.getName() + " has been killed by a wumpus")
                    .append(System.lineSeparator());
              }));
      
      player
          .getFellIntoPitEvent()
          .addListener(wrapThrowingRunnable(
              () -> {
                output
                    .append(player.getName() + " has fallen into a pit and died")
                    .append(System.lineSeparator());
              }));

      player
          .getKilledWumpusEvent()
          .addListener(wrapThrowingRunnable(
              () -> {
                output
                    .append(player.getName() + " has killed the wumpus")
                    .append(System.lineSeparator());
              }));

      player
          .getArrowMissedEvent()
          .addListener(wrapThrowingRunnable(
              () -> {
                output
                    .append(player.getName() + " has missed a shot")
                    .append(System.lineSeparator());
              }));
    }
  }
  
  private Map<String, Runnable> registerCommands(Features features) {
    Map<String, Runnable> commands = new HashMap<String, Runnable>();
    
    commands.put(
        "move",
        () -> features.move(parseDirection(scanner.next())));
    commands.put(
        "shoot",
        () -> features.shootArrow(
            parseDirection(scanner.next()),
            scanner.nextInt()));
    commands.put(
        "hint",
        wrapThrowingRunnable(() -> {
          if (game.isWinnable()) {
            output.append("The game is winnable").append(System.lineSeparator());
          }
          else {
            output.append("The game is not winnable").append(System.lineSeparator());
          }
        }));
    
    commands.put("quit", () -> features.suicide());
    
    return commands;
  }
  
  private Direction parseDirection(String directionString) {
    try {
      return Direction.valueOf(directionString.toUpperCase());
    }
    catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(
          String.format(
              "'%s' is not a direction",
              directionString));
    }
  }
  
  private void displayGameOver() throws IOException {
    ReadOnlyPlayer winner = game.getWinner();
    if (winner != null) {
      output.append(winner.getName() + " has won the game").append(System.lineSeparator());  
    }
    else {
      output.append("The game is over").append(System.lineSeparator());  
    }
  }
  
  private Runnable wrapThrowingRunnable(ThrowingRunnable runnable) {
    return () -> {
      try {
        runnable.runThatThrows();
      }
      catch (Throwable e) {
        throw new RuntimeException(e);
      }
    };
  }
  
  private interface ThrowingRunnable {
  
    public void runThatThrows() throws Throwable;
  }

}
