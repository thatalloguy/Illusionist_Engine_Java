package entities;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import input.KeyBoard;
import input.Mouse;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import textures.ModelTexture;
import toolbox.Utils;

public class Gizmo{

	private String mode = "TRANSLATION";
	private String[] keyConfig = {"t", "r", "y"};
	private ECS ecs;
	private float scale = 0.75f;
	
	private List<Entity> gizmoEntities = new ArrayList<Entity>();
	
	//Translation
	private Entity tx;
	private Entity ty;
	private Entity tz;
	
	// Scale
	private Entity sx;
	private Entity sy;
	private Entity sz;

	// Rotation
	private Entity rx;
	private Entity ry;
	private Entity rz;
	
	
	private Mouse mouse;
	private KeyBoard keyboard;
	public Gizmo(ECS ecs, Loader loader, Mouse mouse, KeyBoard keyboard) {
		// Translation
		ModelData transData = OBJFileLoader.loadOBJ("res/gizmotrans.obj");
		RawModel transModel = loader.loadToVAO(transData.getVertices(), transData.getTextureCoords(), transData.getNormals(), transData.getIndices());
		TexturedModel transxTextureModel = new TexturedModel(transModel,new ModelTexture(loader.loadTexture("res/red.png")));
		TexturedModel transyTextureModel = new TexturedModel(transModel,new ModelTexture(loader.loadTexture("res/debugTexture.png")));
		TexturedModel transzTextureModel = new TexturedModel(transModel,new ModelTexture(loader.loadTexture("res/blue.png")));
		this.tz = new Entity(transxTextureModel, new Vector3f(0, 0, 0),90,0,0,Utils.toVector3f(scale), 1.0f);
		this.ty = new Entity(transyTextureModel, new Vector3f(0, 0, 0),0,0,0,Utils.toVector3f(scale), 1.0f);
		this.tx = new Entity(transzTextureModel, new Vector3f(0, 0, 0),0,0,90,Utils.toVector3f(scale), 1.0f);
		tx.type = "Gizmo"; tx.uselighting = true;// tx.setID(9);
		ty.type = "Gizmo"; ty.uselighting = true;// tx.setID(1);
		tz.type = "Gizmo"; tz.uselighting = true;// tx.setID(2);
		tx.ModelPath = "res/gizmotrans.obj"; ty.ModelPath = "res/gizmotrans.obj"; tz.ModelPath = "res/gizmotrans.obj";
		tx.TexturePath = "res/red.png"; ty.TexturePath = "res/debugTexture.png"; tz.TexturePath = "res/blue.png";
		
		
		ecs.addEntity(this.tx);
		ecs.addEntity(this.ty);
		ecs.addEntity(this.tz);
		System.out.println(this.tz.getID());
		
		// Scale
		ModelData scaleData = OBJFileLoader.loadOBJ("res/gizmoscale.obj");
		RawModel scaleModel = loader.loadToVAO(scaleData.getVertices(), scaleData.getTextureCoords(), scaleData.getNormals(), scaleData.getIndices());
		TexturedModel scalexTextureModel = new TexturedModel(scaleModel,new ModelTexture(loader.loadTexture("res/red.png")));
		TexturedModel scaleyTextureModel = new TexturedModel(scaleModel,new ModelTexture(loader.loadTexture("res/debugTexture.png")));
		TexturedModel scalezTextureModel = new TexturedModel(scaleModel,new ModelTexture(loader.loadTexture("res/blue.png")));
		this.sz = new Entity(scalexTextureModel, new Vector3f(0, 0, 0),90,0,0,Utils.toVector3f(scale), 1.0f);
		this.sy = new Entity(scaleyTextureModel, new Vector3f(0, 0, 0),0,0,0,Utils.toVector3f(scale), 1.0f);
		this.sx = new Entity(scalezTextureModel, new Vector3f(0, 0, 0),0,0,90,Utils.toVector3f(scale), 1.0f);
		sx.type = "Gizmo"; sx.uselighting = true;
		sy.type = "Gizmo"; sy.uselighting = true;
		sz.type = "Gizmo"; sz.uselighting = true;
		sx.ModelPath = "res/gizmoscale.obj"; sy.ModelPath = "res/gizmoscale.obj"; sz.ModelPath = "res/gizmoscale.obj";
		sx.TexturePath = "res/red.png"; sy.TexturePath = "res/debugTexture.png"; sz.TexturePath = "res/blue.png";
		
		ecs.addEntity(this.sx);
		ecs.addEntity(this.sy);
		ecs.addEntity(this.sz);
		

		// Rotation
		ModelData rotData = OBJFileLoader.loadOBJ("res/gizmorot.obj");
		RawModel rotModel = loader.loadToVAO(rotData.getVertices(), rotData.getTextureCoords(), rotData.getNormals(), rotData.getIndices());
		TexturedModel rotxTextureModel = new TexturedModel(rotModel,new ModelTexture(loader.loadTexture("res/red.png")));
		TexturedModel rotyTextureModel = new TexturedModel(rotModel,new ModelTexture(loader.loadTexture("res/debugTexture.png")));
		TexturedModel rotzTextureModel = new TexturedModel(rotModel,new ModelTexture(loader.loadTexture("res/blue.png")));
		this.rx = new Entity(rotxTextureModel, new Vector3f(0, 0, 0),90,0,0,Utils.toVector3f(scale + 0.5f), 1.0f);
		this.ry = new Entity(rotyTextureModel, new Vector3f(0, 0, 0),90,90,0,Utils.toVector3f(scale  + 0.5f), 1.0f);
		this.rz = new Entity(rotzTextureModel, new Vector3f(0, 0, 0),0,0,90,Utils.toVector3f(scale + 0.5f), 1.0f);
		rx.type = "Gizmo"; rx.uselighting = true;
		ry.type = "Gizmo"; ry.uselighting = true;
		rz.type = "Gizmo"; rz.uselighting = true;
		rx.ModelPath = "res/gizmorot.obj"; ry.ModelPath = "res/gizmorot.obj"; rz.ModelPath = "res/gizmorot.obj";
		rx.TexturePath = "res/red.png"; ry.TexturePath = "res/debugTexture.png"; rz.TexturePath = "res/blue.png";
		
		
		ecs.addEntity(this.rx);
		ecs.addEntity(this.ry);
		ecs.addEntity(this.rz);
		// update list
		
		
		// Other
		this.ecs = ecs;
		this.mouse = mouse;
		this.keyboard = keyboard;
	}
	
	public void reload() {
		ecs.addEntity(this.tx);
		ecs.addEntity(this.ty);
		ecs.addEntity(this.tz);
		ecs.addEntity(this.sx);
		ecs.addEntity(this.sy);
		ecs.addEntity(this.sz);
		ecs.addEntity(this.rx);
		ecs.addEntity(this.ry);
		ecs.addEntity(this.rz);
	}
	
	public void Update() {
		gizmoEntities.clear();
		gizmoEntities.add(tx);
		gizmoEntities.add(ty);
		gizmoEntities.add(tz);
		gizmoEntities.add(sx);
		gizmoEntities.add(sy);
		gizmoEntities.add(sz);
		gizmoEntities.add(rx);
		gizmoEntities.add(ry);
		gizmoEntities.add(rz);
		
	
		
		
		Entity selE = ecs.getSelected_entity();

		
		
		if (keyboard.getKey(keyConfig[2])) {
			this.mode = "SCALE";
		}
		
		if (keyboard.getKey(keyConfig[0])) {
			this.mode = "TRANSLATION";
		}
		
		if (keyboard.getKey(keyConfig[1])) {
			this.mode = "ROTATION";
		}
		
		if (selE == null || ecs.game.isRunning) {
			tx.isRendered = false;
			ty.isRendered = false;
			tz.isRendered = false;
			
			sx.isRendered = false;
			sy.isRendered = false;
			sz.isRendered = false;
			
			rx.isRendered = false;
			ry.isRendered = false;
			rz.isRendered = false;
			
		} else {

			
			if (this.mode == "TRANSLATION") {
				tx.isRendered = true;
				ty.isRendered = true;
				tz.isRendered = true;
				
				sx.isRendered = false;
				sy.isRendered = false;
				sz.isRendered = false;
				
				rx.isRendered = false;
				ry.isRendered = false;
				rz.isRendered = false;
				
				tx.position = selE.getPosition();
				ty.position = selE.getPosition();
				tz.position = selE.getPosition();
				
				
				if (tx.isSelected && ty.isSelected == false && tz.isSelected == false ) {
					ty.isSelected = false;
					tz.isSelected = false;
					selE.position = (new Vector3f(selE.getPosition().x += mouse.getRelMouse().x * 0.1f, selE.getPosition().y, selE.getPosition().z));
					mouse.resetRel();	
				}
				
				if (ty.isSelected && tx.isSelected == false && tz.isSelected == false ) {
					tx.isSelected = false;
					tz.isSelected = false;
					selE.position = (new Vector3f(selE.getPosition().x, selE.getPosition().y -= mouse.getRelMouse().y * 0.1f, selE.getPosition().z));
					mouse.resetRel();
				}
				if (tz.isSelected && tx.isSelected == false && ty.isSelected == false ) {
					ty.isSelected = false;
					tx.isSelected = false;
					selE.position = (new Vector3f(selE.getPosition().x, selE.getPosition().y, selE.getPosition().z += mouse.getRelMouse().x * 0.1f));
					mouse.resetRel();
				}
			}
			
			if (this.mode == "SCALE") {
				sx.isRendered = true;
				sy.isRendered = true;
				sz.isRendered = true;
				
				tx.isRendered = false;
				ty.isRendered = false;
				tz.isRendered = false;
				
				rx.isRendered = false;
				ry.isRendered = false;
				rz.isRendered = false;
				
				sx.position = selE.getPosition();
				sy.position = selE.getPosition();
				sz.position = selE.getPosition();
				
				
				if (sx.isSelected && sy.isSelected == false && sz.isSelected == false && selE.scale.x > 0) {
					selE.scale.x += mouse.getRelMouse().x * 0.1f;
					sz.isSelected = false;
					sy.isSelected = false;
					mouse.resetRel();
				}
				
				if (sy.isSelected && sx.isSelected == false && sz.isSelected == false  && selE.scale.x > 0) {
					selE.scale.y += mouse.getRelMouse().y * 0.1f;
					
					sx.isSelected = false;
					sz.isSelected = false;
					mouse.resetRel();
				}
				if (sz.isSelected && sy.isSelected == false && sx.isSelected == false  && selE.scale.x > 0) {
					selE.scale.z += mouse.getRelMouse().x * 0.1f;
					sy.isSelected = false;
					sx.isSelected = false;
					mouse.resetRel();
					
				}
			}
			
			
			
			if (this.mode == "ROTATION") {
				sx.isRendered = false;
				sy.isRendered = false;
				sz.isRendered = false;
				
				tx.isRendered = false;
				ty.isRendered = false;
				tz.isRendered = false;
				
				rx.isRendered = true;
				ry.isRendered = true;
				rz.isRendered = true;
				
				rx.position = selE.getPosition();
				ry.position = selE.getPosition();
				rz.position = selE.getPosition();
				
				
				if (rx.isSelected && ry.isSelected == false && rz.isSelected == false) {
					selE.increaseRotation(mouse.getRelMouse().x * 0.1f, 0, 0); 
					ry.isSelected = false;
					rz.isSelected = false;
					mouse.resetRel();
				}
				
				if (ry.isSelected && rx.isSelected == false && rz.isSelected == false) {
					selE.increaseRotation(0, mouse.getRelMouse().x * 0.1f, 0); 
					rx.isSelected = false;
					rz.isSelected = false;
					mouse.resetRel();
				}
				if (rz.isSelected && ry.isSelected == false && rx.isSelected == false) {
					
					rx.isSelected = false;
					ry.isSelected = false;
					selE.increaseRotation(0, 0, mouse.getRelMouse().x * 0.1f); 
					mouse.resetRel();
					
				}
			}
		}
		

		
		
	}
	
	public void processRendering(MasterRenderer renderer) {
		for (Entity entity: gizmoEntities) {
			renderer.processGizmo(entity);
		}
	}


	public String[] getKeyConfig() {
		return keyConfig;
	}


	public void setKeyConfig(String[] keyConfig) {
		this.keyConfig = keyConfig;
	}

	public Boolean isSelected() {
		return (!tx.isSelected && !ty.isSelected && !tz.isSelected &&
				!sx.isSelected && !sy.isSelected && !sz.isSelected &&
				!rx.isSelected && !ry.isSelected && !rz.isSelected
				);
	}
	
}
