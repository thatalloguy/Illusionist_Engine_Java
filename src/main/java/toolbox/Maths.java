package toolbox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;

import javax.vecmath.Quat4f;

public class Maths {

	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}
	
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
	
	public static Matrix4f createTransformationMatrix(Vector3f tranlation, float rx, float ry, float rz, Vector3f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(tranlation, matrix, matrix);
		
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale), matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1,0,0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0,1,0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0,0,1), viewMatrix, viewMatrix);
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		return viewMatrix;
	}

	public static Quat4f rotateQuat4f(Vector3f rot, Quat4f quat) {
		Quat4f completed = quat;
		rot.x = (float) Math.toRadians(rot.x);
		rot.y = (float) Math.toRadians(rot.y);
		rot.z = (float) Math.toRadians(rot.z);
		completed = rotate(rot.x, rot.x, rot.x, completed);
		return completed;
	}

	private static Quat4f rotate(double angleX, double angleY, double angleZ, Quat4f quat) {
		double sinX = Math.sin(angleX / 2);
		double cosX = Math.cos(angleX / 2);
		double sinY = Math.sin(angleY / 2);
		double cosY = Math.cos(angleY / 2);
		double sinZ = Math.sin(angleZ / 2);
		double cosZ = Math.cos(angleZ / 2);
		return new Quat4f(
				(float) (quat.w * cosY * cosZ - quat.x * sinY * sinZ),
				(float) (quat.w * cosY * sinZ + quat.x * sinY * cosZ),
				(float) (quat.w * sinY * cosX + quat.y * cosY * sinX),
				(float) (quat.z * cosY * cosX - quat.y * sinY * sinX)
		);
	}
}
