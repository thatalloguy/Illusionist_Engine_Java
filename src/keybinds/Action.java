package keybinds;

import entities.Entity;

public class Action {


	
	public String type;
	
	private Entity entity;
	
	public Action(String type) {
		this.type = type;
	}
	
	public void setEntity(Entity entity) {
		this.entity = entity;

	}

	public Entity getEntity() {
		return entity;
	}
}