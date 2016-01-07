package com.badlogic.gdx.tools.skinner.windows;

import java.util.Comparator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.tools.skinner.EventBus.Event;
import com.badlogic.gdx.tools.skinner.EventBus.EventBusListener;
import com.badlogic.gdx.tools.skinner.EventBus.EventType;
import com.badlogic.gdx.tools.skinner.Skinner;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.Scaling;
import com.kotcrab.vis.ui.InputValidator;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter;

public class SkinWindow extends UIWindow implements EventBusListener {
	TextureRegion pixel;
	Table tabContent;
	ColorsTab colorsTab;
	FontsTab fontsTab;
	DrawablesTab drawablesTab;
	StylesTab stylesTab;

	public SkinWindow(Skinner skinner) {
		super(skinner, "Skin");
		setResizable(true);
		top().left();
		skinner.getEventBus().addListener(this);

		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		pixel = new TextureRegion(new Texture(pixmap));
		pixmap.dispose();
		
		setupTabs();
		setVisible(false);
	}

	@Override
	public void dispose() {
		skinner.getEventBus().removeListener(this);
		pixel.getTexture().dispose();
	}
	
	void setupTabs() {			
		TabbedPane tabs = new TabbedPane();
		colorsTab = new ColorsTab();
		fontsTab = new FontsTab();
		drawablesTab = new DrawablesTab();
		stylesTab = new StylesTab();
		
		tabs.add(colorsTab);
		tabs.add(fontsTab);
		tabs.add(drawablesTab);
		tabs.add(stylesTab);
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
	
	private void updateTabs() {
		colorsTab.update();
		fontsTab.update();
		drawablesTab.update();
		stylesTab.update();
	}

	@Override
	public void event(Event event) {
		if (event.getType() == EventType.NewProject) {
			setVisible(true);
			updateTabs();
		}
		if(event.getType() == EventType.ProjectModified) {
			updateTabs();
		}
	}
	
	abstract class BaseTab extends UITab {
		protected ScrollPane scrollPaneTop;
		protected ScrollPane scrollPaneBottom;
		
		public BaseTab(String title) {
			super(title);
			
			Table content = getContentTable();
			content.defaults().left().top();
			
			TextButton newElement = new TextButton("New", skinner.getUI().getSkin());
			TextField filter = new TextField("", skinner.getUI().getSkin());			
			scrollPaneTop = new ScrollPane(null, skinner.getUI().getSkin());
			scrollPaneBottom = new ScrollPane(null, skinner.getUI().getSkin());
			SplitPane splitPane = new SplitPane(scrollPaneTop, scrollPaneBottom, true, skinner.getUI().getSkin());
			
			content.add(newElement).left();
			content.add(filter).fillX().expandX();
			content.row();
			content.add(splitPane).colspan(2).expand().fill();

			newElement.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					newElement();
				}
			});
			
			filter.setTextFieldListener(new TextFieldListener() {
				@Override
				public void keyTyped(TextField textField, char c) {
					filter(textField.getText());
				}
			});
		}
		
		abstract void newElement();
		abstract void filter(String filter);
		abstract void update();
	}

	class ColorsTab extends BaseTab {
		String filter;
		Color lastColor;
		
		public ColorsTab() {
			super("Colors");			
		}
		
		@Override
		void newElement() {
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
					skinner.getUndoManager().beginStateChange("Added Color");
					skinner.getProject().getColors().put(colorName.getText(), color.getImage().getColor());
					skinner.getUndoManager().endStateChange();
					skinner.getEventBus().add(new Event("SkinWindow added color", EventType.ProjectModified));
				}
			});
			newColorDialog.show(skinner.getUI().getStage());
		}

		@Override
		void filter(String filter) {
			this.filter = filter.trim();
			update();
		}

		@Override
		void update() {
			scrollPaneTop.setWidget(null);
			scrollPaneBottom.setWidget(null);
			Tree list = new Tree(skinner.getUI().getSkin());			
			Array<Entry<String, Color>> colors = new Array<>();
			for(Entry<String, Color> entry: skinner.getProject().getColors().entries()) {
				Entry<String, Color> copy = new Entry<String, Color>();
				if(filter == null || 
				   (filter.length() == 0 || entry.key.toLowerCase().contains(filter.toLowerCase()))) {
					copy.key = entry.key;
					copy.value = entry.value;
					colors.add(copy);
				}				
			}
			colors.sort(new Comparator<Entry<String, Color>>() {
				public int compare(Entry<String, Color> o1, Entry<String, Color> o2) {
					return o1.key.compareTo(o2.key);
				}
			});		
			list.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					updateProperties(list);
				}
			});
			for(Entry<String, Color> color: colors) {
				Table colorItem = new Table();
				colorItem.defaults().top().left();				
				TextureRegionDrawable colorDrawable = new TextureRegionDrawable(pixel);					
				Image image = new Image(colorDrawable);
				image.setScaling(Scaling.stretch);		
				image.setColor(color.value);
				colorItem.add(image).width(32).fillY().expandY().spaceRight(5);
				colorItem.add(new Label(color.key, skinner.getUI().getSkin()));
				ListNode<Entry<String, Color>> node = new ListNode<Entry<String, Color>>(colorItem, color);
				list.add(node);
				if(lastColor == color.value) {
					list.getSelection().set(node);
				}
			}			
			scrollPaneTop.setWidget(list);						
		}
		
		void updateProperties(Tree list) {
			if(list.getSelection().first() == null) {
				scrollPaneBottom.setWidget(null);
				return;
			}			
			ListNode<Entry<String, Color>> node = (ListNode<Entry<String, Color>>) list.getSelection().first();
			lastColor = node.getObject().value;
			Table properties = new Table();
			properties.top().left();
			VisValidatableTextField colorName = new VisValidatableTextField(new InputValidator() {
				@Override
				public boolean validateInput(String input) {							
					return input != null && input.trim().length() > 0;
				}
			});
			colorName.setText(node.getObject().key);
			TextureRegionDrawable colorDrawable = new TextureRegionDrawable(pixel);					
			ImageButton color = new ImageButton(colorDrawable);
			color.getImageCell().fill().expand();
			color.getImage().setScaling(Scaling.stretch);
			color.getImage().setColor(node.getObject().value);
			
			properties.add(new Label("Name: ", skinner.getUI().getSkin()));
			properties.add(colorName);
			properties.row();
			properties.add(new Label("Color: ", skinner.getUI().getSkin()));
			properties.add(color).fill();
			
			color.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					ColorPicker colorPicker = new ColorPicker(new ColorPickerAdapter() {
						@Override
						public void finished(Color newColor) {
							skinner.getUndoManager().beginStateChange("SkinWindow color change");
							skinner.getProject().getColors().get(node.getObject().key).set(newColor);
							skinner.getUndoManager().endStateChange();
							skinner.getEventBus().add(new Event("Color changed", EventType.ProjectModified));
						}
					});
					colorPicker.setColor(color.getImage().getColor());
					skinner.getUI().getStage().addActor(colorPicker.fadeIn());
				}
			});
			scrollPaneBottom.setWidget(properties);
		}
	}
	
	class FontsTab extends BaseTab {
		public FontsTab() {
			super("Fonts");						
		}

		@Override
		void newElement() {
		}
		
		@Override
		void filter(String filter) {
		}

		@Override
		void update() {
		}
	}
	
	class DrawablesTab extends BaseTab {
		public DrawablesTab() {
			super("Drawables");
		}

		@Override
		void newElement() {
		}
		
		@Override
		void filter(String filter) {
		}
		
		@Override
		void update() {
		}
	}
	
	class StylesTab extends BaseTab {
		public StylesTab() {
			super("Styles");
		}

		@Override
		void newElement() {
		}
		
		@Override
		void filter(String filter) {
		}
		
		@Override
		void update() {
		}
	}
	
	class ListNode<T> extends Tree.Node {
		final T obj;
		public ListNode(Actor actor, T obj) {
			super(actor);
			this.obj = obj;
		}
		public T getObject() {
			return obj;
		}
	}
}
