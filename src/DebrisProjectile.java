import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class DebrisProjectile extends Entity {
	private float initialX, initialY;

	public DebrisProjectile(float circleCentreX, float circleCentreY, int circleRadius) {
		float[] coordinates = MathUtils.getRandomPointInCircle(circleCentreX, circleCentreY, circleRadius);
		centreX = coordinates[0];
		centreY = coordinates[1];
		draw();
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
