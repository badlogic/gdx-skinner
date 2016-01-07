package com.badlogic.gdx.tools.skinner.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.tools.skinner.model.StyleDrawable.StyleDrawableType;
import com.badlogic.gdx.utils.ObjectMap;

public class Project {
	int nextId;
	final ObjectMap<String, StyleColor> colors = new ObjectMap<>();
	final ObjectMap<String, StyleFont> fonts = new ObjectMap<>();
	final ObjectMap<String, StyleDrawable> drawables = new ObjectMap<>();
	final ObjectMap<String, Style> styles = new ObjectMap<>();	
	
	public StyleColor newColor(String name, Color color) {
		if(hasColor(name)) {
			throw new IllegalArgumentException("Color with name '" + name + "' already exists");
		}
		StyleColor styleColor = new StyleColor();
		styleColor.setName(name);
		styleColor.setColor(new Color(color));
		styleColor.setId("" + nextId++);
		colors.put(styleColor.getId(), styleColor);
		return styleColor;
	}
	
	public boolean hasColor(String name) {
		for(StyleColor c: colors.values()) {
			if(c.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public ObjectMap<String, StyleColor> getColors() {
		return colors;
	}
	
	public StyleFont newFont(String name, String file) {
		if(hasFont(name)) {
			throw new IllegalArgumentException("Font with name '" + name + "' already exists");
		}
		StyleFont styleFont = new StyleFont();
		styleFont.setName(name);
		styleFont.setFile(file);
		styleFont.setId("" + nextId++);
		fonts.put(styleFont.getId(), styleFont);
		return styleFont;
	}
	
	public boolean hasFont(String name) {
		for(StyleFont f: fonts.values()) {
			if(f.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public ObjectMap<String, StyleFont> getFonts() {
		return fonts;
	}
	
	public StyleDrawable newDrawable(String name, String file, StyleDrawableType type) {
		if(hasDrawable(name)) {
			throw new IllegalArgumentException("Drawable with name '" + name + "' already exists");
		}
		StyleDrawable styleDrawable = new StyleDrawable();
		styleDrawable.setName(name);
		styleDrawable.setFile(file);
		styleDrawable.setType(type);
		styleDrawable.setId("" + nextId++);
		drawables.put(styleDrawable.getId(), styleDrawable);
		return styleDrawable;
	}
	
	public boolean hasDrawable(String name) {
		for(StyleDrawable d: drawables.values()) {
			if(d.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public ObjectMap<String, StyleDrawable> getDrawables() {
		return drawables;
	}
	
	public Style newStyle(String name, String className) {
		if(hasStyle(name, className)) {
			throw new IllegalArgumentException("Style with name '" + name + "' and class '" + className + "' already exists");
		}
		Style style = new Style();
		style.setName(name);
		style.setClassName(className);
		style.setId("" + nextId++);
		styles.put(style.getId(), style);
		return style;
	}
	
	public boolean hasStyle(String name, String className) {
		for(Style s: styles.values()) {
			if(s.getName().equals(name) && s.getClassName().equals(className)) {
				return true;
			}
		}
		return false;
	}
	
	public ObjectMap<String, Style> getStyles() {
		return styles;
	}
}
