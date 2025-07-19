package tonton.creative.Creative;

public class IntegerHelper {

	public static boolean IsInteger(String str) {
	    try {
	        Integer.parseInt(str);
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}

}
