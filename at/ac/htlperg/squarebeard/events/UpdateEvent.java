package at.ac.htlperg.squarebeard.events;

import java.util.Collections;
import java.util.Set;

import javafx.scene.input.KeyCode;

public class UpdateEvent {

	private Set<KeyCode> codes;
	private long delta;
	
	public UpdateEvent() {
		this(Collections.emptySet(), 0);
	}

	public UpdateEvent(Set<KeyCode> codes, long delta) {
		super();
		this.codes = codes;
		this.delta = delta;
	}

	public Set<KeyCode> getCodes() {
		return codes;
	}

	public void setCodes(Set<KeyCode> codes) {
		this.codes = codes;
	}
	
	public boolean isPressed(KeyCode code) {
		return codes.contains(code);
	}
	
	public long getDelta() {
		return delta;
	}
	
}
