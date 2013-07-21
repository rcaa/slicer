package sudoku.main;

import sudoku.schemata.FeatureNotSelectedException;
import sudoku.schemata.Variation;

public class ForcedNumber implements Solver {

    public boolean trySolve(Board board) {  //SOLVER
    	if(Variation.SOLVER){
	        // iterate over 
	        // structure types
	        for (int i = 0; i < Structure.values().length; i ++) {  //SOLVER
	            // structure indices
	            for (int j = 0; j < Field.POSSIBILITIES; j++) {  //SOLVER
	                // possibilities
	                for (int k = 1; k <= Field.POSSIBILITIES; k++) {  //SOLVER
	                    int counter = 0;  //SOLVER
	                    int element = -1;  //SOLVER
	                    // structure elements
	                    for (int l = 0; l < Field.POSSIBILITIES; l++) {  //SOLVER
	                        if (board.getField(Structure.values()[i], j, l).getRemainingPos() 
	                                .contains((Object) k)) {  //SOLVER
	                            counter++; //SOLVER
	                            element = l;  //SOLVER
	                        } //SOLVER
	                        if (counter > 1) //SOLVER
	                            break; //SOLVER
	                    } //SOLVER
	                    if (counter == 1 //SOLVER
	                            && !board.getField(Structure.values()[i], j, element).isSet() //SOLVER
	                            && !board.trySetField(Structure.values()[i], j, element, new Field(k))) //SOLVER
	                        return false; //SOLVER
	                } //SOLVER
	            } //SOLVER
	        } //SOLVER
	        return true; //SOLVER
    	} else throw new FeatureNotSelectedException("SOLVER");
    } //SOLVER
}