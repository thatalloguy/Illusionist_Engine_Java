package physics;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import entities.Player;
import renderEngine.DisplayManager;

public class Builder {

	private Player player;
	private Entity CurrentHoldingEntity;
	private float itemOffset = 3;
	
	public Builder(Player player, Entity currentHoldingEntity) {
		this.player = player;
		this.CurrentHoldingEntity = currentHoldingEntity;
	}
	
	public void Update() {
		// this shit doesnt work try making a system where you can rotate it around the player by using "R"
		
		float distanceX = player.getCurrentXSpeed() * DisplayManager.getFrameTimeSeconds();
		float distanceZ = player.getCurrentZSpeed() * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distanceX * Math.sin(player.getPosition().x + itemOffset));
		float dz = (float) (distanceZ * Math.cos(player.getPosition().z + itemOffset));
		CurrentHoldingEntity.isSimulated = false;
		CurrentHoldingEntity.setPosition(new Vector3f(dx, player.getPosition().y, dz));
	}
	
	private void setCurrentEntity(Entity entity) {
		this.CurrentHoldingEntity = entity;
	}
	
	private void clearInventory() {
		
	}
}
