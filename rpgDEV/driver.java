package rpg;

import java.util.*;
import java.util.HashMap;

public class driver {


	public static void main(String[] args) throws InputMismatchException {
		/* Load Buff Library*/
		BuffLibrary.loadBuffLibrary();
		SkillLibrary.loadSkillLibrary();


		System.out.println("WELCOME TO TIITYLAND");
		// Load or New CHAR

		System.out.println("1 to New Chracter, 2 for Load character");
		Scanner s = new Scanner(System.in);
		int newOrLoad = s.nextInt();

		dude mainCharacter = null;
		if (newOrLoad == 1) { // NEW
			System.out.println("NEW CHARACTER CREATION");
			/* Read name and class thru stdin */
			System.out.println("1 for Barb, 2 for Zard, 3 for Rogue");
			int startClass = s.nextInt();

			/* Class Error Check*/
			if (startClass != 1 && startClass != 2 && startClass != 3) {
				System.out.println("CLASS NOT ADDED. COMING SOON IN EXPANSION");
				System.exit(0);
			}

			System.out.println("Heroes Name: ");
			String startName = s.next();

			/* Load beginnerArmor / weapon */
			mainCharacter = loadBegChar(startClass, startName);
		} else { // LOAD
			int id = s.nextInt();
			mainCharacter = loadChar(id);
		}


		System.out.println(mainCharacter.getHealth());
		System.out.println(mainCharacter.getCombatHealth());

		/* START SESSION */
		startGame(mainCharacter);

	}

	/* START GAME */
	protected static void startGame(dude mainCharacter) {

		Scanner s = new Scanner(System.in);
	
		boolean gameSession = true; // game quit
		while (gameSession) {



			System.out.println("\n----------------------------------------------Options Before Entering Dunegon:----------------------------------------------");
			System.out.print("						1. fight monster ");
			System.out.println(" 2. show armor/wep");
			System.out.print("						3. reveal inventory ");
			System.out.println(" 4. Stats ");
			System.out.println("						5. Quit");


			int option = s.nextInt();

			/** Fight Monster **/
			if (option == 1) {

			} else
			/** Eqiupment/Weapons **/
			if (option == 2) {
				mainCharacter.printBody();
				continue;
			} else
			/** Inventory **/
			if (option == 3) {

				continue;
			} else 
			/** Show Stats **/
			if (option == 4) {
				System.out.println("\nHERO'S STATS: ");
				System.out.println("LEVEL: " + mainCharacter.getLevel());
				System.out.println("XP TO NEXT LEVEL : " + mainCharacter.getLevelExp());
				System.out.println("XP: " + mainCharacter.getExp());
				System.out.println("\nSTATS: ");
				System.out.println("	Health: " + mainCharacter.getHealth());
				System.out.println("	Armor: " + mainCharacter.getTotalArmor());
				System.out.println("	Vitality: " + mainCharacter.getVitality());
				System.out.println("	Strength: " + mainCharacter.getStrn());
				System.out.println("	Intelligence: " + mainCharacter.getInti());
				System.out.println("	Dexterity: " + mainCharacter.getDext());
				System.out.println("	Critical Hit Chance: " + mainCharacter.getCritical());
				// Resistances

				continue;
			} else 
			if (option == 5) {
				System.out.println("THANKS FOR PLAYING...SAVING HERO...");
				gameSession = false;
				break;
			} else {
				continue;
			}

			/* Reset player health/BUFFS/DEBUFFS */
			mainCharacter.resetHealth();
			mainCharacter.resetBuffLibrary();
			mainCharacter.resetCombatResource();

			/** Combat **/
			/* TODO GENERATE RANOM ENEMEY */
					Enemey e = new Enemey("BITCH BOIII", 500, 5, 5, 5, 10);
			boolean death = true;

			System.out.println("START COMBAT ");
			System.out.println(mainCharacter.getName() + " VS " + e.getName());
			while (death) {


				/** PRINT HEALTH/RESOURCE BARS **/
				healthBar(mainCharacter.getCombatHealth(), mainCharacter.getHealth(), mainCharacter.getCombatResource(), e.getCombatHealth(), e.getHealth());

				/** Print Move List for specific class **/
				System.out.println("Ability List:");
				if (mainCharacter.getClassName().equals("Barb")) {
					mainCharacter.toStringSkills();
				} else if (mainCharacter.getClassName().equals("Wizard")) {
					mainCharacter.toStringSkills();
				} else { // Rogue
					mainCharacter.toStringSkills();
				}

				/** Checks if attack is valid move or not **/
				/* 1. If move exists */
				/* 2. If there is enough resource */
				int specifiedAttack = s.nextInt();
				boolean validAttack = true;
				while (validAttack) {
					if (mainCharacter.getAbility(specifiedAttack)) {
						int cost = mainCharacter.getSkillSet()[specifiedAttack].getResourceCost();
						if (mainCharacter.getCombatResource() <  cost) {
							System.out.println("Not Enough " + mainCharacter.getResourceName());
							specifiedAttack = s.nextInt();
						} else {
							validAttack = false;
							/* Spend Resource */
							mainCharacter.useCombatResource(cost);
						}
					} else {
						// Invalid attack, ask again
						System.out.println("Select A Correct Ability");
						specifiedAttack = s.nextInt();
					}
				}	

				/* Select Ability */
				mainCharacter.selectAttack(specifiedAttack);

				/* Print Damage Log */
				for(String currentKey : mainCharacter.getBuffLibrary().keySet()) {
					if (!currentKey.equals("damage")) {
						BuffLibrary.getDescription(currentKey + " ");
						System.out.println(mainCharacter.getBuffLibrary().get(currentKey).toString());
					}
				}
				
				/*** Apply damage/buffs/debuffs/armor/resistances ***/
				manageBuffs(mainCharacter, e, mainCharacter.getSkillSet()[specifiedAttack].getMoveName());


				

				/* TODO WHAT TO DO IN TIE */
				/* Check if someone is dead */
				if (mainCharacter.getCombatHealth() <= 0)  {
					System.out.println("Hero Has Died :(");
					death = false;
				}

				if (e.getCombatHealth() <= 0) {
					System.out.println("Enemey Has Died!!!");
					mainCharacter.increaseExp(e.getExp()); // INCRESE HERO
					death = false;
				}
		

				
			}			
			
		} //  end GAME

		/* SAVE HERO TO DB */


	}




	/*** Apply damage/buffs/debuffs/armor/resistances ***/
	private static void manageBuffs(dude mainCharacter, Enemey e, String specifiedAttackName) {
		/** HERO DAMAGE/BUFFS **/

		// Damage
		Buff damage = mainCharacter.getBuffLibrary().get("damage");
		if (damage != null) { 
			/* Take Damage */
			e.setCombatHealth(damage.getDamage()); 

			/** Rage is increased by Basic Attacks, reset if regen past full **/
			if (mainCharacter.getResourceName().equals("Rage") && specifiedAttackName.equals("BasicAttack")) {
				int regen = damage.getDamage()/(4*mainCharacter.getLevel());
				if ((mainCharacter.getCombatResource()+regen)  <= mainCharacter.getResource()) {
					mainCharacter.regenCombatResource(regen);
				} else {
					/* */
					mainCharacter.resetCombatResource();
				}
			}
			mainCharacter.getBuffLibrary().remove("damage");
		}

		// Fireball Dot
		Buff fireDot = mainCharacter.getBuffLibrary().get("fireDot");
		if (fireDot != null && fireDot.getDuration() != 0) { 
			int fireDamage = fireDot.getDamage();
			int newDuration = fireDot.getDuration()-1;
			/* Decrease Duration by 1 or Remvove */
			if (newDuration == 0) {
				mainCharacter.getBuffLibrary().remove("fireDot");
			} else {
				mainCharacter.getBuffLibrary().put("fireDot", new Buff(fireDamage, newDuration));
			}
			/* Take FIREDOT Damage */
			e.setCombatHealth(fireDamage);
		} 

		// Heal Dot
		Buff healDot = mainCharacter.getBuffLibrary().get("healDot");
		if (healDot != null && healDot.getDuration() != 0) {
			int healDamage = healDot.getDamage();
			int newDuration = healDot.getDuration()-1;

			/* Decrease Duration by 1 or Remvove */
			if (newDuration == 0) {
				mainCharacter.getBuffLibrary().remove("healDot");
			} else {
				mainCharacter.getBuffLibrary().put("healDot", new Buff(healDamage, newDuration));
			}
			mainCharacter.setCombatHealth(healDamage);
		}


		/** ENEMEY DAMAGE/BUFFS **/
		int damageToHero = e.basicAttack();

		/** Determine Damage Reduction **/	

		/* Armor Reduction */
		float floatFinalDamage = (float)damageToHero * (float)(mainCharacter.getArmorRating()/100);
		int intFinalDamageReduce = (int)(damageToHero - floatFinalDamage);



		/* Freeze Cone Reeduction */
		Buff frozen = mainCharacter.getBuffLibrary().get("frozen");
		if (frozen != null) {
			int frozenDamageReduce = frozen.getDamage();
			int newDuration = frozen.getDuration()-1;
			/* Decrease Duration by 1 or Remvove */
			if (newDuration == 0) {
				mainCharacter.getBuffLibrary().remove("frozen");
			} else {
				mainCharacter.getBuffLibrary().put("frozen", new Buff(0, newDuration));
			}
			intFinalDamageReduce = (int)((float)intFinalDamageReduce*(float).25);
		}

		/* Resisatnce Reduction TODO*/

		// Final Take Damage
		System.out.println("Damage Taken: " + intFinalDamageReduce);
		mainCharacter.setCombatHealth(intFinalDamageReduce);



		/** Resource Regain **/
		/* INCREASE RESOURCES ONLY IF IT IS BELOW THEIR MAX RESOURCE */
		if (mainCharacter.getResourceName().equals("Mana")) {
			int regen = mainCharacter.getInti()/2;
				if ((mainCharacter.getCombatResource()+regen)  <= mainCharacter.getResource()) {
					mainCharacter.regenCombatResource(regen);
				} else {
					/* Set Resource to Max */
					mainCharacter.resetCombatResource();
				}

		} else if (mainCharacter.getResourceName().equals("Energey")) {
			int regen = 20;
				if ((mainCharacter.getCombatResource()+regen)  <= mainCharacter.getResource()) {
					mainCharacter.regenCombatResource(regen);
				} else {
					/* Set Resource to Max */
					mainCharacter.resetCombatResource();
				}
		}
	}





	/** Write Health Bars **/
	private static void healthBar(int heroCombatHealth, int heroFull, int heroCombatResource, int enemeyCombatHealth, int enemeyFull) {
		float fullBars = (float) 50.00;
		float hero = ((float) heroCombatHealth/(float) heroFull)*fullBars;
		float enemey = ((float)enemeyCombatHealth/(float)enemeyFull)*fullBars;

		/* Print Barz */
		System.out.print("HERO HEALTH: ");
		for (int i = 0; i < hero; i++) 
			System.out.print("=");
		System.out.print(" " + heroCombatHealth);
		System.out.println(" | Resource: " + heroCombatResource);


		System.out.print("ENEMEY HEALTH: ");
		for (int i = 0; i < enemey; i++) 
			System.out.print("=");
		System.out.println(" " + enemeyCombatHealth);
		System.out.println();
	}	








	/*** CHARACTER RETRIEVE OR CREATION ***/
	protected static dude loadChar(int id) {
		dude d = new dude("bean", 1, 0,0,0,0);
		// create dude based on DB loaded option
		// id on character in DB will hold id's for all armor and weps
		
		
		return d;
	}

	protected static dude loadBegChar(int startClass, String name) {
		dude d = null;
		if (startClass == 1) { 
			d = new barb(name, startClass, 40, 20, 10, 10);
		} else if (startClass == 2) {
			d = new wizard(name, startClass, 40, 10, 20, 10);
		} else if (startClass == 3) {
			d = new rogue(name, startClass, 40, 10, 10, 20);
		} 
		
		
		// Create beginning Equipment for all characters
		int[] i = null;
		Armor begTorso = new Armor(1, 10, "Ragged Leather Torso", "Torso", 10, 1, 1, 1, i);
		Armor begLegs = new Armor(2, 10, "Ragged Leather Leggings", "Legs", 10, 1, 1, 1, i);
		Armor begBoots = new Armor(3, 10, "Ragged Leather Boots", "Boots", 10, 1, 1, 1, i);
		Weapon begDaga = new Weapon(1, 5, 0, "Rugged Daga", "MH", i);


		/* Set Hero's armor/weapons */
		d.equipArmor(begTorso);
		d.equipArmor(begLegs);
		d.equipArmor(begBoots);
		d.equipMH(begDaga);
		
		return d;
	}
	
}


