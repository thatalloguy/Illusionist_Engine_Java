package input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.util.vector.Vector2f;

import renderEngine.DisplayManager;

public class Mouse {

	
	public Boolean isLeftClick = false;
	public Boolean isRightClick = false;
	public Boolean isScrollClick = false;
	
	private Vector2f MousePos = new Vector2f(0, 0);
	
	public Mouse() {
		GLFW.glfwSetMouseButtonCallback(DisplayManager.getRawWindow(),new GLFWMouseButtonCallback() {

		    @Override
		    public void invoke(long window, int button, int action, int mods) {
		        if (button == 0 && action == GLFW.GLFW_PRESS) {
		        	isLeftClick = true;
		        } else if (button == 0 && action == GLFW.GLFW_RELEASE) {
		        	isLeftClick = false;
		        }
		        
		        if (button == 1 && action == GLFW.GLFW_PRESS) {
		        	isRightClick = true;
		        } else if (button == 1 && action == GLFW.GLFW_RELEASE) {
		        	isRightClick = false;
		        }
		        
		        if (button == 2 && action == GLFW.GLFW_PRESS) {
		        	isScrollClick = true;
		        } else if (button == 2 && action == GLFW.GLFW_RELEASE) {
		        	isScrollClick = false;
		        }
		    }
		});
		
		
		GLFW.glfwSetCursorPosCallback(DisplayManager.getRawWindow(),new GLFWCursorPosCallback() {

		    @Override
		    public void invoke(long window, double xpos, double ypos) {
		        MousePos.x = (float) xpos;
		        MousePos.y = (float) ypos;
		    }
		});

	}
	
	public Vector2f getMousePosition() {
		return MousePos;
	}
	

	
}
