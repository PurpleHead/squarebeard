package at.ac.htlperg.squarebeard.level;

import java.util.LinkedList;
import java.util.List;

import at.ac.htlperg.squarebeard.objects.GameObject;
import at.ac.htlperg.squarebeard.objects.tiles.Tile;
import util.Pair;

public class LevelData {

	private Tile[][] tiles;
	private List<GameObject> objects;
	private Pair<Integer, Integer> playerPos;

	private int width;
	private int height;
	
	public LevelData(int width, int height) {
		super();
		this.width = width;
		this.height = height;
		this.tiles = new Tile[width][height];
		this.objects = new LinkedList<>();
	}
	
	public Tile[][] getTiles() {
		return tiles;
	}

	public List<GameObject> getObjects() {
		return objects;
	}

	public void setObjects(List<GameObject> objects) {
		this.objects = objects;
	}

	public void setTiles(Tile[][] tiles) {
		this.tiles = tiles;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Pair<Integer, Integer> getPlayerPos() {
		return playerPos;
	}

	public void setPlayerPos(Pair<Integer, Integer> playerPos) {
		this.playerPos = playerPos;
	}

	public void addObject(GameObject obj) {
		objects.add(obj);
	}
	
}
