package sudoku.main;

import java.util.List;
import java.util.LinkedList;

import sudoku.schemata.FeatureNotSelectedException;
import sudoku.schemata.Variation;

public class Guesser {

    public List guess(Board board) { //SOLVER
    	if(Variation.SOLVER){
	        // find unset field with the least possibilities, i.e. struct and field index by iterating over rows
	        int structIndex = -1; //SOLVER
	        int elementIndex = -1; //SOLVER
	        for (int i = 0; i < Field.POSSIBILITIES; i++) //SOLVER
	            for (int j = 0; j < Field.POSSIBILITIES; j++) //SOLVER
	                if (!board.getField(Structure.ROW, i, j).isSet() //SOLVER
	                        && ((-1 == structIndex && -1 == elementIndex) //SOLVER
	                            || (board.getField(Structure.ROW, i, j).getRemainingPos().size() //SOLVER
	                                < board.getField(Structure.ROW, structIndex, elementIndex) //SOLVER
	                                .getRemainingPos().size()))) { //SOLVER
	                    structIndex=i; //SOLVER
	                    elementIndex=j; //SOLVER
	                } //SOLVER
	        List guessed = new LinkedList(); //SOLVER
	        // guess all possibilities
	        for (int i = 0; i < board.getField(Structure.ROW, structIndex, elementIndex) //SOLVER
	                .getRemainingPos().size(); i++) { //SOLVER
	            try { //SOLVER
	                Board guess = (Board) board.clone(); //SOLVER
	                if (guess.trySetField(Structure.ROW, structIndex, elementIndex, //SOLVER
	                                      new Field((Integer) board.getField(Structure.ROW, structIndex, elementIndex) //SOLVER
	                                                .getRemainingPos().get(i)))) //SOLVER
	                    guessed.add(guess); //SOLVER
	            } catch (CloneNotSupportedException e) { //SOLVER
	            	e.printStackTrace();
	            } //SOLVER
	
	        } //SOLVER
	        return guessed; //SOLVER
    	} else throw new FeatureNotSelectedException("SOLVER");
    } //SOLVER

}