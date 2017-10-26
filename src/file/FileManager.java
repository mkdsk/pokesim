package file;

import game.Game;

import java.io.*;
import java.util.Arrays;
import java.util.Random;

public class FileManager {

    public File gameDirectory;

    public FileManager() {
        //Make gamedir on startup if it does not exist
        gameDirectory = new File("C:" + File.separator + "PokeSim");
        if (!gameDirectory.exists()) {
            gameDirectory.mkdirs();
        }
    }

    /*
    Returns a random .poke file from the directory.
     */
    public File getRandomPokemon() {
        File[] list = gameDirectory.listFiles();
        int length = list.length;
        if(length == 0 || length < 0){
            Game.getTextHelper().print("Your Pokemon directory is empty.");
            return null;
        }else{
            Random r = new Random();
            int file = r.nextInt(length - 1);
            Game.getTextHelper().print("Found Pokemon #" + Integer.valueOf(file).toString());
            return list[file];
        }

    }

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
        if (list != null) {
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
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null){
                if(line.startsWith("name:")){
                    namecheck = true;
                }
                if(line.startsWith("atk")){
                    atkcheck = true;
                }
                if(line.startsWith("def")){
                    defcheck = true;
                }
                if(line.startsWith("speed:")){
                    spdcheck = true;
                }
                if(line.startsWith("basehp:")){
                    hpcheck = true;
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
                    attackcheckone = true;
                }
                if(line.startsWith("attack2:")){
                    attackchecktwo = true;
                }
                if(line.startsWith("attack3:")){
                    attackcheckthree = true;
                }
                if(line.startsWith("attack4:")){
                    attackcheckfour = true;
                }
                if(spdcheck && defcheck && atkcheck && namecheck && hpcheck && typecheck && attackcheckone && attackchecktwo && attackcheckthree && attackcheckfour){
                    return true;
                }else{

                }
            }

            }catch (Exception e){
            return false;
        }
        return false;
    }

    public int getAtk(File file){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null){
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

    public int getDef(File file){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null){
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

    public String getName(File file){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("name:")) {
                    String[] nameArray = line.split(":");
                    String name = nameArray[1];
                    return name;
                }
                reader.close();
            }
        } catch (Exception e) {
            Game.getTextHelper().error("An error occurred while trying to read the name of a Pokemon.");
        }
        return null;
    }

    public int getSpd(File file){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line = reader.readLine()) != null){
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
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line = reader.readLine()) != null){
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
            Game.getTextHelper().error("An error occurred. 02");
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
                    name = true;
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
                    power = true;
                }
                if(line.startsWith("accuracy:")){
                    String[] accArr = line.split(":");
                    if(Integer.valueOf(accArr[1]) > 100 || Integer.valueOf(accArr[1]) < 1){
                        accuracy = false;
                    }else{
                        accuracy = true;
                    }
                }
                if(accuracy && power && type && name){
                    return true;
                }
            }
        }catch(Exception e){
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

    /*
    Prints a Pokemon's info off of a file.
     */
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

    public void deleteFile(File file){

    }

    public void writeAttackFile(){

    }

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
}

