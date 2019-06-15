package at.ac.htlperg.squarebeard.objects;

import at.ac.htlperg.squarebeard.level.Level;
import at.ac.htlperg.squarebeard.objects.tiles.Tile;
import at.ac.htlperg.squarebeard.space.Position;
import at.ac.htlperg.squarebeard.space.Vector;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SpreadingBullet extends Bullet {
	
	private final double sqrt2 = Math.sqrt(2);

	public SpreadingBullet(Level level, Position position, Vector movement) {
		super(level, position, movement);
	}
	
	@Override
	public void render(GraphicsContext context) {
		context.save();

		Point2D center = new Point2D(getPositionX(), getPositionY());
		double radius = Tile.TILE_SIZE / 4;

		context.setFill(Color.RED);
		context.setStroke(Color.DARKRED);

		context.fillOval(center.getX(), center.getY(), radius, radius);
		context.strokeOval(center.getX(), center.getY(), radius, radius);

		context.restore();
	}
	
	@Override
	protected void onWallHit() {
		super.onWallHit();
		Vector move = getMovement().clone();
		move.setX(move.getX() * -1);
		move.setY(move.getY() * -1);
		getLevel().addObject(new Bullet(getLevel(), getPosition().clone(), move));
		move = move.clone();
		if (Math.abs(move.getX()) > Math.abs(move.getY())) {
			move.setX(move.getX() / sqrt2);
			move.setY(move.getX() / sqrt2);
			getLevel().addObject(new Bullet(getLevel(), getPosition().clone(), move));
			move = move.clone();
			move.setY(-move.getY());
			getLevel().addObject(new Bullet(getLevel(), getPosition().clone(), move));
		} else {
			move.setY(move.getY() / sqrt2);
			move.setX(move.getY() / sqrt2);
			getLevel().addObject(new Bullet(getLevel(), getPosition().clone(), move));
			move = move.clone();
			move.setX(-move.getX());
			getLevel().addObject(new Bullet(getLevel(), getPosition().clone(), move));
		}
	}
	
	@Override
	public int getDamagePoints() {
		return 2;
	}

}
