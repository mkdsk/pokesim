import java.io.File;
import java.util.Arrays;
import java.net.InetAddress;
import java.net.UnknownHostException;

/* ;;;TODO LIST;;;
    
    10/31/17; Added start command, setpoke command, party command.
    11/1/17; Added newatk.
    11/3/17; Added atkExists() and started to code battling.
    11/6/17; Better checking for POKE files, check command, cls command.
    11/8/17; Better checking for ATK files, better checking for valid attacks in .POKE files.
    11/9/17; Changed how the battle loop begins to load.
    11/13/17; Reworked battle logic a bit more, fixed random command throwing exception.
    11/15/17; Added open <file> command.
    11/16/17; Fixed del command and added arguments for the command.
    12/11/17; Fixed getPkmn() throwing error on first time startup.
    12/12/17; Fixed battle sequence, added quit command
    12/13/17; Added ASCII engine, tour command, battle delays
    12/21/17; Added code for basic trainer battle functionality
    
    Add edit command for .ATK and .POKE with args.
    
    Fix NullPointerException when typing in a invalid file: "start bob.tr"
    Add trainerlist command
    Add profile saving so we don't have to type setpoke each time on startup.
    Add the ability to switch out Pokemon
    Add setpoke <pokemon> <slot> for MULTIPLE pokemon in parties (up to 6 at most)
    Add menu to create a new trainer
    
    Battle
    ======
    Add DMG over time moves, weather effect moves, status effects, stat lowering/raising. This should be marked by atktype in the file: physical/status/weather/time/stat.
    Add better battle AI
    Add natures, held items, bag items, abilities
    Pokemon with less than 4 attacks but atleast one should be allowed to battle
    Check for same name attacks and mark as invalid pokemon, it can break the battle code
    Implement IVs/EVs?
    
    Fix memory management and organize code
    Add multiplayer commands and battles, and tournaments.
    Add lobby chats with other players like in showdown.
    Add graphics
    Add exe launcher file for the game
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
    private static File profile;

    // Determine the course of the program
    private static boolean inBattleMenu = false;
    private static boolean inMenu = false;

    public static final String GAME_NAME = "PokeSim";
    public static final String GAME_REL_VER = "Alpha";
    public static final String GAME_VERSION = "0.37";
    
    private static String[] cmd;

    public static void main(String[] args){
        //get args and use them?
        cmd = args;
        init(cmd);
    }

    public static void init(String[] args){
        try{
            long time = System.currentTimeMillis();
            for(int i = 0; i < 100; i++){ text.blank(); }
            text.drawASCII("PokeSim", 10);
            fileManager = new FileManager();
            if(args.length == 0) text.print("No arguments recieved.");
            text.seperator();
            text.print("Starting " + GAME_NAME + " " + GAME_REL_VER + " v" + GAME_VERSION);
            text.seperator();
            text.print("Initialized classes...");
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
            long timeTaken = System.currentTimeMillis() - time;
            text.print("Initialized in " + String.valueOf(timeTaken) + "ms.");
            text.seperator();
            text.print("Please enter a name for your profile: ");
            String inp = text.getStringInput("> ");
            if(inp.equals("")){
                name = "Player";
            }else{
                name = inp;
            }
            text.print("Profile name set as \"" + name + "\"");
            text.blank();
            text.print("Write \"help\" for command info.");
            text.print("You can also write \"quit\" at any time to quit the game.");
            gameLoop(false);

        }catch (Exception e){ //whoops
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
       loop in the battle menu or the regular menu.)
       PokeSim's command line allows you to create custom
       Pokemon and attacks, initiate battles, and initiate multiplayer battles
       online. Editing of files within the command line is 
       also planned. 
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
            

            //Description: Enters the battle menu.
            if(input.equalsIgnoreCase("battle")){
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
                text.print("tour - Gives you a tour of the game. Useful for new players.");
                text.blank();
            }

            //Description: Gives a tour of the game and how to use it.
            else if(input.equalsIgnoreCase("tour")){
                String decision = text.getStringInput("Take tour? (y/n): ");
                if(decision != "y"){
                    
                }
                
                if(decision.equalsIgnoreCase("y")){
                    text.blank();
                    for(int i = 0; i < 150; i++){
                        text.blank();
                    }
                    text.drawASCII("PokeSim", 10);
                    System.out.println("===========================================");            
                    text.print("PokeSim is a text based game inspired by other simulations of Pokemon battles such as ");
                    text.print("Pokemon Showdown. This version attemps to recreate Showdown but with a much simper ");
                    text.print("approach. Many more features are added, including but not limited to the creation of");
                    text.print("custom Pokemon and attacks, profiles, and easily accessable databases of Pokemon stored in ");
                    text.print("the directory for the game. ");
                    text.seperateText("Getting Started");
                    text.print("Assuming you have the first-gen pack installed in C://PokeSim, you can setup a party by doing");
                    text.print("        \"setpoke <file>.poke\"");
                    text.print("And then battle a Pokemon by simply writing");
                    text.print("        \"start <file>.poke\"");
                    text.print("Other useful commands can be found by typing \"help\" on the command line.");
                    text.blank();
                }
            }
            
            //Description: Displays the user's profile.
            else if(input.equalsIgnoreCase("profile")){
                text.print("Profile name: " + name);
                text.print("Java: Java v" + System.getProperty("java.version") + " by " + System.getProperty("java.vendor"));
                text.print("OS: " + System.getProperty("os.arch") + " " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
                
                text.blank();
            }
            
            
            else if(input.equalsIgnoreCase("quit")){
                text.print("Quitting game...");
                System.exit(0);
            }
        }

        /* Battle menu functions and command */
        while(inBattleMenu) {
            String input = text.getStringInput("BATTLE> ");
            String[] a_Input = input.split(" "); //For multiple arguments.
            
            //Description: Displays commands used in battle menu
            if (input.equalsIgnoreCase("help")) {
                text.print("- start <file> to battle a specific Pokemon.");
                text.print("- setpoke <file> to set your battle Pokemon.");
                text.print("- edit <file> <attr> <repl> to edit a Pokemon.");
                text.print("- check <file> to check syntax for a file.");
                text.print("- open <file> to print a file's contents.");
                text.print("- cls to clear the console window.");
                text.print("- random to battle a random Pokemon.");
                text.print("- back to return to the main menu.");
                text.print("- list to list the available Pokemon to battle.");
                text.print("- del to delete a Pokemon or attack.");
                text.print("- new to create a new Pokemon.");
                text.print("- newatk to create a new attack.");
                text.print("- attacks to list your loaded attacks.");
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
                        else if(!fileManager.atkExists(attackone)){
                            text.print(attackone + " is not a valid attack.");
                            gameLoop(true);
                        }
                        else if(!fileManager.atkExists(attacktwo)){
                            text.print(attacktwo + " is not a valid attack.");
                            gameLoop(true);
                        }
                        else if(!fileManager.atkExists(attackthree)){
                            text.print(attackthree + " is not a valid attack.");
                            gameLoop(true);
                        }
                        else if(!fileManager.atkExists(attackfour)){
                            text.print(attackfour + " is not a valid attack.");
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
                if(fileManager.fileExists(a_Input[1]) && fileManager.getFile(a_Input[1]).getName().endsWith(".poke") || fileManager.getFile(a_Input[1]).getName().endsWith(".trn")){
                    battler.battle(fileManager.getFile(a_Input[1])); //null pointer wtf ? 
                }else{
                    text.print("File not found or file is not a POKE/TRN file.");
                }
                
            
            //Description: Delete a file.
            } else if(Arrays.asList(a_Input).contains("del")){
               if(a_Input.length != 2){
                   text.print("Usage: del <file>");
                   gameLoop(true);
               }
               String filename = a_Input[1];
               if(fileManager.fileExists(filename)){
                   try{
                       if(fileManager.getFile(filename).delete()){
                           text.print("Successfully deleted file " + filename + ".");
                       }else{
                           text.print("Unable to delete file " + filename + ".");
                           text.print("This may be caused by Java being unable to delete files on your OS.");
                       }
                   }catch(SecurityException s){
                       text.print("Java is blocked access from deleting files.");
                   }
                   
               }else{
                   text.print("File not found.");
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
                    text.print("You haven't set your party Pokemon yet. Use setpoke <file>.");
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
            }else if(input.equalsIgnoreCase("cls")){
                for(int i = 0; i < 300; i++){
                    System.out.println("");
                }
            }else if(Arrays.asList(a_Input).contains("check")){
                if(a_Input.length != 2){
                    text.print("Usage: check <file>");
                }else{
                    if(fileManager.fileExists(a_Input[1])){
                        if(a_Input[1].endsWith("poke")){
                            if(fileManager.isValidFile(fileManager.getFile(a_Input[1]))){
                                text.print(a_Input[1] + " is a valid POKE file.");
                            }else{
                                text.print(a_Input[1] + " is not a valid POKE file.");
                            }
                        }
                        else if(a_Input[1].endsWith("atk")){
                            if(fileManager.isValidAttack(fileManager.getFile(a_Input[1]))){
                                text.print(a_Input[1] + " is a valid attack file.");
                            }else{
                                text.print(a_Input[1] + " is not a valid attack file.");
                            }
                        }
                    }else{
                        text.print("File not found.");
                    }
                }
            }else if(Arrays.asList(a_Input).contains("open")){
                if(a_Input.length != 2){
                    text.print("Usage: open <file>");
                    gameLoop(true);
                }
                if(fileManager.fileExists(a_Input[1])){
                    fileManager.open(fileManager.getFile(a_Input[1]));
                }else{
                    text.print("File not found.");
                }
            }else if(input.equalsIgnoreCase("quit")){
                text.print("Quitting game...");
                System.exit(0);
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
    
    
    public static void wait(int seconds){
        try{
            Thread.sleep(seconds*1000);
        }catch(InterruptedException e){
            text.print("Interrupted!!!");
        }
    }

    public static FileManager getFileManager(){
        return fileManager;
    }

    public static Text getTextHelper(){
        return text;
    }

    public static File getPartyPokemon(){
        return party != null ? party : null;
    }
    
    public static boolean getMenuState(){
        return inMenu ? inMenu : inBattleMenu;
    }
    
    public static String getProfile(){
        return name;
    }

}
