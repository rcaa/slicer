package slicer;

import gov.nasa.jpf.util.SourceRef;

public class FeatureSoureRef extends SourceRef {

	protected String feature;

	public FeatureSoureRef(String fileName, int line, String feature) {
		super(fileName, line);
		this.feature = feature;
	}

	public int hashCode() {
		return this.fileName.hashCode() + line + feature.hashCode();
	}

	public boolean equals(Object o) {
		FeatureSoureRef other = (FeatureSoureRef) o;
		return (this.fileName.equals(other.fileName) && this.line == other.line && this.feature
				.equals(other.feature));
	}

	public String getFeature() {
		return feature;
	}
}
