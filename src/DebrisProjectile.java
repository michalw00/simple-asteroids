import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class DebrisProjectile extends Entity {
	private float initialX, initialY;
	private int speedModifier;
	private double spawnAngle;

	public DebrisProjectile(float circleCentreX, float circleCentreY, int circleRadius) {
		float[] coordinates = MathUtils.getRandomPointInCircle(circleCentreX, circleCentreY, circleRadius);
		spawnAngle = Math.toRadians(MathUtils.randomNumber(1, 360));
		centreX = coordinates[0];
		centreY = coordinates[1];
		initializeSpeedModifier();
		draw();
	}

	private void initializeSpeedModifier() {
		speedModifier = Main.ASTEROID_SPEED * 5;
	}

	public void updateMovement() {
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
	void draw() {
		glPushMatrix();
		glTranslatef(centreX, centreY, 0.0f);

		glColor3f(1.0f, 1.0f, 1.0f);
		glBegin(GL_QUADS);
		glVertex2f(-1.0f, -1.0f);
		glVertex2f(1.0f, -1.0f);
		glVertex2f(1.0f, 1.0f);
		glVertex2f(-1.0f, 1.0f);
		glEnd();

		glPopMatrix();


	}
}
