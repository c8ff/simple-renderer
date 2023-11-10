package dev.seeight.renderer.renderer.gl.components;

import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper class for Array Buffer Objects of OpenGL.<p>
 * To create an instance, use {@link Builder}.
 */
public class GLVertexArrayObject {
	private int vertexArrayID;

	public GLVertexArrayObject(int id) {
		this.vertexArrayID = id;
	}

	public void bind() {
		GL30.glBindVertexArray(this.vertexArrayID);
	}

	public int getID() {
		return vertexArrayID;
	}

	public void delete() {
		GL30.glDeleteVertexArrays(this.vertexArrayID);
		this.vertexArrayID = 0;
	}

	/**
	 * A builder for an array buffer object.
	 */
	public static class Builder {
		private final List<GLVertexArrayAttrib> attributes = new ArrayList<>();

		public Builder attribute(GLVertexArrayAttrib attrib) {
			attributes.add(attrib);
			return this;
		}

		public Builder floatAttribute(int size) {
			attributes.add(GLVertexArrayAttrib.floatAttribute(size));
			return this;
		}

		public Builder doubleAttribute(int size) {
			attributes.add(GLVertexArrayAttrib.doubleAttribute(size));
			return this;
		}

		public Builder intAttribute(int size) {
			attributes.add(GLVertexArrayAttrib.intAttribute(size));
			return this;
		}

		public Builder byteAttribute(int size) {
			attributes.add(GLVertexArrayAttrib.byteAttribute(size));
			return this;
		}

		public Builder shortAttribute(int size) {
			attributes.add(GLVertexArrayAttrib.shortAttribute(size));
			return this;
		}

		/**
		 * Creates an instance of GLVertexArrayObject.
		 * The attribute list must not be empty, otherwise this method will throw an exception.<p>
		 * This method automatically binds the created object.
		 *
		 * @return A {@link GLVertexArrayAttrib} object.
		 */
		public GLVertexArrayObject build() {
			if (attributes.isEmpty()) {
				throw new RuntimeException("attributes cannot be empty.");
			}

			int id = GL30.glGenVertexArrays();
			GL30.glBindVertexArray(id);

			int index = 0;
			for (GLVertexArrayAttrib attrib : attributes) {
				GL30.glVertexAttribPointer(index, attrib.getSize(), attrib.getType(), false, 0, 0);
				index++;
			}

			return new GLVertexArrayObject(id);
		}
	}
}
