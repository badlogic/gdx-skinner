package com.badlogic.gdx.tools.skinner;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.tools.skinner.EventBus.Event;
import com.badlogic.gdx.tools.skinner.EventBus.EventBusListener;
import com.badlogic.gdx.tools.skinner.EventBus.EventType;
import com.badlogic.gdx.tools.skinner.model.Project;
import com.badlogic.gdx.tools.skinner.model.UndoManager;
import com.badlogic.gdx.tools.skinner.tools.FileTool;
import com.badlogic.gdx.tools.skinner.tools.UndoTool;
import com.badlogic.gdx.tools.skinner.windows.SkinWindow;
import com.badlogic.gdx.utils.Json;

public class Skinner extends ApplicationAdapter implements EventBusListener {
	Json json = new Json();
	EventBus eventBus;
	Preferences prefs;
	UI ui;
	UndoManager undoManager;
	InputManager inputManager;
	Project project;
	String projectPath;
	long lastModified;
	long lastSaved;
	
	@Override
	public void create() {
		prefs = Gdx.app.getPreferences("skinner");
		eventBus = new EventBus();
		eventBus.addListener(this);
		ui = new UI(this);
		undoManager = new UndoManager(this);
		inputManager = new InputManager(this);

		// add default windows
		ui.addWindow(new SkinWindow(this));
		
		// add default tools
		new FileTool().setup(this);
		new UndoTool().setup(this);
	}

	@Override
	public void resize(int width, int height) {
		ui.resize(width, height);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		eventBus.drain();
		ui.render();
	}

	@Override
	public void dispose() {
		ui.dispose();
	}
	
	private void updateWindowTitle() {
		if(projectPath != null) {
			Gdx.graphics.setTitle("Skinner - " + projectPath + " " + (needsSaving()? "*": ""));
		} else {
			Gdx.graphics.setTitle("Skinner");
		}
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project newProject) {
		if (newProject == null) {
			throw new IllegalArgumentException("Project may not be null");
		}
		this.project = newProject;		
	}
	
	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public UI getUI() {
		return ui;
	}

	public InputManager getInputManager() {
		return inputManager;
	}

	public UndoManager getUndoManager() {
		return undoManager;
	}

	public Preferences getPreferences() {
		return prefs;
	}

	public void writeJSONPref(String key, Object value) {
		prefs.putString(key, json.toJson(value));
		prefs.flush();
	}

	public <T> T readJSONPref(String key, Class<T> clazz) {
		String jsonValue = prefs.getString(key, null);
		if (jsonValue == null) {
			return null;
		} else {
			return (T) json.fromJson(clazz, jsonValue);
		}
	}

	public static void main(String[] args) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Skinner");
		config.setWindowedMode(800, 600);
		new Lwjgl3Application(new Skinner(), config);
	}

	@Override
	public void event(Event event) {
		if(event.type == EventType.NewProject) {
			lastSaved = 0;
			lastModified = 1;
			updateWindowTitle();
		}
		if(event.type == EventType.ProjectSaved) {
			lastSaved = System.nanoTime();
			updateWindowTitle();
		}
		if(event.type == EventType.ProjectModified) {
			lastModified = System.nanoTime();
			updateWindowTitle();
		}
	}

	public boolean needsSaving() {
		return lastModified > lastSaved;
	}
}
