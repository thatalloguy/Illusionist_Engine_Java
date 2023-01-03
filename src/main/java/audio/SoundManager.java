package audio;

import java.util.HashMap;

import org.lwjgl.util.vector.Vector3f;

import Editor.ProjectManager;

public class SoundManager {
	
	private HashMap<String, Sound> sounds = new HashMap<String, Sound>();
	public ProjectManager pm;
	
	
	public SoundManager() {
	}
	
	public void loadSound(String path, String nameID) {
		System.out.println(this.pm.currentProject);
		Sound loadSound = new Sound(path, this.pm);
		
		this.sounds.put(nameID, loadSound);
	}
	
	public void playSound(String nameID, float volume) {
		this.sounds.get(nameID).play(volume);
	}
	
	public void play3DSound(Vector3f speaker, Vector3f lisner, String nameID) {
		this.sounds.get(nameID).playAtPosition(speaker, lisner);
	}
	
	
}
