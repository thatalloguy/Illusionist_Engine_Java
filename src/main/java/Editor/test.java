package Editor;

import toolbox.Utils;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

public class test {
    public static void main(String[] args) {


        Vector3f rot = new Vector3f(4f, 12.3f, 0.2f);

        Quat4f or = new Quat4f(0, 0, 0,0 );//Utils.vector3fToQuat4f(rot);



        Vector3f xyz = Utils.quat4fToVector3f(or);
        System.out.println(or.x + " | " + xyz.x + " | " + or.z + "  |  " + or.w);
    }
}
