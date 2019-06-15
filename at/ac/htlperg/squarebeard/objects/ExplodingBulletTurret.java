package at.ac.htlperg.squarebeard.objects;

import at.ac.htlperg.squarebeard.level.Level;
import at.ac.htlperg.squarebeard.space.Direction;
import at.ac.htlperg.squarebeard.space.Position;
import at.ac.htlperg.squarebeard.space.Vector;

public class ExplodingBulletTurret extends Turret {

	public ExplodingBulletTurret(Level level, Position position, Direction direction) {
		super(level, position, direction);
	}
	
	@Override
	protected void shoot() {
		Position pos = getPosition().clone();
		Bullet bullet = new ExplodingBullet(getLevel(), pos, Vector.of(getDirection(), 3));
		fire(bullet);
	}

}
