package com.example.buecher.observatoireapplication;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by buecher on 26/01/2016.
 */
public class Sphere
{
    private final int BYTES_PER_VERTEX = 3 * Util.BYTES_PER_FLOAT;
    private final int BYTES_PER_NORMAL = 3 * Util.BYTES_PER_FLOAT;
    private final int BYTES_PER_TEXTURE = 2 * Util.BYTES_PER_FLOAT;

    private float m_radius;
    private int m_stacks;
    private int m_slices;

    private int m_nbVertices;

    private FloatBuffer m_vertexBuffer;
    private FloatBuffer m_normalBuffer;
    private FloatBuffer m_textureBuffer;
    private ShortBuffer m_indexBuffer;


    public Sphere(int numberOfStacks, int numberOfSlices, float radius)
    {
        // Members initialization
        this.m_radius = radius;
        this.m_stacks = numberOfStacks;
        this.m_slices = numberOfSlices;

        // Calculate the number of vertices that will actually be used in computing
        this.m_nbVertices = this.m_stacks * 2 * (this.m_slices + 1);

        // Calculate the number of vertices of the sphere
        int vertexCount = (numberOfStacks + 1) * (numberOfSlices + 1);

        // Buffers memory allocation
        this.m_vertexBuffer = ByteBuffer.allocateDirect(vertexCount * BYTES_PER_VERTEX)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.m_normalBuffer = ByteBuffer.allocateDirect(vertexCount * BYTES_PER_NORMAL)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.m_textureBuffer = ByteBuffer.allocateDirect(vertexCount * BYTES_PER_TEXTURE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.m_indexBuffer = ByteBuffer.allocateDirect(this.m_nbVertices * Util.BYTES_PER_SHORT)
                .order(ByteOrder.nativeOrder()).asShortBuffer();

        // Fill Buffers
        this.computeVertices();

        // Reset the position pointer in the buffers
        this.m_vertexBuffer.position(0);
        this.m_normalBuffer.position(0);
        this.m_textureBuffer.position(0);
        this.m_indexBuffer.position(0);

    }

    // Calculate positions, normals, texture coordinates & indices of the vertices & store them in buffers
    private void computeVertices()
    {
        // Fill vertex, normal & texture buffers
        for (int stack = 0; stack <= this.m_stacks; stack++) {
            for (int slice = 0; slice <= this.m_slices; slice++) {
                float theta = (float) (stack * Math.PI / this.m_stacks);
                float phi = (float) (slice * 2 * Math.PI / this.m_slices);
                float sinTheta = (float) Math.sin(theta);
                float cosTheta = (float) Math.cos(theta);
                float sinPhi = (float) Math.sin(phi);
                float cosPhi = (float) Math.cos(phi);

                float nx = cosPhi * sinTheta;
                float ny = cosTheta;
                float nz = sinPhi * sinTheta;

                float x = this.m_radius * nx;
                float y = this.m_radius * ny;
                float z = this.m_radius * nz;

                float u = 1.0f - ((float) slice / (float) this.m_slices);
                float v = (float) stack / (float) this.m_stacks;

                this.m_normalBuffer.put(nx);
                this.m_normalBuffer.put(ny);
                this.m_normalBuffer.put(nz);

                this.m_vertexBuffer.put(x);
                this.m_vertexBuffer.put(y);
                this.m_vertexBuffer.put(z);

                this.m_textureBuffer.put(u);
                this.m_textureBuffer.put(v);
            }
        }

        // Fill index buffer
        int step = this.m_slices + 1;
        for (int i = 0; i < this.m_stacks * step; i++)
        {
            this.m_indexBuffer.put((short)i);
            this.m_indexBuffer.put((short)(i + step));
        }
    }

    // Return the radius of the sphere
    public float getRadius()
    {
        return this.m_radius;
    }

    // Return the number of stacks of the sphere
    public int getNbStacks()
    {
        return this.m_stacks;
    }

    // Return the number of slices of the sphere
    public int getNbSlices()
    {
        return this.m_slices;
    }

    // Return the number of vertices actually used in computing
    public int getNbVertices()
    {
        return this.m_nbVertices;
    }

    // Return vertexBuffer
    public FloatBuffer getVertexBuffer()
    {
        return this.m_vertexBuffer;
    }

    // Return indexBuffer
    public ShortBuffer getIndexBuffer()
    {
        return this.m_indexBuffer;
    }
}
