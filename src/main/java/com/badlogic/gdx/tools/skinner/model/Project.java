package com.badlogic.gdx.tools.skinner.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin.TintedDrawable;
import com.badlogic.gdx.utils.ObjectMap;

public class Project {
	final ObjectMap<String, Color> colors = new ObjectMap<String, Color>();
	final ObjectMap<String, StyleFont> fonts = new ObjectMap<String, StyleFont>();
	final ObjectMap<String, TintedDrawable> tintedDrawable = new ObjectMap<String, TintedDrawable>();
	final ObjectMap<String, StyleDrawable> drawables = new ObjectMap<String, StyleDrawable>();
	final ObjectMap<String, ObjectMap<String, Style>> styles = new ObjectMap<String, ObjectMap<String, Style>>();	
	
	public ObjectMap<String, Color> getColors() {
		return colors;
	}
	public ObjectMap<String, StyleFont> getFonts() {
		return fonts;
	}
	public ObjectMap<String, TintedDrawable> getTintedDrawable() {
		return tintedDrawable;
	}
	public ObjectMap<String, StyleDrawable> getDrawables() {
		return drawables;
	}
	public ObjectMap<String, ObjectMap<String, Style>> getStyles() {
		return styles;
	}
	public void setSaved(boolean saved) {
		
		
	}
}
