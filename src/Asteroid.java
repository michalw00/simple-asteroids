import java.util.Iterator;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

public class Asteroid extends Entity {
	private final int numSegments, seed;
	private int speedModifier;
	public int radius;
	private final float initialX, initialY;
	private final float[][] vertexCoordinates;
	private float rotation;
	private final double spawnAngle;


	public Asteroid(int numSegments, int radius) {
		this.numSegments = numSegments;
		this.radius = radius;
		rotation = 0;
		seed = (int) (Math.random() * 50);
		vertexCoordinates = new float[2][numSegments];

		spawnAngle = Math.toRadians(MathUtils.randomNumber(1, 360));
		initialX = (float) (Main.WINDOW_WIDTH/2 + Main.ASTEROID_SPAWN_RADIUS * Math.cos(spawnAngle));
		initialY = (float) (Main.WINDOW_WIDTH/2 - Main.ASTEROID_SPAWN_RADIUS * Math.sin(spawnAngle));
		this.centreX = initialX;
		this.centreY = initialY;
		initializeSpeedModifier();
		initializeVertexCoordinates();
	}

	public Asteroid(int numSegments, int radius, float initialX, float initialY, double spawnAngle) {
		this.numSegments = numSegments;
		this.radius = radius;
		rotation = 0;
		seed = (int) (Math.random() * 50);
		vertexCoordinates = new float[2][numSegments];

		this.spawnAngle = Math.toRadians(spawnAngle);
		this.initialX = initialX;
		this.initialY = initialY;
		this.centreX = initialX;
		this.centreY = initialY;
		initializeSpeedModifier();
		initializeVertexCoordinates();
	}

	private void initializeSpeedModifier() {
		if (radius < 20) {
			speedModifier = Main.ASTEROID_SPEED * 9;
		} else speedModifier = Main.ASTEROID_SPEED;
	}

	private void initializeVertexCoordinates() {
		Random randomSeries = new Random();
		randomSeries.setSeed(seed);

		for (int i = 0; i < this.numSegments; i++) {
			float angle = 2.0f * (float) Math.PI * i / this.numSegments;
			float maxDisplacement = 15.0f;
			float displacement = randomSeries.nextFloat() * maxDisplacement;
			vertexCoordinates[0][i] = (float) (Math.cos(angle) * (radius - displacement));
			vertexCoordinates[1][i] = (float) (Math.sin(angle) * (radius - displacement));
		}
	}

	public boolean collision() {
		int distance = MathUtils.calculateDistance((int)Main.ship.centreX, (int)Main.ship.centreY, (int)centreX, (int)centreY);
		if (distance < Main.ship.radius + (radius - 10) && !Main.spawnProtection) { // if ship collides with asteroid
			Main.ship.respawn();
		}

		Iterator<Projectile> iterator = Main.ship.projectiles.iterator();
		while (iterator.hasNext()) {
			Projectile projectile = iterator.next();
			int distance1 = MathUtils.calculateDistance((int)projectile.centreX, (int)projectile.centreY, (int)centreX, (int)centreY);
			if (distance1 < 1 + radius) { // if projectile hits asteroid
				Main.score += 1000 / radius;
				iterator.remove();
				return true;
			}
		}
		return false;
	}

	public void outOfBorderCheck() {
		if (centreX < 0.05f * Main.WINDOW_WIDTH) {
			centreX = 0.95f * Main.WINDOW_WIDTH;
		}
		if (centreX > 0.95f * Main.WINDOW_WIDTH) {
			centreX = 0.05f * Main.WINDOW_WIDTH;
		}
		if (centreY < 0.05f * Main.WINDOW_HEIGHT) {
			centreY = 0.95f * Main.WINDOW_HEIGHT;
		}
		if (centreY > 0.95f * Main.WINDOW_HEIGHT) {
			centreY = 0.05f * Main.WINDOW_HEIGHT;
		}
	}

	public void updateRotationAndMovement() {
		rotation = rotation + speedModifier * Main.deltaTime;
		float directionX = (float) (Math.sin(spawnAngle));
		float directionY = -(float) (Math.cos(spawnAngle));
		directionY = -directionY;
		directionX = -directionX;
		float speedModifiedX = speedModifier * Main.deltaTime * directionX;
		float speedModifiedY = speedModifier * Main.deltaTime * directionY;
		move(speedModifiedX, speedModifiedY);
	}

	@Override
	void move(float moveX, float moveY) {
		centreX += moveX;
		centreY += moveY;
	}

	@Override
	public void draw() {
		glPushMatrix();
		glTranslatef(centreX, centreY, 0.0f);
		glRotatef(rotation, 0, 0, 1.0f);
		glBegin(GL_LINE_LOOP);

		Random randomSeries = new Random();
		randomSeries.setSeed(seed);

		for (int i = 0; i < this.numSegments; i++) {
			glVertex2f(vertexCoordinates[0][i], vertexCoordinates[1][i]);
		}

		glEnd();
		glPopMatrix();

	}
}
