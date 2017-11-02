package game;

import battle.Battler;
import file.FileManager;
import text.Text;
import java.io.File;
import java.util.Arrays;

/* ;;;TODO LIST;;;
    
    10/31/17; Added start command, setpoke command, party command
    11/1/17; Added newatk,
    
    Add check command
    Add edit command (void modifyAttr)
  
    Fix getRandomPokemon()
    Fix :FATAL: Fix resource leaks. (Make seperate readers for each function and close them.)
    Fix del command and add arguments for it.
    
    Update valid commands list with new commands.
   
    Print error correctly in game console.
    Find bugs and fix them. Mostly involving errors loading Pokemon files.
    Implement battles, damage calculation functions.
    Handle invalid Pokemon before battling (no HP specified, etc)
    Add damage over time attacks (Leech Seed, etc)
    Add secondary type of attacks (Physical/StatRaise/Weather) move:Physical, move2:StatRaise for multiple effects in one attack.
    Add stat modifying moves to the game (If it lowers/raises a stat, a "true" will be placed under the field "modify:")
 */

public class Game {

    private static FileManager fileManager;
    private static Text text = new Text();
    private static Battler battler = new Battler();

    public static String[] validTypes = {"bug", "dragon", "ice", "fighting", "fire", "flying", "grass", "ghost", "ground", "electric", "normal", "poison", "psychic", "rock", "water", "Bug", "Dragon", "Ice", "Fighting", "Fire", "Flying", "Grass", "Ghost", "Ground", "Electric", "Normal", "Poison", "Psychic", "Rock", "Water"};
    public static String[] valid_Attr = {"name", "atk", "def", "speed", "type", "hp", "atk1", "atk2", "atk3", "atk4"};
    
    private static String[] validCommands = {"", "help", "battle", "list", "credits", "profile"};
    private static String[] battleCommands = {"random", "list", "back", "new", "edit", "start", "del"};
    private static String name = "Player";
    private static File party; // Player's party pokemon used in battle.

    // Determine the course of the program
    private static boolean inBattleMenu = false;
    private static boolean inMenu = false;

    public static final String GAME_NAME = "PokeSim";
    public static final String GAME_REL_VER = "Pre-Alpha";
    public static final String GAME_VERSION = "0.31a";

    public static void main(String[] args){
        //get args and use them?
        init();
    }

    public static void init(){
        try{
            fileManager = new FileManager();
            text.seperator();
            text.print("Starting " + GAME_NAME + " " + GAME_REL_VER + " v" + GAME_VERSION);
            text.seperator();
            text.print("Initalized File Manager...");
            text.print("Initalized Text functions...");
            text.print("Loading files...");
            if(fileManager.getPkmn() == 0){
                text.error("Zero Pokemon were found in your PokeSim directory. Make sure");
                text.error("you have downloaded the first gen pack and installed it in the");
                text.error("proper directory (C:/PokeSim)");
            }else{
                text.print(fileManager.getPkmn() + " Pokemon initialized.");
            }
            if(fileManager.getAttackCount() == 0){
                text.print("Zero attacks were found in your PokeSim directory.");
            }else{
                text.print(fileManager.getAttackCount() + " attack(s) initialized.");
            }
            text.seperator();
            text.print("Please enter a name for your profile: ");
            String inp = text.getStringInput("> ");
            if(inp.equals("")){
                name = "debug";
            }else{
                name = inp;
            }
            text.print("Profile name set as \"" + name + "\"");
            text.blank();
            text.print("Write \"help\" for command info.");
            gameLoop(false);

        }catch (Exception e){
            text.seperator();
            e.printStackTrace();
            text.seperator();
            text.error("\nAn error has occurred while running the game.");
            text.error("Error:");
            e.printStackTrace();
        }
    }

    /* All commands are dealt with within this function.
       Parameters: battle (Determines whether to restart the 
       loop in the battle menu or the regular menu. 
    */
    public static void gameLoop(boolean battle){
        if(battle){
            inBattleMenu = true;
            inMenu = false;
        }else{
            inMenu = true;
            inBattleMenu = false;
        }
        while(inMenu){
            String input = text.getStringInput("MENU> ");
            if(!Arrays.asList(validCommands).contains(input)){ // invalid command!
            }

            //Description: Enters the battle menu.
            else if(input.equalsIgnoreCase("battle")){
                text.print("Entering battle menu...");
                inMenu = false;
                inBattleMenu = true;
            }

            //Description: Displays Pokemon list.
            else if(input.equalsIgnoreCase("list")){
                listPokemon();
                if(fileManager.getPkmn() == 0){
                    text.print("You currently have 0 loaded Pokemon.");
                }
                text.blank();
            }

            //Description: Displays game credits.
            else if(input.equalsIgnoreCase("credits")){
                text.print("PokeSim v" + GAME_VERSION + " created by [][].");
                text.blank();
            }

            //Description: Displays commands available in the help menu.
            else if(input.equalsIgnoreCase("help")){
                text.print("battle - Opens the battle menu. Used to battle certain pokemon.");
                text.print("list - Prints all loaded Pokemon in memory.");
                text.print("help - Display this menu.");
                text.print("profile - Displays your profile info.");
                text.blank();
            }

            //Description: Displays the user's profile name.
            else if(input.equalsIgnoreCase("profile")){
                text.print("Your profile name is \"" + name + "\" ");
                text.blank();
            }
        }

        /* Battle menu functions and command */
        while(inBattleMenu) {
            String input = text.getStringInput("BATTLE> ");
            String[] a_Input = input.split(" "); //For multiple arguments.
            
            //Description: Displays commands used in battle menu
            if (input.equalsIgnoreCase("help")) {
                text.print("- start <file> to battle a specific Pokemon.");
                text.print("- random to battle a random Pokemon.");
                text.print("- back to return to the main menu.");
                text.print("- list to list the available Pokemon to battle");
                text.print("- del to delete a Pokemon or attack.");
                text.print("- edit <file> to edit a Pokemon.");
                text.print("- new to create a new Pokemon.");
                text.print("- newatk to create a new attack.");
                text.print("- attacks to list your loaded attacks.");
                text.print("- setpoke <file> to set your battle Pokemon.");
                text.print("- party to view your current party Pokemon.");
                text.blank();

            //Description: Returns user to main menu.
            } else if (input.equalsIgnoreCase("back")) {
                text.print("Returning to main menu...");
                text.blank();
                gameLoop(false);

            //Description: Battles a random, valid file.    
            } else if (input.equalsIgnoreCase("random")) {
                battler.battle(fileManager.getRandomPokemon());

            //Description: Lists all Pokemon, if any.    
            } else if (input.equalsIgnoreCase("list")) {
                if (fileManager.getPkmn() == 0) {
                    text.print("You currently have 0 loaded Pokemon.");
                }else{
                    listPokemon();
                }
                text.blank();

            //Description: Creates a new Pokemon.    
            } else if(input.equalsIgnoreCase("new")){
                String in = text.getStringInput("Create new Pokemon? (y/n): ");
                while(!in.equalsIgnoreCase("y") && !in.equalsIgnoreCase("n") && !in.equalsIgnoreCase("back")){
                    in = text.getStringInput("Create new Pokemon? (y/n): ");
                }
                if(in.equalsIgnoreCase("back")){
                    gameLoop(true);
                }
                if(in.equalsIgnoreCase("y")){
                    try{
                        String filename = text.getStringInput("Filename?: ");
                        String name = text.getStringInput("Name?: ");
                        String atk = text.getStringInput("Atk Stat?: ");
                        String def = text.getStringInput("Def Stat?: ");
                        String spd = text.getStringInput("Speed?: ");
                        String type = text.getStringInput("Type?: ");
                        String hp = text.getStringInput("HP?: ");
                        String attackone = text.getStringInput("Attack?: ");
                        String attacktwo = text.getStringInput("Attack?: ");
                        String attackthree = text.getStringInput("Attack?: ");
                        String attackfour = text.getStringInput("Attack?: ");
                        //TODO: Check if all attacks are valid except if they are empty ("")
                        if(filename.length() > 16 || filename.length() < 1 || name.length() > 16 || name.length() < 1 || !Arrays.asList(validTypes).contains(type) || Integer.parseInt(hp) < 1){
                            if(filename.length() > 16 || filename.length() < 1){
                                text.print("Filename must be between 1 and 16 characters long.");
                            }
                            if(name.length() > 16 || name.length() < 1){
                                text.print("Name must have between 1 and 16 characters.");
                            }
                            if(!Arrays.asList(validTypes).contains(type)){
                                text.print("\"" + type + "\" is not a valid type.");
                            }
                            if(Integer.parseInt(hp) < 1){
                                text.print("HP must be between 1 and 2147483647.");
                            }
                            gameLoop(true);
                        }
                        //save file now, it is valid.
                        text.print("Validating file...");
                        String newType = type.replace(type.charAt(0), Character.toUpperCase(type.charAt(0)));
                        fileManager.writePokemonFile(filename + ".poke", name, Integer.valueOf(atk), Integer.valueOf(def), Integer.valueOf(spd), newType, hp, attackone, attacktwo, attackthree, attackfour);
                        text.print(fileManager.getPkmn() + " Pokemon initialized.");

                    }catch(Exception e){
                        text.print("Enter a number between 1 and 2147483647.");
                        gameLoop(true);
                    }

                }
                if(in.equalsIgnoreCase("n")){
                    gameLoop(true);
                }

            //Description: Start a battle.    
            }  else if(Arrays.asList(a_Input).contains("start")){
                if(fileManager.getPkmn() == 0 && a_Input[0].equals("start")) {
                    text.print("You cannot battle because you have no Pokemon loaded.");
                    gameLoop(true);
                }
                if(a_Input.length != 2){
                    text.print("Usage: start <file>");
                    gameLoop(true);
                }
                if(fileManager.fileExists(a_Input[1])){
                    battler.battle(fileManager.getFile(a_Input[1]));
                }else{
                    text.print("File not found!");
                }
                
            
            //Description: Delete a file.
            } else if(input.equalsIgnoreCase("del")){
                listPokemon();
                listAttacks();
                boolean found = false;
                String delete = text.getStringInput("Which file to delete?: ");
                for(File file : fileManager.gameDirectory.listFiles()){
                    String name = file.getName();
                    //TODO: Fix.
                    if(name.equalsIgnoreCase(delete)){
                        found = true;
                        text.print("Deleting file " + delete + ".");
                        fileManager.deleteFile(file);
                    }
                }
                if(found == false){
                    text.print("File " + delete + " not found!");
                }

            //Description: Prints all attacks.
            } else if(input.equalsIgnoreCase("attacks")){
                if(fileManager.getAttackCount() == 0){
                    text.print("You currently have 0 loaded attacks.");
                }else{
                    listAttacks();
                }
                text.blank();

            //Description: Edits a .pkmn file or .atk file.
            } else if(Arrays.asList(a_Input).contains("edit")) {
                //Usage: edit <file> <attr> <replacement>
                if(!(a_Input.length == 4)) {
                    text.print("Usage: edit <file.poke> <attribute> <replacement>");
                    text.print("Example: edit charizard.poke speed 500");
                }else{
                    boolean found = false;
                    for(File file : fileManager.gameDirectory.listFiles()) {
                        if(file.getName().equals(a_Input[1])){
                            found = true;
                            fileManager.modifyAttr(a_Input[2], a_Input[3], file);
                        }else{
                            
                        }
                    }
                    if(!found) {
                        text.print("File not found.");
                        gameLoop(true);
                    }
                }
            
            }else if(Arrays.asList(a_Input).contains("setpoke")){
                if(fileManager.getPkmn() == 0 && a_Input[0].equals("setpoke")){
                    text.print("You must have atleast one Pokemon to set as your current Pokemon.");                   
                    gameLoop(true);
                }
                if(a_Input.length != 2){
                    text.print("Usage: setpoke <file>");
                    gameLoop(true);
                }
                if(fileManager.fileExists(a_Input[1])){
                    if(fileManager.isValidFile(fileManager.getFile(a_Input[1]))){
                        party = fileManager.getFile(a_Input[1]);
                        text.print("Party member #1 set to " + fileManager.getName(fileManager.getFile(a_Input[1])));
                    }else{
                        text.print("The specified file is not a valid Pokemon file.");
                    }
                }else{
                    text.print("File not found.");
                }
                
            }else if(input.equalsIgnoreCase("party")){
                if(party == null){
                    text.print("You haven't set your party Pokmeon yet. Use setpoke <file>.");
                    gameLoop(true);
                }
                    
                text.print("File/Name/Atk/Def/Speed/Type/HP/Attack/Attack/Attack/Attack");
                text.print("===========================================================");
                fileManager.printInfo(party);
                
            }else if (input.equalsIgnoreCase("newatk")){
                String in = text.getStringInput("Create new attack? (y/n): ");
                while(!in.equalsIgnoreCase("y") && !in.equalsIgnoreCase("n") && !in.equalsIgnoreCase("back")){
                    in = text.getStringInput("Create new attack? (y/n): ");
                }
                if(in.equalsIgnoreCase("back")){
                    gameLoop(true);
                }
                else if(in.equalsIgnoreCase("y")){
                    try{
                        String filename = text.getStringInput("Filename?: ");
                        String name = text.getStringInput("Name?: ");
                        String type = text.getStringInput("Type?: ");
                        String pwr = text.getStringInput("Power?: ");
                        String acc = text.getStringInput("Accuracy?: ");
                        
                        if(filename.length() < 1 || filename.length() > 16 || name.length() > 16 || name.length() < 1 || !Arrays.asList(validTypes).contains(type) || Integer.valueOf(acc) > 100 || Integer.valueOf(acc) < 1){
                            if(name.length() > 16 || name.length() < 1){
                                text.print("Attack name must be between 1 and 16 characters.");
                            }
                            if(!Arrays.asList(validTypes).contains(type)){
                                text.print(type + " is not a valid type.");
                            }
                            if(Integer.valueOf(acc) > 100 || Integer.valueOf(acc) < 1){
                                text.print("Accuracy must be between 1 and 100.");
                            }
                            if(filename.length() < 1 || filename.length() > 16){
                                text.print("Filename must be between 1 and 16 characters.");
                            }
                            gameLoop(true);
                        }
                        //validate
                        text.print("Validating attack...");
                        type = type.replace(type.charAt(0), Character.toUpperCase(type.charAt(0)));
                        fileManager.writeAttackFile(filename + ".atk", name, type, Integer.valueOf(pwr), Integer.valueOf(acc));
                        text.print(fileManager.getAttackCount() + " attacks initialized.");
                    }catch(Exception e){
                        text.print("An error occurred while reading input.");
                    }
                }
            }
        }
    }

    public static void listPokemon(){
        text.print("File/Name/Atk/Def/Speed/Type/HP/Attack/Attack/Attack/Attack");
        text.print("===========================================================");
        File[] list = fileManager.gameDirectory.listFiles();
        for (File file : list) {
            String filename = file.getName();
            if (filename.endsWith(".poke")) {
                fileManager.printInfo(file);
            }
        }
    }

    public static void listAttacks(){
        text.print("File/Name/Type/Power/Accuracy");
        text.print("=============================");
        File[] list = fileManager.gameDirectory.listFiles();
        for (File file : list){
            if(file.getName().endsWith(".atk")){
                fileManager.printAttack(file);
            }
        }
    }

    public static FileManager getFileManager(){
        return fileManager;
    }

    public static Text getTextHelper(){
        return text;
    }

    public static boolean getMenuState(){
        return inMenu ? inMenu : inBattleMenu;
    }

}
