package dev.seeight.renderer.renderer.gl.components;

import dev.seeight.renderer.renderer.gl.exception.FramebufferException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;

public class GLFramebuffer {
	/**
	 * The main framebuffer object.
	 */
	protected int fbo = -1;
	/**
	 * The attached texture to the framebuffer object.
	 */
	protected int texture = -1;
	/**
	 * The width of the framebuffer.
	 */
	protected int width;
	/**
	 * The height of the framebuffer.
	 */
	protected int height;

	public void create(int width, int height) {
		create(width, height, true);
	}

	/**
	 * Creates a new underlying framebuffer object and texture.
	 * Deletes the old one if it existed.
	 *
	 * @param width       The width of the new framebuffer
	 * @param height      The height of the new framebuffer
	 * @param checkErrors True if {@link #checkFrameBuffer()} should be called after creating the components.
	 */
	public void create(int width, int height, boolean deletePrevious, boolean checkErrors) {
		if (width == 0 && height == 0) {
			throw new IllegalArgumentException("width and height cannot be 0.");
		}

		this.width = width;
		this.height = height;

		// delete previous framebuffer and texture, if existent.
		if (deletePrevious) {
			this.delete();
		}
		// creates a new texture with width and height
		this.createTexture();
		this.fbo = GL30.glGenFramebuffers();
		// bind framebuffer
		this.bind(false);
		// attach texture to framebuffer
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, this.texture, 0);
		// check errors
		if (checkErrors) {
			this.checkFrameBuffer();
		}
		// bind framebuffer id 0
		this.unbind();
	}

	/**
	 * Deletes both the framebuffer and the attached texture.
	 */
	public void delete() {
		this.deleteTexture();
		this.deleteFramebuffer();
	}

	/**
	 * Binds the {@link #fbo} object with optional viewport setting.
	 *
	 * @param viewport True if the viewport should be resized to the frame buffer's size.
	 */
	public void bind(boolean viewport) {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.fbo);

		if (viewport) {
			GL11.glViewport(0, 0, this.width, this.height);
		}
	}

	/**
	 * Unbinds the current framebuffer.
	 */
	public void unbind() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	}

	/**
	 * Creates a new texture and sets {@link #texture}.
	 */
	protected void createTexture() {
		this.texture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.texture);
		this.applyTextureParameters();
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, this.width, this.height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
	}

	/**
	 * Applies the texture's arguments.
	 */
	protected void applyTextureParameters() {
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
	}

	/**
	 * Checks for framebuffer errors from OpenGL.
	 */
	protected void checkFrameBuffer() {
		int status = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER);

		if (status != GL30.GL_FRAMEBUFFER_COMPLETE) {
			throw new FramebufferException(status);
		}
	}

	/**
	 * Deletes the attached texture.
	 */
	public void deleteTexture() {
		if (this.texture > 0) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
			GL11.glDeleteTextures(this.texture);
			this.texture = -1;
		}
	}

	/**
	 * Deletes the framebuffer.
	 */
	public void deleteFramebuffer() {
		if (this.fbo > 0) {
			this.unbind();
			GL30.glDeleteFramebuffers(this.fbo);
			this.fbo = -1;
		}
	}

	public int getTexture() {
		return this.texture;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isCreated() {
		return texture > 0 && fbo > 0;
	}
}
