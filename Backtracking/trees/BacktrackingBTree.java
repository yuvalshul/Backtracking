import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

//import BTree.Node;



public class BacktrackingBTree<T extends Comparable<T>> extends BTree<T> {
	// For clarity only, this is the default ctor created implicitly.
	public BacktrackingBTree() {
		super();
	}

	public BacktrackingBTree(int order) {
		super(order);
	}

	//You are to implement the function Backtrack.
	public void Backtrack() {  
		if(root == null) return;
		int flag = vf.removeFirst();
		int value = vf.removeLast();
		Node<T> cnt = cn.removeLast();
		if(flag==-1) {
			root = null;
			size=0;
			return;
		}
		
		Node<T> insertedTo = cn.removeFirst();
		
		while (cnt.parent == null) {
			if (flag == 10) { // split has occured
				Node<T> splitted = cn.removeFirst();
				int midIndex = splitted.numOfKeys / 2;
				int midValue = (Integer) splitted.keys[midIndex];

				Node<T> parent = splitted.parent;
				int indexOfMidValueParent = -1;
				for (int i = 0; i < parent.numOfKeys & indexOfMidValueParent == -1; i++)
					if ((Integer) parent.getKey(i) == midValue)
						indexOfMidValueParent = i;

				Node<T> splittedTo1 = parent.getChild(indexOfMidValueParent);
				Node<T> splittedTo2 = parent.getChild(indexOfMidValueParent + 1);
				parent.removeKey(indexOfMidValueParent); // remove the midValue from parent
				parent.removeChild(splittedTo1);
				parent.removeChild(splittedTo2);
				parent.addChild(splitted);
			}
			
			else	//flag == -2
				root = (Node<T>) cn.removeFirst();
			cnt = cn.removeLast();
			if(cnt.parent==null) flag = vf.removeFirst();
		}
		
		
		
		
		
		int indexOfValueInInsertedTo = -1; 
		for(int i = 0; i<insertedTo.numOfKeys&indexOfValueInInsertedTo==-1;i++)
			if((Integer)insertedTo.getKey(i)==value)
				indexOfValueInInsertedTo=i;
		insertedTo.removeKey(indexOfValueInInsertedTo);
	}
	
	
	//Change the list returned to a list of integers answering the requirements
	public static List<Integer> BTreeBacktrackingCounterExample(){
		List<Integer> answer = new LinkedList<Integer>();
		int[] copyMe = {1,60,150,200,250,300,450,500,550,600,650,700,750,800,850,900,61,62,201,202,451,452,601,602,453};
		for(int num:copyMe) 
			answer.add(num);
		return answer;
		
	}
	
}