package dev.seeight.renderer.renderer.gl.components;

import org.lwjgl.opengl.GL15;

public class GLVertexBufferObject {
	private int vertexBufferID = 0;

	public GLVertexBufferObject() {

	}

	public void init(boolean bind) {
		this.vertexBufferID = GL15.glGenBuffers();

		if (bind) {
			this.bindBuffer();
		}
	}

	public void bindBuffer() {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vertexBufferID);
	}

	/**
	 * This method will not check if this is the bound VAO.
	 */
	public void bufferData(float[] array) {
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, array, GL15.GL_STATIC_DRAW);
	}

	/**
	 * This method will not check if this is the bound VAO.
	 */
	public void bufferData(int[] array) {
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, array, GL15.GL_STATIC_DRAW);
	}

	/**
	 * This method will not check if this is the bound VAO.
	 */
	public void bufferData(long[] array) {
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, array, GL15.GL_STATIC_DRAW);
	}

	/**
	 * This method will not check if this is the bound VAO.
	 */
	public void bufferData(short[] array) {
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, array, GL15.GL_STATIC_DRAW);
	}

	public void delete() {
		GL15.glDeleteBuffers(this.vertexBufferID);
		this.vertexBufferID = 0;
	}

	public int getID() {
		return vertexBufferID;
	}
}
