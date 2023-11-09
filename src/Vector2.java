import java.util.Vector;

public class Vector2 {
    public float X = 0.0f, Y = 0.0f;

    static Vector2 FromAngle(double radians) {
        Vector2 result = new Vector2();
        //double radians = Math.toRadians((double) degrees);
        result.X = 0.0f + (float) (Math.sin(radians));
        result.Y = 0.0f - (float) (Math.cos(radians));
        return result;
    }
    public Vector2 Perpendicular() {
        Vector2 result = new Vector2();
        result.X = +Y;
        result.Y = -X;
        return result;
    }

    public Vector2 Opposite() {
        Vector2 result = new Vector2();
        result.X = -X;
        result.Y = -Y;
        return result;
    }

    public float Dot() {
        float result = X * X + Y * Y;
        return result;
    }

    public float Dot(Vector2 V) {
        float result = X * V.X + Y * V.Y;
        return result;
    }

    public float Length() {
        float result = (float) Math.sqrt((double) Dot());
        return result;
    }
   public Vector2 Mul(float scalar) {
        Vector2 result = new Vector2();
        result.X *= scalar;
        result.Y *= scalar;
        return result;
    }
    public Vector2 Mul(Vector2 V) {
        Vector2 result = new Vector2();
        result.X = X * V.X;
        result.Y = Y * V.Y;
        return result;
    }

    public Vector2 Div(float scalar) {
        Vector2 result = new Vector2();
        result.X /= scalar;
        result.Y /= scalar;
        return result;
    }
    public Vector2 Div(Vector2 V) {
        Vector2 result = new Vector2();
        result.X = X / V.X;
        result.Y = Y / V.Y;
        return result;
    }

    public Vector2 Add(float scalar) {
        Vector2 result = new Vector2();
        result.X += scalar;
        result.Y += scalar;
        return result;
    }
    public Vector2 Add(Vector2 V) {
        Vector2 result = new Vector2();
        result.X = X + V.X;
        result.Y = Y + V.Y;
        return result;
    }

    public Vector2 Sub(float scalar) {
        Vector2 result = new Vector2();
        result.X -= scalar;
        result.Y -= scalar;
        return result;
    }
    public Vector2 Sub(Vector2 V) {
        Vector2 result = new Vector2();
        result.X = X - V.X;
        result.Y = Y - V.Y;
        return result;
    }
}
