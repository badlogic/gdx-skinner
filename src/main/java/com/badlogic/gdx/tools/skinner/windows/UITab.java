package com.badlogic.gdx.tools.skinner.windows;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;

public class UITab extends Tab {
	final Table content = new Table();
	final String title;
	
	public UITab(String title) {
		super(false, false);
		this.title = title;				
	}
	
	@Override
	public String getTabTitle() {
		return title;
	}

	@Override
	public Table getContentTable() {
		return content;
	}
}
