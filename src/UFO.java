import java.util.ArrayList;
import java.util.Iterator;

import static org.lwjgl.opengl.GL11.*;

public class UFO extends Entity {
	private static final int NUMBER_OF_SEGMENTS = 15;
	private static final int RADIUS = 10;
	private final float[][] vertexCoordinates;
	private final float[][] innerVertexCoordinates;
	public int hitboxRadius = 10;
	public ArrayList<Projectile> projectiles;
	public float gunRotation;
	private float velocity;

	public UFO() {
		projectiles = new ArrayList<>();
		vertexCoordinates = new float[2][NUMBER_OF_SEGMENTS];
		innerVertexCoordinates = new float[2][NUMBER_OF_SEGMENTS];
		gunRotation = 0;

		double spawnAngle = Math.toRadians(MathUtils.randomNumber(1, 360));
		float initialX = (float) (Main.WINDOW_WIDTH / 2 + Main.ASTEROID_SPAWN_RADIUS * Math.cos(spawnAngle));
		float initialY = (float) (Main.WINDOW_WIDTH / 2 - Main.ASTEROID_SPAWN_RADIUS * Math.sin(spawnAngle));
		velocity = 100.0f;
		this.centreX = initialX;
		this.centreY = initialY;
		initializeVertexCoordinates();
	}

	private void initializeVertexCoordinates() {
		float xScale = 1.5f;
		float yScale = 0.5f;

		for (int i = 0; i < NUMBER_OF_SEGMENTS; i++) { // Outer circle
			float angle = 2.0f * (float) Math.PI * i / NUMBER_OF_SEGMENTS;
			vertexCoordinates[0][i] = (float) (Math.cos(angle) * RADIUS * xScale);
			vertexCoordinates[1][i] = (float) (Math.sin(angle) * RADIUS * yScale);
		}

		for (int i = 0; i < NUMBER_OF_SEGMENTS; i++) { // Inner circle
			float angle = 2.0f * (float) Math.PI * i / NUMBER_OF_SEGMENTS;
			innerVertexCoordinates[0][i] = (float) (Math.cos(angle) * RADIUS/2 * xScale);
			innerVertexCoordinates[1][i] = (float) (Math.sin(angle) * RADIUS/2 * yScale);
		}

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

	public boolean ufoIsHit() {
Iterator<Projectile> iterator = Main.ship.projectiles.iterator();
		while (iterator.hasNext()) {
			Projectile projectile = iterator.next();
			int distance1 = MathUtils.calculateDistance((int)projectile.centreX, (int)projectile.centreY, (int)centreX, (int)centreY);
			if (distance1 < 10 + hitboxRadius) { // if ship projectile hits UFO
				Main.score += 500;
				iterator.remove();
				return true;
			}
		}
		return false;
	}

	public float findTarget() {
		float offsetDegrees = 80.0f;
		float inaccuracy = (float) MathUtils.randomNumber(-10, 10);

		float angle = (float) Math.atan2((int)Main.ship.centreY - (int)centreY, (int)Main.ship.centreX - (int)centreX);
		float adjustedAngle = (float) (angle + Math.toRadians(offsetDegrees) + Math.toRadians(inaccuracy));
		return (float) Math.toDegrees(adjustedAngle);
	}

	public void updateMovement() {
		float acceleration = (float) Math.cos(System.currentTimeMillis() * 0.0005);
		velocity = velocity + 100.0f;
		velocity = (float) Math.cos(velocity) * velocity * acceleration * Main.deltaTime;
		move(velocity, velocity);
	}

	@Override
	void move(float moveX, float moveY) {
		centreX += moveX;
		centreY -= moveY;
	}

	@Override
	void draw() {
		glPushMatrix();
		glTranslatef(centreX, centreY, 0.0f);
		glBegin(GL_LINE_LOOP);

		for (int i = 0; i < NUMBER_OF_SEGMENTS; i++) {
			glVertex2f(vertexCoordinates[0][i], vertexCoordinates[1][i]);
		}

		glEnd();
		glPopMatrix();
		glPushMatrix();
		glTranslatef(centreX, centreY-5, 0.0f);
		glBegin(GL_LINE_LOOP);

		for (int i = 0; i < NUMBER_OF_SEGMENTS; i++) {
			glVertex2f(innerVertexCoordinates[0][i], innerVertexCoordinates[1][i]);
		}

		glEnd();
		glPopMatrix();

	}
}
