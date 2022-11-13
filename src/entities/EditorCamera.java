package entities;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import input.Mouse;
import renderEngine.DisplayManager;

public class EditorCamera extends Camera {

	private float Sensitivity = 0.4f;
	private Mouse mouse;
	private Boolean enabled = false;
	
	private float old_x = 0;
	private float old_y = 0;
	
	public EditorCamera(Mouse mouse) {
		this.mouse = mouse;
	}
	
	@Override
	public void rotate() {
		if (enabled) {
			float relx = (float) (mouse.getMousePosition().x * (DisplayManager.getFrameTimeSeconds() * 1000));
			float rely = (float) (mouse.getMousePosition().y * (DisplayManager.getFrameTimeSeconds() * 1000));
			relx -= old_x;
			rely -= old_y;
			
			this.old_x = relx;
			this.old_y = rely;
			System.out.println(relx);
			
			this.yaw =+ relx * Sensitivity;
			this.pitch -= rely * Sensitivity;
			this.pitch = Math.max(-90, Math.min(90, pitch));
		}
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void Enable() {
		this.enabled = true;
		GLFW.glfwSetInputMode(DisplayManager.getRawWindow(), GLFW.GLFW_CURSOR ,GLFW.GLFW_CURSOR_DISABLED);
	}
	
	public void Disable() {
		this.enabled = false;
		GLFW.glfwSetInputMode(DisplayManager.getRawWindow(), GLFW.GLFW_CURSOR ,GLFW.GLFW_CURSOR_NORMAL);
	}
	
	

}
