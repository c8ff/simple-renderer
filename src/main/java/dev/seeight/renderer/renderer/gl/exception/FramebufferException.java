package dev.seeight.renderer.renderer.gl.exception;

import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

public class FramebufferException extends RuntimeException {
	public final int errorId;

	public FramebufferException(int errorId) {
		super(errorId + ": " + desc(errorId));
		this.errorId = errorId;
	}

	/**
	 * Descriptions from <a href="https://docs.gl/gl4/glCheckFramebufferStatus">GL docs</a>.
	 */
	public static String desc(int errorId) {
		return switch (errorId) {
			case GL30.GL_FRAMEBUFFER_UNDEFINED ->
					"The specified framebuffer is the default read or draw framebuffer, but the default framebuffer does not exist.";
			case GL30.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT ->
					"The framebuffer attachment points are incomplete.";
			case GL30.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT ->
					"The framebuffer does not have at least one image attached to it.";
			case GL30.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER ->
					"The value of GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE is GL_NONE for any color attachment point(s) named by GL_DRAW_BUFFER.";
			case GL30.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER ->
					"GL_READ_BUFFER is not GL_NONE and the value of GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE is GL_NONE for the color attachment point named by GL_READ_BUFFER.";
			case GL30.GL_FRAMEBUFFER_UNSUPPORTED ->
					"The combination of internal formats of the attached images violates an implementation-dependent set of restrictions.";
			case GL30.GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE ->
					"The value of GL_RENDERBUFFER_SAMPLES is not the same for all attached renderbuffers.";
			case GL32.GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS ->
					"The attachment is layered, and any populated attachment is not layered, or if all populated color attachments are not from textures of the same target.";

			default -> "Unknown error code: " + errorId;
		};
	}
}
