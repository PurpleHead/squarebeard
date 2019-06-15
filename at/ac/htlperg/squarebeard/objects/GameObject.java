package at.ac.htlperg.squarebeard.objects;

import at.ac.htlperg.squarebeard.Renderable;
import at.ac.htlperg.squarebeard.Resetable;
import at.ac.htlperg.squarebeard.events.DelayedEvent;
import at.ac.htlperg.squarebeard.events.UpdateEvent;
import at.ac.htlperg.squarebeard.level.Level;
import at.ac.htlperg.squarebeard.objects.tiles.Tile;
import at.ac.htlperg.squarebeard.space.Position;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import util.Pair;

public abstract class GameObject implements Renderable, Resetable {

	private Level level;
	private Position position;
	private double width;
	private double height;
	private List<DelayedEvent> delayedEvents = new LinkedList<>();
	private UpdateEvent lastUpdateEvent;

	public GameObject(Level level, Position position) {
		this(level, position, Tile.TILE_SIZE, Tile.TILE_SIZE);
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public GameObject(Level level, Position position, double width, double height) {
		super();
		this.level = level;
		this.position = position;
		this.width = width;
		this.height = height;
	}

	@Override
	public void render(GraphicsContext context) {
		context.drawImage(getImage(), position.getPositionX(), position.getPositionY(), getWidth(), getHeight());
	}

	public boolean isColliding(GameObject other) {
		return this.getPositionX() < other.getPositionX() + other.getWidth() - 1
				&& other.getPositionX() < this.getPositionX() + this.getWidth() - 1
				&& this.getPositionY() < other.getPositionY() + other.getHeight() - 1
				&& other.getPositionY() < this.getPositionY() + this.getHeight() - 1 && this != other;
	}

	public List<GameObject> getColliding() {
		List<GameObject> objects = getLevel().getObjects();
		List<GameObject> temp = new LinkedList<GameObject>();
		for (GameObject obj : objects) {
			if (isColliding(obj)) {
				temp.add(obj);
			}
		}
		return temp;
	}

	public List<Pair<Integer, Integer>> getCollidingTileIndeces() {
		List<Pair<Integer, Integer>> colliding = new LinkedList<>();
		Pair<Integer, Integer> pair1 = getPosition().asArrayIndex();
		Pair<Integer, Integer> pair2 = new Position(getPositionX() + getWidth(), getPositionY() + getHeight())
				.asArrayIndex();
		Pair<Integer, Integer> pair3 = new Position(getPositionX() + getWidth(), getPositionY()).asArrayIndex();
		Pair<Integer, Integer> pair4 = new Position(getPositionX(), getPositionY() + getHeight()).asArrayIndex();

		colliding.add(pair1);
		if (!pair1.equals(pair2))
			colliding.add(pair2);
		if (!pair1.equals(pair3))
			colliding.add(pair3);
		if (!pair1.equals(pair4))
			;
		colliding.add(pair4);

		return colliding;
	}

	public List<Tile> getCollidingTiles() {
		List<Tile> colliding = new LinkedList<>();
		for (Pair<Integer, Integer> pair : getCollidingTileIndeces()) {
			if (pair._1 <= getLevel().getTiles().length && pair._2 <= getLevel().getTiles()[0].length && pair._1 >= 0 && pair._2 >= 0) {
				colliding.add(getLevel().getTiles()[pair._1][pair._2]);
			}
		}
		return colliding;
	}

	public double distanceTo(GameObject other) {
		return Math.sqrt(Math.pow(this.getPositionX() - other.getPositionX(), 2)
				+ Math.pow(this.getPositionY() - other.getPositionY(), 2));
	}

	public void addDelayedEvent(DelayedEvent event) {
		delayedEvents.add(event);
	}

	public int getDamagePoints() {
		return 0;
	}

	public void update(UpdateEvent event) {
		lastUpdateEvent = event;
		ListIterator<DelayedEvent> iter = delayedEvents.listIterator();
		while (iter.hasNext()) {
			DelayedEvent e = iter.next();
			if (e.getTimeout() <= 0) {
				e.getCallback().accept(this);
				iter.remove();
			} else {
				e.setTimeout(e.getTimeout() - 1);
			}
		}
		onUpdate(event);
	}
	
	public boolean isTranslucent(){
		return true;
	}
	
	protected void removeSelf() {
		getLevel().removeObject(this);
	}

	public abstract Image getImage();

	public abstract void onUpdate(UpdateEvent event);

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public Position getPosition() {
		return position;
	}

	public double getPositionX() {
		return position.getPositionX();
	}

	public double getPositionY() {
		return position.getPositionY();
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public void setPosition(int x, int y) {
		this.position = new Position(x, y);
	}

	public long getDelta() {
		return lastUpdateEvent.getDelta();
	}

	@Override
	public void reset() {
	}

	public boolean removeOnReset() {
		return false;
	}

}