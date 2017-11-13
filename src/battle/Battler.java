package battle;

import file.FileManager;
import game.Game;

import java.io.File;

public class Battler {

    // decides whether we should run battle loop at any time during void battle
    private boolean battling;
    private String decision;
    
    public Battler(){

    }

    /*
    Battles the specific file
     */
    public void battle(File file){
        
        battling = true;

        //Files that we will work with throughout this loop
        File enemy = file;
        File player = Game.getPartyPokemon();
        
        //Check if we have valid files here.
        //NULL is the default var for when a file has not been set.;
        if(player == null){
            battling = false;
            Game.getTextHelper().print("Error: You need a party Pokemon to battle.");
            Game.gameLoop(true);
            return;
        }
        else if(enemy == null){
            battling = false;
            Game.getTextHelper().print("Error: Enemy PKMN is null.");
            Game.gameLoop(true);
            return;
        }

        else if(!Game.getFileManager().isValidFile(enemy)){
            battling = false;
            Game.getTextHelper().print("Error: Enemy Pokemon is not a valid .POKE file.");
            Game.gameLoop(true);
            return;
        }

        else if(!Game.getFileManager().isValidFile(player)){
            battling = false;
            Game.getTextHelper().print("Error: Your party Pokemon is not a valid .POKE file.");
            Game.gameLoop(true);
            return;
        }

        /* Both Pokemon are valid. Now we get their stats. */
        Game.getTextHelper().print("Attempting to battle " + file.getName() + "..."); 
        
        //Get the stats of our Pkmn
        String p_Name = Game.getFileManager().getName(player);
        int p_Atk = Game.getFileManager().getAtk(player);
        int p_Def = Game.getFileManager().getDef(player);
        int p_Spd = Game.getFileManager().getSpd(player);
        String p_Type = Game.getFileManager().getType(player);
        long p_Hp = Game.getFileManager().getHP(player);
        String p_Atk_1 = Game.getFileManager().getAttackSlotOne(player);
        String p_Atk_2 = Game.getFileManager().getAttackSlotTwo(player);
        String p_Atk_3 = Game.getFileManager().getAttackSlotThree(player);
        String p_Atk_4 = Game.getFileManager().getAttackSlotFour(player);
        Game.getTextHelper().print("Successfully loaded party Pokemon #1 into battle sequence.");
        
        //Get the stats of enemy Pkmn
        String e_Name = Game.getFileManager().getName(enemy);
        int e_Atk = Game.getFileManager().getAtk(enemy);
        int e_Def = Game.getFileManager().getDef(enemy);
        int e_Spd = Game.getFileManager().getSpd(enemy);
        String e_Type = Game.getFileManager().getType(enemy);
        long e_Hp = Game.getFileManager().getHP(enemy);
        String e_Atk_1 = Game.getFileManager().getAttackSlotOne(enemy);
        String e_Atk_2 = Game.getFileManager().getAttackSlotTwo(enemy);
        String e_Atk_3 = Game.getFileManager().getAttackSlotThree(enemy);
        String e_Atk_4 = Game.getFileManager().getAttackSlotFour(enemy);
        
        Game.getTextHelper().print("Successfully loaded enemy file into battle sequence.");
        
        /* MAIN BATTLE LOOP */
        //Later add AI for enemy Pokemon player.
        //get player input (attack or run)
        //do as so to enemy pokemon or run from battle.
        //randomize a move from the enemy and calculate its damage toward player.
        while(battling){
            //status effects should take affect each time here, as long as the status effect is still active.
            if(p_Hp < 1 || e_Hp < 1){
                //battle is over
                if(p_Hp < 1){
                    Game.getTextHelper().print("You lost.");
                    battling = false;
                    Game.gameLoop(true);
                }
                if(e_Hp < 1){
                    Game.getTextHelper().print("You won.");
                    battling = false;
                    Game.gameLoop(true);
                }
            }
            Game.getTextHelper().blank();
            Game.getTextHelper().print("FIGHT/RUN");
            decision = Game.getTextHelper().getStringInput("What should " + Game.getFileManager().getName(player) + " do?");
            while(!decision.equalsIgnoreCase("fight") && !decision.equalsIgnoreCase("run")){
                Game.getTextHelper().print("Invalid command: type FIGHT or RUN.");
                decision = Game.getTextHelper().getStringInput("What should " + Game.getFileManager().getName(player) + " do?");
            }
            //once we are here, the decision is either fight or run.
            if(decision.equalsIgnoreCase("run")){
                battling = false;
                Game.getTextHelper().print("You ran away from battle.");
                Game.gameLoop(true);
            }
            else if(decision.equalsIgnoreCase("fight")){
                //get input: which attack.
            }
            
        }
        
    }
    
    /* Types of moves: Damage over time, damage, stat raising/lowering, weather, status effects */
    
    /* Calculate damage if the move is an attacking type of move. */
    public long calcDmg(File attackingPkmn, File enemyPkmn, String move){
        return 0; //placeholder
    }
    
    

}
