package rpg;

import java.util.HashMap;

public class dude implements HeroInterface {

    private final static int MOVELISTMAX = 20;

    private String name;
    private int classID;
    private int level = 1;
    private int health; // HEALTH IS HEROES TOTAL HEALTH
    private int combatHealth; // COMBAT HEALTH IS USED TO DETERMINE HEALTH IN ONE INSTANCE OF COMBAT
    private int strn;
    private int inti;
    private int dext;
    private int vit;
    private int Exp;
    private int levelExp;
    private Skill[] skillSet = new Skill[MOVELISTMAX];
    private HashMap<String, Buff> buffLibrary = new HashMap<String, Buff>();

    /* Armor/Weapons */
    private Weapon mainHand = null;
    private Weapon offHand = null;
    private Armor helm = null;
    private Armor shoulders = null;
    private Armor bracers = null;
    private Armor gloves = null;
    private Armor torso = null;
    private Armor legs = null;
    private Armor boots = null;
    
    // TODO RESISTANTS HASHMAP

    public dude(String name, int classID, int vit, int strn, int inti, int dext) {
        this.name = name;
        this.classID = classID;
        this.vit = vit;
        this.strn = strn;
        this.inti = inti;
        this.dext = dext;
        this.Exp = 0;
        this.levelExp = 100; // TODO -- make this a ratio

        /* Set Heatlh Based On Vit */
        resetVariables();

    }


     public HashMap<String,Buff> selectAttack(int selectedAttack) {
        Move m = new Move(skillSet[selectedAttack].getMoveName(), this);
        HashMap<String,Buff> damageAndBuffs = m.moveDamage();
        return damageAndBuffs;
    }

    public void add(Skill ability) {
        for (int i = skillSet.length - 1; i > 0; i--) {
            skillSet[i] = skillSet[i - 1];
        }
        skillSet[0] = ability;
    }

    public void toStringSkills() {
        for (int i = 0; i < skillSet.length; i++) {
            if (skillSet[i] == null) { break; }
            if (skillSet[i].getMoveName().equals("BasicAttack")) {
                if (getClassName().equals("Barb")) {
                    System.out.println(i + ". " + "Swing | " + skillSet[i].getResourceCost());

                } else if (getClassName().equals("Wizard")) {
                    System.out.println(i + ". " + "Shoot | " + skillSet[i].getResourceCost());

                } else { // Rogue
                    System.out.println(i + ". " + "Shank | " + skillSet[i].getResourceCost());
                }
            } else {
                System.out.println(i + ". " + skillSet[i].toString());
            }
        }
    }

    /* Does the ability input number exist? */
    public boolean getAbility(int specifiedAttack) {
        if (specifiedAttack > (MOVELISTMAX-1) || skillSet[specifiedAttack] == null) {
            return false;
        } 
        return true;
    }

    public String getClassName() {
        if (classID == 1) {
            return "Barb";
        } else if (classID == 2) {
            return "Wizard";
        } else {
            return "Rogue";
        }        
    }

    /* Get Stats */
    public int getStrn() { return this.strn; }
    public int getInti() { return this.inti; }
    public int getDext() { return this.dext; }
    public int getVitality() { return this.vit; }
    public int getPrimaryStat() { 
         if (getClassName().equals("Barb")) {
            return this.strn;
        } else if (getClassName().equals("Wizard")) {
            return this.inti;
        } else { // Rogue
            return this.dext;
        } 
    }
    public int getHealth() { return this.health; }
    public int getCombatHealth() { return this.combatHealth; }
    public int getLevel() { return this.level; }
    public int getExp() { return this.Exp; }
    public String getName() { return this.name; } 
    public int getCritical() { return (10+(getDext()/8)+(getPrimaryStat()/10))/getLevel(); }
    public Skill[] getSkillSet() { return skillSet; }

    /* Set Armor/Weapons */
    public void equipHelm(Armor a) { this.helm = a; increaseStrn(a.getStrn()); increaseInti(a.getInti()); increaseDext(a.getDext()); increaseVitality(a.getVit());}
    public void equipShoulders(Armor a) { this.shoulders = a; increaseStrn(a.getStrn()); increaseInti(a.getInti()); increaseDext(a.getDext()); increaseVitality(a.getVit()); }
    public void equipBracers(Armor a) { this.bracers = a; increaseStrn(a.getStrn()); increaseInti(a.getInti()); increaseDext(a.getDext()); increaseVitality(a.getVit()); }
    public void equipGloves(Armor a) { this.gloves = a; increaseStrn(a.getStrn()); increaseInti(a.getInti()); increaseDext(a.getDext()); increaseVitality(a.getVit()); }
    public void equipTorso(Armor a) { this.torso = a; increaseStrn(a.getStrn()); increaseInti(a.getInti()); increaseDext(a.getDext()); increaseVitality(a.getVit()); }
    public void equipLegs(Armor a) { this.legs = a; increaseStrn(a.getStrn()); increaseInti(a.getInti()); increaseDext(a.getDext()); increaseVitality(a.getVit()); }
    public void equipBoots(Armor a) { this.boots = a; increaseStrn(a.getStrn()); increaseInti(a.getInti()); increaseDext(a.getDext()); increaseVitality(a.getVit()); }
    public void equipMH(Weapon w) { this.mainHand = w; }
    public void equipOH(Weapon w) { this.offHand = w; }
    public void equipArmor(Armor a) {
        if (a.getType().equals("Helmet")) {
            equipHelm(a);
        } else if (a.getType().equals("Torso")) {
            equipTorso(a);
        } else if (a.getType().equals("Shoulders")) {
            equipShoulders(a);
        } else if (a.getType().equals("Bracers")) {
            equipBracers(a);
        } else if (a.getType().equals("Gloves")) {
            equipGloves(a);
        } else if (a.getType().equals("Legs")) {
            equipLegs(a);
        } else if (a.getType().equals("Boots")) {
            equipBoots(a);
        } 
        resetVariables();
    }
    public void equipWeapon(Weapon w) { 
        if (w.getType().equals("MH")) {
            equipMH(w);
        } else { // OH
            equipOH(w);
        }
        resetVariables();
    }

    /* Set Health */
    public void resetHealth() { this.combatHealth = this.health; }
    public void setHealth() { 
        if (getClassName().equals("Barb")) { // Barb gets 2 health : 1 Vit
            this.health = (getVitality()*2); 
        } else if (getClassName().equals("Wizard")) {
            this.health = (int)((float)getVitality()*(float)(0.5)); // Wizard gets .5 health : 1 Vit
        } else { // Rogue
            this.health = (int)((float)getVitality()*(float)(0.75)); // Rogue gets .75 health : 1 Vit
        } 
        /* Set Combat Health */
        this.combatHealth = this.health; 
    }
    public void setCombatHealth(int healthReductionOrIncrease) { 
        this.combatHealth = (getCombatHealth()-healthReductionOrIncrease); 
        if (getCombatHealth() > getHealth()) {
            this.combatHealth = getHealth();
        }
    }

    /* Set Stats */
    public void increaseStrn(int strn) { this.strn = getStrn() + strn; }
    public void increaseInti(int inti) { this.inti = getInti() + inti; }
    public void increaseDext(int dext) { this.dext = getDext() + dext; }
    public void increaseVitality(int vit) { this.vit = getVitality() + vit; };
    /* TODO - DETERMINES WHAT HAPPEN WHEN WE LEVEL 
     * 1. Increase Stats
     * 2. Lootbox? 
     * 3. Skills? */
    public void increaseLevel() { 
        System.out.println("\nLEVEL THE FUCK UP!!!\n");
        if (getClassName().equals("Barb")) {
            increaseStrn(10);
            increaseInti(5);
            increaseDext(5);
            increaseVitality(10);
        } else if (getClassName().equals("Wizard")) {
            increaseStrn(5);
            increaseInti(10);
            increaseDext(5);
            increaseVitality(10);
        } else { // Dext
            increaseStrn(5);
            increaseInti(5);
            increaseDext(10);
            increaseVitality(10);
        }
        /* 2 times increase the XP it takes to level */
        this.levelExp = (this.levelExp*2);
        this.level = getLevel()+1; 
    }

    /* Increases XP by paramater. 
     * If level is grown, increase by 1 and carry over Exp */
    public void increaseExp(int xp) { 
        this.Exp = getExp()+xp; 
        while (this.Exp >= getLevelExp()) {
            int difference = getExp()-getLevelExp();
            this.Exp = difference;
            increaseLevel();
            /* Set health/resource/Skill Cost after stat changes */
            resetVariables();
        }
    }
    public int getLevelExp() { return this.levelExp; }

    /* Buff Library */
    public HashMap<String,Buff> getBuffLibrary() { return this.buffLibrary; } 
    public void resetBuffLibrary() { this.buffLibrary = new HashMap<String, Buff>(); }
    
    /* Skill Set */
    public void loadSkills() { }
    public void resetSkills() { this.skillSet = new Skill[MOVELISTMAX]; }

    /* RESOURCES-- OVERIDE BY SUBCLASSES */
    public int getResource() { return 0; }
    public int getCombatResource() { return 0; }
    public String getResourceName() { return ""; }
    public void increaseResource(int rageIncrease) { }
    public void regenCombatResource(int resourceGain) { }
    public void useCombatResource(int resourceUsed) { }
    public void resetCombatResource() { }
    public void setResource() { }
    /** RESET variables that are based on other variables 
     * (Ex. Health, Resources, skill costs)
     * Do this when you, 1. grow a level, 2. equip new armor/wep */
    public void resetVariables() {
        setResource(); // located in specific classes
        setHealth();
        loadSkills();
    }


    /** Determine Total Armor and Armor Rating **/
    public int getTotalArmor() {
        int armor = 0;
        int count = 0; // Hidden armor rating
        if (getTorso() != null) { armor += getTorso().getArmor(); count++; }
        if (getShoulders() != null) { armor += getShoulders().getArmor(); count++; }
        if (getHelm() != null) { armor += getHelm().getArmor(); count++; }
        if (getBoots() != null) { armor += getBoots().getArmor(); count++; }
        if (getLegs() != null) { armor += getLegs().getArmor(); count++; }
        if (getGloves() != null) { armor += getGloves().getArmor(); count++; }
        if (getBracers() != null) { armor += getBracers().getArmor(); count++; }
        return (armor+(count*25));
    }
    public float getArmorRating() {
        return (float)getTotalArmor() * (float)0.12;
    }


    /* Get Armor/Weapons */
    public Weapon getMH() {
        if (mainHand == null) { 

        } else { 
            return this.mainHand; 
        }
        return null;
    }

     public Weapon getOH() {
        if (offHand == null) { 

        } else { 
            return this.offHand; 
        }
        return null;
    }

    public Armor getTorso() {
        if (torso == null) { 

        } else { 
            return this.torso; 
        }
        return null;
    }

     public Armor getShoulders() {
        if (shoulders == null) { 

        } else { 
            return this.shoulders; 
        }
        return null;
    }
    
    public Armor getHelm() {
        if (helm == null) { 

        } else { 
            return this.helm; 
        }
        return null;
    }
    
    public Armor getBoots() {

        if (boots == null) { 

        } else { 
            return this.boots; 
        }
        return null;
    }
    
    public Armor getLegs() {
        if (legs == null) { 

        } else { 
            return this.legs; 
        }
        return null;
    }
    
    public Armor getGloves() {
        if (gloves == null) { 

        } else { 
            return this.gloves; 
        }
        return null;
    }

    public Armor getBracers() {
        if (bracers == null) { 

        } else { 
            return this.bracers; 
        }
        return null;
    }

        /** Show Eqiupment/Weapons **/
    public void printBody() {
        System.out.println();
        if (getMH() != null) {
            System.out.println("Main Hand: " + getMH().toString());
        } else {
            System.out.println("Main Hand: " );
        }

        if (getOH() != null) {
            System.out.println("Off Hand: " + getOH().toString());
        } else {
            System.out.println("Off Hand: " );
        }

        if (getHelm() != null) {
            System.out.println("Helmet: " + getHelm().toString());
        } else {
            System.out.println("Helmet: ");
        }

        if (getShoulders() != null) {
            System.out.println("Shoulders: " + getShoulders().toString());
        } else {
            System.out.println("Shoulders: ");
        }


        if (getTorso() != null) {
            System.out.println("Torso: " + getTorso().toString());
        } else {
            System.out.println("Torso: ");
        }

        if (getGloves() != null) {
            System.out.println("Gloves: " + getGloves().toString());
        } else {
            System.out.println("Gloves: ");
        }

        if (getBracers() != null) {
            System.out.println("Bracers: " + getBracers().toString());
        } else {
            System.out.println("Bracers: ");
        }

        if (getLegs() != null) {
           System.out.println("Legs: " + getLegs().toString());
        } else {
            System.out.println("Legs: ");
        }

        if (getBoots() != null) {
            System.out.println("Boots: " + getBoots().toString());
        } else {
            System.out.println("Boots: ");
        }


    }



}