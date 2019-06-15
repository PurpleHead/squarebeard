package at.ac.htlperg.squarebeard.objects.tiles;

import at.ac.htlperg.squarebeard.level.Level;
import at.ac.htlperg.squarebeard.objects.GameObject;
import at.ac.htlperg.squarebeard.space.Position;
import at.ac.htlperg.squarebeard.space.Vector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public abstract class Tile extends GameObject {

	public static final double DEFAULT_LIGHTNING = 0.7;

	public static final int TILE_SIZE = 30;
	public static final Vector TO_CENTER = Vector.of(TILE_SIZE / 2, TILE_SIZE / 2);
	public double lightLevel = DEFAULT_LIGHTNING;

	public Tile(Level level, Position pos) {
		super(level, pos);
	}

	public abstract Image getImage();

	public abstract boolean isSolid();

	@Override
	public final void render(GraphicsContext context) {
		context.drawImage(getImage(), getPositionX(), getPositionY(), TILE_SIZE, TILE_SIZE);
		context.save();
		context.setGlobalAlpha(lightLevel);
		context.setFill(Color.BLACK);
		context.fillRect(getPositionX(), getPositionY(), TILE_SIZE, TILE_SIZE);
		context.restore();
		if (lightLevel != DEFAULT_LIGHTNING)
			lightLevel = DEFAULT_LIGHTNING;
	}

}
