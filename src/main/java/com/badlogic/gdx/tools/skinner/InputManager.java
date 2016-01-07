package com.badlogic.gdx.tools.skinner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.tools.skinner.ShortCutManager.ShortCut;
import com.badlogic.gdx.tools.skinner.ShortCutManager.ShortCutHandler;

public class InputManager {
	final Skinner skinner;
	final InputMultiplexer multiplexer;
	final ShortCutManager shortCutManager;

	public InputManager(Skinner skinner) {
		this.skinner = skinner;
		this.multiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(multiplexer);
		this.shortCutManager = new ShortCutManager();
		this.multiplexer.addProcessor(skinner.getUI().getStage());
		multiplexer.addProcessor(new InputAdapter() {
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				skinner.getUI().getStage().setKeyboardFocus(null);
				skinner.getUI().getStage().setScrollFocus(null);
				return false;
			}
		});
		multiplexer.addProcessor(shortCutManager);
	}

	public void addShortCut(ShortCut shortCut, ShortCutHandler handler) {
		shortCutManager.addShortCut(shortCut, handler);
	}

	public InputMultiplexer getMultiplexer() {
		return multiplexer;
	}
}
