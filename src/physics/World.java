package physics;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import renderEngine.DisplayManager;
import terrain.Terrain;

public class World {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<BoxCollider> boxes = new ArrayList();
	private Vector3f gravity = new Vector3f(0, -5.1f, 0);
	private Terrain terrain;
	
	//Different Types
	
	public int DYNAMIC = 0;
	public int STATIC  = 1;
	
	public World(Terrain terrain) {
		this.terrain = terrain;
	}

	public void addBoxCollider(BoxCollider box) {
		boxes.add(box);
	}
	
	public void removeBoxCollider(BoxCollider box) {
		boxes.remove(box);
	}
	
	public boolean checkForCollision(BoxCollider a, BoxCollider b) {
		return (a.Min.x <= b.Max.x &&
			    a.Max.x >= b.Min.x &&
			    a.Min.y <= b.Max.y &&
			    a.Max.y >= b.Min.y &&
			    a.Min.z <= b.Max.z &&
			    a.Max.z >= b.Min.z
			  );
	}
	
	
	private void doPhysicsStuff(BoxCollider box1, BoxCollider box2) {
		//First stop all movement
		
		Vector3f box1V = box1.entity.Velocity;
		Vector3f box2V = box2.entity.Velocity;
		
		box1.entity.Velocity.x = 0;
		box1.entity.Velocity.z = 0;
		box1.entity.Velocity.y = 0;
		box2.entity.Velocity.x = 0;
		box2.entity.Velocity.y = 0;
		box2.entity.Velocity.z = 0;

		//box1.entity.Force.y = 0;
		//box2.entity.Force.y = 0;

		
		
		//calculate if both are dynamic
		
		if(box1.getType() == 0 & box2.getType() == 0) {
			float newMass = box1.entity.Mass - box2.entity.Mass;
			box1.entity.Force.x -= box2.entity.Force.x;
			box1.entity.Force.z -=  box2.entity.Force.z;
			box1.entity.Force.y -=  box2.entity.Force.y;
			box2.entity.Force.x -=  box1.entity.Force.x;
			box2.entity.Force.y -=  box1.entity.Force.y;
			box2.entity.Force.z -=  box1.entity.Force.z;
			System.out.println(newMass);
		}
		
		//box1.entity.isSimulated = false;
		//box2.entity.isSimulated = false;
	}
	
	public void Step() {
		BoxCollider pBox = null;
		int i = 0;
		for(BoxCollider box: boxes) {
			box.update();
			if (i != 0) {
				if (box != pBox) {
					if(checkForCollision(box, pBox)) {
						//System.out.println(true);
						doPhysicsStuff(box, pBox);
					} else {
						box.entity.isSimulated = true;
					}
				}
			}
			pBox = box;
			
			Entity entity = box.entity;
			
			if(entity.isSimulated) {
				entity.Force.y += entity.Mass * gravity.y;
				entity.Force.x += entity.Mass * gravity.x;
				entity.Force.z += entity.Mass * gravity.z;
				double delta = DisplayManager.getFrameTimeSeconds();
				entity.Velocity.x += entity.Force.x * entity.Mass * DisplayManager.getFrameTimeSeconds();
				entity.Velocity.y += entity.Force.y * entity.Mass * DisplayManager.getFrameTimeSeconds();
				entity.Velocity.z += entity.Force.z * entity.Mass * DisplayManager.getFrameTimeSeconds();
				
				
				entity.Force = new Vector3f(0.0f, 0.0f, 0.0f);
				
				float terrainHeight = terrain.getHeightOfTerrain(entity.getPosition().x, entity.getPosition().z);
				System.out.println(entity.Velocity.y);
				if (entity.getPosition().y < terrainHeight) {
					entity.Velocity.y = 0;
						
				}
				entity.increasePosition(entity.Velocity.x * (float) delta, entity.Velocity.y * (float) delta, entity.Velocity.z * (float) delta);
			}
			i++;
			
		}
		
	}
}
