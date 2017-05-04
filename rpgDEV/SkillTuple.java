package rpg;

public class SkillTuple { 
    public String x; 
    public int y; 

    public SkillTuple(String x, int y) { 
        this.x = x; 
        this.y = y; 
    } 

    // @Override
    public String toString() {
    	return getMoveName() + " | Cost: " + Integer.toString(getResourceCost());
    }

    /* Getter */
    protected String getMoveName() { return this.x; }
    protected int getResourceCost() { return this.y; }

    /* Setters */

} 