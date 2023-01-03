package Editor;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JFileChooser;

import components.Component;
import components.DynamicBodyComponent;
import groovy.lang.GroovyRuntimeException;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImInt;
import org.codehaus.groovy.GroovyBugError;
import org.codehaus.groovy.GroovyException;
import org.lwjgl.util.vector.Vector3f;
import org.lwjglx.LWJGLException;

import entities.ECS;
import entities.EditorCamera;
import entities.Entity;
import entities.Gizmo;
import entities.Light;
import fontRendering.TextMaster;
import groovy.lang.GroovyShell;
import imgui.ImGui;
import imgui.extension.texteditor.TextEditor;
import imgui.extension.texteditor.TextEditorLanguageDefinition;
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
import script.ScriptEntity;
import script.entityScript;
import script.gameScript;
import textures.ModelTexture;
import toolbox.Maths;
import toolbox.Utils;


public class Main {
	


	public static void main(String[] args) throws LWJGLException, IOException, GroovyRuntimeException, GroovyBugError,  Exception { //String[] args) throws LWJGLException, IOException {

		DisplayManager.createDisplay();
		
		//Create
		Loader loader = new Loader();
		TextMaster.init(loader);
		Mouse mouse = DisplayManager.mouse;
		KeyBoard keyboard = new KeyBoard();
		Console console = new Console();
		boolean firstRead = true;
		ECS ecs = new ECS(console, new Game(),keyboard, mouse);
		EditorCamera camera = new EditorCamera(mouse, keyboard, ecs);	
		
		GroovyShell shell = new GroovyShell();
		
		MasterRenderer renderer = new MasterRenderer(loader,camera);
		TextEditor EDITOR = new TextEditor();
		TextEditorLanguageDefinition lang = TextEditorLanguageDefinition.angelScript();
		EDITOR.setLanguageDefinition(lang);
		// Basic Scene
		
		List<Entity> entities = new ArrayList<>();

		
		List<Light> lights = new ArrayList<>();
		Light sun = new Light(new Vector3f(1000000,1000000,-100000),new Vector3f(1.4f,1.4f,1.4f));
		sun.name = "Sun";
		lights.add(sun);
		ecs.addLight(sun);
		ecs.addCamera(camera);
	
		//Fbo
		ecs.selected_camera = camera;
		FrameBuffer fbo = new FrameBuffer();
		String creationType = "None";
		
		//Project managing
		Gizmo gizmo = new Gizmo(ecs, loader, mouse, keyboard);
		ProjectManager pm = new ProjectManager(keyboard, console, ecs, gizmo);
		ecs.pm = pm;
		ecs.SoundManager.pm = pm;
		JFileChooser fc2 = new JFileChooser();
		String latestSearchModel = "Search";
		String latestSearchTexture = "Search";
		//Main Loop

		camera.Disable();
		
		boolean isCreatingEntity = false;
		ImString Namebuf = new ImString("Entity    ");
		ImFloat Massbuf = new ImFloat(1.0f);
		ImInt Type = new ImInt(3);
		String Runbutton = "Run";
		
		ImFloat Rbuf = new ImFloat(0);
		ImFloat Gbuf = new ImFloat(0);
		ImFloat Bbuf = new ImFloat(0);
		boolean updatedModelsRuntime = false;
		boolean updatedModelsEditor = false;
		
		Vector3f oldPostion = null;
		Vector3f oldRotation = null;
		camera.toggle();
		boolean isFirstFrame = true;
		ecs.initStyle();
		TMW tmw = new TMW(ecs, keyboard);
		int texturePlayId = loader.loadTexture("res/play.png");
		int textureAddId = loader.loadTexture("res/add.png");
		List<Entity> allEntities =  ecs.getAllEntities();
		
		for (Entity entity: allEntities) {
			if (entity.type.startsWith("E")) {
				tmw.addAction(entity.getAction());
			}
		}
		
		int viewTexture = fbo.getRefractionTexture();
		boolean firstGameFrame = true;



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
	
				oldPostion = ecs.cameraEntity.getPosition();
				oldRotation = new Vector3f(ecs.cameraEntity.getRotX(), ecs.cameraEntity.getRotY(), ecs.cameraEntity.getRotZ());
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
			renderer.render(lights, camera, ecs);
			
			fbo.unbindCurrentFrameBuffer();
			
			fbo.bindRefractionFrameBuffer();
			
			
			for(Entity entity:entities){
				entity.isPicking = false;
				if (entity.type.startsWith("E") && entity.isRendered) {
					renderer.processEntity(entity);
				} if (!ecs.game.isRunning && entity.debugEntity != null) {
					entity.debugEntity.updatePosition(entity);
					renderer.processEntity(entity.debugEntity);
				}
			}
			camera.refresh();
			renderer.skyboxRenderer.isPicking = false;
			gizmo.processRendering(renderer);
			
			renderer.renderSkyBox = true;
			renderer.render(lights, camera, ecs);

			fbo.unbindCurrentFrameBuffer();
			
			// Imgui here
			DisplayManager.clearScreen();
			
			DisplayManager.imGuiGlfw.newFrame();
			ImGui.newFrame();
			
			
			
			if (mouse.isLeftClick && !isCreatingEntity && !ecs.game.isRunning) {
				int x = (int) ((int) mouse.getMousePosition().x);
				int y = (int) ((int) mouse.getMousePosition().y);
				
				float id = fbo.readPixel(x, y) * 100;
				id = (float) Utils.round(id, 0) ;
				
				if (x > Utils.getPrecentageOf(15, DisplayManager.getWidth()) && x < Utils.getPrecentageOf(80, DisplayManager.getWidth()) && y < Utils.getPrecentageOf(70, DisplayManager.getHeight()) && ecs.getEntity(id) != null && gizmo.isSelected()) {
					ecs.setSelectedEntity(ecs.getEntity(id));					
				}
				
			}
			if (!mouse.isLeftClick && !isCreatingEntity && !ecs.game.isRunning) {
				ecs.updateGimzos();
			}

			if (mouse.isRightClick && !ecs.game.isRunning) {
				camera.Enable();
			} else {
				camera.Disable();
			}


				//Do input
				ImGui.setNextWindowPos(Utils.getPrecentageOf(15, DisplayManager.getWidth()), 0);
				ImGui.setNextWindowSize(Utils.getPrecentageOf(65,DisplayManager.getWidth()), Utils.getPrecentageOf(70,DisplayManager.getHeight()));
				
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
			ImGui.setNextWindowSize(Utils.getPrecentageOf(15,DisplayManager.getWidth()) - 1, Utils.getPrecentageOf(60,DisplayManager.getHeight()) - 20);
			ImGui.begin("Game Objects", imgui.flag.ImGuiWindowFlags.NoMove | imgui.flag.ImGuiWindowFlags.NoResize | imgui.flag.ImGuiWindowFlags.NoCollapse);
			
			
			
			// Entities Here
			
			// Entities Creation
			ImGui.text("Scene  ");
			ImGui.sameLine();
			if (ImGui.imageButton(textureAddId, 32,  32)) {
				isCreatingEntity = true;
			}
			
			ImGui.sameLine(); ImGui.text(" "); ImGui.sameLine();
			if (ImGui.imageButton(texturePlayId, 32,  32)) {
				if (ecs.game.isRunning) {

					texturePlayId = loader.loadTexture("res/play.png");

					camera.setPosition(oldPostion);
					camera.setPitch(oldRotation.x);
					camera.setYaw(oldRotation.y);
					camera.setRoll(oldRotation.z);
					camera.moveCamera();
					camera.rotate();
					camera.updateVectors();
					camera.customViewMatrix = Maths.createViewMatrix(camera);
					updatedModelsEditor =false;
					ecs.game.isRunning = false;
				} else {
					oldPostion = camera.getPosition();
					oldRotation = new Vector3f(camera.getPitch(), camera.getYaw(), camera.getRoll());
					ecs.game.isRunning = true;
					ECS.isFirstFrame = true;

					texturePlayId = loader.loadTexture("res/pause.png");
					ecs.currentGameScript = null;
					ecs.currentScript = null;
					camera.setPosition(ecs.cameraEntity.getPosition());
					camera.setPitch(0);
					camera.setYaw(0);
					camera.setRoll(180);
					camera.moveCamera();
					camera.rotate();
					camera.updateVectors();
					camera.customViewMatrix = Maths.createViewMatrix(camera);
					updatedModelsRuntime = false;
				}
			}
			if (!ecs.game.isRunning) {


				if (!updatedModelsEditor) {
					ecs.cameraEntity.isRendered = true;
					ecs.worldManager.cleanupWorld();
					for (Entity lightEntity : ecs.getAllEntities()) {
						if (lightEntity.type.startsWith("E")) {
							lightEntity.reset();
							if (lightEntity.myLight != null) {
								lightEntity.isRendered = true;
							}
						}
					}
					updatedModelsEditor = true;
				}
			}
			
			if (ecs.game.isRunning) {

				if (!updatedModelsRuntime) {
					ecs.worldManager.setupWorld();

					for (Entity lightEntity : ecs.getAllEntities()) {
						lightEntity.Velocity.set(0, 0, 0);
						if (lightEntity.type.startsWith("E")) {
							lightEntity.setupReset();
							if (lightEntity.myLight != null) {
								lightEntity.isRendered = false;
							}
						}
					}
					for (DynamicBodyComponent component : ecs.CM.bodies) {
						component.Bake(false);
					}
					updatedModelsRuntime = true;
				}



				try {
					ecs.worldManager.Step(1/60f);
					for (Entity entity : ecs.getAllEntities()) {
						for (entityScript script : entity.scripts) {
							final Object e = shell.evaluate(new File(script.getPath()));


							ScriptEntity engine = (ScriptEntity) Proxy.newProxyInstance(e.getClass().getClassLoader(),
							         new Class[]{ScriptEntity.class},
									(proxy, method, args1) -> {
									  Method m = e.getClass().getMethod(method.getName(), method.getParameterTypes());
									  return m.invoke(e, args1);
									});

							if (firstGameFrame) {
								engine.start(ecs, entity);
								firstGameFrame = false;
								ECS.isFirstFrame = false;
							}
							engine.tick(ecs, entity);
						}

					}

				} catch (Exception e) {

					console.addError(e.toString());
					System.out.println(e.toString());
				}

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

			ImGui.end();
			if (isCreatingEntity && !ecs.game.isRunning) {
				if (creationType.equals("None")) {
					ImGui.setNextWindowPos(Utils.getPrecentageOf(15, DisplayManager.getWidth()), Utils.getPrecentageOf(70, DisplayManager.getHeight()));
					ImGui.setNextWindowSize(Utils.getPrecentageOf(65,DisplayManager.getWidth()), Utils.getPrecentageOf(30,DisplayManager.getHeight()));

					ImGui.begin("Select Node");
					if (ImGui.button("Light") ) {
						creationType = "Light";
					}
					if (ImGui.button("Entity") ) {
						creationType = "Entity";
					}

					
					ImGui.end();
				}

					
					

				if (creationType.equals("Light")) {
					ImGui.setNextWindowPos(Utils.getPrecentageOf(15, DisplayManager.getWidth()), Utils.getPrecentageOf(70, DisplayManager.getHeight()));
					ImGui.setNextWindowSize(Utils.getPrecentageOf(65,DisplayManager.getWidth()), Utils.getPrecentageOf(30,DisplayManager.getHeight()));
					ImGui.begin("Create New Entity: ",  imgui.flag.ImGuiWindowFlags.NoMove | imgui.flag.ImGuiWindowFlags.NoResize | imgui.flag.ImGuiWindowFlags.NoCollapse);
					
					ImGui.text("Light's Name: ");
					
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
				
				if (creationType.equals("Entity")) {
					ImGui.setNextWindowPos(Utils.getPrecentageOf(15, DisplayManager.getWidth()), Utils.getPrecentageOf(70, DisplayManager.getHeight()));
					ImGui.setNextWindowSize(Utils.getPrecentageOf(65,DisplayManager.getWidth()), Utils.getPrecentageOf(30,DisplayManager.getHeight()));
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
							int returnVal = fc.showOpenDialog(null);
							
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
							int returnVal2 = fc2.showOpenDialog(null);
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

								Entity newEntity = new Entity(newtexmod, camera.getPosition(), 0.0f, 0.0f, 0.0f, Utils.toVector3f(1f), Massbuf.get());
								newEntity.name = Namebuf.get();
								newEntity.ModelPath = latestSearchModel;
								newEntity.TexturePath = latestSearchTexture;

								System.out.println("TYPE :" +  " STRING :" + Type.get());

								ecs.addEntity(newEntity);
							} catch(Exception e)  {
								console.addError(e.getMessage());
								System.out.println(e.getMessage());
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
			ImGui.setNextWindowSize(Utils.getPrecentageOf(15,DisplayManager.getWidth()), Utils.getPrecentageOf(40,DisplayManager.getHeight()));
			ImGui.begin("Files", imgui.flag.ImGuiWindowFlags.NoMove  | imgui.flag.ImGuiWindowFlags.NoCollapse);
			
			// Files Here
			if (!ecs.gameScripts.isEmpty()) {
				for (gameScript script :ecs.gameScripts) {
					if (ImGui.button(script.getPath())) {
						ecs.currentGameScript = script;
					}
					ImGui.sameLine();
					if (ImGui.button("Delete")) {
						ecs.gameScripts.remove(script);
						break;
					}
				}
			}
			ImGui.end();
			
			pm.Render(ecs);

			//Console Tab
			ImGui.setNextWindowPos(Utils.getPrecentageOf(15, DisplayManager.getWidth()), Utils.getPrecentageOf(70, DisplayManager.getHeight()));
			ImGui.setNextWindowSize(Utils.getPrecentageOf(65,DisplayManager.getWidth()), Utils.getPrecentageOf(30,DisplayManager.getHeight()));
			ImGui.begin("Logger", imgui.flag.ImGuiWindowFlags.NoMove | imgui.flag.ImGuiWindowFlags.AlwaysVerticalScrollbar | imgui.flag.ImGuiWindowFlags.NoResize | imgui.flag.ImGuiWindowFlags.NoCollapse);
			
			
			List<String> logs = console.getLog();
			if (!isCreatingEntity && ecs.currentScript == null && ecs.currentGameScript == null) {
				if (ImGui.button("Clear Console")) {
					console.clearConsole();
				}
				firstRead = true;
				for (String log: logs) {
					ImGui.text(log);
				}
			}
			
			if (!isCreatingEntity && ecs.currentScript == null  && ecs.currentGameScript != null ) {
				if (ImGui.beginMenu("Options")) {
					if (ImGui.menuItem("Save scripts")) {
						String[] lines = EDITOR.getTextLines();
						ecs.currentGameScript.saveScript(lines);
						console.addMessage("Saved Script: " + ecs.currentGameScript.getPath());
					}
					if (ImGui.menuItem("Close")) {
						ecs.currentGameScript.saveScript(EDITOR.getTextLines());
						console.addMessage("Saved Script: " + ecs.currentGameScript.getPath());
						ecs.currentGameScript = null;
					}
					
					ImGui.endMenu();
				}
				if (ecs.currentGameScript != null) {
					EDITOR.render("Editing -" +ecs.currentGameScript.getPath());
				}
				
				
				if (firstRead) {
					String[] lines = {};
					assert ecs.currentGameScript != null;
					for (String text : ecs.currentGameScript.readScript()) {
	
						String[] newLines = new String[lines.length + 1];
						int i;
						for (i = 0; i < lines.length; i++) {
							newLines[i] = lines[i];
						}
						newLines[lines.length] = text;
						lines = newLines;
					}
					EDITOR.setTextLines(lines);
					firstRead = false;
				}
			}
			
			if (!isCreatingEntity && ecs.currentScript != null) {
				if (ImGui.beginMenu("Options")) {
					if (ImGui.menuItem("Save scripts")) {
						String[] lines = EDITOR.getTextLines();
						console.addMessage("Saved Script: " + ecs.currentScript.getPath());
						ecs.currentScript.saveScript(lines);
					}
					if (ImGui.menuItem("Close")) {
						ecs.currentScript.saveScript(EDITOR.getTextLines());
						console.addMessage("Saved Script: " + ecs.currentScript.getPath());
						ecs.currentScript = null;
					}
					
					ImGui.endMenu();
				}
				if (ecs.currentScript != null) {
					EDITOR.render("Editing -" +ecs.currentScript.getPath());
				}
				
				
				if (firstRead) {
					String[] lines = {};
					assert ecs.currentScript != null;
					for (String text : ecs.currentScript.readScript()) {
	
						String[] newLines = new String[lines.length + 1];
						int i;
						for (i = 0; i < lines.length; i++) {
							newLines[i] = lines[i];
						}
						newLines[lines.length] = text;
						lines = newLines;
					}
					EDITOR.setTextLines(lines);
					firstRead = false;
				}
				
			}
			// Files Here
			
			ImGui.end();
			
			
			
			//Properties Tab
			ImGui.setNextWindowPos(Utils.getPrecentageOf(80, DisplayManager.getWidth()), 0);
			ImGui.setNextWindowSize(Utils.getPrecentageOf(20,DisplayManager.getWidth()), DisplayManager.getHeight());
			ImGui.begin("Properties", imgui.flag.ImGuiWindowFlags.NoMove | imgui.flag.ImGuiWindowFlags.NoResize | ImGuiWindowFlags.AlwaysVerticalScrollbar| imgui.flag.ImGuiWindowFlags.NoCollapse);
			
			// Properties Here
			
			ecs.RenderInfo();


		
			ImGui.end();

			Component comp = ecs.CM.render();


			if (comp != null) {
				ecs.getSelected_entity().addComponent(comp);
				comp.parent = ecs.getSelected_entity();
				if (Objects.equals(comp.type, "shape")) {
					ecs.getSelected_entity().shapes.put(ecs.CM.lastShape.ID, ecs.CM.lastShape);
				}
			}
			
			// GAME RUNNIGN

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
