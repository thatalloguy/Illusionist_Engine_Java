package Editor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import components.CharacterComponent;
import components.Component;
import components.DynamicBodyComponent;
import components.ShapeComponent;
import entities.ECS;
import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import renderEngine.Loader;
import script.entityScript;
import textures.ModelTexture;

public class SaveEntity {
	
	public String modelPath;//private TexturedModel model;
	public String texturePath;
	
	public Vector3f position;
	public float rotX, rotY, rotZ;
	public Vector3f scale;
	public float Mass;
	public boolean isRendered = true;
	@SuppressWarnings("unused")
	private boolean isDebug = false;
	public String type = "Entity";
	public Boolean uselighting;
	public String name = "Entity";
	public List<String> scripts = new ArrayList<String>();
	public HashMap<Double, HashMap<String, Object>> components = new HashMap<Double, HashMap<String, Object>>();

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

		for (entityScript script : entity.scripts) {
			scripts.add(script.getPath());
		}

		for (Component component : entity.components) {
			this.components.put(component.ID, component.onSave());
		}
	}
	
	public Entity ConvertBack(ECS ecs, Console console) {
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
		for (String scriptName : this.scripts) {
			try {
				newEntity.scripts.add(new entityScript(scriptName));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		int i = 0;
		for (HashMap loadData : components.values()) {
			if (loadData.get("type").equals("Body")) {
				DynamicBodyComponent bodyComponent = new DynamicBodyComponent(ecs.worldManager, console);
				newEntity.addComponent(bodyComponent);
				bodyComponent.parent = newEntity;
				bodyComponent.onLoad(loadData, ecs.CM);
			} else if (loadData.get("type").equals("character")) {
				CharacterComponent characterComponent = new CharacterComponent(console);
				newEntity.addComponent(characterComponent);
				characterComponent.parent = newEntity;
				characterComponent.onLoad(loadData);
			}
		}
		return newEntity;
	}
}
