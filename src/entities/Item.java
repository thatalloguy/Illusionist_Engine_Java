package entities;


import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;

public class Item extends Entity{

	private String state;
	private String tag;
	
	public Item(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale, float Mass,String state, String tag) {
		super(model, position, rotX, rotY, rotZ, scale, Mass);
		this.state = state;
		this.tag = tag;
	}
	


}
