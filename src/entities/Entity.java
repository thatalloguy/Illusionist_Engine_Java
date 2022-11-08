package entities;

import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import models.RawModel;
import models.TexturedModel;
import textures.ModelTexture;

public class Entity {

	private TexturedModel model;
	private Vector3f position;
	private float rotX, rotY, rotZ;
	private Vector3f scale;
	public Vector3f Velocity = new Vector3f(0, 0, 0);
	public Vector3f Force  = new Vector3f(0, 0, 0);
	public float Mass;
	public boolean isRendered = true;
	public boolean isSimulated = true;
	private boolean isDebug = false;
	
	private int textureIndex = 0;
	
	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale, float Mass) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.Mass = Mass;
	}

	public Entity(TexturedModel model,int index, Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale) {
		this.model = model;
		this.textureIndex = index;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}
	
	public Entity(RawModel model, Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale, float Mass) {
		
		Loader loader = new Loader();
		
		TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("debugTexture")));
		
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
		this.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
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
	
	
}
