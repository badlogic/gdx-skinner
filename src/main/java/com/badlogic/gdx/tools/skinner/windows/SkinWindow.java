package com.badlogic.gdx.tools.skinner.windows;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.tools.skinner.EventBus.EventBusListener;
import com.badlogic.gdx.tools.skinner.EventBus.ProjectEvent;
import com.badlogic.gdx.tools.skinner.EventBus.ProjectEventType;
import com.badlogic.gdx.tools.skinner.Skinner;
import com.badlogic.gdx.tools.skinner.windows.projectelements.ColorsTab;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter;

public class SkinWindow extends UIWindow implements EventBusListener<ProjectEvent> {
	Table tabContent;
	ColorsTab colorsTab;
	TabbedPane tabs;

	public SkinWindow(Skinner skinner) {
		super(skinner, "Skin");
		setResizable(true);
		top().left();
		skinner.getEventBus().addListener(this);		
		
		colorsTab = new ColorsTab(skinner);	
		
		tabs = new TabbedPane();
		tabs.add(colorsTab);
		add(tabs.getTable()).fillX().row();
		tabContent = new Table();
		tabContent.setName("Skin window tab content");
		add(tabContent).expand().fill();	
		
		tabs.addListener(new TabbedPaneAdapter() {
			@Override
			public void switchedTab(Tab tab) {
				setTab(tab);
			}
		});
		tabs.switchTab(0);
		setTab(tabs.getActiveTab());
		pack();
		setVisible(false);
	}
	
	private void setTab(Tab tab) {
		tabContent.clearChildren();
		tabContent.add(tab.getContentTable()).fill().expand().top().left();
		tabContent.invalidate();
		tabContent.layout();
	}

	@Override
	public void dispose() {
		skinner.getEventBus().removeListener(this);		
	}
	
	@Override
	public void event(ProjectEvent event) {
		if (event.getType() == ProjectEventType.NewProject) {
			setVisible(true);
			updateTabs();
		}
		if(event.getType() == ProjectEventType.ProjectModified) {
			updateTabs();
		}
	}
	
	private void updateTabs() {
		colorsTab.update();		
	}	
}
