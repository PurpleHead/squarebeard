package at.ac.htlperg.squarebeard.gameloop;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import at.ac.htlperg.squarebeard.events.UpdateEvent;
import at.ac.htlperg.squarebeard.level.Level;
import at.ac.htlperg.squarebeard.objects.GameObject;
import at.ac.htlperg.squarebeard.objects.tiles.Tile;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class GameLoop implements EventHandler<KeyEvent> {

	private long timeout;
	private Level level;
	private Set<KeyCode> codes = new HashSet<>();
	private GraphicsContext context;

	private Thread loopThread;
	private long delta = 0;
	
	private static final Logger log = Logger.getLogger(GameLoop.class.getName());

	public static boolean running = true;

	public GameLoop(long timeout, Level level, Scene scene, GraphicsContext context) {
		this.setTimeout(timeout);
		this.setLevel(level);
		this.context = context;
		scene.setOnKeyPressed(this);
		scene.setOnKeyReleased(this);
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public void start() {
		loopThread = new Thread(() -> {
			log.info("Started game loop with timeout " + timeout + "ms");
			
			while (running) {
				update();
				try {
					Thread.sleep(timeout);
				} catch (InterruptedException e) {
				}
			}
			
			log.info("Game Loop stopped");
		}, "Game loop Thread");
		loopThread.start();
	}

	private void update() {
		UpdateEvent event = new UpdateEvent(codes, delta);
		level.getPlayer().onUpdate(event);
		for (GameObject o : level.getObjects()) {
			o.update(event);
		}
		for (Tile[] a : level.getTiles()) {
			for (Tile b : a) {
				b.onUpdate(event);
			}
		}
		Platform.runLater(() -> level.render(context));
		this.delta++;
	}

	@Override
	public void handle(KeyEvent event) {
		if (event.getEventType() == KeyEvent.KEY_PRESSED) {
			codes.add(event.getCode());
		} else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
			codes.remove(event.getCode());
		}
	}

	public void stop() {
		log.info("Stopping game loop");
		
		running = false;
	}
	
	public void clearCodes() {
		codes.clear();
	}
	
	public GraphicsContext getContext() {
		return context;
	}

	public Thread getLoopThread() {
		return loopThread;
	}

}
