package components;

import Editor.Console;
import entities.Entity;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import physics.WorldManager;
import renderEngine.DisplayManager;
import toolbox.Utils;

import java.util.ArrayList;
import java.util.List;

public class ComponentManager {

    private Boolean showComp = false;
    private ImBoolean isOpen = new ImBoolean(true);
    public List<DynamicBodyComponent> bodies = new ArrayList<>();
    private float[] pos = {0, 0};
    public ShapeComponent lastShape;
    private WorldManager worldManager;
    private Console console;

    public ComponentManager(WorldManager worldManager, Console console) {
        this.worldManager = worldManager;
        this.console = console;
    }

    public void showComp() {
        this.showComp = true;
        isOpen.set(true);
        pos[0] = ImGui.getMousePosX();
        pos[1] = ImGui.getMousePosY();
    }

    public Component render() {
        if (this.showComp) {
            ImGui.setNextWindowPos(0, Utils.getPrecentageOf(60, DisplayManager.getHeight()));
            ImGui.setNextWindowSize(Utils.getPrecentageOf(15,DisplayManager.getWidth()), Utils.getPrecentageOf(40,DisplayManager.getHeight()));
            ImGui.begin("Choose", ImGuiWindowFlags.AlwaysVerticalScrollbar);


            if (ImGui.button("RigidBody")) {
                this.showComp = false;
                ImGui.end();
                DynamicBodyComponent component = new DynamicBodyComponent(worldManager, console);
                bodies.add(component);
                return component;
            }

            if (ImGui.button("Shape")) {
                this.showComp = false;
                ImGui.end();

                lastShape = new ShapeComponent();
                return lastShape;
            }

            if (ImGui.button("CharacterController")) {
                this.showComp = false;
                ImGui.end();

                return new CharacterComponent(console);
            }
            ImGui.separator();
            if (ImGui.button("Cancel")) {
                this.showComp = false;
                ImGui.end();
                return null;
            }


            ImGui.end();

        }

        return null;
    }

}
