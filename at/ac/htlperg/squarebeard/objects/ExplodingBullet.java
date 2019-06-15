package at.ac.htlperg.squarebeard.objects;

import at.ac.htlperg.squarebeard.level.Level;
import at.ac.htlperg.squarebeard.space.Position;
import at.ac.htlperg.squarebeard.space.Vector;

public class ExplodingBullet extends Bullet {

	public ExplodingBullet(Level level, Position position, Vector movement) {
		super(level, position, movement);
	}
	
	@Override
	protected void onWallHit() {
		super.onWallHit();
		getLevel().addObject(new Explosion(getLevel(), getPosition(), 10, 10, 1, true));
	}
	
	@Override
	protected void onPlayerHit() {
	}

}
