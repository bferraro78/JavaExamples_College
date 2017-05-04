package rpg;

public class Skill { 
    public String x; 
    public int y; 

    public Skill(String x, int y) { 
        this.x = x;
        this.y = y;
    } 


    public String toString() {
    	return getMoveName() + " | Cost: " + Integer.toString(getResourceCost());
    }

    /* Getter */
    protected String getMoveName() { return this.x; }
    protected int getResourceCost() { return this.y; }

} 