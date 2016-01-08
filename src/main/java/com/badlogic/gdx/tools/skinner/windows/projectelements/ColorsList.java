package com.badlogic.gdx.tools.skinner.windows.projectelements;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.tools.skinner.Skinner;
import com.badlogic.gdx.tools.skinner.model.StyleColor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;

public class ColorsList extends ProjectElementList<StyleColor> {
	public ColorsList(Skinner skinner, ProjectElementListListener<StyleColor> listener) {
		super(skinner, listener);
	}

	@Override
	public void setProjectElements(Array<StyleColor> elements) {
		getContent().clearChildren();

		for (StyleColor color : elements) {
			Table colorItem = new Table();
			colorItem.defaults().top().left();
			Image image = new Image(skinner.getUI().getPixelDrawable());
			image.setScaling(Scaling.stretch);
			image.setColor(color.getColor());
			colorItem.add(image).width(32).fillY().expandY().spaceRight(5);
			colorItem.add(new Label(color.getName(), skinner.getUI().getSkin()));
			Node node = new Node(colorItem);
			node.setObject(color);
			getContent().add(node);
		}
	}
}