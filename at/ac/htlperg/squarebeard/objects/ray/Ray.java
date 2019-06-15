package at.ac.htlperg.squarebeard.objects.ray;

import at.ac.htlperg.squarebeard.events.UpdateEvent;
import at.ac.htlperg.squarebeard.level.Level;
import at.ac.htlperg.squarebeard.objects.GameObject;
import at.ac.htlperg.squarebeard.objects.tiles.Tile;
import at.ac.htlperg.squarebeard.space.Position;
import at.ac.htlperg.squarebeard.space.Vector;
import javafx.scene.image.Image;

public class Ray extends GameObject {

	private Vector dir;
//	private Position pos;
	private double distance = 0;
	private boolean done = false;
	private final long living = System.currentTimeMillis();

	public Ray(Level level, Position position, Vector dir, double speed) {
		super(level, position, 1, 1);
		this.dir = dir.normalize();
		this.dir.mult(speed);
//		this.pos = position.clone();

	}

	@Override
	public Image getImage() {
		return null;
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		if (!done) {
			getPosition().add(dir);
			distance += Math.sqrt(Math.pow(dir.getX(), 2) + Math.pow(dir.getY(), 2));

			if (getLevel().getPlayer().isColliding(this)) {
//				removeSelf();
				distance += 100;
			} else
				for (Tile t : getCollidingTiles()) {
					t.lightLevel = 0 + (distance / 1000);
					if (t.isSolid() || !t.isTranslucent()) {
						done = true;
					}
				}
		}
	}

//	@Override
//	public void render(GraphicsContext context) {
//		if (MainGame.debugMode) {
//			context.save();
//			context.setStroke(Color.GREEN);
//			context.setLineWidth(1);
//			context.setGlobalAlpha(0.1);
//			context.strokeLine(pos.getPositionX(), pos.getPositionY(), getPositionX(), getPositionY());
//			context.restore();
//		}
//	}

	public long getLiving() {
		return living;
	}

}
