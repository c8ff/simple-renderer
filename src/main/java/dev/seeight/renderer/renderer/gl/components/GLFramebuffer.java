package dev.seeight.renderer.renderer.gl.components;

import dev.seeight.renderer.renderer.gl.exception.FramebufferException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;

public class GLFramebuffer {
	protected int fbo = -1;
	protected int texture = -1;
	protected int width;
	protected int height;

	public void create(int width, int height) {
		create(width, height, true);
	}

	public void create(int width, int height, boolean checkErrors) {
		this.width = width;
		this.height = height;

		// delete previous framebuffer and texture, if existent.
		this.delete();
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

	public void delete() {
		if (this.texture > 0) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
			GL11.glDeleteTextures(this.texture);
			this.texture = -1;
		}

		if (this.fbo > 0) {
			this.unbind();
			GL30.glDeleteFramebuffers(this.fbo);
			this.fbo = -1;
		}
	}

	public void bind(boolean viewport) {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.fbo);

		if (viewport) {
			GL11.glViewport(0, 0, this.width, this.height);
		}
	}

	public void unbind() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	}

	protected void createTexture() {
		this.texture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.texture);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, this.width, this.height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
	}

	protected void checkFrameBuffer() {
		int status = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER);

		if (status != GL30.GL_FRAMEBUFFER_COMPLETE) {
			throw new FramebufferException(status);
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
