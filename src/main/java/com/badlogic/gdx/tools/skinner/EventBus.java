package com.badlogic.gdx.tools.skinner;

import com.badlogic.gdx.utils.Array;

public class EventBus<T> {
	final Array<T> events = new Array<>();
	final Array<EventBusListener<T>> listeners = new Array<>();

	public void add(T event) {
		this.events.add(event);
	}
	
	public void addListener(EventBusListener<T> listener) {
		listeners.add(listener);
	}
	
	public void removeListener(EventBusListener<T> listener) {
		listeners.removeValue(listener, true);
	}
	
	public void drain() {
		Array<T> processedEvents = new Array<>(events);
		events.clear();
		for(T event: processedEvents) {
			for(EventBusListener<T> listener: listeners) {
				listener.event(event);
			}
		}
	}
	
	public interface EventBusListener<T> {
		void event(T event);
	}

	static public class ProjectEvent {
		final String source;
		final ProjectEventType type;

		public ProjectEvent(String source, ProjectEventType type) {
			super();
			this.source = source;
			this.type = type;
		}

		public String getSource() {
			return source;
		}

		public ProjectEventType getType() {
			return type;
		}
	}

	static public enum ProjectEventType {
		NewProject,
		ProjectModified, 
		ProjectSaved 
	}
}
