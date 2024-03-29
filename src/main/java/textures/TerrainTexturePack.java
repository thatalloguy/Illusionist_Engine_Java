package textures;

public class TerrainTexturePack {

	private TerrainTexture backgroundTexture;
	private TerrainTexture rTexture;
	private TerrainTexture gTexture;
	private TerrainTexture bTexture;
	private TerrainTexture blendMap;
	
	
	public TerrainTexturePack(TerrainTexture backgroundTexture, TerrainTexture rTexture, TerrainTexture gTexture,
			TerrainTexture bTexture) {
		super();
		this.backgroundTexture = backgroundTexture;
		this.rTexture = rTexture;
		this.gTexture = gTexture;
		this.bTexture = bTexture;
	}


	public TerrainTexture getBackgroundTexture() {
		return backgroundTexture;
	}


	public TerrainTexture getrTexture() {
		return rTexture;
	}


	public TerrainTexture getgTexture() {
		return gTexture;
	}


	public TerrainTexture getbTexture() {
		return bTexture;
	}


	public TerrainTexture getBlendMap() {
		return blendMap;
	}
	
	
	
	
	
	
}
