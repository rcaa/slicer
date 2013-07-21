package sudoku.main;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import sudoku.schemata.FeatureNotSelectedException;
import sudoku.schemata.Variation;

/** //BASE
 * Stores all possible numbers that can still be set on a sudoku field. A Field
 * can be set to final, only one possibility is left after that.
 * 
 * Attention! There is no 0 in sudoku. All values are stored n-1, that means the
 * possibility 1 is stored as 0 in a Bitset. getFinal() can return 0 if no
 * possibility is left, but if that happens the sudoku cannot be solved.
 * getValue(0) is invalid!
 * 
 */ 
public class Field /*STATES*/implements Cloneable, Serializable /*STATES*/ { 
    
	/** //BASE
     * Numbers of sudoku 
     */ 
    public static int POSSIBILITIES = 9; //BASE
    protected boolean set; //BASE
    protected boolean initialSet; //BASE
    protected int value; //BASE
    protected List remainingPos; //SOLVER
    protected String color; //COLOR
    
    /**
     * //BASE
     * Fill the remainingPos list.
     */ 
    public Field() {
    	if(Variation.BASE){
	        this.initialSet = false; //BASE
	        this.set = false; //BASE
	        
	        if(Variation.COLOR){
	        	this.color = "";
	        } 
	        
	        if(Variation.SOLVER){
		        remainingPos = new LinkedList();//SOLVER
		        for (int i = 1; i <= POSSIBILITIES; i++) {//SOLVER
		            remainingPos.add((Object) i);//SOLVER
		        }//SOLVER
	        } 
    	} else throw new FeatureNotSelectedException("BASE");
    } 
        
    /**
     * //BASE
     *  
     */
    public Field(int value, boolean initialSet) {
    	if(Variation.BASE){
	        this.value = value; //BASE
	        this.set = true; //BASE
	        this.initialSet = initialSet; //BASE
	        
	        if(Variation.COLOR){
	        	this.color = "";
	        } 
	        
	        if(Variation.SOLVER){
	        	remainingPos = new LinkedList();//SOLVER
	        } 
    	} else throw new FeatureNotSelectedException("BASE");
    }
    
    public Field(int value) { 
    	if(Variation.BASE){
	        this.value = value; //BASE
	        this.set = true; //BASE
	        this.initialSet = false; //BASE
	        
	        if(Variation.COLOR){
	        	this.color = "";
	        } 
	        
	        if(Variation.SOLVER){
	        	remainingPos = new LinkedList();//SOLVER
	        } 
    	} else throw new FeatureNotSelectedException("BASE");
    } 
    
    /** //BASE
     * 
     * @return 
     */ 
    public int getValue() { //BASE
    	if(Variation.BASE){
    		return value; //BASE
    	} else throw new FeatureNotSelectedException("BASE");
    } //BASE
    
    /** //BASE
     * 
     * @return 
     */ 
    public boolean isInitialSet() { //BASE
    	if(Variation.BASE){
    		return initialSet; //BASE
    	} else throw new FeatureNotSelectedException("BASE");
    } //BASE
    
    /** //BASE
     * 
     * @return 
     */ 
    public boolean isSet() { //BASE
        if(Variation.BASE){
        	return set; //BASE
        } else throw new FeatureNotSelectedException("BASE");
    } //BASE
    
    public void setInitial(boolean flag){ //GENERATOR
    	if(Variation.BASE){
    		if(Variation.GENERATOR){
    			initialSet = flag; //GENERATOR
    		} 
    	} else throw new FeatureNotSelectedException("BASE");
    } //GENERATOR
    
    public Field(List remainingPos) {//SOLVER
    	this();//SOLVER
    	if(Variation.BASE){
    		if(Variation.SOLVER){
		    	this.remainingPos = remainingPos;//SOLVER
    		} 
    	} else throw new FeatureNotSelectedException("BASE");
    }//SOLVER

    /**
     *
     * @return
     */
    public List getRemainingPos() {//SOLVER
    	if(Variation.BASE){
    		if(Variation.SOLVER){
    			return remainingPos;//SOLVER
    		} else throw new FeatureNotSelectedException("SOLVER");
    	} else throw new FeatureNotSelectedException("BASE");
    }//SOLVER

    // fürs Testen
    public String toString() {//SOLVER
    	if(Variation.BASE){
    		if(Variation.SOLVER){
		    	String output = "";//SOLVER
		        if (remainingPos.isEmpty()) {//SOLVER
		            output = "[" + value + "]";//SOLVER
		        } else {//SOLVER
		            output = "{";//SOLVER
		            for (int i = 0; i < remainingPos.size(); i++) {//SOLVER
		                output += remainingPos.get(i).toString();//SOLVER
		            }//SOLVER
		            output += "}";//SOLVER
		        }//SOLVER
		        return output;//SOLVER
    		} else throw new FeatureNotSelectedException("SOLVER");
    	} else throw new FeatureNotSelectedException("BASE");
    }//SOLVER

    public Object clone() throws CloneNotSupportedException {//SOLVER
    	if(Variation.BASE){
    		if(Variation.SOLVER){
		        Field clone = (Field) stateClone();//SOLVER //watch out, i can't find the clone method in base fetaure 
		        LinkedList remainingPosClone = new LinkedList();//SOLVER
		        for (int i = 0; i < remainingPos.size(); i++) {//SOLVER
		            remainingPosClone.add(new Integer(((Integer)remainingPos.get(i)).intValue()));//SOLVER
		        }//SOLVER
		        clone.remainingPos = remainingPosClone;//SOLVER
		        return clone;//SOLVER
    		} else throw new FeatureNotSelectedException("SOLVER");
    	} else throw new FeatureNotSelectedException("BASE");
    }//SOLVER
    
    public Object stateClone() throws CloneNotSupportedException { //STATES
    	if(Variation.BASE){
    		if(Variation.STATES){
		        Field clone = new Field(); //STATES
		        clone.initialSet = initialSet; //STATES
		        clone.set = set; //STATES
		        clone.value = value; //STATES
		        return clone; //STATES
    		} else throw new FeatureNotSelectedException("SOLVER");
    	} else throw new FeatureNotSelectedException("BASE");
    }

    private void writeObject(ObjectOutputStream aOutputStream) throws IOException { //STATES
    	if(Variation.BASE){
    		if(Variation.STATES){
		    	aOutputStream.writeBoolean(set); //STATES
		        aOutputStream.writeBoolean(initialSet); //STATES
		        aOutputStream.writeInt(value); //STATES
		        aOutputStream.defaultWriteObject(); //STATES
    		} 
    	} else throw new FeatureNotSelectedException("BASE");
    }

    private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException { //STATES
    	if(Variation.BASE){
    		if(Variation.STATES){
		    	aInputStream.defaultReadObject(); //STATES
		        set = aInputStream.readBoolean(); //STATES
		        initialSet = aInputStream.readBoolean(); //STATES
		        value = aInputStream.readInt(); //STATES
    		} 
    	} else throw new FeatureNotSelectedException("BASE");
    } //STATES
    
}
