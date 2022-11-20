package entities;


import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import input.KeyBoard;
import input.Mouse;



public class Camera {
	
	private float distanceFromPlayer = 35;
	private float angleAroundPlayer = 0;
	
	private Vector3f position = new Vector3f(0, 0, 0);
	public Vector3f ParentPosition = new Vector3f(0,0,0);
	private float pitch = 0;
	private float yaw = 0;
	private float roll = 0;
	
	private float oldp;
	private float oldy;
	private float oldr;
	
	private Mouse mouse;
	private KeyBoard keyboard;
	public Boolean Editing = true;
	private float SPEED = 0.5f;
	
	public String name = "Camera";
	private int ID = 0;
	
	public Camera(Mouse mouse, KeyBoard keyboard){
		this.mouse = mouse;
		this.keyboard = keyboard;
	}
	
	public void move(){
		if (Editing) {
			this.EnableMove();
			calculateZoom();
			calculatePitch();
			calculateAngleAroundPlayer();
			float horizontalDistance = calculateHorizontalDistance();
			float verticalDistance = calculateVerticalDistance();
			calculateCameraPosition(horizontalDistance, verticalDistance);
			this.yaw = 180 -+ angleAroundPlayer;
			yaw%=360;
		}
	}
	
	public void invertPitch(){
		this.pitch = -pitch;
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
	
	private void calculateCameraPosition(float horizDistance, float verticDistance){
		float theta = angleAroundPlayer;
		float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
		position.x = ParentPosition.x - offsetX;
		position.z = ParentPosition.z - offsetZ;
		position.y = ParentPosition.y + verticDistance + 4;
	}
	
	private float calculateHorizontalDistance(){
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch+4)));
	}
	
	private float calculateVerticalDistance(){
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch+4)));
	}
	
	private void calculateZoom(){
		float zoomLevel = mouse.getMouseScroll().y * 0.6f;
		mouse.MouseScroll = new Vector2f(0, 0);
		distanceFromPlayer -= zoomLevel;
		if(distanceFromPlayer<5){
			distanceFromPlayer = 5;
		}
	}
	
	private void calculatePitch(){
		if(mouse.isRightClick){
			float pitchChange = (mouse.getMousePosition().x / 2) * 0.003f;
			
			pitch -= pitchChange;
			if(pitch < -180){
				pitch = -180;
			}else if(pitch > 180){
				pitch = 180;
			}
		}
	}
	
	private void calculateAngleAroundPlayer(){
		if(mouse.isLeftClick){
			float angleChange = (mouse.getMousePosition().y / 2) * 0.003f;
			//mouse.MousePos = new Vector2f(0, 0);
			angleAroundPlayer -= angleChange;
		}
	}
	
	public void EnableMove() {
		if (keyboard.getKey("w")) {
			this.ParentPosition.x += SPEED;
		}
		if (keyboard.getKey("s")) {
			this.ParentPosition.x -= SPEED;
		}
		if (keyboard.getKey("d")) {
			this.ParentPosition.z += SPEED;
		}
		if (keyboard.getKey("a")) {
			this.ParentPosition.z -= SPEED;
		}
		
		if (keyboard.getKey("space")) {
			this.ParentPosition.y += SPEED;
		}
		if (keyboard.getKey("lshift")) {
			this.ParentPosition.y -= SPEED;
		}
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
	
	public void resetRot() {
		oldp = pitch;
		oldy = yaw;
		oldr = roll;
		
		
		pitch = 0;
		yaw = 0;
		roll = 0;
		//this.invertPitch();
	}
	
	public void restoreRot() {
		yaw = oldy;
		pitch = oldp;
		roll = oldr;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}
	
	
	

}