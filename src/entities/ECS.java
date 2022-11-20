package entities;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import imgui.ImFont;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImGuiStyle;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import imgui.type.ImString;
import imgui.ImVec4;
import imgui.flag.ImGuiCol;
public class ECS {

	private List<Entity> entities = new ArrayList<Entity>();
	private List<Light> lights = new ArrayList<Light>();
	private List<Camera> cameras = new ArrayList<Camera>();

	private int id_counter = 0;
	
	private Entity selected_entity;
	private Light selected_light;
	private Camera selected_camera;
	
	private ImGuiStyle style = ImGui.getStyle();
	
	public ECS() {	

	}

	
	
	public void RenderInfo() {
		if (this.selected_entity != null & this.selected_light == null) {
			
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

					
				
				
				ImGui.treePop();

				
			}	
			
		}
		
		if (this.selected_light != null && this.selected_entity == null) {
			if (ImGui.treeNodeEx("Light Component", imgui.flag.ImGuiTreeNodeFlags.DefaultOpen)) {
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
						ImGui.inputFloat(" ", new ImFloat(this.selected_light.getPosition().x));
						ImGui.sameLine();this.setTextColor(0, 1, 0, 1);
						ImGui.text("Y: ");
						this.resetTextColor();
						ImGui.sameLine();
						ImGui.pushItemWidth(55);
						ImGui.inputFloat(" ", new ImFloat(this.selected_light.getPosition().y));
						ImGui.sameLine();this.setTextColor(0, 0, 10.3f, 3.0f);
						ImGui.text("Z: ");
						this.resetTextColor();
						ImGui.sameLine();
						ImGui.pushItemWidth(55);
						ImGui.inputFloat(" ", new ImFloat(this.selected_light.getPosition().z));
						ImGui.treePop();
						this.resetTextColor();
					}
					ImGui.text(" ");
					// Scale
					if (ImGui.treeNodeEx("Color", imgui.flag.ImGuiTreeNodeFlags.DefaultOpen)) {
						this.setTextColor(1, 0, 0, 1);
						ImGui.text("r: ");
						this.resetTextColor();
						ImGui.sameLine();
						
						ImGui.pushItemWidth(55);
						ImGui.inputFloat(" ", new ImFloat(this.selected_light.getColour().x));
						ImGui.sameLine();this.setTextColor(0, 1, 0, 1);
						ImGui.text("g: ");
						this.resetTextColor();
						ImGui.sameLine();
						ImGui.pushItemWidth(55);
						ImGui.inputFloat(" ", new ImFloat(this.selected_light.getColour().y));
						ImGui.sameLine();this.setTextColor(0, 0, 10.3f, 3.0f);
						ImGui.text("b: ");
						this.resetTextColor();
						ImGui.sameLine();
						ImGui.pushItemWidth(55);
						ImGui.inputFloat(" ", new ImFloat(this.selected_light.getColour().z));
						ImGui.treePop();
						this.resetTextColor();
					}
					
					ImGui.treePop();
				}
				
				ImGui.text(" ");
				ImGui.separator();
				ImGui.text(" ");

					
				
				
				ImGui.treePop();

				
			}	
		}
	}
	
	
	
	public void setSelectedEntity(Entity entity) {
		this.selected_entity = entity;
		this.selected_light = null;
		this.selected_camera = null;
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
	}
	
	public void addLight(Light light) {
		light.setID(id_counter);
		id_counter += 1;
		this.lights.add(light);
	}
	
	public void addCamera(Camera camera) {
		camera.setID(id_counter);
		id_counter++;
		this.cameras.add(camera);
	}
	
	public Entity getEntity(float id) {
		try {
			id -= 1;
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
	
	public List getAllEntities() {
		return this.entities;
	}
	
	public List getAllLights() {
		return this.lights;
	}
	
	public List getAllCameras() {
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
	
	
	private void setTextColor(float r, float g, float b, float a) {
		style.setColor(ImGuiCol.Text, r, g, b, a);

	}
	
	private void resetTextColor() {
		style.setColor(ImGuiCol.Text, 1.00f, 1.00f, 1.00f, 1.00f);
	}



	public Entity getSelected_entity() {
		return selected_entity;
	}



	public void setSelected_entity(Entity selected_entity) {
		this.selected_entity = selected_entity;
	}
	
	
}
