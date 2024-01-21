package dev.seeight.renderer.renderer.gl.components;

import org.lwjgl.opengl.GL15;

public class GLArrayBufferObject {
	private int arrayBufferId = 0;

	public GLArrayBufferObject() {

	}

	public void init(boolean bind) {
		this.arrayBufferId = GL15.glGenBuffers();

		if (bind) {
			this.bind();
		}
	}

	public void bind() {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.arrayBufferId);
	}

	public void delete() {
		GL15.glDeleteBuffers(this.arrayBufferId);
		this.arrayBufferId = 0;
	}

	public int getID() {
		return arrayBufferId;
	}
}
