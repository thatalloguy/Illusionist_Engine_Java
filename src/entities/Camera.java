package entities;


import org.lwjgl.util.vector.Vector3f;

public class Camera {

	
	private float DistanceFromPlayer = 0;
	private float angleAroundPlayer = 0;
	private final float Sensitivity = 0.04f;
	
	private Vector3f position = new Vector3f(0,3,0);
	public float pitch = 20;
	public float yaw = 0;
	public float roll;
	
	private float Yoffset = 5;
	
	private float offsetX;
	private float offsetZ;
	
	private Player player;
	
	private Vector3f right   = new Vector3f(1, 0, 0);
	private Vector3f up      = new Vector3f(0, 1, 0);
	private Vector3f forward = new Vector3f(0, 0, -1);
	
	public Camera() {
	}
	
	public void Move() {
		
		updateCameraVectors();
		rotate();
		//calculateZoom();
		//calculatePitch();
		//calculateAngleAroundPlayer();
		//float horizontalDistance = calculateHorizontalDistance();
		//float verticalDistance = calculateVerticalDistance();
		//calculateCameraPosition(horizontalDistance, verticalDistance);
		//this.yaw = 180 - (player.getRotY() + angleAroundPlayer);

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
	

	public void rotate() {
		
	}
	
	private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {

		float theta =  angleAroundPlayer;
		offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
		offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));

		position.x -= offsetX;
		position.z -= offsetZ;
		position.y += verticalDistance + Yoffset;
	}
	
	private float calculateHorizontalDistance() {
		return (float) (DistanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticalDistance() {
		return (float) (DistanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	/*
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
	}*/
	
	private void updateCameraVectors() {
		//this.yaw = (float) Math.toRadians(this.yaw);
		//this.pitch = (float) Math.toRadians(this.pitch);
		
		this.forward.x = (float) (Math.cos(yaw) * Math.cos(pitch));
		this.forward.y = (float) Math.sin(pitch);
		this.forward.z = (float) (Math.sin(yaw) * Math.cos(pitch));
		
		this.forward.normalise();
		this.right = right.normalise(Vector3f.cross(forward, new Vector3f(0, 1, 0), this.position));
		this.up = up.normalise(Vector3f.cross(right, new Vector3f(0, 0, 1), new Vector3f(0,0,0)));
		
	}
	
	
	public float getOffsetX() {
		return offsetX;
	}
	
	public float getOffsetZ() {
		return offsetZ;
	}


	
	
	
}
