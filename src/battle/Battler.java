import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class Battler {

    // the random attack generator the AI uses
    private Random AttackRnd;

    // determines which menu we should run
    private boolean battling;
    private boolean fightMenu;
    private boolean atkMenu;
    private boolean atkMenuTrn = false;
    private boolean trainerBattleL = false;

    private boolean trainerBattle;

    //player input variables
    private String decision; //fight or run
    private String atk; // the attack the player uses on each turn
    private int timesCycled = 0;
    
    /* TRAINER BATTLE VARIABLES */ 
    /* TRAINER PKMN FILES */ 
    private File e_PKMN6_f;
    private File e_PKMN5_f;
    private File e_PKMN4_f;
    private File e_PKMN3_f;
    private File e_PKMN2_f;
    private File e_PKMN1_f;
    
    private ArrayList<File> trainerParty; //Initialized on trainer battle
    
    /* TRAINER PKMN STATS VARIABLES*/ 
    //THESE VARIABLES ARE CHANGED EACH TIME A NEW TRAINER POKEMON ENTERS A BATTLE. 
    private String t_Name; 
    private int t_Atk; 
    private int t_Def;
    private int t_Spd;
    private String t_Type;
    private long t_Hp;
    
    private File t_Atk1_f;
    private File t_Atk2_f;
    private File t_Atk3_f;
    private File t_Atk4_f;
    private String t_Atk_1;
    private String t_Atk_2;
    private String t_Atk_3;
    private String t_Atk_4; 
    
    /* MOVE STATS VARIABLES */ 
    private String t_Atk1_NAME; //NAME
    private String t_Atk1_TYPE; //TYPE
    private int t_Atk1_POWER; //POWER (DAMAGE)
    private int t_Atk1_ACC; //ACCURACY (1-100%%)
    
    private String t_Atk2_NAME;
    private String t_Atk2_TYPE;
    private int t_Atk2_POWER;
    private int t_Atk2_ACC;
    
    private String t_Atk3_NAME;
    private String t_Atk3_TYPE;
    private int t_Atk3_POWER;
    private int t_Atk3_ACC;
    
    private String t_Atk4_NAME;
    private String t_Atk4_TYPE;
    private int t_Atk4_POWER;
    private int t_Atk4_ACC;

    public Battler(){
        AttackRnd = new Random();
    }

    /*
    Battles the specific file
     */
    public void battle(File file){
        battling = true;
        //Files that we will work with throughout this loop
        File enemy = file;
        File player = Game.getPartyPokemon();

        if(enemy.getName().endsWith(".trn")){
            trainerBattle = true;
        }

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
            Game.getTextHelper().print("Error: Enemy PKMN or Trainer doesn't exist.");
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

        else if(trainerBattle && !Game.getFileManager().isValidTrainer(enemy)){
            battling = false;
            Game.getTextHelper().print("Error: Enemy trainer file is not valid.");
            Game.gameLoop(true);
            return;
        }
        //for trainer, since we know the file is valid now, we just get every pokemon that isnt blank and use them as the enemy.
        //so run through each getPkmnSlot function and check if they are blank or not, and add them to the enemy pkmn array list.

        if(trainerBattle){
            battling = false;
            execTrainerBattle(enemy);
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

        File p_atk1f = Game.getFileManager().getAttackFileByName( p_Atk_1);
        File p_atk2f = Game.getFileManager().getAttackFileByName(p_Atk_2);
        File p_atk3f = Game.getFileManager().getAttackFileByName(p_Atk_3);
        File p_atk4f = Game.getFileManager().getAttackFileByName(p_Atk_4);

        String p_atk1_NAME = Game.getFileManager().getAtkName(p_atk1f);
        String p_atk1_TYPE = Game.getFileManager().getAtkType(p_atk1f);
        int p_atk1_POWER = Game.getFileManager().getPower(p_atk1f);
        int p_atk1_ACC = Game.getFileManager().getAccuracy(p_atk1f);

        String p_atk2_NAME = Game.getFileManager().getAtkName(p_atk2f);
        String p_atk2_TYPE = Game.getFileManager().getAtkType(p_atk2f);
        int p_atk2_POWER = Game.getFileManager().getPower(p_atk2f);
        int p_atk2_ACC = Game.getFileManager().getAccuracy(p_atk2f);

        String p_atk3_NAME = Game.getFileManager().getAtkName(p_atk3f);
        String p_atk3_TYPE = Game.getFileManager().getAtkType(p_atk3f);
        int p_atk3_POWER = Game.getFileManager().getPower(p_atk3f);
        int p_atk3_ACC = Game.getFileManager().getAccuracy(p_atk3f);

        String p_atk4_NAME = Game.getFileManager().getAtkName(p_atk4f);
        String p_atk4_TYPE = Game.getFileManager().getAtkType(p_atk4f);
        int p_atk4_POWER = Game.getFileManager().getPower(p_atk4f);
        int p_atk4_ACC = Game.getFileManager().getAccuracy(p_atk4f);

        Game.getTextHelper().print("Successfully loaded party Pokemon #1 into battle sequence.");

        //Get the stats of enemy Pkmn
        /* Don't do this if its a trainer battle. */
        //If its a trainer battle, then set these variables to the pokemon currently in battle.
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

        File e_atk1f = Game.getFileManager().getAttackFileByName(e_Atk_1);
        File e_atk2f = Game.getFileManager().getAttackFileByName(e_Atk_2);
        File e_atk3f = Game.getFileManager().getAttackFileByName(e_Atk_3);
        File e_atk4f = Game.getFileManager().getAttackFileByName(e_Atk_4);

        String e_atk1_NAME = Game.getFileManager().getAtkName(e_atk1f);
        String e_atk1_TYPE = Game.getFileManager().getAtkType(e_atk1f);
        int e_atk1_POWER = Game.getFileManager().getPower(e_atk1f);
        int e_atk1_ACC = Game.getFileManager().getAccuracy(e_atk1f);

        String e_atk2_NAME = Game.getFileManager().getAtkName(e_atk2f);
        String e_atk2_TYPE = Game.getFileManager().getAtkType(e_atk2f);
        int e_atk2_POWER = Game.getFileManager().getPower(e_atk2f);
        int e_atk2_ACC = Game.getFileManager().getAccuracy(e_atk2f);

        String e_atk3_NAME = Game.getFileManager().getAtkName(e_atk3f);
        String e_atk3_TYPE = Game.getFileManager().getAtkType(e_atk3f);
        int e_atk3_POWER = Game.getFileManager().getPower(e_atk3f);
        int e_atk3_ACC = Game.getFileManager().getAccuracy(e_atk3f);

        String e_atk4_NAME = Game.getFileManager().getAtkName(e_atk4f);
        String e_atk4_TYPE = Game.getFileManager().getAtkType(e_atk4f);
        int e_atk4_POWER = Game.getFileManager().getPower(e_atk4f);
        int e_atk4_ACC = Game.getFileManager().getAccuracy(e_atk4f);

        //Random choice for AI attack
        ArrayList<String> Ai_Attacks = new ArrayList<>();
        Ai_Attacks.add(e_atk1_NAME);
        Ai_Attacks.add(e_atk2_NAME);
        Ai_Attacks.add(e_atk3_NAME);
        Ai_Attacks.add(e_atk4_NAME);

        Game.getTextHelper().print("Successfully loaded enemy file into battle sequence.");
        Game.getTextHelper().blank();
        Game.getTextHelper().print(e_Name + " challenges you to a battle!");
        Game.getTextHelper().print("Go, " + p_Name + "!");
        /* MAIN BATTLE LOOP */
        //Later add AI for enemy Pokemon player.
        //get player input (attack or run)
        //do as so to enemy pokemon or run from battle.
        //randomize a move from the enemy and calculate its damage toward player.
        //after each attack, check to see if either pokemon's hp is <= 0
        fightMenu = true;
        while(battling){    
            while(fightMenu){
                decision = Game.getTextHelper().getStringInput("FIGHT or RUN?: ");
                while(!decision.equalsIgnoreCase("fight") && !decision.equalsIgnoreCase("run")){
                    decision = Game.getTextHelper().getStringInput("FIGHT or RUN?: ");
                    fightMenu = false;
                    battling = false;
                }
                //now we have the decision.
                if(decision.equalsIgnoreCase("run")){
                    Game.getTextHelper().print("Successfully ran away from battle.");
                    fightMenu = false;
                    battling = false;
                }
                else if(decision.equalsIgnoreCase("fight")){
                    atkMenu = true;
                    fightMenu = false;
                }
            }

            //Attack menu: Choose which attack to use, then execute it. Accept "back" as well.
            while(atkMenu){
                Game.getTextHelper().print("Write a move's name to use it or type \"back\" to return back to the FIGHT/RUN menu.");
                Game.getTextHelper().print(p_Name + "'s HP: " + p_Hp);
                Game.getTextHelper().print(e_Name + "'s HP: " + e_Hp);
                Game.getTextHelper().print(p_Atk_1 + " / " + p_Atk_2 + " / " + p_Atk_3 + " / " + p_Atk_4);
                atk = Game.getTextHelper().getStringInput("> ");    
                while(!atk.equalsIgnoreCase("back") && !atk.equalsIgnoreCase( p_Atk_1 ) && !atk.equalsIgnoreCase( p_Atk_2 ) && !atk.equalsIgnoreCase( p_Atk_3 ) && !atk.equalsIgnoreCase( p_Atk_4 )){
                    atk = Game.getTextHelper().getStringInput("> "); //invalid attack, reset input
                }
                //now we have the correct attack choice. 
                if(atk.equalsIgnoreCase("back")){
                    atkMenu = false;
                    fightMenu = true; //ADD THIS BELOW!!!
					Game.getTextHelper().print("You forfeited the battle!");
                    return;
                }
                //its a valid attack so now we get the info about this attack, execute it, then return back to fight menu.
                //check who goes first on the turn using speed stat, the AI or the player.
                //after each attack ends, check each pokemon's hp
                if(e_Spd > p_Spd || e_Spd == p_Spd &&){ //AI's turn
                    //randomize move
                    //after move is done check if player hp is 0 or below then stop battle
                    int index = AttackRnd.nextInt(Ai_Attacks.size());
                    String attack = Ai_Attacks.get(index);
                    //deal damage with the attack chosen
                    //calculate damage then check both pokemon HP
                    if(attack.equals(e_atk1_NAME)){
                        long dmg = calcDmg(enemy, player, e_atk1_NAME);
                        p_Hp = p_Hp - dmg;
                        //check for type effectiveness
                        Game.getTextHelper().print(e_Name + " uses " + e_atk1_NAME + "!");
                        Game.wait(1);
                        Game.getTextHelper().print("It's effective!"); //placeholder for now
                        Game.getTextHelper().print(e_Name + "'s " + e_atk1_NAME + " dealt " + dmg + " damage!");
                        Game.getTextHelper().blank();
                    }
                    else if(attack.equals(e_atk2_NAME)){
                        long dmg = calcDmg(enemy, player, e_atk2_NAME);
                        p_Hp = p_Hp - dmg;
                        //check for type effectiveness 
                        Game.getTextHelper().print(e_Name + " uses " + e_atk2_NAME + "!");
                        Game.wait(1);
                        Game.getTextHelper().print("It's effective!"); //placeholder for now
                        Game.getTextHelper().print(e_Name + "'s " + e_atk2_NAME + " dealt " + dmg + " damage!");
                        Game.getTextHelper().blank();
                    }
                    else if(attack.equals(e_atk3_NAME)){
                        long dmg = calcDmg(enemy, player, e_atk3_NAME);
                        p_Hp = p_Hp - dmg;
                        //check for type effectiveness 
                        Game.getTextHelper().print(e_Name + " uses " + e_atk3_NAME + "!");
                        Game.wait(1);
                        Game.getTextHelper().print("It's effective!"); //placeholder for now
                        Game.getTextHelper().print(e_Name + "'s " + e_atk3_NAME + " dealt " + dmg + " damage!");
                        Game.getTextHelper().blank();
                    }
                    else if(attack.equals(e_atk4_NAME)){
                        long dmg = calcDmg(enemy, player, e_atk4_NAME);
                        p_Hp = p_Hp - dmg;
                        //check for type effectiveness 
                        Game.getTextHelper().print(e_Name + " uses " + e_atk4_NAME + "!");
                        Game.wait(1);
                        Game.getTextHelper().print("It's effective!"); //placeholder for now
                        Game.getTextHelper().print(e_Name + "'s " + e_atk4_NAME + " dealt " + dmg + " damage!");
                        Game.getTextHelper().blank();
                    }
                    //now check the hp of BOTH pokemon.
                    if(p_Hp < 1){
                        battling = false;
                        atkMenu = false;
                        Game.getTextHelper().print("You lose");
                    }
                    else if(e_Hp < 1){
                        battling = false;
                        atkMenu = false;
                        Game.getTextHelper().print("You win");
                    }
                }
                //calculate damage then check both pokemon hp
                if(p_atk1_NAME.equalsIgnoreCase(atk)){
                    long dmg = calcDmg(player, enemy, p_atk1_NAME);
                    e_Hp = e_Hp - dmg;
                    //check for type effectiveness message
                    Game.getTextHelper().print(p_Name + " uses " + p_atk1_NAME + "!");
                    Game.wait(1);
                    Game.getTextHelper().print("It's effective!");
                    Game.getTextHelper().print(p_Name + "'s " + p_atk1_NAME + " dealt " + dmg + " damage!");
                    Game.getTextHelper().blank();
                }
                else if(p_atk2_NAME.equalsIgnoreCase(atk)){
                    long dmg = calcDmg(player, enemy, p_atk2_NAME);
                    e_Hp = e_Hp - dmg;
                    //check for type effectiveness message
                    Game.getTextHelper().print(p_Name + " uses " + p_atk2_NAME + "!");
                    Game.wait(1);
                    Game.getTextHelper().print("It's effective!");
                    Game.getTextHelper().print(p_Name + "'s " + p_atk2_NAME + " dealt " + dmg + " damage!");
                    Game.getTextHelper().blank();
                }
                else if(p_atk3_NAME.equalsIgnoreCase(atk)){
                    long dmg = calcDmg(player, enemy, p_atk3_NAME);
                    e_Hp = e_Hp - dmg;
                    //check for type effectiveness message
                    Game.getTextHelper().print(p_Name + " uses " + p_atk3_NAME + "!");
                    Game.wait(1);
                    Game.getTextHelper().print("It's effective!");
                    Game.getTextHelper().print(p_Name + "'s " + p_atk3_NAME + " dealt " + dmg + " damage!");
                    Game.getTextHelper().blank();
                }
                else if(p_atk4_NAME.equalsIgnoreCase(atk)){
                    long dmg = calcDmg(player, enemy, p_atk4_NAME);
                    e_Hp = e_Hp - dmg;
                    //check for type effectiveness message
                    Game.getTextHelper().print(p_Name + " uses " + p_atk4_NAME + "!");
                    Game.wait(1);
                    Game.getTextHelper().print("It's effective!");
                    Game.getTextHelper().print(p_Name + "'s " + p_atk4_NAME + " dealt " + dmg + " damage!");
                    Game.getTextHelper().blank();
                }
                //check hp
                if(e_Hp < 1){
                    Game.getTextHelper().print("You win");
                }
                else if(p_Hp < 1){
                    Game.getTextHelper().print("You lose");
                }

                if(p_Spd > e_Spd && atk != "back"){
                    //deal AI attack now, player has attacked first.
                    int index = AttackRnd.nextInt(Ai_Attacks.size());
                    String attack = Ai_Attacks.get(index);
                    if(attack.equals(e_atk1_NAME)){
                        long dmg = calcDmg(enemy, player, e_atk1_NAME);
                        p_Hp = p_Hp - dmg;
                        Game.getTextHelper().print(e_Name + " uses " + e_atk1_NAME + "!");
                        Game.wait(1);
                        Game.getTextHelper().print("It's effective!"); //placeholder for now
                        Game.getTextHelper().print(e_Name + "'s " + e_atk1_NAME + " dealt " + dmg + " damage!");
                        Game.getTextHelper().blank();
                    }
                    else if(attack.equals(e_atk2_NAME)){
                        long dmg = calcDmg(enemy, player, e_atk2_NAME);
                        p_Hp = p_Hp - dmg;
                        Game.getTextHelper().print(e_Name + " uses " + e_atk2_NAME + "!");
                        Game.wait(1);
                        Game.getTextHelper().print("It's effective!"); //placeholder for now
                        Game.getTextHelper().print(e_Name + "'s " + e_atk2_NAME + " dealt " + dmg + " damage!");
                        Game.getTextHelper().blank();
                    }
                    else if(attack.equals(e_atk3_NAME)){
                        long dmg = calcDmg(enemy, player, e_atk3_NAME);
                        p_Hp = p_Hp - dmg;
                        Game.getTextHelper().print(e_Name + " uses " + e_atk3_NAME + "!");
                        Game.wait(1);
                        Game.getTextHelper().print("It's effective!"); //placeholder for now
                        Game.getTextHelper().print(e_Name + "'s " + e_atk3_NAME + " dealt " + dmg + " damage!");
                        Game.getTextHelper().blank();
                    }
                    else if(attack.equals(e_atk4_NAME)){
                        long dmg = calcDmg(enemy, player, e_atk4_NAME);
                        p_Hp = p_Hp - dmg;
                        Game.getTextHelper().print(e_Name + " uses " + e_atk4_NAME + "!");
                        Game.wait(1);
                        Game.getTextHelper().print("It's effective!"); //placeholder for now
                        Game.getTextHelper().print(e_Name + "'s " + e_atk4_NAME + " dealt " + dmg + " damage!");
                        Game.getTextHelper().blank();
                    }
                }
            }
        }
    }

    public void execTrainerBattle(File enemy){
        //valid+blankc=6 && invalidpkmn=0
        boolean validTrainer=false;
        int invalidPkmn = 0;
        int validPkmn = 0;
        int blankc = 0;
        File player = Game.getPartyPokemon();
        
        String trainerName = Game.getFileManager().getTrainerPrefix(enemy) + " " + Game.getFileManager().getTrainerName(enemy);
        
        //initialize arraylist here
        //check if is not empty and valid then add to array list.
        //if one or more is invalid then stop battle
        String pkmn1 = Game.getFileManager().getSlotOnePkmn( enemy );
        String pkmn2 = Game.getFileManager().getSlotTwoPkmn( enemy );
        String pkmn3 = Game.getFileManager().getSlotThreePkmn( enemy );
        String pkmn4 = Game.getFileManager().getSlotFourPkmn( enemy );
        String pkmn5 = Game.getFileManager().getSlotFivePkmn( enemy );
        String pkmn6 = Game.getFileManager().getSlotSixPkmn( enemy );
        /*
         * CHECK IF TRAINER AND EVERY ONE OF ITS POKEMON ARE VALID. (ATTACKS ARE VALID IS PKMN IS VALID)
         */
        //blankc+validpkmn = 6 = total valid ().
        if(pkmn1.isEmpty()){ blankc++; }
        if(pkmn2.isEmpty()){ blankc++; }
        if(pkmn3.isEmpty()){ blankc++; }
        if(pkmn4.isEmpty()){ blankc++; }
        if(pkmn5.isEmpty()){ blankc++; }
        if(pkmn6.isEmpty()){ blankc++; }

        if(Game.getFileManager().isValidFile(Game.getFileManager().getPkmnFileByName(pkmn1))){
            validPkmn++;
        }else{ invalidPkmn++; }
        
        if(Game.getFileManager().isValidFile(Game.getFileManager().getPkmnFileByName(pkmn2))){
            validPkmn++;
        }else{ invalidPkmn++; }
        
        if(Game.getFileManager().isValidFile(Game.getFileManager().getPkmnFileByName(pkmn3))){
            validPkmn++;
        }else{ invalidPkmn++; }
        
        if(Game.getFileManager().isValidFile(Game.getFileManager().getPkmnFileByName(pkmn4))){
            validPkmn++;
        }else{ invalidPkmn++; }
        
        if(Game.getFileManager().isValidFile(Game.getFileManager().getPkmnFileByName(pkmn5))){
            validPkmn++;
        }else{ invalidPkmn++; }
        
        if(Game.getFileManager().isValidFile(Game.getFileManager().getPkmnFileByName(pkmn6))){
            validPkmn++;
        }else{ invalidPkmn++; }
        
        if(blankc+validPkmn == 6 && validPkmn > 0){
            validTrainer = true;
        }else{
            Game.getTextHelper().print("Trainer contains one or more invalid Pokemon files.");
            Game.gameLoop(true);
        }
        
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

        File p_atk1f = Game.getFileManager().getAttackFileByName(p_Atk_1);
        File p_atk2f = Game.getFileManager().getAttackFileByName(p_Atk_2);
        File p_atk3f = Game.getFileManager().getAttackFileByName(p_Atk_3);
        File p_atk4f = Game.getFileManager().getAttackFileByName(p_Atk_4);

        String p_atk1_NAME = Game.getFileManager().getAtkName(p_atk1f);
        String p_atk1_TYPE = Game.getFileManager().getAtkType(p_atk1f);
        int p_atk1_POWER = Game.getFileManager().getPower(p_atk1f);
        int p_atk1_ACC = Game.getFileManager().getAccuracy(p_atk1f);

        String p_atk2_NAME = Game.getFileManager().getAtkName(p_atk2f);
        String p_atk2_TYPE = Game.getFileManager().getAtkType(p_atk2f);
        int p_atk2_POWER = Game.getFileManager().getPower(p_atk2f);
        int p_atk2_ACC = Game.getFileManager().getAccuracy(p_atk2f);

        String p_atk3_NAME = Game.getFileManager().getAtkName(p_atk3f);
        String p_atk3_TYPE = Game.getFileManager().getAtkType(p_atk3f);
        int p_atk3_POWER = Game.getFileManager().getPower(p_atk3f);
        int p_atk3_ACC = Game.getFileManager().getAccuracy(p_atk3f);

        String p_atk4_NAME = Game.getFileManager().getAtkName(p_atk4f);
        String p_atk4_TYPE = Game.getFileManager().getAtkType(p_atk4f);
        int p_atk4_POWER = Game.getFileManager().getPower(p_atk4f);
        int p_atk4_ACC = Game.getFileManager().getAccuracy(p_atk4f);

        Game.getTextHelper().print("Successfully loaded party Pokemon #1 into battle sequence.");
        
        //cycle to next item in list when a pokemon faints
        //if list is empty then end battle
        
        e_PKMN1_f = Game.getFileManager().getPkmnFileByName(pkmn1);
        e_PKMN2_f = Game.getFileManager().getPkmnFileByName(pkmn2);
        e_PKMN3_f = Game.getFileManager().getPkmnFileByName(pkmn3);
        e_PKMN4_f = Game.getFileManager().getPkmnFileByName(pkmn4);
        e_PKMN5_f = Game.getFileManager().getPkmnFileByName(pkmn5);
        e_PKMN6_f = Game.getFileManager().getPkmnFileByName(pkmn6);
        
        //cycle to next item in list when a pokemon faints
        //if list is empty then end battle
        trainerParty = new ArrayList<>();
        if(!pkmn1.isEmpty()){ trainerParty.add(e_PKMN1_f); }
        if(!pkmn2.isEmpty()){ trainerParty.add(e_PKMN2_f); }
        if(!pkmn3.isEmpty()){ trainerParty.add(e_PKMN3_f); }
        if(!pkmn4.isEmpty()){ trainerParty.add(e_PKMN4_f); }
        if(!pkmn5.isEmpty()){ trainerParty.add(e_PKMN5_f); }
        if(!pkmn6.isEmpty()){ trainerParty.add(e_PKMN6_f); }
                
        // INITIALIZE VARIABLES FOR THE FIRST PKMN.
        t_Name = Game.getFileManager().getName(trainerParty.get(timesCycled));
        t_Atk = Game.getFileManager().getAtk(trainerParty.get(timesCycled));
        t_Def = Game.getFileManager().getDef(trainerParty.get(timesCycled));
        t_Spd = Game.getFileManager().getSpd(trainerParty.get(timesCycled));
        t_Type = Game.getFileManager().getType(trainerParty.get(timesCycled));
        t_Hp = Game.getFileManager().getHP(trainerParty.get(timesCycled));
        t_Atk_1 = Game.getFileManager().getAttackSlotOne(trainerParty.get(timesCycled));
        t_Atk_2 = Game.getFileManager().getAttackSlotTwo(trainerParty.get(timesCycled));
        t_Atk_3 = Game.getFileManager().getAttackSlotThree(trainerParty.get(timesCycled));
        t_Atk_4 = Game.getFileManager().getAttackSlotFour(trainerParty.get(timesCycled));
        
        t_Atk1_f = Game.getFileManager().getAttackFileByName(t_Atk_1);
        t_Atk2_f = Game.getFileManager().getAttackFileByName(t_Atk_2);
        t_Atk3_f = Game.getFileManager().getAttackFileByName(t_Atk_3);
        t_Atk4_f = Game.getFileManager().getAttackFileByName(t_Atk_4);
        
        //name, type, power, acc
        t_Atk1_NAME = Game.getFileManager().getAtkName(t_Atk1_f);
        t_Atk1_TYPE = Game.getFileManager().getAtkType(t_Atk1_f);
        t_Atk1_POWER = Game.getFileManager().getPower(t_Atk1_f);
        t_Atk1_ACC = Game.getFileManager().getAccuracy(t_Atk1_f);
        
        t_Atk2_NAME = Game.getFileManager().getAtkName(t_Atk2_f);
        t_Atk2_TYPE = Game.getFileManager().getAtkType(t_Atk2_f);
        t_Atk2_POWER = Game.getFileManager().getPower(t_Atk2_f);
        t_Atk2_ACC = Game.getFileManager().getAccuracy(t_Atk2_f);
        
        t_Atk3_NAME = Game.getFileManager().getAtkName(t_Atk3_f);
        t_Atk3_TYPE = Game.getFileManager().getAtkType(t_Atk3_f);
        t_Atk3_POWER = Game.getFileManager().getPower(t_Atk3_f);
        t_Atk3_ACC = Game.getFileManager().getAccuracy(t_Atk3_f);
        
        t_Atk4_NAME = Game.getFileManager().getAtkName(t_Atk4_f);
        t_Atk4_TYPE = Game.getFileManager().getAtkType(t_Atk4_f);
        t_Atk4_POWER = Game.getFileManager().getPower(t_Atk4_f);
        t_Atk4_ACC = Game.getFileManager().getAccuracy(t_Atk4_f);
        
        
        
    }
    
    //use timesCycled and the public trainerparty arraylist to get the stats of the NEXT pokemon (++)
    public void resetTrainerVariables(){
        //called when a trainer's pkmn faints. 
        timesCycled++;
        if(timesCycled > trainerParty.size()){
            //end battle, player wins.
        }
        
    }
    
    
    /* Types of moves: Damage over time, damage, stat raising/lowering, weather, status effects */

    /* Calculate damage if the move is an attacking type of move. */
    // Then round up to a round number
    public long calcDmg(File attackingPkmn, File enemyPkmn, String move){
        //first get the stats of the move
        return Game.getFileManager().getPower(Game.getFileManager().getAttackFileByName(move));
    }

    //Sets the enemy pokemon data variables to the next available pokemon.
    public void resetInfo(){
        
    }


}
