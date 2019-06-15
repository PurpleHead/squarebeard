package at.ac.htlperg.squarebeard.objects.tiles;

import java.util.logging.Logger;

import at.ac.htlperg.squarebeard.MainGame;
import at.ac.htlperg.squarebeard.events.UpdateEvent;
import at.ac.htlperg.squarebeard.io.IOUtil;
import at.ac.htlperg.squarebeard.level.Level;
import at.ac.htlperg.squarebeard.space.Position;
import javafx.scene.image.Image;

public class GroundTile extends Tile {

	private static Image image = IOUtil.loadImage("assets/images/tiles/ground.png");
	
	private static final Logger log = Logger.getLogger(GroundTile.class.getName());
	
	static {
		if (image == null) {
			log.warning("Unable to load image");
			image = MainGame.debugImage();
		} else {
			log.info("Image successfully loaded");
		}
	}
	
	public GroundTile(Level level, Position pos) {
		super(level, pos);
	}

	@Override
	public Image getImage() {
		return image;
	}

	@Override
	public boolean isSolid() {
		return false;
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		// TODO Auto-generated method stub
		
	}

}
