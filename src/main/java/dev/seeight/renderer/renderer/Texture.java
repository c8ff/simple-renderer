package dev.seeight.renderer.renderer;

public interface Texture {
	int getWidth();

	int getHeight();

	int getId();

	void delete();

	boolean isDeleted();
}
