package dev.seeight.renderer.renderer;

import java.nio.FloatBuffer;

public interface Renderer {
	void rect2f(float x, float y, float x2, float y2);

	void rect2d(double x, double y, double x2, double y2);

	void hollowRect2f(float x, float y, float x2, float y2, float size);

	void hollowRect2d(double x, double y, double x2, double y2, float size);

	void texRect2f(Texture texture, float x, float y, float x2, float y2);

	void texRect2d(Texture texture, double x, double y, double x2, double y2);

	void texRect2f(Texture texture, float x, float y, float x2, float y2, float u, float v, float u2, float v2);

	void texRect2d(Texture texture, double x, double y, double x2, double y2, double u, double v, double u2, double v2);

	void color(double r, double g, double b, double a);

	void bindTexture(int texture);

	void frameStart();

	void frameEnd();

	void ortho(float left, float right, float bottom, float top, float zNear, float zFar);

	void resetProjection();

	void translate(float x, float y, float z);

	void scale(float x, float y, float z);

	void rotate(float angle, float x, float y, float z);

	void setViewMatrix4f(float[] array);

	void setViewMatrix4f(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23, float m30, float m31, float m32, float m33);

	void getViewMatrix4f(float[] array);

	void getViewMatrix4f(FloatBuffer buffer);

	void getProjectionMatrix4f(float[] array);

	void setProjectionMatrix4f(float[] array);
}
