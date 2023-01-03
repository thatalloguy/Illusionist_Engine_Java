package physics;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.linearmath.DebugDrawModes;
import com.bulletphysics.linearmath.IDebugDraw;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.linearmath.VectorUtil.*;

import javax.vecmath.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class debugDraw {
    // Store the debug mode as a field in the class
    private int debugMode;

    private List<BoxShape> shapeList = new ArrayList<>();

    private Vector3f lineColor = new Vector3f(0, 1, 0);

    public void setDebugMode(final int debugMode) {
        this.debugMode = debugMode;
    }

    public int getDebugMode() {
        return 0;
    }

    public void drawLine(Vector3f from, Vector3f to, Vector3f color) {
        // Set the line color
        glColor3f(color.x, color.y, color.z);

        // Draw the line
        glBegin(GL_LINES);
        glVertex3f(from.x, from.y, from.z);
        glVertex3f(to.x, to.y, to.z);
        glEnd();
    }

    public void drawContactPoint(Vector3f PointOnB, Vector3f normalOnB, float distance, int lifeTime, Vector3f color) {
        // Set the point color
        glColor3f(color.x, color.y, color.z);

        // Draw the point
        glBegin(GL_POINTS);
        glVertex3f(PointOnB.x, PointOnB.y, PointOnB.z);
        glEnd();
    }

    public void reportErrorWarning(String warningString) {
        System.err.println(warningString);
    }

    public void draw3dText(Vector3f location, String textString) {
        // Not implemented
    }
    public void drawAabb(Vector3f from, Vector3f to, Vector3f color) {
        // Set the AABB color
        glColor3f(color.x, color.y, color.z);

        // Draw the AABB lines
        glBegin(GL_LINES);
        glVertex3f(from.x, from.y, from.z);
        glVertex3f(to.x, from.y, from.z);
        glVertex3f(to.x, from.y, from.z);
        glVertex3f(to.x, from.y, to.z);
        glVertex3f(to.x, from.y, to.z);
        glVertex3f(from.x, from.y, to.z);
        glVertex3f(from.x, from.y, to.z);
        glVertex3f(from.x, from.y, from.z);
        glVertex3f(from.x, to.y, from.z);
        glVertex3f(to.x, to.y, from.z);
        glVertex3f(to.x, to.y, from.z);
        glVertex3f(to.x, to.y, to.z);
        glVertex3f(to.x, to.y, to.z);
        glVertex3f(from.x, to.y, to.z);
        glVertex3f(from.x, to.y, to.z);
        glVertex3f(from.x, to.y, from.z);
        glEnd();
    }

    public void drawTransform(Transform transform, float orthoLen) {
        // Set the transform color
        glColor3f(1, 0, 0);

        // Draw the x axis
        glBegin(GL_LINES);
        glVertex3f(transform.origin.x, transform.origin.y, transform.origin.z);
        glVertex3f(transform.origin.x + orthoLen, transform.origin.y, transform.origin.z);
        glEnd();

        // Set the transform color
        glColor3f(0, 1, 0);

        // Draw the y axis
        glBegin(GL_LINES);
        glVertex3f(transform.origin.x, transform.origin.y, transform.origin.z);
        glVertex3f(transform.origin.x, transform.origin.y + orthoLen, transform.origin.z);
        glEnd();

        // Set the transform color
        glColor3f(0, 0, 1);

        // Draw the z axis
        glBegin(GL_LINES);
        glVertex3f(transform.origin.x, transform.origin.y, transform.origin.z);
        glVertex3f(transform.origin.x, transform.origin.y, transform.origin.z + orthoLen);
        glEnd();
    }


    public void Render() {
        for (BoxShape shape : shapeList) {

            // Create a DebugRenderer object
            Vector3f dimensions = shape.getHalfExtentsWithMargin(new Vector3f());

            // Calculate the vertices of the collision shape
            Vector3f[] vertices = new Vector3f[8];
            vertices[0] = new Vector3f(-dimensions.x, dimensions.y, dimensions.z);
            vertices[1] = new Vector3f(dimensions.x, dimensions.y, dimensions.z);
            vertices[2] = new Vector3f(dimensions.x, dimensions.y, -dimensions.z);
            vertices[3] = new Vector3f(-dimensions.x, dimensions.y, -dimensions.z);
            vertices[4] = new Vector3f(-dimensions.x, -dimensions.y, dimensions.z);
            vertices[5] = new Vector3f(dimensions.x, -dimensions.y, dimensions.z);
            vertices[6] = new Vector3f(dimensions.x, -dimensions.y, -dimensions.z);
            vertices[7] = new Vector3f(-dimensions.x, -dimensions.y, -dimensions.z);
            System.out.println("RENDERRR");

            // Set the line color
            Vector3f lineColor = new Vector3f(1, 0, 0);

            // Draw the vertices
            for (int i = 0; i < vertices.length; i++) {
                drawLine(vertices[i], vertices[(i + 1) % vertices.length], lineColor);
            }
        }
    }

    public void addBoxShape(BoxShape shape) {
        shapeList.add(shape);
    }

    public void removeBoxShape(BoxShape shape) {
        shapeList.remove(shape);
    }
}