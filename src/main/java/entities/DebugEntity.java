package entities;

import org.lwjgl.util.vector.Vector3f;

import models.RawModel;

public class DebugEntity extends Entity{

	public DebugEntity(RawModel model, Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale, float Mass) {
		super(model, position, rotX, rotY, rotZ, scale, Mass);
		super.isDebug = true;
		super.uselighting = true;
	}

	public void updatePosition(Entity entity) {
		this.position = entity.position;
		this.rotX = entity.rotX;
		this.rotY = entity.rotY;
		this.rotZ = entity.rotZ;
	}

	

}
