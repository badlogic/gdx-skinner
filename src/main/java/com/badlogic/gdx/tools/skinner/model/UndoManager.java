package com.badlogic.gdx.tools.skinner.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.tools.skinner.EventBus.Event;
import com.badlogic.gdx.tools.skinner.EventBus.EventBusListener;
import com.badlogic.gdx.tools.skinner.EventBus.EventType;
import com.badlogic.gdx.tools.skinner.Skinner;
import com.badlogic.gdx.utils.Array;

public class UndoManager implements EventBusListener {
	public static class UndoState {
		byte[] state;
		final String action;
		
		public UndoState(String action) {
			this.action = action;		
		}

		public byte[] getState() {
			return state;
		}
		
		public void setState(byte[] state) {
			this.state = state;
		}

		public String getAction() {
			return action;
		}
	}
	
	final Skinner skinner;
	final Array<UndoState> states = new Array<>();
	int stateIndex = -1;
	UndoState currentChange = null;
	
	public UndoManager(Skinner skinner) {
		this.skinner = skinner;
		skinner.getEventBus().addListener(this);
	}
	
	public void clear() {		
		states.clear();
		currentChange = null;
		stateIndex = -1;
		Gdx.app.log("UndoManager", "Cleared undo states");
	}
	
	public void beginStateChange(String action) {
		currentChange = new UndoState(action);		
	}
	
	public void endStateChange() {		
		long start = System.nanoTime();
		currentChange.state = ProjectSerializer.serialize(skinner.getProject());
		Array<UndoState> newStates = new Array<UndoState>();
		for(int i = 0; i <= stateIndex; i++) {
			newStates.add(states.get(i));
		}		
		states.clear();
		states.addAll(newStates);
		states.add(currentChange);
		stateIndex = states.size - 1;
		Gdx.app.log("UndoManager", "Saved state for action " + currentChange.action + " in " + (System.nanoTime() - start) / 1000000000.0);
		currentChange = null;
		printStateSize();
	}
	
	private void printStateSize() {
		long size = 0;
		for(UndoState state: states) {
			size += state.state.length;
		}
		Gdx.app.log("UndoManager", "Total undo state size: " + size / (1024*1024f) + "mb");
	}
	
	public void undo() {
		if(states.size == 1 || stateIndex == 0) {
			return;
		}
		
		stateIndex--;
		Project project = ProjectSerializer.deserialize(states.get(stateIndex).state);
		replaceProject(project);
		Gdx.app.log("UndoManager", "Undid state " + stateIndex + 1);
		printStateSize();
	}
	
	public void redo() {
		if(stateIndex == states.size - 1) {
			return;
		}
		
		stateIndex++;
		Project project = ProjectSerializer.deserialize(states.get(stateIndex).state);
		replaceProject(project);
		Gdx.app.log("UndoManager", "Redid state " + stateIndex + 1);
		printStateSize();
	}
	
	private void replaceProject(Project newProject) {		
		skinner.setProject(newProject);
		skinner.getEventBus().add(new Event("Undo Manager event", EventType.ProjectModified));
	}

	@Override
	public void event(Event event) {
		if(event.getType() == EventType.NewProject) {
			clear();
		}
	}
}