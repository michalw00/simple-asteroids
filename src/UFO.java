import static org.lwjgl.opengl.GL11.*;

public class UFO extends Entity { //todo
	private static final int NUMBER_OF_SEGMENTS = 15;
	private static final int RADIUS = 10;
	private int speedModifier;
	private final float initialX, initialY;
	private final float[][] vertexCoordinates;
	private final float[][] innerVertexCoordinates;
	private float rotation;
	private final double spawnAngle;

	public UFO() {
		rotation = 0;
		vertexCoordinates = new float[2][NUMBER_OF_SEGMENTS];
		innerVertexCoordinates = new float[2][NUMBER_OF_SEGMENTS];

		spawnAngle = Math.toRadians(MathUtils.randomNumber(1, 360));
		initialX = (float) Main.WINDOW_WIDTH/2;
		initialY = (float) Main.WINDOW_HEIGHT/2;
		this.centreX = initialX;
		this.centreY = initialY;
		initializeSpeedModifier();
		initializeVertexCoordinates();
	}

	private void initializeSpeedModifier() {
		if (RADIUS < 20) {
			speedModifier = Main.ASTEROID_SPEED * 9;
		} else speedModifier = Main.ASTEROID_SPEED;
	}

	private void initializeVertexCoordinates() {
		float xScale = 1.5f;
		float yScale = 0.5f;

		for (int i = 0; i < NUMBER_OF_SEGMENTS; i++) { // Outer circle
			float angle = 2.0f * (float) Math.PI * i / NUMBER_OF_SEGMENTS;
			vertexCoordinates[0][i] = (float) (Math.cos(angle) * RADIUS * xScale);
			vertexCoordinates[1][i] = (float) (Math.sin(angle) * RADIUS * yScale);
		}

		for (int i = 0; i < NUMBER_OF_SEGMENTS; i++) { // Inner circle
			float angle = 2.0f * (float) Math.PI * i / NUMBER_OF_SEGMENTS;
			innerVertexCoordinates[0][i] = (float) (Math.cos(angle) * RADIUS/2 * xScale);
			innerVertexCoordinates[1][i] = (float) (Math.sin(angle) * RADIUS/2 * yScale);
		}

	}

	@Override
	void move(float moveX, float moveY) {

	}

	@Override
	void draw() {
		glPushMatrix();
		glTranslatef(centreX, centreY, 0.0f);
		glRotatef(rotation, 0, 0, 1.0f);
		glBegin(GL_LINE_LOOP);

		for (int i = 0; i < NUMBER_OF_SEGMENTS; i++) {
			glVertex2f(vertexCoordinates[0][i], vertexCoordinates[1][i]);
			//glVertex2f(innerVertexCoordinates[0][i], innerVertexCoordinates[1][i]);
		}

		glEnd();
		glPopMatrix();
		glPushMatrix();
		glTranslatef(centreX, centreY-5, 0.0f);
		glRotatef(rotation, 0, 0, 1.0f);
		glBegin(GL_LINE_LOOP);

		for (int i = 0; i < NUMBER_OF_SEGMENTS; i++) {
			glVertex2f(innerVertexCoordinates[0][i], innerVertexCoordinates[1][i]);
		}

		glEnd();
		glPopMatrix();

	}
}
