package dev.seeight.renderer.renderer.gl.components;

import dev.seeight.renderer.renderer.Texture;
import dev.seeight.renderer.util.JarUtils;
import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * A class that represents an OpenGL texture
 *
 * @author C8FF
 */
public class GLTexture implements Texture {
	/**
	 * The min filter that is used on new textures. By default, it is {@link GL11#GL_NEAREST}
	 */
	public static int DEFAULT_TEXTURE_MIN_FILTER = GL11.GL_NEAREST;
	/**
	 * The max filter that is used on new textures. By default, it is {@link GL11#GL_NEAREST}
	 */
	public static int DEFAULT_TEXTURE_MAG_FILTER = GL11.GL_NEAREST;
	public static boolean CREATE_MIPMAPS = false;
	public static boolean INVERT_IMAGE_AT_LOAD = false;
	public int width;
	public int height;
	protected int id;
	protected boolean deleted;
	protected boolean missing;
	protected boolean mipmap;

	/**
	 * Creates a Texture with {@code GL11.GL_NEAREST} as the filter and the path being inside the jar.
	 *
	 * @param filePath A path that is 'inside' the jar.
	 */
	public GLTexture(String filePath) {
		this(filePath, true);
	}

	/**
	 * Creates a Texture with {@code GL11.GL_NEAREST} as the filter.
	 *
	 * @param filePath A path which depends on {@code fromJar}.
	 * @param fromJar  True if the path is from the jar.
	 */
	public GLTexture(String filePath, boolean fromJar) {
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);

		ByteBuffer data;

		try {
			STBImage.stbi_set_flip_vertically_on_load(INVERT_IMAGE_AT_LOAD);
			data = fromJar ? JarUtils.loadImageFromJar(filePath, width, height, comp) : JarUtils.loadImageFromFile(filePath, width, height, comp);
		} catch (FileNotFoundException exception) {
			exception.printStackTrace();
			missing = true;
			return;
		}

		if (data == null) {
			throw new RuntimeException("Couldn't load data: Unknown reason.");
		}

		this.createTexture(width, height, data);

		STBImage.stbi_image_free(data);
	}

	public GLTexture(ByteBuffer byteBuffer) {
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);

		STBImage.stbi_set_flip_vertically_on_load(INVERT_IMAGE_AT_LOAD);
		ByteBuffer data = STBImage.stbi_load_from_memory(byteBuffer, width, height, comp, 4);

		if (data == null) {
			throw new RuntimeException("stb returned null data (probably the image is in a wrong format)");
		}

		this.createTexture(width, height, data);
		STBImage.stbi_image_free(data);
	}

	/**
	 * Creates a Texture.
	 *
	 * @param width  The width of the texture in pixels.
	 * @param height The height of the texture in pixels.
	 * @param data   The texture data in RGBA unsigned byte format.
	 */
	public GLTexture(IntBuffer width, IntBuffer height, ByteBuffer data) {
		if (data == null) {
			throw new RuntimeException("null parameter -> data: ByteBuffer");
		}
		if (width == null) {
			throw new RuntimeException("null parameter -> width: IntBuffer");
		}
		if (height == null) {
			throw new RuntimeException("null parameter -> height: IntBuffer");
		}

		this.createTexture(width, height, data);
	}

	/**
	 * Creates a Texture, this won't load anything, it will just hold an id.
	 */
	public GLTexture(int id) {
		this.id = id;
	}

	/**
	 * Creates a Texture, this won't load anything, it will just hold an id, width and height.
	 */
	public GLTexture(int id, int width, int height) {
		this.id = id;
		this.width = width;
		this.height = height;
	}

	/**
	 * Sets the fields of a Texture with the supplied parameters.
	 *
	 * @param width  The width of the texture in pixels.
	 * @param height The height of the texture in pixels.
	 * @param data   The texture data in RGBA unsigned byte format.
	 */
	protected void createTexture(IntBuffer width, IntBuffer height, ByteBuffer data) {
		this.width = width.get();
		this.height = height.get();
		this.id = GL11.glGenTextures();

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id);
		this.applyTextureParameters();
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, this.width, this.height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);

		if (CREATE_MIPMAPS) {
			this.createMipmap();
		}
	}

	protected void applyTextureParameters() {
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, DEFAULT_TEXTURE_MIN_FILTER);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, DEFAULT_TEXTURE_MAG_FILTER);
	}

	/**
	 * Binds this texture and creates mipmaps for it.
	 */
	public void createMipmap() {
		if (this.hasMipmap()) {
			return;
		}

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id);
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		this.mipmap = true;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}

	/**
	 * Deletes the texture from OpenGL
	 */
	@Override
	public void delete() {
		if (this.deleted) {
			throw new RuntimeException("Invalid call: The texture is already deleted.");
		}

		GL11.glDeleteTextures(id);

		this.deleted = true;
	}

	public void bind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id);
	}

	/**
	 * @return True if the texture wasn't found
	 */
	public boolean isMissing() {
		return missing;
	}

	@Override
	public boolean isDeleted() {
		return this.deleted;
	}

	public boolean hasMipmap() {
		return mipmap;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getId() {
		if (this.deleted) {
			throw new RuntimeException("The texture was deleted.");
		}

		return id;
	}

	public static GLTexture fromInputStream(InputStream inputStream) throws IOException {
		return fromBytes(IOUtils.toByteArray(inputStream));
	}

	public static GLTexture fromBytes(byte[] bytes) {
		ByteBuffer byteBuffer = BufferUtils.createByteBuffer(bytes.length);
		byteBuffer.put(bytes);
		byteBuffer.flip();
		return new GLTexture(byteBuffer);
	}
}
