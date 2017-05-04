package rpg;

import java.util.HashMap;

/*** This class hdld all of the descriptions of a particular buff, debuff, or skill ***/
public class BuffLibrary {

	private static HashMap<String, String> buffLib = new HashMap<String, String>();


	public static void loadBuffLibrary() {
		System.out.println("LOADING");
		buffLib.put("frozen", "Frozen: Take 25% Less Damage ");
		buffLib.put("fireDot", "Burning: Take Dot Damage ");
		buffLib.put("healDot", "Heal: Heal HOT Damage ");

	}

	public static void getDescription(String s) { System.out.print(buffLib.get(s.trim())); }



}