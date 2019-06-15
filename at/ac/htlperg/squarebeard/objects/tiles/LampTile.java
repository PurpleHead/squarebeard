package at.ac.htlperg.squarebeard.objects.tiles;

import java.util.logging.Logger;

import at.ac.htlperg.squarebeard.events.UpdateEvent;
import at.ac.htlperg.squarebeard.io.IOUtil;
import at.ac.htlperg.squarebeard.level.Level;
import at.ac.htlperg.squarebeard.objects.ray.Ray;
import at.ac.htlperg.squarebeard.space.Position;
import at.ac.htlperg.squarebeard.space.Vector;
import javafx.scene.image.Image;

public class LampTile extends Tile {
	
	private static final Logger log = Logger.getLogger(LampTile.class.getName());

	public LampTile(Level level, Position pos) {
		super(level, pos);
	}

	@Override
	public Image getImage() {
		return IOUtil.loadImage("/assets/images/tiles/lamp.png");
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		log.info(getPosition().toString());
		for(double angle = 0; angle <= Math.PI * 2; angle+=0.03) {
			getLevel().addObject(new Ray(getLevel(), this.getPosition().clone(Tile.TO_CENTER), Vector.ofAngle(angle, 1), 29));
		}
	}
	
	@Override
	public boolean isSolid() {
		return true;
	}
	
	@Override
	public boolean isTranslucent() {
		return true;
	}

}
