package at.ac.htlperg.squarebeard.objects;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import at.ac.htlperg.squarebeard.MainGame;
import at.ac.htlperg.squarebeard.events.UpdateEvent;
import at.ac.htlperg.squarebeard.io.IOUtil;
import at.ac.htlperg.squarebeard.level.Level;
import at.ac.htlperg.squarebeard.space.Position;
import javafx.scene.image.Image;

public class Explosion extends AnimatedObject {

	private static final int MAX_IMAGES = 4;
	private long lastTime = 0;
	private boolean damaged = false;
	private int damage;
	private static List<Image> frames = new LinkedList<>();
	
	private static final Logger log = Logger.getLogger(Explosion.class.getName());
	
	static {
		for (int i = 0; i < MAX_IMAGES; i++) {
			Image image = IOUtil.loadImage("assets/images/explosion/explosion" + i + ".png");
			if (image == null) {
				log.log(java.util.logging.Level.WARNING, "Unable to load frame " + i + " of animation");
				image = MainGame.debugImage();
			} else {
				log.info("Frame " + i + " successfully loaded");
			}
			frames.add(image);
		}
	}

	public Explosion(Level level, Position position, double width, double height, int damage) {
		this(level, position, width, height, damage, false);
	}
	
	public Explosion(Level level, Position position, double width, double height, int damage, boolean autoStart) {
		super(level, position, width, height);
		this.damage = damage;
		for (Image img : frames) {
			getImages().add(img);
		}
		if (autoStart) {
			start();
		}
	}

	@Override
	protected void nextImage() {
		if (getCurrentImage() < getImages().size() - 1  && isRunning()) {
			setCurrentImage(getCurrentImage() + 1);
		} else {
			getLevel().removeObject(this);
			setRunning(false);
		}
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		if (isRunning()) {
			if(event.getDelta() - lastTime > 1 || lastTime == 0) {
				lastTime = event.getDelta();
				nextImage();
			}
		}
		if (getColliding().contains(getLevel().getPlayer())) {
			if(!damaged) {
				getLevel().getPlayer().damage(damage);
				damaged = true;
			}
		}
	}

	@Override
	public void start() {
		setRunning(true);
	}

}
