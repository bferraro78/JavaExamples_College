package rpg;

public class rogue extends dude {

	private int energey = 100;
	private int combatEnergey;

 	public rogue(String name, int classID, int health, int strn, int inti, int dext) {
	 	super(name, classID, health, strn, inti, dext);
	 	/* Tuple: (Name of skill, Resource cost) */
	 	loadSkills();
	 	this.combatEnergey = this.energey;
    }

    public void loadSkills() {
    	resetSkills();
    	add(new Skill("Heal", 10));
	 	add(new Skill("BackStab", 50));
	 	add(new Skill("BasicAttack", 5));
	}

	public int getResource() { return energey; }
	public int getCombatResource() { return combatEnergey; }
	public String getResourceName() { return "Energey"; }

	public void setResource() { }
	public void increaseResource(int energeyIncrease) { this.energey = energeyIncrease; }
	public void useCombatResource(int resourceUsed) { this.combatEnergey = (this.combatEnergey-resourceUsed); }
	public void regenCombatResource(int resourceGain) { this.combatEnergey = (this.combatEnergey+resourceGain); }
	public void resetCombatResource() { this.combatEnergey = this.energey; }

}
