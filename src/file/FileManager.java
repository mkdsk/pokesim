package file;

import game.Game;

import java.io.*;
import java.util.Random;

public class FileManager {

    public File gameDirectory;

    public FileManager() {
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

    public void getPokemonList() {
        File[] list = gameDirectory.listFiles();
        if (list != null) {
            for (File file : list) {
                String name = file.getName();
                if (name.endsWith(".poke")) {
                    Game.getTextHelper().print("");
                }
            }
        }
        int count = this.getPkmn();
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
    Finds out if a file contains valid syntax.
     */
    public boolean isValidFile(File file) {
        try{
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
                if(line.startsWith("basehp:")){
                    hpcheck = true;
                }
                if(line.startsWith("type:")){
                    typecheck = true;
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
                if(namecheck && hpcheck && typecheck && attackcheckone && attackchecktwo && attackcheckthree && attackcheckfour){
                    return true;
                }else{

                }
            }

            }catch (Exception e){
            return false;
        }
        return false;
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
            Game.getTextHelper().error("An error occurred. 03");
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

    /*
    Prints a Pokemon's info off of a file.
     */
    public void printInfo(File file) {
        if(isValidFile(file)){
            String name = this.getName(file);
            String type = this.getType(file);
            int hp = this.getHP(file);
            String attackOne = this.getAttackSlotOne(file);
            String attackTwo = this.getAttackSlotTwo(file);
            String attackThree = this.getAttackSlotThree(file);
            String attackFour = this.getAttackSlotFour(file);
            Game.getTextHelper().print(name + "/" + type + "/" + hp + "/" + attackOne + "/" + attackTwo + "/" + attackThree + "/" + attackFour);
        }else{
            Game.getTextHelper().print("Found invalid file " + file.getName() + ", ignoring!");
        }
    }

    public void deleteFile(String filename){

    }

    public void writeAttackFile(){

    }

    public void writePokemonFile(String filename, String name, String type, String hp, String attack_one, String attack_two, String attack_three, String attack_four){
        try{
            File f = new File("C:" + File.separator + "PokeSim" + File.separator + filename);
            FileWriter fWriter = new FileWriter(f);
            PrintWriter w = new PrintWriter(fWriter);
            w.print("name:" + name);
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

