package Editor;



import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import org.lwjgl.util.vector.Vector3f;
import org.lwjglx.LWJGLException;



import entities.ECS;
import entities.EditorCamera;
import entities.Entity;
import entities.Gizmo;
import entities.Light;
import fontRendering.TextMaster;
import imgui.ImGui;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.type.ImFloat;
import imgui.type.ImString;
import input.Mouse;
import keybinds.TMW;
import input.KeyBoard;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import textures.ModelTexture;
import toolbox.Utils;


public class Main extends Application {
	
	@Override
    protected void configure(Configuration config) {
        config.setTitle("Dear ImGui is Awesome!");
    }

    @Override
    public void process() {
        ImGui.text("Hello, World!");
    }

	public static void main(String[] args) throws LWJGLException, IOException {

		DisplayManager.createDisplay();
		
		//Create
		Loader loader = new Loader();
		TextMaster.init(loader);
		Mouse mouse = new Mouse();
		KeyBoard keyboard = new KeyBoard();
		Console console = new Console();
		
		ECS ecs = new ECS(console);
		EditorCamera camera = new EditorCamera(mouse, keyboard, ecs);	
		
		MasterRenderer renderer = new MasterRenderer(loader,camera);
		
		
		// Basic Scene
		
		List<Entity> entities = new ArrayList<Entity>();

		
		List<Light> lights = new ArrayList<Light>();
		Light sun = new Light(new Vector3f(1000000,1000000,-100000),new Vector3f(1.4f,1.4f,1.4f));
		sun.name = "Sun";
		lights.add(sun);
		ecs.addLight(sun);
		ecs.addCamera(camera);
	
		//Fbo
		
		FrameBuffer fbo = new FrameBuffer();
		String creationType = "None";
		
		//Project managing
		Gizmo gizmo = new Gizmo(ecs, loader, mouse, keyboard);
		gizmo.camera = camera;
		ProjectManager pm = new ProjectManager(keyboard, console, ecs, gizmo);
		
		JFileChooser fc2 = new JFileChooser();
		Component a2 = null;
		
		String latestSearchModel = "Search";
		String latestSearchTexture = "Search";
		//Main Loop

		camera.Disable();
		
		Boolean isCreatingEntity = false;
		ImString Namebuf = new ImString("Entity                          ");
		

		
		ImFloat Rbuf = new ImFloat(0);
		ImFloat Gbuf = new ImFloat(0);
		ImFloat Bbuf = new ImFloat(0);
		
		camera.toggle();
		boolean isFirstFrame = true;
		ecs.initStyle();
		TMW tmw = new TMW(ecs, keyboard);
		ecs.setSelectedCamera(camera);
		List<Entity> allEntities =  ecs.getAllEntities();
		
		for (Entity entity: allEntities) {
			if (entity.type.startsWith("E")) {
				tmw.addAction(entity.getAction());
			}
		}
		
		int viewTexture = fbo.getRefractionTexture();
		

		while (!DisplayManager.shouldClose()) {
			
			tmw.clearActions();
			for (Entity entity: entities) {
				if (entity.type.startsWith("E")) {
					tmw.addAction(entity.getAction());
				}
			}
			if (isFirstFrame) {
				camera.Enable();
				isFirstFrame = false;
			}
			

			camera.move();
			//mouser.update();
			
			
			tmw.Update();
			gizmo.Update();
			// renderr here
			renderer.renderShadowMap(entities, sun);
			lights = ecs.getAllLights();
			// Now mouse picking
			fbo.bindReflectionFrameBuffer();
			DisplayManager.clearScreen();
			entities = ecs.getAllEntities();
			for(Entity entity:entities){
				entity.isPicking = true;
				if (entity.type.startsWith("E") && entity.isRendered) {
					
					renderer.processEntity(entity);
				}
			}
			gizmo.processRendering(renderer);
			renderer.skyboxRenderer.isPicking = true;
			renderer.render(lights, camera);
			
			fbo.unbindCurrentFrameBuffer();
			
			fbo.bindRefractionFrameBuffer();
			
			
			for(Entity entity:entities){
				entity.isPicking = false;
				if (entity.type.startsWith("E") && entity.isRendered) {
					renderer.processEntity(entity);
				}
			}
			renderer.skyboxRenderer.isPicking = false;
			gizmo.processRendering(renderer);
			
			renderer.renderSkyBox = true;
			renderer.render(lights, camera);
			
			fbo.unbindCurrentFrameBuffer();
			
			// Imgui here
			DisplayManager.clearScreen();
			
			DisplayManager.imGuiGlfw.newFrame();
			ImGui.newFrame();
			
			
			
			if (mouse.isLeftClick && isCreatingEntity == false) {
				int x = (int) ((int) mouse.getMousePosition().x);
				int y = (int) ((int) mouse.getMousePosition().y);
				
				float id = fbo.readPixel(x, y) * 100;
				System.out.println(id);
				id = (float) Utils.round(id, 0) ;
				
				if (x > Utils.getPrecentageOf(25, DisplayManager.getWidth()) && x < Utils.getPrecentageOf(80, DisplayManager.getWidth()) && y < Utils.getPrecentageOf(60, DisplayManager.getHeight()) && ecs.getEntity(id) != null) {
					ecs.setSelectedEntity(ecs.getEntity(id));					
				}
				
			}
			if (mouse.isLeftClick == false && isCreatingEntity == false) {
				ecs.updateGimzos();
			}
			
			
			int x = (int) ((int) mouse.getMousePosition().x);
			int y = (int) ((int) mouse.getMousePosition().y);
			if (x > Utils.getPrecentageOf(25, DisplayManager.getWidth()) && x < Utils.getPrecentageOf(80, DisplayManager.getWidth()) && y < Utils.getPrecentageOf(60, DisplayManager.getHeight()) && mouse.isRightClick) {
				camera.Enable();
			} else {
				camera.Disable();
			}
 			

				//Do input
				ImGui.setNextWindowPos(Utils.getPrecentageOf(25, DisplayManager.getWidth()), 0);
				ImGui.setNextWindowSize(Utils.getPrecentageOf(55,DisplayManager.getWidth()), Utils.getPrecentageOf(60,DisplayManager.getHeight()));
				
				ImGui.begin("ViewPort", imgui.flag.ImGuiWindowFlags.NoMove | imgui.flag.ImGuiWindowFlags.NoResize | imgui.flag.ImGuiWindowFlags.NoCollapse);
				ImGui.getWindowDrawList().addImage(viewTexture, 0, 0, DisplayManager.getWidth(), DisplayManager.getHeight());
					
				ImGui.end();

				
			
			
			
			///// LAYOUT
			
			ImGui.beginMainMenuBar();
			
			if (ImGui.beginMenu("Project")) {
				if (ImGui.menuItem("New  | CTRL + N")) {
					pm.New();
					
				}
				ImGui.separator();
				ImGui.spacing();
				if (ImGui.menuItem("Open | CTRL + O")) {
					pm.Open(ecs);
				}
				
				ImGui.separator();
				ImGui.spacing();
				if (ImGui.menuItem("Save | CTRL + S")) {
					pm.Save();
				}
				
				
				ImGui.endMenu();
			}
			
			ImGui.endMainMenuBar();
			
			//ImGui.pushFont(font1);
			//Entities Tab
			ImGui.setNextWindowPos(0, 19);
			ImGui.setNextWindowSize(Utils.getPrecentageOf(25,DisplayManager.getWidth()) - 1, Utils.getPrecentageOf(60,DisplayManager.getHeight()) - 20);
			ImGui.begin("Game Objects", imgui.flag.ImGuiWindowFlags.NoMove | imgui.flag.ImGuiWindowFlags.NoResize | imgui.flag.ImGuiWindowFlags.NoCollapse);
			
			
			
			// Entities Here
			
			// Entities Creation
			ImGui.text("Scene  ");
			ImGui.sameLine();
			if (ImGui.button("+")) {
				isCreatingEntity = true;
			}
			
			
			
			// Entities List!
			ImGui.separator();
			entities = ecs.getAllEntities();
			for (Entity entity:entities) {
				if (entity.type.startsWith("E") && entity.isRendered) {
					if (ImGui.treeNode(entity.name + "(" + entity.getID() + ")")) {
						ecs.setSelectedEntity(entity);
						if (ImGui.treeNode(entity.getModel().name)) {
							ImGui.treePop();
						}
						ImGui.treePop();
					}
				}
			}
			lights = ecs.getAllLights();
			
			
			ImGui.end();
			if (isCreatingEntity) {
				if (creationType == "None") {
					ImGui.setNextWindowPos(Utils.getPrecentageOf(25, DisplayManager.getWidth()), Utils.getPrecentageOf(60, DisplayManager.getHeight()));
					ImGui.setNextWindowSize(Utils.getPrecentageOf(55,DisplayManager.getWidth()), Utils.getPrecentageOf(40,DisplayManager.getHeight()));
					
					ImGui.begin("Select Node");
					if (ImGui.button("Light") ) {
						creationType = "Light";
					}
					if (ImGui.button("Entity") ) {
						creationType = "Entity";
					}
					
					ImGui.end();
				}
				if (creationType == "Light") {
					ImGui.setNextWindowPos(Utils.getPrecentageOf(25, DisplayManager.getWidth()), Utils.getPrecentageOf(60, DisplayManager.getHeight()));
					ImGui.setNextWindowSize(Utils.getPrecentageOf(55,DisplayManager.getWidth()), Utils.getPrecentageOf(40,DisplayManager.getHeight()));
					ImGui.begin("Create New Entity: ",  imgui.flag.ImGuiWindowFlags.NoMove | imgui.flag.ImGuiWindowFlags.NoResize | imgui.flag.ImGuiWindowFlags.NoCollapse);
					
					ImGui.text("Entity's Name: ");
					
					ImGui.inputText(" ", Namebuf);
					ImGui.text(" ");
					
					
					if(ImGui.treeNodeEx("Color", imgui.flag.ImGuiTreeNodeFlags.DefaultOpen)) {
						ecs.setTextColor(1, 0, 0, 1);
						ImGui.text("R: "); ImGui.sameLine();
						ecs.resetTextColor();
						ImGui.inputFloat(" ", Rbuf);
											
						
						ecs.setTextColor(0, 1, 0, 1);
						ImGui.text("G: "); ImGui.sameLine();
						ecs.resetTextColor();
						ImGui.inputFloat(" ", Gbuf);
						
						ecs.setTextColor(0, 0, 1, 1);
						ImGui.text("B: "); ImGui.sameLine();
						ecs.resetTextColor();
						ImGui.inputFloat(" ", Bbuf);
						
						ImGui.text(" ");
						
						ImGui.treePop();
					}
					
					ImGui.text(" ");
					ImGui.text(" ");
					
					ImGui.separator();
					
					if (ImGui.button("Cancel")) {
						isCreatingEntity = false;
						latestSearchTexture = "Search";
						latestSearchModel = "Search";
						creationType = "None";
					}
					ImGui.sameLine();
					ImGui.text(" 		");
					
					ImGui.sameLine();
					
					if (ImGui.button("Done")) {
						isCreatingEntity = false;
						creationType = "None";
						try {
							Light newLight = new Light(camera.getPosition(), new Vector3f(Rbuf.get(), Gbuf.get(), Bbuf.get()));
							ecs.addEntity(newLight.visualEntity2);
							ecs.addLight(newLight);
							System.out.println(newLight.getID());
						} catch(Exception e)  {
							console.addError(e.getMessage());
						}
						
					}
					ImGui.end();
					
				}
				
				if (creationType == "Entity") {
					ImGui.setNextWindowPos(Utils.getPrecentageOf(25, DisplayManager.getWidth()), Utils.getPrecentageOf(60, DisplayManager.getHeight()));
					ImGui.setNextWindowSize(Utils.getPrecentageOf(55,DisplayManager.getWidth()), Utils.getPrecentageOf(40,DisplayManager.getHeight()));
					ImGui.begin("Create New Entity: ",  imgui.flag.ImGuiWindowFlags.NoMove | imgui.flag.ImGuiWindowFlags.NoResize | imgui.flag.ImGuiWindowFlags.NoCollapse);
					
					ImGui.text("Entity's Name: ");
					
					ImGui.inputText(" ", Namebuf);
					ImGui.text(" ");
					
					
					if(ImGui.treeNodeEx("Model", imgui.flag.ImGuiTreeNodeFlags.DefaultOpen)) {
						
						ImGui.text(".OBJ Path: "); ImGui.sameLine();
						if (ImGui.button(latestSearchModel)) {
							JFileChooser fc = new JFileChooser();
							if (pm.currentProject != null) {
								fc.setCurrentDirectory(new File("Projects/" + pm.currentProject + "/Resources"));
							} else {
								fc.setCurrentDirectory(new File("res/"));
							}
							Component a = null;
							int returnVal = fc.showOpenDialog(a);
							
							if (returnVal == JFileChooser.APPROVE_OPTION) {
								File file = fc.getSelectedFile();
								
								latestSearchModel = file.getPath();
							}
						}
						
						ImGui.text(".PNG Path: "); ImGui.sameLine();
						if (ImGui.button(latestSearchTexture)) {
							
							
							if (pm.currentProject != null) {
								fc2.setCurrentDirectory(new File("Projects/" + pm.currentProject + "/Resources"));
							}else {
								fc2.setCurrentDirectory(new File("res/"));
							}
							int returnVal2 = fc2.showOpenDialog(a2);
							if (returnVal2 == JFileChooser.APPROVE_OPTION) {
								File file2 = fc2.getSelectedFile();
								
								latestSearchTexture = file2.getPath();
							}
						}
						
						ImGui.text(" ");
						ImGui.text(" ");
						
						ImGui.separator();
						
						if (ImGui.button("Cancel")) {
							isCreatingEntity = false;
							latestSearchTexture = "Search";
							latestSearchModel = "Search";
							creationType = "None";
						}
						ImGui.sameLine();
						ImGui.text(" 		");
						
						ImGui.sameLine();
						
						if (ImGui.button("Done")) {
							isCreatingEntity = false;
							creationType = "None";
							try {
								ModelData newData = OBJFileLoader.loadOBJ(latestSearchModel);
								RawModel newModel = loader.loadToVAO(newData.getVertices(), newData.getTextureCoords(), newData.getNormals(), newData.getIndices());
								TexturedModel newtexmod = new TexturedModel(newModel,new ModelTexture(loader.loadTexture(latestSearchTexture)));
								
								//latestSearchTexture = "Search";
								//latestSearchModel = "Search";
								Entity newEntity = new Entity(newtexmod, camera.getPosition(), 0.0f, 0.0f, 0.0f, Utils.toVector3f(1f), 2.8f);
								newEntity.name = Namebuf.get();
								newEntity.ModelPath = latestSearchModel;
								newEntity.TexturePath = latestSearchTexture;
								ecs.addEntity(newEntity);
							} catch(Exception e)  {
								console.addError(e.getMessage());
								System.out.println(e);
							}
							
						}
						
						ImGui.treePop();
					}
					ImGui.end();
				}
				
				
				
			}
			
			//ViewPort Tab
			
			
			
			//Files Tab
			ImGui.setNextWindowPos(0, Utils.getPrecentageOf(60, DisplayManager.getHeight()));
			ImGui.setNextWindowSize(Utils.getPrecentageOf(25,DisplayManager.getWidth()), Utils.getPrecentageOf(40,DisplayManager.getHeight()));
			ImGui.begin("Files", imgui.flag.ImGuiWindowFlags.NoMove | imgui.flag.ImGuiWindowFlags.NoResize | imgui.flag.ImGuiWindowFlags.NoCollapse);
			
			// Files Here
			
			ImGui.end();
			
			pm.Render(ecs);

			//Console Tab
			ImGui.setNextWindowPos(Utils.getPrecentageOf(25, DisplayManager.getWidth()), Utils.getPrecentageOf(60, DisplayManager.getHeight()));
			ImGui.setNextWindowSize(Utils.getPrecentageOf(55,DisplayManager.getWidth()), Utils.getPrecentageOf(40,DisplayManager.getHeight()));
			ImGui.begin("Logger", imgui.flag.ImGuiWindowFlags.NoMove | imgui.flag.ImGuiWindowFlags.AlwaysVerticalScrollbar | imgui.flag.ImGuiWindowFlags.NoResize | imgui.flag.ImGuiWindowFlags.NoCollapse);
			
			
			List<String> logs = console.getLog();
			if (!isCreatingEntity) {
				for (String log: logs) {
					ImGui.text(log);
				}
			}
			// Files Here
			
			ImGui.end();
			
			
			
			//Properties Tab
			ImGui.setNextWindowPos(Utils.getPrecentageOf(80, DisplayManager.getWidth()), 0);
			ImGui.setNextWindowSize(Utils.getPrecentageOf(20,DisplayManager.getWidth()), DisplayManager.getHeight());
			ImGui.begin("Properties", imgui.flag.ImGuiWindowFlags.NoMove | imgui.flag.ImGuiWindowFlags.NoResize | imgui.flag.ImGuiWindowFlags.NoCollapse);
			
			// Properties Here
			
			ecs.RenderInfo();
		
			ImGui.end();

			//ImGui.popFont();
			//imgui render
			 
			imgui.ImGui.render();
			DisplayManager.imGuiGl13.renderDrawData(ImGui.getDrawData());
			
			DisplayManager.updateImGui();
			
			DisplayManager.updateDisplay();
		}
		fbo.cleanUp();
		loader.cleanUp();
		
		renderer.cleanUp();
		imgui.ImGui.destroyContext();

		System.exit(0);
	}
	
	


	
}
