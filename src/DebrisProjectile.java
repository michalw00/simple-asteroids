import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class DebrisProjectile extends Entity {
	private float initialX, initialY;
	private float Time;

	public DebrisProjectile(float circleCentreX, float circleCentreY, int circleRadius) {
		float[] coordinates = MathUtils.getRandomPointInCircle(circleCentreX, circleCentreY, circleRadius);
		centreX = coordinates[0];
		centreY = coordinates[1];
		Time = 0.0f;
		draw();
	}

	@Override
	void move(float moveX, float moveY) {
		centreX += moveX;
		centreY += moveY;
	}

	@Override
	void draw() {
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_COLOR);
		Time += Main.deltaTime;
		System.out.println(Time);
		glPushMatrix();
		glTranslatef(centreX, centreY, 0.0f);
		glScalef(32.0f*(1.0f-Time),32.0f*(1.0f-Time),0.0f);
		glTranslatef(-0.5f,-0.5f,0.0f);
		glBegin(GL_QUADS);
		glColor4f(1.0f, 1.0f, 1.0f, 1.0f - Time*1.4f);
		glVertex2f(0.0f,0.0f);
		glVertex2f(1.0f,0.0f);
		glVertex2f(1.0f,1.0f);
		glVertex2f(0.0f,1.0f);
		glEnd();
		glDisable(GL_BLEND);
		glPopMatrix();
	}
}
