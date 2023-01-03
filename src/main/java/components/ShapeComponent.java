package components;

import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.util.ObjectArrayList;
import entities.DebugEntity;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.type.ImBoolean;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import imgui.type.ImString;
import models.RawModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import renderEngine.Loader;

import javax.vecmath.Vector3f;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Objects;

public class ShapeComponent extends Component{

    public String shapeName = "Shape";
    public String geometryType = "Choose..";
    public String[] geometryList = {"None", "Box", "Sphere", "Capsule", "Custom"};
    private ImString namebuf = new ImString(shapeName + "      ");
    private ImInt currentOption = new ImInt(0);

    public ImFloat radiusBuf = new ImFloat(0.000f);
    public ImFloat heightBuf = new ImFloat(0.000f);

    public ImFloat scaleXBuf = new ImFloat(0.000f);
    public ImFloat scaleYBuf = new ImFloat(0.000f);
    public ImFloat scaleZBuf = new ImFloat(0.000f);

    public ImBoolean isTrigger = new ImBoolean(false);
    private ImBoolean isRender = new ImBoolean(true);
    public ShapeComponent() {
        super.type = "shape";
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onRender() {
        if (ImGui.treeNodeEx("Shape (" + super.ID + ")", ImGuiTreeNodeFlags.DefaultOpen)) {


            ImGui.text("Name: "); ImGui.sameLine(); ImGui.inputText(" ", namebuf);
            shapeName = namebuf.toString();
            if (ImGui.combo(" Geometry", currentOption, geometryList)) {
                geometryType = geometryList[currentOption.get()];
            }

            ImGui.checkbox(" IsTrigger", isTrigger);
            ImGui.checkbox("Render debug", isRender);
            if (parent.debugEntity != null) {
                parent.debugEntity.isRendered = isRender.get();
            }
            if (Objects.equals(geometryType, "Box")) {
                ImGui.text(" ");
                ImGui.text("Size: ");
                ImGui.inputFloat("ScaleX: ", scaleXBuf);
                ImGui.inputFloat("ScaleY: ", scaleYBuf);
                ImGui.inputFloat("ScaleZ: ", scaleZBuf);
            }

            if (Objects.equals(geometryType, "Sphere")) {
                ImGui.text(" ");
                ImGui.text("Size: ");
                ImGui.inputFloat("Radius: ", radiusBuf);
            }

            if (Objects.equals(geometryType, "Capsule")) {
                ImGui.text(" ");
                ImGui.text("Size: ");
                ImGui.inputFloat("Radius: ", radiusBuf);
                ImGui.inputFloat("Height: ", heightBuf);
            }

            if (geometryType.equals("Custom")) {
                ImGui.text("Shape is automatically made");
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
        HashMap<String, Object> saveData = new HashMap<>();
        saveData.put("shapeName", shapeName);
        saveData.put("geometry", geometryType);
        saveData.put("nameBuf", namebuf.get());
        saveData.put("current", currentOption.get());
        saveData.put("radius", radiusBuf.get());
        saveData.put("height", heightBuf.get());
        saveData.put("scaleX", scaleXBuf.get());
        saveData.put("scaleY", scaleYBuf.get());
        saveData.put("scaleZ", scaleZBuf.get());
        saveData.put("isTrigger", isTrigger.get());
        saveData.put("isRender", isRender.get());
        saveData.put("ID", ID);
        saveData.put("type", type);
        return saveData;
    }

    @Override
    public void onLoad(HashMap loadData) {
        this.shapeName = (String) loadData.get("shapeName");
        this.geometryType = (String) loadData.get("geometry");
        this.namebuf.set(loadData.get("nameBuf"));
        Double tempD = (Double) loadData.get("current");
        this.currentOption.set(tempD.intValue());
        tempD = (Double) loadData.get("radius");
        this.radiusBuf.set(tempD.floatValue());
        tempD = (Double) loadData.get("height");
        this.heightBuf.set(tempD.floatValue());
        tempD = (Double) loadData.get("scaleX");
        this.scaleXBuf.set(tempD.floatValue());
        tempD = (Double) loadData.get("scaleY");
        this.scaleYBuf.set(tempD.floatValue());
        tempD = (Double) loadData.get("scaleZ");
        this.scaleZBuf.set(tempD.floatValue());
        this.isTrigger.set((boolean) loadData.get("isTrigger"));
        this.isRender.set((boolean) loadData.get("isRender"));
        tempD = (Double) loadData.get("ID");
        ID = tempD.intValue();
        tempD = null;
    }

    public CollisionShape getShape() {
        if (geometryType.startsWith("B")) {
            return new BoxShape(new Vector3f(scaleXBuf.get() / 2 , scaleYBuf.get() / 2 , scaleZBuf.get() / 2));
        } else if (geometryType.startsWith("S")) {
            return new SphereShape(radiusBuf.get());
        } else if (geometryType.startsWith("Ca")) {
            return new CapsuleShape(radiusBuf.get(), heightBuf.get());
        } else if (geometryType.startsWith("Cu")) {
            ModelData data = OBJFileLoader.loadOBJ(parent.ModelPath);
          return createCustomShape(data.getVertices(), data.getIndices());
        } else {
            System.out.println("none" + geometryType);
            return null;
        }
    }

    public void setupDebug() {
        String location = "";
        org.lwjgl.util.vector.Vector3f size = new org.lwjgl.util.vector.Vector3f();
        if (geometryType.startsWith("B")) {
            location = "res/cube.obj";
            size = new org.lwjgl.util.vector.Vector3f(scaleXBuf.get() / 0.5f, scaleYBuf.get() / 0.5f, scaleZBuf.get() / 0.5f);
        } else if (geometryType.startsWith("S")) {
            location = "res/sphere.obj";
            size.x = radiusBuf.get(); size.y = radiusBuf.get(); size.z = radiusBuf.get();
        } else if (geometryType.startsWith("Ca")) {
            location = "res/capsule.obj";
            size.x = radiusBuf.get();
            size.z = radiusBuf.get();
            size.y = heightBuf.get();
        } else if (geometryType.startsWith("Cu")) {
             location = parent.ModelPath;
             size = parent.getScale();
        }

        ModelData data = OBJFileLoader.loadOBJ(location);
        RawModel model = new Loader().loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
        super.parent.debugEntity = new DebugEntity(model, parent.position, parent.rotX, parent.rotY, parent.rotZ, size, 0);
    }

    private CollisionShape createCustomShape(float[] vertices, int[] indices) {
        // Create a TriangleIndexVertexArray to store the mesh data
        TriangleIndexVertexArray indexVertexArrays = new TriangleIndexVertexArray();

        // Create an ObjectArrayList to store the IndexedMesh objects
        ObjectArrayList<IndexedMesh> indexedMeshes = new ObjectArrayList<IndexedMesh>();

        // Create an IndexedMesh and add it to the list
        IndexedMesh indexedMesh = new IndexedMesh();
        indexedMesh.numVertices = vertices.length / 3;
        indexedMesh.triangleIndexBase = ByteBuffer.allocateDirect(indices.length * 4);
        indexedMesh.triangleIndexBase.order(ByteOrder.nativeOrder());
        indexedMesh.triangleIndexBase.asIntBuffer().put(indices);
        indexedMesh.triangleIndexStride = 3 * 4;
        indexedMesh.numTriangles = indices.length / 3;
        indexedMesh.vertexBase = ByteBuffer.allocateDirect(vertices.length * 4);
        indexedMesh.vertexBase.order(ByteOrder.nativeOrder());
        indexedMesh.vertexBase.asFloatBuffer().put(vertices);
        indexedMesh.vertexStride = 3 * 4;
        indexedMeshes.add(indexedMesh);

        // Set the indexed meshes in the TriangleIndexVertexArray
        indexVertexArrays.addIndexedMesh(indexedMesh);

        // Create the CollisionShape
        CollisionShape shape = new BvhTriangleMeshShape(indexVertexArrays, true);

        return shape;
    }
}
