package dev.seeight.renderer.renderer.gl.exception;

public class UniformNotFoundException extends RuntimeException {
	public final int programId;
	public final String uniformName;

	public UniformNotFoundException(int programId, String uniformName) {
		super("Uniform '" + uniformName + "' not found on program " + programId);
		this.programId = programId;
		this.uniformName = uniformName;
	}
}
