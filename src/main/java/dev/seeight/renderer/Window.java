package dev.seeight.renderer;

import dev.seeight.renderer.util.JarUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

/**
 * A class that represents a GLFW window.
 *
 * @author C8FF
 */
@SuppressWarnings("ConstantConditions")
class Window {
	private long window;

	private int width, height;
	private String title;

	private boolean visible = false;
	private boolean resizable = false;

	private String iconName = "default-icon.png";

	/**
	 * Creates a window instance. It doesn't initialize the window.
	 */
	public Window(int width, int height, String title) {
		this.title = title;
		this.window = -1;
		setSize(width, height);
	}

	/**
	 * Should be called before createWindow() method.
	 */
	public Window setVisible(boolean visible) {
		this.visible = visible;

		return this;
	}

	/**
	 * Should be called before createWindow() method.
	 */
	public Window setResizable(boolean resizable) {
		this.resizable = resizable;

		return this;
	}

	/**
	 * Should be called before createWindow() method.
	 */
	public Window setIconName(String iconName) {
		this.iconName = iconName;

		return this;
	}

	/**
	 * Creates the window with the previously set 'parameters'.
	 */
	public void createWindow() {
		GLFW.glfwDefaultWindowHints(); // optional, the current window hints are already the default
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, this.visible ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE); // the window will stay hidden after creation
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, this.resizable ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);

		window = GLFW.glfwCreateWindow(getWidth(), getHeight(), title, 0, 0);

		if (window == 0) {
			throw new RuntimeException("Failed to create window.");
		}

		GLFW.glfwMakeContextCurrent(window);
		this.setVSync(1);
		centerWindow();
		GLFW.glfwShowWindow(window);

		if (iconName != null) {
			JarUtils.setIcon(iconName, window);
		}
	}

	/**
	 * 0: Unlimited
	 * 1: V-Sync
	 * 2... : The monitor's refresh rate divided by the number. (ex. (60 / 2) = 30)
	 */
	public void setVSync(int vSync) {
		GLFW.glfwSwapInterval(vSync);
	}

	public void setTitle(String title) {
		this.title = title;

		if (this.window != -1) {
			GLFW.glfwSetWindowTitle(this.window, title);
		}
	}

	public void reset() {
		if (this.window != -1) {
			GLFW.glfwSetWindowTitle(window, title);
		}
	}

	/**
	 * Tries to center the window on the primary monitor/screen.
	 */
	public void centerWindow() {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);

			// Get the window size passed to glfwCreateWindow
			GLFW.glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

			// Center the window
			GLFW.glfwSetWindowPos(
					window,
					(vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2
			);
		}
	}

	public long id() {
		return window;
	}

	public void swapBuffers() {
		GLFW.glfwSwapBuffers(window);
	}

	public void setClosed(boolean closed) {
		GLFW.glfwSetWindowShouldClose(window, closed);
	}

	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(window);
	}

	/**
	 * Sets the size. If the window was already initialized, it will set the size with GLFW.
	 */
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;

		if (this.window != -1) {
			GLFW.glfwSetWindowSize(this.window, width, height);
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	/**
	 * Destroys the window.
	 */
	public void destroy() {
		GLFW.glfwDestroyWindow(this.id());
		window = -1;
	}
}
