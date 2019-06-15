package at.ac.htlperg.squarebeard.level;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import at.ac.htlperg.squarebeard.MainGame;
import at.ac.htlperg.squarebeard.Renderable;
import at.ac.htlperg.squarebeard.Resetable;
import at.ac.htlperg.squarebeard.io.IOUtil;
import at.ac.htlperg.squarebeard.objects.GameObject;
import at.ac.htlperg.squarebeard.objects.Player;
import at.ac.htlperg.squarebeard.objects.bars.Infobar;
import at.ac.htlperg.squarebeard.objects.ray.Ray;
import at.ac.htlperg.squarebeard.objects.tiles.Tile;
import at.ac.htlperg.squarebeard.space.Position;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Level implements Renderable, Resetable {

	private Tile[][] tiles;
	private List<GameObject> objects = new LinkedList<>();
	private List<Ray> rays = new LinkedList<>();
	private Infobar bar;
	private Player player;
	private GraphicsContext context;
	private static Image winImage;

	private int frames = 0;
	private int lastFrames = 0;
	private long time = System.currentTimeMillis();

	private static final Logger log = Logger.getLogger(Level.class.getName());

	static {
		if (MainGame.COOL_MODE) {
			winImage = IOUtil.loadImage("assets/images/win/win_cool.png");
		} else {
			winImage = IOUtil.loadImage("assets/images/win/win.png");
		}
	}

	public Level(int w, int h) {
		this.tiles = new Tile[w][h];
		this.bar = new Infobar(player);
	}

	public void addObject(GameObject object) {
		objects.add(object);
	}

	public void removeObject(GameObject object) {
		objects.remove(object);
	}

	public void removeRay(Ray ray) {
		rays.remove(ray);
	}

	@Override
	public void render(GraphicsContext context) {

		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				tiles[i][j].render(context);
			}
		}
		for (GameObject obj : new LinkedList<>(objects)) {
			obj.render(context);
			if (MainGame.debugMode) {
				context.setStroke(Color.RED);
				context.strokeRect(obj.getPositionX(), obj.getPositionY(), obj.getWidth(), obj.getHeight());
			}
		}
		player.render(context);
		if (MainGame.debugMode) {
			context.setStroke(Color.YELLOW);
			context.strokeRect(player.getPositionX(), player.getPositionY(), player.getWidth(), player.getHeight());
		}
		
		bar.render(context);

		context.save();
		context.setFont(Infobar.font);
		context.setFill(Color.WHITE);
		context.fillText(lastFrames + " FPS", 10, 35);
		context.restore();
		
		if (System.currentTimeMillis() - time >= 1000) {
			lastFrames = frames;
			frames = 0;
			time = System.currentTimeMillis();
		}
		
		frames++;
	}

	public Tile[][] getTiles() {
		return tiles;
	}

	public List<GameObject> getObjects() {
		List<GameObject> temp = new LinkedList<>(objects);
		temp.add(player);
		return temp;
	}

	public void setObjects(List<GameObject> objects) {
		this.objects = objects;
	}

	public void setTiles(Tile[][] tiles) {
		this.tiles = tiles;
	}

	public Infobar getBar() {
		return bar;
	}

	public void setBar(Infobar bar) {
		this.bar = bar;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
		getBar().setPlayer(player);
	}

	public GraphicsContext getContext() {
		return context;
	}

	public void setContext(GraphicsContext context) {
		this.context = context;
	}

	public void win() {
		log.info("Complete Level");
		MainGame.getGameLoop().clearCodes();
		if (MainGame.levels.isEmpty() && MainGame.loadingThread.isFinished()) {
			log.info("Complete Game");
			MainGame.getGameLoop().stop();
			Platform.runLater(() -> {
				Stage winScene = new Stage();
				winScene.setResizable(false);
				winScene.setScene(new Scene(new BorderPane(new Canvas(900, 900))));
				((Canvas) ((BorderPane) winScene.getScene().getRoot()).getCenter()).getGraphicsContext2D()
						.drawImage(winImage, 0, 0, 900, 900);
				((Canvas) ((BorderPane) winScene.getScene().getRoot()).getCenter()).getGraphicsContext2D()
						.setFill(Color.WHEAT);
				((Canvas) ((BorderPane) winScene.getScene().getRoot()).getCenter()).getGraphicsContext2D()
						.setFont(Font.font("Comic Sans MS", 42));
				((Canvas) ((BorderPane) winScene.getScene().getRoot()).getCenter()).getGraphicsContext2D()
						.fillText("Wäl dan", 0, 700);
				winScene.showAndWait();
			});
		} else {
			loadLevelData(MainGame.levels.remove());
		}
	}

	@Override
	public void reset() {
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				tiles[i][j].reset();
			}
		}
		ListIterator<GameObject> iterator = (ListIterator<GameObject>) objects.iterator();
		while (iterator.hasNext()) {
			GameObject obj = iterator.next();
			if (obj.removeOnReset()) {
				iterator.remove();
			} else {
				obj.reset();
			}
		}
		rays.clear();
		player.reset();
	}

	public void loadLevelData(LevelData data) {
		log.info("Loading Level Data + " + data.toString());
		setTiles(data.getTiles());
		setPlayer(MainGame.getPlayer());
		getPlayer().setPosition(data.getPlayerPos()._1, data.getPlayerPos()._2);
		getPlayer().setSpawnPosition(new Position(data.getPlayerPos()._1, data.getPlayerPos()._2));
		setObjects(new LinkedList<>(data.getObjects()));
		reset();
	}

	public List<Ray> getRays() {
		return rays;
	}

	public void setRays(List<Ray> rays) {
		this.rays = rays;
	}

}
