package at.ac.htlperg.squarebeard.space;

public class Vector implements Cloneable {

	private double x;
	private double y;

	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public static Vector of(double x, double y) {
		return new Vector(x, y);
	}
	
	public static Vector ofAngle(double radians, double lenght) {
		Vector vec = new Vector(Math.cos(radians), Math.sin(radians));
		vec.mult(lenght);
		return vec;
	}

	public static Vector of(Direction direction, double lenght) {
		switch (direction) {
		case NORTH:
			return new Vector(0, -lenght);
		case SOUTH:
			return new Vector(0, lenght);
		case EAST:
			return new Vector(lenght, 0);
		case WEST:
			return new Vector(-lenght, 0);
		default:
			return new Vector(0, 0);
		}
	}

	public void add(Vector other) {
		this.x += other.x;
		this.y += other.y;
	}

	public void subtract(Vector other) {
		this.x -= other.x;
		this.y -= other.y;
	}
	
	public void mult(double d) {
		this.x *= d;
		this.y *= d;
	}
	
	public void div(double d) {
		this.x /= d;
		this.y /= d;
	}

	public double abs() {
		return Math.sqrt(x * x + y * y);
	}
	
	public Vector normalize() {
		Vector clone = this.clone();
		clone.div(this.abs());
		return clone;
	}

	@Override
	public Vector clone() {
		return new Vector(x, y);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

}
