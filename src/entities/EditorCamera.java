package entities;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import input.KeyBoard;
import input.Mouse;
import renderEngine.DisplayManager;

public class EditorCamera extends Camera {

	private float Sensitivity = 0.4f;
	private Mouse mouse;
	private Boolean enabled = false;
	private KeyBoard keyboard;
	private float old_x = 0;
	private float old_y = 0;
	private float SPEED = 0.5f;
	
	public EditorCamera(Mouse mouse, KeyBoard keyboard) {
		super(mouse, keyboard);
		this.keyboard = keyboard;
		this.mouse = mouse;
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
		GLFW.glfwSetInputMode(DisplayManager.getRawWindow(), GLFW.GLFW_CURSOR ,GLFW.GLFW_CURSOR_DISABLED);
	}
	
	public void Disable() {
		this.enabled = false;
		super.Editing = false;
		GLFW.glfwSetInputMode(DisplayManager.getRawWindow(), GLFW.GLFW_CURSOR ,GLFW.GLFW_CURSOR_NORMAL);
	}
	
	
	

}
