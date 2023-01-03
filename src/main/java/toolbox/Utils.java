package toolbox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.hackoeur.jglm.Vec3;

import javax.vecmath.Quat4f;


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

	public static javax.vecmath.Vector3f quat4fToVector3f(Quat4f quat) {
		// Convert the Quat4f object to a Vector3f object
		javax.vecmath.Vector3f v = new javax.vecmath.Vector3f();
		v.x = (float) Math.atan2(2 * (quat.w * quat.x + quat.y * quat.z), 1 - 2 * (quat.x * quat.x + quat.y * quat.y));
		v.y = (float) Math.asin(2 * (quat.w * quat.y - quat.z * quat.x));
		v.z = (float) Math.atan2(2 * (quat.w * quat.z + quat.x * quat.y), 1 - 2 * (quat.y * quat.y + quat.z * quat.z));
		return v;
	}

	public static Quat4f vector3fToQuat4f(javax.vecmath.Vector3f v) {
		// Convert the Vector3f object to a Quat4f object
		Quat4f quat = new Quat4f();
		float c1 = (float) Math.cos(v.z / 2);
		float s1 = (float) Math.sin(v.z / 2);
		float c2 = (float) Math.cos(v.x / 2);
		float s2 = (float) Math.sin(v.x / 2);
		float c3 = (float) Math.cos(v.y / 2);
		float s3 = (float) Math.sin(v.y / 2);
		float c1c2 = c1 * c2;
		float s1s2 = s1 * s2;
		quat.w = c1c2 * c3 - s1s2 * s3;
		quat.x = c1c2 * s3 + s1s2 * c3;
		quat.y = s1 * c2 * c3 + c1 * s2 * s3;
		quat.z = c1 * s2 * c3 - s1 * c2 * s3;
		return quat;
	}

	
	public static float getDistance(Vector3f point1, Vector3f point2) {
		float dx = point1.x - point2.x;
		float dy = point1.y - point2.y;
		float dz = point1.z - point2.z;

		return (float) Math.sqrt(dx*dx + dy*dy + dz*dz);
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
