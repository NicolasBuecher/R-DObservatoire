package com.example.buecher.observatoireapplication;

/**
 * Created by Nicolas Buecher on 26/01/2016.
 */
public final class WorldLayoutData
{
    public static final float[] CUBE_COORDS = new float[]
    {
         // Front face
        -1.0f,  1.0f,  1.0f,
        -1.0f, -1.0f,  1.0f,
         1.0f,  1.0f,  1.0f,
        -1.0f, -1.0f,  1.0f,
         1.0f, -1.0f,  1.0f,
         1.0f,  1.0f,  1.0f,

         // Right face
         1.0f,  1.0f,  1.0f,
         1.0f, -1.0f,  1.0f,
         1.0f,  1.0f, -1.0f,
         1.0f, -1.0f,  1.0f,
         1.0f, -1.0f, -1.0f,
         1.0f,  1.0f, -1.0f,

         // Back face
         1.0f,  1.0f, -1.0f,
         1.0f, -1.0f, -1.0f,
        -1.0f,  1.0f, -1.0f,
         1.0f, -1.0f, -1.0f,
        -1.0f, -1.0f, -1.0f,
        -1.0f,  1.0f, -1.0f,

         // Left face
        -1.0f,  1.0f, -1.0f,
        -1.0f, -1.0f, -1.0f,
        -1.0f,  1.0f,  1.0f,
        -1.0f, -1.0f, -1.0f,
        -1.0f, -1.0f,  1.0f,
        -1.0f,  1.0f,  1.0f,

         // Top face
        -1.0f,  1.0f, -1.0f,
        -1.0f,  1.0f,  1.0f,
         1.0f,  1.0f, -1.0f,
        -1.0f,  1.0f,  1.0f,
         1.0f,  1.0f,  1.0f,
         1.0f,  1.0f, -1.0f,

         // Bottom face
         1.0f, -1.0f, -1.0f,
         1.0f, -1.0f,  1.0f,
        -1.0f, -1.0f, -1.0f,
         1.0f, -1.0f,  1.0f,
        -1.0f, -1.0f,  1.0f,
        -1.0f, -1.0f, -1.0f
    };

    public static final float[] CUBE_COLORS = new float[]
    {
        // Front, green
        0.0f, 0.5273f, 0.2656f, 1.0f,
        0.0f, 0.5273f, 0.2656f, 1.0f,
        0.0f, 0.5273f, 0.2656f, 1.0f,
        0.0f, 0.5273f, 0.2656f, 1.0f,
        0.0f, 0.5273f, 0.2656f, 1.0f,
        0.0f, 0.5273f, 0.2656f, 1.0f,

        // right, blue
        0.0f, 0.3398f, 0.9023f, 1.0f,
        0.0f, 0.3398f, 0.9023f, 1.0f,
        0.0f, 0.3398f, 0.9023f, 1.0f,
        0.0f, 0.3398f, 0.9023f, 1.0f,
        0.0f, 0.3398f, 0.9023f, 1.0f,
        0.0f, 0.3398f, 0.9023f, 1.0f,

        // Back, also green
        0.0f, 0.5273f, 0.2656f, 1.0f,
        0.0f, 0.5273f, 0.2656f, 1.0f,
        0.0f, 0.5273f, 0.2656f, 1.0f,
        0.0f, 0.5273f, 0.2656f, 1.0f,
        0.0f, 0.5273f, 0.2656f, 1.0f,
        0.0f, 0.5273f, 0.2656f, 1.0f,

        // Left, also blue
        0.0f, 0.3398f, 0.9023f, 1.0f,
        0.0f, 0.3398f, 0.9023f, 1.0f,
        0.0f, 0.3398f, 0.9023f, 1.0f,
        0.0f, 0.3398f, 0.9023f, 1.0f,
        0.0f, 0.3398f, 0.9023f, 1.0f,
        0.0f, 0.3398f, 0.9023f, 1.0f,

        // Top, red
        0.8359375f,  0.17578125f,  0.125f, 1.0f,
        0.8359375f,  0.17578125f,  0.125f, 1.0f,
        0.8359375f,  0.17578125f,  0.125f, 1.0f,
        0.8359375f,  0.17578125f,  0.125f, 1.0f,
        0.8359375f,  0.17578125f,  0.125f, 1.0f,
        0.8359375f,  0.17578125f,  0.125f, 1.0f,

        // Bottom, also red
        0.8359375f,  0.17578125f,  0.125f, 1.0f,
        0.8359375f,  0.17578125f,  0.125f, 1.0f,
        0.8359375f,  0.17578125f,  0.125f, 1.0f,
        0.8359375f,  0.17578125f,  0.125f, 1.0f,
        0.8359375f,  0.17578125f,  0.125f, 1.0f,
        0.8359375f,  0.17578125f,  0.125f, 1.0f
    };
}
