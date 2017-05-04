package rpg;

public class Weapon {
	int id;
	int attack;
	String name;
	String type;
	int range;
	int [] skills;
	
	
	Weapon(int id, int attack, int range, String name, String type, int[] skills) {
		this.id = id;
		this.attack = attack;
		this.range = range; // attack - (attack + range) Ex. 5-10 for "Rugged Daga"
		this.name = name;
		this.type = type;
		this.skills = skills;
	}

	public String toString() { return name; }
	public String getType() { return type; }
	public int getAttack() { return this.attack; }
	public int getRange() { return this.range; }


}
