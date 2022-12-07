package keybinds;

import input.KeyBoard;

import java.util.List;
import java.util.ArrayList;


import entities.ECS;

public class TMW {

	private ECS ecs;
	private List<Action> actions  = new ArrayList<Action>();
	
	private KeyBoard keyboard;
	
	public TMW(ECS ecs, KeyBoard keyboard) {
		this.keyboard = keyboard;
		this.ecs = ecs;
	}
	
	public void clearActions() {
		actions.clear();
	}
	
	public void Update() {
		if (keyboard.getKey("lcontrol") && keyboard.getKey("z")) {
			
			for (Action action: actions) {
				if (action.getEntity().type == "Entity") {
					action.getEntity().Undo();
				}
			}
		}
		
		if (keyboard.getKey("lcontrol") && keyboard.getKey("z") && keyboard.getKey("lshift")) {
			for (Action action: actions) {
				action.getEntity().Redo();
			}
		}
		
		if (keyboard.getKey("lcontrol") && keyboard.getKey("c")) {
			ecs.Copy();
		}
		
		if (keyboard.getKey("lcontrol") && keyboard.getKey("v")) {
			ecs.Paste();
		} else {
			ecs.isPasted = false;
		}
		
	}
	
	public void addAction(Action action) {
		actions.add(action);
	
	}
}
