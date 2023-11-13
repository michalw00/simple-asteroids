import java.util.ListIterator;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class DebrisProjectile extends Entity {
	private float initialX, initialY;
	private int speedModifier;
	private double spawnAngle;
	Vector2 Velocity = Vector2.V2(0.0f,0.0f);
	public DebrisProjectile(float circleCentreX, float circleCentreY, int circleRadius) {
		float[] coordinates = MathUtils.getRandomPointInCircle(circleCentreX, circleCentreY, circleRadius);
		spawnAngle = Math.toRadians(MathUtils.randomNumber(1, 360));
		centreX = coordinates[0];
		centreY = coordinates[1];
		speedModifier = Main.ASTEROID_SPEED * 5;
		draw();

		float directionX = -((float) (Math.sin(spawnAngle)));
		float directionY = -(-(float) (Math.cos(spawnAngle)));
		Velocity.X = speedModifier * directionX;
		Velocity.Y = speedModifier * directionY;
	}

	public void updateMovement() {
		move(Velocity.X * Main.deltaTime, Velocity.Y * Main.deltaTime);

		ListIterator<Asteroid> iterator = Main.asteroids.listIterator();
		while (iterator.hasNext()) {
			Asteroid asteroid = iterator.next();

			Vector2 A = Vector2.V2(asteroid.centreX,asteroid.centreY);
			Vector2 B = Vector2.V2(centreX,centreY);
			Vector2 C = B.sub(A);
			float Distance = (float)Math.sqrt((double)C.dot());
			if(Distance<asteroid.radius) {
				Vector2 Normal = C.normalize();
				Velocity = Normal.mul(speedModifier);
			}
		}
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
