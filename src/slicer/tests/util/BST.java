package slicer.tests.util;
public class BST {
	Node root;           //A
	public  int size;    //B 			
	static class Node{
		int value;       //C
		Node left,right; //D
		Node(int value){ //E
			this.value = value;     //F
		}
	}
	public void insert(int value){
		if(root == null){           //G
			root = new Node(value); //H
			size++;				    //I
			return;				    //J
		}
		Node current = root;        //L
		boolean z = true;
		while(z){                //M
			if(value < current.value){    //N
				if(current.left == null){ //O
					current.left = new Node(value);  //P
					break;                           //Q	  
				}else{
					current = current.left;          //R
				}
			}else if (value >= current.value){       //S
				if(current.right == null){           //T
					current.right = new Node(value); //U
					break;					         //V
				}else{
					current = current.right;         //X
				}
			}else {
				return;                              //Z
			}
		}
		size++;                                      //A1
	}
	//[BST.A1, BST.F, BST.T, BST.S, B8, BST.V, BST.I, A7, BST.G, BST.H, B7] sem filtro
	//[BST.A1, BST.T, BST.S, BST.V, A7] - com filtro
/*	[
 * slicer/tests/SergioTests:253,
 * slicer/tests/SergioTests:252,
 * slicer/tests/SergioTests:251,
 * slicer/tests/SergioTests:281
 * slicer/tests/util/BST:9,  --- F
 * slicer/tests/util/BST:13, --- G
 * slicer/tests/util/BST:15, --- I
 * slicer/tests/util/BST:14, --- H
 * slicer/tests/util/BST:27, --- S
 * slicer/tests/util/BST:28, --- T
 * slicer/tests/util/BST:38, --- A1
 * slicer/tests/util/BST:30, --- V
 * ] */
}