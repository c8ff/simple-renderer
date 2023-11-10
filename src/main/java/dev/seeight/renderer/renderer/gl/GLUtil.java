package dev.seeight.renderer.renderer.gl;

import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.lwjgl.opengl.*;

import java.nio.*;

public class GLUtil {
	public static void arrayBufferData(int size, int usage) {
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, size, usage);
	}

	public static void arrayBufferData(float[] data, int usage) {
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, usage);
	}

	public static void arrayBufferData(double[] data, int usage) {
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, usage);
	}

	public static void arrayBufferData(short[] data, int usage) {
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, usage);
	}

	public static void arrayBufferData(int[] data, int usage) {
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, usage);
	}

	public static void arrayBufferData(long[] data, int usage) {
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, usage);
	}

	public static void arrayBufferData(ByteBuffer data, int usage) {
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, usage);
	}

	public static void arrayBufferData(LongBuffer data, int usage) {
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, usage);
	}

	public static void arrayBufferData(FloatBuffer data, int usage) {
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, usage);
	}

	public static void arrayBufferData(ShortBuffer data, int usage) {
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, usage);
	}

	public static void arrayBufferData(IntBuffer data, int usage) {
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, usage);
	}

	public static void arrayBufferData(DoubleBuffer data, int usage) {
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, usage);
	}

	public static void arrayBufferSubData(long offset, float[] data) {
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, offset, data);
	}

	public static void arrayBufferSubData(long offset, int[] data) {
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, offset, data);
	}

	public static void arrayBufferSubData(long offset, short[] data) {
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, offset, data);
	}

	public static void arrayBufferSubData(long offset, double[] data) {
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, offset, data);
	}

	public static void arrayBufferSubData(long offset, ByteBuffer data) {
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, offset, data);
	}

	public static void arrayBufferSubData(long offset, LongBuffer data) {
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, offset, data);
	}

	public static void arrayBufferSubData(long offset, ShortBuffer data) {
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, offset, data);
	}

	public static void arrayBufferSubData(long offset, FloatBuffer data) {
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, offset, data);
	}

	public static void arrayBufferSubData(long offset, DoubleBuffer data) {
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, offset, data);
	}

	public static void arrayBufferSubData(long offset, IntBuffer data) {
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, offset, data);
	}

	public static void shaderStorageData(int size, int usage) {
		GL15.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, size, usage);
	}

	public static void shaderStorageData(int[] data, int usage) {
		GL15.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, data, usage);
	}

	public static void shaderStorageData(short[] data, int usage) {
		GL15.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, data, usage);
	}

	public static void shaderStorageData(double[] data, int usage) {
		GL15.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, data, usage);
	}

	public static void shaderStorageData(float[] data, int usage) {
		GL15.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, data, usage);
	}

	public static void shaderStorageData(ByteBuffer data, int usage) {
		GL15.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, data, usage);
	}

	public static void shaderStorageData(FloatBuffer data, int usage) {
		GL15.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, data, usage);
	}

	public static void shaderStorageData(ShortBuffer data, int usage) {
		GL15.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, data, usage);
	}

	public static void shaderStorageData(IntBuffer data, int usage) {
		GL15.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, data, usage);
	}

	public static void shaderStorageData(LongBuffer data, int usage) {
		GL15.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, data, usage);
	}

	public static void shaderStorageData(DoubleBuffer data, int usage) {
		GL15.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, data, usage);
	}

	public static void shaderStorageSubData(long offset, int[] data) {
		GL15.glBufferSubData(GL43.GL_SHADER_STORAGE_BUFFER, offset, data);
	}

	public static void shaderStorageSubData(long offset, double[] data) {
		GL15.glBufferSubData(GL43.GL_SHADER_STORAGE_BUFFER, offset, data);
	}

	public static void shaderStorageSubData(long offset, float[] data) {
		GL15.glBufferSubData(GL43.GL_SHADER_STORAGE_BUFFER, offset, data);
	}

	public static void shaderStorageSubData(long offset, short[] data) {
		GL15.glBufferSubData(GL43.GL_SHADER_STORAGE_BUFFER, offset, data);
	}

	public static void shaderStorageSubData(long offset, long[] data) {
		GL15.glBufferSubData(GL43.GL_SHADER_STORAGE_BUFFER, offset, data);
	}

	public static void shaderStorageSubData(long offset, ByteBuffer data) {
		GL15.glBufferSubData(GL43.GL_SHADER_STORAGE_BUFFER, offset, data);
	}

	public static void shaderStorageSubData(long offset, FloatBuffer data) {
		GL15.glBufferSubData(GL43.GL_SHADER_STORAGE_BUFFER, offset, data);
	}

	public static void shaderStorageSubData(long offset, IntBuffer data) {
		GL15.glBufferSubData(GL43.GL_SHADER_STORAGE_BUFFER, offset, data);
	}

	public static void shaderStorageSubData(long offset, ShortBuffer data) {
		GL15.glBufferSubData(GL43.GL_SHADER_STORAGE_BUFFER, offset, data);
	}

	public static void shaderStorageSubData(long offset, LongBuffer data) {
		GL15.glBufferSubData(GL43.GL_SHADER_STORAGE_BUFFER, offset, data);
	}

	public static void shaderStorageSubData(long offset, DoubleBuffer data) {
		GL15.glBufferSubData(GL43.GL_SHADER_STORAGE_BUFFER, offset, data);
	}

	public static void resetMatrix(Matrix4f mat) {
		mat.set(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
	}

	public static void resetMatrix(Matrix4d mat) {
		mat.set(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
	}
}
