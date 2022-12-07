package renderEngine;


import org.lwjgl.opengl.*;
import org.lwjglx.input.Mouse;

import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

//import org.lwjgl.opengl.GL11;
import org.lwjgl.glfw.*;

public class DisplayManager {

	private static final int WIDTH = 1920;
	private static final int HEIGHT = 1090;
	
	@SuppressWarnings("unused")
	private static final int FPS_CAP = 120;
	public static String Title = "Illusion";
	
	private static double lastFrameTime;
	private static double delta;
	
	private static long window;
	
	public static Mouse mouse;
    
    public static ImGuiImplGlfw imGuiGlfw;// = new ImGuiImplGlfw(); 
    public static ImGuiImplGl3 imGuiGl13;// = new ImGuiImplGl3();
    
	
	public static void createDisplay() {
		
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, 0);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		if (!GLFW.glfwInit()) {
		    throw new IllegalStateException("Unable to initialize GLFW");
		}
		long window = GLFW.glfwCreateWindow(WIDTH, HEIGHT, Title, 0, 0);
		GLFW.glfwInit();
		DisplayManager.mouse = new Mouse();
		DisplayManager.window = window;
		if(window == 0) {
		    throw new RuntimeException("Failed to create window");
		}
		
		GLFW.glfwMakeContextCurrent(window);
		GL.createCapabilities();
		
		GLFW.glfwShowWindow(window);
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		lastFrameTime = getCurrentTime();
		
		ImGui.createContext();
		//ImGui.setCurrentContext(null);
		
		DisplayManager.imGuiGlfw = new ImGuiImplGlfw(); 
	    DisplayManager.imGuiGl13 = new ImGuiImplGl3();
		
		imGuiGlfw.init(window, true);
		imGuiGl13.init(null);
		

	}
	
	public static void updateDisplay() {
		GLFW.glfwPollEvents();
		GLFW.glfwSwapBuffers(window);
		double currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currentFrameTime;
	}
	
	public static double getFrameTimeSeconds() {
		return delta;
	}
	
	public static boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(window);
	}
	
	public static void closeDisplay() {
		imGuiGl13.dispose();
		imGuiGlfw.dispose();
		ImGui.destroyContext();
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
	}
	
	private static double getCurrentTime() {
		  return GLFW.glfwGetTime() * 1000;
	}

	public static int getWidth() {
		return WIDTH;
	}

	public static int getHeight() {
		return HEIGHT;
	}

	public static long getWindow() {
		return window;
	}
	
	public static void updateImGui() {
		if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = org.lwjgl.glfw.GLFW.glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            GLFW.glfwMakeContextCurrent(backupWindowPtr);
        }
	}
	
	public static void clearScreen() {
		GL11.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}
	
	public static long getRawWindow() {
		return window;
	}
	
}
