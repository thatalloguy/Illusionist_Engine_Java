package audio;

import java.io.File;
import javax.sound.sampled.*;

import org.lwjgl.util.vector.Vector3f;

import Editor.ProjectManager;
import toolbox.Utils;

public class Sound {

	private Clip clip;
	@SuppressWarnings("unused")
	private String file;
	private FloatControl gainControl;
	
	public Sound(String file, ProjectManager pm){
		AudioInputStream aIS;
		try {
			aIS = AudioSystem.getAudioInputStream(new File("Projects/" + pm.currentProject + "/Resources/" + file));
			System.out.println("Projects/" + pm.currentProject + "/Resources/" + file);
			try {
				this.clip = AudioSystem.getClip();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.file = file;
			try {
				clip.open(aIS);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		this.gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		
	}
	
	public void play(float volumeReduce) {
		clip.setFramePosition(0);
		if (volumeReduce > 6.02f) {
			volumeReduce = 6.02f;
		}
		this.gainControl.setValue(volumeReduce);
		clip.start();
	}
	
	public void stop() {
		this.clip.stop();
	}
	
	public void playAtPosition(Vector3f speaker, Vector3f lisener) {
		float reduceAmount = Utils.getDistance(speaker, lisener);
		System.out.println("DEBUG LISTEN AMOUNT: " + reduceAmount);
		this.gainControl.setValue(-reduceAmount * 2);
		clip.start();
	}
}
