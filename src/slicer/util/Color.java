package slicer.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Color extends ArrayList<String> implements Serializable {
	public enum InteractionType {
		INTERACTIONS_INTRA_METHOD, // interaction that happening in the method
		INTERACTIONS_INTER_METHODS, // interaction that happening in different
									// methods
		INTERACTIONS_INTER_CLASSES, // interaction that happening in different
									// classes
		NOTHING // it is nothing
	};

	private static final long serialVersionUID = 1L;

	private String sourceClass;
	private String sourceMethod;
	private String sourceLine;
	private int setId;

	public Color(String sourceClass, String sourceMethod, String sourceLine,
			int setId) {
		super();
		this.sourceClass = sourceClass;
		this.sourceMethod = sourceMethod;
		this.sourceLine = sourceLine;
		this.setId = setId;
	}

	public int getSetId() {
		return setId;
	}

	// public Color() {
	// super();
	// }

	public List<String> getFeatures() {
		List<String> features = new ArrayList<String>();
		for (String f : this) {
			if (f != null) {
				features.add(f);
			}
		}
		return features;
	}

	// I:smashed/Vertex.java:163:[SEARCH, DFS]
	// private Collection<? extends String> extractFeature(String line) {
	//
	// }

	public String getSourceClass() {
		return sourceClass;
	}

	public String getSourceMethod() {
		return sourceMethod;
	}

	public String getSourceLine() {
		return this.sourceLine;
	}

	// optimize this
	// -----
	public boolean containMandatoryFeature() {
		boolean result = false;
		for (String feature : this) {
			if (!feature.contains(FeatureUtilities.FEATURE_INFO_SEPARATOR)) {
				result = true;
				break;
			}
		}
		return result;
	}

	public boolean containOptionalFeature() {
		boolean result = false;
		for (String feature : this) {
			if (feature.contains(FeatureUtilities.OPTIONAL_FEATURE_INDICATOR)) {
				result = true;
				break;
			}
		}
		return result;
	}

	public String toString() {
		String orig = super.toString();
		return "I:" + sourceLine + ":" + orig;
	}

	public boolean equals(Object o) {
		if (this.sourceLine.equals(((Color) o).getSourceLine()))
			return true;
		// return this.sourceLine.equals( ((Color) o).getSourceLine() );
		return this.hashCode() == ((Color) o).hashCode();
	}

	public InteractionType getInteractionType(Color other) {
		// checking if this and other are a valid color && and if are different
		if (this.sourceClass != null && other.sourceClass != null
				&& other != this) {
			if (!this.sourceClass.equals(other.sourceClass)) {
				return InteractionType.INTERACTIONS_INTER_CLASSES;
			} else if (!this.sourceMethod.equals(other.sourceMethod)) {
				return InteractionType.INTERACTIONS_INTER_METHODS;
			} else {
				return InteractionType.INTERACTIONS_INTRA_METHOD;
			}
		}
		return InteractionType.NOTHING;
	}

	private int hashCode = -1;

	public int hashCode() {
		if (hashCode == -1) {
			if (this.sourceLine == null) {
				hashCode = -2;
			} else {
				hashCode = toString().hashCode();
			}
		}
		return hashCode;
	}

}
