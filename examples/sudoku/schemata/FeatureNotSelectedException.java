package sudoku.schemata;

public class FeatureNotSelectedException extends RuntimeException {
	
	private String featureName;
	
	public FeatureNotSelectedException(String featureName){
		this.featureName = featureName;
	}
	
	public String getMessage(){
		return "Feature: " + this.featureName + " not selected." ;
	}

}
