import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class BacktrackingAVL extends AVLTree {
    // For clarity only, this is the default ctor created implicitly.
    public BacktrackingAVL() {
        super();
    }
    
    private void updateSubTreesSizes(Node node) { 
    	if(node.right==null) node.rightSubTreeSize=0;
    	else {
    		updateSubTreesSizes(node.right);
    		node.rightSubTreeSize = node.right.rightSubTreeSize+node.right.leftSubTreeSize+1;
    	}
    	if(node.left==null) node.leftSubTreeSize=0;
    	else {
    		updateSubTreesSizes(node.left);
    		node.leftSubTreeSize = node.left.rightSubTreeSize+node.left.leftSubTreeSize+1;
    	}
    	
    }

	private void updateHeights(Node node) {
		if (node.left == null & node.right == null) {
			node.updateHeight();
		} else {
			if (node.right != null & node.left != null) {
				updateHeights(node.right);
				updateHeights(node.left);
			}
			if (node.right != null) {
				updateHeights(node.right);
			}
			if (node.left != null) {
				updateHeights(node.left);
			}
			node.updateHeight();
		}
	}
    
    
	public void delete(int value) {
		boolean found = false;
		Node curr = root;
		if (curr.value == value)	//root is the requested deleted node
			root=null;
		else {
			while (!found) {
				if (curr.value == value) {
					found = true;
					Node p = curr.parent;
					if (p.value > curr.value)
						p.left = null;
					else
						p.right = null;
				}
				else {
					if (curr.value > value)
						curr = curr.left;
					else
						curr = curr.right;
				}
			}
		}
	}
    
    private void backtrackR(Node x) {
    	Node y = x.parent;
    	Node T2 = x.left;
    	if(y==root) {
    		root=x;
    		x.parent=null;
    	}
    	else {
    		x.parent=y.parent;
    		if(y.parent.value>x.value)
    			y.parent.left=x;
    		else
    			y.parent.right=x;
    	}	
    	x.left=y;  	
    	y.parent=x;
    	y.right=T2;
    	if(T2!=null) {
    		T2.parent=y;
    	}
    }
    
    private void backtrackL(Node x) {
    	Node y = x.parent;
    	Node T2 = x.right;
    	if(y==root) {
    		root=x;
    		x.parent=null;
    	}
    	else {
    		x.parent=y.parent;
    		if(y.parent.value>x.value)
    			y.parent.left=x;
    		else
    			y.parent.right=x;
    	}	
    	x.right=y;
    	y.parent=x;
    	y.left=T2;
    	if(T2!=null) {
    		T2.parent=y;
    	}
    }

	//You are to implement the function Backtrack.
	public void Backtrack() {
		if(root == null) return;
		int flag = vf.removeFirst();
		int value = vf.removeLast();
		int num = nr.removeFirst().value;
		if (flag == -1) { // last insertion was root
			root = null;
			return;
		}
		while (num > 0) {
			Node x = nr.removeLast();
			if (flag == 11) { // RR rotation
				backtrackR(x);
			}
			if (flag == 12) { // RL rotation
				backtrackL(x);
				x = nr.removeLast();
				backtrackR(x);
			}
			if (flag == 21) { // LR rotation
				backtrackR(x);
				x = nr.removeLast();
				backtrackL(x);
			}
			if (flag == 22) { // LL rotation
				backtrackL(x);
			}
			num = nr.removeFirst().value;
			flag = vf.removeFirst();
		}
		delete(value);
		updateHeights(root);
		updateSubTreesSizes(root);
	}
    
    //Change the list returned to a list of integers answering the requirements
    public static List<Integer> AVLTreeBacktrackingCounterExample() {
        int[] copyMe = {1,2,3,4,5,6};
        List<Integer> answer = new LinkedList<Integer>();
        for(int num:copyMe)
        	answer.add(num);
        return answer;
    }
    
    public int Select(int index) {
    	if(root == null) return 0;
        Node curr  = this.root;
        boolean found = false;
        while(!found) {
        	int h = curr.leftSubTreeSize+1;
        	if(h > index) //left subtree size>index
        		curr = curr.left;
        	else {
        		if(h < index) {
        			index = index-h;
        			curr = curr.right;
        		}
        		else  //curr is the requested node
        			found =true;
        	}
        }
        return curr.value;		
    }
    
    public int Rank(int value) {
    	if(root == null) return 0;
       Node curr = this.root;
       int output = 0;
       while(curr!=null) {
	      if(curr.value<value) {
	    	  output = output+curr.leftSubTreeSize+1;
	    	  curr = curr.right;
	      }
	      else
	    	  curr =curr.left;   
       }
       return output;
    }
}
