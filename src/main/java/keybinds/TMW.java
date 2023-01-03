package keybinds;

import input.KeyBoard;
import renderEngine.DisplayManager;
import toolbox.Utils;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;


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
				if (Objects.equals(action.getEntity().type, "Entity")) {
					action.getEntity().Undo();
				}
			}
		}
		
		if (keyboard.getKey("lcontrol") && keyboard.getKey("z") && keyboard.getKey("lshift")) {
			for (Action action: actions) {
				action.getEntity().Redo();
			}
		}
		
		if (keyboard.getKey("lcontrol") && keyboard.getKey("c") && (ecs.mouse.getMousePosition().x > Utils.getPrecentageOf(25, DisplayManager.getWidth()) && ecs.mouse.getMousePosition().x < Utils.getPrecentageOf(80, DisplayManager.getWidth()) && ecs.mouse.getMousePosition().y < Utils.getPrecentageOf(60, DisplayManager.getHeight()))) {
			ecs.Copy();
		}
		
		if (keyboard.getKey("lcontrol") && keyboard.getKey("v") && (ecs.mouse.getMousePosition().x > Utils.getPrecentageOf(25, DisplayManager.getWidth()) && ecs.mouse.getMousePosition().x < Utils.getPrecentageOf(80, DisplayManager.getWidth()) && ecs.mouse.getMousePosition().y < Utils.getPrecentageOf(60, DisplayManager.getHeight()))) {
			ecs.Paste();
		} else {
			ecs.isPasted = false;
		}
		
	}
	
	public void addAction(Action action) {
		actions.add(action);
	
	}
}
