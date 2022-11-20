package toolbox;

import org.lwjgl.util.vector.Vector3f;

public class Utils {

	
	public static float getPrecentageOf(float precentage, float Number) {
		return (float) (Number / 100) * precentage;
	}
	
	public static Vector3f toVector3f(float var) {
		return new Vector3f(var, var, var);
	}
	
	public static double round (double value, int precision) {
	    int scale = (int) Math.pow(10, precision);
	    return (double) Math.round(value * scale) / scale;
	}
}
