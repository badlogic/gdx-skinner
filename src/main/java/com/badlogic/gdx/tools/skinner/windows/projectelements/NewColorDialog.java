package com.badlogic.gdx.tools.skinner.windows.projectelements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.tools.skinner.EventBus.ProjectEventType;
import com.badlogic.gdx.tools.skinner.EventBus.ProjectEvent;
import com.badlogic.gdx.tools.skinner.Skinner;
import com.badlogic.gdx.tools.skinner.windows.UIDialog;
import com.badlogic.gdx.utils.Scaling;
import com.kotcrab.vis.ui.InputValidator;
import com.kotcrab.vis.ui.util.dialog.DialogUtils;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;

public class NewColorDialog extends UIDialog {

	public NewColorDialog(Skinner skinner) {
		super(skinner, "New Color", UIDialogType.OKCancel);

		getOk().setDisabled(true);
		VisValidatableTextField colorName = new VisValidatableTextField(new InputValidator() {
			@Override
			public boolean validateInput(String input) {
				boolean result = input != null && input.trim().length() > 0;
				getOk().setDisabled(!result);
				return result;
			}
		});
		ImageButton color = new ImageButton(skinner.getUI().getPixelDrawable());
		color.getImageCell().fill().expand();
		color.getImage().setScaling(Scaling.stretch);

		getContentTable().add(new Label("Name: ", skinner.getUI().getSkin()));
		getContentTable().add(colorName);
		getContentTable().row();
		getContentTable().add(new Label("Color: ", skinner.getUI().getSkin()));
		getContentTable().add(color).fill();

		color.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ColorPicker colorPicker = new ColorPicker(new ColorPickerAdapter() {
					@Override
					public void finished(Color newColor) {
						color.getImage().setColor(newColor);
					}
				});
				colorPicker.setColor(color.getImage().getColor());
				skinner.getUI().getStage().addActor(colorPicker.fadeIn());
			}
		});

		setListener(new UIDialogAdapter() {
			@Override
			public void yesOrOk() {
				if (skinner.getProject().hasColor(colorName.getText())) {
					DialogUtils.showErrorDialog(skinner.getUI().getStage(),
							"Color names " + colorName.getText() + " already exists");
				} else {
					hide();
					skinner.getUndoManager().beginStateChange("Added Color");
					skinner.getProject().newColor(colorName.getText(), color.getImage().getColor());
					skinner.getUndoManager().endStateChange();
					skinner.getEventBus().add(new ProjectEvent("SkinWindow added color", ProjectEventType.ProjectModified));
				}
			}

			@Override
			public void cancel() {
				hide();
			}
		});
	}
}
