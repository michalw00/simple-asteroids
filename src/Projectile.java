import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glColor3f;

public class Projectile extends Entity {
	float finalVelocityX, finalVelocityY, originRotation;

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
		finalVelocityX = velocityX * projectileSpeed;
		finalVelocityY = velocityY * projectileSpeed;
		move(finalVelocityX, finalVelocityY);
	}

	public boolean outOfBorderCheck() {
		if (centreX < (float) Main.WINDOW_WIDTH * 0.05f) return true;
		if (centreX > (float) Main.WINDOW_WIDTH * 0.95f) return true;
		if (centreY < (float) Main.WINDOW_WIDTH * 0.05f) return true;
		if (centreY > (float) Main.WINDOW_WIDTH * 0.95f) return true;
		return false;
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
		glVertex2f(-1.0f, -1.0f); // Bottom-left vertex
		glVertex2f(1.0f, -1.0f);  // Bottom-right vertex
		glVertex2f(1.0f, 1.0f);   // Top-right vertex
		glVertex2f(-1.0f, 1.0f);  // Top-left vertex
		glEnd();
		glColor3f(1.0f, 1.0f, 1.0f);

		glPopMatrix();
	}
}
