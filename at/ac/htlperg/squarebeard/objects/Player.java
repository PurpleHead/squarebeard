package at.ac.htlperg.squarebeard.objects;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import at.ac.htlperg.squarebeard.MainGame;
import at.ac.htlperg.squarebeard.events.UpdateEvent;
import at.ac.htlperg.squarebeard.io.IOUtil;
import at.ac.htlperg.squarebeard.item.Item;
import at.ac.htlperg.squarebeard.item.MoreHealthItem;
import at.ac.htlperg.squarebeard.item.SpeedBoostItem;
import at.ac.htlperg.squarebeard.level.Level;
import at.ac.htlperg.squarebeard.objects.tiles.ChestTile;
import at.ac.htlperg.squarebeard.objects.tiles.Tile;
import at.ac.htlperg.squarebeard.space.Position;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class Player extends GameObject {

	public static final int MAX_ITEM_COUNT = 10;
	private static Image image = IOUtil.loadImage("assets/images/player/player.png");
	private String name;
	public static final int MAX_LIVES = 5;
	private int lives = MAX_LIVES;
	private static final double SPEED = Tile.TILE_SIZE / 10;
	private Position spawnPosition;
	private List<Item> items = new LinkedList<>();
	private int currentItemIndex = 0;

	private float speedMultiplier = 1.0f;

	private long lastKeyInput = 0;
	private boolean canUseItem = true;

	private static final Logger log = Logger.getLogger(Player.class.getName());

	static {
		if (image == null) {
			log.warning("Unable to load image");
			image = MainGame.debugImage();
		} else {
			log.info("Image successfully loaded");
		}
	}

	public Player(Level level, Position position, String name) {
		super(level, position);
		this.name = name;

		if (MainGame.debugMode) {
			addAllItems(new MoreHealthItem(), new SpeedBoostItem());
		}
		this.spawnPosition = position.clone();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public Image getImage() {
		return image;
	}

	@Override
	public void render(GraphicsContext context) {
		context.save();
		context.drawImage(getImage(), getPosition().getPositionX(), getPosition().getPositionY(), Tile.TILE_SIZE,
				Tile.TILE_SIZE);
		context.restore();
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		if (event.isPressed(KeyCode.W)) {
			moveY(false);
		} else if (event.isPressed(KeyCode.S)) {
			moveY(true);
		}
		if (event.isPressed(KeyCode.A)) {
			moveX(false);
		} else if (event.isPressed(KeyCode.D)) {
			moveX(true);
		}
		updateItems(event);
		if (lives <= 0) {
			getLevel().reset();
		}
		if (event.isPressed(KeyCode.F)) {
			if (canUseItem) {
				items.remove(currentItemIndex).use(this);
				canUseItem = false;
				if (currentItemIndex == items.size()) {
					currentItemIndex--;
				}
			}
		} else {
			canUseItem = true;
		}
		checkWin();
	}

	private void updateItems(UpdateEvent event) {
		if (event.getDelta() - lastKeyInput >= 10) {
			if (event.isPressed(KeyCode.LEFT)) {
				currentItemIndex--;
				if (currentItemIndex < 0) {
					currentItemIndex = items.size() - 1;
				}
				lastKeyInput = event.getDelta();
			} else if (event.isPressed(KeyCode.RIGHT)) {
				currentItemIndex++;
				if (currentItemIndex > items.size() - 1) {
					currentItemIndex = 0;
				}
				lastKeyInput = event.getDelta();
			}
		}
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public void damage(int dmg) {
		if (dmg < 0) {
			throw new IllegalArgumentException("Damage must be greater than 0");
		}
		this.lives -= dmg;
	}

	@Override
	public void reset() {
		this.setPosition(spawnPosition.clone());
		this.lives = MAX_LIVES;
		if (MainGame.debugMode) {
			this.items.clear();
			addAllItems(new MoreHealthItem(), new SpeedBoostItem());
		}
	}

	@Override
	public void setPosition(int x, int y) {
		super.setPosition(x, y);
		this.spawnPosition = new Position(x, y);

	}

	@Override
	public void setPosition(Position position) {
		super.setPosition(position);
		this.spawnPosition = position.clone();
	}

	public int getCurrentItemIndex() {
		return currentItemIndex;
	}

	public void setCurrentItemIndex(int currentItemIndex) {
		this.currentItemIndex = currentItemIndex;
	}

	public void addItem(Item item) {
		if (items.size() < MAX_ITEM_COUNT)
			items.add(item);
	}

	public void addAllItems(Item... items) {
		for (Item i : items) {
			addItem(i);
		}
	}

	public List<Item> getItems() {
		return Collections.unmodifiableList(items);
	}

	public float getSpeedMultiplier() {
		return speedMultiplier;
	}

	public void setSpeedMultiplier(float speedMultiplier) {
		this.speedMultiplier = speedMultiplier;
	}

	public Position getSpawnPosition() {
		return spawnPosition;
	}

	public void setSpawnPosition(Position spawnPosition) {
		this.spawnPosition = spawnPosition;
	}

	private void moveY(boolean pos) {
		double sp = SPEED * speedMultiplier * (pos ? 1 : -1);
		this.getPosition().addToY(sp);
		while (onSolidTile()) {
			if (pos) {
				this.getPosition().addToY(-0.5);
			} else {
				this.getPosition().addToY(0.5);
			}
		}
	}

	private boolean onSolidTile() {
		for (Tile t : getCollidingTiles()) {
			if (t.isSolid()) {
				return true;
			}
		}
		return false;
	}

	private void moveX(boolean pos) {
		double sp = SPEED * speedMultiplier * (pos ? 1 : -1);
		this.getPosition().addToX(sp);
		while (onSolidTile()) {
			if (pos) {
				this.getPosition().addToX(-0.5);
			} else {
				this.getPosition().addToX(0.5);
			}
		}
	}

	private void checkWin() {
		for (Tile t : getCollidingTiles()) {
			if (t instanceof ChestTile) {
				getLevel().win();
			}
		}
	}

}
