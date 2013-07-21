package sudoku.main;
import java.util.Map; 
import java.util.HashMap; 
import java.util.Iterator; 
import java.util.SortedSet; 
import java.util.TreeSet; 

import sudoku.schemata.FeatureNotSelectedException;
import sudoku.schemata.Variation;

/** //BASE
 * <code>AbstractEnum</code> models <a 
 * href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/Enum.html">the JDK5
 * <code>enum</code> feature</a>. 
 * <p/> /
 * Extending classes should follow this example: 
 * 
 * <pre> 
 * public class Suit extends AbstractEnum {
 * 	public static final Suit CLUBS = new Suit(&quot;CLUBS&quot;); 
 * 	public static final Suit DIAMONDS = new Suit(&quot;DIAMONDS&quot;); 
 * 	public static final Suit HEARTS = new Suit(&quot;HEARTS&quot;); 
 * 	public static final Suit SPADES = new Suit(&quot;SPADES&quot;); 
 * 
 * 	public static Suit[] values() { 
 * 		return (Suit[]) values0(Suit.class, new Suit[count(Suit.class)]); 
 * 	}
 * 
 * 	public static Suit valueOf(final String name) { 
 * 		return (Suit) valueOf0(Suit.class, name); //BASE
 * 	} 
 * 
 * 	private Suit(final String name) { 
 * 		super(name); 
 * 	} 
 * } 
 * </pre>
 * @author <a href="mailto:binkley@alumni.rice.edu">B. K. Oxley (binkley)</a> 
 * @version $Id$ 
 * @since Dec 14, 2005 6:32:23 PM 
 */ 
public abstract class AbstractEnum implements Comparable { //BASE
    private static final Map ALL_VALUES_BY_NAME = new HashMap(4); //BASE
    private static final Map ALL_VALUES_BY_ORDINAL = new HashMap(4); //BASE
    private static final Map ALL_COUNTS = new HashMap(4); //BASE

    /** //BASE
     * The ordinal of this enumeration constant (its position in the enum 
     * declaration, where the initial constant is assigned an ordinal of zero). 
     * <p/> 
     * Most programmers will have no use for this field. It is designed for use 
     * by sophisticated enum-based data structures. 
     */ 
    private final int ordinal = createNextOrdinal(getClass()); //BASE

    /** //BASE
     * The name of this enum constant, as declared in the enum declaration. Most
     * programmers should use the {@link #toString} method rather than accessing 
     * this field. 
     */ 
    private final String name; //BASE
 
    /** //BASE
     * Gets all enum values of type <var>clazz</var> into <var>array</var>. Use
     * {@link #count(Class)} to size the array parameter.
     * <p/>
     * Concrete subclasses <em>should</em> provide <code>values()</code> which
     * follows the example:
     * 	<pre> 
     * 		public static Suit[] values() {
     * 		return (Suit[]) valuesOf(Suit.class, new Suit[count(Suit.class)]);
     * }
     * </pre>
     * 
     * @param clazz
     *            the enum type
     * @param array
     *            the array
     * 
     * @return the enum values
     * 
     * @throws ClassCastException
     *             if <var>clazz</var> is not an enum class
     */
    protected static AbstractEnum[] values0(final Class clazz, //BASE
                                            final AbstractEnum[] array) { //BASE
        if(Variation.BASE){
	    	checkClassIsEnum(clazz); //BASE
	        int i = 0; //BASE
	        for (final Iterator valueIt = getValuesByOrdinal(clazz).iterator(); valueIt //BASE
	                .hasNext(); ++i) //BASE
	            array[i] = (AbstractEnum) valueIt.next(); //BASE
	        return array; //BASE
        } else throw new FeatureNotSelectedException("BASE");
    } //BASE

    /** //BASE
     * Gets the count of enums of type <var>clazz</var>, zero (0) if none.
     * 
     * @param clazz
     *            the enum type
     * 
     * @return the enum count
     * 
     * @throws ClassCastException
     *             if <var>clazz</var> is not an enum class
     */
    protected static int count(final Class clazz) { //BASE
    	if(Variation.BASE){
	        checkClassIsEnum(clazz); //BASE
	        return ALL_COUNTS.containsKey(clazz) ? ((Integer) ALL_COUNTS.get(clazz)) //BASE
	               .intValue() //BASE
	               : 0; //BASE
    	} else throw new FeatureNotSelectedException("BASE");
    } //BASE
 
    /**
     * Gets an enum with the given <var>name</var>.
     * <p/>
     * Concrete subclasses <em>should</em> provide <code>valuesOf(String)</code>
     * which follows the example:
     * 	<pre> //BASE
     * 		public static Suit valueOf(final String name) {
     * 			return (Suit) valueOf(Suit.class, name);
     * 		}
     * </pre>
     * 
     * @param clazz
     *            the enum class
     * @param name
     *            the enum name
     * 
     * @return the corresponding enum value
     * 
     * @throws ClassCastException
     *             if <var>clazz</var> is not an enum class
     * @throws IllegalArgumentException
     *             if <var>name</var> is not an enum name
     */ //BASE
    protected static AbstractEnum valueOf0(final Class clazz, final String name) //BASE
    throws IllegalArgumentException { //BASE
    	if(Variation.BASE){
	        checkClassIsEnum(clazz); //BASE
	        final Map values = getValuesByName(clazz); //BASE
	        if (values.containsKey(name)) //BASE
	            return (AbstractEnum) values.get(name); //BASE
	        throw new IllegalArgumentException(name); //BASE
    	} else throw new FeatureNotSelectedException("BASE");
    } //BASE

    /**
     * Constructs a new <code>AbstractEnum</code> with the given
     * <var>name</var>.
     * 
     * @param name 
     *            the name of this enum constant, which is the identifier used
     *            to declare it
     */
    protected AbstractEnum(final String name) { //BASE
    	if(Variation.BASE){
	        this.name = name; //BASE
	        getValuesByName(getClass()).put(name, this); //BASE
	        getValuesByOrdinal(getClass()).add(this); //BASE
    	} else throw new FeatureNotSelectedException("BASE");
    } //BASE

    /** //BASE
     * Returns the name of this enum constant, exactly as declared in its enum declaration.
     * <p/>
     * <b>Most programmers should use the {@link #toString} method in preference
     * to this one, as the toString method may return a more user-friendly
     * name.</b> This method is designed primarily for use in specialized
     * situations where correctness depends on getting the exact name, which
     * will not vary from release to release.
     * 
     * @return the name of this enum constant
     */
    public String name() { //BASE
    	if(Variation.BASE){
    		return name; //BASE
    	} else throw new FeatureNotSelectedException("BASE");
    } //BASE
 
    /** //BASE
     * Returns the ordinal of this enumeration constant (its position in its
     * enum declaration, where the initial constant is assigned an ordinal of
     * zero).
     * <p/>
     * Most programmers will have no use for this method. It is designed for use
     * by sophisticated enum-based data structures.
     * 
     * @return the ordinal of this enumeration constant
     */
    public int ordinal() { //BASE
    	if(Variation.BASE){
    		return ordinal; //BASE
    	} else throw new FeatureNotSelectedException("BASE");
    } //BASE
    
    // Object //BASE

    /** //BASE
     * Throws CloneNotSupportedException. This guarantees that enums are never 
     * cloned, which is necessary to preserve their "singleton" status. 
     * 
     * @return (never returns) 
     * 
     * @throws CloneNotSupportedException 
     *             if called 
     */ 
    protected Object clone() throws CloneNotSupportedException { //BASE
    	if(Variation.BASE){
    		throw new CloneNotSupportedException(); //BASE
    	} else throw new FeatureNotSelectedException("BASE");
    } //BASE
 
    /** //BASE
     * Returns the name of this enum constant, as contained in the declaration. 
     * This method may be overridden, though it typically isn't necessary or 
     * desirable. An enum type should override this method when a more 
     * "programmer-friendly" string form exists. 
     * 
     * @return the name of this enum constant 
     */ 
    public String toString() { //BASE
    	if(Variation.BASE){
    		return name; //BASE
    	} else throw new FeatureNotSelectedException("BASE");
    } //BASE
 
    // Comparable //BASE
 
    /** //BASE
     * Compares this enum with the specified object for order. Returns a 
     * negative integer, zero, or a positive integer as this object is less 
     * than, equal to, or greater than the specified object. 
     * <p/> 
     * Enum constants are only comparable to other enum constants of the same 
     * enum type. The natural order implemented by this method is the order in 
     * which the constants are declared. 
     */ 
    public int compareTo(final Object o) { //BASE
    	if(Variation.BASE){
	        if (!getClass().equals(o.getClass())) //BASE
	            throw new ClassCastException(o.getClass().toString()); //BASE
	        return ordinal - ((AbstractEnum) o).ordinal; //BASE
    	} else throw new FeatureNotSelectedException("BASE");
    } //BASE
 
    private static Map getValuesByName(final Class clazz) { //BASE
    	if(Variation.BASE){
	        final Map values; //BASE
	         if (ALL_VALUES_BY_NAME.containsKey(clazz)) //BASE
	            values = (Map) ALL_VALUES_BY_NAME.get(clazz); //BASE
	        else //BASE
	            ALL_VALUES_BY_NAME.put(clazz, values = new HashMap(8)); //BASE
	         return values; //BASE
    	} else throw new FeatureNotSelectedException("BASE");
    } //BASE
 
    private static SortedSet getValuesByOrdinal(final Class clazz) { //BASE
    	if(Variation.BASE){
	       final SortedSet values; //BASE
	       if (ALL_VALUES_BY_ORDINAL.containsKey(clazz)) //BASE
	            values = (SortedSet) ALL_VALUES_BY_ORDINAL.get(clazz); //BASE
	        else //BASE
	            ALL_VALUES_BY_ORDINAL.put(clazz, values = new TreeSet()); //BASE
	         return values; //BASE
    	} else throw new FeatureNotSelectedException("BASE");
    } //BASE
 
    private static int createNextOrdinal(final Class clazz) { //BASE
    	if(Variation.BASE){
	        final int count = count(clazz); //BASE
	         ALL_COUNTS.put(clazz, new Integer(count + 1)); //BASE
	         return count; //BASE
    	} else throw new FeatureNotSelectedException("BASE");
    } //BASE
 
    private static void checkClassIsEnum(final Class clazz) { //BASE
    	if(Variation.BASE){
	        if (!AbstractEnum.class.isAssignableFrom(clazz)) //BASE
	            throw new ClassCastException(clazz.toString()); //BASE
    	} else throw new FeatureNotSelectedException("BASE");
    } //BASE
} //BASE
