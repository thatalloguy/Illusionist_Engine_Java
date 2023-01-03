package skybox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import renderEngine.DisplayManager;
import shaders.ShaderProgram;
import toolbox.Maths;

public class SkyboxShader extends ShaderProgram{

	private static final String VERTEX_FILE = "Shaders/skyboxVertexShader.txt";
	private static final String FRAGMENT_FILE = "Shaders/skyboxFragmentShader.txt";
	
	private static final float ROTATION_SPEED = 1f;
	public Boolean doSpin = true;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_cubeMap;
	private int location_cubeMap2;
	private int location_blendFactor;
	private int location_picking;
	
	private float rotation = 0;
	
	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix(location_projectionMatrix, matrix);
	}

	public void loadViewMatrix(Camera camera){
		Matrix4f matrix = Maths.createViewMatrix(camera);
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		if (!this.doSpin) {
			rotation = 0;
		} else {
			rotation += ROTATION_SPEED * DisplayManager.getFrameTimeSeconds();
		}
		Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0,1,0), matrix, matrix);
		super.loadMatrix(location_viewMatrix, matrix);
	}
	
	public void connectTextureUnits() {
		super.loadInt(location_cubeMap, 0);
		super.loadInt(location_cubeMap2, 1);
	}
	
	public void loadBlendFactor(float blend) {
		super.loadFloat(location_blendFactor, blend);
	}
	
	public void loadPicking(boolean bool) {
		super.loadBoolean(location_picking, bool);
	}
	
	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_cubeMap = super.getUniformLocation("cubeMap");
		location_cubeMap2 = super.getUniformLocation("cubeMap2");
		location_blendFactor = super.getUniformLocation("blendFactor");
		location_picking = super.getUniformLocation("isPicking");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
