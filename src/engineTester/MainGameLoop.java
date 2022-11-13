package engineTester;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.DebugEntity;
import entities.Entity;
import entities.Item;
import entities.Light;
import entities.Player;
import fontMeshCreator.FontType;
import fontRendering.TextMaster;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import particles.ParticleMaster;
import particles.ParticleSystem;
import particles.ParticleTexture;
import physics.BoxCollider;
import physics.World;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;

//import renderEngine.EntityRenderer;
//import shaders.StaticShader;
import terrain.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;

public class MainGameLoop {

	private static Vector3f toVector3f(float var) {
		return new Vector3f(var, var, var);
	}
	
	public static void main(String[] args) {

		DisplayManager.createDisplay();
		Loader loader = new Loader();
		TextMaster.init(loader);
		ModelData playerModelData = OBJFileLoader.loadOBJ("person");
		RawModel playerModel = loader.loadToVAO(playerModelData.getVertices(), playerModelData.getTextureCoords(), playerModelData.getNormals(), playerModelData.getIndices());
		TexturedModel stanfordBunny = new TexturedModel(playerModel, new ModelTexture(loader.loadTexture("playerTexture")));
		
		Player player = new Player(stanfordBunny, new Vector3f(100,0,-50),0,180,0, toVector3f(0.6f), 0);
		
		Camera camera = new Camera();	
		
		MasterRenderer renderer = new MasterRenderer(loader,camera);
		ParticleMaster.init(loader, renderer.getProjectionMatrix());
		
		ModelData data = OBJFileLoader.loadOBJ("tree_pine");
		RawModel model = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
		
		TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("treePineTexture")));
		
		ModelData grassData = OBJFileLoader.loadOBJ("grassModel");
		RawModel grassModel = loader.loadToVAO(grassData.getVertices(), grassData.getTextureCoords(), grassData.getNormals(), grassData.getIndices());
		TexturedModel grassTextureModel = new TexturedModel(grassModel,new ModelTexture(loader.loadTexture("grassTexture")));
		
		ModelData lampData = OBJFileLoader.loadOBJ("lamp");
		RawModel lampModel = loader.loadToVAO(lampData.getVertices(), lampData.getTextureCoords(), lampData.getNormals(), lampData.getIndices());
		TexturedModel lamp = new TexturedModel(lampModel,new ModelTexture(loader.loadTexture("lamp")));
		
		ModelData wheelData = OBJFileLoader.loadOBJ("Wheel");
		RawModel wheelModel = loader.loadToVAO(wheelData.getVertices(), wheelData.getTextureCoords(), wheelData.getNormals(), wheelData.getIndices());
		TexturedModel wheeltexmod = new TexturedModel(wheelModel,new ModelTexture(loader.loadTexture("wheelTexture")));
		
		ModelData cubeData = OBJFileLoader.loadOBJ("cube");
		RawModel cubeModel = loader.loadToVAO(cubeData.getVertices(), cubeData.getTextureCoords(), cubeData.getNormals(), cubeData.getIndices());
		TexturedModel cubetexmod = new TexturedModel(cubeModel,new ModelTexture(loader.loadTexture("dog")));
		
		grassTextureModel.getTexture().setHasTransparency(true);
		grassTextureModel.getTexture().setUseFakeLighting(true);
		
		
		ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern2"));
		fernTextureAtlas.setNumberOfRows(2);

		
		ModelData fernData = OBJFileLoader.loadOBJ("fern");
		RawModel fernModel = loader.loadToVAO(fernData.getVertices(), fernData.getTextureCoords(), fernData.getNormals(), fernData.getIndices());
		TexturedModel fernTextureModel = new TexturedModel(fernModel,
				fernTextureAtlas);
		
		fernTextureModel.getTexture().setHasTransparency(true);
		fernTextureModel.getTexture().setUseFakeLighting(true);

		//*****************TERRAINNSSS******************************
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("prototype"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("Dev_map"));
		
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "Dev_map");
		
		
		
		List<Entity> entities = new ArrayList<Entity>();
		//entities.add(player);
		Random random = new Random();
		for(int i=0;i<500;i++){
			float x = random.nextFloat()*800 - 400;
			float z = random.nextFloat() * -600;
			//entities.add(new Entity(staticModel, new Vector3f(x,terrain.getHeightOfTerrain(x, z),z),0,0,0,3));
		}
		
		List<Entity> grass = new ArrayList<Entity>();
		Random grassrandom = new Random();
		for(int i=0;i<500;i++){
			float x = grassrandom.nextFloat()*800 - 400;
			float z = grassrandom.nextFloat() * -600;
			//entities.add(new Entity(grassTextureModel, new Vector3f(x,terrain.getHeightOfTerrain(x, z),z),0,0,0,grassrandom.nextFloat() * 1.3f));
		}
		
		List<Entity> plants = new ArrayList<Entity>();
		Random plantsrandom = new Random();
		for(int i=0;i<500;i++){
			float x = plantsrandom.nextFloat()*800 - 400;
			float z = plantsrandom.nextFloat() * -600;
			
			//entities.add(new Entity(fernTextureModel, new Vector3f(x,terrain.getHeightOfTerrain(x, z),z),0,0,0,plantsrandom.nextFloat() * 0.5f));
		}
		
		//**********LIGHTS********************
		
		
		
		List<Light> lights = new ArrayList<Light>();
		Light sun = new Light(new Vector3f(1000000,1000000,-100000),new Vector3f(1.4f,1.4f,1.4f));
		lights.add(sun);
		//lights.add(new Light(new Vector3f(185,6.7f,-293),new Vector3f(2,0,0), new Vector3f(1, 0.1f, 0.002f)));
		//lights.add(new Light(new Vector3f(370,14.2f,-300),new Vector3f(0,2,2), new Vector3f(1, 0.1f, 0.002f)));
		//.add(new Light(new Vector3f(293,9.8f,-305),new Vector3f(2,2,0), new Vector3f(1, 0.1f, 0.002f)));
		//Entity lampEntity = new Entity(lamp, new Vector3f(185,-4.7f,-293), 0, 0, 0, 0.6f);
		//entities.add(lampEntity);
		//entities.add(new Entity(lamp, new Vector3f(370,4.2f,-300), 0, 0, 0, 0.6f));
		//entities.add(new Entity(lamp, new Vector3f(293,-6.8f,-305), 0, 0, 0, 0.6f));
		
		//Terrain terrain2 = new Terrain(-1, -1, loader, texturePack, blendMap, "heightmap");
		
		
		
		FontType font = new FontType(loader.loadTexture("arial"), new File("res/arial.fnt"));
		//GUIText text = new GUIText("hello world", 3f, font, new Vector2f(0f, 0f), 1f, true);
		//text.setColour(1, 0, 0);
		
		//***********GUIS********************//
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture gui = new GuiTexture(loader.loadTexture("health"), new Vector2f(-0.75f, -0.75f), new Vector2f(0.25f, 0.25f));
		guis.add(gui);
		
		//GuiTexture shadowMap = new GuiTexture(renderer.getShadowMapTexture(), new Vector2f(0.5f, 0.5f), new Vector2f(0.5f, 0.5f));
		//guis.add(shadowMap);
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		
		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);
		
		ParticleTexture particleTexture = new ParticleTexture(loader.loadTexture("particleAtlas"),4);
		
		ParticleSystem system = new ParticleSystem(particleTexture, 100, 25, 0.3f, 4, 1);
		system.randomizeRotation();
		system.setDirection(new Vector3f(0, 1, 0), 0.07f);
		system.setLifeError(0.1f);
		system.setSpeedError(0.01f);
		system.setScaleError(0.8f);
		
		
		World world = new World(terrain);
		
		//************WHEEL
		
		Item wheel = new Item(wheeltexmod, new Vector3f(player.getPosition()), 0.0f, 0.0f, 0.0f, toVector3f(4.5f), 0.8f, "inv", "wheel");
		entities.add(wheel);
		wheel.setPosition(new Vector3f(player.getPosition().x, player.getPosition().y, player.getPosition().z + 10));
		BoxCollider wheelbox = new BoxCollider(wheel, true, world.DYNAMIC);

		
		Entity cube = new Entity(cubetexmod, new Vector3f(player.getPosition()), 0.0f, 0.0f, 0.0f, toVector3f(2f), 2.8f);
		Entity cube2 = new Entity(cubetexmod, new Vector3f(player.getPosition()), 0.0f, 0.0f, 0.0f, toVector3f(2f), 3.8f);
		BoxCollider cubebox = new BoxCollider(cube, true, world.DYNAMIC);
		BoxCollider cubebox2 = new BoxCollider(cube2, true, world.DYNAMIC);
		entities.add(cube);
		entities.add(cube2);
		entities.add(cubebox.debug);
		entities.add(cubebox2.debug);
		entities.add(wheelbox.debug);

		DebugEntity debugentity = new DebugEntity(cubeModel, new  Vector3f(player.getPosition()), 0.0f, 0.0f, 0.0f, toVector3f(2f), 2.8f);
		entities.add(debugentity);
		//Entity playerEntity = new Entity(player.getModel(), new Vector3f(player.getPosition()), 0.0f, 0.0f, 0.0f, player.getScale(), 2.8f);
		//BoxCollider playerHitBox = new BoxCollider(playerEntity, false, world.DYNAMIC);
		//Builder builder = new Builder(player, cube2);
		

		
		world.addBoxCollider(cubebox2);
		world.addBoxCollider(cubebox);
		world.addBoxCollider(wheelbox);
		//world.addBoxCollider(playerHitBox);
		
		while(!DisplayManager.shouldClose()){
			camera.Move();
			//playerEntity.setPosition(player.getPosition());
			world.Step();
			//player.setPosition(playerEntity.getPosition());
			player.move(terrain);
			//builder.Update();
			//picker.update();
			//wheel.setPosition(new Vector3f(player.getPosition().x, player.getPosition().y, player.getPosition().z + 10));

			//system.generateParticles(player.getPosition());
			
			ParticleMaster.update();
			renderer.renderShadowMap(entities, sun);
			


			
			if (renderer.skyboxRenderer.getIsDay()) {
				lights.get(0).setColour(new Vector3f(1.4f, 1.4f, 1.4f));
			} else {
				lights.get(0).setColour(new Vector3f(0.4f, 0.4f, 0.4f));
			}
			
			//renderer.processEntity(player);
			renderer.processTerrain(terrain);
			//renderer.processTerrain(terrain2);
			for(Entity entity:grass){
				renderer.processEntity(entity);
			}
			for(Entity entity:entities){
				renderer.processEntity(entity);
			}
			for(Entity entity:plants){
				renderer.processEntity(entity);
			}
			
			
			renderer.render(lights, camera);
			
			ParticleMaster.renderParticles(camera);
			
			guiRenderer.render(guis);
			TextMaster.render();
			//DisplayManager.Title = "Illusion Editor | FPS: " + DisplayManager.getFrameTimeSeconds();
			DisplayManager.updateDisplay();
		}

		ParticleMaster.cleanUp();
		TextMaster.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		
		DisplayManager.closeDisplay();

	}

}
