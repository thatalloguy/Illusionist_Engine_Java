package Editor;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import renderEngine.Loader;
import textures.ModelTexture;

public class SaveEntity {
	
	public String modelPath;//private TexturedModel model;
	public String texturePath;
	
	public Vector3f position;
	public float rotX, rotY, rotZ;
	public Vector3f scale;
	public Vector3f Velocity = new Vector3f(0, 0, 0);
	public Vector3f Force  = new Vector3f(0, 0, 0);
	public float Mass;
	public boolean isRendered = true;
	public boolean isSimulated = true;
	@SuppressWarnings("unused")
	private boolean isDebug = false;
	public String type = "Entity";
	public int lightID = 0;
	public Boolean uselighting;
	public String name = "Entity";
	
	
	
	public SaveEntity() {
		
	}
	
	public void ConvertEntity(Entity entity) {
		this.modelPath = entity.ModelPath;
		this.texturePath = entity.TexturePath;
		this.position = entity.position;
		this.rotX = entity.rotX;
		this.rotY = entity.rotY;
		this.rotZ = entity.rotZ;
		this.scale = entity.scale;
		this.uselighting = entity.uselighting;
		this.Mass = entity.Mass;
		this.isRendered = entity.isRendered;
		this.name = entity.name;
		this.type = entity.type;
		
	}
	
	public Entity ConvertBack() {
		System.out.println(modelPath + " | MODELL");
		Loader loader = new Loader();
		ModelData Data = OBJFileLoader.loadOBJ(this.modelPath);
		RawModel Model = loader.loadToVAO(Data.getVertices(), Data.getTextureCoords(), Data.getNormals(), Data.getIndices());
		TexturedModel texmod = new TexturedModel(Model,new ModelTexture(loader.loadTexture(this.texturePath)));
		
		Entity newEntity = new Entity(texmod, this.position, this.rotX, this.rotY, this.rotZ, this.scale, this.Mass);
		newEntity.ModelPath = this.modelPath;
		newEntity.TexturePath = this.texturePath;
		newEntity.name = this.name;
		newEntity.type = this.type;
		newEntity.uselighting = this.uselighting;
		//newEntity.myLight = ecs.getLight(this.lightID);
		return newEntity;
	}
}
