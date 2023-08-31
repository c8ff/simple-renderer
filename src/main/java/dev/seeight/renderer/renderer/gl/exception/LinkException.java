package dev.seeight.renderer.renderer.gl.exception;

public class LinkException extends RuntimeException {
	public final int id;

	public LinkException(String message, int id) {
		super(message);
		this.id = id;
	}
}
