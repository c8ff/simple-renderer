package dev.seeight.renderer.renderer.gl.components;

import org.lwjgl.opengl.GL11;

import java.util.Objects;

public class GLVertexArrayAttrib {
	private final int size;
	private final int sizeInBytes;
	private final int type;

	public GLVertexArrayAttrib(int size, int type) {
		this.type = type;
		this.size = size;

		if (type == GL11.GL_FLOAT) {
			sizeInBytes = size * Float.BYTES;
		} else if (type == GL11.GL_INT) {
			sizeInBytes = size * Integer.BYTES;
		} else if (type == GL11.GL_SHORT) {
			sizeInBytes = size * Short.BYTES;
		} else if (type == GL11.GL_BYTE) {
			sizeInBytes = size;
		} else {
			throw new UnsupportedOperationException("Invalid type argument.");
		}
	}

	private GLVertexArrayAttrib(int size, int type, int sizeInBytes) {
		if (size > 4) {
			throw new RuntimeException("Attribute's size is outside bounds.");
		}

		this.size = size;
		this.type = type;
		this.sizeInBytes = sizeInBytes;
	}

	public int getSize() {
		return size;
	}

	public int getType() {
		return type;
	}

	public int getSizeInBytes() {
		return sizeInBytes;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof GLVertexArrayAttrib attrib)) {
			return false;
		}

		return attrib.size == size && attrib.sizeInBytes == sizeInBytes && attrib.type == type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(size, sizeInBytes, type);
	}

	@Override
	public String toString() {
		return "GLVertexArrayAttrib[" +
			   "size=" + size + ", " +
			   "sizeInBytes=" + sizeInBytes + ", " +
			   "type=" + type + ']';
	}

	public static GLVertexArrayAttrib floatAttribute(int size) {
		return new GLVertexArrayAttrib(size, GL11.GL_FLOAT, size * Float.BYTES);
	}

	public static GLVertexArrayAttrib intAttribute(int size) {
		return new GLVertexArrayAttrib(size, GL11.GL_INT, size * Integer.BYTES);
	}

	public static GLVertexArrayAttrib doubleAttribute(int size) {
		return new GLVertexArrayAttrib(size, GL11.GL_DOUBLE, size * Double.BYTES);
	}

	public static GLVertexArrayAttrib byteAttribute(int size) {
		return new GLVertexArrayAttrib(size, GL11.GL_BYTE, size);
	}

	public static GLVertexArrayAttrib shortAttribute(int size) {
		return new GLVertexArrayAttrib(size, GL11.GL_SHORT, size * Short.BYTES);
	}
}