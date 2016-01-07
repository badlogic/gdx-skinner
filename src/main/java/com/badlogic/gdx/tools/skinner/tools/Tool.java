package com.badlogic.gdx.tools.skinner.tools;

import com.badlogic.gdx.tools.skinner.ShortCutManager.ShortCutHandler;
import com.badlogic.gdx.tools.skinner.Skinner;

public interface Tool extends ShortCutHandler {
	void setup(Skinner skinner);
	void render();
}
