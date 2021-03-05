package game;

import controller.Controller;
import controller.GameFactoryImpl;
import view.View;
import view.graphical.GraphicalView;
import view.text.TextView;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * A driver that creates a game of Hunt the Wumpus.
 * @author Liam Scholte
 *
 */
public class Driver {
  
  /**
   * Entry point for the program.
   * @param args the command line arguments for
   *      the program
   */
  public static void main(String[] args) throws IOException {
    
    if (args.length != 1) {
      System.out.println("Invalid arguments. Expected either '--text' or '--gui'");
      return;
    }
    
    boolean guiMode = false;
    if (args[0].equals("--gui")) {
      guiMode = true;
    }
    else if (!args[0].equals("--text")) {
      System.out.println("Invalid arguments. Expected either '--text' or '--gui'");
      return;
    }
    
    View view;
    if (guiMode) {
      view = new GraphicalView();
    }
    else {
      view = new TextView(new InputStreamReader(System.in), System.out);
    }
    
    Controller controller = new Controller(view, new GameFactoryImpl());
    controller.start();
  }
}
