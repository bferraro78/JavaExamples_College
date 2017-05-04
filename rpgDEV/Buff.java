package rpg;

/* This class is for a moves damage and duration */
public class Buff<X, Y> { 
    public int x; 
    public int y; 

    public Buff(int x, int y) { 
        this.x = x; 
        this.y = y; 
    } 

    public String toString() {
    	return "Heal or Damage: " + Integer.toString(getDamage()) + " | Duration: " + Integer.toString(getDuration());
    }

    /* Getter */
    protected int getDamage() { return this.x; }
    protected int getDuration() { return this.y; }

    /* Setters */
    protected void setDamage(int damage) { this.x = damage; }
    protected void setDuration(int duration) { this.y = duration; }
} 