package sudoku.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import sudoku.schemata.FeatureNotSelectedException;
import sudoku.schemata.Variation;

public class BoardManager { 
	
	protected Board board; //BASE
	protected List sudokuViews; //BASE	
	protected Stack history; //STATES
	
	public BoardManager() { //BASE
		if(Variation.BASE){
			sudokuViews = new LinkedList(); //BASE
			if(Variation.STATES){
				history = new Stack();//STATES
			} 
		} else throw new FeatureNotSelectedException("BASE");
	} //BASE
	
//	public void registerSudokuView(Gui f) { //BASE
//		if(Variation.BASE){
//			sudokuViews.add(f); //BASE
//			updateSudokuViews(); //BASE
//		} else throw new FeatureNotSelectedException("BASE");
//	} //BASE
	
	protected void updateSudokuViews() { //BASE
		if(Variation.BASE){
			for (int i = 0; i < sudokuViews.size(); i++) { //BASE
//				((Gui) sudokuViews.get(i)).update(getBoard()); //BASE
			} //BASE
		} else throw new FeatureNotSelectedException("BASE");
	} //BASE
	
	public void loadFile(File f) throws IOException { //BASE
		if(Variation.BASE){
			preLoadWrapper(); //BASE
			board = new Board(); //BASE
			BufferedReader fileReader = new BufferedReader(new FileReader(f)); //BASE
			
			int digit = (Field.POSSIBILITIES / 10) + 1;		 //BASE
			
			int row = 0; //BASE
			while (row < Field.POSSIBILITIES) { //BASE
				String sudokuLine = fileReader.readLine(); //BASE
				int value; //BASE
				char c; //BASE
				
				int extendedInt; //BASE
				char extendedC; //BASE
				
				if (digit == 1) { //BASE
					for (int i = 0; i < Field.POSSIBILITIES; i++) {            	 //BASE
						c = sudokuLine.charAt(i); //BASE
						
						if (c != '.') { //BASE
							value = Integer.parseInt(Character.toString(c)); //BASE
							setFieldPrivate(Structure.ROW, row, i, new Field(value,true)); //BASE
						} //BASE
					} //BASE
				} else if (digit == 2) {             //BASE
					for (int i = 0; i < Field.POSSIBILITIES * digit; i = i + digit) { //BASE
						c = sudokuLine.charAt(i); //BASE
						extendedC = sudokuLine.charAt(i + 1); //BASE
						
						if (c != '.') { //BASE
							value = Integer.parseInt(Character.toString(c)) * 10; //BASE
							extendedInt = Integer.parseInt(Character.toString(extendedC)); //BASE
							value += extendedInt; //BASE
							setFieldPrivate(Structure.ROW, row, (i/digit), new Field(value,true));						 //BASE
						}  //BASE
					} //BASE
				} //BASE
				
				row++; //BASE
			} //BASE
			updateSudokuViews(); //BASE
		} else throw new FeatureNotSelectedException("BASE");
	} //BASE
	
	protected void setFieldPrivate(Structure structure, int structNr, int element, Field f) { //BASE
		if(Variation.BASE){
			board.setField(structure, structNr, element, f); //BASE
		} else throw new FeatureNotSelectedException("BASE");
	} //BASE
	
	protected void preSetFieldWrapper(Structure structure, int structNr, int element, Field f) {
		if(Variation.BASE){
			if(Variation.STATES){
				try {//STATES
		            history.push(board.clone());//STATES
		        } catch (CloneNotSupportedException e) {//STATES
		        	e.printStackTrace();
		        }//STATES
			} 
		} else throw new FeatureNotSelectedException("BASE");
        
      //BASE do nothing
	} 
	
	protected void preLoadWrapper() { //BASE
		if(Variation.BASE){
			if(Variation.STATES){
				history.clear();//STATES
			} 
		} else throw new FeatureNotSelectedException("BASE");
		//BASE do nothing
	} //BASE
	
	public void setField(Structure structure, int structNr, int element, Field f) { //BASE
		if(Variation.BASE){
			preSetFieldWrapper(structure, structNr, element, f); //BASE
			setFieldPrivate(structure, structNr, element, f); //BASE
			updateSudokuViews(); //BASE
		} else throw new FeatureNotSelectedException("BASE");
	} //BASE
	
	public Field getField(Structure structure, int structNr, int element) { //BASE
		if(Variation.BASE){
			return getBoard().getField(structure, structNr, element); //BASE
		} else throw new FeatureNotSelectedException("BASE");
	} //BASE
	
	public Board getBoard() { 
		if(Variation.BASE){
			if (board == null) { //BASE
				board = new Board(); //BASE
			} //BASE
			return board; //BASE
			//return this.board; // SOLVER extrange
		} else throw new FeatureNotSelectedException("BASE");
	} 
	
	public void setBoard(Board board) { //BASE
		if(Variation.BASE){
			this.board = board; //BASE
		} else throw new FeatureNotSelectedException("BASE");
	} //BASE
	
    public void setPossibilities(int possibilities) {//EXTENDEDSUDOKU
    	if(Variation.EXTENDEDSUDOKU){
	        Field.POSSIBILITIES = possibilities;//EXTENDEDSUDOKU
	        this.board = null;//EXTENDEDSUDOKU
	        updateSudokuViews();//EXTENDEDSUDOKU
    	} else throw new FeatureNotSelectedException("EXTENDEDSUDOKU");
    }//EXTENDEDSUDOKU
    
    public void loadSudoku(Board board) { //GENERATOR
    	if(Variation.GENERATOR){
	        preLoadWrapper(); //GENERATOR
	        this.board = board; //GENERATOR
	        updateSudokuViews(); //GENERATOR
    	} else throw new FeatureNotSelectedException("GENERATOR");
    } //GENERATOR

    public void setBusy(boolean busy) { //SOLVER
    	if(Variation.SOLVER){
	        for (int i = 0; i < sudokuViews.size(); i++) { //SOLVER
//	            ((Gui) sudokuViews.get(i)).setBusy(busy); //SOLVER
	        } //SOLVER
    	} else throw new FeatureNotSelectedException("SOLVER");
    } //SOLVER
    
    protected boolean busy;  //SOLVER
    
    protected boolean trySetFieldPrivate(Structure structure, int structNr, int element, Field f) { //SOLVER
        if(Variation.SOLVER){
        	return board.trySetField(structure, structNr, element, f); //SOLVER
        } else throw new FeatureNotSelectedException("SOLVER");
    } //SOLVER
    
    public boolean trySetField(Structure structure, int structNr, int element, Field f) {//SOLVER
    	if(Variation.SOLVER){
	        preSetFieldWrapper(structure, structNr, element, f);//SOLVER
	        boolean set = trySetFieldPrivate(structure, structNr, element, f);//SOLVER
	        if (set) {//SOLVER
	            updateSudokuViews();//SOLVER
	            return true;//SOLVER
	        } else {//SOLVER
	            undo();//SOLVER
	            return false;//SOLVER
	        }//SOLVER
    	} else throw new FeatureNotSelectedException("SOLVER");
    }//SOLVER
    
    
    public boolean tryLoadFile(File f) throws IOException { //SOLVER
    	if(Variation.SOLVER){
	        preLoadWrapper(); //SOLVER
	        board = new Board(); //SOLVER
	        BufferedReader fileReader = new BufferedReader(new FileReader(f)); //SOLVER
	        
	        int digit = (Field.POSSIBILITIES / 10) + 1;//SOLVER
	
	        int row = 0;//SOLVER
	        while (row < Field.POSSIBILITIES) {//SOLVER
	            String sudokuLine = fileReader.readLine();//SOLVER
	            int value;//SOLVER
	            char c;//SOLVER
	
	            //for (int i = 0; i < Field.POSSIBILITIES; i++) {
	            //    c = sudokuLine.charAt(i);
	
	            //    if (c != '.') {
	            //        value = Integer.parseInt(Character.toString(c));
	            //        if (!trySetFieldPrivate(Structure.ROW, row, i, new Field(value,true))) {
	            //            board = null;
	            //            updateSudokuViews();
	            //            return false;
	            //        }
	            //    }            
	            //}
	            
	            int extendedInt;//SOLVER
	            char extendedC;//SOLVER
	            
	            if (digit == 1) {//SOLVER
		            for (int i = 0; i < Field.POSSIBILITIES; i++) {//SOLVER            	
		                c = sudokuLine.charAt(i);//SOLVER
		
		                if (c != '.') {//SOLVER
		                    value = Integer.parseInt(Character.toString(c));//SOLVER
		                    if (!trySetFieldPrivate(Structure.ROW, row, i, new Field(value,true))) {//SOLVER
	                        	board = null;//SOLVER
	                        	updateSudokuViews();//SOLVER
	                        	return false;//SOLVER
	                        }//SOLVER
		                }//SOLVER
		            }//SOLVER
	            } else if (digit == 2) {//SOLVER            
					for (int i = 0; i < Field.POSSIBILITIES * digit; i = i + digit) {//SOLVER
						c = sudokuLine.charAt(i);//SOLVER
						
						extendedC = sudokuLine.charAt(i + 1);//SOLVER
						
						if (c != '.' && extendedC != '.') {//SOLVER
							value = Integer.parseInt(Character.toString(c)) * 10;//SOLVER
							extendedInt = Integer.parseInt(Character.toString(extendedC));//SOLVER
							value += extendedInt;//SOLVER
							if (!trySetFieldPrivate(Structure.ROW, row, (i/digit), new Field(value,true))) {//SOLVER
	                        	board = null;//SOLVER
	                        	updateSudokuViews();//SOLVER
	                        	return false;//SOLVER
	                        }						//SOLVER
						} //SOLVER
					}//SOLVER
				}//SOLVER
	            
	            row++;//SOLVER
	        }//SOLVER
	        updateSudokuViews();//SOLVER
	        return true;//SOLVER
    	} else throw new FeatureNotSelectedException("SOLVER");
    } //SOLVER
    
    public boolean solutionHint() { //SOLVER
        //new ForcedField().trySolve(board);
        //if (true) {
        //    updateSudokuViews();
        //    return true;
        //}
    	if(Variation.SOLVER){
	        if (board.isSolved()) //SOLVER
	            return true; //SOLVER
	        try { //SOLVER
	            setBusy(true); //SOLVER
	            List solutions = solve((Board) board.clone()); //SOLVER
	            if (solutions.isEmpty()) { //SOLVER
	                setBusy(false); //SOLVER
	                return false;            //SOLVER     
	            } //SOLVER
	            for (int i = 0; i < Field.POSSIBILITIES; i++) //SOLVER
	                for (int j = 0; j < Field.POSSIBILITIES; j++) //SOLVER
	                    if (!board.getField(Structure.ROW, i, j).isSet() //SOLVER
	                            && ((Board) solutions.get(0)).getField(Structure.ROW, i, j).isSet()) { //SOLVER
	                        trySetField(Structure.ROW, i, j, //SOLVER
	                                    ((Board) solutions.get(0)).getField(Structure.ROW, i, j)); //SOLVER
	                        updateSudokuViews(); //SOLVER
	                        return true; //SOLVER
	                    } //SOLVER
	            setBusy(false); //SOLVER
	        } catch (CloneNotSupportedException e) { //SOLVER
	        	e.printStackTrace();
	        }
	        return false; //SOLVER
    	} else throw new FeatureNotSelectedException("SOLVER");
    } //SOLVER
    
    protected List solve(Board board) { //SOLVER
    	if(Variation.SOLVER){
	        List solutions = new LinkedList(); //SOLVER
	        List solvers = new LinkedList(); //SOLVER
	        solvers.add(new ForcedField()); //SOLVER
	        solvers.add(new ForcedNumber()); //SOLVER
	        Guesser guesser = new Guesser(); //SOLVER
	        for (int i = 0; i < solvers.size(); i++) //SOLVER
	            if (!((Solver) solvers.get(i)).trySolve(board)) //SOLVER
	                return solutions; //SOLVER
	        if (!board.isSolved()) { //SOLVER
	            List guessed = guesser.guess(board); //SOLVER
	            for (int i = 0; i < guessed.size(); i++) //SOLVER
	                solutions.addAll(solve(((Board)guessed.get(i)))); //SOLVER
	        } else { //SOLVER
	            solutions.add(board); //SOLVER
	        } //SOLVER
	        return solutions; //SOLVER
    	} else throw new FeatureNotSelectedException("SOLVER");
    } //SOLVER

    public void loadState(File f) throws IOException, ClassNotFoundException {//STATES
    	if(Variation.STATES){
	        ObjectInput i = new ObjectInputStream(new FileInputStream(f));//STATES
//	        board = (Board) i.readObject();//STATES
//	        history = (Stack) i.readObject();//STATES
	        updateSudokuViews();//STATES
    	} else throw new FeatureNotSelectedException("STATES");
    }//STATES

    public void saveState(File f) throws IOException {//STATES
    	if(Variation.STATES){
	        ObjectOutput o = new ObjectOutputStream(new FileOutputStream(f));//STATES
	        o.writeObject(getBoard());//STATES
	        o.writeObject(history);//STATES
	        o.close(); //STATES
    	} else throw new FeatureNotSelectedException("STATES");
    } //STATES
    
    public void undo() { //UNDO
    	if(Variation.UNDO){
	        if (!history.empty()) { //UNDO
	            board = (Board) history.pop(); //UNDO
	            updateSudokuViews(); //UNDO
	        } //UNDO
    	} else throw new FeatureNotSelectedException("UNDO");
    } //UNDO
    
}

