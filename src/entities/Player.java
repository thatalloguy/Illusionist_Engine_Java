//phill

package entities;


import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.DisplayManager;
import terrain.Terrain;

public class Player extends Entity{

	private static float RUN_SPEED = 20;
	private static final float TURN_SPEED = 160;
	public static final float GRAVITY = -50;
	private static final float JUMP_POWER = 30;
	
	@SuppressWarnings("unused")
	private static final float TERRAIN_HEIGHT = 0;
	
	private float currentXSpeed = 0;
	private float currentZSpeed = 0;
	private float currentTurnSpeed = 0;
	@SuppressWarnings("unused")
	private float upwardsSpeed = 0;
	public Camera camera;
	public boolean isInAir = false;
	
	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale, float Mass) {
		super(model, position, rotX, rotY, rotZ, scale, Mass);
		
	}
	
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	
	public void move(Terrain terrain) {
		//checkInputs();
		
		//super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		//float distanceX = currentXSpeed * DisplayManager.getFrameTimeSeconds();
		//float distanceZ = currentZSpeed * DisplayManager.getFrameTimeSeconds();
		//float dx = (float) (distanceX * Math.sin(Math.toRadians(this.camera.getYaw())));
		//float dz = (float) (distanceZ * Math.cos(Math.toRadians(-this.camera.getYaw())));
		//super.increasePosition(dx, 0, dz);
		
		
		// Jumpp
		upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		//super.increasePosition(0, upwardsSpeed  * DisplayManager.getFrameTimeSeconds(), 0);
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		if (super.getPosition().y < terrainHeight) {
			upwardsSpeed= 0;
			isInAir = false;
			super.getPosition().y = terrainHeight;
			super.setPosition(new Vector3f(super.getPosition().x, terrainHeight, super.getPosition().z));
			super.Velocity.y = 0;
		}
		
		
	}
	
	
	@SuppressWarnings("unused")
	private void jump() {
		if (!isInAir) {
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
		
	}
	/*
	private void checkInputs() {
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.currentZSpeed = -RUN_SPEED;
			this.currentXSpeed = RUN_SPEED;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.currentZSpeed = RUN_SPEED;
			this.currentXSpeed = -RUN_SPEED;
		}else {
			if (this.currentZSpeed > 0) {
				this.currentZSpeed -= 1;
			} else if (this.currentZSpeed < 0) {
				this.currentZSpeed += 1;
			}
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.currentXSpeed = -RUN_SPEED;
			this.currentZSpeed = -RUN_SPEED;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.currentXSpeed = RUN_SPEED;
			this.currentZSpeed = RUN_SPEED;
		}else {
			if (this.currentXSpeed > 0) {
				this.currentXSpeed -= 1;
			} else if (this.currentXSpeed < 0) {
				this.currentXSpeed += 1;
			}
		}
		
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			this.RUN_SPEED = 30;
		} else {
			this.RUN_SPEED = 20;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}
		
	}
	*/
	public static float getRUN_SPEED() {
		return RUN_SPEED;
	}

	public static float getTurnSpeed() {
		return TURN_SPEED;
	}

	public float getCurrentTurnSpeed() {
		return currentTurnSpeed;
	}

	public float getCurrentXSpeed() {
		return currentXSpeed;
	}

	public float getCurrentZSpeed() {
		return currentZSpeed;
	}
	
	public Entity getEntity() {
		return super.getEntity();
	}
	
}
