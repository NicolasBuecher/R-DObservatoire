package com.example.buecher.observatoireapplication;

import android.opengl.GLES20;

/**
 * Created by Nicolas Buecher on 26/01/2016.
 */
public class Util
{
    public static int BYTES_PER_FLOAT = 4;
    public static int BYTES_PER_SHORT = 2;

    public static void loadGLShader(int shader, String source)
    {
        if (shader != 0)
        {
            GLES20.glShaderSource(shader, source);
            GLES20.glCompileShader(shader);

            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            if (compileStatus[0] == 0)
            {
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }

        if (shader == 0)
        {
            throw new RuntimeException("Error creating shader.");
        }
    }
}
