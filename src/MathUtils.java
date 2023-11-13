import java.util.Random;

public final class MathUtils {
	private MathUtils() {
	}

	public static int randomNumber(int min, int max) {
		return (int) (Math.random() * (max - min + 1)) + min; // Both min and max are inclusive.
	}

	public static int calculateDistance(int x1, int y1, int x2, int y2) {
		int dx = x2 - x1;
		int dy = y2 - y1;
		return (int) Math.sqrt(dx * dx + dy * dy);
	}

	public static float calculateVectorLength(float x, float y) {
		return (float) Math.sqrt(x*x+y*y);
	}

	public static float[] getRandomPointInCircle(float centerX, float centerY, float radius) {
		Random random = new Random();

		double angle = 2.0 * Math.PI * random.nextDouble();

		double randomRadius = radius * Math.sqrt(random.nextDouble());

		float x = (float) (centerX + randomRadius * Math.cos(angle));
		float y = (float) (centerY + randomRadius * Math.sin(angle));

		return new float[]{x, y};
	}

}
