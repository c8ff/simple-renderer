package dev.seeight.renderer.renderer;

public interface Texture {
	/**
	 * @return The width of the texture.
	 */
	int getWidth();

	/**
	 * @return The height of the texture.
	 */
	int getHeight();

	/**
	 * @return The identification number (ID) of the texture. Can be from OpenGL or another renderer.
	 */
	int getId();

	/**
	 * Deletes the texture.
	 * @throws RuntimeException If the texture was already deleted.
	 */
	void delete();

	/**
	 * @return True if the texture was deleted.
	 */
	boolean isDeleted();

	/**
	 * @return Normalized horizontal coordinates for the start of the texture.
	 */
	default float u() {
		return 0;
	}

	/**
	 * @return Normalized vertical coordinates for the start of the texture.
	 */
	default float v() {
		return 0;
	}

	/**
	 * @return Normalized horizontal coordinates for the end of the texture.
	 */
	default float u2() {
		return 1;
	}

	/**
	 * @return Normalized vertical coordinates for the end of the texture.
	 */
	default float v2() {
		return 1;
	}
}
