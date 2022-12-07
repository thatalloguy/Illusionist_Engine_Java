package toolbox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.hackoeur.jglm.Vec3;


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
	
	public static Vector3f getDistance(Vector3f point1, Vector3f point2) {
		return new Vector3f( (point2.x - point1.x),(point2.y - point1.y), (point2.z - point1.z));
	}
	
	public static Matrix4f lookAt(Vector3f eye, Vector3f center, Vector3f up) {
		Vector3f f = new Vector3f(0, 0, 0).normalise(new Vector3f(center.x - eye.x, center.y - eye.y, center.z - eye.z));
		Vector3f u = new Vector3f(0, 0, 0).normalise(up);
		
		Vec3 fV = new Vec3(f.x, f.y, f.z);
		Vec3 uV = new Vec3(u.x, u.y, u.z);
		Vec3 sV = fV.cross(uV);
		
		Vector3f s = new Vector3f(0, 0, 0).normalise(new Vector3f(sV.getX(), sV.getY(), sV.getZ()));
		
		Matrix4f Result = new Matrix4f();
		Result.m00 = s.x;
		Result.m10 = s.y;
		Result.m20 = s.z;
		Result.m01 = u.x;
		Result.m11 = u.y;
		Result.m21 = u.z;
		Result.m02 = -f.x;
		Result.m12 = -f.y;
		Result.m22 = -f.z;
		Result.m30 = -Vector3f.dot(s, eye);
		Result.m31 = -Vector3f.dot(u, eye);
		Result.m32 = -Vector3f.dot(f, eye);
		return Result;
	}
}
