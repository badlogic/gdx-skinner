package com.badlogic.gdx.tools.skinner.tools;

import java.io.File;
import java.io.FileFilter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowListener;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.tools.skinner.EventBus.Event;
import com.badlogic.gdx.tools.skinner.EventBus.EventType;
import com.badlogic.gdx.tools.skinner.ShortCutManager.ShortCut;
import com.badlogic.gdx.tools.skinner.Skinner;
import com.badlogic.gdx.tools.skinner.model.Project;
import com.badlogic.gdx.tools.skinner.model.ProjectSerializer;
import com.badlogic.gdx.tools.skinner.utils.PathValidator;
import com.kotcrab.vis.ui.InputValidator;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.util.dialog.DialogUtils;
import com.kotcrab.vis.ui.util.dialog.DialogUtils.OptionDialogType;
import com.kotcrab.vis.ui.util.dialog.OptionDialogAdapter;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooser.Mode;
import com.kotcrab.vis.ui.widget.file.FileChooser.SelectionMode;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;

public class FileTool implements Tool {
	private final String SHORTCUT_NEW_PROJECT = FileTool.class.getSimpleName() + ".shortcut-new";
	private final String SHORTCUT_SAVE = FileTool.class.getSimpleName() + ".shortcut-save";
	private final String SHORTCUT_EXPORT = FileTool.class.getSimpleName() + ".shortcut-export";
	private final String SHORTCUT_OPEN = FileTool.class.getSimpleName() + ".shortcut-open";
	
	Skinner skinner;

	public void addShortCuts() {
		ShortCut[] shortCuts = new ShortCut[] { new ShortCut("New", SHORTCUT_NEW_PROJECT, new int[] { Keys.CONTROL_LEFT, Keys.N }),
				new ShortCut("Save", SHORTCUT_SAVE, new int[] { Keys.CONTROL_LEFT, Keys.S }),
				new ShortCut("Export", SHORTCUT_EXPORT, new int[] { Keys.CONTROL_LEFT, Keys.E }),
				new ShortCut("Open", SHORTCUT_OPEN, new int[] { Keys.CONTROL_LEFT, Keys.O }) };
		for(ShortCut shortCut: shortCuts) {
			skinner.getInputManager().addShortCut(shortCut, this);
		}
	}

	@Override
	public void handleShortCut(ShortCut shortCut) {
		if (shortCut.getIdentifier().equals(SHORTCUT_NEW_PROJECT)) {
			newProject(true);
		} else if (shortCut.getIdentifier().equals(SHORTCUT_EXPORT)) {
			exportProject();
		} else if (shortCut.getIdentifier().equals(SHORTCUT_SAVE)) {
			saveProject();
		} else if (shortCut.getIdentifier().equals(SHORTCUT_OPEN)) {
			openProject(true);
		}
	}

	@Override
	public void setup(Skinner skinner) {
		this.skinner = skinner;
		
		Lwjgl3Window window = ((com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics)Gdx.graphics).getWindow();		
		window.setWindowListener(new Lwjgl3WindowListener() {
			@Override
			public void iconified() {
			}

			@Override
			public void deiconified() {
			}

			@Override
			public void focusLost() {
			}

			@Override
			public void focusGained() {
			}

			@Override
			public boolean windowIsClosing() {
				if(skinner.needsSaving()) {
					DialogUtils.showOptionDialog(skinner.getUI().getStage(), "Save Changes", "Save changes before closing Skinner?", OptionDialogType.YES_NO_CANCEL, new OptionDialogAdapter() {
						@Override
						public void yes() {
							saveProject();
							window.closeWindow();
						}

						@Override
						public void no() {
							window.closeWindow();
						}
					});
					return false;
				} else {
					return true;
				}				
			}
		});
		addShortCuts();

		// create toolbar entries
		TextButton fileButton = new TextButton("File", skinner.getUI().getSkin());
		skinner.getUI().getToolbar().add(fileButton);
		fileButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				PopupMenu menu = new PopupMenu();
				MenuItem newItem = new MenuItem("New");
				MenuItem openItem = new MenuItem("Open");
				MenuItem saveItem = new MenuItem("Save");
				saveItem.setDisabled(skinner.getProject() == null);
				MenuItem exportItem = new MenuItem("Export");
				menu.addItem(newItem);
				menu.addItem(openItem);
				menu.addItem(saveItem);
				menu.pack();
				menu.setPosition(fileButton.getX(), fileButton.getY() - menu.getHeight());
				skinner.getUI().getStage().addActor(menu);

				newItem.addListener(new ChangeListener() {
					@Override
					public void changed(ChangeEvent event, Actor actor) {
						newProject(true);
					}
				});

				openItem.addListener(new ChangeListener() {
					@Override
					public void changed(ChangeEvent event, Actor actor) {
						openProject(true);
					}
				});

				saveItem.addListener(new ChangeListener() {
					@Override
					public void changed(ChangeEvent event, Actor actor) {
						saveProject();
					}
				});

				exportItem.addListener(new ChangeListener() {
					@Override
					public void changed(ChangeEvent event, Actor actor) {
						exportProject();
					}
				});
			}
		});
	}

	private void newProject(boolean checkSave) {
		if(checkSave && skinner.needsSaving()) {
			DialogUtils.showOptionDialog(skinner.getUI().getStage(), "Save Changes", "Save changes before opening a new project?", OptionDialogType.YES_NO_CANCEL, new OptionDialogAdapter() {
				@Override
				public void yes() {
					saveProject();
					newProject(false);
				}

				@Override
				public void no() {
					newProject(false);
				}
			});
		} else {
			VisDialog dialog = new VisDialog("New Project");
			TableUtils.setSpacingDefaults(dialog);
			final TextButton ok = new TextButton("Ok", skinner.getUI().getSkin());
			ok.setDisabled(true);
			final TextButton cancel = new TextButton("Cancel", skinner.getUI().getSkin());
			final VisValidatableTextField name = new VisValidatableTextField(new InputValidator() {
				@Override
				public boolean validateInput(String input) {				
					return input != null && input.trim().length() > 0;
				}			
			});
			final VisValidatableTextField path = new VisValidatableTextField(new PathValidator() {
				@Override
				public boolean validateInput(String input) {
					boolean result = super.validateInput(input);
					ok.setDisabled(!result);
					return result;
				}
			});
			final TextButton browse = new TextButton("Browse", skinner.getUI().getSkin());
			
			dialog.getContentTable().add(new Label("Name: ", skinner.getUI().getSkin()));
			dialog.getContentTable().add(name).colspan(2).fillX();
			dialog.getContentTable().row();
			dialog.getContentTable().add(new Label("Path: ", skinner.getUI().getSkin()));
			dialog.getContentTable().add(path);
			dialog.getContentTable().add(browse);	
			dialog.getButtonsTable().add(cancel);
			dialog.getButtonsTable().add(ok);		
	
			browse.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					FileChooser fileChooser = new FileChooser(Mode.OPEN);
					fileChooser.setSelectionMode(SelectionMode.DIRECTORIES);
					fileChooser.setListener(new FileChooserAdapter() {
						@Override
						public void selected(FileHandle file) {
							if(file.child(name.getText() +  ".skin").exists()) {
								DialogUtils.showErrorDialog(skinner.getUI().getStage(), "File '" + file.child(name.getText() +  ".skin") + "' already exists");
							} else {
								path.setText(file.file().getAbsolutePath());
							}
						}
					});
					skinner.getUI().getStage().addActor(fileChooser.fadeIn());
				}
			});
			
			cancel.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					dialog.hide();
				}
			});
	
			ok.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if(!ok.isDisabled()) {
						dialog.hide();
						Project project = new Project();
						skinner.setProject(project);
						skinner.setProjectPath(new File(path.getText() + "/" + name.getText() + ".skin").getAbsolutePath());
						skinner.getEventBus().add(new Event("File tool action", EventType.NewProject));
					}
				}
			});
			
			dialog.show(skinner.getUI().getStage());
		}
	}

	private void openProject(boolean checkSave) {
		if(checkSave && skinner.needsSaving()) {
			DialogUtils.showOptionDialog(skinner.getUI().getStage(), "Save Changes", "Save changes before opening a new project?", OptionDialogType.YES_NO_CANCEL, new OptionDialogAdapter() {
				@Override
				public void yes() {
					saveProject();
					newProject(true);
				}

				@Override
				public void no() {
					newProject(true);
				}
			});
		} else {
			FileChooser fileChooser = new FileChooser(Mode.OPEN);
			fileChooser.setSelectionMode(SelectionMode.FILES);
			fileChooser.setFileFilter(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return pathname.getName().endsWith(".skin");
				}
			});			
			fileChooser.setListener(new FileChooserAdapter() {
				@Override
				public void selected(FileHandle file) {
					try {						
						skinner.setProject(ProjectSerializer.deserialize(file.readBytes()));
						skinner.setProjectPath(file.file().getAbsolutePath());
						skinner.getEventBus().add(new Event("Project opened", EventType.NewProject));
					} catch(Throwable t) {
						DialogUtils.showErrorDialog(skinner.getUI().getStage(), "Couldn't open project " + file.file().getAbsolutePath());
					}
				}
			});
			skinner.getUI().getStage().addActor(fileChooser.fadeIn());
		}
	}

	private void saveProject() {
		if(skinner.getProject() != null) {
			try {
				byte[] bytes = ProjectSerializer.serialize(skinner.getProject());
				Gdx.files.absolute(skinner.getProjectPath()).writeBytes(bytes, false);
				skinner.getEventBus().add(new Event("File tool action", EventType.ProjectSaved));
			} catch(Throwable t) {
				Gdx.app.error("FileTool",  "Couldn't save project", t);	
			}
		}
	}

	private void exportProject() {
	}

	@Override
	public void render() {
	}
}
