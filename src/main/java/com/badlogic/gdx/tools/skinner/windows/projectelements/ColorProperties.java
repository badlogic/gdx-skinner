package com.badlogic.gdx.tools.skinner.windows.projectelements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.tools.skinner.Skinner;
import com.badlogic.gdx.tools.skinner.EventBus.ProjectEvent;
import com.badlogic.gdx.tools.skinner.EventBus.ProjectEventType;
import com.badlogic.gdx.tools.skinner.model.StyleColor;
import com.badlogic.gdx.utils.Scaling;
import com.kotcrab.vis.ui.util.dialog.DialogUtils;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;

public class ColorProperties extends ProjectElementProperties<StyleColor> {
	public ColorProperties(Skinner skinner) {
		super(skinner);
	}

	@Override
	public void setProjectElement(StyleColor element) {
		getContent().clearChildren();
		getContent().top().left();
		VisValidatableTextField colorName = new VisValidatableTextField((input) -> {
			return input != null && input.trim().length() > 0;
		});
		colorName.setText(element.getName());
		ImageButton color = new ImageButton(skinner.getUI().getPixelDrawable());
		color.getImageCell().fill().expand();
		color.getImage().setScaling(Scaling.stretch);
		color.getImage().setColor(element.getColor());

		getContent().add(new Label("Name: ", skinner.getUI().getSkin()));
		getContent().add(colorName);
		getContent().row();
		getContent().add(new Label("Color: ", skinner.getUI().getSkin()));
		getContent().add(color).fill();

		colorName.setTextFieldListener((field, c) -> {
			if (c == '\n') {
				String newName = colorName.getText();
				if (colorName.isInputValid() && !element.getName().equals(newName)) {
					if (skinner.getProject().hasColor(newName)) {
						DialogUtils.showErrorDialog(skinner.getUI().getStage(),
								"Color names " + colorName.getText() + " already exists");
						colorName.setText(element.getName());
					} else {
						skinner.getUndoManager().beginStateChange("Color name change");
						element.setName(newName);
						skinner.getUndoManager().endStateChange();
						skinner.getEventBus().add(new ProjectEvent("Color name changed", ProjectEventType.ProjectModified));					
					}
				} else if(!colorName.isInputValid()) {
					colorName.setText(element.getName());
				}
				skinner.getUI().getStage().unfocus(colorName);
			}
		});

		color.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ColorPicker colorPicker = new ColorPicker(new ColorPickerAdapter() {
					@Override
					public void finished(Color newColor) {
						skinner.getUndoManager().beginStateChange("Color properties changed");
						skinner.getProject().getColors().get(element.getId()).setColor(newColor);
						skinner.getUndoManager().endStateChange();
						skinner.getEventBus().add(new ProjectEvent("Color changed", ProjectEventType.ProjectModified));
					}
				});
				colorPicker.setColor(color.getImage().getColor());
				skinner.getUI().getStage().addActor(colorPicker.fadeIn());
			}
		});
	}
}