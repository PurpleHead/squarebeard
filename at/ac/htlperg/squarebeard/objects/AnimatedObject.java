package at.ac.htlperg.squarebeard.objects;

import java.util.ArrayList;
import java.util.List;

import at.ac.htlperg.squarebeard.level.Level;
import at.ac.htlperg.squarebeard.space.Position;
import javafx.scene.image.Image;

public abstract class AnimatedObject extends GameObject {

	private List<Image> images = new ArrayList<>();
	private boolean running = false;
	private int currentImage = 0;
	
	public AnimatedObject(Level level, Position position, double width, double height) {
		super(level, position, width, height);
	}
	
	public abstract void start();

	protected abstract void nextImage();

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
	@Override
	public Image getImage() {
		return getImages().get(currentImage);
	}

	public int getCurrentImage() {
		return currentImage;
	}

	public void setCurrentImage(int currentImage) {
		this.currentImage = currentImage;
	}

}
