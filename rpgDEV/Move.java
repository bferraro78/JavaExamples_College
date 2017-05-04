package rpg;

import java.lang.reflect.*;
import java.util.HashMap; 
import java.util.Random;

public class Move { 

	private String attack;
	private dude mainCharacter;

	public Move(String attack, dude mainCharacter) {
		this.attack = attack;
		this.mainCharacter = mainCharacter;
	}
	
    protected String getAttack() { return this.attack; }

    /** Reads attack, carries out ability: RETURN damage of the attack **/
	protected HashMap<String,Buff> moveDamage() {
		Integer damage = 0;
		try {
		System.out.println();
		Method meth = this.getClass().getDeclaredMethod(getAttack());
		damage = (Integer) meth.invoke(this);
		} catch (NoSuchMethodException e) { 
			System.out.println("NO ABILITY WITH THAT NAME"); 
			System.exit(0); 
		} catch (IllegalAccessException e) {
			System.out.println(e); 
			System.exit(0);
		} catch (InvocationTargetException e) {
			System.out.println(e); 
			System.exit(0);
		}

		return mainCharacter.getBuffLibrary();
	}

	/*** ABILITIES ***/
	/** ALl CLASSES **/
    protected void BasicAttack() {
        if (mainCharacter.getClassName().equals("Barb")) {
            System.out.println("----Swing----");
        } else if (mainCharacter.getClassName().equals("Wizard")) {
            System.out.println("----Shoot----");
        } else { // Rogue
            System.out.println("----Shank----");
        }

		int damage = mainCharacter.getLevel()+mainCharacter.getPrimaryStat()+5;

		/* Calculate Crit Chance */
		if (critChance(0)) { damage = damage*2; }

		System.out.println("Damage Done: " + damage);
		mainCharacter.getBuffLibrary().put("damage", new Buff(damage, 0));
    }

	private void Heal() { // Heal over time
		System.out.println("----Heal Over Time----");
		int heal = -(mainCharacter.getInti()/2);

		/* Calculate Crit Chance */
		if (critChance(mainCharacter.getInti()/2)) { heal = heal*2; }

		mainCharacter.getBuffLibrary().put("healDot", new Buff(heal, 3));
	}



    /** BARB **/
	private void Frenzy() { // Chance to do double on top of Crit
		System.out.println("----FRENZY----");
		int damage = (mainCharacter.getStrn()+(25*mainCharacter.getLevel()));

		/* Calculate Crit Chance */
		if (critChance(0)) { damage = damage*2; }

		Random rand = new Random();
		int doubleStrike = rand.nextInt(100)+1;
		if(doubleStrike < 10) { damage = damage*2; System.out.println("Double Strike"); }

		System.out.println("Damage Done: " + damage);
		mainCharacter.getBuffLibrary().put("damage", new Buff(damage, 0));
	}

	private void Frenzy() { 


	}
	


	/** WIZARD **/
	private void Fireball() { // Chance to add DOT
		System.out.println("----Fireball BOOOOOM!!!----");
		int damage = (mainCharacter.getInti()+(25*mainCharacter.getLevel()));

		/* Calculate Crit Chance */
		if (critChance(0)) { damage = damage*2; }

		System.out.println("Damage Done: " + damage);
		mainCharacter.getBuffLibrary().put("damage", new Buff(damage, 0));

		/* %20 chance to Fire Dot ?? */
		Random rand = new Random();
		int dot = rand.nextInt(100)+1;
		if(dot < 20) { 
			System.out.println("BURNT!! 3 Turns Take Damage "); 
			mainCharacter.getBuffLibrary().put("fireDot", new Buff((mainCharacter.getInti()/2), 3));
		}
			
	}

	private void FreezeCone() { // Chance to reduce target enemey damage for few turns
		System.out.println("----Freeze Cone----");
		int damage = (mainCharacter.getInti()+(10*mainCharacter.getLevel()));

		/* Calculate Crit Chance */
		if (critChance(0)) { damage = damage*2; }

		System.out.println("Damage Done: " + damage);
		mainCharacter.getBuffLibrary().put("damage", new Buff(damage, 0));
		mainCharacter.getBuffLibrary().put("frozen", new Buff(0, 3));
	}



	/** ROGUE **/
	private void BackStab() { // Huge Crit increase
		System.out.println("----BACKSTAB!!!----");

		int damage = (mainCharacter.getDext()+(25*mainCharacter.getLevel()));

		/* Calculate Crit Chance */
		if (critChance(50)) { damage = damage*2; }
		
		System.out.println("Damage Done: " + damage);
		mainCharacter.getBuffLibrary().put("damage", new Buff(damage, 0));
	}







	/** Calculations **/	 
	private boolean critChance(int extraChance) {
		Random rand = new Random();
		int critChance = rand.nextInt(100) + 1;
		if (critChance >= 1 && critChance <= (mainCharacter.getCritical()+extraChance)) {
			System.out.println("~~~~~~~~~~~~CRITICAL HIT");
			return true;
		}
		return false;
	} 





}