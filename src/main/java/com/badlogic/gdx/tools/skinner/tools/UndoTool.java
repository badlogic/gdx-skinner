package com.badlogic.gdx.tools.skinner.tools;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.tools.skinner.ShortCutManager.ShortCut;
import com.badlogic.gdx.tools.skinner.Skinner;

public class UndoTool implements Tool {
	private static final String SHORTCUT_UNDO = UndoTool.class.getSimpleName() + ".shortcut-undo";
	private static final String SHORTCUT_REDO = UndoTool.class.getSimpleName() + ".shortcut-redo";
	Skinner skinner;

	@Override
	public void setup(Skinner skinner) {
		this.skinner = skinner;
		addShortCuts();
	}

	public void addShortCuts() {
		ShortCut[] shortCuts = new ShortCut[] { new ShortCut("Undo", SHORTCUT_UNDO, new int[] { Keys.CONTROL_LEFT, Keys.Z }),
				new ShortCut("Undo", SHORTCUT_UNDO, new int[] { Keys.CONTROL_LEFT, Keys.Y }),
				new ShortCut("Redo", SHORTCUT_REDO, new int[] { Keys.CONTROL_LEFT, Keys.SHIFT_LEFT, Keys.Z }),
				new ShortCut("Redo", SHORTCUT_REDO, new int[] { Keys.CONTROL_LEFT, Keys.SHIFT_LEFT, Keys.Y }), };
		for(ShortCut shortCut: shortCuts) {
			skinner.getInputManager().addShortCut(shortCut, this);
		}
	}

	@Override
	public void handleShortCut(ShortCut shortCut) {
		if (shortCut.getIdentifier().equals(SHORTCUT_UNDO)) {
			skinner.getUndoManager().undo();
		} else if (shortCut.getIdentifier().equals(SHORTCUT_REDO)) {
			skinner.getUndoManager().redo();
		}
	}

	@Override
	public void render() {
	}
}
