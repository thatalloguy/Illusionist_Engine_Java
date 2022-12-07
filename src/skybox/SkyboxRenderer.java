package skybox;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import renderEngine.DisplayManager;
import models.RawModel;
import renderEngine.Loader;

public class SkyboxRenderer {

	private static final float SIZE = 500f;
	
	private static final float[] VERTICES = {        
	    -SIZE,  SIZE, -SIZE,
	    -SIZE, -SIZE, -SIZE,
	    SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	    -SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE
	};

	private static String[] TEXTURE_FILES = {"right", "left", "top", "bottom", "front", "back"};
	private static String[] NIGHT_TEXTURE_FILES = {"nightRight", "nightLeft", "nightTop", "nightBottom", "nightBack", "nightFront"};
	
	private Boolean isDay = true;
	
	public Boolean doCycle = false;
	public Boolean doSpining = false;
	private RawModel cube;
	public Boolean isPicking = false;
	private int texture;
	private int nightTexture;
	private SkyboxShader shader;
	private float time = 0;
	
	public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix) {
		cube = loader.loadToVAO(VERTICES, 3);
		texture = loader.loadCubeMap(TEXTURE_FILES);
		nightTexture = loader.loadCubeMap(NIGHT_TEXTURE_FILES);
		shader = new SkyboxShader();
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(Camera camera) {
		shader.doSpin = this.doSpining;
		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadPicking(this.isPicking);
		GL30.glBindVertexArray(cube.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		bindTextures();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	private void bindTextures(){
		if (doCycle) {
			time += DisplayManager.getFrameTimeSeconds() * 1000;
			time %= 24000;
			int texture1;
			int texture2;
			float blendFactor;		
			if(time >= 0 && time < 5000){
				texture1 = nightTexture;
				texture2 = nightTexture;
				isDay = false;
				blendFactor = (time - 0)/(5000 - 0);
			}else if(time >= 5000 && time < 8000){
				texture1 = nightTexture;
				texture2 = texture;
				isDay = true;
				blendFactor = (time - 5000)/(8000 - 5000);
			}else if(time >= 8000 && time < 21000){
				texture1 = texture;
				texture2 = texture;
				isDay = true;
				blendFactor = (time - 8000)/(21000 - 8000);
			}else{
				isDay = false;
				texture1 = texture;
				texture2 = nightTexture;
				blendFactor = (time - 21000)/(24000 - 21000);
			}
			
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
			shader.loadBlendFactor(blendFactor);
		} else {
			int texture1 = texture;
			int texture2 = texture;
			
			int blendFactor = 0;
			
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
			shader.loadBlendFactor(blendFactor);
		}
		
	}
	
	public boolean getIsDay() {
		return isDay;
	}
}
