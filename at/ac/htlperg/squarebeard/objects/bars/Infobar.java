package at.ac.htlperg.squarebeard.objects.bars;

import java.net.URISyntaxException;

import at.ac.htlperg.squarebeard.MainGame;
import at.ac.htlperg.squarebeard.io.IOUtil;
import at.ac.htlperg.squarebeard.item.Item;
import at.ac.htlperg.squarebeard.objects.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Infobar extends Bar {

	private Player player;
	private Font font = Font.loadFont(this.getClass().getResourceAsStream("../../../../../../assets/fonts/font.otf"),
			45);
	private Image heart;

	// TODO: Cool ausrechna
	public Infobar(Player player) {
		super(Color.LIGHTGRAY, MainGame.SCREEN_WIDTH, 100);
		this.setPlayer(player);

		try {
			heart = new Image(IOUtil.resourceURI("assets/images/player/heart.png").toString());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render(GraphicsContext context) {
		context.save();
		context.setFont(font);
		context.setFill(getFill());
		context.fillRect(0, MainGame.SCREEN_HEIGHT - getHeight(), getWidth(), getHeight());
		context.setFill(Color.DARKGRAY);
		context.fillRect(0, MainGame.SCREEN_HEIGHT - getHeight(), MainGame.SCREEN_WIDTH, getHeight() / 2);
		context.setFill(getFill().invert());
//		context.fillText(player.getName(), 5, ((MainGame.SCREEN_HEIGHT - getHeight() + getHeight() / 2) + context.getFont().getSize() / 3));
		context.fillText(player.getName(), 5, (MainGame.SCREEN_HEIGHT) - font.getSize() / 4);
		renderHearts(context);
		renderItems(context);

		context.restore();
	}

	private void renderItems(GraphicsContext context) {
		double width = 8;

		for (Item item : player.getItems()) {
			if (player.getItems().get(player.getCurrentItemIndex()).equals(item)) {
				context.setFill(Color.BLACK);
				context.fillRoundRect(width - 2.5, MainGame.SCREEN_HEIGHT - (getHeight() - 8) - 2.5, item.getImage().getWidth() + 5,
						item.getImage().getHeight() + 5, 20, 20);
			}
			context.setFill(Color.DIMGREY);
			context.fillRoundRect(width, MainGame.SCREEN_HEIGHT - (getHeight() - 8), item.getImage().getWidth(),
					item.getImage().getHeight(), 20, 20);

			context.drawImage(item.getImage(), width, MainGame.SCREEN_HEIGHT - (getHeight() - 8));
			width += item.getImage().getWidth() + 5;
		}
	}

	private void renderHearts(GraphicsContext context) {
		for (int i = 0; i < player.getLives(); i++) {
			context.drawImage(heart, MainGame.SCREEN_WIDTH - ((i + 1) * 47), MainGame.SCREEN_HEIGHT - 47);
		}
		context.setFill(Color.BLACK);
		context.fillRect(MainGame.SCREEN_WIDTH - ((Player.MAX_LIVES) * 49), MainGame.SCREEN_HEIGHT - getHeight(), 4,
				getHeight());
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;

	}

}
