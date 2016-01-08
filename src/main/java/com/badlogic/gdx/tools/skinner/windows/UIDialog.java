package com.badlogic.gdx.tools.skinner.windows;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.tools.skinner.Skinner;
import com.kotcrab.vis.ui.widget.VisDialog;

public class UIDialog extends VisDialog {
	final Skinner skinner;
	final UIDialogType type;
	UIDialogListener listener;
	TextButton ok;
	TextButton yes;
	TextButton no;
	TextButton cancel;

	public UIDialog(Skinner skinner, String title, UIDialogType type) {
		super(title);
		this.skinner = skinner;
		this.type = type;

		ok = new TextButton("OK", skinner.getUI().getSkin());
		yes = new TextButton("Yes", skinner.getUI().getSkin());
		no = new TextButton("No", skinner.getUI().getSkin());
		cancel = new TextButton("Cancel", skinner.getUI().getSkin());

		if (type == UIDialogType.OK) {
			getButtonsTable().add(ok);
		} else if (type == UIDialogType.OKCancel) {
			getButtonsTable().add(cancel);
			getButtonsTable().add(ok);
		} else if (type == UIDialogType.YesNo) {
			getButtonsTable().add(no);
			getButtonsTable().add(yes);
		} else {
			getButtonsTable().add(cancel);
			getButtonsTable().add(no);
			getButtonsTable().add(yes);
		}

		ok.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (listener != null) {
					listener.yesOrOk();
				}
			}
		});
		yes.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (listener != null) {
					listener.yesOrOk();
				}
			}
		});
		no.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (listener != null) {
					listener.no();
				}
			}
		});
		cancel.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (listener != null) {
					listener.cancel();
				}
			}
		});
	}

	public TextButton getOk() {
		return ok;
	}

	public TextButton getYes() {
		return yes;
	}

	public TextButton getNo() {
		return no;
	}

	public TextButton getCancel() {
		return cancel;
	}

	public void show() {
		show(skinner.getUI().getStage());
	}

	protected void setListener(UIDialogListener listener) {
		this.listener = listener;
	}

	public interface UIDialogListener {
		void yesOrOk();

		void no();

		void cancel();
	}

	static public class UIDialogAdapter implements UIDialogListener {
		@Override
		public void yesOrOk() {
		}

		@Override
		public void no() {
		}

		@Override
		public void cancel() {
		}
	}

	static public enum UIDialogType {
		OK, OKCancel, YesNo, YesNoCancel
	}
}
