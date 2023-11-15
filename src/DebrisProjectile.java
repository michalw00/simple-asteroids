import java.util.ListIterator;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class DebrisProjectile extends Entity {
	private final int speedModifier;
	private float red, green, blue;

	Vector2 Velocity = Vector2.V2(0.0f,0.0f);
	public DebrisProjectile(float circleCentreX, float circleCentreY, int circleRadius, float red, float green, float blue) {
		float[] coordinates = MathUtils.getRandomPointInCircle(circleCentreX, circleCentreY, circleRadius);
		double spawnAngle = Math.toRadians(MathUtils.randomNumber(1, 360));
		this.red = red;
		this.green = green;
		this.blue = blue;
		centreX = coordinates[0];
		centreY = coordinates[1];
		speedModifier = Main.ASTEROID_SPEED * 2;
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
			if (Distance<asteroid.radius) {
				Vector2 Normal = C.normalize();
				Velocity = Normal.mul(speedModifier);
			}
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

	@Override
	void move(float moveX, float moveY) {
		centreX += moveX;
		centreY += moveY;
	}

	@Override
	void draw() {
		glPushMatrix();
		glTranslatef(centreX, centreY, 0.0f);
		glScalef(0.8f, 0.8f, 1);

		glColor3f(red, green, blue);
		glBegin(GL_QUADS);
		glVertex2f(-1.0f, -1.0f);
		glVertex2f(1.0f, -1.0f);
		glVertex2f(1.0f, 1.0f);
		glVertex2f(-1.0f, 1.0f);
		glEnd();

		glPopMatrix();


	}
}
