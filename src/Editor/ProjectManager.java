package Editor;

import java.awt.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

import org.lwjgl.util.vector.Vector3f;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import entities.ECS;
import entities.Entity;
import entities.Gizmo;
import entities.Light;
import imgui.ImGui;
import imgui.type.ImString;
import input.KeyBoard;
import renderEngine.DisplayManager;
import toolbox.Utils;

public class ProjectManager {

	public String currentProject;	
	@SuppressWarnings("unused")
	private List<String> projects = new ArrayList<String>();
	private Gson gson = new Gson();
	
	private Boolean creatingProject = false;
	private Boolean openingProject = false;
	
	private ImString input = new ImString();
	private KeyBoard keyboard;
	private Console console;
	
	private File mainFile;
	private ECS ecs;
	private Gizmo gizmo;
	
	public ProjectManager(KeyBoard keyboard, Console console, ECS ecs, Gizmo gizmo) {
		this.keyboard = keyboard;
		this.console = console;
		this.gizmo = gizmo;
		this.ecs = ecs;
	}
	
	public void Save() {
		if (mainFile != null) {
			// ENTITIES
			 Writer writer;
			 try {
				writer = new FileWriter("Projects/" + this.currentProject + "/" + this.currentProject + ".json");
				// convert users list to JSON file
				 List<Entity> entities = ecs.getAllEntities();
				 List<SaveEntity> savable = new ArrayList<SaveEntity>();
				 for (Entity entity: entities) {
					 if (entity.type == "Entity" && entity.name != "Light") {
						 SaveEntity save = new SaveEntity();
						 System.out.println(entity.ModelPath + " | " + entity.TexturePath);
						 save.ConvertEntity(entity);
						 savable.add(save);
					 }
					 
				 }	 
				 this.gson.toJson(savable, writer); 
				 
				 console.addMessage("Sucsessfully wrote to file: " + "Projects/" + this.currentProject + "/" + this.currentProject + ".json");
				 writer.close();
			} catch (IOException e) {
				console.addError("Couldn't write to file: " + "Projects/" + this.currentProject + "/" + this.currentProject + ".json");
				
			}
			 // LIGHTS
			 Writer writer2;
			 try {
				 writer2 = new FileWriter("Projects/" + this.currentProject + "/lights.json");
				// convert users list to JSON file
				 List<Light> lights = ecs.getAllLights(); 
				 List<SaveLight> savableLights = new ArrayList<SaveLight>();
				 for (Light light : lights) {
					 SaveLight saveLight = new SaveLight();
					 saveLight.ConvertEntity(light);
					 savableLights.add(saveLight);
				 }
				 this.gson.toJson(savableLights, writer2); 
				 
				 console.addMessage("Sucsessfully wrote to file: " + "Projects/" + this.currentProject + "/lights.json");
				 writer2.close();
			} catch (IOException e) {
				console.addError("Couldn't write to file: " + "Projects/" + this.currentProject + "/lights.json");
				
			}
			 
			 // CAMERA
			 Writer writer3;
			 try {
				 writer3 = new FileWriter("Projects/" + this.currentProject + "/camera.json");
				// convert users list to JSON file
				 SaveCamera camera = new SaveCamera();
				 camera.Convert(ecs.cameraEntity.getPosition(), new Vector3f(ecs.cameraEntity.getRotX(), ecs.cameraEntity.getRotY(),ecs.cameraEntity.getRotZ())); 
				 this.gson.toJson(camera, writer3); 
				 
				 console.addMessage("Sucsessfully wrote to file: " + "Projects/" + this.currentProject + "/camera.json");
				 writer3.close();
			} catch (IOException e) {
				console.addError("Couldn't write to file: " + "Projects/" + this.currentProject + "/camera.json");
				
			}

			 
		} else {
			console.addError("Couldn't save because there is no current project");
		}
	}
	
	public void Open(ECS ecs) {
		openingProject = true;
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("Projects/"));
		Component a = null;
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fc.showOpenDialog(a);
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			this.openingProject = false;
			this.currentProject = file.getName();
			console.addMessage("Opened Project: " + currentProject);
			this.mainFile =new File("Projects/" + currentProject + "/"+ currentProject + ".json");
			try {
				loadProject("Projects/" + currentProject + "/"+ currentProject + ".json", ecs);
				console.addMessage("Sucsessfuly loaded project: " + currentProject);
			} catch (IOException e) {
				e.printStackTrace();
				console.addError("Couldn't load project: " + currentProject);
			}
		}
	}
	
	private void loadProject(String path, ECS ecs) throws IOException {
		ecs.clearAllEntities();
		ecs.clearAllCamera();
		ecs.clearAllLights();
		
		
		
		
		
		
		Reader reader = Files.newBufferedReader(Paths.get("Projects/" + currentProject + "/" + currentProject + ".json"));
		
		List<SaveEntity> entities = gson.fromJson(reader, new TypeToken<List<SaveEntity>>() {}.getType());
		
		ecs.id_counter = 0;
		gizmo.reload();
		for (SaveEntity entity: entities) {
			Entity newEntity = entity.ConvertBack();
			
			ecs.addEntity(newEntity);

		}
		
		
		reader.close();
		ecs.id_light_counter = 0;
		Reader reader2 = Files.newBufferedReader(Paths.get("Projects/" + currentProject + "/lights.json"));
		
		List<SaveLight> savelights = gson.fromJson(reader2, new TypeToken<List<SaveLight>>() {}.getType());
		for (SaveLight light: savelights) {
			Light coolLight = light.ConvertBack(ecs);
			ecs.addLight(coolLight);
		}
		reader2.close();
		
		
		
		Reader reader3 = Files.newBufferedReader(Paths.get("Projects/" + currentProject + "/camera.json"));
		
		SaveCamera camera = gson.fromJson(reader3, SaveCamera.class);
		Vector3f[] list = camera.getData();
		ecs.cameraEntity.position = list[0];
		ecs.cameraEntity.rotX = list[1].x;
		ecs.cameraEntity.rotY = list[1].y;
		ecs.cameraEntity.rotZ = list[1].z;
		reader3.close();
	}
	
	public void New() {
		this.creatingProject = true;
	}
	
	public void Render(ECS ecs) throws IOException {
		if (keyboard.getKey("lcontrol") && keyboard.getKey("n") && this.creatingProject == false) {
			New();
		}
		
		if (keyboard.getKey("lcontrol") && keyboard.getKey("o") && this.openingProject == false) {
			Open(ecs);
		}
		
		if (this.creatingProject) {
			ImGui.setNextWindowPos(0, Utils.getPrecentageOf(60, DisplayManager.getHeight()));
			ImGui.setNextWindowSize(Utils.getPrecentageOf(25,DisplayManager.getWidth()), Utils.getPrecentageOf(40,DisplayManager.getHeight()));
			ImGui.begin("New Project", imgui.flag.ImGuiWindowFlags.NoMove | imgui.flag.ImGuiWindowFlags.NoResize | imgui.flag.ImGuiWindowFlags.NoCollapse);
			
			ImGui.text("TITLE: ");
			ImGui.separator();
			ImGui.inputText(" ", input);
			ImGui.spacing();
			ImGui.spacing();
			if (ImGui.button("Done")) {
				SetupProject(input.get());
				this.creatingProject = false;
			}
			
			if (ImGui.button("Cancel")) {
				this.creatingProject = false;
			}
			ImGui.end();
		}
	}
	
	
	private void SetupProject(String name) throws IOException {
		File f = new File("Projects/" + name);
		
		if (f.mkdir()) {
			console.addMessage("Sucsessfully made the Folder of project: " + name);
			this.creatingProject = false;
		} else {
			console.addError("Couldn't make the Folder of Project: " + name);
		}
		
		File resf = new File("Projects/" + name + "/Resources");
		if (resf.mkdir()) {
			console.addMessage("Sucsessfully made the Resource folder of: " + name);
		} else {
			console.addError("Couldn't make the Resource folder of: " + name);
		}
		this.currentProject = name;
		this.mainFile =new File("Projects/" + name + "/"+ name + ".json");
		this.mainFile.createNewFile();
		
		File lights = new File("Projects/" + name + "/" + "lights.json");
		lights.createNewFile();
		
		File camerefile = new File("Projects/" + name + "/" + "camera.json");
		camerefile.createNewFile();
	}
}
