package com.example.buecher.observatoireapplication;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;

/**
 * Created by Nicolas Buecher on 26/01/2016.
 */
public class CardboardRenderer implements CardboardView.StereoRenderer
{
    /** Constants definition **/

    // Number of Buffer Objects to generate
    private final int NB_BUFFER_OBJECTS = 4;
    // Array of references to Buffer Objects
    private final int[] bufferObjects = new int[NB_BUFFER_OBJECTS];


    // MainActivity context to load textures from raws
    private final Context context;


    // A geometric sphere
    private Sphere sphere;

    // Point light position (used to hold a light centered on the origin in model space. We need a 4th coordinate so we can get translations to work.)
    private float[] lightPosInModelSpace;

    // point light position in eye space (camera space)
    private float[] lightPosInEyeSpace;

    // Cube buffers (FOR TESTS, will be deleted)
    private FloatBuffer cubeVertices;
    private FloatBuffer cubeColors;
    private FloatBuffer cubeNormals;
    private FloatBuffer cubeTexCoords;

    // Transformation Matrices
    private float[] modelMatrix;
    private float[] cameraMatrix;
    private float[] viewMatrix;
    private float[] projectionMatrix;
    private float[] MVPMatrix;

    // Handlers for the basic program and its uniforms / attributes
    private int basicProgramHandle;
    private int basicProgramMVPMatrixHandle;
    private int basicProgramPositionHandle;

    // Handlers for the cube program and its uniforms / attributes (FOR TESTS, will be deleted)
    private int cubeProgramHandle;
    private int cubeProgramMVPMatrixHandle;
    private int cubeProgramMVMatrixHandle;
    private int cubeProgramTextureHandle;
    private int cubeProgramLightPosHandle;
    private int cubeProgramPositionHandle;
    private int cubeProgramColorHandle;
    private int cubeProgramNormalHandle;
    private int cubeProgramTexCoordsHandle;

    // Handlers for the sphere program and its uniforms / attributes
    private int sphereProgramHandle;
    private int sphereProgramMVPMatrixHandle;
    private int sphereProgramMVMatrixHandle;
    private int sphereProgramTextureHandle;
    private int sphereProgramLightPosHandle;
    private int sphereProgramPositionHandle;
    private int sphereProgramNormalHandle;
    private int sphereProgramTexCoordsHandle;

    // Handlers for the point program and its uniforms / attributes
    private int pointProgramHandle;
    private int pointProgramMVPMatrixHandle;
    private int pointProgramPositionHandle;

    // Handler for the texture
    private int textureHandle;

    public CardboardRenderer(final Context activityContext)
    {
        // Get the MainActivity context
        context = activityContext;

        // Initialize matrices and vectors at the beginning of the application
        modelMatrix = new float[16];
        cameraMatrix = new float[16];
        viewMatrix = new float[16];
        projectionMatrix = new float[16];
        MVPMatrix = new float[16];
        lightPosInEyeSpace = new float[4];

        // Initialize the light position
        lightPosInModelSpace = new float[] {0.0f, 0.0f, 0.0f, 1.0f};
    }

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig)
    {
        // Set up clear color to medium gray
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);


        /** Set up buffers **/

        // Initialize a sphere of radius 5 and its buffers (do it in onSurfaceCreated() method in order to be sure to be in OpenGL thread)
        sphere = new Sphere(15, 15, 1.0f);

        // Allocate memory for cube buffers (FOR TESTS, will be deleted)
        cubeVertices = ByteBuffer.allocateDirect(WorldLayoutData.CUBE_COORDS.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        cubeColors = ByteBuffer.allocateDirect(WorldLayoutData.CUBE_COLORS.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        cubeNormals = ByteBuffer.allocateDirect(WorldLayoutData.CUBE_NORMALS.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        cubeTexCoords = ByteBuffer.allocateDirect(WorldLayoutData.CUBE_TEXCOORDS.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();

        // Fill the cube buffers (FOR TESTS, will be deleted)
        cubeVertices.put(WorldLayoutData.CUBE_COORDS).position(0);
        cubeColors.put(WorldLayoutData.CUBE_COLORS).position(0);
        cubeNormals.put(WorldLayoutData.CUBE_NORMALS).position(0);
        cubeTexCoords.put(WorldLayoutData.CUBE_TEXCOORDS).position(0);

        // Generate 2 buffer objects
        GLES20.glGenBuffers(4, bufferObjects, 0);

        // Vertex Buffer Object
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferObjects[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, sphere.getVertexBuffer().capacity() * Util.BYTES_PER_FLOAT, sphere.getVertexBuffer(), GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        // Normal Buffer Object
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferObjects[1]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, sphere.getNormalBuffer().capacity() * Util.BYTES_PER_FLOAT, sphere.getNormalBuffer(), GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        // Texture Coordinates Buffer Object
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferObjects[2]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, sphere.getTextureBuffer().capacity() * Util.BYTES_PER_FLOAT, sphere.getTextureBuffer(), GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        // Index Buffer Object
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, bufferObjects[3]);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, sphere.getIndexBuffer().capacity() * Util.BYTES_PER_SHORT, sphere.getIndexBuffer(), GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);


        /** Shaders & Programs **/

        // Create shaders and keep their references
        int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);


        // Load and compile basic shaders
        Util.loadGLShader(vertexShaderHandle, Shaders.basicVertexShader);
        Util.loadGLShader(fragmentShaderHandle, Shaders.basicFragmentShader);

        // Create and link basic program
        basicProgramHandle = Util.createProgram(vertexShaderHandle, fragmentShaderHandle);

        // Get the position of in variables of the basic program
        basicProgramMVPMatrixHandle = GLES20.glGetUniformLocation(basicProgramHandle, "u_MVPMatrix");
        basicProgramPositionHandle = GLES20.glGetAttribLocation(basicProgramHandle, "a_Position");


        // Load and compile cube shaders (FOR TESTS, will be deleted)
        Util.loadGLShader(vertexShaderHandle, Shaders.cubeTextureVertexShader);
        Util.loadGLShader(fragmentShaderHandle, Shaders.cubeTextureFragmentShader);

        // Create and link cube program (FOR TESTS, will be deleted)
        cubeProgramHandle = Util.createProgram(vertexShaderHandle, fragmentShaderHandle);

        // Get the position of in variables of the cube program (FOR TESTS, will be deleted)
        cubeProgramMVPMatrixHandle = GLES20.glGetUniformLocation(cubeProgramHandle, "u_MVPMatrix");
        cubeProgramMVMatrixHandle = GLES20.glGetUniformLocation(cubeProgramHandle, "u_MVMatrix");
        cubeProgramTextureHandle = GLES20.glGetUniformLocation(cubeProgramHandle, "u_Texture");
        cubeProgramLightPosHandle = GLES20.glGetUniformLocation(cubeProgramHandle, "u_LightPos");
        cubeProgramPositionHandle = GLES20.glGetAttribLocation(cubeProgramHandle, "a_Position");
        cubeProgramColorHandle = GLES20.glGetAttribLocation(cubeProgramHandle, "a_Color");
        cubeProgramNormalHandle = GLES20.glGetAttribLocation(cubeProgramHandle, "a_Normal");
        cubeProgramTexCoordsHandle = GLES20.glGetAttribLocation(cubeProgramHandle, "a_TexCoordinate");


        // Load and compile sphere shaders
        Util.loadGLShader(vertexShaderHandle, Shaders.sphereTextureVertexShader);
        Util.loadGLShader(fragmentShaderHandle, Shaders.sphereTextureFragmentShader);

        // Create and link sphere program
        sphereProgramHandle = Util.createProgram(vertexShaderHandle, fragmentShaderHandle);

        // Get the position of in variables of the sphere program
        sphereProgramMVPMatrixHandle = GLES20.glGetUniformLocation(sphereProgramHandle, "u_MVPMatrix");
        sphereProgramMVMatrixHandle = GLES20.glGetUniformLocation(sphereProgramHandle, "u_MVMatrix");
        sphereProgramTextureHandle = GLES20.glGetUniformLocation(sphereProgramHandle, "u_Texture");
        sphereProgramLightPosHandle = GLES20.glGetUniformLocation(sphereProgramHandle, "u_LightPos");
        sphereProgramPositionHandle = GLES20.glGetAttribLocation(sphereProgramHandle, "a_Position");
        sphereProgramNormalHandle = GLES20.glGetAttribLocation(sphereProgramHandle, "a_Normal");
        sphereProgramTexCoordsHandle = GLES20.glGetAttribLocation(sphereProgramHandle, "a_TexCoordinate");


        // Load and compile point light shaders
        Util.loadGLShader(vertexShaderHandle, Shaders.pointVertexShader);
        Util.loadGLShader(fragmentShaderHandle, Shaders.pointFragmentShader);

        // Create and link point light program
        pointProgramHandle = Util.createProgram(vertexShaderHandle, fragmentShaderHandle);

        // Get the position of in variables of the point light program
        pointProgramMVPMatrixHandle = GLES20.glGetUniformLocation(pointProgramHandle, "u_MVPMatrix");
        pointProgramPositionHandle = GLES20.glGetAttribLocation(pointProgramHandle, "a_Position");

        // NOTE : We could put "glGetUniformLocation" and "glGetAttribLocation" methods in "drawObject" methods and stop using global variables like in tutorials
        // NOTE 2 : But if we do, they will be called every frame. Bad idea ?

        // Load the texture
        textureHandle = Util.loadTexture(context, R.drawable.sky_test_pic);
    }

    @Override
    public void onSurfaceChanged(int width, int height)
    {
        // Should not happen in Cardboard application ?
    }

    @Override
    public void onNewFrame(HeadTransform headTransform)
    {
        // Set up cameraMatrix just behind the origin
        Matrix.setLookAtM(cameraMatrix, 0, 0.0f, 0.0f, 0.01f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
    }

    @Override
    public void onDrawEye(Eye eye)
    {
        /** Set up OpenGL Flags **/

        // Enable depth testing, draw closest objects first for optimization
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // Use culling to remove back faces (delete it if inside the sphere ? or create sphere using clockwise vertices ?)
        GLES20.glEnable(GLES20.GL_CULL_FACE);


        // Clear buffers
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);


        // Calculate the angle in order to do a complete rotation every 10 seconds
        long time = SystemClock.uptimeMillis() % 10000L;
        float angle = (360.0f / 10000.0f) * ((int) time);


        // Set up viewMatrix
        Matrix.multiplyMM(viewMatrix, 0, eye.getEyeView(), 0, cameraMatrix, 0);

        // Set up projectionMatrix
        projectionMatrix = eye.getPerspective(0.1f, 100.0f);

        // Set up the modelMatrix for the light
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.rotateM(modelMatrix, 0, angle, 0.0f, 1.0f, 0.0f);
        //Matrix.translateM(modelMatrix, 0, 1.5f, -2.0f, 0.0f);
        Matrix.translateM(modelMatrix, 0, 0.9f, 0.0f, 0.0f);

        // Calculate the light position in eye space
        Matrix.multiplyMV(lightPosInEyeSpace, 0, modelMatrix, 0, lightPosInModelSpace, 0);
        Matrix.multiplyMV(lightPosInEyeSpace, 0, viewMatrix, 0, lightPosInEyeSpace, 0);

        // Draw the point light with all previous parameters
        drawLight();


        // Set up modelMatrix for the cube (FOR TESTS, will be deleted)
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, 0.0f, 0.0f, -3.0f);
        Matrix.rotateM(modelMatrix, 0, 45.0f, 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(modelMatrix, 0, 45.0f, 1.0f, 0.0f, 0.0f);
        Matrix.rotateM(modelMatrix, 0, angle, 0.0f, 0.0f, 1.0f);

        // Draw cube with all previous parameters (FOR TESTS, will be deleted)
        drawCube();

        // Set up modelMatrix for the sphere
        Matrix.setIdentityM(modelMatrix, 0);
        //Matrix.translateM(modelMatrix, 0, 0.0f, -2.0f, 0.0f);
        //Matrix.rotateM(modelMatrix, 0, angle, 0.5f, 0.5f, 1.0f);

        // Draw sphere with all previous parameters
        drawSphere();
    }

    @Override
    public void onFinishFrame(Viewport viewport)
    {

    }

    @Override
    public void onRendererShutdown()
    {

    }

    // FOR TESTS, will be deleted
    private void drawCube()
    {
        GLES20.glUseProgram(cubeProgramHandle);


        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle);

        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(cubeProgramTextureHandle, 0);


        // Calculate MVMatrix
        Matrix.multiplyMM(MVPMatrix, 0, viewMatrix, 0, modelMatrix, 0);

        // Pass in MVMatrix to the program
        GLES20.glUniformMatrix4fv(cubeProgramMVMatrixHandle, 1, false, MVPMatrix, 0);

        // Calculate MVPMatrix
        Matrix.multiplyMM(MVPMatrix, 0, projectionMatrix, 0, MVPMatrix, 0);

        // Pass in MVPMatrix to the program
        GLES20.glUniformMatrix4fv(cubeProgramMVPMatrixHandle, 1, false, MVPMatrix, 0);


        // Pass in light position to the program
        GLES20.glUniform3f(cubeProgramLightPosHandle, lightPosInEyeSpace[0], lightPosInEyeSpace[1], lightPosInEyeSpace[2]);


        // Pass in cubeVertices to the program
        GLES20.glVertexAttribPointer(cubeProgramPositionHandle, 3, GLES20.GL_FLOAT, false, 0, cubeVertices);
        GLES20.glEnableVertexAttribArray(cubeProgramPositionHandle);

        // Pass in cubeColors to the program
        GLES20.glVertexAttribPointer(cubeProgramColorHandle, 4, GLES20.GL_FLOAT, false, 0, cubeColors);
        GLES20.glEnableVertexAttribArray(cubeProgramColorHandle);

        // Pass in cubeNormals to the program
        GLES20.glVertexAttribPointer(cubeProgramNormalHandle, 3, GLES20.GL_FLOAT, false, 0, cubeNormals);
        GLES20.glEnableVertexAttribArray(cubeProgramNormalHandle);

        // Pass in cubeTexCoords to the program
        GLES20.glVertexAttribPointer(cubeProgramTexCoordsHandle, 2, GLES20.GL_FLOAT, false, 0, cubeTexCoords);
        GLES20.glEnableVertexAttribArray(cubeProgramTexCoordsHandle);

        // DRAW
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);
    }

    private void drawSphere()
    {
        GLES20.glUseProgram(sphereProgramHandle);


        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle);

        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(sphereProgramTextureHandle, 0);


        // Calculate MVMatrix
        Matrix.multiplyMM(MVPMatrix, 0, viewMatrix, 0, modelMatrix, 0);

        // Pass in MVMatrix to the program
        GLES20.glUniformMatrix4fv(sphereProgramMVMatrixHandle, 1, false, MVPMatrix, 0);

        // Calculate MVPMatrix
        Matrix.multiplyMM(MVPMatrix, 0, projectionMatrix, 0, MVPMatrix, 0);

        // Pass in MVPMatrix to the program
        GLES20.glUniformMatrix4fv(sphereProgramMVPMatrixHandle, 1, false, MVPMatrix, 0);


        // Pass in light position to the program
        GLES20.glUniform3f(sphereProgramLightPosHandle, lightPosInEyeSpace[0], lightPosInEyeSpace[1], lightPosInEyeSpace[2]);


        // Define VBO as current array buffer
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferObjects[0]);

        // Enable position attributes
        GLES20.glVertexAttribPointer(sphereProgramPositionHandle, 3, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(sphereProgramPositionHandle);

        // Define NBO as current array buffer
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferObjects[1]);

        // Enable normal attributes
        GLES20.glVertexAttribPointer(sphereProgramNormalHandle, 3, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(sphereProgramNormalHandle);

        // Define TCBO as current array buffer
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferObjects[2]);

        // Enable texture coordinates attributes
        GLES20.glVertexAttribPointer(sphereProgramTexCoordsHandle, 2, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(sphereProgramTexCoordsHandle);

        // Define IBO as current buffer
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, bufferObjects[3]);

        // Draw sphere
        GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, sphere.getNbVertices(), GLES20.GL_UNSIGNED_SHORT, 0);

        // Unbind buffers
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    private void drawLight()
    {
        GLES20.glUseProgram(pointProgramHandle);

        // Calculate MVPMatrix
        this.computeMVPMatrix();

        // Pass in MVPMatrix to the program
        GLES20.glUniformMatrix4fv(pointProgramMVPMatrixHandle, 1, false, MVPMatrix, 0);

        // Pass in the position.
        GLES20.glVertexAttrib3f(pointProgramPositionHandle, lightPosInModelSpace[0], lightPosInModelSpace[1], lightPosInModelSpace[2]);

        // Since we are not using a buffer object, disable vertex arrays for this attribute.
        GLES20.glDisableVertexAttribArray(pointProgramPositionHandle);

        // Draw the point
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
    }

    private void computeMVPMatrix()
    {
        Matrix.multiplyMM(MVPMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(MVPMatrix, 0, projectionMatrix, 0, MVPMatrix, 0);
    }
}
