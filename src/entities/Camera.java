package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	
	private float DistanceFromPlayer = 0;
	private float angleAroundPlayer = 0;
	private final float Sensitivity = 0.04f;
	
	private Vector3f position = new Vector3f(0,3,0);
	private float pitch = 20;
	private float yaw = 0;
	private float roll;
	
	private float Yoffset = 5;
	
	private float offsetX;
	private float offsetZ;
	
	private Player player;
	
	public Camera(Player player) {
		this.player = player;
		this.player.setCamera(this);
		Mouse.setGrabbed(true);
		
	}
	
	public void Move() {
		//calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			Mouse.setGrabbed(false);
		}
	}
	
	public Vector3f getPosition() {
		return position;
	}
	public float getPitch() {
		return pitch;
	}
	public float getYaw() {
		return yaw;
	}
	public float getRoll() {
		return roll;
	}
	
	private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {
		float theta = player.getRotY() + angleAroundPlayer;
		offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
		offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));

		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + verticalDistance + Yoffset;
	}
	
	private float calculateHorizontalDistance() {
		return (float) (DistanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticalDistance() {
		return (float) (DistanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * Sensitivity;
		DistanceFromPlayer -= zoomLevel;
	}
	
	private void calculatePitch() {
		float pitchChange = Mouse.getDY() * Sensitivity;
		pitch -= pitchChange;
	}
	
	private void calculateAngleAroundPlayer() {
		float angleChange = Mouse.getDX() * 0.3f;
		angleAroundPlayer -= angleChange;
	}
	
	public float getOffsetX() {
		return offsetX;
	}
	
	public float getOffsetZ() {
		return offsetZ;
	}
}
