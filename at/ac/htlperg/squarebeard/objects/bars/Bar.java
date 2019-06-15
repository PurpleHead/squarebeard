package at.ac.htlperg.squarebeard.objects.bars;

import at.ac.htlperg.squarebeard.Renderable;
import javafx.scene.paint.Color;

public abstract class Bar implements Renderable {
	
	private Color fill;
	private double width;
	private double height;
	
	public Bar(Color fill, double width, double height) {
		this.fill = fill;
		this.setWidth(width);
		this.setHeight(height);
	}

	public Color getFill() {
		return fill;
	}

	public void setFill(Color fill) {
		this.fill = fill;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}
	
}
