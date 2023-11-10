package dev.seeight.renderer.renderer.gl.components;

import org.lwjgl.opengl.GL15;

public class GLVertexBufferObject {
	private int vertexBufferID = 0;

	public GLVertexBufferObject() {

	}

	public void init(boolean bind) {
		this.vertexBufferID = GL15.glGenBuffers();

		if (bind) {
			this.bind();
		}
	}

	public void bind() {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vertexBufferID);
	}

	public void delete() {
		GL15.glDeleteBuffers(this.vertexBufferID);
		this.vertexBufferID = 0;
	}

	public int getID() {
		return vertexBufferID;
	}
}
