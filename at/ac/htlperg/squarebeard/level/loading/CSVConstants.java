package at.ac.htlperg.squarebeard.level.loading;

public class CSVConstants {
	
	private CSVConstants () {}
	
	public static final class Tiles {
		
		private Tiles() {}
		
		public static final int GROUND = 1;
		public static final int WALL = 2;
		public static final int CHEST = 3;
		public static final int SPIKE = 4;
		public static final int LAMP = 11;
	}
	
	public static final class Directions {
		
		private Directions () {}
		
		public static final int UP = 1;
		public static final int RIGHT = 2;
		public static final int DOWN = 3;
		public static final int LEFT = 4;
		
	}
	
	public static final class Spawns {
		
		private Spawns() {}
		
		public static final int PLAYER = 5;
		public static final int TURRET = 6;
		public static final int TURRET_ROTATING = 7;
		public static final int TURRET_SPREADING = 8;
		public static final int MINE = 9;
		public static final int TURRET_EXPLODING = 10;
		
	}
	
}
