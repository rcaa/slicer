package slicer.tests;

public class BiggerSwitchSample {
	
	public static void main(String[] args) {
		
	}
	
	static int SHOW_COMMENT = 0;
	static int SHOW_TEXT = 1;
	static int SHOW_CDATA_SECTION = 2;
	static int SHOW_PROCESSING_INSTRUCTION = 3;
	static int SHOW_NAMESPACE = 4;
	static int SHOW_ATTRIBUTE = 5;
	static int SHOW_ALL = 6;
	static int SHOW_DOCUMENT = 7;
	static int SHOW_DOCUMENT_FRAGMENT = 8;
	static int SHOW_ELEMENT = 9;
	static int SHOW_BYFUNCTION = 10;
	
	public static int getWhatToShowSimple(int opPos) {
	    int axesType = getOp(opPos);
	    switch (axesType) {
	    case 1:
	    	return SHOW_NAMESPACE;
	    case 2:
	    case 3:
	    	return SHOW_ATTRIBUTE;
	    	// break;
	    case 4:
	    case 5:
	    	return SHOW_ELEMENT;
	    	// break;
	    default :
	    	return SHOW_ELEMENT;	
	    }
	}

	public static int getWhatToShow(int opPos) {

	    int axesType = getOp(opPos);
	    int testType = getOp(opPos + 3);

	    switch (testType) {
	    case 1:
	        return SHOW_COMMENT;
	    case 2:
	        return SHOW_TEXT | SHOW_CDATA_SECTION;
	    case 3:
	        return SHOW_PROCESSING_INSTRUCTION;
	    case 4:
	        switch (axesType) {
	        case 1:
	            return SHOW_NAMESPACE;
	        case 2:
	        case 3:
	            return SHOW_ATTRIBUTE;
	        case 4:
	        case 5:
	        case 6:
	            return SHOW_ALL;
	        default:
	            if (getOp(0) == 1)
	                return ~SHOW_ATTRIBUTE
	                    & ~SHOW_DOCUMENT
	                    & ~SHOW_DOCUMENT_FRAGMENT;
	            else
	                return ~SHOW_ATTRIBUTE;
	        }
	    case 5:
	    	return SHOW_DOCUMENT | SHOW_DOCUMENT_FRAGMENT;
	    case 6:
	    	return SHOW_BYFUNCTION;
	    case 7:
	    	switch (axesType) {
	    	case 1:
	    		return SHOW_NAMESPACE;
	    	case 2:
	    	case 3:
	    		return SHOW_ATTRIBUTE;
	    		// break;
	    	case 4:
	    	case 5:
	    		return SHOW_ELEMENT;
	    		// break;
	    	default :
	    		return SHOW_ELEMENT;
	    	}
	    default :
	    	return SHOW_ALL;
	    }
	}

	public static int getOp(int k) {
	    return 0;
	}


}
