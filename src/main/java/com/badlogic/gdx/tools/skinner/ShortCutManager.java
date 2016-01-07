package com.badlogic.gdx.tools.skinner;

import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class ShortCutManager extends InputAdapter {
	final static boolean isMac = System.getProperty("os.name").toLowerCase().contains("mac");

	private static final int MAC_COMMAND_KEY = 63;

	ObjectMap<ShortCut, ShortCutHandler> shortCuts = new ObjectMap<>();
	Array<ShortCut> sortedShortCuts = new Array<>();
	Comparator<ShortCut> shortCutSorter = new Comparator<ShortCut>() {
		@Override
		public int compare(ShortCut o1, ShortCut o2) {
			return o2.getKeys().length - o1.getKeys().length;
		}
	};

	public void addShortCut(ShortCut shortCut, ShortCutHandler provider) {
		shortCuts.put(shortCut, provider);
		sortedShortCuts.add(shortCut);
		sortedShortCuts.sort(shortCutSorter);
	}

	@Override
	public boolean keyDown(int keycode) {
		for (ShortCut shortCut : sortedShortCuts) {
			boolean hit = true;
			for (int key : shortCut.getKeys()) {
				if (key == Keys.CONTROL_LEFT) {
					if (!isMac) {
						hit &= (Gdx.input.isKeyPressed(key) || Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT));
					} else {
						hit &= Gdx.input.isKeyPressed(MAC_COMMAND_KEY);
					}
				} else {
					hit &= Gdx.input.isKeyPressed(key);
				}
			}
			if (hit) {
				shortCuts.get(shortCut).handleShortCut(shortCut);
				return true;
			}
		}
		return false;
	}

	static public class ShortCut {
		final String name;
		final String identifier;
		final int[] keys;

		public ShortCut(String name, String identifier, int[] keys) {
			this.name = name;
			this.identifier = identifier;
			this.keys = keys;
		}

		public String getName() {
			return name;
		}

		public String getIdentifier() {
			return identifier;
		}

		public int[] getKeys() {
			return keys;
		}
	}

	public interface ShortCutHandler {
		void handleShortCut(ShortCut shortCut);
	}
}
