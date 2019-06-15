package at.ac.htlperg.squarebeard.objects;

import java.util.logging.Logger;

import at.ac.htlperg.squarebeard.MainGame;
import at.ac.htlperg.squarebeard.events.DelayedEvent;
import at.ac.htlperg.squarebeard.events.UpdateEvent;
import at.ac.htlperg.squarebeard.io.IOUtil;
import at.ac.htlperg.squarebeard.level.Level;
import at.ac.htlperg.squarebeard.objects.tiles.Tile;
import at.ac.htlperg.squarebeard.space.Position;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

public class Mine extends GameObject {

	private static Image image = IOUtil.loadImage("assets/images/mine/mine.png");
	
	private static Media explosionSound = IOUtil.loadSound("assets/sounds/explosion.mp3");
	private MediaPlayer player = new MediaPlayer(explosionSound);

	private State state = State.IDLE;

	private static final Logger log = Logger.getLogger(Mine.class.getName());
	private static final int ACTIVE_FRAMES = 7;

	static {
		if (image == null) {
			log.warning("Unable to load image");
			image = MainGame.debugImage();
		} else {
			log.info("Image successfully loaded");
		}
		Explosion.class.getName();
	}

	public Mine(Level level, Position position) {
		super(level, position);
	}

	public Mine(Level level, Position position, double width, double height) {
		super(level, position, width, height);
	}
	
	@Override
	public void render(GraphicsContext context) {
		if (state == State.ACTIVATED && getDelta() % 2 == 0) {
			context.save();
			context.setFill(Color.RED);
			context.fillRect(getPositionX(), getPositionY(), getWidth(), getHeight());
			context.restore();
		} else {
			super.render(context);
		}
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		if (getColliding().contains(getLevel().getPlayer())) {
			if (state == State.IDLE) {
				state = State.ACTIVATED;
				addDelayedEvent(new DelayedEvent(ACTIVE_FRAMES, this::explode));
			}
		}
	}

	private void explode(GameObject self) {
		log.info("Exploding");
		player.setVolume(0.5);
		player.play();
		getLevel().removeObject(this);
		Explosion explosion = new Explosion(getLevel(), getPosition(), Tile.TILE_SIZE, Tile.TILE_SIZE, 4);
		getLevel().addObject(explosion);
		explosion.start();
		for (GameObject obj : getLevel().getObjects()) {
			if (this.distanceTo(obj) <= Tile.TILE_SIZE * 2) {
				if (obj instanceof Mine) {
					obj.addDelayedEvent(new DelayedEvent(2, mine -> ((Mine) mine).explode(mine)));
				}
			}
		}
	}

	@Override
	public Image getImage() {
		return image;
	}

	private static enum State {
		IDLE, ACTIVATED
	}

}
