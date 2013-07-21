package sudoku.main;
import java.util.List;
import java.util.Random;

import sudoku.schemata.FeatureNotSelectedException;
import sudoku.schemata.Variation;

public class SudokuGenerator { //GENERATOR

    public Board generate() { //GENERATOR
        if(Variation.GENERATOR){
	        Board board = new Board(); //GENERATOR                 
	        fillBoard(board); //GENERATOR            
	        makeSolvable(board, 50); //GENERATOR                  
	        return board; //GENERATOR
        } else throw new FeatureNotSelectedException("GENERATOR");
    }
    
    private void fillBoard(Board board){ //GENERATOR
    	if(Variation.GENERATOR){
	        BoardManager bm = new BoardManager(); //GENERATOR	        
	        Random r = new Random(999); //GENERATOR	        
	        bm.setBoard(board);       //GENERATOR	
	        int fieldsToFill = Field.POSSIBILITIES * Field.POSSIBILITIES; //GENERATOR	        
	        //set all fields with random numbers if possible
	        //try to solve, if not possible remove one number and try again...	        
	        for(int i=0; i< fieldsToFill; i++){ //GENERATOR	
	            int row = r.nextInt(Field.POSSIBILITIES);//GENERATOR
	            int fieldIndex = r.nextInt(Field.POSSIBILITIES); //GENERATOR	            
	            List remainingPos = bm.getField(Structure.ROW, //GENERATOR
	                row, fieldIndex).getRemainingPos(); //GENERATOR	
	            if(remainingPos.size()>0){//GENERATOR
	                int value = (Integer)remainingPos.get(r.nextInt(remainingPos.size()));//GENERATOR	             
	                boolean result = bm.trySetFieldPrivate(Structure.ROW, row, fieldIndex, new Field(value, true));//GENERATOR	        
	                if (!result) {//GENERATOR
	                    bm.undo();//GENERATOR
	                }  //GENERATOR	
	            } else {//GENERATOR
	                bm.undo();//GENERATOR
	            }//GENERATOR
	        } //GENERATOR
    	} else throw new FeatureNotSelectedException("GENERATOR");
    } //GENERATOR
    
    private boolean makeSolvable(Board board, int steps){ //GENERATOR
    	if(Variation.GENERATOR){
	        BoardManager bm = new BoardManager(); //GENERATOR
	        bm.setBoard(board); //GENERATOR
	        Random r = new Random(999); //GENERATOR
	        int k = steps; //GENERATOR       
	        try{ //GENERATOR        
		        //board filled, check solution
		        List solutions = bm.solve((Board) board.clone()); //GENERATOR
		        boolean solvable = !solutions.isEmpty(); //GENERATOR
		        while(k>0){ //GENERATOR
		            //remove random number and try again
		            board.removeRandomSetField(); //GENERATOR
		            solutions = bm.solve((Board) board.clone()); //GENERATOR
		            if(!solutions.isEmpty()){ //GENERATOR
		                return true; //GENERATOR
		            } //GENERATOR
		            k--;          //GENERATOR    
		        }        
	        } catch (CloneNotSupportedException e) { //GENERATOR
	        	e.printStackTrace();
	        }
	        return false; //GENERATOR
    	} else throw new FeatureNotSelectedException("GENERATOR");
    }
       
}