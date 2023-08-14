package dev.seeight.renderer.renderer;

import java.nio.FloatBuffer;

public interface Renderer {
	/**
	 * Draws a rectangle on the screen.
	 *
	 * @param x  The X position of the rectangle.
	 * @param y  The Y position of the rectangle.
	 * @param x2 The X position at the end of the rectangle.
	 * @param y2 The Y position at the end of the rectangle.
	 */
	void rect2f(float x, float y, float x2, float y2);

	/**
	 * Draws a rectangle on the screen.
	 *
	 * @param x  The X position of the rectangle.
	 * @param y  The Y position of the rectangle.
	 * @param x2 The X position at the end of the rectangle.
	 * @param y2 The Y position at the end of the rectangle.
	 */
	void rect2d(double x, double y, double x2, double y2);

	/**
	 * Draws a hollow rectangle on the screen. A.k.a. a border or margin.
	 *
	 * @param x    The X position of the rectangle.
	 * @param y    The Y position of the rectangle.
	 * @param x2   The X position at the end of the rectangle.
	 * @param y2   The Y position at the end of the rectangle.
	 * @param size The size of the margins.
	 */
	void hollowRect2f(float x, float y, float x2, float y2, float size);

	/**
	 * Draws a hollow rectangle on the screen. A.k.a. a border or margin.
	 *
	 * @param x    The X position of the rectangle.
	 * @param y    The Y position of the rectangle.
	 * @param x2   The X position at the end of the rectangle.
	 * @param y2   The Y position at the end of the rectangle.
	 * @param size The size of the margins.
	 */
	void hollowRect2d(double x, double y, double x2, double y2, float size);

	/**
	 * Draws a textured rectangle on the screen.
	 *
	 * @param x  The X position of the texture.
	 * @param y  The Y position of the texture.
	 * @param x2 The X position at the end of the texture.
	 * @param y2 The Y position at the end of the texture.
	 */
	void texRect2f(Texture texture, float x, float y, float x2, float y2);

	/**
	 * Draws a textured rectangle on the screen.
	 *
	 * @param x  The X position of the texture.
	 * @param y  The Y position of the texture.
	 * @param x2 The X position at the end of the texture.
	 * @param y2 The Y position at the end of the texture.
	 */
	void texRect2d(Texture texture, double x, double y, double x2, double y2);

	/**
	 * Draws a textured rectangle on the screen.
	 *
	 * @param x  The X position of the texture.
	 * @param y  The Y position of the texture.
	 * @param x2 The X position at the end of the texture.
	 * @param y2 The Y position at the end of the texture.
	 * @param u  The U coordinate of the texture.
	 * @param v  The V coordinate of the texture.
	 * @param u2 The U coordinate at the end of the texture.
	 * @param v2 The V coordinate at the end of the texture.
	 */
	void texRect2f(Texture texture, float x, float y, float x2, float y2, float u, float v, float u2, float v2);

	/**
	 * Draws a textured rectangle on the screen.
	 *
	 * @param x  The X position of the texture.
	 * @param y  The Y position of the texture.
	 * @param x2 The X position at the end of the texture.
	 * @param y2 The Y position at the end of the texture.
	 * @param u  The U coordinate of the texture.
	 * @param v  The V coordinate of the texture.
	 * @param u2 The U coordinate at the end of the texture.
	 * @param v2 The V coordinate at the end of the texture.
	 */
	void texRect2d(Texture texture, double x, double y, double x2, double y2, double u, double v, double u2, double v2);

	/**
	 * Changes the current color if it wasn't set before.
	 *
	 * @param r The red color channel.
	 * @param g The green color channel.
	 * @param b The blue color channel.
	 * @param a The alpha color channel.
	 */
	void color(double r, double g, double b, double a);

	/**
	 * Binds the texture with the {@code texture} parameter.
	 *
	 * @param texture The ID of the texture to bind.
	 */
	void bindTexture(int texture);

	/**
	 * Called before rendering. This may initialize Shaders, Vertex Buffer Objects, Vertex Array Objects, etc.
	 */
	void frameStart();

	/**
	 * Called after rendering.
	 */
	void frameEnd();

	/**
	 * Resets and sets the projection matrix to an orthographic projection.
	 */
	void ortho(float left, float right, float bottom, float top, float zNear, float zFar);

	/**
	 * Resets the projection matrix.
	 */
	void resetProjection();

	/**
	 * Translates the view matrix.
	 *
	 * @param x The X-axis translation.
	 * @param y The Y-axis translation.
	 * @param z The Z-axis translation.
	 */
	void translate(float x, float y, float z);

	/**
	 * Scales the view matrix.
	 *
	 * @param x The X-axis scale.
	 * @param y The Y-axis scale.
	 * @param z The Z-axis scale.
	 */
	void scale(float x, float y, float z);

	/**
	 * Rotates the view matrix.
	 *
	 * @param angle The angle in radians.
	 * @param x     The X-axis rotation.
	 * @param y     The Y-axis rotation.
	 * @param z     The Z-axis rotation.
	 */
	void rotate(float angle, float x, float y, float z);

	/**
	 * Sets the view matrix values to the {@code array}'s values.
	 *
	 * @param array Must be bigger or equal than a 4x4 Matrix.
	 */
	void setViewMatrix4f(float[] array);

	/**
	 * Sets the view matrix values to the parameter values.
	 */
	void setViewMatrix4f(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23, float m30, float m31, float m32, float m33);

	/**
	 * Sets the {@code array}'s values to the view matrix values.
	 */
	void getViewMatrix4f(float[] array);

	/**
	 * Sets the {@code buffer}'s to the view matrix values.
	 */
	void getViewMatrix4f(FloatBuffer buffer);

	/**
	 * Sets the {@code array}'s to the projection matrix values.
	 */
	void getProjectionMatrix4f(float[] array);

	/**
	 * Sets the projection matrix values to the {@code array}'s values.
	 */
	void setProjectionMatrix4f(float[] array);
}
