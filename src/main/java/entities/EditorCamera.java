package entities;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector3f;

import com.hackoeur.jglm.Vec3;

import input.KeyBoard;
import input.Mouse;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import textures.ModelTexture;
import toolbox.Maths;

public class EditorCamera extends Camera {

	// SomeMovement thingss
	private Vec3 up = new Vec3(0, 1, 0);
	private Vec3 right = new Vec3(1, 0, 0);
	private Vec3 forward = new Vec3(0, 0, -1);
	
	private float sensititvity = 0.2f;
	@SuppressWarnings("unused")
	private float velocity = 0;
	private float speed = 0.055f;

	private Boolean enabled = false;

	private Mouse mouse2;
	private KeyBoard keyboard2;
	private Entity visualEntity;
	private ECS ecs;
	
	public EditorCamera(Mouse mouse, KeyBoard keyboard, ECS ecs) {
		super(mouse, keyboard, ecs);
		this.mouse2 = mouse;
		this.keyboard2 = keyboard;
		Loader loader = new Loader();
		ModelData newData = OBJFileLoader.loadOBJ("res/camera.obj");
		RawModel newModel = loader.loadToVAO(newData.getVertices(), newData.getTextureCoords(), newData.getNormals(), newData.getIndices());
		TexturedModel newtexmod = new TexturedModel(newModel,new ModelTexture(loader.loadTexture("res/camera.png")));
		this.visualEntity = new Entity(newtexmod, super.getPosition(), 0, 0, 0, new Vector3f(2, 2, 0), 0);
		this.visualEntity.uselighting = true;
		this.visualEntity.name = "Camera";
		this.visualEntity.ModelPath = "res/camera.obj";
		this.visualEntity.TexturePath = "res/camera.png";
		this.ecs = ecs;
		this.ecs.addEntity(visualEntity);

		ecs.cameraEntity = visualEntity;
	}

	@Override
	public void move() {
		if (super.Editing) {
			moveCamera();
			rotate();
			updateVectors();

			super.customViewMatrix = Maths.createViewMatrix(this);
		}
	}

	
	
	public void moveCamera() {
		this.velocity = (float) (this.speed * DisplayManager.getFrameTimeSeconds());
		
		
		if (keyboard2.getKey("w")) {
			Vector3f pos = super.getPosition();
			Vector3f f = new Vector3f(this.right.getX(), this.right.getY(), this.right.getZ());
			super.setPosition(new Vector3f(pos.x - f.x, pos.y - f.y, pos.z - f.z));
		}
		
		if (keyboard2.getKey("s")) {
			Vector3f pos = super.getPosition();
			Vector3f f = new Vector3f(this.right.getX(), this.right.getY(), this.right.getZ());
			super.setPosition(new Vector3f(pos.x + f.x, pos.y + f.y, pos.z + f.z));
		}
		
		if (keyboard2.getKey("a")) {
			Vector3f pos = super.getPosition();
			Vector3f f = new Vector3f(this.forward.getX(), this.forward.getY(), this.forward.getZ());
			super.setPosition(new Vector3f(pos.x - f.x, pos.y - f.y, pos.z - f.z));
		}
		
		if (keyboard2.getKey("d")) {
			Vector3f pos = super.getPosition();
			Vector3f f = new Vector3f(this.forward.getX(), this.forward.getY(), this.forward.getZ());
			super.setPosition(new Vector3f(pos.x + f.x, pos.y + f.y, pos.z + f.z));
		}
		
		if (keyboard2.getKey("lshift")) {
			Vector3f pos = super.getPosition();
			Vector3f f = new Vector3f(this.up.getX(), this.up.getY(), this.up.getZ());
			super.setPosition(new Vector3f(pos.x - f.x, pos.y - f.y, pos.z - f.z));
		}
		
		if (keyboard2.getKey("space")) {
			Vector3f pos = super.getPosition();
			Vector3f f = new Vector3f(this.up.getX(), this.up.getY(), this.up.getZ());
			super.setPosition(new Vector3f(pos.x + f.x, pos.y + f.y, pos.z + f.z));
		}
		
		
	}

	public void rotate() {
		float rel_x = super.mouse.getRelMouse().x;
		float rel_y =  super.mouse.getRelMouse().y;
		mouse.resetRel();

		if (rel_x != 0) {
			super.yaw -= rel_x * sensititvity;
		}
		if (rel_y != 0) {
			super.pitch = super.pitch - (rel_y * sensititvity);
		}

		// this.super.pitch = Math.max(-89, Math.min(89, super.pitch));
		
	}

	public void updateVectors() {
		float ryaw = (float) Math.toRadians(super.yaw);
		float rpitch = (float) Math.toRadians(super.pitch);

		this.forward = new Vec3((float) (Math.cos(ryaw) * Math.cos(rpitch)), (float) Math.sin(rpitch),
				(float) ((float) Math.sin(ryaw) * Math.cos(rpitch)));
		this.right = forward.cross(new Vec3(0, 1, 0));
		
		this.up = right.cross(forward);

	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void toggle() {
		if (this.enabled) {
			Disable();
		} else {
			Enable();
		}
	}

	public void Enable() {
		super.Editing = true;
		this.enabled = true;
		GLFW.glfwSetInputMode(DisplayManager.getRawWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
	}
	

	public void Disable() {
		this.enabled = false;
		super.Editing = false;
		GLFW.glfwSetInputMode(DisplayManager.getRawWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
	}

}
