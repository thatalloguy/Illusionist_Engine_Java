package components;

import Editor.Console;
import com.google.gson.internal.LinkedTreeMap;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import imgui.type.ImFloat;
import physics.WorldManager;
import renderEngine.DisplayManager;
import toolbox.Utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DynamicBodyComponent extends Component{

    private String Shapename = "Choose..";
    private Double shapeID;

    public ImFloat massBuf = new ImFloat(1.00f);

    private boolean renderMenu = false;
    private boolean renderMatMenu = false;
    private float[] pos = {0, 0};
    private ShapeComponent currentshape;

    public ImBoolean freezeRotation = new ImBoolean(false);

    private WorldManager worldManager;
    private Console console;
    private float Mass = 1.0f;

    public DynamicBodyComponent(WorldManager worldManager, Console console) {
        this.worldManager = worldManager;
        this.console = console;
        super.Name = "Dynamic";
    }

    @Override
    public void onRender() {
        if (ImGui.treeNodeEx("Rigid Body (" + super.ID + ")", ImGuiTreeNodeFlags.DefaultOpen)) {


            ImGui.text("Shape: "); ImGui.sameLine();
            if (ImGui.button(Shapename)) {
                pos[0] = ImGui.getMousePosX();
                pos[1] = ImGui.getMousePosY();
                this.renderMenu = true;
            }



            if (this.renderMenu) {
                renderChooseMenu();
            }

            ImGui.text("Mass: "); ImGui.sameLine();
            ImGui.inputFloat("|", massBuf);
            Mass = massBuf.get();

            ImGui.checkbox("Freeze Rotation", freezeRotation);
            parent.freeze = freezeRotation.get();


            if (this.currentshape != null) {
                if (this.currentshape.isDeleted) {
                    this.currentshape = null;
                    this.Shapename = "Choose..";
                }
            }



            ImGui.text(" ");
            if (ImGui.button("Bake")) {
                Bake(true);
            }

            ImGui.text(" ");
            if (ImGui.button("Delete")) {
                super.isDeleted = true;
            }

            ImGui.treePop();
        }
    }


    @Override
    public HashMap<String, Object> onSave() {
        HashMap<String, Object> saveData = new HashMap<String, Object>();
        saveData.put("Shapename", Shapename);
        saveData.put("shapeID", shapeID);
        saveData.put("freezeRotation", freezeRotation.get());
        saveData.put("Mass", massBuf.get());
        saveData.put("type", "Body");
        saveData.put("ID", super.ID);
        saveData.put("currentShape", currentshape.onSave());
        return saveData;
    }

    public void onLoad(HashMap<String, Object> loadData, ComponentManager manager) {
        this.Shapename = (String) loadData.get("Shapename");
        this.shapeID = (Double) loadData.get("shapeID");
        this.freezeRotation.set((boolean) loadData.get("freezeRotation"));
        parent.freeze = (boolean) loadData.get("freezeRotation");
        Double mass = (Double) loadData.get("Mass");
        this.massBuf.set(mass.floatValue());
        this.ID = (double) loadData.get("ID");
        currentshape = new ShapeComponent();
        HashMap result = toHashMap((LinkedTreeMap) loadData.get("currentShape"));
        currentshape.onLoad(result);
        currentshape.parent = parent;
        parent.components.add(currentshape);
        parent.shapes.put(currentshape.ID, currentshape);
        manager.bodies.add(this);
        manager.lastShape = currentshape;
        Bake(true);
        worldManager.setupWorld();
    }

    private HashMap toHashMap(LinkedTreeMap map) {
        HashMap result = new HashMap();
        map.forEach((key, value) -> result.put(key, value));
        return result;
    }

    private void renderChooseMenu() {
        ImGui.setNextWindowPos(0, Utils.getPrecentageOf(60, DisplayManager.getHeight()));
        ImGui.setNextWindowSize(Utils.getPrecentageOf(15,DisplayManager.getWidth()), Utils.getPrecentageOf(40,DisplayManager.getHeight()));
        ImGui.begin("Choose", ImGuiWindowFlags.AlwaysVerticalScrollbar);
        if (ImGui.button("Cancel")) {
            this.renderMenu = false;
            ImGui.end();
        }
        ImGui.separator();
        for (ShapeComponent shape : super.parent.shapes.values()) {
            if (ImGui.button(shape.shapeName + " (" +shapeID + ")")) {
                currentshape = shape;
                Shapename = shape.shapeName + " (" +shapeID + ")";
                this.renderMenu = false;
                ImGui.end();
            }
        }
        if (this.renderMenu) {
            ImGui.end();
        }
    }

    public void Bake(boolean useConsole) {
        if (this.currentshape != null) {
            worldManager.addPhysicsToEntity(super.parent, this.currentshape, this, console);
            if (useConsole) {
                console.addMessage("Baking Collision / physics for " + super.parent.name);
            }
        } else {
            if (useConsole) {
                console.addError("Missing a Shape for:" + this.Name);
            }
        }
    }


}
