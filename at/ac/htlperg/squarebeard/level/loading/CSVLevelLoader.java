package at.ac.htlperg.squarebeard.level.loading;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import at.ac.htlperg.squarebeard.MainGame;
import at.ac.htlperg.squarebeard.level.LevelData;
import at.ac.htlperg.squarebeard.objects.ExplodingBulletTurret;
import at.ac.htlperg.squarebeard.objects.Mine;
import at.ac.htlperg.squarebeard.objects.RotatingTurret;
import at.ac.htlperg.squarebeard.objects.SpreadingBulletTurret;
import at.ac.htlperg.squarebeard.objects.Turret;
import at.ac.htlperg.squarebeard.objects.tiles.ChestTile;
import at.ac.htlperg.squarebeard.objects.tiles.GroundTile;
import at.ac.htlperg.squarebeard.objects.tiles.LampTile;
import at.ac.htlperg.squarebeard.objects.tiles.SpikeTile;
import at.ac.htlperg.squarebeard.objects.tiles.Tile;
import at.ac.htlperg.squarebeard.objects.tiles.WallTile;
import at.ac.htlperg.squarebeard.space.Direction;
import at.ac.htlperg.squarebeard.space.Position;
import util.Pair;

public class CSVLevelLoader implements LevelLoader {
	
	private static final Logger log = Logger.getLogger(CSVLevelLoader.class.getName());

	@Override
	public LevelData load(File file) throws FileNotFoundException, IOException {
		LevelData data = new LevelData(30, 30);
		CSVParser parser = new CSVParser(new FileReader(file), CSVFormat.newFormat(','));
		List<CSVRecord> records = parser.getRecords();
		Tile[][] loadedTiles = new Tile[30][30];
		for (int i = 0; i < records.size(); i++) {
			CSVRecord record = records.get(i);
			int j = 0;
			for (String s : record) {
				int tileID = Integer.parseInt(s.split(":")[0]);
				Direction dir = Direction.NORTH;
				switch (tileID) {
				case CSVConstants.Tiles.GROUND:
					loadedTiles[j][i] = new GroundTile(MainGame.getLevel(), new Position(j * Tile.TILE_SIZE,i * Tile.TILE_SIZE));
					break;
				case CSVConstants.Tiles.WALL:
					loadedTiles[j][i] = new WallTile(MainGame.getLevel(), new Position(j * Tile.TILE_SIZE, i * Tile.TILE_SIZE));
					break;
				case CSVConstants.Tiles.CHEST:
					loadedTiles[j][i] = new ChestTile(MainGame.getLevel(), new Position(j * Tile.TILE_SIZE,i * Tile.TILE_SIZE));
					break;
				case CSVConstants.Tiles.LAMP:
					loadedTiles[j][i] = new LampTile(MainGame.getLevel(), new Position(j * Tile.TILE_SIZE,i * Tile.TILE_SIZE));
					break;
				case CSVConstants.Spawns.PLAYER:
					data.setPlayerPos(new Pair<Integer, Integer>(j * Tile.TILE_SIZE, i * Tile.TILE_SIZE));
					loadedTiles[j][i] = new GroundTile(MainGame.getLevel(), new Position(j * Tile.TILE_SIZE, i * Tile.TILE_SIZE));
					break;
				case CSVConstants.Tiles.SPIKE:
					loadedTiles[j][i] = new SpikeTile(MainGame.getLevel(), new Position(j * Tile.TILE_SIZE, i * Tile.TILE_SIZE));
					break;
				case CSVConstants.Spawns.TURRET:
					loadedTiles[j][i] = new GroundTile(MainGame.getLevel(), new Position(j * Tile.TILE_SIZE, i * Tile.TILE_SIZE));
					if (s.split(":").length > 1) {
						int dirId = Integer.parseInt(s.split(":")[1]);
						dir = dirOfId(dirId);
					}
					data.addObject(new Turret(MainGame.getLevel(), new Position(j * Tile.TILE_SIZE, i * Tile.TILE_SIZE), dir));
					break;
				case CSVConstants.Spawns.TURRET_ROTATING:
					loadedTiles[j][i] = new GroundTile(MainGame.getLevel(), new Position(j * Tile.TILE_SIZE, i * Tile.TILE_SIZE));
					if (s.split(":").length > 1) {
						int dirId = Integer.parseInt(s.split(":")[1]);
						dir = dirOfId(dirId);
					}
					data.addObject(new RotatingTurret(MainGame.getLevel(), new Position(j * Tile.TILE_SIZE, i * Tile.TILE_SIZE), dir));
					break;
				case CSVConstants.Spawns.TURRET_SPREADING:
					loadedTiles[j][i] = new GroundTile(MainGame.getLevel(), new Position(j * Tile.TILE_SIZE, i * Tile.TILE_SIZE));
					if (s.split(":").length > 1) {
						int dirId = Integer.parseInt(s.split(":")[1]);
						dir = dirOfId(dirId);
					}
					data.addObject(new SpreadingBulletTurret(MainGame.getLevel(), new Position(j * Tile.TILE_SIZE, i * Tile.TILE_SIZE), dir));
					break;
				case CSVConstants.Spawns.MINE:
					loadedTiles[j][i] = new GroundTile(MainGame.getLevel(), new Position(j * Tile.TILE_SIZE, i * Tile.TILE_SIZE));
					data.addObject(new Mine(MainGame.getLevel(), new Position(j * Tile.TILE_SIZE, i * Tile.TILE_SIZE), Tile.TILE_SIZE, Tile.TILE_SIZE));
					break;
				case CSVConstants.Spawns.TURRET_EXPLODING:
					loadedTiles[j][i] = new GroundTile(MainGame.getLevel(), new Position(j * Tile.TILE_SIZE, i * Tile.TILE_SIZE));
					if (s.split(":").length > 1) {
						int dirId = Integer.parseInt(s.split(":")[1]);
						dir = dirOfId(dirId);
					}
					data.addObject(new ExplodingBulletTurret(MainGame.getLevel(), new Position(j * Tile.TILE_SIZE, i * Tile.TILE_SIZE), dir));
					break;
				default:
					loadedTiles[j][i] = new GroundTile(MainGame.getLevel(), new Position(j * Tile.TILE_SIZE,i * Tile.TILE_SIZE));
					log.warning(String.format("Unknown tile id: %s", s));
					break;
				}
				j++;
			}
		}
		data.setTiles(loadedTiles);
		parser.close();
		return data;
	}
	
	private Direction dirOfId(int dirId) {
		Direction dir = Direction.NORTH;
		switch (dirId) {
		case CSVConstants.Directions.DOWN:
			dir = Direction.SOUTH;
			break;
		case CSVConstants.Directions.LEFT:
			dir = Direction.WEST;
			break;
		case CSVConstants.Directions.UP:
			dir = Direction.NORTH;
			break;
		case CSVConstants.Directions.RIGHT:
			dir = Direction.EAST;
			break;
		default:
			break;
		}
		return dir;
	}

}
