package dev.seeight.renderer.renderer.gl.components;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.GL44;

public class GLShaderStorageBufferObject {
	private int ssbo;

	public GLShaderStorageBufferObject() {

	}

	public void init(boolean bind) {
		ssbo = GL15.glGenBuffers();

		if (bind) {
			bindBuffer();
		}
	}

	public void bindBuffer() {
		GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, ssbo);
	}

	public void bufferData(int[] data, int usage) {
		GL15.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, data, usage);
	}

	public void bufferData(float[] data, int usage) {
		GL15.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, data, usage);
	}

	public void bufferData(double[] data, int usage) {
		GL15.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, data, usage);
	}

	public void subData(int offset, int[] data) {
		GL15.glBufferSubData(GL43.GL_SHADER_STORAGE_BUFFER, offset, data);
	}

	public void subData(int offset, float[] data) {
		GL15.glBufferSubData(GL43.GL_SHADER_STORAGE_BUFFER, offset, data);
	}

	public void subData(int offset, double[] data) {
		GL15.glBufferSubData(GL43.GL_SHADER_STORAGE_BUFFER, offset, data);
	}

	public void bindBufferBase(int id) {
		GL30.glBindBufferBase(GL44.GL_SHADER_STORAGE_BUFFER, id, ssbo);
	}

	public void delete() {
		GL15.glDeleteBuffers(this.ssbo);
		this.ssbo = 0;
	}

	public int getID() {
		return ssbo;
	}
}
