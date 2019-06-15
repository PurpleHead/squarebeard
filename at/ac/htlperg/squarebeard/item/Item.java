package at.ac.htlperg.squarebeard.item;

import at.ac.htlperg.squarebeard.objects.Player;
import javafx.scene.image.Image;

public abstract class Item {
	
	private String name;
	private Image image;
	
	public Item(Image image, String name) {
		this.image = image;
		this.name = name;
	}
	
	public Item(Image image) {
		this.image = image;
		this.name = "item";
	}
	
	public abstract void use(Player user);

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Image getImage() {
		return image;
	}
	
}
