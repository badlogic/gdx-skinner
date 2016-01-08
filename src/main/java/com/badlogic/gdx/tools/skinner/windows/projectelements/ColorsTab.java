package com.badlogic.gdx.tools.skinner.windows.projectelements;

import com.badlogic.gdx.tools.skinner.Skinner;
import com.badlogic.gdx.tools.skinner.model.StyleColor;
import com.badlogic.gdx.utils.Array;

public class ColorsTab extends ProjectElementTab<StyleColor> {
	protected ColorsList list;
	protected ColorProperties properties;

	public ColorsTab(Skinner skinner) {
		super(skinner, "Colors");		
	}

	@Override
	public ProjectElementList<StyleColor> getList() {
		if(list == null) {
			list = new  ColorsList(skinner, this);
		}
		return list;
	}

	@Override
	public ProjectElementProperties<StyleColor> getProperties() {
		if(properties == null) {
			properties = new ColorProperties(skinner);
		}
		return properties;
	}

	@Override
	public Array<StyleColor> getAllElements() {
		return skinner.getProject().getColors().values().toArray();
	}

	@Override
	public void newElement() {
		new NewColorDialog(skinner).show();
	}

	@Override
	public void removeElement(StyleColor element) {
	}
}
