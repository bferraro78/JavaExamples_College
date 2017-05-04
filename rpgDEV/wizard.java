package rpg;

public class wizard extends dude {

	private int mana;
	private int combatMana;

 	public wizard(String name, int classID, int health, int strn, int inti, int dext) {
	 	super(name, classID, health, strn, inti, dext);
	 	/* Tuple: (Name of skill, Resource cost) */
	 	loadSkills();
	 	setResource();
	 	this.combatMana = this.mana;
    }

    public void loadSkills() {
    	resetSkills();
    	add(new Skill ("Heal", (500)/getInti()));
	 	add(new Skill ("FreezeCone", 25));
	 	add(new Skill ("Fireball", 35));
	 	add(new Skill ("BasicAttack", 8));
    }

	public int getResource() { return mana; }
	public int getCombatResource() { return combatMana; }
	public String getResourceName() { return "Mana"; }

	public void setResource() { this.mana = (100+getInti()); }
	public void increaseResource(int manaIncrease) { this.mana = manaIncrease; }
	public void useCombatResource(int resourceUsed) { this.combatMana = (this.combatMana-resourceUsed); }
	public void regenCombatResource(int resourceGain) { this.combatMana = (this.combatMana+resourceGain); }
	public void resetCombatResource() { this.combatMana = this.mana; }


}
