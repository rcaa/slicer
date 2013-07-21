package slicer.util;

import java.io.Serializable;
import java.util.Collection;

import slicer.Set;

public class SplSets implements Serializable{
	private static final long serialVersionUID = 2381869353171310177L;
	public Collection<Set> sets;
	public String product;
	
	public SplSets(Collection<Set> sets, String product) {
		if(sets == null || product == null || sets.isEmpty()){
			throw new RuntimeException("Check the slices: " + this.sets +" or check the product: " + product + " passed to serialization");
		}
		this.sets = sets;
		this.product = product;
	}
	
}
