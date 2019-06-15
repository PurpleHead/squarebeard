package at.ac.htlperg.squarebeard;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import at.ac.htlperg.squarebeard.gameloop.GameLoop;
import at.ac.htlperg.squarebeard.io.IOUtil;
import at.ac.htlperg.squarebeard.level.Level;
import at.ac.htlperg.squarebeard.level.LevelData;
import at.ac.htlperg.squarebeard.level.loading.CSVLevelLoader;
import at.ac.htlperg.squarebeard.level.loading.LevelLoader;
import at.ac.htlperg.squarebeard.level.loading.LevelLoadingThread;
import at.ac.htlperg.squarebeard.logging.LoggingWindow;
import at.ac.htlperg.squarebeard.objects.Player;
import at.ac.htlperg.squarebeard.objects.tiles.Tile;
import at.ac.htlperg.squarebeard.space.Position;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

public class MainGame extends Application {

	public final String GAME_NAME = "Square-Beard";
	private static Image debugImage = null;

	// Constants
	public static final double SCREEN_WIDTH = Tile.TILE_SIZE * 30;
	public static final double SCREEN_HEIGHT = Tile.TILE_SIZE * 30 + 100;

	// Global Variables
	public static final Queue<LevelData> levels = new LinkedList<>();
	public static LevelLoadingThread loadingThread = null;

	public static boolean debugMode = false;
	public static boolean COOL_MODE = false;
	public static boolean playMusic = !false;
	public static final boolean LOG_TO_FILE = false;

	public static final LoggingWindow logWindow = LoggingWindow.swingInstance();
	public static DiscordRichPresence state;

	// Basic Layout
	private Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
	private GraphicsContext context = canvas.getGraphicsContext2D();
	private BorderPane pane = new BorderPane(canvas);
	private Scene scene = new Scene(pane);
	private static Level level = new Level(30, 30);
	private static Player player = null;

	// Levels
	private static LevelLoader levelLoader = new CSVLevelLoader();
	private static Queue<File> levelFiles = new LinkedList<>();

	private static GameLoop gameLoop;
	private static final Logger log = Logger.getLogger(MainGame.class.getName());

	private MediaPlayer audioPlayer = new MediaPlayer(IOUtil.loadSound("assets/sounds/music/mainmusic.mp3"));

	static {
		LogManager.getLogManager().reset();
		Logger rootLogger = LogManager.getLogManager().getLogger("");
		if (LOG_TO_FILE) {
			long time = System.currentTimeMillis();
			System.out.println("Logging to " + System.getProperty("user.home") + "\\wow\\logs");
			File logFile = new File(System.getProperty("user.home") + "\\wow\\logs\\" + time + ".log");
			logFile.getParentFile().mkdirs();
			try {
				FileHandler fh = new FileHandler(System.getProperty("user.home") + "\\wow\\logs\\" + time + ".log");
				fh.setLevel(java.util.logging.Level.INFO);
				log.addHandler(fh);
				SimpleFormatter formatter = new SimpleFormatter();
				fh.setFormatter(formatter);
			} catch (SecurityException e) {
			} catch (IOException e) {
			}

		}

		rootLogger.addHandler(logWindow.newHandler());
		rootLogger.setLevel(java.util.logging.Level.INFO);
		player = new Player(level, new Position(0, 0), "Name");
	}

	public static void main(String[] args) {
		log.info("Launching...");
		loadingThread = new LevelLoadingThread(levelFiles, levels, levelLoader);
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		log.info("Initializing Discord Rich presence");
		DiscordRPC.discordInitialize("573136425646555171", new DiscordEventHandlers(), true);
		if (!debugMode) {
			addLevelFiles();
		} else {
			levelFiles.add(IOUtil.resourceFile("levels/debug.csv"));
		}
		log.info("Starting Level loading Thread");
		loadingThread.start();

		log.info("Loading Logo");
		Image logo = IOUtil.loadImage("assets/images/logo/logo_small.png");
		log.info("Initializing Menu");
		BorderPane pane = new BorderPane();
		BorderPane options = new BorderPane();
		Scene menuScene = new Scene(pane);
		menuScene.getStylesheets().add(STYLESHEET_MODENA);
		Button startButton = new Button("Start Game!");
		TextField nameField = new TextField();
		CheckBox showLog = new CheckBox("Show In-Game Log");
		CheckBox debugBox = new CheckBox("Debug-Mode");
		CheckBox musicBox = new CheckBox("Play Music");

		nameField.setPromptText("Name");

		pane.setCenter(startButton);
		pane.setTop(new ImageView(logo));
		pane.setRight(nameField);
		pane.setBottom(options);
		options.setTop(showLog);
		options.setLeft(debugBox);
		options.setBottom(musicBox);

		primaryStage.setTitle("Square-Beard | Menu");
		primaryStage.setScene(menuScene);
		primaryStage.setResizable(!false);
		primaryStage.show();

		startButton.setOnAction(event -> {
			try {
				debugMode = debugBox.isSelected();
				playMusic = musicBox.isSelected();
				enterName(nameField.getText());
				startGame(primaryStage, showLog.isSelected());
			} catch (IOException | URISyntaxException | InterruptedException e) {
				e.printStackTrace();
			}
		});
	}

	private void addLevelFiles() {
		log.info("Adding level files to Queue");

		File folder = IOUtil.resourceFile("levels");
		Arrays.stream(folder.listFiles(new FilenameFilter () {

			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith("level");
			}
		})).filter(file -> file.getName().startsWith("level"))
		.forEach(e -> {
			levelFiles.add(e);
			log.log(java.util.logging.Level.FINE, "Adding level from {}", e.getAbsolutePath());
		});

		log.info("Done adding Level files");
	}

	private void enterName(String name) {
		if (name.length() > 16) {
			name = name.substring(0, 16);
		}
		if (name != null) {
			player.setName(name);
		}

		log.info("Player name set to " + name);
	}

	private void startGame(Stage primaryStage, boolean showLog)
			throws IOException, URISyntaxException, InterruptedException {
		if (showLog) {
			logWindow.show();
		}

		if(playMusic) {
			audioPlayer.play();
		}
		
		if (!debugMode) {
			state = new DiscordRichPresence.Builder("Playing as " + player.getName())
					.setBigImage("logo", "Square-Beard").setStartTimestamps(System.currentTimeMillis())
					.setSmallImage("star", "Square-Beard").build();
		} else {
			log.info("Debug mode is active");
			player.setName("");
			state = new DiscordRichPresence.Builder("Playing in Debug-Mode").setBigImage("logo", "Square-Beard")
					.setStartTimestamps(1556668800).setSmallImage("star", "Square-Beard").build();
		}
		DiscordRPC.discordUpdatePresence(state);

		gameLoop = new GameLoop(timeoutVsync(30), level, scene, context); //Vsync

		primaryStage.setScene(scene);
		primaryStage.setTitle(GAME_NAME);

		log.info("Starting game");

		while (levels.isEmpty()) {
			Thread.sleep(0);
		}
		level.loadLevelData(levels.remove());
		level.setContext(context);

		log.info("Starting game loop");
		gameLoop.start();
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		if (gameLoop != null) {
			log.info("Stopping game loop");
			gameLoop.stop();
		}
		log.info("Shuting down Rich presence");
		DiscordRPC.discordShutdown();
		log.info("Stopping game");
		logWindow.close();
	}

	private long timeoutVsync(long fps) {
		return 1000 / fps;
	}

	public static Player getPlayer() {
		return player;
	}

	public static Level getLevel() {
		return level;
	}

	public static GameLoop getGameLoop() {
		return gameLoop;
	}

	public static Image debugImage() {
		if (debugImage == null) {
			log.info("Starting to generate debug image");
			Instant start = Instant.now();
			debugImage = new WritableImage(Tile.TILE_SIZE, Tile.TILE_SIZE);

			PixelWriter writer = ((WritableImage) debugImage).getPixelWriter();
			for (int i = 0; i < Tile.TILE_SIZE; i++) {
				for (int j = 0; j < Tile.TILE_SIZE; j++) {
					if (i < Tile.TILE_SIZE / 2) {
						if (j < Tile.TILE_SIZE / 2) {
							writer.setColor(i, j, Color.DEEPPINK);
						} else {
							writer.setColor(i, j, Color.BLACK);
						}
					} else {
						if (j < Tile.TILE_SIZE / 2) {
							writer.setColor(i, j, Color.BLACK);
						} else {
							writer.setColor(i, j, Color.DEEPPINK);
						}
					}
				}
			}

			log.info("Generated debug image in " + Duration.between(start, Instant.now()).toNanos() + "ns");
		}
		return debugImage;
	}

}
