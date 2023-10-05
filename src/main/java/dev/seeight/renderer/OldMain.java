package dev.seeight.renderer;

import dev.seeight.renderer.renderer.Renderer;
import dev.seeight.renderer.renderer.gl.OpenGLRenderer2;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class OldMain {
	public static void main(String[] args) {
		if (!GLFW.glfwInit()) {
			System.err.println("GLFW Failed to initialize.");
			System.exit(1);
			return;
		}

		Window window = new Window(1280, 720, "Test Window");
		window.setIconName(null).setResizable(true).setVisible(false).createWindow();

		GL.createCapabilities();

		GL11.glClearColor(1.0f, 1.0f, 0.0f, 0.0f);

		Renderer renderer = new OpenGLRenderer2();


		while (!window.shouldClose()) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

			renderer.frameStart();

			renderStuff(renderer);

			renderer.frameEnd();

			window.swapBuffers();

			GLFW.glfwPollEvents();
		}

		Callbacks.glfwFreeCallbacks(window.id());
		GLFW.glfwDestroyWindow(window.id());

		// Terminate GLFW and free the error callback
		GLFW.glfwTerminate();
	}

	private static void renderStuff(Renderer renderer) {
		renderer.color(1, 1, 1, 0.5f);

		for (int i = 1; i < 5; i++) {
			float a = (5 - i) / 5f;

			renderer.rect2f(-a, -a, a, a);
		}
	}
}
