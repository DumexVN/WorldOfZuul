/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 2006.03.30
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    Character currentPlayer;
    Character superman, rambo;
    Room town, moutain, hiddencave, field, swamp, cave, highland, castle, ruin, forest, victorychamber; 
     /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {      
        createRooms();
        createMonsters();
        createCharacters();
        createItems();
        parser = new Parser();
    }
    private void createCharacters()
    {
    //Character superman, rambo;
    superman = new Character(100,"The legendary Superman");
    rambo = new Character(50,"The Rambo"); 
    
    }
    
    private void createMonsters()
    {// Creat Monster
    Monster imp,im;
    imp = new Monster(10);
    hiddencave.addMonster("m1", imp);
    moutain.addMonster("2", imp);
    field.addMonster("3", imp);
    }
 
    private void createItems()
    {   //add items to rooms
        Item grenade;
        grenade = new Item("A grenade which can kill enemy with 1 hit", 20);
        hiddencave.addItem("1", grenade);
        rambo.addItem("1",grenade);
        rambo.addItem("2",grenade);
    }
    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {         
        // create the rooms
        town = new Room("in a deserted town");
        moutain = new Room("in a high, cold moutain");
        hiddencave = new Room("in a hiddencave, no body ever been here before");
        field = new Room("in a wide field, better becareful");
        swamp = new Room("in a swamp");
        cave = new Room("in a cave");
        highland = new Room("in a highland");
        castle = new Room("in the castle");
        ruin = new Room("in a ruin");
        forest = new Room("in a forest");
        victorychamber = new Room("in the Victory Chamber");
        // initialise room exits
        town.setExit("north", field);
        town.setExit("west", swamp);
        town.setExit("east", moutain);
        town.setExit("south", cave);
        
        moutain.setExit("north", hiddencave);
        moutain.setExit("west", town);
        
        hiddencave.setExit("south", moutain);
        
        field.setExit("south", town);
        
        swamp.setExit("east", town);
        
        cave.setExit("north", town);
        cave.setExit("south", highland);
        
        highland.setExit("north", cave);
        highland.setExit("south", castle);
        highland.setExit("east", ruin);
        highland.setExit("west", forest);
        
        ruin.setExit("east", highland);
        
        forest.setExit("west", highland);
        
        castle.setExit("north", highland);
        
        castle.setExit("up", victorychamber);
        currentRoom = town;  
      
    }

    /**
     *  Please chose between superman and rambo
     */
    public void play(String strCharacterName) 
    { //Chose the Character
        if(strCharacterName.equalsIgnoreCase("superman")){
            currentPlayer=superman;
            //currentPlayer=new Character(100,"The legendary Superman");
        }else if(strCharacterName.equalsIgnoreCase("rambo")){
            currentPlayer=rambo;
        }else{
            System.out.println("Please choose between superman or rambo or you will not be able to play the game.");
            return;
        }
        printWelcome();
        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished){ if (currentPlayer.health != 0){ 
           if (currentRoom != victorychamber){
            Command command = parser.getCommand();
            finished = processCommand(command);}
            else {System.out.println("Congratulation. You are the winner !");
            finished = true;}
            }
            else finished = true;   
            } 
                 
         
        System.out.println("Thank you for playing.Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("WELCOME!");
        System.out.println("If you can get to the castle, you're the winner. Be careful, there are monsters in the way, your health will decreased if you meet those!!  ");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
        System.out.println();
        System.out.println("You are playing as");
        printPlayerDetails();
        printPocketItemInfo();
     }
       
    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
            
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("reveal")){
            printRoomInfo();
        }
        else if (commandWord.equals("status")){
            printPocketItemInfo();
            printPlayerDetails();
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are here to rescue the princess, trapped in the castle");
        System.out.println("Go rescue her!");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }
    

    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no way there!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
        fight();
        
        
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
    private void printRoomInfo()
    {  // print out the items current in the room
       System.out.println("Current items in the room: ");
       System.out.println();
       System.out.println(currentRoom.listItems());
    }
      
    private void printPlayerDetails(){// print current health of the player
        System.out.println(currentPlayer.getInfo());
    }
    
    private void printPocketItemInfo()
    {  // print out the items current in the player
       System.out.println("Current items in your pocket: ");
       System.out.println();
       System.out.println(currentPlayer.listItems());
    }
    private void fight()
    { 
       int a = currentPlayer.getHealth();
       int b = currentRoom.getMonstersHealth();
       if (b==0){
           System.out.println("Current Health:" + currentPlayer.getHealth());}
           else {if (currentPlayer.itemSize() > 0){
               useItem();
            }
            else {if(a > b){ //if the player health is more than that of the monster in the room
           
           //Step1: Modify the player health.
           
           currentPlayer.health= a-b;
           System.out.println("Current Health:" + currentPlayer.getHealth());
           
           //Step2: Remove the monster
           currentRoom.clear();
           
        }else {// if the player health is less, DEAD !
          currentPlayer.health = 0;
          System.out.println("You were eaten by the monster");} }  }
    }
    
    private void useItem(){
    currentPlayer.removeLastItem();
    currentRoom.clear();
    System.out.println("Current Health:" + currentPlayer.getHealth());
    }
    
}
