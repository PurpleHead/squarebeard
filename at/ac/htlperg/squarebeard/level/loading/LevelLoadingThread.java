package at.ac.htlperg.squarebeard.level.loading;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Queue;
import java.util.logging.Logger;

import at.ac.htlperg.squarebeard.level.LevelData;

public class LevelLoadingThread extends Thread {
	
	private Queue<File> srcQueue;
	private Queue<LevelData> dstQueue;
	private LevelLoader loader;
	private boolean finished;
	
	private static final Logger log = Logger.getLogger(LevelLoadingThread.class.getName());
	
	public LevelLoadingThread(Queue<File> srcQueue, Queue<LevelData> dstQueue, LevelLoader loader) {
		super("Level Loading Thread");
		this.srcQueue = Objects.requireNonNull(srcQueue);
		this.dstQueue = Objects.requireNonNull(dstQueue);
		this.loader = Objects.requireNonNull(loader);
		this.finished = false;
	}
	
	@Override
	public void run() {
		log.info("Starting loading levels");
		LevelData data = null;
		while(!srcQueue.isEmpty()) {
			try {
				log.info("loading level from" + srcQueue.peek().getAbsolutePath());
				data = loader.load(srcQueue.remove());
				dstQueue.add(data);
			} catch (IOException e) {
			}
		}
		this.finished = true;
		log.info("Finished loading levels");
	}
	
	public boolean isFinished() {
		return finished;
	}

}
