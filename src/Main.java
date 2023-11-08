import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import java.util.ArrayList;
import java.util.ListIterator;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

class Main {
	static final int WINDOW_WIDTH = 800, WINDOW_HEIGHT = 600;
	static private long window;
	private boolean spacePressed = false;
	private static double previousTime;
	public static float deltaTime;

	static final int MIN_RADIUS = 40, MAX_RADIUS = 65;
	static final int MIN_SEGMENTS = 5, MAX_SEGMENTS = 9;
	static final int ASTEROID_SPAWN_RADIUS = 500, ASTEROID_SPEED = 200;
	static final float ASTEROID_SPAWN_DELAY = 0.2f;
	static Ship ship;
	static ArrayList<Asteroid> asteroids = new ArrayList<>();
	static int score = 0; // 10*asteroid.radius per asteroid


	Main() {
		if (!glfwInit()) {
			System.err.println("Error: GLFW initialization failed.");
			System.exit(1);
		}
		GLFWErrorCallback.createPrint(System.err).set();
		window = GLFW.glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "Asteroids", 0, 0);
		if (window == NULL) throw new RuntimeException("Failed to create the GLFW window.");
		GLFW.glfwMakeContextCurrent(window);
		GL.createCapabilities();
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glLoadIdentity();
		glOrtho(0,WINDOW_WIDTH,WINDOW_HEIGHT,0,0,1.0);
	}

	private void loop() {
		glClear(GL_COLOR_BUFFER_BIT);
		ship = new Ship();
		float accumulator = 0.0f;

		while (!glfwWindowShouldClose(window)) {
			double time = glfwGetTime();
			float minStep = 1.0f;
			deltaTime = Math.min((float) (time - previousTime), minStep);
			previousTime = time;

			glClear(GL_COLOR_BUFFER_BIT);

			accumulator += deltaTime;
			if (accumulator >= ASTEROID_SPAWN_DELAY) {
				asteroids.add(new Asteroid(MathUtils.randomNumber(MIN_SEGMENTS, MAX_SEGMENTS), MathUtils.randomNumber(MIN_RADIUS, MAX_RADIUS)));
				accumulator = 0;
			}

			ListIterator<Asteroid> iterator = asteroids.listIterator();
			while (iterator.hasNext()) {
				Asteroid asteroid = iterator.next();
				asteroid.draw();
				asteroid.move(10.0f, 10.0f);
				if (asteroid.collision()) {
					float tempX = asteroid.centreX;
					float tempY = asteroid.centreY;
					float tempRadius = asteroid.radius;
					iterator.remove();
					if (tempRadius > MIN_RADIUS) {
						for (int i = 0; i < 4; i++) {
							int spawnAngle = MathUtils.randomNumber(1, 360);
							float x = (float) (tempX + tempRadius * Math.cos(spawnAngle));
							float y = (float) (tempY + tempRadius * Math.sin(spawnAngle));
							iterator.add(new Asteroid(MathUtils.randomNumber(MIN_SEGMENTS, MAX_SEGMENTS), MathUtils.randomNumber(MIN_RADIUS/2, MAX_RADIUS/2), x, y, spawnAngle));
						}
					}
				}
			}

			for (Projectile projectile : ship.projectiles) {
				projectile.calculateVelocity();
				projectile.draw();
			}

			controller();
			ship.draw();
			glfwSwapBuffers(window);
			glfwPollEvents();
		}

		glfwTerminate();
	}

	private void controller() {
		int keyStateA = glfwGetKey(window, GLFW_KEY_A);
		int keyStateD = glfwGetKey(window, GLFW_KEY_D);
		int keyStateW = glfwGetKey(window, GLFW_KEY_W);
		int keyStateSpace = glfwGetKey(window, GLFW_KEY_SPACE);
		if (keyStateA == GLFW_PRESS) {
			ship.rotate(-150f);
			// Key 'A' is pressed
		}
		if (keyStateD == GLFW_PRESS) {
			ship.rotate(150f);
			// Key 'D' is pressed
		}
		if (keyStateW == GLFW_PRESS) {
			ship.updateAcceleration(800.0f, true);
			// Key 'W' is pressed
		}
		if (keyStateW == GLFW_RELEASE) {
			ship.updateAcceleration(0, false);
			// Key 'W' is released
		}
		if (keyStateSpace == GLFW_PRESS && !spacePressed) {
			ship.projectiles.add(new Projectile(ship));
			spacePressed = true;
			// Key 'Space' is pressed
		}
		if (keyStateSpace == GLFW_RELEASE) {
			spacePressed = false;
			// Key 'Space' is released
		}
	}

	public static void main(String[] args) {
		Main main = new Main();
		main.loop();
	}
}