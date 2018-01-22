import java.io.*;
import java.util.Arrays;
import java.util.Random;

public class FileManager {

    public File gameDirectory; //C://PokeSim

    public FileManager() {
        //Make gamedir on startup if it does not exist
        gameDirectory = new File("C:" + File.separator + "PokeSim");
        if (!gameDirectory.exists()) {
            gameDirectory.mkdirs();
        }
    }
    
    public int getTotalFileCount(){
        int k = 0;
        String name;
        for(File file : this.gameDirectory.listFiles()){
            name = file.getName();
            if(name.endsWith(".trn") || name.endsWith(".atk") || name.endsWith(".poke")){
                k++;
            }
            
        }
        return k; 
    }
    
    public int getTrainerCnt(){
        int k = 0;
        for(File file : this.gameDirectory.listFiles()){
            if (file.getName().endsWith(".trn")){
                k++;
            }
        }
        return k;
    }
    
    public void tryLoadProfile(){
        
    }
    
    //returns true if a profile file doesn't exist 
    public boolean firstStartup(){
        //check if profile.txt exists
        for(File file : this.gameDirectory.listFiles()){
            //do stuff
        }
        return false;
    }

    /* Returns a file based off it's name. Null if it doesn't exist.*/
    public File getFile(String filename){
        for(File file : this.gameDirectory.listFiles()){
            if(file.getName().equals(filename)){
                return file;
            }
        }
        return null;
    }
    
    public File getPkmnFileByName(String name){
        for(File file : this.gameDirectory.listFiles()){
            if(file.getName().endsWith(".poke")){
                if(this.getName(file).equals(name)){
                    return file;
                }
            }
        }
        return null;
    }
    
    public File getAttackFileByName(String name){
        for(File file : this.gameDirectory.listFiles()){
            if(file.getName().endsWith(".atk")){
                if(this.getAtkName(file).equals(name)){
                    return file;
                }
            }
        }
        return null;
    }

    /* Finds out if the specified file exists or not. */
    public boolean fileExists(String filename){
        for(File file : this.gameDirectory.listFiles()){
            if(file.getName().equals(filename)){
                return true;
            }
        }
        return false;
    }

    /*
    Returns a random .poke file from the directory.
     */
    public File getRandomPokemon() {
        //very rarely this thing throws nullpointer or arrayoutofbounds
        try{
            File[] list = gameDirectory.listFiles();
            int length = list.length;
            if(length == 0 || length < 0){
                Game.getTextHelper().print("Your Pokemon directory is empty.");
                return null;
            }else{
                Random r = new Random();
                int file = r.nextInt(length +1 - 1); //bound must be positive?
                while(!isValidFile(list[file])){
                    file = r.nextInt(length +1 - 1);
                }
                Game.getTextHelper().print("Found Pokemon #" + Integer.valueOf(file).toString());
                this.printInfo(list[file]);
                return list[file];
            }
        }catch(Exception e){
            Game.gameLoop(true);
        }
        return null;
    }

    /* Returns how many attacks are loaded into the game. */
    public int getAttackCount(){
        int counter = 0;
        for(File file : gameDirectory.listFiles()){
            if(file.getName().endsWith(".atk")){
                counter++;
            }
        }
        return counter;
    }

    /*
    Goes through the game's directory and counts how many are
    of file type .pkmn. Returns the resulting number.
     */
    public int getPkmn() {
        int counter = 0;
        File[] list = gameDirectory.listFiles();
        if (list != null && list.length > 0) {
            for (File file : list) {
                String name = file.getName();
                if (name.endsWith(".poke")) {
                    counter++;
                }
            }
        }
        return counter;
    }
    
    /*
    Finds out if a .poke contains valid syntax.
     */
    public boolean isValidFile(File file) {
        try{
            if(file == null) return false;
            if(file.getName().endsWith(".trn")) return true;
            int attacks = 0;
            boolean atk1_E = false;
            boolean atk2_E = false;
            boolean atk3_E = false;
            boolean atk4_E = false;
            //Once all these variables are true, we return true.
            boolean extcheck = false;
            boolean atkcheck = false;
            boolean defcheck = false;
            boolean spdcheck = false;
            boolean namecheck = false;
            boolean hpcheck = false;
            boolean typecheck = false;
            boolean attackcheckone = false;
            boolean attackchecktwo = false;
            boolean attackcheckthree = false;
            boolean attackcheckfour = false;
            BufferedReader br_validf = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br_validf.readLine()) != null){
                if(line.startsWith("name:")){
                    String[] l = line.split(":");
                    if(l[1].isEmpty()){
                        namecheck = false;
                    }else{
                        namecheck = true;    
                    }
                }

                if(line.startsWith("atk")){
                    String[] l = line.split(":");
                    try
                    {
                        int k = Integer.parseInt(l[1]);
                        if(l[1].isEmpty()){
                            atkcheck = false;;
                        }else{
                            atkcheck = true;
                        }
                    }catch(Exception e){
                        atkcheck = false;
                    }

                }

                if(line.startsWith("def")){
                    String[] l = line.split(":");
                    try
                    {
                        int k = Integer.parseInt(l[1]);
                        if(l[1].isEmpty()){
                            defcheck = false;;
                        }else{
                            defcheck = true;
                        }
                    }catch(Exception e){
                        defcheck = false;
                    }
                }

                if(line.startsWith("speed:")){
                    String[] l = line.split(":");
                    try
                    {
                        int k = Integer.parseInt(l[1]);
                        if(l[1].isEmpty()){
                            spdcheck = false;;
                        }else{
                            spdcheck = true;
                        }
                    }catch(Exception e){
                        spdcheck = false;
                    }
                }

                if(line.startsWith("basehp:")){
                    try
                    {
                        String[] l = line.split(":");
                        int k = Integer.parseInt(l[1]);
                        if(l[1].isEmpty()){
                            hpcheck = false;
                        }else{
                            hpcheck = true;
                        }
                    }catch(Exception e){
                        hpcheck = false;
                    }
                }

                if(line.startsWith("type:")){
                    String[] type = line.split(":");
                    if(Arrays.asList(Game.validTypes).contains(type[1])){
                        typecheck = true;
                    }else{
                        typecheck = false;
                    }
                }

                if(line.startsWith("attack1:")){
                    String[] k = line.split(":");
                    if(this.atkExists(k[1])){
                        attackcheckone = true;
                        attacks++;
                    }

                }

                if(line.startsWith("attack2:")){
                    String[] k = line.split(":");
                    if(this.atkExists(k[1])){
                        attackchecktwo = true;
                        attacks++;
                    }
                }

                if(line.startsWith("attack3:")){
                    String[] k = line.split(":");
                    if(this.atkExists(k[1])){
                        attackcheckthree = true;
                        attacks++;
                    }
                }

                if(line.startsWith("attack4:")){
                    String[] k = line.split(":");
                    if(this.atkExists(k[1])){
                        attackcheckfour = true;
                        attacks++;
                    }
                }

                if(file.getName().endsWith(".poke")){
                    extcheck = true;
                }

                if(extcheck && spdcheck && defcheck && atkcheck && namecheck && hpcheck && typecheck && attackcheckone && attackchecktwo && attackcheckthree && attackcheckfour){
                    return true;
                }else{

                }
            }

        }catch (Exception e){
            return false;
        }
        return false;
    }

    /* Gets the attack stat from the specified .pkmn file. */
    public int getAtk(File file){
        try{
            BufferedReader br_atkp = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br_atkp.readLine()) != null){
                if(line.startsWith("atk")){
                    String[] atkArray = line.split(":");
                    int atk = Integer.valueOf(atkArray[1]);
                    return atk;
                }
            }

        }catch(Exception e){
            Game.getTextHelper().error("An error occurred while trying to read " + file.getName() + "'s attack.");
        }
        return 1;
    }

    /* Returns the defense stat of the specified .pkmn file. */
    public int getDef(File file){
        try{
            BufferedReader br_defp = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br_defp.readLine()) != null){
                if(line.startsWith("def")){
                    String[] defArray = line.split(":");
                    int atk = Integer.valueOf(defArray[1]);
                    return atk;
                }
            }

        }catch(Exception e){
            Game.getTextHelper().error("An error occurred while trying to read " + file.getName() + "'s defense.");
        }
        return 1;
    }

    /* Returns the name of the specified .pkmn file. */
    public String getName(File file){
        try {
            BufferedReader br_namep = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br_namep.readLine()) != null) {
                if (line.startsWith("name:")) {
                    String[] nameArray = line.split(":");
                    String name = nameArray[1];
                    return name;
                }
            }
        } catch (Exception e) {
            Game.getTextHelper().error("An error occurred while trying to read the name of a Pokemon.");
        }
        return null;
    }

    public int getSpd(File file){
        try{
            BufferedReader br_spdf = new BufferedReader(new FileReader(file));
            String line;
            while((line = br_spdf.readLine()) != null){
                if(line.startsWith("speed:")){
                    String[] speedArr = line.split(":");
                    return Integer.valueOf(speedArr[1]);
                }
            }
        }catch(Exception e){
            Game.getTextHelper().error("An error occurred while trying to read the speed of a Pokemon.");
        }
        return 1;
    }

    public String getType(File file){
        try{
            BufferedReader br_typef = new BufferedReader(new FileReader(file));
            String line;
            while((line = br_typef.readLine()) != null){
                if(line.startsWith("type:")){
                    String[] typeArray = line.split(":");
                    String type = typeArray[1];
                    return type;
                }
            }
        }catch (Exception e){
            Game.getTextHelper().error("An error occurred while trying to read the type of a Pokemon.");
        }
        return null;
    }

    public int getHP(File file){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null){
                if(line.startsWith("basehp:")){
                    String[] hpArray = line.split(":");
                    int hp = Integer.valueOf(hpArray[1]);
                    return hp;
                }
            }
        }catch (Exception e){
            Game.getTextHelper().error("An error occurred while trying to read " + file.getName() + "'s HP.");
        }
        return -1;
    }

    public String getAttackSlotOne(File file){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line = reader.readLine()) != null){
                if(line.startsWith("attack1:")){
                    String[] attackArray = line.split(":");
                    String attack = attackArray[1];
                    return attack;
                }
            }
        }catch (Exception e){
            return "none";
        }
        return "none";
    }

    public String getAttackSlotTwo(File file){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line = reader.readLine()) != null){
                if(line.startsWith("attack2:")){
                    String[] attackArray = line.split(":");
                    String attack = attackArray[1];
                    return attack;
                }
            }
        }catch (Exception e){
            return "none";
        }
        return "none";
    }

    public String getAttackSlotThree(File file){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line = reader.readLine()) != null){
                if(line.startsWith("attack3:")){
                    String[] attackArray = line.split(":");
                    String attack = attackArray[1];
                    return attack;
                }
            }
        }catch (Exception e){
            return "none";
        }
        return "none";
    }

    public String getAttackSlotFour(File file){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line = reader.readLine()) != null){
                if(line.startsWith("attack4:")){
                    String[] attackArray = line.split(":");
                    String attack = attackArray[1];
                    return attack;
                }
            }
        }catch (Exception e){
            return "none";            
        }
        return "none";
    }

    public boolean isValidAttack(File file){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            boolean name = false;
            boolean type = false;
            boolean power = false;
            boolean accuracy = false;
            while((line = reader.readLine()) != null){
                if(line.startsWith("name:")){
                    String[] l = line.split(":");
                    if(l[1].isEmpty()){
                        name = false;
                    }else{
                        name = true;
                    }

                }
                if(line.startsWith("type:")){
                    String[] typeArr = line.split(":");
                    if(Arrays.asList(Game.validTypes).contains(typeArr[1])){
                        type = true;
                    }else{
                        type = false;
                    }
                }
                if(line.startsWith("power:")){
                    boolean test = true;
                    try
                    {
                        String[] l = line.split(":");
                        if(l[1].isEmpty()){
                            test = false;
                            power = false;
                        }else{
                            Integer.parseInt(l[1]);
                        }
                    }catch(Exception e){
                        test = false;
                    }
                    if(test){
                        power = true;
                    }else{
                        power = false;
                    }

                }
                if(line.startsWith("accuracy:")){
                    boolean test2 = true;
                    try{
                        String[] accArr = line.split(":");
                        if(Integer.valueOf(accArr[1]) > 100 || Integer.valueOf(accArr[1]) < 1){
                            test2 = false;
                            accuracy = false;
                        }else{

                        }
                    }catch(Exception e){
                        test2 = false;
                    }
                    if(test2){
                        accuracy = true;
                    }

                }
                if(accuracy && power && type && name){
                    return true;
                }
            }
        }catch(Exception e){
            Game.getTextHelper().print("Error:");
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public String getAtkName(File file){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null){
                if(line.startsWith("name:")){
                    String[] atkArr = line.split(":");
                    return atkArr[1];
                }
            }
        }catch(Exception e){
            Game.getTextHelper().error("An error occurred while trying to read " + file.getName() + "'s name.");
        }
        return null;
    }

    public int getPower(File file){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null){
                if(line.startsWith("power:")){
                    String[] pwrArr = line.split(":");
                    return Integer.valueOf(pwrArr[1]);
                }
            }
        }catch(Exception e){
            Game.getTextHelper().error("An error occurred while trying to read " + file.getName() + "'s power.");
        }
        return 1;
    }

    public String getAtkType(File file){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null){
                if(line.startsWith("type:")){
                    String[] typeArr = line.split(":");
                    return typeArr[1];
                }
            }
        }catch(Exception e){
            Game.getTextHelper().error("An error occurred while trying to read " + file.getName() + "'s type.");
        }
        return null;
    }

    public int getAccuracy(File file){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null){
                if(line.startsWith("accuracy:")){
                    String[] accArr = line.split(":");
                    return Integer.valueOf(accArr[1]);
                }
            }
        }catch(Exception e){
            Game.getTextHelper().error("An error occurred while trying to read " + file.getName() + "'s accuracy.");
        }
        return 100;
    }

    public void printAttack(File file){
        if(isValidAttack(file)){
            String name = this.getAtkName(file);
            String type = this.getAtkType(file);
            int power = this.getPower(file);
            int accuracy = this.getAccuracy(file);
            Game.getTextHelper().print(file.getName() + "/" + name + "/" + type + "/" + power + "/" + accuracy);
        }else{
            Game.getTextHelper().print("Found invalid attack file " + file.getName() + ", ignoring!");
        }
    }

    /* Finds out if a attack of the specified name exists. */
    public boolean atkExists(String name){
        for(File file : this.gameDirectory.listFiles()){
            if(file.getName().endsWith(".atk")){
                if(this.getAtkName(file).equalsIgnoreCase(name) && this.isValidAttack(file)){
                    return true;
                }
            }
        }
        return false;
    }

    public void printInfo(File file) {
        if(isValidFile(file)){
            String name = this.getName(file);
            String type = this.getType(file);
            int atk = this.getAtk(file);
            int def = this.getDef(file);
            int hp = this.getHP(file);
            int spd = this.getSpd(file);
            String attackOne = this.getAttackSlotOne(file);
            String attackTwo = this.getAttackSlotTwo(file);
            String attackThree = this.getAttackSlotThree(file);
            String attackFour = this.getAttackSlotFour(file);
            Game.getTextHelper().print(file.getName() + "/" + name + "/" + atk + "/" + def + "/" + spd + "/" + type + "/" + hp + "/" + attackOne + "/" + attackTwo + "/" + attackThree + "/" + attackFour);
        }else{
            Game.getTextHelper().print("Found invalid file " + file.getName() + ", ignoring!");
        }
    }

    //Change attr to repl in specified file.
    public void modifyAttr(String attr, String repl, File file) {
        try {
            Game.getTextHelper().print("Editing " + file.getName() + ". Replacing attribute " + attr + " with " + repl + ".");
        }catch(Exception e) {
            Game.getTextHelper().print("An error occurred while trying to edit a file.");
        }
    }

    //write an attack file with parameters to the game directory. 
    public void writeAttackFile(String filename, String name, String type, int power, int accuracy){
        try{
            File f = new File("C:" + File.separator + "PokeSim" + File.separator + filename);
            FileWriter writer = new FileWriter(f);
            PrintWriter w = new PrintWriter(writer);
            w.print("name:" + name);
            w.print("\ntype:" + type);
            w.print("\npower:" + power);
            w.print("\naccuracy:" + accuracy);
            w.close();
        }catch(Exception e){
            Game.getTextHelper().print("Error: ");
            Game.getTextHelper().print(e.getMessage());

        }
    }

    //write a pokemon file with the parameters to the game directory.
    public void writePokemonFile(String filename, String name, int atk, int def, int spd, String type, String hp, String attack_one, String attack_two, String attack_three, String attack_four){
        try{
            File f = new File("C:" + File.separator + "PokeSim" + File.separator + filename);
            FileWriter fWriter = new FileWriter(f);
            PrintWriter w = new PrintWriter(fWriter);
            w.print("name:" + name);
            w.print("\natk:" + atk);
            w.print("\ndef:" + def);
            w.print("\nspeed:" + spd);
            w.print("\ntype:" + type);
            w.print("\nbasehp:" + hp);
            w.print("\nattack1:" + attack_one);
            w.print("\nattack2:" + attack_two);
            w.print("\nattack3:" + attack_three);
            w.print("\nattack4:" + attack_four);
            w.close();
        }catch(IOException e){
            Game.getTextHelper().print("Error: " + e.getMessage());
        }

    }

    //print file to window
    public void open(File file){
        Game.getTextHelper().seperateText(file.getName());
        //now print
        try{
            StringBuilder sb=new StringBuilder("");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null){
                sb.append(line + "\n");
            }
            System.out.println(sb);
        }catch(Exception e){
            Game.getTextHelper().print("An error occurred while trying to open a file.");
            Game.getTextHelper().error("Error: " + e.getMessage() + ".");
        }
    }
    
    public String getTrainerName(File trainer){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(trainer));
            String line;
            while((line = reader.readLine()) != null){
                if(line.startsWith("name:")){
                    String[] msg = line.split(":");
                    return msg[1];
                }
            }
        }catch(Exception e){
            
        }
        return null;
    }
    
    public String getTrainerPrefix(File trainer){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(trainer));
            String line;
            while((line = reader.readLine()) != null){
                if(line.startsWith("prefix:")){
                    String[] msg = line.split(":");
                    return msg[1];
                }
            }
        }catch(Exception e){
            
        }
        return null;
    }
    
    public String getWinningMsg(File trainer){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(trainer));
            String line;
            while((line = reader.readLine()) != null){
                if(line.startsWith("winningMsg:")){
                    String[] msg = line.split(":");
                    return msg[1];
                }
            }
        }catch(Exception e){
            
        }
        return null;
    }
    
    public String getLosingMsg(File trainer){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(trainer));
            String line;
            while((line = reader.readLine()) != null){
                if(line.startsWith("losingMsg:")){
                    String[] msg = line.split(":");
                    return msg[1];
                }
            }
        }catch(Exception e){
            
        }
        return null;
    }
    
    //returns null if the file isnt valid
    public String getSlotOnePkmn(File trainer){
        //get name of slot 
        //get file then return
        try{
            BufferedReader reader = new BufferedReader(new FileReader(trainer));
            String line;
            while((line = reader.readLine()) != null){
                if(line.startsWith("slot1:")){
                    String[] slot = line.split(":");
                    if(slot.length == 1){
                        return "";
                    }
                    if(slot[1] == null || slot[1].isEmpty() || slot[1].equals("")){
                        return "";
                    }else{
                        return slot[1];
                    }
                }
            }
        }catch(Exception e){
            
        }
        return null;
    }
    
    public String getSlotTwoPkmn(File trainer){
        //get name of slot 
        //get file then return
        try{
            BufferedReader reader = new BufferedReader(new FileReader(trainer));
            String line;
            while((line = reader.readLine()) != null){
                if(line.startsWith("slot2:")){
                    String[] slot = line.split(":");
                    if(slot.length == 1){
                        return "";
                    }
                    if(slot[1] == null || slot[1].isEmpty() || slot[1].equals("")){
                        return "";
                    }else{
                        return slot[1];
                    }
                }
            }
        }catch(Exception e){
            
        }
        return null;
    }
    
    public String getSlotThreePkmn(File trainer){
        //get name of slot 
        //get file then return
        try{
            BufferedReader reader = new BufferedReader(new FileReader(trainer));
            String line;
            while((line = reader.readLine()) != null){
                if(line.startsWith("slot3:")){
                    String[] slot = line.split(":");
                    if(slot.length == 1){
                        return "";
                    }
                    if(slot[1] == null || slot[1].isEmpty() || slot[1].equals("")){
                        return "";
                    }else{
                        return slot[1];
                    }
                }
            }
        }catch(Exception e){
            
        }
        return null;
    }
    
   public String getSlotFourPkmn(File trainer){
        //get name of slot 
        //get file then return
        try{
            BufferedReader reader = new BufferedReader(new FileReader(trainer));
            String line;
            while((line = reader.readLine()) != null){
                if(line.startsWith("slot4:")){
                    String[] slot = line.split(":");
                    if(slot.length == 1){
                        return "";
                    }
                    if(slot[1] == null || slot[1].isEmpty() || slot[1].equals("")){
                        return "";
                    }else{
                        return slot[1];
                    }
                }
            }
        }catch(Exception e){
            
        }
        return null;
    }
    
    public String getSlotFivePkmn(File trainer){
        //get name of slot 
        //get file then return
        try{
            BufferedReader reader = new BufferedReader(new FileReader(trainer));
            String line;
            while((line = reader.readLine()) != null){
                if(line.startsWith("slot5:")){
                    String[] slot = line.split(":");
                    if(slot.length == 1){
                        return "";
                    }
                    if(slot[1] == null || slot[1].isEmpty() || slot[1].equals("")){
                        return "";
                    }else{
                        return slot[1];
                    }
                }
            }
        }catch(Exception e){
            
        }
        return null;
    }
    
    public String getSlotSixPkmn(File trainer){
        //get name of slot 
        //get file then return
        try{
            BufferedReader reader = new BufferedReader(new FileReader(trainer));
            String line;
            while((line = reader.readLine()) != null){
                if(line.startsWith("slot6:")){
                    String[] slot = line.split(":");
                    if(slot.length == 1){
                        return "";
                    }
                    if(slot[1] == null || slot[1].isEmpty() || slot[1].equals("")){
                        return "";
                    }else{
                        return slot[1];
                    }
                }
            }
        }catch(Exception e){
            
        }
        return null;
    }
    
    public boolean isValidTrainer(File file){
        boolean namecheck = false;
        boolean prefixcheck = false;
        boolean winningmsgc = false;
        boolean losingmsgc = false;
        boolean extcheck = false; //file extension = .trn
        
        try{
            //get the attributes and check them
            String name = this.getTrainerName(file);
            String prefix = this.getTrainerPrefix(file);
            String winningMsg = this.getWinningMsg(file);
            String losingMsg = this.getLosingMsg(file);
            //check the name, prefix, losing/winning msgs, and if the pokemon files are valid.
            if(name != null && !(name.length() < 3) && !(name.length() > 20)){
                namecheck = true;
            }
            if(prefix != null && !(prefix.length() < 3) && !(prefix.length() > 15)){
                prefixcheck = true; 
            }
            if(winningMsg != null  && !(winningMsg.length() < 3) && !(winningMsg.length() > 50)){
                winningmsgc = true;
            }
            if(losingMsg != null && !(losingMsg.length() < 3) && !(losingMsg.length() > 50)){
                losingmsgc = true;
            }
           
            if(file.getName().endsWith(".trn")) extcheck = true;
            
            if(extcheck && namecheck && prefixcheck && winningmsgc && losingmsgc){
                return true;
            }
            
            
        }catch(Exception e){
            Game.getTextHelper().print("An error occurred while validating a trainer file.:");
            Game.getTextHelper().print("Extension?: " + String.valueOf(extcheck));
            Game.getTextHelper().print("Prefix?: " + String.valueOf(prefixcheck));
            Game.getTextHelper().print("Win Msg?: " + String.valueOf(winningmsgc));
            Game.getTextHelper().print("Lose Msg?: " + String.valueOf(losingmsgc));
            Game.getTextHelper().print("Name?: " + String.valueOf(namecheck));
            e.printStackTrace();
        }
        return false;
    }

    
}
