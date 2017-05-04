package rpg;

public class barb extends dude {

	private int rage = 100;
	private int combatRage;

 	public barb(String name, int classID, int health, int strn, int inti, int dext) {
	 	super(name, classID, health, strn, inti, dext);
	 	/* Tuple: (Name of skill, Resource cost) */
	 	loadSkills();
	 	this.combatRage = this.rage;
    }

    public void loadSkills() {
    	resetSkills();
    	add(new Skill("Heal", 10));
	 	add(new Skill("Frenzy", 20));
	 	add(new Skill("BasicAttack", 0));
	}

	public int getResource() { return rage; }
	public int getCombatResource() { return combatRage; }
	public String getResourceName() { return "Rage"; }

	public void setResource() { }
	public void increaseResource(int rageIncrease) { this.rage = rageIncrease; }
	public void useCombatResource(int resourceUsed) { this.combatRage = (this.combatRage-resourceUsed); }
	public void regenCombatResource(int resourceGain) { this.combatRage = (this.combatRage+resourceGain); }
	public void resetCombatResource() { this.combatRage = this.rage; }


}
