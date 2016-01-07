package com.badlogic.gdx.tools.skinner.windows;

import com.badlogic.gdx.tools.skinner.Skinner;
import com.badlogic.gdx.utils.Disposable;
import com.kotcrab.vis.ui.widget.VisWindow;

public abstract class UIWindow extends VisWindow implements Disposable {
	protected final Skinner skinner;
	
	public UIWindow(Skinner skinner, String title) {
		super(title);
		this.skinner = skinner;
	}
}