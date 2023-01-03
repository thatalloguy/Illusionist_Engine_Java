package Editor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.IDebugDraw;
import com.bulletphysics.linearmath.Transform;

import models.RawModel;
import models.TexturedModel;

import objConverter.ModelData;
import objConverter.OBJFileLoader;

import org.lwjgl.util.vector.Vector3f;

import entities.ECS;
import entities.EditorCamera;
import entities.Entity;
import entities.Gizmo;
import entities.Light;

import fontRendering.TextMaster;

import groovy.lang.GroovyShell;

import imgui.ImGui;
import imgui.extension.texteditor.TextEditor;
import imgui.extension.texteditor.TextEditorLanguageDefinition;

import input.KeyBoard;
import input.Mouse;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;

import textures.ModelTexture;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;

public class PhysicsTest {

    private static final float BOX_SIZE = 1.0f;
    private static final float BOX_MASS = 1.0f;

    private static final float GRAVITY = -9.81f;
    private static final javax.vecmath.Vector3f GRAVITY_VECTOR = new javax.vecmath.Vector3f(0, GRAVITY, 0);

    private static final float GROUND_SIZE = 100.0f;
    private static final float GROUND_THICKNESS = 1.0f;

    private static final float SIMULATION_STEP = 1.0f / 60.0f;
    private static final int MAX_SUB_STEPS = 10;

    private static DiscreteDynamicsWorld dynamicsWorld;
    private static IDebugDraw debugDraw;
    private static RigidBody boxRigidBody;

    public static void main(String[] args) throws IOException {
        DisplayManager.createDisplay();



        Loader loader = new Loader();
        TextMaster.init(loader);
        Mouse mouse = new Mouse();
        KeyBoard keyboard = new KeyBoard();
        Console console = new Console();
        ECS ecs = new ECS(console, new Game(),keyboard, mouse);
        EditorCamera camera = new EditorCamera(mouse, keyboard, ecs);

        GroovyShell shell = new GroovyShell();

        MasterRenderer renderer = new MasterRenderer(loader,camera);
        TextEditor EDITOR = new TextEditor();
        TextEditorLanguageDefinition lang = TextEditorLanguageDefinition.angelScript();
        EDITOR.setLanguageDefinition(lang);
        // Basic Scene

        List<Entity> entities = new ArrayList<Entity>();


        List<Light> lights = new ArrayList<Light>();
        Light sun = new Light(new Vector3f(1000000,1000000,-100000),new Vector3f(1.4f,1.4f,1.4f));
        sun.name = "Sun";
        lights.add(sun);
        ecs.addLight(sun);
        ecs.addCamera(camera);

        //Fbo
        ecs.selected_camera = camera;
        FrameBuffer fbo = new FrameBuffer();

        //Project managing
        Gizmo gizmo = new Gizmo(ecs, loader, mouse, keyboard);
        ProjectManager pm = new ProjectManager(keyboard, console, ecs, gizmo);
        ecs.pm = pm;
        ecs.SoundManager.pm = pm;


        ////////////////////////////PHYSICSSS///////////////////////////////
        CollisionShape groundShape = new StaticPlaneShape(new javax.vecmath.Vector3f(0, 1, 0), GROUND_THICKNESS);
        Entity ground = createFast(new Vector3f(10, 0.75f, 10), new Vector3f(0, -GROUND_THICKNESS, 0), new Vector3f(0, 0, 0));
        ecs.addEntity(ground);
        CollisionShape boxShape = new BoxShape(new javax.vecmath.Vector3f(BOX_SIZE /2, BOX_SIZE / 2, BOX_SIZE / 2));
        Entity box = createFast(new Vector3f(BOX_SIZE /2, BOX_SIZE / 2, BOX_SIZE / 2), new Vector3f(0, 10, 0), new Vector3f(0, 0, 0));
        ecs.addEntity(box);
        DefaultMotionState groundMotionState = new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new javax.vecmath.Vector3f(0, -GROUND_THICKNESS, 0), 1.0f)));
        DefaultMotionState boxMotionState = new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new javax.vecmath.Vector3f(0, 10, 0), 1.0f)));

        // Create the rigid body construction info for the ground and the box
        javax.vecmath.Vector3f groundInertia = new javax.vecmath.Vector3f(0, 0, 0);
        groundShape.calculateLocalInertia(0, groundInertia);
        RigidBodyConstructionInfo groundRigidBodyCI = new RigidBodyConstructionInfo(0, groundMotionState, groundShape, groundInertia);

        javax.vecmath.Vector3f boxInertia = new javax.vecmath.Vector3f(0, 0, 0);
        boxShape.calculateLocalInertia(BOX_MASS, boxInertia);
        RigidBodyConstructionInfo boxRigidBodyCI = new RigidBodyConstructionInfo(BOX_MASS, boxMotionState, boxShape, boxInertia);

        // Create the rigid bodies for the ground and the box
        RigidBody groundRigidBody = new RigidBody(groundRigidBodyCI);
        boxRigidBody = new RigidBody(boxRigidBodyCI);
        // Set up the dynamics world


        CollisionDispatcher collisionDispatcher = new CollisionDispatcher(new DefaultCollisionConfiguration());
        BroadphaseInterface broadphase = new DbvtBroadphase();

        DiscreteDynamicsWorld dynamicsWorld = new DiscreteDynamicsWorld(collisionDispatcher, broadphase, new SequentialImpulseConstraintSolver(), new DefaultCollisionConfiguration());
        dynamicsWorld.setGravity(GRAVITY_VECTOR);

        dynamicsWorld.addRigidBody(groundRigidBody);
        dynamicsWorld.addRigidBody(boxRigidBody);



    //Main Loop

        ecs.initStyle();

        boolean firstGameFrame = true;


        ecs.game.isRunning = true;
        camera.setPosition(ecs.cameraEntity.getPosition());

        while (!DisplayManager.shouldClose()) {
            //mouser.update();
            // Physics

            dynamicsWorld.stepSimulation(SIMULATION_STEP, MAX_SUB_STEPS);
            javax.vecmath.Vector3f boxPos = new javax.vecmath.Vector3f();
            boxRigidBody.getCenterOfMassPosition(boxPos);
            box.position.x = boxPos.x;
            box.position.y = boxPos.y;
            box.position.z = boxPos.z;
            Quat4f or = new Quat4f();
            boxRigidBody.getOrientation(or);
            box.rotX = or.x;
            box.rotY = or.y;
            box.rotZ = or.z;

            //rendering
            DisplayManager.clearScreen();
            renderer.renderShadowMap(entities, sun);
            lights = ecs.getAllLights();
            entities = ecs.getAllEntities();
            fbo.bindRefractionFrameBuffer();
            for(Entity entity:entities){
                entity.isPicking = false;
                if (entity.type.startsWith("E") && entity.isRendered) {
                    renderer.processEntity(entity);
                }
            }

            camera.refresh();
            renderer.skyboxRenderer.isPicking = false;
            gizmo.processRendering(renderer);

            renderer.renderSkyBox = true;
            renderer.render(lights, camera, ecs);
            fbo.unbindCurrentFrameBuffer();
            // REnder the fbo with imgui
            camera.move();
            DisplayManager.clearScreen();

            if (mouse.isRightClick) {
                camera.Enable();
            } else {
                camera.Disable();
            }

            if (ecs.keyboard.getKey("e")) {
                Transform transform = new Transform();
                transform.setIdentity();
                transform.origin.set(0, 10, 0);
                boxRigidBody.setCenterOfMassTransform(transform);
            }

            DisplayManager.imGuiGlfw.newFrame();
            ImGui.newFrame();


            //ImGui.setNextWindowPos(Utils.getPrecentageOf(25, DisplayManager.getWidth()), 0);
            ImGui.setNextWindowSize(DisplayManager.getWidth(), DisplayManager.getHeight());
            ImGui.setNextWindowPos(0, 0);
            ImGui.begin("ViewPort", imgui.flag.ImGuiWindowFlags.NoMove | imgui.flag.ImGuiWindowFlags.NoResize | imgui.flag.ImGuiWindowFlags.NoTitleBar |imgui.flag.ImGuiWindowFlags.NoCollapse);
            ImGui.getWindowDrawList().addImage(fbo.getRefractionTexture(), 0, 0, DisplayManager.getWidth(), DisplayManager.getHeight());

            ImGui.end();



            //camera.customViewMatrix = Maths.createViewMatrix(camera);
            if (firstGameFrame) {
                camera.setPosition(ecs.cameraEntity.getPosition());
                camera.setPitch(0);
                camera.setYaw(0);
                camera.setRoll(180);
                camera.rotate();
                camera.updateVectors();
                camera.refresh();


                ecs.cameraEntity.isRendered = false;
                for (Entity lightEntity : ecs.getAllEntities()) {
                    if (lightEntity.type.startsWith("E")) {
                        if (lightEntity.myLight != null) {
                            lightEntity.isRendered = false;
                        }
                    }
                }
                firstGameFrame = false;
            }

            // Running Scripts



            imgui.ImGui.render();
            DisplayManager.imGuiGl13.renderDrawData(ImGui.getDrawData());

            DisplayManager.updateImGui();
            DisplayManager.updateDisplay();
        }

        loader.cleanUp();

        renderer.cleanUp();
        imgui.ImGui.destroyContext();
        System.exit(0);
    }



    public static Entity createFast(Vector3f size, Vector3f pos, Vector3f rot) {
        ModelData newData = OBJFileLoader.loadOBJ("res/cube.obj");
        RawModel newModel = new Loader().loadToVAO(newData.getVertices(), newData.getTextureCoords(), newData.getNormals(), newData.getIndices());
        TexturedModel newTexmodel = new TexturedModel(newModel, new ModelTexture(new Loader().loadTexture("res/debugTexture.png")));
        return new Entity(newTexmodel, pos, rot.x, rot.y, rot.z, size, 0.0f);
    }


    
}
