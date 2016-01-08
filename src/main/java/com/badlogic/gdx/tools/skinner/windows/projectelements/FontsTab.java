package com.badlogic.gdx.tools.skinner.windows.projectelements;

import com.badlogic.gdx.tools.skinner.Skinner;
import com.badlogic.gdx.tools.skinner.model.StyleFont;
import com.badlogic.gdx.utils.Array;

public class FontsTab extends ProjectElementTab<StyleFont> {
	public FontsTab(Skinner skinner, String title) {
		super(skinner, title);
	}

	@Override
	public Array<StyleFont> getAllElements() {
		return new Array<>();
	}

	@Override
	public ProjectElementList<StyleFont> getList() {
		return null;
	}

	@Override
	public ProjectElementProperties<StyleFont> getProperties() {
		return null;
	}

	@Override
	public void newElement() {
	}

	@Override
	public void removeElement(StyleFont element) {
	}
}
