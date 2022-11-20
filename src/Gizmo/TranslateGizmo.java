package Gizmo;

import org.lwjgl.util.vector.Vector3f;

import input.Mouse;

public class TranslateGizmo extends Gizmo{

	
	private Mouse mouse;
	
	private static int AXIS_X = 0;
	private static int AXIS_Y = 1;
	private static int AXIS_Z = 2;
	
	public TranslateGizmo(Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale, float Mass, Mouse mouse, int AXIS) {
		super(position, rotX, rotY, rotZ, scale, Mass);
		this.mouse = mouse;
	}

	@Override
	public void update() {
		if (super.isPicking) {
			System.out.println("YESS SIRR");
		}
	}
	
}
