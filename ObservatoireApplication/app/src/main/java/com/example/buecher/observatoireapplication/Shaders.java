package com.example.buecher.observatoireapplication;


/**
 * Created by Nicolas Buecher on 26/01/2016.
 */
public final class Shaders
{
    // Per-pixel diffuse and ambient lighting, hard code red color, add textures
    public static final String sphereTextureFragmentShader =
          "precision mediump float;         \n"     // Set the default precision to medium. We don't need as high of a
        + "                                 \n"     // precision in the fragment shader.
        + "uniform vec3 u_LightPos;         \n"     // The position of the light in eye space.
        + "                                 \n"
        + "varying vec3 v_Position;         \n"     // Interpolated position for this fragment.
        + "varying vec3 v_Normal;           \n"     // Interpolated normal for this fragment.
        + "                                 \n"
        + "void main()                      \n"     // The entry point for our vertex shader.
        + "{                                \n"
        + "     float distance = length(u_LightPos - v_Position);                   \n" // Will be used for attenuation.
        + "                                                                         \n"
        + "     vec3 lightVector = normalize(u_LightPos - v_Position);              \n" // Get a lighting direction vector from the light to the vertex.
        + "                                                                         \n"
        + "     float diffuse = max(dot(v_Normal, lightVector), 0.0);               \n" // Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
        + "                                                                         \n" // pointing in the same direction then it will get max illumination.
        + "     diffuse = diffuse * (1.0 / (1.0 + (0.5 * distance * distance))) + 0.5;   \n" // Attenuate the light based on distance. We scale the square of the distance by 0.25 to dampen the attenuation effect, and we also add 1.0 to the modified distance so that we don't get oversaturation when the light is very close to an object.
        + "                                                                         \n"      // Ambient light of 0.5
        + "                                                                         \n"
        + "     gl_FragColor = vec4(1.0f, 0.0f, 0.0f, 1.0f) * diffuse;              \n" // Multiply the color by the illumination level. It will be interpolated across the triangle.
        + "}                                                                        ";

    // Per-pixel diffuse & ambient lighting, hard code red color, add textures
    public static final String sphereTextureVertexShader =
          "uniform mat4 u_MVPMatrix;        \n"     // A constant representing the combined model/view/projection matrix.
        + "uniform mat4 u_MVMatrix;         \n"     // A constant representing the combined model/view matrix.
        + "                                 \n"
        + "attribute vec4 a_Position;       \n"     // Per-vertex position information we will pass in.
        + "attribute vec3 a_Normal;         \n"     // Per-vertex normal information we will pass in.
        + "                                 \n"
        + "varying vec3 v_Position;         \n"     // This will be passed into the fragment shader.
        + "varying vec3 v_Normal;           \n"     // This will be passed into the fragment shader
        + "                                 \n"
        + "void main()                      \n"     // The entry point for our vertex shader.
        + "{                                \n"
        + "     v_Position = vec3(u_MVMatrix * a_Position);         \n" // Transform the vertex into eye space.
        + "                                                         \n"
        + "     v_Normal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));  \n" // Transform the normal's orientation into eye space.
        + "                                                         \n"
        + "     gl_Position = u_MVPMatrix * a_Position;             \n" // Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
        + "}                                                                        ";


    // Per-pixel diffuse and ambient lighting, hard code red color
    public static final String spherePerPixelFragmentShader =
          "precision mediump float;         \n"     // Set the default precision to medium. We don't need as high of a
        + "                                 \n"     // precision in the fragment shader.
        + "uniform vec3 u_LightPos;         \n"     // The position of the light in eye space.
        + "                                 \n"
        + "varying vec3 v_Position;         \n"     // Interpolated position for this fragment.
        + "varying vec3 v_Normal;           \n"     // Interpolated normal for this fragment.
        + "                                 \n"
        + "void main()                      \n"     // The entry point for our vertex shader.
        + "{                                \n"
        + "     float distance = length(u_LightPos - v_Position);                   \n" // Will be used for attenuation.
        + "                                                                         \n"
        + "     vec3 lightVector = normalize(u_LightPos - v_Position);              \n" // Get a lighting direction vector from the light to the vertex.
        + "                                                                         \n"
        + "     float diffuse = max(dot(v_Normal, lightVector), 0.0);               \n" // Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
        + "                                                                         \n" // pointing in the same direction then it will get max illumination.
        + "     diffuse = diffuse * (1.0 / (1.0 + (0.5 * distance * distance))) + 0.5;   \n" // Attenuate the light based on distance. We scale the square of the distance by 0.25 to dampen the attenuation effect, and we also add 1.0 to the modified distance so that we don't get oversaturation when the light is very close to an object.
        + "                                                                         \n"      // Ambient light of 0.5
        + "                                                                         \n"
        + "     gl_FragColor = vec4(1.0f, 0.0f, 0.0f, 1.0f) * diffuse;              \n" // Multiply the color by the illumination level. It will be interpolated across the triangle.
        + "}                                                                        ";

    // Per-pixel diffuse & ambient lighting, hard code red color
    public static final String spherePerPixelVertexShader =
          "uniform mat4 u_MVPMatrix;        \n"     // A constant representing the combined model/view/projection matrix.
        + "uniform mat4 u_MVMatrix;         \n"     // A constant representing the combined model/view matrix.
        + "                                 \n"
        + "attribute vec4 a_Position;       \n"     // Per-vertex position information we will pass in.
        + "attribute vec3 a_Normal;         \n"     // Per-vertex normal information we will pass in.
        + "                                 \n"
        + "varying vec3 v_Position;         \n"     // This will be passed into the fragment shader.
        + "varying vec3 v_Normal;           \n"     // This will be passed into the fragment shader
        + "                                 \n"
        + "void main()                      \n"     // The entry point for our vertex shader.
        + "{                                \n"
        + "     v_Position = vec3(u_MVMatrix * a_Position);         \n" // Transform the vertex into eye space.
        + "                                                         \n"
        + "     v_Normal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));  \n" // Transform the normal's orientation into eye space.
        + "                                                         \n"
        + "     gl_Position = u_MVPMatrix * a_Position;             \n" // Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
        + "}                                                                        ";

    // Per-vertex diffuse and ambient lighting, using material color (FOR TESTS, will be deleted)
    public static final String cubeVertexShader =
          "uniform mat4 u_MVPMatrix;        \n"     // A constant representing the combined model/view/projection matrix.
        + "uniform mat4 u_MVMatrix;         \n"     // A constant representing the combined model/view matrix.
        + "uniform vec3 u_LightPos;         \n"     // The position of the light in eye space.
        + "                                 \n"
        + "attribute vec4 a_Position;       \n"     // Per-vertex position information we will pass in.
        + "attribute vec4 a_Color;          \n"
        + "attribute vec3 a_Normal;         \n"     // Per-vertex normal information we will pass in.
        + "                                 \n"
        + "varying vec4 v_Color;            \n"     // This will be passed into the fragment shader.
        + "                                 \n"
        + "void main()                      \n"     // The entry point for our vertex shader.
        + "{                                \n"
        + "     vec3 modelViewVertex = vec3(u_MVMatrix * a_Position);               \n" // Transform the vertex into eye space.
        + "                                                                         \n"
        + "     vec3 modelViewNormal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));      \n" // Transform the normal's orientation into eye space.
        + "                                                                         \n"
        + "     float distance = length(u_LightPos - modelViewVertex);              \n" // Will be used for attenuation.
        + "                                                                         \n"
        + "     vec3 lightVector = normalize(u_LightPos - modelViewVertex);         \n" // Get a lighting direction vector from the light to the vertex.
        + "                                                                         \n"
        + "     float diffuse = max(dot(modelViewNormal, lightVector), 0.0);        \n" // Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
        + "                                                                         \n" // pointing in the same direction then it will get max illumination.
        + "     diffuse = diffuse * (1.0 / (1.0 + (0.5 * distance * distance))) + 0.5;   \n" // Attenuate the light based on distance.
        + "                                                                         \n"
        + "                                                                         \n"
        + "     v_Color = a_Color * diffuse;                                        \n" // Multiply the color by the illumination level. It will be interpolated across the triangle.
        + "                                                                         \n"
        + "     gl_Position = u_MVPMatrix * a_Position;                             \n" // Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
        + "}                                                                        ";

    // Per-vertex diffuse and ambient lighting, hard code red color
    public static final String sphereVertexShader =
          "uniform mat4 u_MVPMatrix;        \n"     // A constant representing the combined model/view/projection matrix.
        + "uniform mat4 u_MVMatrix;         \n"     // A constant representing the combined model/view matrix.
        + "uniform vec3 u_LightPos;         \n"     // The position of the light in eye space.
        + "                                 \n"
        + "attribute vec4 a_Position;       \n"     // Per-vertex position information we will pass in.
        + "attribute vec3 a_Normal;         \n"     // Per-vertex normal information we will pass in.
        + "                                 \n"
        + "varying vec4 v_Color;            \n"     // This will be passed into the fragment shader.
        + "                                 \n"
        + "void main()                      \n"     // The entry point for our vertex shader.
        + "{                                \n"
        + "     vec3 modelViewVertex = vec3(u_MVMatrix * a_Position);               \n" // Transform the vertex into eye space.
        + "                                                                         \n"
        + "     vec3 modelViewNormal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));      \n" // Transform the normal's orientation into eye space.
        + "                                                                         \n"
        + "     float distance = length(u_LightPos - modelViewVertex);              \n" // Will be used for attenuation.
        + "                                                                         \n"
        + "     vec3 lightVector = normalize(u_LightPos - modelViewVertex);         \n" // Get a lighting direction vector from the light to the vertex.
        + "                                                                         \n"
        + "     float diffuse = max(dot(modelViewNormal, lightVector), 0.0);        \n" // Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
        + "                                                                         \n" // pointing in the same direction then it will get max illumination.
        + "     diffuse = diffuse * (1.0 / (1.0 + (0.5 * distance * distance))) + 0.5;   \n" // Attenuate the light based on distance. We scale the square of the distance by 0.25 to dampen the attenuation effect, and we also add 1.0 to the modified distance so that we don't get oversaturation when the light is very close to an object.
        + "                                                                         \n"      // Ambient light of 0.5
        + "                                                                         \n"
        + "     v_Color = vec4(1.0f, 0.0f, 0.0f, 1.0f) * diffuse;                   \n" // Multiply the color by the illumination level. It will be interpolated across the triangle.
        + "                                                                         \n"
        + "     gl_Position = u_MVPMatrix * a_Position;                             \n" // Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
        + "}                                                                        ";

    // No light, hard code red color
    public static final String basicVertexShader =
          "uniform mat4 u_MVPMatrix;      \n"		// A constant representing the combined model/view/projection matrix.
        + "                               \n"
        + "attribute vec4 a_Position;     \n"		// Per-vertex position information we will pass in.
        + "                               \n"
        + "varying vec4 v_Color;          \n"		// This will be passed into the fragment shader.
        + "                               \n"
        + "void main()                    \n"		// The entry point for our vertex shader.
        + "{                              \n"
        + "   v_Color = vec4(1.0f, 0.0f, 0.0f, 1.0f);\n"		// Pass the color through to the fragment shader.
        + "                               \n"                   // It will be interpolated across the triangle.
        + "   gl_Position = u_MVPMatrix * a_Position;\n" 	    // gl_Position is a special variable used to store the final position.
        + "                               \n"                   // Multiply the vertex by the matrix to get the final point in
        + "}                              ";        // normalized screen coordinates.


    // "Pass through" shader
    public static final String basicFragmentShader =
          "precision mediump float;       \n"		// Set the default precision to medium. We don't need as high of a
        + "                               \n"       // precision in the fragment shader.
        + "varying vec4 v_Color;          \n"		// This is the color from the vertex shader interpolated across the
        + "                               \n"       // triangle per fragment.
        + "void main()                    \n"		// The entry point for our fragment shader.
        + "{                              \n"
        + "   gl_FragColor = v_Color;     \n"		// Pass the color directly through the pipeline.
        + "}                              \n";

    // Written for a point light, so no light and no color
    public static final String pointVertexShader =
          "precision mediump float;         \n"
        + "                                 \n"
        + "uniform mat4 u_MVPMatrix;        \n"
        + "                                 \n"
        + "attribute vec4 a_Position;       \n"
        + "                                 \n"
        + "void main()                      \n"
        + "{                                \n"
        + "     gl_Position = u_MVPMatrix * a_Position;\n"
        + "                                 \n"
        + "     gl_PointSize = 40.0;         \n"
        + "}                                ";

    // Written for a point light, hard code white color
    public static final String pointFragmentShader =
          "precision mediump float;         \n"
        + "void main()                      \n"
        + "{                                \n"
        + "     gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);\n"
        + "}                                ";
}
