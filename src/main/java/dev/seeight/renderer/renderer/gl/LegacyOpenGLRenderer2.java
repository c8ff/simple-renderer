package dev.seeight.renderer.renderer.gl;

import dev.seeight.renderer.renderer.Renderer;
import dev.seeight.renderer.renderer.Texture;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import java.util.function.Consumer;

@Deprecated
@SuppressWarnings("SameParameterValue")
public class LegacyOpenGLRenderer2 implements Renderer {
	public int glDrawCalls;
	public int zLevel = 0;
	public boolean flipTextures = false;
	public boolean checkQuads = true;
	private int glDrawMode = -1;
	private double glRed, glBlue, glGreen, glAlpha;
	private int glBoundTexture2D;
	private int glSFactor;
	private int glDFactor;
	private boolean glTexture2D;
	private boolean glBlend;
	private boolean glBegin;
	private int glDrawCallsTemp;

	public void glBegin(int mode) {
		if (!this.glBegin) {
			this.checkErrors();

			GL11.glBegin(mode);
			this.glDrawMode = mode;
			this.glBegin = true;

			this.checkErrors();
		}
	}

	public void glEnd() {
		if (this.glBegin) {
			this.checkErrors();

			GL11.glEnd();
			this.glBegin = false;

			this.checkErrors();

			this.glDrawCallsTemp++;
		}
	}

	private void glEnableTexture2D() {
		if (!this.glTexture2D) {
			this.glEnableEnd(GL11.GL_TEXTURE_2D);
			this.glTexture2D = true;
		}
	}

	private void glDisableTexture2D() {
		if (this.glTexture2D) {
			this.glDisableEnd(GL11.GL_TEXTURE_2D);
			this.glTexture2D = false;
		}
	}

	private void glEnableBlend() {
		if (!this.glBlend) {
			this.glEnableEnd(GL11.GL_BLEND);
			this.glBlend = true;
		}
	}

	private void glDisableBlend() {
		if (this.glBlend) {
			this.glDisableEnd(GL11.GL_BLEND);
			this.glBlend = false;
		}
	}

	private void glBlendFunc(int sfactor, int dfactor) {
		if (this.glSFactor != sfactor || this.glDFactor != dfactor) {
			GL11.glBlendFunc(sfactor, dfactor);

			this.glSFactor = sfactor;
			this.glDFactor = dfactor;
		}
	}

	private void glEnableEnd(int target) {
		boolean endBegin = this.glBegin;

		if (endBegin) {
			this.glEnd();
		}

		GL11.glEnable(target);

		if (endBegin) {
			this.glBegin(this.glDrawMode);
		}
	}

	private void glDisableEnd(int target) {
		boolean endBegin = this.glBegin;

		if (endBegin) {
			this.glEnd();
		}

		GL11.glDisable(target);

		if (endBegin) {
			this.glBegin(this.glDrawMode);
		}
	}

	private void glVertex2f(float x, float y) {
		if (zLevel != 0) {
			GL11.glVertex3f(x, y, zLevel);
		} else {
			GL11.glVertex2f(x, y);
		}
	}

	private void glVertex2d(double x, double y) {
		if (zLevel != 0) {
			GL11.glVertex3d(x, y, zLevel);
		} else {
			GL11.glVertex2d(x, y);
		}
	}

	private void glTexCoord2f(float u, float v) {
		GL11.glTexCoord2f(u, v);
	}

	private void glTexCoord2d(double u, double v) {
		GL11.glTexCoord2d(u, v);
	}

	private void glEnableBindTexture2D(int texture) {
		boolean endBegin = !this.glBegin || (this.checkQuads && this.glDrawMode != GL11.GL_QUADS) || (!this.glTexture2D || this.glBoundTexture2D != texture);

		if (endBegin) {
			this.glEnd();
		}

		if (!this.glTexture2D) {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			this.checkErrors();
			this.glTexture2D = true;
		}

		if (this.glBoundTexture2D != texture) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
			this.checkErrors();
			this.glBoundTexture2D = texture;
		}

		if (endBegin) {
			if (this.checkQuads && this.glDrawMode != GL11.GL_QUADS) {
				this.glBegin(GL11.GL_QUADS);
			} else {
				this.glBegin(this.glDrawMode);
			}
		}
	}

	private void glDisableBindTexture2D(int texture) {
		boolean endBegin = !this.glBegin || (this.checkQuads && this.glDrawMode != GL11.GL_QUADS) || (!this.glTexture2D || this.glBoundTexture2D != texture);

		if (endBegin) {
			this.glEnd();
		}

		if (this.glBoundTexture2D != texture) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
			this.glBoundTexture2D = texture;

			this.checkErrors();
		}

		if (this.glTexture2D) {
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			this.glTexture2D = false;

			this.checkErrors();
		}

		if (endBegin) {
			if (this.checkQuads && this.glDrawMode != GL11.GL_QUADS) {
				this.glBegin(GL11.GL_QUADS);
			} else {
				this.glBegin(this.glDrawMode);
			}
		}
	}

	@Override
	public void color(double r, double g, double b, double a) {
		if (r != this.glRed || g != this.glGreen || b != this.glBlue || a != this.glAlpha) {
			GL11.glColor4d(r, g, b, a);

			this.glRed = r;
			this.glGreen = g;
			this.glBlue = b;
			this.glAlpha = a;
		}
	}

	@Override
	public void bindTexture(int texture) {
		boolean endBegin = this.glBegin && this.glBoundTexture2D != texture;

		if (endBegin) {
			this.glEnd();
		}

		if (this.glBoundTexture2D != texture) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
			this.glBoundTexture2D = texture;

			this.checkErrors();
		}

		if (endBegin) {
			this.glBegin(this.glDrawMode);
		}
	}

	@Override
	public void rect2f(float x, float y, float x2, float y2) {
		this.glDisableBindTexture2D(0);

		this.glVertex2f(x, y);
		this.glVertex2f(x, y2);
		this.glVertex2f(x2, y2);
		this.glVertex2f(x2, y);
	}

	@Override
	public void rect2d(double x, double y, double x2, double y2) {
		this.glDisableBindTexture2D(0);

		this.checkErrors();

		this.glVertex2d(x, y);
		this.glVertex2d(x, y2);
		this.glVertex2d(x2, y2);
		this.glVertex2d(x2, y);

		this.checkErrors();
	}

	@Override
	public void hollowRect2f(float x, float y, float x2, float y2, float size) {
		this.glDisableBindTexture2D(0);

		float x1 = x + size;
		float y1 = y + size;
		float x3 = x2 - size;
		float y3 = y2 - size;

		// Top
		this.glVertex2f(x, y);
		this.glVertex2f(x, y1);
		this.glVertex2f(x2, y1);
		this.glVertex2f(x2, y);

		// Bottom
		this.glVertex2f(x, y3);
		this.glVertex2f(x, y2);
		this.glVertex2f(x2, y2);
		this.glVertex2f(x2, y3);

		// Left
		this.glVertex2f(x, y);
		this.glVertex2f(x, y2);
		this.glVertex2f(x1, y2);
		this.glVertex2f(x1, y);

		// Right
		this.glVertex2f(x3, y);
		this.glVertex2f(x3, y2);
		this.glVertex2f(x2, y2);
		this.glVertex2f(x2, y);
	}

	@Override
	public void hollowRect2d(double x, double y, double x2, double y2, float size) {
		this.glDisableBindTexture2D(0);

		double x1 = x + size;
		double y1 = y + size;
		double x3 = x2 - size;
		double y3 = y2 - size;

		// Top
		this.glVertex2d(x, y);
		this.glVertex2d(x, y1);
		this.glVertex2d(x2, y1);
		this.glVertex2d(x2, y);

		// Bottom
		this.glVertex2d(x3, y3);
		this.glVertex2d(x3, y2);
		this.glVertex2d(x2, y2);
		this.glVertex2d(x2, y3);

		// Left
		this.glVertex2d(x, y);
		this.glVertex2d(x, y1);
		this.glVertex2d(x1, y1);
		this.glVertex2d(x1, y);

		// Right
		this.glVertex2d(x3, y);
		this.glVertex2d(x3, y1);
		this.glVertex2d(x2, y1);
		this.glVertex2d(x2, y);
	}

	@Override
	public void texRect2f(Texture texture, float x, float y, float x2, float y2) {
		this.texRect2f(texture, x, y, x2, y2, 0, 0, 1, 1);
		this.checkErrors();
	}

	@Override
	public void texRect2d(Texture texture, double x, double y, double x2, double y2) {
		this.texRect2d(texture, x, y, x2, y2, 0, 0, 1, 1);
		this.checkErrors();
	}

	@Override
	public void texRect2f(Texture texture, float x, float y, float x2, float y2, float u, float v, float u2, float v2) {
		this.glEnableBindTexture2D(texture.getId());

		this.checkErrors();

		if (this.flipTextures) {
			this.glTexCoord2f(u, v2);
			this.glVertex2f(x, y);
			this.glTexCoord2f(u, v);
			this.glVertex2f(x, y2);
			this.glTexCoord2f(u2, v);
			this.glVertex2f(x2, y2);
			this.glTexCoord2f(u2, v2);
			this.glVertex2f(x2, y);
		} else {
			this.glTexCoord2f(u, v);
			this.glVertex2f(x, y);
			this.glTexCoord2f(u, v2);
			this.glVertex2f(x, y2);
			this.glTexCoord2f(u2, v2);
			this.glVertex2f(x2, y2);
			this.glTexCoord2f(u2, v);
			this.glVertex2f(x2, y);
		}
	}

	@Override
	public void texRect2d(Texture texture, double x, double y, double x2, double y2, double u, double v, double u2, double v2) {
		this.glEnableBindTexture2D(texture.getId());

		if (this.flipTextures) {
			this.glTexCoord2d(u, v2);
			this.glVertex2d(x, y);
			this.glTexCoord2d(u, v);
			this.glVertex2d(x, y2);
			this.glTexCoord2d(u2, v);
			this.glVertex2d(x2, y2);
			this.glTexCoord2d(u2, v2);
			this.glVertex2d(x2, y);
		} else {
			this.glTexCoord2d(u, v);
			this.glVertex2d(x, y);
			this.glTexCoord2d(u, v2);
			this.glVertex2d(x, y2);
			this.glTexCoord2d(u2, v2);
			this.glVertex2d(x2, y2);
			this.glTexCoord2d(u2, v);
			this.glVertex2d(x2, y);
		}
	}

	@Override
	public void frameStart() {
		this.glEnableBlend();
		this.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		this.bindTexture(0);
		this.glDrawMode = GL11.GL_QUADS;
		this.glBegin = false;
	}

	@Override
	public void frameEnd() {
		if (this.glBegin) {
			this.glEnd();
		}
		this.glDisableBlend();

		this.glDrawCalls = this.glDrawCallsTemp;
		this.glDrawCallsTemp = 0;
		this.zLevel = 0;
	}

	@Override
	public void ortho(float left, float right, float bottom, float top, float zNear, float zFar) {
		this.resetProjection();
		GL11.glOrtho(left, right, bottom, top, zNear, zFar);
	}

	@Override
	public void resetProjection() {
		GL11.glLoadIdentity();
	}

	@Override
	public void resetView() {
		GL11.glLoadIdentity();
	}

	@Override
	public void translate(float x, float y, float z) {
		GL11.glTranslatef(x, y, z);
	}

	@Override
	public void scale(float x, float y, float z) {
		GL11.glScalef(x, y, z);
	}

	@Override
	public void rotate(float angle, float x, float y, float z) {
		GL11.glRotatef(angle, x, y, z);
	}

	@Override
	public void setViewMatrix4f(float[] array) {
		GL11.glLoadMatrixf(array);
	}

	@Override
	public void setViewMatrix4f(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23, float m30, float m31, float m32, float m33) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		buffer.put(m00)
				.put(m01)
				.put(m02)
				.put(m03)
				.put(m10)
				.put(m11)
				.put(m12)
				.put(m13)
				.put(m20)
				.put(m21)
				.put(m22)
				.put(m23)
				.put(m30)
				.put(m31)
				.put(m32)
				.put(m33);
		GL11.glLoadMatrixf(buffer);
	}

	@Override
	public void getViewMatrix4f(float[] array) {
		GL11.glGetFloatv(GL11.GL_MODELVIEW_MATRIX, array);
	}

	@Override
	public void getViewMatrix4f(FloatBuffer buffer) {
		GL11.glGetFloatv(GL11.GL_MODELVIEW_MATRIX, buffer);
	}

	@Override
	public void getProjectionMatrix4f(float[] array) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setProjectionMatrix4f(float[] array) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Runs custom OpenGL code. Disables blend and calls glEnd.
	 * You should use only the methods that are inside this class. For example, {@link #glBegin(int)} or {@link #glEnableBlend()}.
	 *
	 * @param consumer The code to be run.
	 */
	public void custom(Consumer<LegacyOpenGLRenderer2> consumer) {
		if (this.glBegin) {
			this.glEnd();
		}
		this.glDisableBlend();

		consumer.accept(this);

		this.glEnableBlend();
		this.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		this.bindTexture(0);
	}

	private void checkErrors() {
		checkErrors(true);
	}

	private void checkErrors(boolean simple) {
		// TIL: You can't call glGetError while being inside glBegin and glEnd, bummer :(
		if (this.glBegin) {
			return;
		}

		int a = GL11.glGetError();

		if (a != GL11.GL_NO_ERROR) {
			if (simple) {
				if (a == GL11.GL_INVALID_ENUM) {
					throw new RuntimeException("OpenGL: Invalid Enum.");
				} else if (a == GL11.GL_INVALID_OPERATION) {
					throw new RuntimeException("OpenGL: Invalid Operation.");
				} else if (a == GL11.GL_INVALID_VALUE) {
					throw new RuntimeException("OpenGL: Invalid value.");
				}
			} else {
				StringBuilder str = new StringBuilder();
				boolean f = false;

				if (a == GL11.GL_INVALID_ENUM) {
					f = true;
					str.append("OpenGL: Invalid Enum.").append("\n");
				} else if (a == GL11.GL_INVALID_OPERATION) {
					f = true;
					str.append("OpenGL: Invalid Operation.").append("\n");
				} else if (a == GL11.GL_INVALID_VALUE) {
					f = true;
					str.append("OpenGL: Invalid value.").append("\n");
				}

				if (f) {
					str.append("Fields after the reported error: ").append("\n");
					str.append("\tglDrawMode: ").append(this.glDrawMode).append("\n");
					str.append("\tglTexture2D: ").append(this.glTexture2D).append("\n");
					str.append("\tglBoundTexture2D: ").append(this.glBoundTexture2D).append("\n");
					str.append("\tglBlend: ").append(this.glBlend).append("\n");
					str.append("\tglSFactor: ").append(this.glSFactor).append("\n");
					str.append("\tglDFactor: ").append(this.glDFactor).append("\n");
					str.append("\tglBegin: ").append(this.glBegin).append("\n");
					str.append("Stacktrace: ");

					new RuntimeException(str.toString()).printStackTrace();
				}
			}
		}
	}

	public int getGLDrawMode() {
		return glDrawMode;
	}
}
