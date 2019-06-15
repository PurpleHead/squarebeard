package at.ac.htlperg.squarebeard.item;

import java.util.logging.Logger;

import at.ac.htlperg.squarebeard.MainGame;
import at.ac.htlperg.squarebeard.events.DelayedEvent;
import at.ac.htlperg.squarebeard.io.IOUtil;
import at.ac.htlperg.squarebeard.objects.Player;
import javafx.scene.image.Image;

public class SpeedBoostItem extends Item {

	private static Image image = IOUtil.loadImage("assets/images/items/speed_boost.png");
	
	private static final Logger log = Logger.getLogger(SpeedBoostItem.class.getName());
	
	private static final float SPEED_BOOST = 42f;
	
	static {
		if (image == null) {
			log.warning("Unable to load " + image.getUrl());
			image = MainGame.debugImage();
		} else {
			log.info("Image successfully loaded");
		}
	}
	
	public SpeedBoostItem() {
		super(image, "Speed Boost Potion");
	}

	@Override
	public void use(Player user) {
		user.setSpeedMultiplier(SPEED_BOOST);
		user.addDelayedEvent(new DelayedEvent(30, obj ->  {
			if (obj instanceof Player) {
				((Player)obj).setSpeedMultiplier(1f);
			}
		}));
	}

}
