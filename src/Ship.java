import java.util.ArrayList;
import static org.lwjgl.opengl.GL11.*;

public class Ship extends Entity {
	public int radius;
	private float velocityX, velocityY, acceleration;
	private final float maxAcceleration, maxSpeed, friction;
	public float rotation;
	public ArrayList<Projectile> projectiles;

	public Ship() {
		projectiles = new ArrayList<>();
		rotation = 0;
		maxAcceleration = 100.0f;
		maxSpeed = 200.0f;
		friction = 0.4f;
		radius = 1;
		centreX = Main.WINDOW_WIDTH/2.0f;
		centreY = Main.WINDOW_HEIGHT/2.0f+100.0f;
		draw();
	}

	public void rotate(float angle) {
		rotation += angle * Main.deltaTime;
	}

	public void updateAcceleration(float increase, boolean playerIsAccelerating) {
		if (playerIsAccelerating) {
			if (acceleration < maxAcceleration) acceleration = acceleration + increase;
			acceleration = Math.min(acceleration, maxAcceleration);
			updateVelocity();
		} else {
			acceleration = 0;
			velocityX -= velocityX * friction * Main.deltaTime;
			velocityY -= velocityY * friction * Main.deltaTime;
			move(velocityX, velocityY);
		}
	}

	private void updateVelocity() {
		velocityX += acceleration * Math.sin(rotation * Math.PI / 180) * Main.deltaTime;
		velocityY += acceleration * Math.cos(rotation * Math.PI / 180) * Main.deltaTime;

		float speed = MathUtils.calculateVectorLength(velocityX, velocityY);
		if (speed > maxSpeed) {
			float ratio = maxSpeed / speed;
			velocityX *= ratio;
			velocityY *= ratio;
		}

		move(velocityX, velocityY);
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
		glRotatef(rotation, 0.0f, 0.0f, 1.0f);

		glBegin(GL_LINE_LOOP);
		glColor3f(1.0f, 1.0f, 1.0f);
		glVertex2f(-4.0f, 0.0f);
		glVertex2f(4.0f, 0.0f);
		glVertex2f(0.0f, -15.0f);
		glEnd();

		glBegin(GL_LINES);
		glColor3f(1.0f, 0.0f, 0.0f);
		glVertex2f(0.0f, 0.0f);
		float lineLength;
		lineLength = (float) (Math.log(acceleration + 1)/Math.log(2) * 100);
		lineLength = Math.min(lineLength, 10);
		glVertex2f(0, lineLength);
		glEnd();
		glColor3f(1.0f, 1.0f, 1.0f);

		glPopMatrix();
	}
}
