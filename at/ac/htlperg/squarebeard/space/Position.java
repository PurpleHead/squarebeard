package at.ac.htlperg.squarebeard.space;

import at.ac.htlperg.squarebeard.objects.tiles.Tile;
import util.Pair;

public class Position implements Cloneable {

	private double positionX = 0;
	private double positionY = 0;

	public Position(double x, double y) {
		this.positionX = x;
		this.positionY = y;
	}

	public void addToX(double delta) {
		this.positionX += delta;
	}

	public void addToY(double delta) {
		this.positionY += delta;
	}

	public void setPosition(double x, double y) {
		this.positionX = x;
		this.positionY = y;
	}

	public double getPositionX() {
		return positionX;
	}

	public void setPositionX(double positionX) {
		this.positionX = positionX;
	}

	public double getPositionY() {
		return positionY;
	}

	public void setPositionY(double positionY) {
		this.positionY = positionY;
	}
	
	public void add(Vector vector) {
		this.addToX(vector.getX());
		this.addToY(vector.getY());
	}

	@Override
	public String toString() {
		return "Position [positionX=" + positionX + ", positionY=" + positionY + "]";
	}

	public Pair<Integer, Integer> asArrayIndex(int size) {
		int x = (int) Math.floor((positionX / size));
		int y = (int) Math.floor((positionY / size));
		return new Pair<Integer, Integer>(x, y);
	}
	
	public Pair<Integer, Integer> asArrayIndex() {
		return asArrayIndex(Tile.TILE_SIZE);
	}
	
	@Override
	public Position clone() {
		return new Position(positionX, positionY);
	}
	
	public Position clone(double xOff, double yOff) {
		return new Position(positionX + xOff, positionY + yOff);
	}
	
	public Position clone(Vector offset) {
		return clone(offset.getX(), offset.getY());
	}

}
