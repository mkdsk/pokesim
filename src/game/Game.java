package game;

import battle.Battler;
import file.FileManager;
import text.Text;
import java.io.File;
import java.util.Arrays;

public class Game {

    private static FileManager fileManager; // Useful file manager.
    private static Text text = new Text();
    private static Battler battler = new Battler();

    private static String[] validCommands = {"", "help", "battle", "list", "credits", "profile"};
    private static String[] battleCommands = {"battlerandom", "list", "back"};
    private static String name = "default";

    private static boolean inBattleMenu = false;
    private static boolean inMenu = false; // Determines whether we should run the menu loop

    public static final String GAME_NAME = "PokeSim";
    public static final String GAME_REL_VER = "Pre-Alpha";
    public static final String GAME_VERSION = "0.2";

    public static void main(String[] args){
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
            text.print("Loading Pokemon...");
            if(fileManager.getPkmn() == 0){
                text.error("Zero Pokemon were found in your PokeSim directory. Make sure");
                text.error("You have downloaded the first gen pack and installed it in the");
                text.error("Proper directory. If the error persists, contact xxq on Discord.");
            }else {
                text.print(fileManager.getPkmn() + " Pokemon initialized.");
            }
            text.seperator();
            gameLoop();

        }catch (Exception e){ // Some sort of error occurred??
            text.seperator();
            text.error(e.getMessage());
            text.seperator();
            text.error("\nAn unknown error has occurred while attempting to start the game.");
            text.error("Please contact xxq if you believe this is an error.");
        }
    }

    /*
    After the game has started, everything happens within
    this function. It handles everything from the menus
    to the battles.
     */
    public static void gameLoop(){ //TODO: Add setPoke command to set current party pokemon, also add battle <pokemon> command
        text.print("Please enter a name for your profile: ");
        String inp = text.getStringInput("> ");
        if(inp.equals("")){
            name = "debug";
        }else{
            name = inp;
        }
        text.print("Profile name set as \"" + name + "\"");
        text.blank();
        inMenu = true;
        while(inMenu){
            text.print("Write \"help\" for command info.");
            String input = text.getStringInput("> ");
            if(!Arrays.asList(validCommands).contains(input)){ // invalid command!
                text.print("Invalid command!");
            }
            else if(input.equalsIgnoreCase("battle")){
                text.print("Entering battle menu...");
                inMenu = false;
                inBattleMenu = true;
            }
            else if(input.equalsIgnoreCase("list")){
                //prints all loaded pokemon
                text.print("Loading Pokemon list...");
                text.blank();
                text.print("Name/Type/HP/Attack/Attack/Attack/Attack");
                text.print("========================================");
                File[] list = fileManager.gameDirectory.listFiles();
                for(File file : list){
                    String filename = file.getName();
                    if(filename.endsWith(".poke")){
                        fileManager.printInfo(file);
                    }
                }
                if(list.length == 0){
                    text.print("No Pokemon were found in your directory.");
                }
                text.blank();
            }
            else if(input.equalsIgnoreCase("credits")){
                text.print("PokeSim v" + GAME_VERSION + " created by xxq.");
                text.blank();
            }
            else if(input.equalsIgnoreCase("help")){
                text.print("battle - Opens the battle menu. Used to battle certain pokemon.");
                text.print("list - Prints all loaded Pokemon in memory.");
                text.print("help - Display this menu.");
                text.print("profile - Displays your profile info.");
                text.blank();
            }
            else if(input.equalsIgnoreCase("profile")){
                text.print("Your profile name is \"" + name + "\" ");
                text.blank();
            }
        }
        while(inBattleMenu) {
            text.print("Write \"help\" for command info.");
            String input = text.getStringInput("BATTLE> ");
            if (input.equalsIgnoreCase("help")) {
                text.print("- Type any Pokemon filename to battle it.");
                text.print("- battlerandom to battle a random Pokemon.");
                text.print("- back to return to the main menu.");
                text.print("- list to list the available Pokemon to battle");
                text.blank();
            } else if (input.equalsIgnoreCase("back")) {
                text.print("Returning to main menu...");
                inBattleMenu = false;
                inMenu = true;
                text.blank();
            } else if (input.equalsIgnoreCase("battlerandom")) {
                battler.battle(fileManager.getRandomPokemon());
            } else if (input.equalsIgnoreCase("list")) {
                //prints all loaded pokemon
                text.print("Loading Pokemon list...");
                text.blank();
                text.print("Name/Type/HP/Attack/Attack/Attack/Attack");
                text.print("========================================");
                File[] list = fileManager.gameDirectory.listFiles();
                for (File file : list) {
                    String filename = file.getName();
                    if (filename.endsWith(".poke")) {
                        fileManager.printInfo(file);
                    }
                }
                if (list.length == 0) {
                    text.print("No Pokemon were found in your directory.");
                }
                text.blank();
            }else{
                //user wants to battle pokemon
                //input = pokemon to battle
                //check if there is pokemon in directory to battle named the string the user gave, if theres not,
                //give invalid command message.
                File[] list = fileManager.gameDirectory.listFiles();
                for(File file : list){
                    String filename = file.getName();
                    if(filename.endsWith(".poke")){
                        if(fileManager.getName(file).equalsIgnoreCase(input)){
                            battler.battle(file);
                        }
                    }
                }
                text.print("Pokemon name not found in directory!");
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
        return inMenu;
    }

}
