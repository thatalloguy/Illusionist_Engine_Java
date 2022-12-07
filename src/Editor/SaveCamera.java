package Editor;

import org.lwjgl.util.vector.Vector3f;

public class SaveCamera {

	private Vector3f position;
	private Vector3f rotation;
	
	public SaveCamera() {}
	
	public void Convert(Vector3f position, Vector3f rotation) {
		this.position = position;
		this.rotation = rotation;
	}
	
	public Vector3f[] getData() {
		Vector3f[] list = {position, rotation};
		return list;
	}
	
}
