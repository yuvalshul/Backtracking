
import java.util.NoSuchElementException;

public class BacktrackingBST implements Backtrack, ADTSet<BacktrackingBST.Node> {
	private Stack stack;
	private Stack redoStack;
	private BacktrackingBST.Node root = null;

	// Do not change the constructor's signature
	public BacktrackingBST(Stack stack, Stack redoStack) {
		this.stack = stack;
		this.redoStack = redoStack;
	}

	public Node getRoot() {
		if (root == null) {
			throw new NoSuchElementException("empty tree has no root");
		}
		return root;
	}

	public Node search(int k) {
		BacktrackingBST.Node curr = root;
		while (curr != null) {
			if (k == curr.getKey())
				return curr;
			if (k > curr.getKey())
				curr = curr.right;
			else
				curr = curr.left;
		}
		return null;
	}

	public void insert(Node node) {
		if (root == null)
			root = node;
		else {
			BacktrackingBST.Node curr = root;
			BacktrackingBST.Node parent = null;
			while (curr != null) {
				if (node.getKey() > curr.getKey()) {
					parent =curr;
					curr = curr.right;
				}
				else {
					parent =curr;
					curr = curr.left;
				}
			}
			if (node.getKey() > parent.getKey()) {
				parent.right=node;
				stack.push(new Node(1,null));
			}
			else {
				parent.left=node;
				stack.push(new Node(-1,null));
			}
			node.parent=parent;
		}
		stack.push(node.parent);
		stack.push(null);
		
	}


	public Node minimum() {
		if(root==null) throw new IllegalArgumentException();
		BacktrackingBST.Node curr = root;
		while (curr.left != null)
			curr = curr.left;
		return curr;
	}

	public Node maximum() {
		if(root==null) throw new IllegalArgumentException();
		BacktrackingBST.Node curr = root;
		while (curr.right != null)
			curr = curr.right;
		return curr;
	}

	public Node successor(Node node) {
		BacktrackingBST.Node curr = search(node.getKey());
		if (curr == null)
			throw new IllegalArgumentException();
		if (curr.right == null) {
			while(curr.parent.right==curr) {   //curr is the right son of his parent
				curr = curr.parent;
				if(curr.parent==null&&curr.right.key>curr.key) //node has no successor
					throw new IllegalArgumentException();
			}
			return curr.parent;

		}
		curr = curr.right;
		while (curr.left != null)
			curr = curr.left;
		return curr;

	}

	public Node predecessor(Node node) {
		BacktrackingBST.Node curr = search(node.getKey());
		if (curr == null)
			throw new IllegalArgumentException();
		if (curr.left == null) {
			if(curr.key==root.key)
				return null;
			while(curr.parent.getKey()>node.getKey()) {
				if(curr.parent.key==root.key&&curr.getKey()>node.getKey())
					throw new IllegalArgumentException();
				curr=curr.parent;
			}
			return curr.parent;
		}
		curr = curr.left;
		while (curr.right != null)
			curr = curr.right;
		return curr;
	}


	@Override
	public void delete(Node node) {
		if(node ==null||search(node.key)==null) return;
		if(node == root) {
			deleteRoot();
			return;
		}
		Node parent = node.parent;
		boolean rightSon =false;
		if(node.key>node.parent.key) rightSon = true;
		if(node.left==null&node.right==null) { //node is a leaf
			if(rightSon) parent.right=null;
			else parent.left=null;
			stack.push(node);
			stack.push(new Node(0,null)); 
			return;
		}
		if(node.left==null) { //node has a right son only
			node.right.parent=node.parent;
			if(rightSon) 
				parent.right=node.right;
			else 
				parent.left=node.right;
			stack.push(parent);
			stack.push(node);
			stack.push(new Node(1,null));
			return;
		}
		if(node.right==null) { //node has a left son only
			node.left.parent=node.parent;
			if(rightSon) 
				parent.right=node.left;
			else 
				parent.left=node.left;
			stack.push(parent);
			stack.push(node);
			stack.push(new Node(1,null));
			return;
		}
		// node has 2 sons
		Node succ = successor(node);
		if(succ.right!=null)stack.push(new Node(1,null));
		else stack.push(new Node(0,null));
		delete(succ);
		if(succ.right==null&succ.left==null)
			stack.push(succ.parent);
		if(rightSon) parent.right=succ;
		else parent.left=succ;
		succ.parent=parent;
		succ.right=node.right;
		succ.left = node.left;
		if(node.right!=null)
			node.right.parent=succ;
		if(node.left!=null)
			node.left.parent=succ;
		stack.push(succ);
		stack.push(node);
		stack.push(new Node(2,null));
	}
	
	
	
	public void backtrack() {
		if(stack.isEmpty()) return;
		Node flag =(Node)stack.pop();
		if (flag==null) {  //last function was insert
			Node parent = (Node)stack.pop();
			if(((Node)stack.pop()).key==1) {
				redoStack.push(parent.right);
				parent.right=null;
			}
			else {
				redoStack.push(parent.left);
				parent.left=null;
			}
			redoStack.push(new Node(1,null));
			return;
		} 
		//last function was delete
		if(flag.key==3) { //deleted was a root
			Node oldRoot = (Node)stack.pop();
			redoStack.push(oldRoot);
			redoStack.push(new Node(0,null));
			if(root==null) {
				root=oldRoot;
				return;
			}
			if(root.right==null) {
				root.parent=root;
				root =oldRoot;
				return;
			}
			Node sp = (Node)stack.pop();
			if(((Node)stack.pop()).key==0) {
				stack.pop();
				stack.pop();
			}
			else {
				stack.pop();
				stack.pop();
				stack.pop();
			}
			root.right.parent=oldRoot;
			root.right=sp.left;
			sp.left=root;
			root.parent=sp;
			if(root.left!=null)
				root.left.parent=oldRoot;
			root.left=null;
			root=oldRoot;
			return;
		}
		 
		if(flag.key==0) { //deleted was a leaf
			Node insertMe = (Node)stack.pop();
			if(insertMe.key>insertMe.parent.key)
				insertMe.parent.right=insertMe;
			else
				insertMe.parent.left=insertMe;
			redoStack.push(insertMe);
			redoStack.push(new Node(0,null));
			return;	
		}
		if (flag.key==1) { //deleted node had 1 son 
			Node node = (Node)stack.pop();
			redoStack.push(node);
			redoStack.push(new Node(0,null));
			Node parent = (Node)stack.pop();
			node.parent=parent;
			Node child;
			
			if(node.key>parent.key) {
				child = parent.right;
				parent.right=node;
			}
			else { //node is a left son of his parent
				child = parent.left;
				parent.left=node;
			}
			child.parent=node;
			if(child.key>node.key) 
				node.right=child;
			else 
				node.left=child;
			return;
		}
		// deleted node had 2 sons
		Node node = (Node)stack.pop();
		Node replaceMe = (Node)stack.pop();
		redoStack.push(node);
		redoStack.push(new Node(0,null));
		replaceMe.left=null;
		Node parent = replaceMe.parent;
		if(parent==null) { //deleted was the root
			
		}
		if(node.key>parent.key)
			parent.right=node;
		else
			parent.left=node;
		Node sp =(Node) stack.pop();
		if(sp.key==1) { //succ had 1 son
			stack.pop();
			Node replaceMeOldParent = (Node)stack.pop();
			replaceMe.parent=replaceMeOldParent;
			Node replaceMeOldChild;
			if(replaceMe.key>replaceMeOldParent.key) {
				replaceMeOldChild= replaceMeOldParent.right;
				replaceMeOldParent.right=replaceMe;
			}
			else {
				replaceMeOldChild= replaceMeOldParent.left;
				replaceMeOldParent.left=replaceMe;
			}
			replaceMe.right = replaceMeOldChild;
			replaceMeOldChild.parent=replaceMe;
		}
		else {
			replaceMe.parent=sp;
			if(replaceMe.key>sp.key) 
				sp.right=replaceMe;
			else
				sp.left=replaceMe;
			replaceMe.right=null;
			
		}
		if(((Node)stack.pop()).key==1) {
			stack.pop();stack.pop();stack.pop();
		}
		else
			stack.pop();stack.pop();	
		return;
	}
		
	
	

	@Override
	public void retrack() {
		if(redoStack.isEmpty())return;
		Node flag =(Node)redoStack.pop();
		if(flag.key==1) { //backtrack was to delete an inserted node
			Node deleted = (Node)redoStack.pop();
			if(deleted.key>deleted.parent.key)
				deleted.parent.right=deleted;
			else deleted.parent.left=deleted;
			return;
		}
		Node deleteMe = (Node)redoStack.pop();
		delete(deleteMe);
	}

	public void printPreOrder() {
		if(root==null)
			return;
		System.out.print(root.key);
		if(root.left!=null) {
			System.out.print(" ");
			BacktrackingBST leftSubTree = new BacktrackingBST(stack,redoStack);
			leftSubTree.root=root.left;
			leftSubTree.printPreOrder();
			
		}
		if(root.right!=null) {
			System.out.print(" ");
			BacktrackingBST rightSubTree = new BacktrackingBST(stack,redoStack);
			rightSubTree.root=root.right;
			rightSubTree.printPreOrder();
		}
		
	}

	@Override
	public void print() {
		printPreOrder();
		System.out.println();
	}
	
	private void deleteRoot() {
		Node temp = root;
		if(root.right==null) {
			if(root.left!=null) {
				root = root.left;
				root.parent = null;
			}
			else
				root=null;
		}
		else {
			Node succ = successor(root);
			Node succIsLeaf;
			if(succ.right==null&succ.left==null)
				succIsLeaf = new Node(0,null);
			else
				succIsLeaf = new Node(1,null);
			delete(succ);
			stack.push(succIsLeaf);
			stack.push(succ.parent);
			succ.right=root.right;
			succ.left=root.left;
			succ.parent=null;
			root =succ;
		}
		stack.push(temp);
		stack.push(new Node(3,null));
	}
	
	

	public static class Node {
		// These fields are public for grading purposes. By coding conventions and best
		// practice they should be private.
		public BacktrackingBST.Node left;
		public BacktrackingBST.Node right;

		private BacktrackingBST.Node parent;
		private int key;
		private Object value;

		public Node(int key, Object value) {
			this.key = key;
			this.value = value;
		}

		public int getKey() {
			return key;
		}

		public Object getValue() {
			return value;
		}

	}
}