package entities;


import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import Editor.Console;
import imgui.ImGui;
import imgui.ImGuiStyle;
import imgui.type.ImFloat;
import imgui.type.ImString;
import imgui.flag.ImGuiCol;
public class ECS {

	private List<Entity> entities = new ArrayList<Entity>();
	private List<Light> lights = new ArrayList<Light>();
	private List<Camera> cameras = new ArrayList<Camera>();

	public int id_counter = 0;
	public int id_light_counter = 0;
	private int id_camera_counter =0;
	private Entity selected_entity;
	private Light selected_light;
	public Camera selected_camera;
	
	private Entity ClipboardEntity;
	
	private ImGuiStyle style = ImGui.getStyle();
	public Entity current_gizmo;
	public Boolean isPasted = false;
	
	public Entity cameraEntity;
	
	private float[] h = {
			255, 255, 255,
	};
	
	private Console console;
	public ECS(Console console) {	
		this.console = console;
	}

	
	
	
	public void Copy() {
		this.ClipboardEntity = selected_entity;
	}
	
	public void Paste() {
		if (!isPasted && ClipboardEntity != null) {
			Entity newEntity = new Entity(ClipboardEntity.getModel(), ClipboardEntity.getPosition(), ClipboardEntity.getRotX(), ClipboardEntity.getRotY(), ClipboardEntity.getRotZ(), ClipboardEntity.getScale(), 0);
			newEntity.ModelPath = ClipboardEntity.ModelPath;
			newEntity.TexturePath = ClipboardEntity.TexturePath;
			newEntity.setID(id_counter);
			newEntity.isRendered = ClipboardEntity.isRendered;
			newEntity.uselighting = ClipboardEntity.uselighting;
			newEntity.myLight = ClipboardEntity.myLight;
			if (newEntity.myLight != null) {
				addLight(newEntity.myLight);
			}
			id_counter++;
			this.entities.add(newEntity);
			isPasted = true;
		}
	}
	
	
	public void RenderInfo() {
		
		if (this.selected_entity != null & this.selected_light == null && selected_entity.type.startsWith("C")) {
			if (ImGui.treeNodeEx("Camera Component", imgui.flag.ImGuiTreeNodeFlags.DefaultOpen)) {
				ImGui.text(" ");
				ImGui.separator();
				ImGui.text(" ");
				
				ImGui.text("Name: ");
				ImGui.sameLine();
				ImString buf = new ImString(this.selected_light.name);
				ImGui.inputText(" ", buf);
				ImGui.text(" ");
				ImGui.separator();
				ImGui.text(" ");
				if (ImGui.treeNodeEx("Transformation", imgui.flag.ImGuiTreeNodeFlags.DefaultOpen)) {
					//Position
					ImGui.text(" ");
					if (ImGui.treeNodeEx("Position", imgui.flag.ImGuiTreeNodeFlags.DefaultOpen)) {
						this.setTextColor(1, 0, 0, 1);
						ImGui.text("X: ");
						this.resetTextColor();
						ImGui.sameLine();
						ImGui.pushItemWidth(55);
						ImGui.inputFloat(" ", new ImFloat(this.selected_entity.getPosition().x));
						ImGui.sameLine();this.setTextColor(0, 1, 0, 1);
						ImGui.text("Y: ");
						this.resetTextColor();
						ImGui.sameLine();
						ImGui.pushItemWidth(55);
						ImGui.inputFloat(" ", new ImFloat(this.selected_entity.getPosition().y));
						ImGui.sameLine();this.setTextColor(0, 0, 10.3f, 3.0f);
						ImGui.text("Z: ");
						this.resetTextColor();
						ImGui.sameLine();
						ImGui.pushItemWidth(55);
						ImGui.inputFloat(" ", new ImFloat(this.selected_entity.getPosition().z));
						ImGui.treePop();
						this.resetTextColor();
					}
					
					ImGui.text(" ");
					
				}
			}
		}
		
		if (this.selected_entity != null & this.selected_light == null && selected_entity.type.startsWith("E")) {
			//Display info here
			
			//name
			if (ImGui.treeNodeEx("Entity Component", imgui.flag.ImGuiTreeNodeFlags.DefaultOpen)) {
				ImGui.text(" ");
				ImGui.separator();
				ImGui.text(" ");
				
				ImGui.text("Name: ");
				ImGui.sameLine();
				ImString buf = new ImString(this.selected_entity.name);
				ImGui.inputText(" ", buf);
				ImGui.text(" ");
				ImGui.separator();
				ImGui.text(" ");
				if (ImGui.treeNodeEx("Transformation", imgui.flag.ImGuiTreeNodeFlags.DefaultOpen)) {
					//Position
					ImGui.text(" ");
					if (ImGui.treeNodeEx("Position", imgui.flag.ImGuiTreeNodeFlags.DefaultOpen)) {
						this.setTextColor(1, 0, 0, 1);
						ImGui.text("X: ");
						this.resetTextColor();
						ImGui.sameLine();
						
						ImGui.pushItemWidth(55);
						ImGui.inputFloat(" ", new ImFloat(this.selected_entity.getPosition().x));
						ImGui.sameLine();this.setTextColor(0, 1, 0, 1);
						ImGui.text("Y: ");
						this.resetTextColor();
						ImGui.sameLine();
						ImGui.pushItemWidth(55);
						ImGui.inputFloat(" ", new ImFloat(this.selected_entity.getPosition().y));
						ImGui.sameLine();this.setTextColor(0, 0, 10.3f, 3.0f);
						ImGui.text("Z: ");
						this.resetTextColor();
						ImGui.sameLine();
						ImGui.pushItemWidth(55);
						ImGui.inputFloat(" ", new ImFloat(this.selected_entity.getPosition().z));
						ImGui.treePop();
						this.resetTextColor();
						ImGui.treePop();
					}
					ImGui.text(" ");
					// Scale
					if (ImGui.treeNodeEx("Scale", imgui.flag.ImGuiTreeNodeFlags.DefaultOpen)) {
						this.setTextColor(1, 0, 0, 1);
						ImGui.text("X: ");
						this.resetTextColor();
						ImGui.sameLine();
						
						ImGui.pushItemWidth(55);
						ImGui.inputFloat(" ", new ImFloat(this.selected_entity.getScale().x));
						ImGui.sameLine();this.setTextColor(0, 1, 0, 1);
						ImGui.text("Y: ");
						this.resetTextColor();
						ImGui.sameLine();
						ImGui.pushItemWidth(55);
						ImGui.inputFloat(" ", new ImFloat(this.selected_entity.getScale().y));
						ImGui.sameLine();this.setTextColor(0, 0, 10.3f, 3.0f);
						ImGui.text("Z: ");
						this.resetTextColor();
						ImGui.sameLine();
						ImGui.pushItemWidth(55);
						ImGui.inputFloat(" ", new ImFloat(this.selected_entity.getScale().z));
						ImGui.treePop();
						this.resetTextColor();
					}
					ImGui.text(" ");
					if (ImGui.treeNodeEx("Rotation", imgui.flag.ImGuiTreeNodeFlags.DefaultOpen)) {
						this.setTextColor(1, 0, 0, 1);
						ImGui.text("X: ");
						this.resetTextColor();
						ImGui.sameLine();
						
						ImGui.pushItemWidth(55);
						ImGui.inputFloat(" ", new ImFloat(this.selected_entity.getRotX()));
						ImGui.sameLine();
						this.setTextColor(0, 1, 0, 1);
						ImGui.text("Y: ");
						this.resetTextColor();
						ImGui.sameLine();
						ImGui.pushItemWidth(55);
						ImGui.inputFloat(" ", new ImFloat(this.selected_entity.getRotY()));
						ImGui.sameLine();
						this.setTextColor(0, 0, 10.3f, 3.0f);
						ImGui.text("Z: ");
						this.resetTextColor();
						ImGui.sameLine();
						ImGui.pushItemWidth(55);
						ImGui.inputFloat(" ", new ImFloat(this.selected_entity.getRotZ()));
						ImGui.treePop();
						this.resetTextColor();
					}
					ImGui.treePop();
				}
				ImGui.text(" ");
				ImGui.separator();
				ImGui.text(" ");
				if (this.selected_entity.getModel() != null) {
					if (ImGui.treeNodeEx("Model", imgui.flag.ImGuiTreeNodeFlags.DefaultOpen)) {
						this.resetTextColor();
						ImGui.text("Model ID: ");
						ImGui.sameLine();
						ImGui.inputFloat(" ", new ImFloat((float) this.selected_entity.getModel().getRawModel().getVaoID()));
						ImGui.treePop();
					}
				}
				ImGui.text(" ");
				ImGui.separator();
				ImGui.text(" ");


				if (ImGui.button("Delete")) {
					try {
						this.selected_entity.isRendered = false;
						this.selected_entity = null;
					} catch (Exception E){
						
							console.addError("Couldn't remove ENTITY: " + this.selected_entity.getID());
							console.addMessage("Entity ID: " + (this.selected_entity.getID()));
					
					}
				

				
			}	
			
		}
		if (this.selected_entity != null) {
			if (this.selected_entity.myLight != null) {
				if (ImGui.treeNodeEx("Light Component", imgui.flag.ImGuiTreeNodeFlags.DefaultOpen)) {
					ImGui.text(" ");
					ImGui.separator();
					ImGui.text(" ");
					
					ImGui.text("Name: ");
					ImGui.sameLine();
					ImString buf = new ImString(this.selected_entity.myLight.name);
					ImGui.inputText(" ", buf);
					ImGui.text(" ");
					ImGui.separator();
					ImGui.text(" ");
					if (ImGui.treeNodeEx("Transformation", imgui.flag.ImGuiTreeNodeFlags.DefaultOpen)) {
						//Position
						ImGui.text(" ");
						if (ImGui.treeNodeEx("Position", imgui.flag.ImGuiTreeNodeFlags.DefaultOpen)) {
							this.setTextColor(1, 0, 0, 1);
							ImGui.text("X: ");
							this.resetTextColor();
							ImGui.sameLine();
							ImGui.pushItemWidth(55);
							ImGui.inputFloat(" ", new ImFloat(this.selected_entity.myLight.getPosition().x));
							ImGui.sameLine();this.setTextColor(0, 1, 0, 1);
							ImGui.text("Y: ");
							this.resetTextColor();
							ImGui.sameLine();
							ImGui.pushItemWidth(55);
							ImGui.inputFloat(" ", new ImFloat(this.selected_entity.myLight.getPosition().y));
							ImGui.sameLine();this.setTextColor(0, 0, 10.3f, 3.0f);
							ImGui.text("Z: ");
							this.resetTextColor();
							ImGui.sameLine();
							ImGui.pushItemWidth(55);
							ImGui.inputFloat(" ", new ImFloat(this.selected_entity.myLight.getPosition().z));
							ImGui.treePop();
							this.resetTextColor();
						}
						ImGui.text(" ");
						// Scale
						
						
						ImGui.treePop();
					}
					
					ImGui.text(" ");
					ImGui.separator();
					ImGui.text(" ");
					this.selected_entity.myLight.setPosition(this.selected_entity.getPosition());
					if (ImGui.treeNodeEx("Color", imgui.flag.ImGuiTreeNodeFlags.DefaultOpen)) {
						ImGui.colorPicker3("Test", h);
						this.selected_entity.myLight.setColour(new Vector3f(h[0], h[1], h[2]));
						
						ImGui.treePop();
						this.resetTextColor();
					}
					
					
					
					ImGui.treePop();
	
					
				}	
			}
		}
		}
	}
	
	
	
	public void setSelectedEntity(Entity entity) {
		if (entity.type.startsWith("E") && entity.isRendered) {
			this.selected_entity = entity;
			this.selected_light = null;
			this.selected_camera = null;
			if (entity.myLight != null) {
				h[0] = entity.myLight.getColour().x;
				h[1] = entity.myLight.getColour().y;
				h[2] = entity.myLight.getColour().z;
			}
		} else {
			
			Entity entityGIZMO = getEntity(entity.getID());
			
			if (entityGIZMO != null && entityGIZMO.type.startsWith("G")) {
				this.updateGimzos();
				entityGIZMO.isSelected = true;
				this.current_gizmo = entityGIZMO;
			}
		}
	}
	
	public void updateGimzos() {
		if (this.current_gizmo != null) {
			this.current_gizmo.isSelected = false;
		}
	}
	
	public void setSelectedLight(Light entity) {
		this.selected_entity = null;
		this.selected_camera = null;
		this.selected_light = entity;
	}
	
	public void setSelectedCamera(Camera entity) {
		this.selected_camera = entity;
		this.selected_entity = null;
		this.selected_light = null;
	}
	
	public void addEntity(Entity entity) {
		entity.setID(id_counter);
		id_counter += 1;
		this.entities.add(entity);
		System.out.println(entities.get(id_counter - 1).type);
	}
	
	
	public void setEntity(int index, Entity entity) {
		if (index > id_counter) {
			id_counter = index;
		}
		
		entities.add(index, entity);
	}
	
	public void addLight(Light light) {
		light.setID(id_light_counter);
		id_light_counter++;
		this.lights.add(light);
	}
	
	public void addCamera(Camera camera) {
		camera.setID(id_camera_counter);
		id_camera_counter++;
		this.cameras.add(camera);
	}
	
	public Entity getEntity(float id) {
		try {
			//System.out.println(entities.get((int) id).name + "| ID:" + id );
			return entities.get((int) id);
			
		} catch (Exception e) {
			
			return null;
		}
		
	}
	
	public Light getLight(int ID) {
		return lights.get(ID);
	}
	
	public Camera getCamera(int ID) {
		return cameras.get(ID);
	}
	
	public List<Entity> getAllEntities() {
		return this.entities;
	}
	
	public void clearAllEntities() {
		entities.clear();
	}
	
	public void clearAllLights() {
		lights.clear();
	}
	public void clearAllCamera() {
		cameras.clear();
	}
	
	public List<Light> getAllLights() {
		return this.lights;
	}
	
	public List<Camera> getAllCameras() {
		return this.cameras;
	}
	
	public Entity getEntity(Vector3f coords) {
		for (Entity entity:entities) {
			if (entity.getPosition() == coords) {
				return entity;
			}
		} 
		return null;
	}
	
	public void setAllEntities(List<Entity> entities2, int id_counter2) {
		this.entities = entities2;
		this.id_counter = id_counter2;
	}
	
	public void initStyle() {		
		style.setColor(ImGuiCol.Text, 1.00f, 1.00f, 1.00f, 1.00f);
		style.setColor(ImGuiCol.TextDisabled, 0.50f, 0.50f, 0.50f, 1.00f);
		style.setColor(ImGuiCol.WindowBg, 0.0f, 0.17f, 0.26f, 0.07f);
		style.setColor(ImGuiCol.ChildBg, 1.13f, 0.14f, 0.15f, 1.00f);
		style.setColor(ImGuiCol.PopupBg, 0.13f, 0.14f, 0.15f, 1.00f);
		style.setColor(ImGuiCol.Border, 0.43f, 0.43f, 0.50f, 0.50f);
		style.setColor(ImGuiCol.BorderShadow         ,0.00f, 0.00f, 0.00f, 0.00f);
		style.setColor(ImGuiCol.FrameBg              ,0.25f, 0.25f, 0.25f, 1.00f);
		style.setColor(ImGuiCol.FrameBgHovered       ,0.38f, 0.38f, 0.38f, 1.00f);
		style.setColor(ImGuiCol.FrameBgActive        ,0.67f, 0.67f, 0.67f, 0.39f);
		style.setColor(ImGuiCol.TitleBg              ,0.08f, 0.08f, 0.09f, 1.00f);
		style.setColor(ImGuiCol.TitleBgActive        ,0.08f, 0.08f, 0.09f, 1.00f);
		style.setColor(ImGuiCol.TitleBgCollapsed     ,0.00f, 0.00f, 0.00f, 0.51f);
		style.setColor(ImGuiCol.MenuBarBg            ,0.14f, 0.14f, 0.14f, 1.00f);
		style.setColor(ImGuiCol.ScrollbarBg          ,0.02f, 0.02f, 0.02f, 0.53f);
		style.setColor(ImGuiCol.ScrollbarGrab        ,0.31f, 0.31f, 0.31f, 1.00f);
		style.setColor(ImGuiCol.ScrollbarGrabHovered ,0.41f, 0.41f, 0.41f, 1.00f);
		style.setColor(ImGuiCol.ScrollbarGrabActive  ,0.51f, 0.51f, 0.51f, 1.00f);
		style.setColor(ImGuiCol.CheckMark            ,0.11f, 0.64f, 0.92f, 1.00f);
		style.setColor(ImGuiCol.SliderGrab           ,0.11f, 0.64f, 0.92f, 1.00f);
		style.setColor(ImGuiCol.SliderGrabActive     ,0.08f, 0.50f, 0.72f, 1.00f);
		style.setColor(ImGuiCol.Button               ,0.25f, 0.25f, 0.25f, 1.00f);
		style.setColor(ImGuiCol.ButtonHovered        ,0.38f, 0.38f, 0.38f, 1.00f);
		style.setColor(ImGuiCol.ButtonActive         ,0.67f, 0.67f, 0.67f, 0.39f);
		style.setColor(ImGuiCol.Header               ,0.22f, 0.22f, 0.22f, 1.00f);
		style.setColor(ImGuiCol.HeaderHovered        ,0.25f, 0.25f, 0.25f, 1.00f);
		style.setColor(ImGuiCol.HeaderActive         ,0.67f, 0.67f, 0.67f, 0.39f);
		style.setColor(ImGuiCol.Separator           , 0.43f, 0.43f, 0.50f, 0.50f);
		style.setColor(ImGuiCol.SeparatorHovered     ,0.41f, 0.42f, 0.44f, 1.00f);
		style.setColor(ImGuiCol.SeparatorActive      ,0.26f, 0.59f, 0.98f, 0.95f);
		style.setColor(ImGuiCol.ResizeGrip           ,0.00f, 0.00f, 0.00f, 0.00f);
		style.setColor(ImGuiCol.ResizeGripHovered    ,0.29f, 0.30f, 0.31f, 0.67f);
		style.setColor(ImGuiCol.ResizeGripActive     ,0.26f, 0.59f, 0.98f, 0.95f);
		style.setColor(ImGuiCol.Tab                  ,0.08f, 0.8f, 0.09f, 0.83f);
		style.setColor(ImGuiCol.TabHovered           ,0.33f, 0.34f, 0.36f, 0.83f);
		style.setColor(ImGuiCol.TabActive            ,0.23f, 0.23f, 0.24f, 1.00f);
		style.setColor(ImGuiCol.TabUnfocused         ,0.08f, 0.08f, 0.09f, 1.00f);
		style.setColor(ImGuiCol.TabUnfocusedActive   ,0.13f, 0.14f, 0.15f, 1.00f);
		style.setColor(ImGuiCol.DockingPreview       ,0.26f, 0.59f, 0.98f, 0.70f);
		style.setColor(ImGuiCol.DockingEmptyBg       ,0.20f, 0.20f, 0.20f, 1.00f);
		style.setColor(ImGuiCol.PlotLines            ,0.61f, 0.61f, 0.61f, 1.00f);
		style.setColor(ImGuiCol.PlotLinesHovered     ,1.00f, 0.43f, 0.35f, 1.00f);
		style.setColor(ImGuiCol.PlotHistogram        ,0.90f, 0.70f, 0.00f, 1.00f);
		style.setColor(ImGuiCol.PlotHistogramHovered ,1.00f, 0.60f, 0.00f, 1.00f);
		style.setColor(ImGuiCol.TextSelectedBg       ,0.26f, 0.59f, 0.98f, 0.35f);
		style.setColor(ImGuiCol.DragDropTarget       ,0.11f, 0.64f, 0.92f, 1.00f);
		style.setColor(ImGuiCol.NavHighlight         ,0.26f, 0.59f, 0.98f, 1.00f);
		style.setColor(ImGuiCol.NavWindowingHighlight,1.00f, 1.00f, 1.00f, 0.70f);
		style.setColor(ImGuiCol.NavWindowingDimBg    ,0.80f, 0.80f, 0.80f, 0.20f);
		style.setColor(ImGuiCol.ModalWindowDimBg     ,0.80f, 0.80f, 0.80f, 0.35f);

		style.setFrameRounding(2.3f);
		style.setGrabRounding(2.3f);
	}
	
	
	public void setTextColor(float r, float g, float b, float a) {
		style.setColor(ImGuiCol.Text, r, g, b, a);

	}
	
	public void resetTextColor() {
		style.setColor(ImGuiCol.Text, 1.00f, 1.00f, 1.00f, 1.00f);
	}



	public Entity getSelected_entity() {
		return selected_entity;
	}



	public void setSelected_entity(Entity selected_entity) {
		this.selected_entity = selected_entity;
	}
	
	
}
