import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glColor3f;

public class Projectile extends Entity {
	private final float originRotation;

	public Projectile(Ship sourceShip) {
		centreX = sourceShip.centreX;
		centreY = sourceShip.centreY;
		originRotation = sourceShip.rotation;
		draw();
	}

	void calculateVelocity() {
		float velocityX = (float) Math.sin(Math.toRadians(originRotation));
		float velocityY = (float) Math.cos(Math.toRadians(originRotation));
		int projectileSpeed = 450;
		float finalVelocityX = velocityX * projectileSpeed;
		float finalVelocityY = velocityY * projectileSpeed;
		move(finalVelocityX, finalVelocityY);
	}

	public boolean outOfBorderCheck() {
		return (centreX < Main.WINDOW_WIDTH * 0.05f) ||
				(centreX > Main.WINDOW_WIDTH * 0.95f) ||
				(centreY < Main.WINDOW_HEIGHT * 0.05f) ||
				(centreY > Main.WINDOW_HEIGHT * 0.95f);
	}

	@Override
	void move(float moveX, float moveY) {
		centreX += moveX * Main.deltaTime;
		centreY -= moveY * Main.deltaTime;
	}

	@Override
	void draw() {
		glPushMatrix();
		glTranslatef(centreX, centreY, 0.0f);

		glColor3f(1.0f, 0.0f, 0.0f);
		glBegin(GL_QUADS);
		glVertex2f(-1.0f, -1.0f);
		glVertex2f(1.0f, -1.0f);
		glVertex2f(1.0f, 1.0f);
		glVertex2f(-1.0f, 1.0f);
		glEnd();
		glColor3f(1.0f, 1.0f, 1.0f);

		glPopMatrix();
	}
}
