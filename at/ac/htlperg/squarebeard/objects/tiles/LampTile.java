package at.ac.htlperg.squarebeard.objects.tiles;

import at.ac.htlperg.squarebeard.events.UpdateEvent;
import at.ac.htlperg.squarebeard.io.IOUtil;
import at.ac.htlperg.squarebeard.level.Level;
import at.ac.htlperg.squarebeard.objects.ray.Ray;
import at.ac.htlperg.squarebeard.space.Position;
import at.ac.htlperg.squarebeard.space.Vector;
import javafx.scene.image.Image;

public class LampTile extends Tile {

	public LampTile(Level level, Position pos) {
		super(level, pos);
	}

	@Override
	public Image getImage() {
		return IOUtil.loadImage("/assets/images/tiles/lamp.png");
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		for (double angle = 0; angle <= Math.PI * 2; angle += 0.02) {
			Ray ray = new Ray(getLevel(), this.getPosition().clone(Tile.TO_CENTER), Vector.ofAngle(angle, 1), 29);
			getLevel().getRays().add(ray);
		}

	}

	@Override
	public boolean isSolid() {
		return false;
	}

	@Override
	public boolean isTranslucent() {
		return true;
	}

}
