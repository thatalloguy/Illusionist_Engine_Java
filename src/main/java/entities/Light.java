package entities;

import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import renderEngine.Loader;
import textures.ModelTexture;

public class Light {

	private Vector3f position;
	private Vector3f colour;
	private Vector3f attenuation = new Vector3f(1, 0, 0);
	
	public String name = "Light";
	private int ID = 0;
	public Entity visualEntity2;
	@SuppressWarnings("unused")
	private ECS ecs;
	public Light(Vector3f position, Vector3f colour) {
		this.position = position;
		this.colour = colour;
		
		Loader loader = new Loader();
		ModelData newData = OBJFileLoader.loadOBJ("res/camera.obj");
		RawModel newModel = loader.loadToVAO(newData.getVertices(), newData.getTextureCoords(), newData.getNormals(), newData.getIndices());
		TexturedModel newtexmod = new TexturedModel(newModel,new ModelTexture(loader.loadTexture("res/light.png")));
		this.visualEntity2 = new Entity(newtexmod, position , 0, 0, 0, new Vector3f(2, 2, 0), 0);
		this.visualEntity2.uselighting = true;
		this.visualEntity2.name = "Light";
		this.visualEntity2.myLight = this;
		
	}

	public Light(Vector3f position, Vector3f colour, Vector3f attenuation) {
		this.position = position;
		this.colour = colour;
		this.attenuation = attenuation;
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getColour() {
		return colour;
	}

	public void setColour(Vector3f colour) {
		this.colour = colour;
	}

	public Vector3f getAttenuation() {
		return attenuation;
	}

	public void setAttenuation(Vector3f attenuation) {
		this.attenuation = attenuation;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
	
	
	
	
}
