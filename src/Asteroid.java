import java.util.Iterator;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

public class Asteroid extends Entity {
	private final int numSegments, seed, speedModifier;
	public int radius;
	private final float initialX, initialY;
	private final double spawnAngle;


	public Asteroid(int numSegments, int radius) {
		this.numSegments = numSegments;
		this.radius = radius;
		seed = (int) (Math.random() * 50);
		speedModifier = (int) ((1.0f - Math.pow((float)radius / Main.MAX_RADIUS * 2, 2.0)) * Main.ASTEROID_SPEED);

		spawnAngle = Math.toRadians(MathUtils.randomNumber(1, 360));
		initialX = (float) (Main.WINDOW_WIDTH/2 + Main.ASTEROID_SPAWN_RADIUS * Math.cos(spawnAngle));
		initialY = (float) (Main.WINDOW_WIDTH/2 - Main.ASTEROID_SPAWN_RADIUS * Math.sin(spawnAngle));
		this.centreX = initialX;
		this.centreY = initialY;
		draw();
	}

	public Asteroid(int numSegments, int radius, float initialX, float initialY, double spawnAngle) {
		this.numSegments = numSegments;
		this.radius = radius;
		seed = (int) (Math.random() * 50);
		speedModifier = (int) ((1.0f - Math.pow((float)radius / Main.MAX_RADIUS * 2, 2.0)) * Main.ASTEROID_SPEED);

		this.spawnAngle = Math.toRadians(spawnAngle);
		this.initialX = initialX;
		this.initialY = initialY;
		this.centreX = initialX;
		this.centreY = initialY;
		draw();
	}

	boolean collision() {
		int distance = MathUtils.calculateDistance((int)Main.ship.centreX, (int)Main.ship.centreY, (int)centreX, (int)centreY);
		if (distance < Main.ship.radius + (radius - 10) && !Main.spawnProtection) { // if ship collides with asteroid
			Main.ship.respawn();
			Main.spawnProtection = true;
			Main.score -= 300;
		}

		if (Math.abs(centreX) > Main.WINDOW_WIDTH || Math.abs(centreY) > Main.WINDOW_HEIGHT) { // if asteroid hits the border
			// todo: need to add spawn protection first, so I can spawn asteroids out of border
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

	@Override
	void move(float moveX, float moveY) {
		float directionX = (float) (Main.WINDOW_WIDTH/2 + Main.ASTEROID_SPAWN_RADIUS * Math.cos((spawnAngle + 180) % 360)) - centreX;
		float directionY = (float) (Main.WINDOW_WIDTH/2 - Main.ASTEROID_SPAWN_RADIUS * Math.sin((spawnAngle + 180) % 360)) - centreY;

		float length = MathUtils.calculateVectorLength(directionX, directionY);
		directionX /= length;
		directionY /= length;

		float speedModifiedX = speedModifier * Main.deltaTime * directionX;
		float speedModifiedY = speedModifier * Main.deltaTime * directionY;
		centreX += speedModifiedX;
		centreY -= speedModifiedY;
	}

	@Override
	public void draw() {
		glPushMatrix();
		glBegin(GL_LINE_LOOP);

		Random randomSeries = new Random();
		randomSeries.setSeed(seed);

		for (int i = 0; i < this.numSegments; i++) {
			float angle = 2.0f * (float) Math.PI * i / this.numSegments;
			float maxDisplacement = 15.0f;
			float displacement = randomSeries.nextFloat() * maxDisplacement;
			float x = (float) (Math.cos(angle) * (radius - displacement));
			float y = (float) (Math.sin(angle) * (radius - displacement));
			glVertex2f(centreX + x, centreY + y); // todo: store x and y in an array; this doesn't have to be calculated everytime
		}

		glEnd();
		glPopMatrix();

	}
}
