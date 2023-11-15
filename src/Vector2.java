public class Vector2 {
    public float X = 0.0f, Y = 0.0f;

    static Vector2 fromAngle(double radians) {
        Vector2 result = new Vector2();
        //double radians = Math.toRadians((double) degrees);
        result.X = 0.0f + (float) (Math.sin(radians));
        result.Y = 0.0f - (float) (Math.cos(radians));
        return result;
    }

    public Vector2 perpendicular() {
        Vector2 result = new Vector2();
        result.X = +Y;
        result.Y = -X;
        return result;
    }

    public Vector2 opposite() {
        Vector2 result = new Vector2();
        result.X = -X;
        result.Y = -Y;
        return result;
    }

    public float dot() {
        float result = X * X + Y * Y;
        return result;
    }

    public float dot(Vector2 V) {
        float result = X * V.X + Y * V.Y;
        return result;
    }

    public float length() {
        float result = (float) Math.sqrt((double) dot());
        return result;
    }

    public Vector2 mul(float scalar) {
        Vector2 result = new Vector2();
        result.X = X * scalar;
        result.Y = Y * scalar;
        return result;
    }

    public Vector2 mul(Vector2 V) {
        Vector2 result = new Vector2();
        result.X = X * V.X;
        result.Y = Y * V.Y;
        return result;
    }

    public Vector2 div(float scalar) {
        Vector2 result = new Vector2();
        result.X = X / scalar;
        result.Y = Y / scalar;
        return result;
    }

    public Vector2 div(Vector2 V) {
        Vector2 result = new Vector2();
        result.X = X / V.X;
        result.Y = Y / V.Y;
        return result;
    }

    public Vector2 add(float scalar) {
        Vector2 result = new Vector2();
        result.X = X+scalar;
        result.Y = Y+scalar;
        return result;
    }

    public Vector2 add(Vector2 V) {
        Vector2 result = new Vector2();
        result.X = X + V.X;
        result.Y = Y + V.Y;
        return result;
    }

    public Vector2 sub(float scalar) {
        Vector2 result = new Vector2();
        result.X = X - scalar;
        result.Y = Y - scalar;
        return result;
    }

    public Vector2 sub(Vector2 V) {
        Vector2 result = new Vector2();
        result.X = X - V.X;
        result.Y = Y - V.Y;
        return result;
    }

    public static Vector2 V2(float x, float y)
    {
        Vector2 result = new Vector2();
        result.X = x;
        result.Y = y;
        return result;
    }

    public Vector2 normalize()
    {
        Vector2 result = V2(X,Y);
        float length = length();
        if(length>0.0f) {
            result.X /= length;
            result.Y /= length;
        }
        return result;
    }
}
