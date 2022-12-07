package entities;


import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;



import input.KeyBoard;
import input.Mouse;
import toolbox.Maths;



public class Camera {

	
	private Vector3f position = new Vector3f(0, 0, 0);
	public Vector3f ParentPosition = new Vector3f(0,0,0);
	public float pitch = 0;
	public float yaw = 0;
	private float roll = 0;
	
	private float oldp;
	private float oldy;
	private float oldr;
	
	@SuppressWarnings("unused")
	private Mouse mouse;
	@SuppressWarnings("unused")
	private KeyBoard keyboard;
	public Boolean Editing = true;
	
	public String name = "Camera";
	private int ID = 0;
	
	public Matrix4f customViewMatrix = null;
	
	@SuppressWarnings("unused")
	private ECS ecs;
	
	
	public Camera(Mouse mouse, KeyBoard keyboard, ECS ecs){
		this.mouse = mouse;
		this.keyboard = keyboard;
		this.ecs = ecs;
	}
	
	public void move(){

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
	
	public Matrix4f getViewMatrix() {
		if (this.customViewMatrix == null) {
			return Maths.createViewMatrix(this);
		} else {
			
			return customViewMatrix;
		}
		
	}
	

}