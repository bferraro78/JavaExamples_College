package rpg;

public class Enemey {
	String name;
	int health;
	int combatHealth;
	int strn;
	int inti;
	int dext;
	int armor;
	int Exp;

	public Enemey(String name, int health, int strn, int inti, int dext, int armor) {
		 	this.name = name;
		 	this.health = health;
		 	this.strn = strn;
		 	this.inti = inti;
		 	this.dext = dext;
		 	this.armor = armor;
		 	this.combatHealth = this.health;
		 	setExp();
	}

	protected int basicAttack() {
        return 10;
    }

    protected void setExp() { this.Exp = (getHealth()/3); } 
    protected int getExp() { return this.Exp; }

	protected int getStr() { return this.strn; }
	protected int getInti() { return this.inti; }
	protected int getDext() { return this.dext; }
    protected int getArmorRating() { return this.armor; }
    protected int getHealth() { return this.health; }
    protected int getCombatHealth() { return this.combatHealth; }
    protected String getName() { return this.name; } 

	protected void setCombatHealth(int healthReductionOrIncrease) { this.combatHealth = (getCombatHealth()-healthReductionOrIncrease); }

    /* Generate Enemey based on Heroes level (and Armor/Wep rating?) */
    protected void generateRadomEnemey(int userLevel) {

    }
}


