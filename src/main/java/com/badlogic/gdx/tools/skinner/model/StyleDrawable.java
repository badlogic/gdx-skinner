package com.badlogic.gdx.tools.skinner.model;

public class StyleDrawable extends ProjectElement {
	String file;
	StyleDrawableType type;
	
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}

	public StyleDrawableType getType() {
		return type;
	}

	public void setType(StyleDrawableType type) {
		this.type = type;
	}
	
	static public enum StyleDrawableType {
		Image,
		TiledImage,
		NinePatch
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StyleDrawable other = (StyleDrawable) obj;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}
