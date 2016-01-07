package com.badlogic.gdx.tools.skinner.utils;

import java.io.File;

import com.kotcrab.vis.ui.InputValidator;

public class PathValidator implements InputValidator {
	@Override
	public boolean validateInput(String input) {
		return input != null && new File(input).exists();
	}
}
