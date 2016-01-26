package com.example.buecher.observatoireapplication;

/**
 * Created by Nicolas Buecher on 26/01/2016.
 */
public final class Shaders
{
    /**
     public static final String vertexShader =
     "uniform mat4 u_Model;          \n"
     + "uniform mat4 u_MVPMatrix;      \n"
     + "uniform mat4 u_MVMatrix;       \n"
     + "uniform vec3 u_LightPos;       \n"
     + "                               \n"
     + "attribute vec4 a_Position;     \n"
     + "attribute vec4 a_Color;        \n"
     + "attribute vec3 a_Normal;       \n"
     + "                               \n"
     + "varying vec4 v_Color;          \n"
     + "varying vec3 v_Grid;           \n"
     + "                               \n"
     + "void main() {                  \n"
     + "   v_Grid = vec3(u_Model * a_Position);                                  \n"
     + "                                                                         \n"
     + "   vec3 modelViewVertex = vec3(u_MVMatrix * a_Position);                 \n"
     + "   vec3 modelViewNormal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));        \n"
     + "                                                                         \n"
     + "   float distance = length(u_LightPos - modelViewVertex);                \n"
     + "   vec3 lightVector = normalize(u_LightPos - modelViewVertex);           \n"
     + "   float diffuse = max(dot(modelViewNormal, lightVector), 0.5);          \n"
     + "                                                                         \n"
     + "   diffuse = diffuse * (1.0 / (1.0 + (0.00001 * distance * distance)));  \n"
     + "   v_Color = a_Color * diffuse;                                          \n"
     + "   gl_Position = u_MVPMatrix * a_Position;                               \n"
     + "}";
     */
    public static final String vertexShader =
            "uniform mat4 u_MVPMatrix;      \n"		// A constant representing the combined model/view/projection matrix.
                    + "                               \n"
                    + "attribute vec4 a_Position;     \n"		// Per-vertex position information we will pass in.
                    + "attribute vec4 a_Color;        \n"		// Per-vertex color information we will pass in.
                    + "                               \n"
                    + "varying vec4 v_Color;          \n"		// This will be passed into the fragment shader.
                    + "                               \n"
                    + "void main()                    \n"		// The entry point for our vertex shader.
                    + "{                              \n"
                    + "   v_Color = a_Color;          \n"		// Pass the color through to the fragment shader.
                    + "                               \n"       // It will be interpolated across the triangle.
                    + "   gl_Position = u_MVPMatrix * a_Position;\n" 	    // gl_Position is a special variable used to store the final position.
                    + "                               \n"                   // Multiply the vertex by the matrix to get the final point in
                    + "}                              ";        // normalized screen coordinates.

    public static final String fragmentShader =
            "precision mediump float;       \n"		// Set the default precision to medium. We don't need as high of a
                    + "                               \n"       // precision in the fragment shader.
                    + "varying vec4 v_Color;          \n"		// This is the color from the vertex shader interpolated across the
                    + "                               \n"       // triangle per fragment.
                    + "void main()                    \n"		// The entry point for our fragment shader.
                    + "{                              \n"
                    + "   gl_FragColor = v_Color;     \n"		// Pass the color directly through the pipeline.
                    + "}                              \n";
}
