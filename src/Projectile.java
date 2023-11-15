import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glColor3f;

public class Projectile extends Entity {
	private final float originRotation;
	private float red, green, blue;

	public Projectile(Ship sourceShip) {
		red = 1.0f;
		green = 0.0f;
		blue = 0.0f;
		centreX = sourceShip.centreX;
		centreY = sourceShip.centreY;
		originRotation = sourceShip.rotation;
		draw();
	}

	public Projectile(UFO sourceUfo, float targetAngle) {
		red = 0.0f;
		green = 1.0f;
		blue = 0.0f;
		centreX = sourceUfo.centreX;
		centreY = sourceUfo.centreY;
		originRotation = targetAngle;
		draw();
	}

	void calculateVelocity() {
		float velocityX = (float) Math.sin(Math.toRadians(originRotation));
		float velocityY = (float) Math.cos(Math.toRadians(originRotation));
		float finalVelocityX = velocityX * Main.PROJECTILE_SPEED;
		float finalVelocityY = velocityY * Main.PROJECTILE_SPEED;
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

		glColor3f(red, green, blue);
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
