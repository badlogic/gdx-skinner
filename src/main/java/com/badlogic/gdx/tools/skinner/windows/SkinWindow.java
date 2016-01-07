package com.badlogic.gdx.tools.skinner.windows;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.tools.skinner.Skinner;
import com.badlogic.gdx.utils.Scaling;
import com.kotcrab.vis.ui.InputValidator;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter;

public class SkinWindow extends UIWindow {
	TextureRegion pixel;
	Table tabContent;
	
	public SkinWindow(Skinner skinner) {
		super(skinner, "Skin");
		setResizable(true);		
		top().left();
		
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		pixel = new TextureRegion(new Texture(pixmap));
		pixmap.dispose();
				
		TabbedPane tabs = new TabbedPane();
		
		tabs.add(new UITab("Colors") {{
			Table content = getContentTable();			
			content.left().top();
			TextButton newColor = new TextButton("New", skinner.getUI().getSkin());
			
			content.add(newColor);
			
			newColor.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					TextButton ok = new TextButton("Ok", skinner.getUI().getSkin());
					TextButton cancel = new TextButton("Cancel", skinner.getUI().getSkin());
					
					Dialog newColorDialog = new Dialog("New Color", skinner.getUI().getSkin());
					VisValidatableTextField colorName = new VisValidatableTextField(new InputValidator() {
						@Override
						public boolean validateInput(String input) {							
							boolean result = input != null && input.trim().length() > 0;
							ok.setDisabled(!result);
							return result;
						}
					});
					TextureRegionDrawable colorDrawable = new TextureRegionDrawable(pixel);					
					ImageButton color = new ImageButton(colorDrawable);
					color.getImageCell().fill().expand();
					color.getImage().setScaling(Scaling.stretch);					
					
					newColorDialog.getContentTable().add(new Label("Name: ", skinner.getUI().getSkin()));
					newColorDialog.getContentTable().add(colorName);
					newColorDialog.getContentTable().row();
					newColorDialog.getContentTable().add(new Label("Color: ", skinner.getUI().getSkin()));
					newColorDialog.getContentTable().add(color).fill();
					newColorDialog.getButtonTable().add(cancel);
					newColorDialog.getButtonTable().add(ok);					
					
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
					
					cancel.addListener(new ClickListener() {
						@Override
						public void clicked(InputEvent event, float x, float y) {
							newColorDialog.hide();
						}
					});
					
					ok.addListener(new ClickListener() {
						@Override
						public void clicked(InputEvent event, float x, float y) {
							newColorDialog.hide();
							// FIXME add color
						}
					});
					
					newColorDialog.show(skinner.getUI().getStage());
				}
			});
		}});
		
		tabs.add(new UITab("Fonts"));
		
		tabs.add(new UITab("Drawables"));
		
		tabs.add(new UITab("Styles"));
		
		tabs.addListener(new TabbedPaneAdapter() {
			@Override
			public void switchedTab(Tab tab) {
				tabContent.clearChildren();
				tabContent.add(tab.getContentTable()).fill().expand().top().left();
			}
		});		
		
		add(tabs.getTable()).fillX().row();
		tabContent = new Table();
		add(tabContent).expand().fill();		
		tabs.switchTab(0);
		invalidate();
		layout();		
		pack();
	}

	@Override
	public void dispose() {
		pixel.getTexture().dispose();
	}
}
