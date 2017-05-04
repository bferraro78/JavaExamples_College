package rpg;

import java.util.HashMap;

/*** This class hdld all of the descriptions of a particular buff, debuff, or skill ***/
public class SkillLibrary {

	private static HashMap<String, String> skillLib = new HashMap<String, String>(); // Skill to Skill Description


	public static void loadSkillLibrary() {
		System.out.println("LOADING");
		skillLib.put("BasicAttack", "A Basic Attack. ");
		skillLib.put("Frenzy", "Chance to swing for double damage. ");
		skillLib.put("Heal", "A HOT. ");
		skillLib.put("FreezeCone", "Take 25% less damage for 3 turns. ");
		skillLib.put("BackStab", "50% crit chance increase. ");
		skillLib.put("Fireball", "Chance to apply a fire dot for 3 turns. ");
	}

	public static void getDescription(String s) { System.out.print(skillLib.get(s.trim())); }



}