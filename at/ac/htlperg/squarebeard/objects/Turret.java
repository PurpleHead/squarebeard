package at.ac.htlperg.squarebeard.objects;

import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

import at.ac.htlperg.squarebeard.MainGame;
import at.ac.htlperg.squarebeard.events.UpdateEvent;
import at.ac.htlperg.squarebeard.io.IOUtil;
import at.ac.htlperg.squarebeard.level.Level;
import at.ac.htlperg.squarebeard.objects.tiles.Tile;
import at.ac.htlperg.squarebeard.space.Direction;
import at.ac.htlperg.squarebeard.space.Position;
import at.ac.htlperg.squarebeard.space.Vector;
import javafx.scene.image.Image;

public class Turret extends GameObject {

	private Image image = null;;
	private long timeout;
	private Direction direction;
	private static final long TIMEOUT = 30;

	private static Image upImage;
	private static Image downImage;
	private static Image leftImage;
	private static Image rightImage;

	private static final Logger log = Logger.getLogger(Turret.class.getName());

	static {
		if (MainGame.COOL_MODE) {
			upImage = IOUtil.loadImage("assets/images/turret/cool/turret_up.png");
			downImage = IOUtil.loadImage("assets/images/turret/cool/turret_down.png");
			leftImage = IOUtil.loadImage("assets/images/turret/cool/turret_left.png");
			rightImage = IOUtil.loadImage("assets/images/turret/cool/turret_right.png");
		} else {
			upImage = IOUtil.loadImage("assets/images/turret/turret_up.png");
			downImage = IOUtil.loadImage("assets/images/turret/turret_down.png");
			leftImage = IOUtil.loadImage("assets/images/turret/turret_left.png");
			rightImage = IOUtil.loadImage("assets/images/turret/turret_right.png");
		}
		if (upImage == null) {
			log.warning("Unable to load up image");
			upImage = MainGame.debugImage();
		} else {
			log.info("Up image successfully loaded");
		}

		if (downImage == null) {
			log.warning("Unable to load down image");
			downImage = MainGame.debugImage();
		} else {
			log.info("Down image successfully loaded");
		}

		if (leftImage == null) {
			log.warning("Unable to load left image");
			leftImage = MainGame.debugImage();
		} else {
			log.info("Left image successfully loaded");
		}

		if (rightImage == null) {
			log.warning("Unable to load right image");
			rightImage = MainGame.debugImage();
		} else {
			log.info("Right image successfully loaded");
		}
	}

	public Turret(Level level, Position position, Direction direction) {
		super(level, position);
		this.timeout = ThreadLocalRandom.current().nextLong(50);
		this.direction = direction;
		setTexture(direction);
	}

	protected void setTexture(Direction direction) {
		switch (direction) {
		case NORTH:
			image = upImage;
			break;
		case SOUTH:
			image = downImage;
			break;
		case EAST:
			image = rightImage;
			break;
		case WEST:
			image = leftImage;
			break;
		}
	}

	@Override
	public Image getImage() {
		return image;
	}

	protected void shoot() {
		Position pos = getPosition().clone();
		Bullet bullet = new Bullet(getLevel(), pos, Vector.of(direction, Tile.TILE_SIZE / 10));
		fire(bullet);
	}

	protected final void fire(Bullet bullet) {
		bullet.getPosition().addToX((Tile.TILE_SIZE / 2) - bullet.getWidth() / 2);
		bullet.getPosition().addToY((Tile.TILE_SIZE / 2) - bullet.getHeight() / 2);
		getLevel().addObject(bullet);
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		timeout--;
		if (timeout <= 0) {
			shoot();
			timeout = TIMEOUT;
		}
	}

	@Override
	public void reset() {
		this.timeout = ThreadLocalRandom.current().nextLong(50);
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public static long getTimeoutConstant() {
		return TIMEOUT;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	@Override
	public boolean isTranslucent() {
		return false;
	}

}
