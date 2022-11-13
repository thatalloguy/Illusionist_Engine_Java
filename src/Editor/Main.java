package Editor;



import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.lwjglx.LWJGLException;
import org.lwjglx.opengl.Display;

import entities.Camera;
import entities.EditorCamera;
import entities.Entity;
import entities.Item;
import entities.Light;
import fontRendering.TextMaster;
import imgui.ImGui;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import input.Mouse;
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

	public static void main(String[] args) throws ProtocolException, LWJGLException {

		DisplayManager displayManager = new DisplayManager();
		displayManager.createDisplay();
		
		
		//Create
		Loader loader = new Loader();
		TextMaster.init(loader);
		Mouse mouse = new Mouse();
		EditorCamera camera = new EditorCamera(mouse);	
		
		MasterRenderer renderer = new MasterRenderer(loader,camera);
		
		// Basic Scene
		
		List<Entity> entities = new ArrayList<Entity>();

		ModelData wheelData = OBJFileLoader.loadOBJ("Wheel");
		RawModel wheelModel = loader.loadToVAO(wheelData.getVertices(), wheelData.getTextureCoords(), wheelData.getNormals(), wheelData.getIndices());
		TexturedModel wheeltexmod = new TexturedModel(wheelModel,new ModelTexture(loader.loadTexture("wheelTexture")));
		
		List<Light> lights = new ArrayList<Light>();
		Light sun = new Light(new Vector3f(1000000,1000000,-100000),new Vector3f(1.4f,1.4f,1.4f));
		lights.add(sun);
		
		Entity cube = new Entity(wheeltexmod, new Vector3f(camera.getPosition()), 0.0f, 0.0f, 0.0f, Utils.toVector3f(2f), 2.8f);
		entities.add(cube);
		//Fbo
		
		FrameBuffer fbo = new FrameBuffer();
		
		//Main Loop
		

		
		KeyBoard keyboard = new KeyBoard();
		
		while (!DisplayManager.shouldClose()) {
			//System.out.println(mouse.getMousePosition());
			
			
			camera.Move();
			
			if (mouse.isRightClick) {
				camera.Enable();
			} else {
				camera.Disable();
			}
			
			// renderr here
			renderer.renderShadowMap(entities, sun);
			fbo.bindRefractionFrameBuffer();
			
			
			for(Entity entity:entities){
				renderer.processEntity(entity);
			}
			
			renderer.render(lights, camera);
			//GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			fbo.unbindCurrentFrameBuffer();
			
			// Imgui here
			DisplayManager.clearScreen();
			DisplayManager.imGuiGlfw.newFrame();
			ImGui.newFrame();
			//// LAYOUT
			
			//Entities Tab
			ImGui.setNextWindowPos(0, 0);
			ImGui.setNextWindowSize(Utils.getPrecentageOf(25,DisplayManager.getWidth()), Utils.getPrecentageOf(60,DisplayManager.getHeight()));
			ImGui.begin("Entities", imgui.flag.ImGuiWindowFlags.NoMove | imgui.flag.ImGuiWindowFlags.NoResize | imgui.flag.ImGuiWindowFlags.NoCollapse);
			
			// Entities Here
			
			ImGui.end();
			
			
			//ViewPort Tab
			ImGui.setNextWindowPos(Utils.getPrecentageOf(25, DisplayManager.getWidth()), 0);
			ImGui.setNextWindowSize(Utils.getPrecentageOf(55,DisplayManager.getWidth()), Utils.getPrecentageOf(60,DisplayManager.getHeight()));
			ImGui.begin("ViewPort", imgui.flag.ImGuiWindowFlags.NoMove | imgui.flag.ImGuiWindowFlags.NoResize | imgui.flag.ImGuiWindowFlags.NoCollapse);
			
			// ViewPort Here
			
			ImGui.getWindowDrawList().addImage(fbo.getRefractionTexture(), 0, 0, DisplayManager.getWidth(), DisplayManager.getHeight());
			
			ImGui.end();
			
			
			//Files Tab
			ImGui.setNextWindowPos(0, Utils.getPrecentageOf(60, DisplayManager.getHeight()));
			ImGui.setNextWindowSize(Utils.getPrecentageOf(25,DisplayManager.getWidth()), Utils.getPrecentageOf(40,DisplayManager.getHeight()));
			ImGui.begin("Files", imgui.flag.ImGuiWindowFlags.NoMove | imgui.flag.ImGuiWindowFlags.NoResize | imgui.flag.ImGuiWindowFlags.NoCollapse);
			
			// Files Here
			
			ImGui.end();
			
			

			//Console Tab
			ImGui.setNextWindowPos(Utils.getPrecentageOf(25, DisplayManager.getWidth()), Utils.getPrecentageOf(60, DisplayManager.getHeight()));
			ImGui.setNextWindowSize(Utils.getPrecentageOf(55,DisplayManager.getWidth()), Utils.getPrecentageOf(40,DisplayManager.getHeight()));
			ImGui.begin("Logger", imgui.flag.ImGuiWindowFlags.NoMove | imgui.flag.ImGuiWindowFlags.NoResize | imgui.flag.ImGuiWindowFlags.NoCollapse);
			
			// Files Here
			
			ImGui.end();
			
			
			
			//Properties Tab
			ImGui.setNextWindowPos(Utils.getPrecentageOf(80, DisplayManager.getWidth()), 0);
			ImGui.setNextWindowSize(Utils.getPrecentageOf(20,DisplayManager.getWidth()), DisplayManager.getHeight());
			ImGui.begin("Properties", imgui.flag.ImGuiWindowFlags.NoMove | imgui.flag.ImGuiWindowFlags.NoResize | imgui.flag.ImGuiWindowFlags.NoCollapse);
			
			// Properties Here
			
			ImGui.end();

			
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
		
		//DisplayManager.closeDisplay();
		System.exit(0);
	}
	
}
