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

    private static String[] validTypes = {"Bug", "Dragon", "Ice", "Fighting", "Fire", "Flying", "Grass", "Ghost", "Ground", "Electric", "Normal", "Poison", "Psychic", "Rock", "Water"};
    private static String[] validCommands = {"", "help", "battle", "list", "credits", "profile"};
    private static String[] battleCommands = {"random", "list", "back", "new", "edit"};
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
            text.print("Please enter a name for your profile: ");
            String inp = text.getStringInput("> ");
            if(inp.equals("")){
                name = "debug";
            }else{
                name = inp;
            }
            text.print("Profile name set as \"" + name + "\"");
            text.blank();
            gameLoop(false);

        }catch (Exception e){
            text.seperator();
            e.printStackTrace();
            text.seperator();
            text.error("\nAn unknown error has occurred while running the game.");
            text.error(e.getMessage());
        }
    }

    /*
    After the game has started, everything happens within
    this function. It handles everything from the menus
    to the battles.
     */
    public static void gameLoop(boolean battle){ //TODO: Add setPoke command to set current party pokemon, also add battle <pokemon> command
        if(battle){
            inBattleMenu = true;
            inMenu = false;
        }else{
            inMenu = true;
            inBattleMenu = false;
        }

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
                text.print("- random to battle a random Pokemon.");
                text.print("- back to return to the main menu.");
                text.print("- list to list the available Pokemon to battle");
                text.blank();

            } else if (input.equalsIgnoreCase("back")) {
                text.print("Returning to main menu...");
                text.blank();
                gameLoop(false);

            } else if (input.equalsIgnoreCase("random")) {
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
                        String name = text.getStringInput("Name?: ");
                        String type = text.getStringInput("Type?: ");
                        String hp = text.getStringInput("HP?: ");
                        String attackone = text.getStringInput("Attack?: ");
                        String attacktwo = text.getStringInput("Attack?: ");
                        String attackthree = text.getStringInput("Attack?: ");
                        String attackfour = text.getStringInput("Attack?: ");
                        //TODO: Check if attackone is not valid aswell.
                        while(name.length() > 16 || name.length() < 1 || !Arrays.asList(validTypes).contains(type) || Integer.parseInt(hp) < 1){
                            if(name.length() > 16 || name.length() < 1){
                                text.print("Name must have between 1 and 16 characters.");
                                text.blank();
                            }
                            if(!Arrays.asList(validTypes).contains(type)){
                                text.print("\"" + type + "\" is not a valid type.");
                                text.blank();
                            }
                            if(Integer.parseInt(hp) < 1){
                                text.print("HP must be between 1 and 2147483647.");
                                text.blank();
                            }
                            name = text.getStringInput("Name?: ");
                            type = text.getStringInput("Type?: ");
                            hp = text.getStringInput("HP?: ");
                            attackone = text.getStringInput("Attack?: ");
                            attacktwo = text.getStringInput("Attack?: ");
                            attackthree = text.getStringInput("Attack?: ");
                            attackfour = text.getStringInput("Attack?: ");
                        }
                        //save file now, it is valid.
                        text.print("saving file.");
                    }catch(Exception e){
                        text.print("HP must be a number between 1 and 2147483647.");
                        gameLoop(true);
                    }

                }
                if(in.equalsIgnoreCase("n")){
                    gameLoop(true);
                }

            } else if(input.equalsIgnoreCase("edit")){
                text.print("");
            }

            else if(!Arrays.asList(battleCommands).contains(input)){
                text.print("Invalid command!");
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
