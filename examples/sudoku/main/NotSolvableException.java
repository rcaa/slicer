package sudoku.main;

import sudoku.schemata.Variation;

/** //BASE
 * 
 * 
 */ 
public class NotSolvableException extends Exception { //BASE
 
    /** //BASE
     * 
     */ 
    public NotSolvableException() { //BASE
    	if(Variation.BASE){ }
    } //BASE
 
    /** //BASE
     * @param msg 
     *           Error message 
     */ 
    public NotSolvableException(String msg) { //BASE
        super(msg); //BASE
        if(Variation.BASE){ }
    } //BASE
 
} //BASE
