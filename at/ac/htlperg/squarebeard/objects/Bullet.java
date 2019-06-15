package at.ac.htlperg.squarebeard.objects;

import at.ac.htlperg.squarebeard.events.UpdateEvent;
import at.ac.htlperg.squarebeard.level.Level;
import at.ac.htlperg.squarebeard.objects.tiles.Tile;
import at.ac.htlperg.squarebeard.space.Position;
import at.ac.htlperg.squarebeard.space.Vector;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import util.Pair;

public class Bullet extends GameObject {

	private Vector movement;

	public Bullet(Level level, Position position, Vector movement) {
		super(level, position, Tile.TILE_SIZE / 4, Tile.TILE_SIZE / 4);
		this.movement = movement;
	}

	@Override
	public Image getImage() {
		return null;
	}

	@Override
	public void render(GraphicsContext context) {
		context.save();

		Point2D center = new Point2D(getPositionX(), getPositionY());
		double radius = Tile.TILE_SIZE / 4;

		context.setFill(Color.web("#e2e200"));
		context.setStroke(Color.ORANGE);

		context.fillOval(center.getX(), center.getY(), radius, radius);
		context.strokeOval(center.getX(), center.getY(), radius, radius);

		context.restore();
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		this.getPosition().addToX(movement.getX());
		this.getPosition().addToY(movement.getY());

		if (isColliding(getLevel().getPlayer())) {
			onPlayerHit();
		}
		for (Pair<Integer, Integer> pair : getCollidingTileIndeces()) {
			if (getLevel().getTiles()[pair._1][pair._2].isSolid()) {
				onWallHit();
			}
		}
	}

	protected void onPlayerHit() {
		getLevel().removeObject(this);
		getLevel().getPlayer().damage(getDamagePoints());
	}

	protected void onWallHit() {
		getLevel().removeObject(this);
	}

	public Vector getMovement() {
		return movement;
	}

	public void setMovement(Vector movement) {
		this.movement = movement;
	}

	@Override
	public int getDamagePoints() {
		return 1;
	}
	
	@Override
	public boolean removeOnReset() {
		return true;
	}

}
