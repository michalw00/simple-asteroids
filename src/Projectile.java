import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glColor3f;

public class Projectile extends Entity {
	private int projectileSpeed = 450;
	float finalVelocityX;
	float finalVelocityY;
	float originRotation;
	private Ship sourceShip;

	public Projectile(Ship sourceShip) {
		this.sourceShip = sourceShip;
		centreX = sourceShip.centreX;
		centreY = sourceShip.centreY;
		originRotation = sourceShip.rotation;
		draw();
	}

	void calculateVelocity() {
		float velocityX =(float) Math.sin(Math.toRadians(originRotation));
		float velocityY = (float) Math.cos(Math.toRadians(originRotation));
		finalVelocityX = velocityX * projectileSpeed;
		finalVelocityY = velocityY * projectileSpeed;
		move(finalVelocityX, finalVelocityY);
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

		glColor3f(1.0f, 1.0f, 0.0f); // Set color to yellow
		glBegin(GL_QUADS); // Use GL_QUADS to draw a small rectangle
		glVertex2f(-1.0f, -1.0f); // Bottom-left vertex
		glVertex2f(1.0f, -1.0f);  // Bottom-right vertex
		glVertex2f(1.0f, 1.0f);   // Top-right vertex
		glVertex2f(-1.0f, 1.0f);  // Top-left vertex
		glEnd();
		glColor3f(1.0f, 1.0f, 1.0f); // Restore white color.

		glPopMatrix();
	}
}
