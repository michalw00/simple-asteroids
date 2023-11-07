import java.util.Iterator;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

public class Asteroid extends Entity {
	private int numSegments, startingPosition, seed, positionRandomness, speedModifier;
	public int radius;

	public Asteroid(int numSegments, int radius) {
		this.numSegments = numSegments;
		this.radius = radius;
		seed = (int) (Math.random() * 50);
		speedModifier = (int)(1.0f - Math.pow(radius / Main.MAX_RADIUS*2, 2.0)) * 10;

		this.startingPosition = (int) (1 + (Math.random() * (9 - 1)));
		spawnAt(startingPosition);
		draw();

	}

	void spawnAt(int startingPosition) {
		switch (startingPosition) {
			case 1: // This one starts at upper left corner, and latter positions move clockwise.
				positionRandomness = MathUtils.randomNumber(0, (int) (Main.WINDOW_WIDTH/2.0f));
				centreX = positionRandomness;
				centreY = -Main.WINDOW_HEIGHT;
				break;
			case 2:
				positionRandomness = MathUtils.randomNumber((int) (-Main.WINDOW_WIDTH/4.0f), (int) (Main.WINDOW_WIDTH/4.0f));
				centreX = Main.WINDOW_WIDTH/2.0f + positionRandomness;
				centreY = -Main.WINDOW_HEIGHT;
				break;
			case 3:
				positionRandomness = MathUtils.randomNumber((int) (-Main.WINDOW_WIDTH/2.0f), 0);
				centreX = Main.WINDOW_WIDTH + positionRandomness;
				centreY = -Main.WINDOW_HEIGHT;
				break;
			case 4:
				positionRandomness = MathUtils.randomNumber((int) (-Main.WINDOW_HEIGHT/2.0f), (int) (Main.WINDOW_HEIGHT / 2.0f));
				centreX = Main.WINDOW_WIDTH;
				centreY = -Main.WINDOW_HEIGHT/2.0f + positionRandomness;
				break;
			case 5:
				positionRandomness = MathUtils.randomNumber((int) (-Main.WINDOW_WIDTH/2.0f), 0);
				centreX = Main.WINDOW_WIDTH + positionRandomness;
				centreY = Main.WINDOW_HEIGHT;
				break;
			case 6:
				positionRandomness = MathUtils.randomNumber((int) (-Main.WINDOW_WIDTH/4.0f), (int) (Main.WINDOW_WIDTH/4.0f));
				centreX = Main.WINDOW_WIDTH/2.0f + positionRandomness;
				centreY = Main.WINDOW_HEIGHT;
				break;
			case 7:
				positionRandomness = MathUtils.randomNumber(0, (int) (Main.WINDOW_WIDTH/2.0f));
				centreX = positionRandomness;
				centreY = Main.WINDOW_HEIGHT;
				break;
			case 8:
				positionRandomness = MathUtils.randomNumber((int) (-Main.WINDOW_HEIGHT/2.0f), (int) (Main.WINDOW_HEIGHT/2.0f));
				centreX = positionRandomness;
				centreY = -Main.WINDOW_HEIGHT/2.0f;
				break;
			default:
				System.err.println("This should not have happened.");
				System.exit(1);
		}
	}

	boolean collision() {
		int distance = MathUtils.calculateDistance((int)Main.ship.centreX, (int)Main.ship.centreY, (int)centreX, (int)centreY);
		if (distance < Main.ship.radius + (radius - 10)) { // if ship collides with asteroid
			System.out.println("Collision"); // todo: just temp, needs ship gets destroyed logic
		}

		if (Math.abs(centreX) > Main.WINDOW_WIDTH || Math.abs(centreY) > Main.WINDOW_HEIGHT) { // if asteroid hits the border
			System.out.println("Asteroid out of border!"); // todo: borders don't align with window fully in some places, try to fix
			startingPosition = MathUtils.randomNumber(1, 8);
			spawnAt(startingPosition);
		}

		Iterator<Projectile> iterator = Main.ship.projectiles.iterator();
		while (iterator.hasNext()) {
			Projectile projectile = iterator.next();
			int distance1 = MathUtils.calculateDistance((int)projectile.centreX, (int)projectile.centreY, (int)centreX, (int)centreY);
			if (distance1 < 1 + radius) { // if projectile hits asteroid
				iterator.remove();
				// todo: spawn smaller, faster asteroids
				return true;
			}
		}
		return false;
	}

	@Override
	void move(float moveX, float moveY) {
		moveX *= speedModifier * Main.deltaTime;
		moveY *= speedModifier * Main.deltaTime;
		switch (startingPosition) {
			case 1: // Upper left corner
				centreX += moveX;
				centreY += moveY;
				break;
			case 2: // Upper middle
				centreY += moveY;
				break;
			case 3: // Upper right corner
				centreX -= moveX;
				centreY += moveY;
				break;
			case 4: // Middle right
				centreX -= moveX;
				break;
			case 5: // Lower right corner
				centreX -= moveX;
				centreY -= moveY;
				break;
			case 6: // Lower middle
				centreY -= moveY;
				break;
			case 7: // Lower left corner
				centreX += moveX;
				centreY -= moveY;
				break;
			case 8: // Middle left
				centreX += moveX;
				break;
			default:
				System.err.println("This should not have happened.");
				System.exit(1);
		}
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
			glVertex2f(centreX + x, centreY + y); // todo: moglbym trzymac zmienne x i y w array'u, zamiast robic te obliczenia za kazdym razem
		}

		glEnd();
		glPopMatrix();

	}
}
