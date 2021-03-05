OVERVIEW
-------------------------------------------------
This allows you to play either a GUI or command line version of Hunt the Wumpus.




FEATURES
-------------------------------------------------
* Supports command line mode and GUI mode
* Supports 1 or 2 players
* Supports random generation of a cave system for Hunt the Wumpus
* Supports wrapping cave systems
* Supports an arbitrary number of pits and/or super bats (but requires exactly 1 wumpus)
* Supports an arbitrary number of arrows
* Supports shooting arrows through curved tunnels
* Supports detection if the game is in a winnable state


		
	
LIMITATIONS
-------------------------------------------------
* Since maze generation is randomized and users can specify number of pits and bats, it is possible that ordinarily valid parameters could be specified but the program is unable to generate a maze with the required number of caves. The program will abort and prompt the user to try again or change the parameters.
* The amount of wumpuses is not configurable (fixed at 1)
    * This simplifies aspects of the design such as not needing to remove a wumpus from the game and it simplifies determination of whether the game is winnable from the player's current position (order of killing wumpuses could matter)
* User cannot specify starting position of the player
	* Since mazes are randomly generated and a player cannot be placed in a hallway, it's not guaranteed that the specified room is a room that a player could possibly start in
	* Instead the game picks a safe starting room (no bats/wumpus/pit) to place the player in
* In GUI mode, clicking a space to move will not work to traverse through a wrapped edge
	* If you wish to take move to the otherside via wrapping, use arrow keys
	



USAGE
-------------------------------------------------
Run the Driver class with either --gui or --text as a command line argument to start the program on GUI or text mode, respectively.

Once run, enter the configuration options for the game. In GUI mode, these will be presented all at once and can be submitted by pressing "Start Game".

In text mode, the options will be presented one by one for the user to input.

If the configuration options cannot create a valid game, the user will stay in the configuration page to try new configuration options

In GUI mode, the arrow keys can be used to traverse the cave system. There is a shoot button with drop down menus for distance and direction to shoot an arrow. The hint button will bring up a dialog informing you if the current player has a possible path to victory. The give up button will kill the current player. Once the game has ended, a message will pop up over the maze indicating a winner or "Game Over" if no winner.

In text mode, the following commands are supported
* move <north|east|south|west>
* shoot <north|east|south|west> <distance>
* hint
* quit



