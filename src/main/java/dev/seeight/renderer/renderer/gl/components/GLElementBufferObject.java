package dev.seeight.renderer.renderer.gl.components;

import org.lwjgl.opengl.GL15;

public class GLElementBufferObject {
	private int ebo;

	public void init(boolean bind) {
		ebo = GL15.glGenBuffers();

		if (bind) {
			this.bind();
		}
	}

	public void bind() {
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.ebo);
	}

	/**
	 * This method will not check if this is the bound EBO.
	 */
	public void bufferData(float[] array) {
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, array, GL15.GL_STATIC_DRAW);
	}

	/**
	 * This method will not check if this is the bound EBO.
	 */
	public void bufferData(int[] array) {
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, array, GL15.GL_STATIC_DRAW);
	}

	/**
	 * This method will not check if this is the bound EBO.
	 */
	public void bufferData(long[] array) {
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, array, GL15.GL_STATIC_DRAW);
	}

	/**
	 * This method will not check if this is the bound EBO.
	 */
	public void bufferData(short[] array) {
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, array, GL15.GL_STATIC_DRAW);
	}

	public void delete() {
		GL15.glDeleteBuffers(this.ebo);
	}
}
