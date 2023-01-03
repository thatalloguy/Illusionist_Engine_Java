package components;

import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.type.ImFloat;
import imgui.type.ImString;
public class MaterialComponent extends Component{

    private float staticFriction = 000.0f;
    private float restitution = 000.0f;
    private float dynamicFriction = 000.0f;

    private ImFloat staticBuf = new ImFloat(staticFriction);
    private ImFloat restitutionBuf = new ImFloat(restitution);
    private ImFloat dynamicBuf = new ImFloat(dynamicFriction);

    public String materialName = "Material";
    private ImString nameBuf = new ImString(materialName);

    public MaterialComponent() {
        super.Name = "Physics Material";
        super.type = "Material";
    }



    @Override
    public void onRender() {

        if (ImGui.treeNodeEx("Material (" + super.ID + ")", ImGuiTreeNodeFlags.DefaultOpen)) {

            ImGui.text(super.Name + ":");
            ImGui.text(" ");
            ImGui.inputText(" Name", nameBuf);
            ImGui.separator();
            ImGui.inputFloat("staticFriction", staticBuf);
            ImGui.text(" ");
            ImGui.text(super.Name + ":");
            ImGui.inputFloat("restitution", restitutionBuf);
            ImGui.text(" ");
            ImGui.text(super.Name + ":");
            ImGui.inputFloat("dynamicFriction", dynamicBuf);
            ImGui.text(" ");
            if (ImGui.button("Update")) {
                this.staticFriction = this.staticBuf.get();
                this.restitution = this.restitutionBuf.get();
                this.dynamicFriction = this.restitutionBuf.get();
                this.materialName = nameBuf.toString();
            } ImGui.sameLine();
            ImGui.text(" ");
            ImGui.sameLine();
            if (ImGui.button("Delete")) {
                super.isDeleted = true;
            }
            ImGui.text(" ");
            ImGui.treePop();
        }
    }



}
