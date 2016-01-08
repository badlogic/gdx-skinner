package com.badlogic.gdx.tools.skinner.windows.projectelements;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.tools.skinner.Skinner;
import com.badlogic.gdx.tools.skinner.model.ProjectElement;
import com.badlogic.gdx.utils.Array;

public abstract class ProjectElementList<T extends ProjectElement> {
	protected final Skinner skinner;
	private final Tree content;

	public ProjectElementList(Skinner skinner, ProjectElementListListener<T> listener) {
		this.skinner = skinner;		
		this.content = new Tree(skinner.getUI().getSkin());
		
		content.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Node node = content.getSelection().first();
				listener.listElementSelected(node, node != null ? (T) node.getObject() : null);
			}
		});
	}
	
	public Tree getContent() {
		return content;
	}

	public abstract void setProjectElements(Array<T> elements);
	
	public interface ProjectElementListListener<T> {
		void listElementSelected(Node node, T element);
	}

	public T getSelection() {
		Node node = content.getSelection().first();
		if(node != null) {
			return (T)node.getObject();
		} else {
			return null;
		}
	}
}
