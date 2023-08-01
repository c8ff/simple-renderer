package dev.seeight.renderer.renderer.gl.components;

import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;
import java.util.HashMap;

/**
 * TODO: auto setup uniforms
 */
public class GLProgram {
	private final HashMap<String, Integer> uniforms = new HashMap<>();
	private int programID;

	public GLProgram() {

	}

	public void init(String vertexSource, String fragmentSource) {
		int vertexShaderID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		int fragmentShaderID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);

		debug("Compiling vertex shader.");
		GL20.glShaderSource(vertexShaderID, vertexSource);
		GL20.glCompileShader(vertexShaderID);

		this.checkCompileErrors(vertexShaderID, "An error occurred while compiling the vertex shader: ");

		debug("Compiling fragment shader.");
		GL20.glShaderSource(fragmentShaderID, fragmentSource);
		GL20.glCompileShader(fragmentShaderID);

		this.checkCompileErrors(fragmentShaderID, "An error occurred while compiling the fragment shader: ");

		debug("Linking shaders.");
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

	public void createUniform(String name) {
		int uniformLocation = GL20.glGetUniformLocation(this.programID, name);
		if (uniformLocation < 0)
			throw new RuntimeException("Could not find uniform: " + name);

		uniforms.put(name, uniformLocation);
	}

	public void uniformVec4f(String name, Vector4f vec) {
		uniform4f(name, vec.x, vec.y, vec.z, vec.w);
	}

	public void uniformMatrix4fv(String name, FloatBuffer floatBuffer) {
		GL20.glUniformMatrix4fv(uniforms.get(name), false, floatBuffer);
	}

	public void uniform2f(String name, float a, float b) {
		GL20.glUniform2f(uniforms.get(name), a, b);
	}

	public void uniform3f(String name, float a, float b, float c) {
		GL20.glUniform3f(uniforms.get(name), a, b, c);
	}

	public void uniform4f(String name, float x, float y, float z, float w) {
		GL20.glUniform4f(uniforms.get(name), x, y, z, w);
	}

	public void uniform1fv(String name, float[] array) {
		GL20.glUniform1fv(uniforms.get(name), array);
	}

	private void debug(String str) {

	}

	private void checkCompileErrors(int id, String failString) {
		int info = GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS);
		if (info != GL20.GL_TRUE) {
			int length = GL20.glGetShaderi(id, GL20.GL_INFO_LOG_LENGTH);
			throw new RuntimeException(failString + GL20.glGetShaderInfoLog(id, length));
		}
	}

	private void checkLinkErrors(int id) {
		int info = GL20.glGetProgrami(id, GL20.GL_LINK_STATUS);
		if (info != GL20.GL_TRUE) {
			int length = GL20.glGetProgrami(id, GL20.GL_INFO_LOG_LENGTH);
			throw new RuntimeException("Error while linking shaders: " + GL20.glGetProgramInfoLog(id, length));
		}
	}

	public void delete() {
		GL20.glDeleteProgram(this.programID);
		this.programID = 0;
		this.uniforms.clear();
	}
}
