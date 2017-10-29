package game;

import battle.Battler;
import file.FileManager;
import text.Text;
import java.io.File;
import java.util.Arrays;

/* TODO LIST;;;
    Add edit command, modifyAttr function. Will edit both atk and pkmn files.
    Add setpoke <pkmn file> command.
    Add arguments for start command.
    
    Fix FATAL: Fix resource leaks. (Make seperate readers for each function and close them?)
    Fix del command and add arguments for it.
    
    Make sure types of Pokemon are valid before entering battle.
    Implement battles. Damage calculation
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
            text.error(e.getMessage());
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
                    text.print("No Pokemon were found in your directory.");
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
                text.print("- start to battle a specific Pokemon.");
                text.print("- random to battle a random Pokemon.");
                text.print("- back to return to the main menu.");
                text.print("- list to list the available Pokemon to battle");
                text.print("- del to delete a Pokemon or attack.");
                text.print("- edit to edit a Pokemon.");
                text.print("- new to create a new Pokemon.");
                text.print("- attacks to list your loaded attacks.");
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
                    text.print("No Pokemon were found in your directory.");
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
            }  else if(input.equalsIgnoreCase("start")){
            	if(fileManager.getPkmn() == 0) {
            		text.print("You cannot battle because you have no Pokemon loaded.");
            		gameLoop(true);
            	}
                listPokemon();
                String pokemon = text.getStringInput("Filename to battle?: ");
                for(File file : fileManager.gameDirectory.listFiles()){
                    if(file.getName().equalsIgnoreCase(pokemon)){
                        if(fileManager.isValidFile(file)){
                            battler.battle(file);
                        }else{
                            text.print("File " + file.getName() + " is not a valid file to battle.");
                            gameLoop(true);
                        }
                    }
                }
                text.print("File not found.");
            
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

            //Description: Edits a Pokemon file.    
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
            
            }else if(!Arrays.asList(battleCommands).contains(input)){
            	//Empty space.
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
