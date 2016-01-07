package com.badlogic.gdx.tools.skinner.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.tools.skinner.model.Project;
import com.badlogic.gdx.tools.skinner.model.ProjectSerializer;
import com.badlogic.gdx.tools.skinner.model.StyleColor;
import com.badlogic.gdx.utils.ObjectMap;

public class SerializationTest {
	@Test
	public void testSerialization() {
		ObjectMap<Integer, String> map = new ObjectMap<>();
		map.put(1, "Test");
		String out = map.get(1);
		
		Project project = new Project();
		project.newColor("red", Color.RED);
		project.newColor("green", Color.GREEN);
		
		byte[] bytes = ProjectSerializer.serialize(project);
		Project projectCopy = ProjectSerializer.deserialize(bytes);
		
		assertEquals(project.getColors().size, projectCopy.getColors().size);
		for(String id: project.getColors().keys()) {
			StyleColor color = project.getColors().get(id);
			StyleColor colorCopy = projectCopy.getColors().get(id);
			assertEquals(color, colorCopy);
		}
	}
}
