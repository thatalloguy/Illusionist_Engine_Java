package input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.util.vector.Vector2f;

import renderEngine.DisplayManager;

public class Mouse {

	
	public Boolean isLeftClick = false;
	public Boolean isRightClick = false;
	public Boolean isScrollClick = false;
	
	private float relx = 0;
	private float rely = 0;
	
	public Vector2f oldMousePos = new Vector2f(0, 0);
	public Vector2f MousePos = new Vector2f(0, 0);
	public Vector2f MouseScroll = new Vector2f(0, 0);
	
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
		
		GLFW.glfwSetScrollCallback(DisplayManager.getRawWindow(), new GLFWScrollCallback() {
			
			@Override
			public void invoke(long arg0, double arg1, double arg2) {
				MouseScroll.x = (float) arg1;
				MouseScroll.y = (float) arg2;
				
			}
		});
		
		GLFW.glfwSetCursorPosCallback(DisplayManager.getRawWindow(),new GLFWCursorPosCallback() {

		    @Override
		    public void invoke(long window, double xpos, double ypos) {
		    	
		    	
		        MousePos.x = (float) xpos;// - MousePos.x;
		        MousePos.y = (float) ypos;// - MousePos.y;
		        
		        relx = oldMousePos.x - MousePos.x;
		        rely = MousePos.y -  oldMousePos.y;
		        
		        oldMousePos.x = (float) xpos;
		        oldMousePos.y = (float) ypos;
		    }
		});

	}
	
	public Vector2f getRelMouse() {
		return new Vector2f(relx, rely);
	}
	
	public void resetRel() {
		relx = 0;
		rely = 0;
	}
	
	public String getHorDirection() {
		System.out.println(MousePos.x);
		if (MousePos.x > oldMousePos.x) {
			return "right";
		} if (MousePos.x < oldMousePos.x) {
			return "left";
		} else {
			return "none";
		}
	}
	
	public String getVerDirection() {
		if (MousePos.y > oldMousePos.y) {
			return "up";
		} if (MousePos.y < oldMousePos.y) {
			return "down";
		} else {
			return "none";
		}
	}
	
	public Vector2f getMousePosition() {
		Vector2f mos = new Vector2f(0, 0);
		mos.x += MousePos.x;
		mos.y += MousePos.y;
		return mos;
	}

	public Vector2f getMouseScroll() {
		return MouseScroll;
	}
	
	
	
}
