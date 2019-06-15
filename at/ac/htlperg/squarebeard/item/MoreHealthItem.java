package at.ac.htlperg.squarebeard.item;

import java.util.logging.Logger;

import at.ac.htlperg.squarebeard.MainGame;
import at.ac.htlperg.squarebeard.io.IOUtil;
import at.ac.htlperg.squarebeard.objects.Player;
import javafx.scene.image.Image;

public class MoreHealthItem extends Item {

	private static Image image = IOUtil.loadImage("assets/images/items/more_health.png");
	
	private static final Logger log = Logger.getLogger(MoreHealthItem.class.getName());
	
	static {
		if (image == null) {
			log.warning("Unable to load image");
			image = MainGame.debugImage();
		} else {
			log.info("Image successfully loaded");
		}
	}

	public MoreHealthItem() {
		super(image, "More Health Potion");
	}

	@Override
	public void use(Player user) {
		user.setLives(Player.MAX_LIVES);
	}

}
