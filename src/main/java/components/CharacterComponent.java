package components;

import Editor.Console;
import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.GhostObject;
import com.bulletphysics.collision.dispatch.PairCachingGhostObject;
import com.bulletphysics.collision.shapes.ConeShape;
import com.bulletphysics.collision.shapes.ConvexShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.character.KinematicCharacterController;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.type.ImFloat;
import org.lwjgl.Sys;

import java.util.HashMap;
import java.util.Objects;

public class CharacterComponent extends Component{

    public ImFloat stepHeight = new ImFloat(1.0f);

    private final GhostObject ghostObject;
    private Console console;

    public CharacterComponent(Console console) {
        ghostObject = new GhostObject();
        ghostObject.setCollisionFlags(CollisionFlags.CHARACTER_OBJECT);
        this.console = console;
    }

    @Override
    public void onRender() {
        if (ImGui.treeNodeEx("CharacterController (" + super.ID + ")", ImGuiTreeNodeFlags.DefaultOpen)) {
            ImGui.text("Body:"); ImGui.sameLine(); ImGui.text("body is the same as parent");
            ImGui.text(" ");

            ImGui.inputFloat("StepHeight", stepHeight);
            ImGui.text(" ");
            if (ImGui.button("Bake")) {
                Bake();
            }

            ImGui.text(" ");
            if (ImGui.button("Delete")) {
                super.isDeleted = true;
            }



            ImGui.treePop();
        }
    }

    public void Bake() {
        if (parent.rigidBody != null) {
            ghostObject.setCollisionShape(parent.rigidBody.getCollisionShape());
            PairCachingGhostObject pairCachingGhostObject = new PairCachingGhostObject();
            pairCachingGhostObject.setCollisionShape(parent.rigidBody.getCollisionShape());
            pairCachingGhostObject.setUserPointer(ghostObject);
            parent.ghostObject = ghostObject;
            parent.characterController = new KinematicCharacterController(pairCachingGhostObject, (ConvexShape) parent.rigidBody.getCollisionShape(), stepHeight.get());
        } else {
            console.addMessage("Cant Bake because there is no Body!");
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onLoad(HashMap<String, Object> hashMap) {
        Double tempD = (Double) hashMap.get("stepHeight");
        stepHeight.set(tempD.floatValue());
        tempD = null;
        Bake();
    }

    @Override
    public HashMap onSave() {

        HashMap<String, Object> saveData = new HashMap<>();
        saveData.put("type", "character");
        saveData.put("stepHeight", stepHeight.get());
        return saveData;
    }
}
