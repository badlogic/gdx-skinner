package com.badlogic.gdx.tools.skinner;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.tools.skinner.windows.UIWindow;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.file.FileChooser;

public class UI implements Disposable {
	final Skinner skinner;
	final Skin skin;
	final Stage stage;
	final Table toolBar;
	final ObjectMap<Class<? extends UIWindow>, UIWindow> windows = new ObjectMap<Class<? extends UIWindow>, UIWindow>();
	final TextureRegionDrawable pixelDrawable;
	
	public UI(Skinner skinner) {
		VisUI.load();
		FileChooser.setFavoritesPrefsName("skinner.filechooser");
		this.skinner = skinner;
		this.skin = VisUI.getSkin();
		this.stage = new Stage(new ScreenViewport());

		toolBar = new Table();
		toolBar.setFillParent(true);
		toolBar.top().left();
		stage.addActor(toolBar);
		
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		pixelDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
		pixmap.dispose();
	}

	public Skin getSkin() {
		return skin;
	}

	public Table getToolbar() {
		return toolBar;
	}
	
	public TextureRegionDrawable getPixelDrawable() {
		return pixelDrawable;
	}

	@SuppressWarnings("unchecked")
	public <T extends UIWindow> T getWindow(Class<T> windowClass) {
		return (T) windows.get(windowClass);
	}
	
	public void addWindow(UIWindow window) {
		if(windows.containsKey(window.getClass())) {
			windows.get(window.getClass()).remove();
		}
		Rectangle lastPos = skinner.readJSONPref(window.getClass().getName() + ".size", Rectangle.class);
		if (lastPos != null) {
			window.setPosition(lastPos.x, lastPos.y);
			window.setSize(lastPos.width, lastPos.height);
		}
		windows.put(window.getClass(), window);
		stage.addActor(window);
	}

	public void render() {
		stage.act();
		stage.draw();
	}

	public Stage getStage() {
		return stage;
	}

	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void dispose() {
		for (UIWindow window : windows.values()) {
			skinner.writeJSONPref(window.getClass().getName() + ".size",
					new Rectangle(window.getX(), window.getY(), window.getWidth(), window.getHeight()));
			window.dispose();
		}
		stage.dispose();
		pixelDrawable.getRegion().getTexture().dispose();
	}	
}
