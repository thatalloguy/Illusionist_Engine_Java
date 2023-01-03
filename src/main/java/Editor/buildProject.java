package Editor;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.groovy.GroovyBugError;
import org.lwjgl.util.vector.Vector3f;

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
import input.KeyBoard;
import input.Mouse;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import script.Script;
import script.ScriptEntity;
import script.entityScript;
import script.gameScript;

public class buildProject {

	public static void main(String[] args) throws IOException {
		DisplayManager.createDisplay();
		
		String PATH = "moveTest";
		
		Loader loader = new Loader();
		TextMaster.init(loader);
		Mouse mouse = new Mouse();
		KeyBoard keyboard = new KeyBoard();
		Console console = new Console();
		ECS ecs = new ECS(console, new Game(),keyboard, mouse);
		EditorCamera camera = new EditorCamera(mouse, keyboard, ecs);	
		
		GroovyShell shell = new GroovyShell();
		
		MasterRenderer renderer = new MasterRenderer(loader,camera);
		TextEditor EDITOR = new TextEditor();
		TextEditorLanguageDefinition lang = TextEditorLanguageDefinition.angelScript();
		EDITOR.setLanguageDefinition(lang);
		// Basic Scene
		
		List<Entity> entities = new ArrayList<Entity>();

		
		List<Light> lights = new ArrayList<Light>();
		Light sun = new Light(new Vector3f(1000000,1000000,-100000),new Vector3f(1.4f,1.4f,1.4f));
		sun.name = "Sun";
		lights.add(sun);
		ecs.addLight(sun);
		ecs.addCamera(camera);
	
		//Fbo
		ecs.selected_camera = camera;
		FrameBuffer fbo = new FrameBuffer();
		
		//Project managing
		Gizmo gizmo = new Gizmo(ecs, loader, mouse, keyboard);
		ProjectManager pm = new ProjectManager(keyboard, console, ecs, gizmo);
		ecs.pm = pm;
		ecs.SoundManager.pm = pm;
		
	
		
		//Main Loop

		ecs.initStyle();
		
		Boolean firstGameFrame = true;
		
		pm.currentProject = PATH;
		pm.loadProject(PATH, ecs, console);
		ecs.game.isRunning = true;
		camera.setPosition(ecs.cameraEntity.getPosition());

		while (!DisplayManager.shouldClose()) {
			//mouser.update();
			DisplayManager.clearScreen();
			renderer.renderShadowMap(entities, sun);
			lights = ecs.getAllLights();
			entities = ecs.getAllEntities();
			fbo.bindRefractionFrameBuffer();
			for(Entity entity:entities){
				entity.isPicking = false;
				if (entity.type.startsWith("E") && entity.isRendered) {
					renderer.processEntity(entity);
				}
			}

			camera.refresh();
			renderer.skyboxRenderer.isPicking = false;
			gizmo.processRendering(renderer);
			
			renderer.renderSkyBox = true;
			renderer.render(lights, camera, ecs);
			fbo.unbindCurrentFrameBuffer();
			// REnder the fbo with imgui
			DisplayManager.clearScreen();
			
			DisplayManager.imGuiGlfw.newFrame();
			ImGui.newFrame();

			
			//ImGui.setNextWindowPos(Utils.getPrecentageOf(25, DisplayManager.getWidth()), 0);
			ImGui.setNextWindowSize(DisplayManager.getWidth(), DisplayManager.getHeight());
			ImGui.setNextWindowPos(0, 0);
			ImGui.begin("ViewPort", imgui.flag.ImGuiWindowFlags.NoMove | imgui.flag.ImGuiWindowFlags.NoResize | imgui.flag.ImGuiWindowFlags.NoTitleBar |imgui.flag.ImGuiWindowFlags.NoCollapse);
			ImGui.getWindowDrawList().addImage(fbo.getRefractionTexture(), 0, 0, DisplayManager.getWidth(), DisplayManager.getHeight());
				
			ImGui.end();
			
			
			
			//camera.customViewMatrix = Maths.createViewMatrix(camera);
			if (firstGameFrame) {
				camera.setPosition(ecs.cameraEntity.getPosition());
				camera.setPitch(0);
				camera.setYaw(0);
				camera.setRoll(180);
				camera.rotate();
				camera.updateVectors();
				camera.refresh();
				
				
				ecs.cameraEntity.isRendered = false;
				for (Entity lightEntity : ecs.getAllEntities()) {
					if (lightEntity.type.startsWith("E")) {
						if (lightEntity.myLight != null) {
							lightEntity.isRendered = false;
						}
					}
				}
			}
			
			// Running Scripts

			try {
				for (gameScript script : ecs.gameScripts) {
					final Object e = shell.evaluate(new File(script.getPath()));

					
					Script engine = (Script) Proxy.newProxyInstance(e.getClass().getClassLoader(),
					         new Class[]{Script.class},
					         new InvocationHandler() {
					           public Object invoke(Object proxy, Method method, Object[] args) 
					             throws Throwable {
					             Method m = e.getClass().getMethod(method.getName(), method.getParameterTypes());
					             return m.invoke(e, args);
					           }});
				
					if (firstGameFrame) {
						engine.start(ecs, ecs.game);
					}
					engine.tick(ecs, ecs.game); 
					
				}
				for (Entity entity : ecs.getAllEntities()) {
					for (entityScript script : entity.scripts) {
						final Object e = shell.evaluate(new File(script.getPath()));
	
						
						ScriptEntity engine = (ScriptEntity) Proxy.newProxyInstance(e.getClass().getClassLoader(),
						         new Class[]{ScriptEntity.class},
						         new InvocationHandler() {
						           public Object invoke(Object proxy, Method method, Object[] args) 
						             throws Throwable {
						             Method m = e.getClass().getMethod(method.getName(), method.getParameterTypes());
						             return m.invoke(e, args);
						           }});
					
						if (firstGameFrame) {
							engine.start(ecs, entity);
							firstGameFrame = false;
							ECS.isFirstFrame = false;
						}
						engine.tick(ecs, entity); 
					}
					
				}
			
			} catch (GroovyBugError e) {
				
				console.addError(e.getMessage());
				System.out.println(e);
			}
			
			

			imgui.ImGui.render();
			DisplayManager.imGuiGl13.renderDrawData(ImGui.getDrawData());
			
			DisplayManager.updateImGui();
			DisplayManager.updateDisplay();
		}
		
		loader.cleanUp();
		
		renderer.cleanUp();
		imgui.ImGui.destroyContext();
		System.exit(0);
	}
	
}
