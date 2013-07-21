package sudoku.main;

import sudoku.schemata.FeatureNotSelectedException;
import sudoku.schemata.Variation;

public class ForcedField implements Solver {

    public boolean trySolve(Board board) { //SOLVER
    	if(Variation.SOLVER){
	        for (int i = 0; i < Field.POSSIBILITIES; i++)  //SOLVER
	            for (int j = 0; j < Field.POSSIBILITIES; j++)  //SOLVER
	                if ((board.getField(Structure.ROW, i, j).getRemainingPos().size() == 1)  //SOLVER
	                        && !board.getField(Structure.ROW, i, j).isSet())  //SOLVER
	                    if (!board.trySetField(Structure.ROW, i, j, new Field((Integer) board.getField(Structure.ROW, i, j).getRemainingPos().get(0))))  //SOLVER
	                        return false;  //SOLVER
	        return true;  //SOLVER
    	} else throw new FeatureNotSelectedException("SOLVER");
    }  //SOLVER
} 