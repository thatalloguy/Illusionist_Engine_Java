package entities;

import org.lwjgl.util.vector.Vector3f;

import models.RawModel;

public class DebugEntity extends Entity{

	public DebugEntity(RawModel model, Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale, float Mass) {
		super(model, position, rotX, rotY, rotZ, scale, Mass);
	}

	

}
