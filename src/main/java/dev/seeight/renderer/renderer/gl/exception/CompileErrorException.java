package dev.seeight.renderer.renderer.gl.exception;

public class CompileErrorException extends RuntimeException {
	public final int shaderId;

	public CompileErrorException(String message, int shaderId) {
		super(message);
		this.shaderId = shaderId;
	}
}
