package shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Light;

public class StaticShader extends ShaderProgram{
	
	private static final int MAX_LIGHTS = 6;
	
	private static final String VERTEX_FILE = "Shaders/VertexShader.txt";
	private static final String FRAGMENT_FILE = "Shaders/FragmentShader.txt";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition[];
	private int location_lightColour[];
	private int location_attenuation[];
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_useFakelighting;
	private int location_numbersOfRows;
	private int location_offset;
	private int location_ID;
	private int location_usePicking;
	private int location_max_lights = MAX_LIGHTS;
	private int location_selected;
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "texCoords");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		//location_max_lights = super.getUniformLocation("max_lights");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_useFakelighting = super.getUniformLocation("useFakeLighting");
		location_ID = super.getUniformLocation("ID");
		location_usePicking = super.getUniformLocation("usePicking");
		location_selected = super.getUniformLocation("isSelected");
//		location_numbersOfRows = super.getUniformLocation("numbersOfRows");
//		location_offset = super.getUniformLocation("offset");
		
		location_lightPosition = new int[MAX_LIGHTS];
		location_lightColour = new int[MAX_LIGHTS];
		location_attenuation = new int[MAX_LIGHTS];
		for (int i=0;i<MAX_LIGHTS;i++) {
			location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
			location_lightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
			location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
		
	}
	
	public void loadNumberOfRows(int numberOfRows) {
		super.loadFloat(location_numbersOfRows, numberOfRows);
	}
	
	public void loadOffset(float x, float y) {
		super.load2DVector(location_offset, new Vector2f(x, y));
	}
	
	public void loadFakeLightingVariable(boolean  useFake) {
		super.loadBoolean(location_useFakelighting, useFake);
		
	}
	
	public void loadSelectionVariable(boolean isSelected) {
		super.loadBoolean(location_selected, isSelected);
	}
	
	public void loadShineVariables(float damper,float reflectivity) {
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
	
	public void loadLights(List<Light> lights) {
		for (int i=0;i<MAX_LIGHTS;i++) {
			if (i<lights.size()) {
				super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
				super.loadVector(location_lightColour[i], lights.get(i).getColour());
				super.loadVector(location_attenuation[i], lights.get(i).getAttenuation());
			} else {
				super.loadVector(location_lightPosition[i], new Vector3f(0, 0, 0));
				super.loadVector(location_lightColour[i], new Vector3f(0, 0, 0));
				super.loadVector(location_attenuation[i], new Vector3f(1, 0, 0));
			}
		}
	}
	
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadViewMatrix(Matrix4f camera) {
		Matrix4f viewMatrix = camera;
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadPicking(float ID, Boolean usePicking) {
		super.loadFloat(location_ID, ID);
		super.loadBoolean(location_usePicking, usePicking);
	}

	public int getLocation_max_lights() {
		return location_max_lights;
	}

	public void setLocation_max_lights(int location_max_lights) {
		this.location_max_lights = location_max_lights;
	}
	
	

}