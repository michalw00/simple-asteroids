import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.ListIterator;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

class Main {
	public static final int WINDOW_WIDTH = 800, WINDOW_HEIGHT = 600;
	private static final String WINDOW_TITLE = "Asteroids";
	private final long window;
	private boolean spacePressed = false;
	private double previousTime;
	public static float deltaTime;

	public static final int MIN_RADIUS = 40, MAX_RADIUS = 65;
	public static final int MIN_SEGMENTS = 5, MAX_SEGMENTS = 9;
	public static final int ASTEROID_SPAWN_RADIUS = 500, ASTEROID_SPEED = 100;
	public static final int PROJECTILE_SPEED = 450;
	public static final int ASTEROID_INITIAL_AMOUNT = 6;
	public static final float ASTEROID_SPAWN_DELAY = 0.2f;
	public static final float PROJECTILE_SHOOT_DELAY = 0.3f;
	public static int asteroidLowerLimit = 1, asteroidUpperLimit = 15;

	public static Ship ship;
	public static ArrayList<Asteroid> asteroids;
	public static int score = 0; // 10*asteroid.radius per asteroid, life lost penalty score -= 300, game over score = 0
	public static int highScore = 0;
	public static int lives = 3;
	public static boolean spawnProtection = false;
	public static boolean isPaused = true;
	public static String menuState = "PRESS ENTER TO START";

	private float accumulatorAsteroidSpawn = 0.0f;
	private float accumulatorShootDelay = PROJECTILE_SHOOT_DELAY;
	private float accumulatorSpawnProtection = 0.0f;
	private float accumulatorSpawnProtectionInner = 0.0f;


	Main() {
		GLFWErrorCallback.createPrint(System.err).set();
		if (!glfwInit()) {
			System.err.println("Error: GLFW initialization failed.");
			System.exit(1);
		}
		window = GLFW.glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_TITLE, 0, 0);
		if (window == NULL) throw new RuntimeException("Failed to create the GLFW window.");
		GLFW.glfwMakeContextCurrent(window);
		GL.createCapabilities();
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glLoadIdentity();
		glOrtho(0, WINDOW_WIDTH, WINDOW_HEIGHT, 0, 0, 1.0);
	}

	private void gameLoop() {
		glClear(GL_COLOR_BUFFER_BIT);
		ship = new Ship();
		initializeAsteroids();


		while (!glfwWindowShouldClose(window)) {
			glClear(GL_COLOR_BUFFER_BIT);

			if (lives <= 0 && !isPaused) {
				if (highScore < score) highScore = score;
				restartGameState();
				isPaused = true;
				menuState = "PRESS ENTER TO RETRY";
			} else if (isPaused) menu(menuState);

			if (!isPaused) {
				// Asteroid spawn logic.
				accumulatorAsteroidSpawn += deltaTime;
				if (accumulatorAsteroidSpawn >= ASTEROID_SPAWN_DELAY && asteroids.size() <= asteroidLowerLimit) {
					if (asteroids.size() < asteroidUpperLimit) {
						asteroids.add(new Asteroid(MathUtils.randomNumber(MIN_SEGMENTS, MAX_SEGMENTS),
								MathUtils.randomNumber(MIN_RADIUS, MAX_RADIUS)));
						asteroidLowerLimit++;
					}
					accumulatorAsteroidSpawn = 0;
				}

				// Asteroid handling.
				ListIterator<Asteroid> iterator = asteroids.listIterator();
				while (iterator.hasNext()) {
					Asteroid asteroid = iterator.next();
					asteroid.outOfBorderCheck();
					asteroid.draw();
					asteroid.updateRotationAndMovement();
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
								iterator.add(new Asteroid(MathUtils.randomNumber(MIN_SEGMENTS, MAX_SEGMENTS),
										MathUtils.randomNumber(MIN_RADIUS / 2, MAX_RADIUS / 2), x, y, spawnAngle));
							}
						}
					}
				}

				// Projectile handling.
				ListIterator<Projectile> projectileListIterator = ship.projectiles.listIterator();
				while (projectileListIterator.hasNext()) {
					Projectile projectile = projectileListIterator.next();
					projectile.calculateVelocity();
					if (projectile.outOfBorderCheck()) projectileListIterator.remove();
					projectile.draw();
				}

				// Ship handling.
				if (!spawnProtection) {
					ship.draw();
					accumulatorSpawnProtection = 0;
				} else {
					accumulatorSpawnProtection += deltaTime;
					accumulatorSpawnProtectionInner += deltaTime;
					if (accumulatorSpawnProtection > 0.1f) {
						ship.draw();
						accumulatorSpawnProtection = 0.0f;
					}
					if (accumulatorSpawnProtectionInner > 5.0f) {
						spawnProtection = false;
						accumulatorSpawnProtection = 0;
						accumulatorSpawnProtectionInner = 0;
					}
				}
				ship.outOfBorderCheck();
				controller();

				// Etc.
				updateDeltaTime();
				drawPlayableAreaBorders();
				drawText("SCORE:" + score, 60.0f, 15.0f);
				drawText("LIVES:" + lives, 280.0f, 15.0f);
			}

			glfwSwapBuffers(window);
			glfwPollEvents();
		}


		glfwTerminate();
	}

	private void menu(String title) {
		drawText(title,WINDOW_WIDTH/3.0f - title.length(),WINDOW_HEIGHT/2.0f);
		drawText("HIGH SCORE:"+highScore,WINDOW_WIDTH/9.0f,WINDOW_HEIGHT/7.0f);
		int keyStateEnter = glfwGetKey(window, GLFW_KEY_ENTER);
		if (keyStateEnter == GLFW_PRESS) {
			isPaused = false;
		}
	}

	private void updateDeltaTime() {
		double time = glfwGetTime();
		float minStep = 1.0f;
		deltaTime = Math.min((float) (time - previousTime), minStep);
		previousTime = time;
	}

	private void drawPlayableAreaBorders() {
		glPushMatrix();

		glBegin(GL_LINE_LOOP);
		glColor3f(1.0f, 1.0f, 1.0f);
		glVertex2i(WINDOW_WIDTH * 5 / 100, WINDOW_HEIGHT * 5 / 100);  // Top-left corner
		glVertex2i(WINDOW_WIDTH - WINDOW_WIDTH * 5 / 100, WINDOW_HEIGHT * 5 / 100);  // Top-right corner
		glVertex2i(WINDOW_WIDTH - WINDOW_WIDTH * 5 / 100, WINDOW_HEIGHT * 95 / 100);  // Bottom-right corner
		glVertex2i(WINDOW_WIDTH * 5 / 100, WINDOW_HEIGHT * 95 / 100);  // Bottom-left corner
		glEnd();

		glPopMatrix();
	}

	private void initializeAsteroids() {
		asteroids = new ArrayList<>();
		for (int i = 0; i < ASTEROID_INITIAL_AMOUNT; i++) {
			asteroids.add(new Asteroid(MathUtils.randomNumber(MIN_SEGMENTS, MAX_SEGMENTS),
					MathUtils.randomNumber(MIN_RADIUS, MAX_RADIUS)));
		}
	}

	private void restartGameState() {
		asteroidLowerLimit = 1;
		asteroidUpperLimit = 15;

		ship = new Ship();
		initializeAsteroids();
		score = 0;
		lives = 3;
		spawnProtection = false;

		accumulatorAsteroidSpawn = 0.0f;
		accumulatorShootDelay = PROJECTILE_SHOOT_DELAY;
		accumulatorSpawnProtection = 0.0f;
		accumulatorSpawnProtectionInner = 0.0f;
	}

	private void controller() {
		int keyStateA = glfwGetKey(window, GLFW_KEY_A);
		int keyStateD = glfwGetKey(window, GLFW_KEY_D);
		int keyStateW = glfwGetKey(window, GLFW_KEY_W);
		int keyStateSpace = glfwGetKey(window, GLFW_KEY_SPACE);
		if (keyStateA == GLFW_PRESS) {
			ship.rotate(-150f);
		}
		if (keyStateD == GLFW_PRESS) {
			ship.rotate(150f);
		}
		if (keyStateW == GLFW_PRESS) {
			ship.updateAcceleration(6.0f, true);
		}
		if (keyStateW == GLFW_RELEASE) {
			ship.updateAcceleration(0, false);
		}
		accumulatorShootDelay += deltaTime;
		if (keyStateSpace == GLFW_PRESS && !spacePressed && accumulatorShootDelay > PROJECTILE_SHOOT_DELAY) {
			ship.projectiles.add(new Projectile(ship));
			spacePressed = true;
			accumulatorShootDelay = 0;
		}
		if (keyStateSpace == GLFW_RELEASE) {
			spacePressed = false;
		}
	}

	public static int[] Glyphs;
	public static void drawText(String text, float X, float Y) {
		//48 * 128

		if((Glyphs == null)) {
			Glyphs = new int[256];
			glGenTextures(Glyphs);

			int Codepoint = 0;
			for (int texture : Glyphs) {
				byte []pixels = new byte[8*8*4];

				int OffsetX = (Codepoint % 16) * 8;
				int OffsetY = (Codepoint / 16) * 8;
				Codepoint++;

				int Offset = ((OffsetY*Font.Stride)+OffsetX);
				if(Offset<Font.Pixels.length) {
					for (int y = 0; y < 8; y++) {
						for (int x = 0; x < 8; x++) {
							int index = y * 8 + x;
							int Color = Font.Pixels[Offset + ((y * Font.Stride + x))];
							pixels[(index * 4) + 0] = (byte) Color;
							pixels[(index * 4) + 1] = (byte) Color;
							pixels[(index * 4) + 2] = (byte) Color;
							pixels[(index * 4) + 3] = (byte) Color;
						}
					}
				}
				glBindTexture(GL_TEXTURE_2D, texture);
				glTexImage2D(GL_TEXTURE_2D, 0,GL_RGBA,8,8,0,GL_RGBA,GL_UNSIGNED_BYTE, ByteBuffer.allocateDirect(pixels.length).put(pixels).flip());
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,GL_NEAREST);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER,GL_NEAREST);
			}
		}

		float PixelHeight = 16.0f;
		for(int index = 0; index < text.length(); index++) {
			char Codepoint = text.charAt(index);
			Codepoint = Character.toUpperCase(Codepoint);
			glPushMatrix();
			glTranslatef(X, Y, 0.0f);
			glScalef(PixelHeight, PixelHeight, 0.0f);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glEnable(GL_TEXTURE_2D);

			glBindTexture(GL_TEXTURE_2D, Glyphs[Codepoint]);
			glBegin(GL_QUADS);
			glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			glTexCoord2f(0.0f, 0.0f);
			glVertex2f(0.0f, 0.0f);
			glTexCoord2f(1.0f, 0.0f);
			glVertex2f(1.0f, 0.0f);
			glTexCoord2f(1.0f, 1.0f);
			glVertex2f(1.0f, 1.0f);
			glTexCoord2f(0.0f, 1.0f);
			glVertex2f(0.0f, 1.0f);
			glEnd();
			glBindTexture(GL_TEXTURE_2D, 0);
			glDisable(GL_BLEND);
			glDisable(GL_TEXTURE_2D);
			glPopMatrix();
			X += PixelHeight;
		}
	}

	public static void main(String[] args) {
		Main main = new Main();
		main.gameLoop();
	}
}