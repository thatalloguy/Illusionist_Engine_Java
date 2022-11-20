package Gizmo;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import input.Mouse;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import renderEngine.Loader;
import textures.ModelTexture;

public class Gizmo extends Entity{
	
	
	private static Loader loader = new Loader();
	private static ModelData wheelData = OBJFileLoader.loadOBJ("gizmotrans");
	private static RawModel wheelModel = loader.loadToVAO(wheelData.getVertices(), wheelData.getTextureCoords(), wheelData.getNormals(), wheelData.getIndices());
	private static TexturedModel wheeltexmod = new TexturedModel(wheelModel,new ModelTexture(loader.loadTexture("white")));
		
	public Gizmo(Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale,
			float Mass) {
		
		
		super(wheeltexmod, position, rotX, rotY, rotZ, scale, Mass);
		super.name = "XGIZMO";
	}
	
	
	public void update() {
		
	}

	
	
	
}
