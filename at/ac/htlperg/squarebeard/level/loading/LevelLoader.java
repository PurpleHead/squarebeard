package at.ac.htlperg.squarebeard.level.loading;

import java.io.File;
import java.io.IOException;

import at.ac.htlperg.squarebeard.level.LevelData;

public interface LevelLoader {
	
	public LevelData load(File file) throws IOException;

}
