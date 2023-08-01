package dev.seeight.renderer.renderer.gl.components;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;

public class GLVertexArrayObject {
	private final ArrayList<GLVertexArrayAttrib> attributes = new ArrayList<>();
	private int vertexArrayID;

	public GLVertexArrayObject() {

	}

	public void init(boolean bind) {
		this.vertexArrayID = GL30.glGenVertexArrays();

		if (bind) {
			this.bindVertexArray();
		}
	}

	public void addAttrib(int size, int type) {
		attributes.add(new GLVertexArrayAttrib(size, type));
	}

	public void uploadAttributes() {
		if (attributes.isEmpty()) {
			return;
		}

		int currentVertexArray = GL11.glGetInteger(GL30.GL_VERTEX_ARRAY_BINDING);
		if (currentVertexArray != this.getID()) {
			this.bindVertexArray();
		}

		int totalLengthInBytes = 0;
		for (GLVertexArrayAttrib attrib : attributes) {
			totalLengthInBytes += attrib.getSizeInBytes();
		}

		int index = 0;
		int offsetInBytes = 0;
		for (GLVertexArrayAttrib attrib : attributes) {
			GL30.glVertexAttribPointer(index, attrib.getSize(), attrib.getType(), false, totalLengthInBytes, offsetInBytes);
			offsetInBytes += attrib.getSizeInBytes();
			index++;
		}

		if (currentVertexArray != this.getID()) {
			GL30.glBindVertexArray(currentVertexArray);
		}
	}

	public void bindVertexArray() {
		GL30.glBindVertexArray(this.vertexArrayID);
	}

	public int getID() {
		return vertexArrayID;
	}

	public void delete() {
		GL30.glDeleteVertexArrays(this.vertexArrayID);
		this.vertexArrayID = 0;
		this.attributes.clear();
	}
}
