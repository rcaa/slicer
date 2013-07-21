package sudoku.main;

import java.io.File;
import java.io.IOException;


import sudoku.schemata.FeatureNotSelectedException;
import sudoku.schemata.Variation;

public class SudokuFacade {
	
	private Board board;
	private BoardManager bm; //BASE
	private SudokuGenerator sGen;  //GENERATOR     
	
	public SudokuFacade(){
		if(Variation.BASE){
			this.bm = new BoardManager();
			if(Variation.GENERATOR){
				this.sGen = new SudokuGenerator();
			} 
		} else throw new FeatureNotSelectedException("BASE");
	}
	
	public void setField(Structure structure, int structNr, int element, int field){
		this.board = bm.getBoard();
		Field f = new Field(field);
		System.out.println(f.toString());
		board.setField(structure, structNr, element, f);		
	}
	
	/**
     * Executes Load without GUI. 
     */
    public void loadFile(File f){
    	if(Variation.BASE){
	        if (f != null) { //BASE
	            try { //BASE
	                bm.loadFile(f); //BASE
	            } catch (IOException ex) { //BASE
	                ex.printStackTrace(); //BASE
	            } //BASE
	        } //BASE
	        
	        if(Variation.SOLVER){
		        if (f != null) { //SOLVER
		            try { //SOLVER
		                if (!bm.tryLoadFile(f)) //SOLVER
		                    System.out.println("Invalid sudoku! File was not loaded."); //SOLVER
		            } catch (IOException ex) { //SOLVER
		            	System.out.println("Invalid sudoku! File was not loaded."); //SOLVER
		            } //SOLVER
		        } //SOLVER
	        } 
    	} else throw new FeatureNotSelectedException("BASE");
    }
    
    /**
     * Executes GenerateSudoku without GUI. 
     */
    public void GenerateSudoku() {
    	if(Variation.GENERATOR){
//	        Thread worker = new Thread() { //GENERATOR
//	            public void run() { //GENERATOR
//	                bm.setBusy(true); //GENERATOR
	                Board b = sGen.generate(); //GENERATOR
//	                bm.loadSudoku(b); //GENERATOR
//	                bm.setBusy(false); //GENERATOR
//	            } //GENERATOR
//	        }; //GENERATOR
//	        worker.start(); //GENERATOR
    	} else throw new FeatureNotSelectedException("GENERATOR");
    }
    
    /**
     * Executes LoadSatate without GUI. 
     */
    public void LoadState(File f) {
    	if(Variation.STATES){
	        if (f != null) { //STATES
	            try { //STATES
	                bm.loadState(f); //STATES
	            } catch (IOException ex) { //STATES
	                ex.printStackTrace(); //STATES
	            } catch (ClassNotFoundException ex) { //STATES
	                ex.printStackTrace(); //STATES
	            } //STATES
	        } //STATES
	    } else throw new FeatureNotSelectedException("STATES");
    }
    
    /**
     * Executes SaveSatate without GUI. 
     */
    public void SaveState(File f) { 
    	if(Variation.STATES){
	        if (f != null) { //STATES
	            try { //STATES
	                bm.saveState(f); //STATES
	            } catch (IOException ex) { //STATES
	                ex.printStackTrace(); //STATES
	            } //STATES
	        } //STATES
    	} else throw new FeatureNotSelectedException("STATES");
    }
    
    /**
     * Executes SetPossibilities without GUI. 
     */
    public void setPossibilities(int numberOfPossibilities) {  //EXTENDEDSUDOKU
    	if(Variation.EXTENDEDSUDOKU){
        	bm.setPossibilities(numberOfPossibilities);  //EXTENDEDSUDOKU
		} else throw new FeatureNotSelectedException("EXTENDEDSUDOKU");
    }
    
    /**
     * Executes SolutionHint without GUI. 
     */
    public void solutionHint(){
    	if(Variation.SOLVER){
	        Thread worker = new Thread() {//SOLVER
	            public void run() {//SOLVER
	                if (!bm.solutionHint())//SOLVER
	                    System.out.println("Sudoku not solvable!");//SOLVER
	            }//SOLVER
	        };//SOLVER
	        worker.start();//SOLVER
    	} else throw new FeatureNotSelectedException("SOLVER");
    }
    
    /**
     * Executes Undo without GUI. 
     */
    public void undo(){
    	if(Variation.UNDO){
    		bm.undo();
    	} else throw new FeatureNotSelectedException("UNDO");
    }

}
