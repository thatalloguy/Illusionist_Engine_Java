package physics;

import Editor.Console;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CapsuleShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.character.KinematicCharacterController;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import components.DynamicBodyComponent;
import components.MaterialComponent;
import components.ShapeComponent;
import components.StaticBodyComponent;
import entities.Entity;
import org.lwjgl.Sys;
import toolbox.Maths;
import toolbox.Utils;


import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorldManager {
    private List<RigidBody> rigidBodyList = new ArrayList<>();
    private List<Transform> originalTransforms = new ArrayList<>();
    private List<Entity> entities = new ArrayList<>();
    private Vector3f GRAVITY_VECTOR = new Vector3f(0, -9.81f, 0);
    public DiscreteDynamicsWorld dynamicsWorld;

    public WorldManager() {
        CollisionDispatcher collisionDispatcher = new CollisionDispatcher(new DefaultCollisionConfiguration());
        BroadphaseInterface broadphase = new DbvtBroadphase();

        this.dynamicsWorld = new DiscreteDynamicsWorld(collisionDispatcher, broadphase, new SequentialImpulseConstraintSolver(), new DefaultCollisionConfiguration());
        dynamicsWorld = new DiscreteDynamicsWorld(collisionDispatcher, broadphase, new SequentialImpulseConstraintSolver(), new DefaultCollisionConfiguration());
        dynamicsWorld.setGravity(GRAVITY_VECTOR);
    }

    public void Step(float timeStep) {

        for (Entity entity : entities) {
            if (entity.ghostObject != null) {
                Transform transform = new Transform();
                entity.ghostObject.getWorldTransform(transform);
                transform.setRotation(entity.rigidBody.getOrientation(new Quat4f()));
                entity.rigidBody.setCenterOfMassTransform(transform);
            }
        }

        dynamicsWorld.stepSimulation(timeStep, 10);

        for (Entity entity : entities) {
            if (entity.freeze) {
                entity.rigidBody.setAngularFactor(0);
            }
            Vector3f position = new Vector3f();
            Transform transform = new Transform();
            entity.rigidBody.getCenterOfMassPosition(position);
            entity.rigidBody.getCenterOfMassTransform(transform);
            entity.position.x = position.x;
            entity.position.y = position.y;
            entity.position.z = position.z;
            Quat4f rot = new Quat4f();
            entity.rigidBody.getOrientation(rot);
            entity.rotateWithQuanternion(rot);
            if (entity.ghostObject != null) {
                Transform tan =  new Transform();
                tan.setIdentity();
                tan.origin.set(position);
                entity.ghostObject.setWorldTransform(transform);
            }
        }
    }

    public RigidBody createRigidBody(CollisionShape shape, float mass, Transform startTransform) { //}, Vector3f rotation) {
        // Calculate the local inertia of the shape
        Vector3f localInertia = new Vector3f(0, 0, 0);
        shape.calculateLocalInertia(mass, localInertia);


        DefaultMotionState motionState = new DefaultMotionState(startTransform);
        RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(mass, motionState, shape, localInertia);
        RigidBody rigidBody = new RigidBody(rbInfo);
        //rigidBody.setRo(Utils.toQuat4f(rotation));
        return rigidBody;
    }



    public void resetRigidBodyPositions() {
        // Check that the lists are the same size


        // Loop through all the rigid bodies and reset their position
        for (int i = 0; i < entities.size(); i++) {
            RigidBody body = entities.get(i).rigidBody;
            Transform originalTransform = originalTransforms.get(i);
            body.setCenterOfMassTransform(originalTransform);
            body.setLinearVelocity(new Vector3f(0, 0, 0));
            body.setAngularVelocity(new Vector3f(0, 0, 0));
            if (entities.get(i).ghostObject != null) {
                entities.get(i).ghostObject.setWorldTransform(originalTransform);
            }
        }
    }

    public void setupWorld() {
        originalTransforms.clear();
        for (Entity entity : entities) {
            RigidBody body = entity.rigidBody;
            Transform transform = new Transform();
            body.getCenterOfMassTransform(transform);
            originalTransforms.add(transform);
            if (entity.ghostObject != null) {
                entity.ghostObject.setWorldTransform(transform);
            }
        }
    }

    public void cleanupWorld() {
        resetRigidBodyPositions();
    }



    public void addPhysicsToEntity(Entity entity, ShapeComponent sc, DynamicBodyComponent DC, Console console) {
        sc.setupDebug();
        if (!entities.contains(entity)) {
            entities.add(entity);
            entity.rigidBody = createRigidBody(sc.getShape(), DC.massBuf.get(), new Transform(new Matrix4f(Maths.rotateQuat4f(new org.lwjgl.util.vector.Vector3f(entity.rotX, entity.rotY, entity.rotZ), new Quat4f()), new javax.vecmath.Vector3f(entity.position.x, entity.position.y, entity.position.z), 1.0f)));
            dynamicsWorld.addRigidBody(entity.rigidBody);

        } else {
            entities.remove(entity);
            entities.add(entity);
            dynamicsWorld.removeRigidBody(entity.rigidBody);
            entity.rigidBody = createRigidBody(sc.getShape(), DC.massBuf.get(), new Transform(new Matrix4f(Utils.vector3fToQuat4f(new Vector3f(entity.rotX, entity.rotY, entity.rotZ)), new javax.vecmath.Vector3f(entity.position.x, entity.position.y, entity.position.z), 1.0f)));
            dynamicsWorld.addRigidBody(entity.rigidBody);

        }
    }

}
