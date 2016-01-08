package com.badlogic.gdx.tools.skinner.windows.projectelements;

import java.util.Iterator;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.tools.skinner.Skinner;
import com.badlogic.gdx.tools.skinner.model.ProjectElement;
import com.badlogic.gdx.tools.skinner.windows.UITab;
import com.badlogic.gdx.tools.skinner.windows.projectelements.ProjectElementList.ProjectElementListListener;
import com.badlogic.gdx.utils.Array;

public abstract class ProjectElementTab<T extends ProjectElement> extends UITab implements ProjectElementListListener<T> {
	protected final Skinner skinner;
	protected final ScrollPane scrollPaneTop;
	protected final ScrollPane scrollPaneBottom;
	protected String filter = "";
	protected String selectionId;
	
	public ProjectElementTab(Skinner skinner, String title) {
		super(title);
		this.skinner = skinner;			
		
		TextButton newElement = new TextButton("New", skinner.getUI().getSkin());
		TextField filter = new TextField("", skinner.getUI().getSkin());			
		scrollPaneTop = new ScrollPane(getList().getContent(), skinner.getUI().getSkin());
		scrollPaneBottom = new ScrollPane(getProperties().getContent(), skinner.getUI().getSkin());
		SplitPane splitPane = new SplitPane(scrollPaneTop, scrollPaneBottom, true, skinner.getUI().getSkin());
		
		Table content = getContentTable();
		content.defaults().left().top();
		content.add(newElement).left();
		content.add(filter).fillX().expandX();
		content.row();
		content.add(splitPane).colspan(2).expand().fill();		

		newElement.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				newElement();				
			}
		});
		
		filter.setTextFieldListener(new TextFieldListener() {
			@Override
			public void keyTyped(TextField textField, char c) {
				ProjectElementTab.this.filter = textField.getText().trim().toLowerCase();
				update();
			}
		});				
	}
	
	@Override
	public void listElementSelected(Node node, T element) {
		if(node == null) {
			getProperties().getContent().setVisible(false);
		} else {
			selectionId = element.getId();
			getProperties().setProjectElement(element);
			getProperties().getContent().setVisible(true);
		}
	}
	
	public void update() {
		Array<T> elements = new Array<>(getAllElements());
		Iterator<T> iter = elements.iterator();
		while(iter.hasNext()) {
			T element = iter.next();
			if(filter.length() != 0 && !element.getName().toLowerCase().contains(filter)) {
				iter.remove();
			}
		}
		elements.sort((o1, o2) -> {
			return o1.getName().compareTo(o2.getName());
		});
		getList().setProjectElements(elements);
		for(Node node: getList().getContent().getRootNodes()) {
			if(((ProjectElement)node.getObject()).getId().equals(selectionId)) {
				getList().getContent().getSelection().set(node);
				break;
			}
		}
	}
	
	public abstract Array<T> getAllElements();
	public abstract ProjectElementList<T> getList();
	public abstract ProjectElementProperties<T> getProperties();		
	
	public abstract void newElement();
	public abstract void removeElement(T element);
}