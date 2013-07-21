package sudoku.main;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import sudoku.schemata.FeatureNotSelectedException;
import sudoku.schemata.Variation;

/** //BASE
 * Implementation of a State
 * 
 */
public class Board /*STATES*/implements Cloneable, Serializable /*STATES*/{  
	
	/** //BASE
	 * Number of elements of the Sudoku //BASE
	 */ 
	public static int ELEMENTS = Field.POSSIBILITIES * Field.POSSIBILITIES; //BASE
		
	protected Field[] board; //BASE
	
	
	/** //BASE
	 * 
	 */	
	public Board() { //BASE
		if(Variation.BASE){
			this.board = new Field[ELEMENTS]; //BASE
			for (int i = 0; i < ELEMENTS; i++) { //BASE
				this.board[i] = new Field(); //BASE
			} //BASE
		} else throw new FeatureNotSelectedException("BASE");
	} //BASE
	
	/** //BASE
	 * Resolves field addresses. 
	 * 
	 * @param struct 
	 * @param structNr 
	 * @param element 
	 * @return 
	 */ 
	public Field getField(Structure struct, int structNr, int element) { //BASE
		if(Variation.BASE){
			return board[getIndex(struct, structNr, element)]; //BASE
		} else throw new FeatureNotSelectedException("BASE");
	} //BASE
	
	protected int getIndex(Structure str, int nr, int ele) { //BASE
		if(Variation.BASE){
			int sqrt = (int) Math.round(Math.sqrt(Field.POSSIBILITIES)); //BASE
			//BASE
			if (str.name().equals("COL")) //BASE
				return nr + (ele * Field.POSSIBILITIES); //BASE
			else if (str.name().equals("ROW")) //BASE
				return (nr * Field.POSSIBILITIES) + ele; //BASE
			else if (str.name().equals("BOX")) //BASE
				return Field.POSSIBILITIES * (nr / sqrt * sqrt + ele / sqrt) + (nr % sqrt * sqrt + ele % sqrt); //BASE
			else //BASE
				return -1; //BASE
		} else throw new FeatureNotSelectedException("BASE");
	} //BASE
	
	public void setField(Structure structure, int structNr, int element, Field f) { //BASE
		if(Variation.BASE){
			board[getIndex(structure, structNr, element)] = f; //BASE
		} else throw new FeatureNotSelectedException("BASE");
	} //BASE

	public void removeRandomSetField(){//GENARATOR
		if(Variation.BASE){
			if(Variation.GENERATOR){
				Random r = new Random(999);//GENARATOR
				int size = Field.POSSIBILITIES*Field.POSSIBILITIES; //GENARATOR
				int rIndex = r.nextInt(size); //GENARATOR
				int counter = 0; //GENARATOR
				while( (board[rIndex].value <= 0) && counter < size){ //GENARATOR
					rIndex = ((rIndex + counter) % size); //GENARATOR
					counter++; //GENARATOR
				}

				//rIndex known
				//recreate field
		
				Board output = new Board(); //GENARATOR
		
				for(int i=0;i<Field.POSSIBILITIES;i++){ //GENARATOR
					for(int j=0;j<Field.POSSIBILITIES;j++){ //GENARATOR
						if(getIndex(Structure.ROW, i, j) != rIndex){ //GENARATOR
							Field f = getField(Structure.ROW, i, j); //GENARATOR
							if(f.isSet()) //GENARATOR
								output.trySetField(Structure.ROW, i, j, new Field(f.getValue())); //GENARATOR
						} //GENARATOR
					} //GENARATOR
				}
				board = output.board;
			} 
		} else throw new FeatureNotSelectedException("BASE");

	}      

	public boolean isSolved() { //SOLVER
		if(Variation.BASE){
			if(Variation.SOLVER){
				for (int i = 0; i < board.length; i++) //SOLVER
					if (!board[i].isSet()) //SOLVER
						return false; //SOLVER
				return true; //SOLVER
			} else throw new FeatureNotSelectedException("SOLVER");
		} else throw new FeatureNotSelectedException("BASE");
	} //SOLVER

	public boolean trySetField(Structure str, int strIndex, int element, Field f) { //SOLVER
		if(Variation.BASE){
			if(Variation.SOLVER){
				boolean validRemoveAction = removeValueFromStructures(getIndex(str, strIndex, element), f.getValue()); //SOLVER
				if (validRemoveAction && getField(str, strIndex, element).getRemainingPos().contains((Object) f.getValue())) { //SOLVER
					setField(str, strIndex, element, f); //SOLVER
					return true; //SOLVER
				} else //SOLVER
					return false; //SOLVER
			} else throw new FeatureNotSelectedException("SOLVER");
		} else throw new FeatureNotSelectedException("BASE");
	} //SOLVER

	protected boolean removeValueFromStructures(int index, int value) { //SOLVER
		if(Variation.BASE){
			if(Variation.SOLVER){
				List relatedFieldIndices = getRelatedFieldIndices(index); //SOLVER
				for (int i = 0; i < relatedFieldIndices.size(); i++) { //SOLVER
					if (!board[(Integer) relatedFieldIndices.get(i)].isSet()) { //SOLVER
						List remainingPos = board[(Integer) relatedFieldIndices.get(i)].getRemainingPos();  //SOLVER
						if (remainingPos.contains(value) && remainingPos.size() <= 1) //SOLVER
							return false; //SOLVER
						remainingPos.remove((Object)value); //SOLVER
						board[(Integer) relatedFieldIndices.get(i)] = new Field(remainingPos); //SOLVER
					} //SOLVER
				} //SOLVER
				return true; //SOLVER
			} else throw new FeatureNotSelectedException("SOLVER");
		} else throw new FeatureNotSelectedException("BASE");
	} //SOLVER

	protected int getStructureIndex(int index, Structure str) { //SOLVER
		if(Variation.BASE){
			if(Variation.SOLVER){
				int sqrt = (int) Math.round(Math.sqrt(Field.POSSIBILITIES)); //SOLVER
				if (str.name().equals("ROW")) //SOLVER
					return index / Field.POSSIBILITIES; //SOLVER
				else if (str.name().equals("COL")) //SOLVER
					return index % Field.POSSIBILITIES; //SOLVER
				else if (str.name().equals("BOX")) //SOLVER
					return sqrt * (index / (sqrt * Field.POSSIBILITIES)) + (index % Field.POSSIBILITIES) / sqrt; //SOLVER
				else //SOLVER
					return -1; //SOLVER
			} else throw new FeatureNotSelectedException("SOLVER");
		} else throw new FeatureNotSelectedException("BASE");
	} //SOLVER

	protected List getRelatedFieldIndices(int index) {  //SOLVER
		if(Variation.BASE){
			if(Variation.SOLVER){
		        List indices = new LinkedList(); //SOLVER
		        Structure str; //SOLVER
		        int strIndex; //SOLVER
		        int indexProcessing; //SOLVER
		        for (int i = 0; i < Structure.values().length; i++) { //SOLVER
		            str = Structure.values()[i]; //SOLVER
		            strIndex = getStructureIndex(index, str); //SOLVER
		            for (int j = 0; j < Field.POSSIBILITIES; j++) { //SOLVER
		                indexProcessing = getIndex(str, strIndex, j); //SOLVER
		                if (! (indices.contains(indexProcessing) || indexProcessing == index)) { //SOLVER
		                    indices.add(indexProcessing); //SOLVER
		                } //SOLVER
		            } //SOLVER
		        } //SOLVER
		        return indices; //SOLVER
			} else throw new FeatureNotSelectedException("SOLVER");
		} else throw new FeatureNotSelectedException("BASE");
    } //SOLVER
	
	 public Object clone() throws CloneNotSupportedException { //STATES
		 if(Variation.BASE){
			 if(Variation.STATES){
				 Board clone = new Board(); //STATES
				 for (int i = 0; i < board.length; i++) { //STATES
					 clone.board[i] = (Field) board[i].clone(); //STATES
				 } //STATES
				 return clone; //STATES
			 } else throw new FeatureNotSelectedException("STATES");
		 } else throw new FeatureNotSelectedException("BASE");
	    }

	    private void writeObject(ObjectOutputStream aOutputStream) throws IOException { //STATES
	    	if(Variation.BASE){
				 if(Variation.STATES){
					 aOutputStream.writeObject(board); //STATES
					 aOutputStream.defaultWriteObject(); //STATES
				 } 
			 } else throw new FeatureNotSelectedException("BASE");
	    } //STATES

	    private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException { //STATES
	    	if(Variation.BASE){
				 if(Variation.STATES){
					 aInputStream.defaultReadObject(); //STATES
					 board = (Field[]) aInputStream.readObject(); //STATES
				 } 
			 } else throw new FeatureNotSelectedException("BASE");
	    } //STATES

}
