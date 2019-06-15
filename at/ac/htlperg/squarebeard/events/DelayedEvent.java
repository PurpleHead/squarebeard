package at.ac.htlperg.squarebeard.events;

import java.util.function.Consumer;

import at.ac.htlperg.squarebeard.objects.GameObject;

public class DelayedEvent {
	
	private long timeout;
	private Consumer<GameObject> callback;
	
	public DelayedEvent (long timeout, Consumer<GameObject> callback) {
		this.timeout = timeout;
		this.callback = callback;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public Consumer<GameObject> getCallback() {
		return callback;
	}

	public void setCallback(Consumer<GameObject> callback) {
		this.callback = callback;
	}

}
