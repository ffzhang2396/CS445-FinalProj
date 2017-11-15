package finalproject;

/***************************************************************
* file: CameraController.java
* authors: Kristin Adachi
*          Je'Don Roc Carter
*          Calvin Teng
*          Felix Zhang
*          Oscar Zhang
* class: CS 445 – Computer Graphics
*
* assignment: Final Program
* date last modified: 11/15/2017
*
* purpose: The CameraController class represents our first-person camera 
*          for our world. This class handles the movements of the camera in 
*          accordance to the user input.
*
****************************************************************/ 

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.opengl.GL11.*;

public class CameraController {

    private Vector3f position = null;    //3d vector to store camera position
    private float yaw = 0.0f;            //Rotation around y axis of camera
    private float pitch = 0.0f;          //Rotations around x axis of camera
    private Chunk chunk;
    private boolean firstRender;
    
    //method: CameraController
    //purpose: CameraController constructor.
    public CameraController(float x, float y, float z) {
        this.position = new Vector3f(x, y, z);
        firstRender = true;
    }

    //method: yaw
    //purpose: Allows for camera rotation about the y axis (horizontal 
    //         camera rotation).
    public void yaw(float amount) {
        this.yaw += amount;
    }

    //method: pitch
    //purpose: Allows for camera rotation about the x axis (vertical 
    //         camera rotation).
    public void pitch(float amount) {
        this.pitch -= amount;
    }

    //method: walkForward
    //purpose: Allows for forward camera movement.
    public void walkForward(float distance) {
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw));
        this.position.x -= xOffset;
        this.position.z += zOffset;
    }

    //method: walkBackwards
    //purpose: Allows for backwards camera movement.
    public void walkBackwards(float distance) {
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw));
        position.x += xOffset;
        position.z -= zOffset;
    }

    //method: strafeLeft
    //purpose: Allows for leftward camera movement.
    public void strafeLeft(float distance) {
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw - 90));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw - 90));
        this.position.x -= xOffset;
        this.position.z += zOffset;
    }

    //method: strafeRight
    //purpose: Allows for rightward camera movement.
    public void strafeRight(float distance) {
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw + 90));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw + 90));
        this.position.x -= xOffset;
        this.position.z += zOffset;
    }

    //method: moveUp
    //purpose: Allows for upward camera movement.
    public void moveUp(float distance) {
        this.position.y -= distance;
    }

    //method: moveDown
    //purpose: Allows for downward camera movement.
    public void moveDown(float distance) {
        this.position.y += distance;
    }

    
    //method: lookThrough
    //purpose: Translates and rotates the matrix so that it looks through 
    //         the camera.
    public void lookThrough() {
        //Rotates the pitch around the X axis
        glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        //Rotates the yaw around the Y axis
        glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        //Translates to the position vector's location
        glTranslatef(this.position.x, this.position.y, this.position.z);
    }

    //method: loop
    //purpose: Essential loop of the program that calls all camera movement 
    //         functions. Sets and maintains values including the distance 
    //         of the movement, movement speed, and mouse sensitivity. The 
    //         mouse is locked in the window and the cursor is removed. 
    //         Rendering also occurs in this method. The window is closed 
    //         when ESC is pressed or when the "x" button on the window is 
    //         clicked.
    public void loop() {
        CameraController camera = new CameraController(0, 0, 0);
        float dx, dy;
        float dt = 0.0f, lastTime = 0.0f;
        long time = 0;
        float mouseSens = 0.09f;
        float movementSpeed = .35f;
        Mouse.setGrabbed(true); //Hides mouse

        while (!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !Display.isCloseRequested()) {
            time = Sys.getTime();
            lastTime = time;

            dx = Mouse.getDX();
            dy = Mouse.getDY();
            
            //Camera movement control using the mouse
            camera.yaw(dx * mouseSens);
            camera.pitch(dy * mouseSens);
            //Press "W" to move forward
            if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
                camera.walkForward(movementSpeed);
            }
            //Press "S" to move backwards
            if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
                camera.walkBackwards(movementSpeed);
            }
            //Press "A" to move leftward
            if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
                camera.strafeLeft(movementSpeed);
            }
            //Press "D" to move rightward
            if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
                camera.strafeRight(movementSpeed);
            }
            //Press "SPACE" to move upward
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                camera.moveUp(movementSpeed);
            }
            //Press "LSHIFT" to move downward
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                camera.moveDown(movementSpeed);
            }
            glLoadIdentity();
            camera.lookThrough();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            try {
                //For the first render, the chunk is generated
                if( firstRender ){
                    chunk = new Chunk( 0, 8, 0 );
                    firstRender = false;
                }
                chunk.render();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Display.update();
            Display.sync(60);
        }
        Display.destroy();
    }

}
