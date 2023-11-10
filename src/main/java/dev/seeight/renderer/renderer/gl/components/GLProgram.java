package dev.seeight.renderer.renderer.gl.components;

import dev.seeight.renderer.renderer.gl.exception.CompileErrorException;
import dev.seeight.renderer.renderer.gl.exception.LinkException;
import dev.seeight.renderer.renderer.gl.exception.UniformNotFoundException;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;

public class GLProgram {
	protected int programID;

	public GLProgram() {

	}

	public void init(CharSequence vertexSource, CharSequence fragmentSource) {
		if (this.isInitialized()) {
			return;
		}

		int vertexShaderID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		int fragmentShaderID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);

		GL20.glShaderSource(vertexShaderID, vertexSource);
		GL20.glCompileShader(vertexShaderID);

		this.checkCompileErrors(vertexShaderID, "An error occurred while compiling the vertex shader: ");

		GL20.glShaderSource(fragmentShaderID, fragmentSource);
		GL20.glCompileShader(fragmentShaderID);

		this.checkCompileErrors(fragmentShaderID, "An error occurred while compiling the fragment shader: ");

		this.programID = GL20.glCreateProgram();

		GL20.glAttachShader(this.programID, vertexShaderID);
		GL20.glAttachShader(this.programID, fragmentShaderID);

		GL20.glLinkProgram(this.programID);

		this.checkLinkErrors(this.programID);

		GL20.glDetachShader(this.programID, vertexShaderID);
		GL20.glDetachShader(this.programID, fragmentShaderID);

		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
	}

	public void useProgram() {
		if (this.programID == 0) {
			throw new RuntimeException("init method hasn't been called.");
		}

		GL20.glUseProgram(this.programID);
	}

	@Deprecated
	public void createUniform(String name) {

	}

	public void uniformVec4f(String name, Vector4f vec) {
		uniform4f(name, vec.x, vec.y, vec.z, vec.w);
	}

	public void uniformMatrix4fv(String name, FloatBuffer floatBuffer) {
		GL20.glUniformMatrix4fv(getUniformAssert(name), false, floatBuffer);
	}

	public void uniform1f(String name, float a) {
		GL20.glUniform1f(getUniformAssert(name), a);
	}

	public void uniform1i(String name, int a) {
		GL20.glUniform1i(getUniformAssert(name), a);
	}

	public void uniform1fv(String name, float[] array) {
		GL20.glUniform1fv(getUniformAssert(name), array);
	}

	public void uniform2f(String name, float a, float b) {
		GL20.glUniform2f(getUniformAssert(name), a, b);
	}

	public void uniform2i(String name, int a, int b) {
		GL20.glUniform2i(getUniformAssert(name), a, b);
	}

	public void uniform3f(String name, float a, float b, float c) {
		GL20.glUniform3f(getUniformAssert(name), a, b, c);
	}

	public void uniform4f(String name, float x, float y, float z, float w) {
		GL20.glUniform4f(getUniformAssert(name), x, y, z, w);
	}

	protected int getUniformAssert(String name) {
		int loc = GL20.glGetUniformLocation(this.programID, name);
		if (loc < 0) {
			throw new UniformNotFoundException(this.programID, name);
		}

		return loc;
	}

	protected void checkCompileErrors(int id, String failString) {
		int info = GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS);
		if (info == GL20.GL_FALSE) {
			int length = GL20.glGetShaderi(id, GL20.GL_INFO_LOG_LENGTH);
			throw new CompileErrorException(failString + GL20.glGetShaderInfoLog(id, length), id);
		}
	}

	protected void checkLinkErrors(int id) {
		int info = GL20.glGetProgrami(id, GL20.GL_LINK_STATUS);
		if (info == GL20.GL_FALSE) {
			int length = GL20.glGetProgrami(id, GL20.GL_INFO_LOG_LENGTH);
			throw new LinkException(GL20.glGetProgramInfoLog(id, length), id);
		}
	}

	public void delete() {
		if (!isInitialized()) {
			return;
		}

		GL20.glDeleteProgram(this.programID);
		this.programID = 0;
	}

	public boolean isInitialized() {
		return this.programID != 0;
	}
}
