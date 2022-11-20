package Editor;



import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjglx.LWJGLException;
import org.lwjglx.opengl.Display;

import Gizmo.Gizmo;
import entities.Camera;
import entities.ECS;
import entities.EditorCamera;
import entities.Entity;
import entities.Item;
import entities.Light;
import fontRendering.TextMaster;
import imgui.ImFont;
import imgui.ImFontAtlas;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.extension.imguizmo.ImGuizmo;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
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
import toolbox.Maths;
import toolbox.MousePicker;
import toolbox.Utils;


public class Main extends Application {
	private static final float[] VIEW_MANIPULATE_SIZE = new float[]{128f, 128f};
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
		int opperation = imgui.extension.imguizmo.flag.Operation.TRANSLATE;
		
		//Create
		Loader loader = new Loader();
		TextMaster.init(loader);
		Mouse mouse = new Mouse();
		KeyBoard keyboard = new KeyBoard();
		EditorCamera camera = new EditorCamera(mouse, keyboard);	
		
		MasterRenderer renderer = new MasterRenderer(loader,camera);
		ECS ecs = new ECS();
		StateManager statemanager = new StateManager();
		
		// Basic Scene
		
		List<Entity> entities = new ArrayList<Entity>();

		ModelData wheelData = OBJFileLoader.loadOBJ("Wheel");
		RawModel wheelModel = loader.loadToVAO(wheelData.getVertices(), wheelData.getTextureCoords(), wheelData.getNormals(), wheelData.getIndices());
		TexturedModel wheeltexmod = new TexturedModel(wheelModel,new ModelTexture(loader.loadTexture("wheelTexture")));
		
		List<Light> lights = new ArrayList<Light>();
		Light sun = new Light(new Vector3f(1000000,1000000,-100000),new Vector3f(1.4f,1.4f,1.4f));
		lights.add(sun);
		ecs.addLight(sun);
		ecs.addCamera(camera);
		
		Entity cube = new Entity(wheeltexmod, new Vector3f(0, 0, 0), 0.0f, 0.0f, 0.0f, Utils.toVector3f(2f), 2.8f);
		Entity cube2 = new Entity(wheeltexmod, new Vector3f(0, 2, 0), 0.0f, 0.0f, 0.0f, Utils.toVector3f(2f), 2.8f);
		cube2.name = "Im am trying";
		ecs.addEntity(cube);
		ecs.addEntity(cube2);
		//Fbo
		
		FrameBuffer fbo = new FrameBuffer();
		
		// GIZMO
		float[] color = {1, 0, 0};
		
		
		//System.out.println("Gizmo :" + testGizmo.getID() + " CUBE: " + cube.getID());
		//Main Loop
		Boolean toggle = false;
		int frame_lag = 15;
		int current_frame = 0;
		camera.Disable();
		
		
		//ImFont font1 = ImGui.getIO().getFonts().addFontFromFileTTF("res/Fonts/Roboto-Medium.ttf", 32);
		
		ecs.initStyle();
		while (!DisplayManager.shouldClose()) {
			entities = ecs.getAllEntities();
			
			camera.move();
			//mouser.update();
			if (keyboard.getKey("tab")) {
				if (current_frame >= frame_lag) {
					statemanager.setMainState("EDIT_EDITORCAMERA");
					camera.toggle();
					current_frame = 0;
				} else {
					current_frame++;
				}
				
			}
			
			// renderr here
			renderer.renderShadowMap(entities, sun);
			
			// Now mouse picking
			fbo.bindReflectionFrameBuffer();
			DisplayManager.clearScreen();
			for(Entity entity:entities){
				entity.isPicking = true;
				renderer.processEntity(entity);
			}
			renderer.renderSkyBox = false;
			renderer.render(lights, camera);
			
			fbo.unbindCurrentFrameBuffer();
			
			fbo.bindRefractionFrameBuffer();
			
			
			for(Entity entity:entities){
				entity.isPicking = false;
				renderer.processEntity(entity);
			}
			renderer.renderSkyBox = true;
			renderer.render(lights, camera);
			//GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			fbo.unbindCurrentFrameBuffer();
			
			// Imgui here
			DisplayManager.clearScreen();
			
			DisplayManager.imGuiGlfw.newFrame();
			ImGui.newFrame();
			ImGuizmo.beginFrame();
			if (mouse.isLeftClick && ImGuizmo.isUsing() == false) {
				int x = (int) ((int) mouse.getMousePosition().x);
				//x = (int) DisplayManager.getWidth() - ((x / DisplayManager.getWidth()) * DisplayManager.getWidth()) ;
				int y = (int) ((int) mouse.getMousePosition().y);
				//y = (int) DisplayManager.getHeight() - ((x / DisplayManager.getHeight()) * DisplayManager.getHeight()) ;
				float id = fbo.readPixel(x, y) * 10;
 				System.out.println(id);
				if (x > Utils.getPrecentageOf(25, DisplayManager.getWidth()) && x < Utils.getPrecentageOf(80, DisplayManager.getWidth())) {
					if (y < Utils.getPrecentageOf(60, DisplayManager.getHeight())) {
						if (ecs.getEntity(id) != null) {
							ecs.setSelectedEntity(ecs.getEntity(id));
						} else {
							//System.out.println("EMPTY");
						}
					}
				}
				
			}
			
			
			// IMGUIZMOOO ( i dont want to touch this shitty code ever AGAIN)
			
			if (true) {
				//Do input
				ImGui.setNextWindowPos(Utils.getPrecentageOf(25, DisplayManager.getWidth()), 0);
				ImGui.setNextWindowSize(Utils.getPrecentageOf(55,DisplayManager.getWidth()), Utils.getPrecentageOf(60,DisplayManager.getHeight()));
				
				ImGui.begin("ViewPort", imgui.flag.ImGuiWindowFlags.NoMove | imgui.flag.ImGuiWindowFlags.NoResize | imgui.flag.ImGuiWindowFlags.NoCollapse);
				ImGui.getWindowDrawList().addImage(fbo.getRefractionTexture(), 0, 0, DisplayManager.getWidth(), DisplayManager.getHeight());
				
				if (ecs.getSelected_entity() != null) {
					Matrix4f cv = Maths.createViewMatrix(camera);	

					
					// INPUT
					
					if (keyboard.getKey("t")) {
						opperation = imgui.extension.imguizmo.flag.Operation.TRANSLATE;
					}
 					
					if (keyboard.getKey("r")) {
						opperation = imgui.extension.imguizmo.flag.Operation.ROTATE;
					}
					
					if (keyboard.getKey("y")) {
						opperation = imgui.extension.imguizmo.flag.Operation.SCALE;
					}
					

					float[] cameraView = {
							cv.m00, cv.m01, cv.m02, cv.m03,
							cv.m10, cv.m11, cv.m12, cv.m13,
							cv.m20, cv.m21, cv.m22, cv.m23,
							cv.m30, cv.m31, cv.m32, cv.m33,
							
					};
					
					
					Matrix4f cp = createProjectionMatrix(renderer, DisplayManager.getWidth(), DisplayManager.getHeight());
					float[] cameraProjection = { 
							cp.m00, cp.m01, cp.m02, cp.m03,
							cp.m10, cp.m11, cp.m12, cp.m13,
							cp.m20, cp.m21, cp.m22, cp.m23,
							cp.m30, cp.m31, cp.m32, cp.m33,
					};
					Entity entity = ecs.getSelected_entity();
					Matrix4f ep = Maths.createTransformationMatrix(entity.getPosition(), ecs.getSelected_entity().getRotX(),  ecs.getSelected_entity().getRotY(),  ecs.getSelected_entity().getRotZ(),  ecs.getSelected_entity().getScale());
					float[] entityTransform = {
							ep.m00, ep.m01, ep.m02, ep.m03,
							ep.m10, ep.m11, ep.m12, ep.m13,
							ep.m20, ep.m21, ep.m22, ep.m23,
							ep.m30, ep.m31, ep.m32, ep.m33,
					};

							
					
					// CREATE VIEPWORTT
					
					
					// ViewPort Here
					float[] entitypos = {
						entity.getPosition().x, entity.getPosition().y, entity.getPosition().z	
					};
					
					float[] entityrot = {
							entity.getRotX(), entity.getRotY(), entity.getRotZ()	
					};
					
					float[] entityscale = {
							0, 0, 0//entity.getScale().x , entity.getScale().y, entity.getScale().z
					};
					
					
					
					
					float[] camerapos = {
						camera.getPosition().x, camera.getPosition().y, camera.getPosition().z	
					};
					
					float[] camerarot = {
						camera.getYaw(), camera.getPitch(), camera.getRoll()	
					};
					
					float[] camerascale = {
							1.0f, 1.f, 1.f
					};

					//ImGuizmo.recomposeMatrixFromComponents(entityTransform, entitypos, entityrot, entityscale);
					//ImGuizmo.recomposeMatrixFromComponents(cameraView, camerapos, camerarot, camerascale);
					ImGui.getWindowDrawList().addImage(fbo.getRefractionTexture(), 0, 0, DisplayManager.getWidth(), DisplayManager.getHeight());
					

					float[] IDENTITY_MATRIX = {
							1.f, 0.f, 0.f, 0.f,
					        0.f, 1.f, 0.f, 0.f,
					        0.f, 0.f, 1.f, 0.f,
					        0.f, 0.f, 0.f, 1.f
					};	
						

			        ImGuizmo.setOrthographic(false);
			        ImGuizmo.setEnabled(true);
			        ImGuizmo.setDrawList();

			        float windowWidth = ImGui.getWindowWidth();
			        float windowHeight = ImGui.getWindowHeight();
			        ImGuizmo.setRect(ImGui.getWindowPosX(), ImGui.getWindowPosY(), windowWidth, windowHeight);

			        //ImGuizmo.drawGrid(cameraView, cameraProjection, IDENTITY_MATRIX, 100);
			        ImGuizmo.setId(0);
			        //ImGuizmo.drawCubes(cameraView, cameraProjection, entityTransform);
				
			        ImGuizmo.setEnabled(true);
					ImGuizmo.manipulate(cameraView, cameraProjection, entityTransform, opperation, imgui.extension.imguizmo.flag.Mode.LOCAL);
					float viewManipulateRight = ImGui.getWindowPosX() + windowWidth;
			        float viewManipulateTop = ImGui.getWindowPosY();
			        //ImGuizmo.viewManipulate(cameraView, 8, new float[]{viewManipulateRight - 128, viewManipulateTop}, VIEW_MANIPULATE_SIZE, 0x10101010);
					//System.out.println(ep.m00);
			        
			        ImGuizmo.decomposeMatrixToComponents(entityTransform, entitypos, entityrot, entityscale);
			        float[] entityRotation = {
			        	entity.getRotX(), entity.getRotY(), entity.getRotZ()	
			        };
			        //float[] Deltarotation = {
			        //		entityrot[0] - entityRotation[0],
			        //		entityrot[1] - entityRotation[1],
			        //		entityrot[2] - entityRotation[2],
			        //};
			        
			        //float[] newrot = {
			        //		entityrot[0] + Deltarotation[0],
			        //		entityrot[1] + Deltarotation[1],
			        //		entityrot[2] + Deltarotation[2]
			        //};
			       

                    //entityrot = newrot;

			        
			        //ImGuizmo.decomposeMatrixToComponents(cameraView, camerapos, camerarot, camerascale);
			        
			        //camera.setPosition(new Vector3f(camerapos[0], camerapos[1], camerapos[2]));
			       	//camera.setYaw(camerarot[0]);
			       	//camera.setPitch(camerarot[1]);
			       	//camera.setRoll(camerarot[2]);
			        
			        
			        entity.setPosition(new Vector3f(entitypos[0] , entitypos[1] , entitypos[2]));
			        entity.setRotX(entityrot[0]);
			        entity.setRotY(entityrot[1]);
			        entity.setRotZ(entityrot[2]);
			        entity.setScale(new Vector3f(entityscale[0], entityscale[1], entityscale[2]));
			        ImGui.end();
				} else {
					ImGui.end();
				}
				
			}
			
			///// LAYOUT
			//ImGui.pushFont(font1);
			//Entities Tab
			ImGui.setNextWindowPos(0, 0);
			ImGui.setNextWindowSize(Utils.getPrecentageOf(25,DisplayManager.getWidth()), Utils.getPrecentageOf(60,DisplayManager.getHeight()));
			ImGui.begin("Game Objects", imgui.flag.ImGuiWindowFlags.NoMove | imgui.flag.ImGuiWindowFlags.NoResize | imgui.flag.ImGuiWindowFlags.NoCollapse);
			
			// Entities Here
			
			// Entities Creation
			ImGui.text("Scene  ");
			ImGui.sameLine();
			if (ImGui.button("+")) {
				// Some Code
			}
			
			// Entities List!
			ImGui.separator();
			entities = ecs.getAllEntities();
			for (Entity entity:entities) {
				if (ImGui.treeNode(entity.name)) {
					ecs.setSelectedEntity(entity);
					if (ImGui.treeNode(entity.getModel().name)) {
						ImGui.treePop();
					}
					ImGui.treePop();
				}
			}
			lights = ecs.getAllLights();
			
			for (Light entity:lights) {
				if (ImGui.treeNode(entity.name)) {
					ecs.setSelectedLight(entity);
					
					ImGui.treePop();
				}
			}
			ImGui.end();
			
			
			//ViewPort Tab
			
			
			
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
		
		//DisplayManager.closeDisplay();
		System.exit(0);
	}
	
	
	private static Matrix4f createProjectionMatrix(MasterRenderer renderer, float width,  float height){
		Matrix4f projectionMatrix = new Matrix4f();
		projectionMatrix.setIdentity();
		float aspectRatio = (float) DisplayManager.getWidth() / (float) DisplayManager.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(renderer.FOV / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = renderer.FAR_PLANE - renderer.NEAR_PLANE;

		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((renderer.FAR_PLANE + renderer.NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * renderer.NEAR_PLANE * renderer.FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
		return projectionMatrix;
    }

	
}
