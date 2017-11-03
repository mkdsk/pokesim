package battle;

import file.FileManager;
import game.Game;

import java.io.File;

public class Battler {

    private boolean battling;

    public Battler(){

    }

    /*
    Battles the specific file
     */
    public void battle(File file){
        battling = true;
        Game.getTextHelper().print("Battling " + file.getName());
        while(battling){
            /* MAIN BATTLE LOOP */
            File enemy = file;
            File player = Game.getPartyPokemon();
            if(player == null){
                battling = false;
                Game.getTextHelper().print("Error: You need a party Pokemon to battle.");
                Game.gameLoop(true);
            }
            //Check if our attacks and enemy attacks are valid.
            int p_Atk = Game.getFileManager().getAtk(player);
            int p_Def = Game.getFileManager().getDef(player);
            
            
            
            //Get the stats of our Pkmn
            
            //Get the stats of enemy Pkmn
        }
    }

}
