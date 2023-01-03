package components;

import Editor.Console;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.flag.ImGuiWindowFlags;
import physics.WorldManager;

public class StaticBodyComponent extends Component{

    private String Shapename = "Choose..";
    private int shapeID;

    private String MaterialName = "Choose..";
    private int materialID;

    private boolean renderMenu = false;
    private boolean renderMatMenu = false;
    private float[] pos = {0, 0};
    private ShapeComponent currentshape;
    private WorldManager worldManager;
    private Console console;
    public StaticBodyComponent(WorldManager worldManager, Console console) {
        this.worldManager = worldManager;
        this.console = console;
    }

    @Override
    public void onRender() {
        if (ImGui.treeNodeEx("Static Body (" + super.ID + ")", ImGuiTreeNodeFlags.DefaultOpen)) {


            ImGui.text("Shape: "); ImGui.sameLine();
            if (ImGui.button(Shapename)) {
                pos[0] = ImGui.getMousePosX();
                pos[1] = ImGui.getMousePosY();
                this.renderMenu = true;
            }


            if (this.renderMenu) {
                renderChooseMenu();
            }


            if (this.currentshape != null) {
                if (this.currentshape.isDeleted) {
                    this.currentshape = null;
                    this.Shapename = "Choose..";
                }
            }


            ImGui.text(" ");
            if (ImGui.button("Bake")) {
                if (this.currentshape != null) {
                    //worldManager.addPhysicsToEntity(super.parent, this.currentshape,this, null, console);
                    console.addMessage("SucsessFully Baked: " + this.parent.name);
                } else {
                    console.addError("Missing a Shape for:" + this.Name);
                }
            }

            ImGui.text(" ");
            if (ImGui.button("Delete")) {
                super.isDeleted = true;
            }

            ImGui.treePop();
        }
    }

    private void renderChooseMenu() {
        ImGui.setNextWindowPos(pos[0], pos[1]);
        ImGui.begin("Choose", ImGuiWindowFlags.AlwaysVerticalScrollbar);
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


}
