package com.badlogic.gdx.tools.skinner.windows.projectelements;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.tools.skinner.Skinner;

public abstract class ProjectElementProperties<T> {
	protected final Skinner skinner;
	private final Table content;
	
	public ProjectElementProperties(Skinner skinner) {
		this.skinner = skinner;
		this.content = new Table();
	}
	
	public Table getContent() {
		return content;
	}
	
	public abstract void setProjectElement(T element);
}
