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

}
