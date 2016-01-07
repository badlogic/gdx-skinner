package com.badlogic.gdx.tools.skinner;

import com.badlogic.gdx.utils.Array;

public class EventBus {
	final Array<Event> events = new Array<Event>();
	final Array<EventBusListener> listeners = new Array<EventBusListener>();

	public void add(Event event) {
		this.events.add(event);
	}
	
	public void addListener(EventBusListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(EventBusListener listener) {
		listeners.removeValue(listener, true);
	}
	
	public void drain() {
		Array<Event> processedEvents = new Array<Event>(events);
		events.clear();
		for(Event event: processedEvents) {
			for(EventBusListener listener: listeners) {
				listener.event(event);
			}
		}
	}
	
	public interface EventBusListener {
		void event(Event event);
	}

	static public class Event {
		final String source;
		final EventType type;

		public Event(String source, EventType type) {
			super();
			this.source = source;
			this.type = type;
		}

		public String getSource() {
			return source;
		}

		public EventType getType() {
			return type;
		}
	}

	static public enum EventType {
		NewProject,
		ProjectModified, 
		ProjectSaved 
	}
}
