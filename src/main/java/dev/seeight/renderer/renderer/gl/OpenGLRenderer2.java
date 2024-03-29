package dev.seeight.renderer.renderer.gl;

import dev.seeight.renderer.renderer.Renderer;
import dev.seeight.renderer.renderer.Texture;
import dev.seeight.renderer.renderer.gl.components.*;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

/**
 * An immediate mode OpenGL renderer that uses LWJGL 3.
 * The tutorial I used was <a href="https://learnopengl.com">learnopengl.com</a> to make this renderer.
 *
 * @author seeight
 */
public class OpenGLRenderer2 implements Renderer {
	protected final Matrix4f projection = new Matrix4f();
	protected final Matrix4f view = new Matrix4f();
	protected final Matrix4f model = new Matrix4f();

	private final float[] uvCoordinates = new float[]{
			0, 1,
			1, 0,
			0, 0,

			0, 1,
			1, 1,
			1, 0
	};
	public boolean INVERT_V_COORDINATES;

	protected GLProgram shader;
	protected GLArrayBufferObject vbo;
	protected GLVertexArrayObject vao;
	protected GLShaderStorageBufferObject ssbo;
	protected GLTexture pixelTexture;

	protected GLProgram program;

	private boolean initialized;

	// Low values to 'force' setting of these values correctly via the color(float, float, float, float) method.
	private float r = 0.001F;
	private float g = 0.001F;
	private float b = 0.001F;
	private float a = 0.001F;
	private float u = 0.01F;
	private float v = 0.01F;
	private float u2 = 0.01F;
	private float v2 = 0.01F;

	private FloatBuffer matrixBuffer;

	public OpenGLRenderer2() {
		this(false);
	}

	public OpenGLRenderer2(boolean invertV) {
		this.INVERT_V_COORDINATES = invertV;
		if (GLFW.glfwGetCurrentContext() != MemoryUtil.NULL) {
			this.init();
			this.initialized = true;
		} else {
			throw new RuntimeException("GLFW context doesn't exist.");
		}
	}

	protected void init() {
		this.matrixBuffer = BufferUtils.createFloatBuffer(16);

		this.shader = new GLProgram();
		this.shader.init("""
				#version 440 core
				layout (location = 0) in vec3 vertex; // <vec2 position, vec2 texCoords>
				                
				layout(std430, binding = 2) buffer ssbo
				{
					float texCoords[];
				};
				                
				out vec2 TexCoords;
				                
				uniform mat4 model;
				uniform mat4 projection;
				uniform mat4 view;
				                
				void main()
				{
				    int i = int(vertex.z);
				    TexCoords = vec2(texCoords[i], texCoords[i + 1]);
				    gl_Position = projection * view * model * vec4(vertex.xy, 0.0, 1.0);
				}
				""", """         
				#version 330 core
				in vec2 TexCoords;
				out vec4 color;
				                
				uniform sampler2D image;
				uniform vec4 shapeColor;
				                
				void main()
				{
				    color = shapeColor * texture(image, TexCoords);
				}
				""");
		this.useProgram(this.shader);

		this.pixelTexture = new GLTexture("/assets/textures/pixel.png", true);

		// Create a vertex buffer object
		this.vbo = new GLArrayBufferObject();
		this.vbo.init(true);

		// Upload vertex data (vertex information)
		GLUtil.arrayBufferData(new float[] {
				//x,    y,      index for UV coordinates
				0.0f, 1.0f, 0,
				1.0f, 0.0f, 2,
				0.0f, 0.0f, 4,

				0.0f, 1.0f, 6,
				1.0f, 1.0f, 8,
				1.0f, 0.0f, 10
		}, GL15.GL_STATIC_DRAW);

		// Create a vertex array object
		this.vao = new GLVertexArrayObject.Builder().floatAttribute(3).build();
		GL20.glEnableVertexAttribArray(0);

		// Create a shader storage buffer
		this.ssbo = new GLShaderStorageBufferObject();
		this.ssbo.init(true);
		this.ssbo.bindBufferBase(2);

		// Upload data (UV coordinates)
		GLUtil.shaderStorageData(this.uvCoordinates, GL15.GL_STATIC_DRAW);

		this.setUvCoordinates(0, 0, 1, 1);
		this.color(1, 1, 1, 1);

		this.uploadViewMatrix();
	}

	@Override
	public void rect2f(float x, float y, float x2, float y2) {
		float width = Math.abs(x2 - x);
		float height = Math.abs(y2 - y);

		GLUtil.resetMatrix(this.model);
		this.model.translate(x, y, 0);
		this.model.scale(width, height, 1F);

		this.uploadMatrixUniform("model", this.model);

		this.bindTexture(pixelTexture.getId());

		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);
	}

	@Override
	public void rect2d(double x, double y, double x2, double y2) {
		rect2f((float) x, (float) y, (float) x2, (float) y2);
	}

	@Override
	public void hollowRect2f(float x, float y, float x2, float y2, float size) {
		float x1 = x + size;
		float y1 = y + size;
		float x3 = x2 - size;
		float y3 = y2 - size;

		// Top
		this.rect2f(x, y, x2, y1);

		// Bottom
		this.rect2f(x, y2, x2, y3);

		// Left
		this.rect2f(x, y, x1, y2);

		// Right
		this.rect2f(x3, y, x2, y2);
	}

	@Override
	public void hollowRect2d(double x, double y, double x2, double y2, float size) {
		this.hollowRect2f((float) x, (float) y, (float) x2, (float) y2, size);
	}

	@Override
	public void texRect2f(Texture texture, float x, float y, float x2, float y2) {
		texRect2f(texture, x, y, x2, y2, texture.u(), texture.v(), texture.u2(), texture.v2());
	}

	@Override
	public void texRect2d(Texture texture, double x, double y, double x2, double y2) {
		this.texRect2f(texture, (float) x, (float) y, (float) x2, (float) y2);
	}

	@Override
	public void texRect2f(Texture texture, float x, float y, float x2, float y2, float u, float v, float u2, float v2) {
		this.setUvCoordinates(u, v, u2, v2);

		float width = Math.abs(x2 - x);
		float height = Math.abs(y2 - y);

		GLUtil.resetMatrix(this.model);
		this.model.translate(x, y, 0);
		this.model.scale(width, height, 1F);

		this.uploadMatrixUniform("model", this.model);

		this.bindTexture(texture.getId());

		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);
	}

	@Override
	public void texRect2d(Texture texture, double x, double y, double x2, double y2, double u, double v, double u2, double v2) {
		this.texRect2f(texture, (float) x, (float) y, (float) x2, (float) y2, (float) u, (float) v, (float) u2, (float) v2);
	}

	@Override
	public void color(double r, double g, double b, double a) {
		if (this.r != (float) r || this.g != (float) g || this.b != (float) b || this.a != (float) a) {
			this.r = (float) r;
			this.g = (float) g;
			this.b = (float) b;
			this.a = (float) a;
			this.uploadColor();
		}
	}

	@Override
	public void bindTexture(int texture) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
	}

	@Override
	public void frameStart() {
		if (!initialized) {
			this.init();
			this.initialized = true;
		}

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		this.useDefaultProgram();
		this.useDefaultVbo();
		this.useDefaultVao();
	}

	@Override
	public void frameEnd() {

	}

	@Override
	public void ortho(float left, float right, float bottom, float top, float zNear, float zFar) {
		this.resetProjection();
		this.projection.ortho(left, right, bottom, top, zNear, zFar);
		this.uploadProjectionMatrix();
	}

	@Override
	public void resetProjection() {
		GLUtil.resetMatrix(this.projection);
		this.uploadProjectionMatrix();
	}

	@Override
	public void resetView() {
		GLUtil.resetMatrix(this.view);
		this.uploadViewMatrix();
	}

	@Override
	public void translate(float x, float y, float z) {
		this.view.translate(x, y, z);
		this.uploadViewMatrix();
	}

	@Override
	public void scale(float x, float y, float z) {
		this.view.scale(x, y, z);
		this.uploadViewMatrix();
	}

	@Override
	public void rotate(float angle, float x, float y, float z) {
		this.view.rotate(angle, x, y, z);
		this.uploadViewMatrix();
	}

	@Override
	public void setViewMatrix4f(float[] array) {
		this.view.set(array);
		this.uploadViewMatrix();
	}

	@Override
	public void setViewMatrix4f(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23, float m30, float m31, float m32, float m33) {
		this.view.set(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
		this.uploadViewMatrix();
	}

	@Override
	public void getViewMatrix4f(float[] array) {
		this.view.get(array);
	}

	@Override
	public void getViewMatrix4f(FloatBuffer buffer) {
		this.view.get(buffer);
	}

	@Override
	public void getProjectionMatrix4f(float[] array) {
		this.projection.get(array);
	}

	@Override
	public void setProjectionMatrix4f(float[] array) {
		this.projection.set(array);
		this.uploadProjectionMatrix();
	}

	public void setUvCoordinates(float u, float v, float u2, float v2) {
		//  0, 1,
		//  1, 0,
		//  0, 0,
		//
		//  0, 1,
		//  1, 1,
		//  1, 0
		if (INVERT_V_COORDINATES) {
			float a = v;
			v = v2;
			v2 = a;
		}

		if (u != this.u || v != this.v || u2 != this.u2 || v2 != this.v2) {
			// First triangle
			uvCoordinates[0] = u;
			uvCoordinates[1] = v;

			uvCoordinates[2] = u2;
			uvCoordinates[3] = v2;

			uvCoordinates[4] = u;
			uvCoordinates[5] = v2;


			// Second triangle
			uvCoordinates[6] = u;
			uvCoordinates[7] = v;

			uvCoordinates[8] = u2;
			uvCoordinates[9] = v;

			uvCoordinates[10] = u2;
			uvCoordinates[11] = v2;
			this.u = u;
			this.v = v;
			this.u2 = u2;
			this.v2 = v2;

			GLUtil.shaderStorageSubData(0, uvCoordinates);
		}
	}

	public void useDefaultProgram() {
		this.useProgram(this.shader);
	}

	public void uploadProjectionAndView() {
		this.uploadProjectionMatrix();
		this.uploadViewMatrix();
	}

	public void uploadProjectionMatrix() {
		this.uploadMatrixUniform("projection", this.projection);
	}

	public void uploadViewMatrix() {
		this.uploadMatrixUniform("view", this.view);
	}

	public void uploadColor() {
		this.getProgram().uniform4f("shapeColor", r, g, b, a);
	}

	public void useProgram(GLProgram program) {
		if (program != this.program) {
			this.program = program;
			program.useProgram();
		}
	}

	public void useDefaultVao() {
		this.vao.bind();
	}

	public void useDefaultVbo() {
		this.vbo.bind();
	}

	public GLProgram getProgram() {
		return program;
	}

	private void uploadMatrixUniform(String name, Matrix4f matrix4f) {
		matrix4f.get(this.matrixBuffer);
		this.getProgram().uniformMatrix4fv(name, this.matrixBuffer);
	}
}
