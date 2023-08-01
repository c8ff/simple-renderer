package dev.seeight.renderer.util;

import dev.seeight.renderer.renderer.gl.components.GLTexture;
import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.stb.STBImage;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class JarUtils {
	public static ByteBuffer loadImageFromJar(String fileName, IntBuffer width, IntBuffer height, IntBuffer comp) throws FileNotFoundException {
		InputStream r = GLTexture.class.getResourceAsStream(fileName);
		if (r == null) {
			throw new FileNotFoundException("File '" + fileName + " not found.");
		}

		return STBImage.stbi_load_from_memory(getByteBuffer(r), width, height, comp, 4);
	}

	public static ByteBuffer loadImageFromFile(String fileName, IntBuffer width, IntBuffer height, IntBuffer comp) throws FileNotFoundException {
		File file = new File(fileName);

		if (!file.exists()) {
			throw new FileNotFoundException("'" + fileName + "' does not exist.");
		}

		InputStream r;
		try {
			r = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}

		return STBImage.stbi_load_from_memory(getByteBuffer(r), width, height, comp, 4);
	}

	public static Image loadImageFromJarA(String fileName) {
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);

		InputStream r = JarUtils.class.getResourceAsStream("/assets/textures/" + fileName);
		if (r == null) throw new NullPointerException("Couldn't find file " + fileName + ".");

		ByteBuffer data = STBImage.stbi_load_from_memory(getByteBuffer(r), width, height, comp, 4);
		return new Image(width.get(), height.get(), data);
	}

	/**
	 * From <a href="https://stackoverflow.com/questions/45171816/lwjgl-3-stbi-load-from-memory-not-working-when-in-jar">here</a>
	 */
	public static ByteBuffer getByteBuffer(InputStream stream) {
		byte[] imgData = new byte[0];
		try {
			imgData = IOUtils.toByteArray(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			stream.read(imgData);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ByteBuffer buff = BufferUtils.createByteBuffer(imgData.length);
		buff.put(imgData);
		buff.flip();

		return buff;
	}

	public static void setIcon(String name, long window) {
		GLFWImage image = GLFWImage.malloc();
		GLFWImage.Buffer imagebf = GLFWImage.malloc(1);
		Image img = JarUtils.loadImageFromJarA(name);
		image.set(img.width(), img.height(), img.data());
		imagebf.put(0, image);
		GLFW.glfwSetWindowIcon(window, imagebf);
	}

	public record Image(int width, int height, ByteBuffer data) {
	}
}
