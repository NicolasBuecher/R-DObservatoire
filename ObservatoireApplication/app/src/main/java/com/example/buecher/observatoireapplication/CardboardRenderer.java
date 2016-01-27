package com.example.buecher.observatoireapplication;

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
    private final int NB_BUFFER_OBJECTS = 2;

    private final int[] bufferObjects = new int[NB_BUFFER_OBJECTS];

    private Sphere sphere;

    private FloatBuffer cubeVertices;
    private FloatBuffer cubeColors;

    private float[] modelMatrix;
    private float[] cameraMatrix;
    private float[] viewMatrix;
    private float[] projectionMatrix;
    private float[] MVPMatrix;

    private int programHandle;

    private int MVPMatrixHandle;
    private int positionHandle;
    private int colorHandle;

    public CardboardRenderer()
    {
        // Matrix initialization at the beginning of the application
        modelMatrix = new float[16];
        cameraMatrix = new float[16];
        viewMatrix = new float[16];
        projectionMatrix = new float[16];
        MVPMatrix = new float[16];

        // Initialization of a sphere of radius 5
        sphere = new Sphere(15, 15, 1.0f);
    }

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig)
    {
        // Set up default Color
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);

        // Buffer memory allocation
        cubeVertices = ByteBuffer.allocateDirect(WorldLayoutData.CUBE_COORDS.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        cubeColors = ByteBuffer.allocateDirect(WorldLayoutData.CUBE_COLORS.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();

        // Fill the buffers
        cubeVertices.put(WorldLayoutData.CUBE_COORDS).position(0);
        cubeColors.put(WorldLayoutData.CUBE_COLORS).position(0);

        // Generate and fill buffer objects
        GLES20.glGenBuffers(2, bufferObjects, 0);

        // Vertex Buffer Object
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferObjects[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, sphere.getVertexBuffer().capacity() * Util.BYTES_PER_FLOAT, sphere.getVertexBuffer(), GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        // Index Buffer Object
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, bufferObjects[1]);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, sphere.getIndexBuffer().capacity() * Util.BYTES_PER_SHORT, sphere.getIndexBuffer(), GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);

        // Reference shaders
        int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

        // Shader loading and compiling
        Util.loadGLShader(vertexShaderHandle, Shaders.vertexShader);
        Util.loadGLShader(fragmentShaderHandle, Shaders.fragmentShader);

        // Reference program
        programHandle = GLES20.glCreateProgram();

        if (programHandle != 0)
        {
            // Link the shaders in the program
            GLES20.glAttachShader(programHandle, vertexShaderHandle);
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);

            GLES20.glLinkProgram(programHandle);

            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            if (linkStatus[0] == 0)
            {
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }

        if (programHandle == 0)
        {
            throw new RuntimeException("Error creating program.");
        }

        // Get the position of in variables of the program
        MVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        positionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        colorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");
    }

    @Override
    public void onSurfaceChanged(int width, int height)
    {

    }

    @Override
    public void onNewFrame(HeadTransform headTransform)
    {
        // Set up cameraMatrix
        Matrix.setLookAtM(cameraMatrix, 0, 0.0f, 0.0f, 0.01f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
    }

    @Override
    public void onDrawEye(Eye eye)
    {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        // Calculate the angle in order to do a complete rotation every 10 seconds
        long time = SystemClock.uptimeMillis() % 10000L;
        float angle = (360.0f / 10000.0f) * ((int) time);

        // Set up viewMatrix
        Matrix.multiplyMM(viewMatrix, 0, eye.getEyeView(), 0, cameraMatrix, 0);

        // Set up projectionMatrix
        projectionMatrix = eye.getPerspective(0.1f, 100.0f);

        // Set up modelMatrix for the cube
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, 0.0f, 0.0f, -3.0f);
        Matrix.rotateM(modelMatrix, 0, 45.0f, 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(modelMatrix, 0, 45.0f, 1.0f, 0.0f, 0.0f);
        Matrix.rotateM(modelMatrix, 0, angle, 0.0f, 0.0f, 1.0f);

        // Draw cube with all previous parameters, for tests only don't appear in the final application
        //drawCube();

        // Set up modelMatrix for the sphere
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, 0.0f, -3.0f, 0.0f);
        Matrix.rotateM(modelMatrix, 0, angle, 0.5f, 0.5f, 1.0f);

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

    // For tests only, don't appear in the final application
    private void drawCube()
    {
        GLES20.glUseProgram(programHandle);

        // Calculate MVPMatrix
        Matrix.multiplyMM(MVPMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(MVPMatrix, 0, projectionMatrix, 0, MVPMatrix, 0);

        // Pass in MVPMatrix to the program
        GLES20.glUniformMatrix4fv(MVPMatrixHandle, 1, false, MVPMatrix, 0);

        // Pass in cubeVertices to the program
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, cubeVertices);
        GLES20.glEnableVertexAttribArray(positionHandle);

        // Pass in cubeColors to the program
        GLES20.glVertexAttribPointer(colorHandle, 4, GLES20.GL_FLOAT, false, 0, cubeColors);
        GLES20.glEnableVertexAttribArray(colorHandle);

        // DRAW
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);
    }

    private void drawSphere()
    {
        GLES20.glUseProgram(programHandle);

        // Calculate MVPMatrix
        this.computeMVPMatrix();

        // Pass in MVPMatrix to the program
        GLES20.glUniformMatrix4fv(MVPMatrixHandle, 1, false, MVPMatrix, 0);

        // Define VBO as current array buffer
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferObjects[0]);

        // Enable position attributes
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(positionHandle);

        // Define IBO as current buffer
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, bufferObjects[1]);

        // Draw sphere
        GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, sphere.getNbVertices(), GLES20.GL_UNSIGNED_SHORT, 0);

        // Unbind buffers
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    private void computeMVPMatrix()
    {
        Matrix.multiplyMM(MVPMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(MVPMatrix, 0, projectionMatrix, 0, MVPMatrix, 0);
    }
}
