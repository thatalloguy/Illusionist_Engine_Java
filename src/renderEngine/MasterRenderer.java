package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader;
import shaders.TerrainShader;
import shadows.ShadowMapMasterRenderer;
import skybox.SkyboxRenderer;
import terrain.Terrain;

public class MasterRenderer {

	private StaticShader shader = new StaticShader();
	private StaticShader gizmoShader = new StaticShader();
	private Matrix4f projectionMatrix;
	
	private EntityRenderer renderer;
	private GizmoRenderer gizmoRenderer;
	

	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();
	
	public boolean renderSkyBox = true;
	public static final float FOV = 70;
	public static final float NEAR_PLANE = 0.1f;
	public static final float FAR_PLANE = 1000;
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel,List<Entity>>();
	private Map<TexturedModel, List<Entity>> gizmos = new HashMap<TexturedModel,List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	public SkyboxRenderer skyboxRenderer;
	private ShadowMapMasterRenderer shadowMapRenderer;
	
	public MasterRenderer(Loader loader, Camera cam) {
		enableCulling();
		createProjectionMatrix();
		renderer = new EntityRenderer(shader,projectionMatrix);
		gizmoRenderer = new GizmoRenderer(gizmoShader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader,projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
		this.shadowMapRenderer = new ShadowMapMasterRenderer(cam);
	}
	
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);

	}
	
	public Matrix4f getProjectionMatrix() {
		return this.projectionMatrix;
	}
	
	public void render(List<Light> lights, Camera camera) {
		prepare();
		GL11.glDepthRange(0.01, 1.0);
		shader.start();
		shader.loadLights(lights);
		shader.loadViewMatrix(camera.getViewMatrix());
		
		
		renderer.render(entities, camera);
		
		shader.stop();
		
		
		terrainShader.start();
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains, shadowMapRenderer.getToShadowMapSpaceMatrix());
		terrainShader.stop();
		
		GL11.glDepthRange(0, 0.01);
		
		gizmoShader.start();
		gizmoShader.loadLights(lights);
		gizmoShader.loadViewMatrix(camera.getViewMatrix());
		
		
		gizmoRenderer.render(gizmos);
		
		gizmoShader.stop();
		
		GL11.glDepthRange(0, 1.0);
		
		
		if (this.renderSkyBox) {
			skyboxRenderer.render(camera);
		}
		terrains.clear();
		entities.clear();
		gizmos.clear();
		
	}
	
	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	
	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		
		if(batch!=null) {
			batch.add(entity); 
		}else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
		
	}

	public void processGizmo(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = gizmos.get(entityModel);
		
		if(batch!=null) {
			batch.add(entity); 
		}else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			gizmos.put(entityModel, newBatch);
		}
		
	}
	
	public void renderShadowMap(List<Entity> entitylist, Light sun) {
		for (Entity entity : entitylist) {
			processEntity(entity);
		}
		
		shadowMapRenderer.render(entities, sun);
		entities.clear();
	}
	
	public int getShadowMapTexture() {
		return shadowMapRenderer.getShadowMap();
	}
	
	public void cleanUp() {
		shader.cleanUp();
		terrainShader.cleanUp();
		shadowMapRenderer.cleanUp();
	}
	
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT| GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0, 1, 1, 1);
		GL13.glActiveTexture(GL13.GL_TEXTURE5);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, getShadowMapTexture());
	}
	
	private void createProjectionMatrix(){
    	projectionMatrix = new Matrix4f();
		float aspectRatio = (float) DisplayManager.getWidth() / (float) DisplayManager.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
    }
	
	
}
