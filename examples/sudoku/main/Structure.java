package sudoku.main;

import sudoku.schemata.FeatureNotSelectedException;
import sudoku.schemata.Variation;

/** //BASE
 * 
 * 
 */
public class Structure extends AbstractEnum { //BASE
    public static final Structure ROW = new Structure("ROW"); //BASE
    public static final Structure COL = new Structure("COL"); //BASE
    public static final Structure BOX = new Structure("BOX"); //BASE

    public static Structure[] values() { //BASE
    	if(Variation.BASE){
    		return (Structure[]) values0(Structure.class, //BASE
                                     	 new Structure[count(Structure.class)]); //BASE
    	} else throw new FeatureNotSelectedException("BASE");
    } //BASE

    public static Structure valueOf(final String name) { //BASE
    	if(Variation.BASE){
    		return (Structure) valueOf0(Structure.class, name); //BASE
    	} else throw new FeatureNotSelectedException("BASE");
    } //BASE

    private Structure(final String name) { //BASE
        super(name); //BASE
        if(Variation.BASE){ }
    } //BASE
} //BASE
