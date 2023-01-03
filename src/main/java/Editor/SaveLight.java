package Editor;

import org.lwjgl.util.vector.Vector3f;

import entities.ECS;
import entities.Light;

public class SaveLight {
	
	
	private Vector3f position;
	private Vector3f colour;
	@SuppressWarnings("unused")
	private Vector3f attenuation = new Vector3f(1, 0, 0);
	
	public String name = "Light";
	@SuppressWarnings("unused")
	private int ID = 0;
	
	public SaveLight() {
		
	}
	
	public void ConvertEntity(Light entity) {
		this.position = entity.getPosition();
		this.colour = entity.getColour();
		this.attenuation = entity.getAttenuation();
		this.name = entity.name;
		this.ID = entity.getID();
		
	}
	
	public Light ConvertBack(ECS ecs) {
		Light newLight = new Light(this.position, this.colour);
		ecs.addEntity(newLight.visualEntity2);
		return newLight;
	}
}
