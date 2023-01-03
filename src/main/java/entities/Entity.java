package entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.bulletphysics.collision.dispatch.GhostObject;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.character.KinematicCharacterController;
import components.Component;
import components.ShapeComponent;
import org.lwjgl.util.vector.Vector3f;

import keybinds.Action;
import renderEngine.Loader;
import script.entityScript;
import models.RawModel;
import models.TexturedModel;
import textures.ModelTexture;

import javax.vecmath.Quat4f;

public class Entity {

	private TexturedModel model;
	public Vector3f position;
	public float rotX, rotY, rotZ;
	public Vector3f scale;
	public Vector3f Velocity = new Vector3f(0, 0, 0);
	public Vector3f Force  = new Vector3f(0, 0, 0);
	public float Mass;
	public boolean isRendered = true;
	public boolean isDebug = false;
	public boolean isPicking = false;
	public boolean uselighting = false;


	
	private Vector3f resetPosition;
	private Vector3f resetRotation;
	private Vector3f resetScale;
	public boolean isSelected =false;


	private int ID = 0;
	public String name = "Entity";
	
	public Light myLight; // his light :)
	public List<entityScript> scripts = new ArrayList<entityScript>();
	private Vector3f oldPosition;
	private Vector3f oldRotation;
	private Vector3f oldScale;
	private Action action;

	// Physics
	public boolean freeze = false;
	public List<Component> components = new ArrayList<Component>();
	public HashMap<Double, ShapeComponent> shapes = new HashMap<Double, ShapeComponent>();
	public RigidBody rigidBody;
	public KinematicCharacterController characterController;
	public DebugEntity debugEntity;
	public GhostObject ghostObject;


	private Vector3f tempPos =   new Vector3f(0, 0, 0);
	private Vector3f tempRot =   new Vector3f(0, 0, 0);
	private Vector3f tempScale = new Vector3f(0, 0, 0);
	
	private int frameCounter = 0;
	private int frameTimer = 155;
	private int idCounter = 0;
	private Boolean isChanged = false;
	
	private int textureIndex = 0;
	public String ModelPath;
	public String TexturePath;
	public Boolean isFacingCamera = false;
	
	public String type = "Entity";

	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale, float Mass) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.Mass = Mass;
		
		this.action = new Action("Entity");
		this.action.setEntity(this);
		this.oldPosition = position;
		this.oldRotation = new Vector3f(rotX, rotY, rotZ);
		this.oldScale = scale;
		
		
		this.resetPosition = position;
		this.resetRotation = new Vector3f(rotX, rotY, rotZ);
		this.resetScale = scale;
		
	}
	
	public Entity(RawModel model, Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale, float Mass) {
		
		Loader loader = new Loader();
		
		TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("res/blue.png")));
		
		this.model = staticModel;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.isDebug = true;
	}

	public void Update() {

		
		
	}

	public void setupReset() {
		this.resetPosition = this.getPosition();
		this.resetRotation = new Vector3f(getRotX(), getRotY(), getRotZ());
		this.resetScale = this.getScale();
	}
	
	public void reset() {
		setPosition(resetPosition);
		this.rotX = this.resetRotation.x; this.rotY = this.resetRotation.y; this.rotZ = this.resetRotation.z;
		setScale(resetScale);;
	}

	public void addComponent(Component component) {
		component.ID = idCounter;
		idCounter++;
		this.components.add(component);
	}

	public void removeComponent(Component component) {
		this.components.remove(component);
	}
	
	public void Undo() {
		
		this.tempPos = new Vector3f(position.x, position.y, position.z);
		this.tempRot = new Vector3f(rotX, rotY, rotZ);
		this.tempScale = new Vector3f(scale.x, scale.y, scale.z);
		
		float nelx = this.oldPosition.x;
		float nely = this.oldPosition.y;
		float nelz = this.oldPosition.z;
		this.position.x = nelx;
		this.position.y = nely;
		this.position.z = nelz;
		
		//Rot
		
		float relx = this.oldRotation.x;
		float rely = this.oldRotation.y;
		float relz = this.oldRotation.z;
		this.rotX = relx;
		this.rotY = rely;
		this.rotZ = relz;
		
		
		// Scale
		float selx = this.oldScale.x;
		float sely = this.oldScale.y;
		float selz = this.oldScale.z;
		this.scale.x = selx;
		this.scale.y = sely;
		this.scale.z = selz;
		
	}

	public void rotateWithQuanternion(Quat4f rotation) {
		double[] rot = new double[3];
		rot[0] = Math.toDegrees(Math.atan2(2 * (rotation.w * rotation.x + rotation.y * rotation.z), 1 - 2 * (rotation.x * rotation.x + rotation.y * rotation.y)));
		rot[1] = Math.toDegrees(Math.asin(2 * (rotation.w * rotation.y - rotation.z * rotation.x)));
		rot[2] = Math.toDegrees(Math.atan2(2 * (rotation.w * rotation.z + rotation.x * rotation.y), 1 - 2 * (rotation.y * rotation.y + rotation.z * rotation.z)));
		this.rotX = (float) rot[0];
		this.rotY = (float) rot[1];
		this.rotZ = (float) rot[2];
	}
	
	public void Redo() {
		this.position.x = tempPos.x;
		this.position.y = tempPos.y;
		this.position.z = tempPos.z;
		
		this.rotX = tempRot.x;
		this.rotY = tempRot.y;
		this.rotZ = tempRot.z;
		
		this.scale.x = tempScale.x;
		this.scale.y = tempScale.y;
		this.scale.z = tempScale.z;
	}

	public float getTextureXOffset() {
		int column = textureIndex%model.getTexture().getNumberOfRows();
		return (float) column/model.getTexture().getNumberOfRows();
	}
	
	public float getTextureYOffset() {
		int row = textureIndex/model.getTexture().getNumberOfRows();
		return (float)row/model.getTexture().getNumberOfRows();
	}
	
	public void increasePosition(float dx, float dy, float dz) {
		this.position.x +=dx;
		this.position.y +=dy;
		this.position.z +=dz;
	}

	public void increaseRotation(float rx, float ry, float rz) {
		this.rotX +=rx;
		this.rotY +=ry;
		this.rotZ +=rz;
	}
	
	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		Vector3f pos = new Vector3f(0, 0, 0);
		pos.x += position.x;
		pos.y += position.y;
		pos.z += position.z;
		return pos;
	}

	public void setPosition(Vector3f position) {
		
		if (frameCounter > frameTimer) {
			if (!(this.position.x == this.oldPosition.x && this.position.y == this.oldPosition.y && this.position.z == this.oldPosition.z)) {
						
				
						//Positon
						float nelx = this.getPosition().x;
						float nely = this.getPosition().y;
						float nelz = this.getPosition().z;
						this.oldPosition.x = nelx;
						this.oldPosition.y = nely;
						this.oldPosition.z = nelz;
						
						//Rot
						
						float relx = this.rotX;
						float rely = this.rotY;
						float relz = this.rotZ;
						this.oldRotation.x = relx;
						this.oldRotation.y = rely;
						this.oldRotation.z = relz;
						
						
						// Scale
						float selx = this.scale.x;
						float sely = this.scale.y;
						float selz = this.scale.z;
						this.oldScale.x = selx;
						this.oldScale.y = sely;
						this.oldScale.z = selz;
					
				
			}
			
			
			frameCounter = 0;
		} else {
			frameCounter += 1;
			
		}
		
		this.position = position;
	}

	public float getRotX() {
		float rotx = 0; rotx += rotX;
		return rotx;
	}

	public void setRotX(float rotX) {
		if (frameCounter > frameTimer) {
			if (!(this.position.x == this.oldPosition.x && this.position.y == this.oldPosition.y && this.position.z == this.oldPosition.z)) {
						
				
						//Positon

						float nelx = this.getPosition().x;
						float nely = this.getPosition().y;
						float nelz = this.getPosition().z;
						this.oldPosition.x = nelx;
						this.oldPosition.y = nely;
						this.oldPosition.z = nelz;
						
						//Rot
						
						float relx = this.rotX;
						float rely = this.rotY;
						float relz = this.rotZ;
						this.oldRotation.x = relx;
						this.oldRotation.y = rely;
						this.oldRotation.z = relz;
						
						
						// Scale
						float selx = this.scale.x;
						float sely = this.scale.y;
						float selz = this.scale.z;
						this.oldScale.x = selx;
						this.oldScale.y = sely;
						this.oldScale.z = selz;
					
				
			}
			
			
			frameCounter = 0;
		} else {
			frameCounter += 1;
			
		}
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public Vector3f getScale() {
		return scale;
	}

	public void setScale(Vector3f scale) {
		this.scale = scale;
	}

	public Vector3f getVelocity() {
		return Velocity;
	}

	public void setVelocity(Vector3f velocity) {
		Velocity = velocity;
	}

	public Vector3f getForce() {
		return Force;
	}

	public void setForce(Vector3f force) {
		Force = force;
	}

	public float getMass() {
		return Mass;
	}

	public void setMass(float mass) {
		Mass = mass;
	}
	
	public boolean getDebug() {
		return isDebug;
	}
	
	public Entity getEntity() {
		return this;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public Vector3f getOldPosition() {
		return oldPosition;
	}

	public Action getAction() {
		return action;
	}

}
