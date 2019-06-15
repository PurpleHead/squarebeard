package at.ac.htlperg.squarebeard.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import at.ac.htlperg.squarebeard.events.UpdateEvent;
import at.ac.htlperg.squarebeard.level.Level;
import at.ac.htlperg.squarebeard.space.Direction;
import at.ac.htlperg.squarebeard.space.Position;

public class RotatingTurret extends Turret {
	
	private List<Direction> dirRow;
	private int currentDir = 0;

	public RotatingTurret(Level level, Position position, Direction direction) {
		super(level, position, direction);
		setTimeout(0);
		this.dirRow = new ArrayList<Direction>();
		dirRow.addAll(EnumSet.allOf(Direction.class));
	}
	
	@Override
	public void onUpdate(UpdateEvent event) {
		setTimeout(getTimeout() - 1);
		if (getTimeout() <= 0) {
			shoot();
			setTimeout(getTimeoutConstant());
		}
		
		if(event.getDelta() % getTimeoutConstant() == 0) {
			nextDir();
			setTexture(getDirection());
			setDirection(dirRow.get(currentDir));
		}
	}
	
	private void nextDir() {
		if(dirRow != null) {
			if(currentDir >= dirRow.size() - 1) {
				currentDir = 0;
			} else 
				currentDir++;
		} else {
			if(currentDir >= 3) {
				currentDir = 0;
			} else 
				currentDir++;
		}
	}

	public void setAllowedDirections(Direction... dir) {
		this.dirRow = Arrays.asList(dir);
		this.currentDir = 0;
	}
	
	@Override
	public void reset() {
	}
	
}
