package com.badlogic.gdx.tools.skinner.model;

import java.io.UnsupportedEncodingException;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;

public class ProjectSerializer {
	private static final Json json = new Json();
	
	public static byte[] serialize(Project project) {
		String str = json.prettyPrint(project);
		try {
			return str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new GdxRuntimeException("This should never ever happen", e);
		}
	}
	
	public static Project deserialize(byte[] bytes) {
		try {
			return json.fromJson(Project.class, new String(bytes, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new GdxRuntimeException("This should never ever happen", e);
		}
	}
}
