package physics;

import org.lwjgl.util.vector.Vector3f;

import entities.DebugEntity;
import entities.Entity;

public class BoxCollider {

	//private Vector3f my position;
	public Vector3f Scale;
	
	public Entity entity;
	
	public Vector3f Min = new Vector3f(0, 0, 0);
	public Vector3f Max =  new Vector3f(0, 0, 0);
	
	public boolean isSolid = true;
	public boolean isRendered = false;
	public DebugEntity debug;
	
	private int type;
	
	public BoxCollider (Entity entity, boolean isRendererd, int type) {
		//this.myposition = entity.getPosition();
		this.Scale = entity.getScale();
		this.entity = entity;
		this.isRendered = isRendererd;
		this.type = type;
		setupDebug();
		update();
	}
	

	
	private void setupDebug() {
		this.debug = new DebugEntity(entity.getModel().getRawModel(), entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), 
				new Vector3f(entity.getScale().x + 1f, entity.getScale().y + 1f, entity.getScale().z + 1f), entity.getMass());
	}
	
	private void updateDebugEntity() {
	
		debug.setPosition(entity.getPosition());
		debug.setScale(Scale);
		debug.isRendered = true;
	}
	
	private void calculateMinMax() {
		this.Min.x = entity.getPosition().x;
		this.Min.y = entity.getPosition().y;
		this.Min.z = entity.getPosition().z;
		
		this.Max.x = entity.getPosition().x + Scale.x + 2.1f;
		this.Max.y = entity.getPosition().y + Scale.y + 2.1f;
		this.Max.z = entity.getPosition().z + Scale.z + 2.1f;
	}
	
	
	public void update() {
		calculateMinMax();
		updateDebugEntity();
	}
	
	public int getType() {
		return this.type;
	}
	
}
