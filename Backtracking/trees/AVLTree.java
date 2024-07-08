import java.util.List;
import java.util.NoSuchElementException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;

public class AVLTree implements Iterable<Integer> {
    // You may edit the following nested class:
    protected class Node {
    	public Node left = null;
    	public Node right = null;
    	public Node parent = null;
    	public int height = 0;
    	public int value;
    	protected int leftSubTreeSize; //our field
    	protected int rightSubTreeSize; //our field
    	

    	public Node(int val) {
            this.value = val;
        	leftSubTreeSize=0;
        	rightSubTreeSize=0;
        }

        public void updateHeight() {
            int leftHeight = (left == null) ? -1 : left.height;
            int rightHeight = (right == null) ? -1 : right.height;

            height = Math.max(leftHeight, rightHeight) + 1;
        }

        public int getBalanceFactor() {
            int leftHeight = (left == null) ? -1 : left.height;
            int rightHeight = (right == null) ? -1 : right.height;

            return leftHeight - rightHeight;
        }
    }
    
    protected Node root;
    
    //You may add fields here.
    protected Deque<Integer> vf; // values-flags
    protected Deque<Node> nr; //nodes - rotations
    
    
    public AVLTree() {
    	this.root = null;
    	vf = new ArrayDeque<Integer>(); //values - flags
    	nr = new ArrayDeque<Node>(); //nodes - rotations
    }
    
    /*
     * IMPORTANT: You may add code to both "insert" and "insertNode" functions.
     */
	public void insert(int value) {
		vf.addLast(value);
		nr.addFirst(new Node(0));
    	root = insertNode(root, value);
    }
	
	protected Node insertNode(Node node, int value) { 
	    // Perform regular BST insertion
        if (node == null&&root==null) {
        	vf.addFirst(-1);
        	Node insertedNode = new Node(value);
            return insertedNode;
        }
        if (node == null) {
        	vf.addFirst(0);
        	Node insertedNode = new Node(value);
            return insertedNode;
        }

        if (value < node.value) {
            node.left  = insertNode(node.left, value);
            node.left.parent = node;
            node.leftSubTreeSize++; //our line
            
        }
        else {
            node.right = insertNode(node.right, value);
            node.right.parent = node;
            node.rightSubTreeSize++; //our line
        }
            
        node.updateHeight();

        /* 
         * Check For Imbalance, and fix according to the AVL-Tree Definition
         * If (balance > 1) -> Left Cases, (balance < -1) -> Right cases
         */
        
        int balance = node.getBalanceFactor();
        
        if (balance > 1) {
        	nr.addFirst(new Node(1));
            if (value > node.left.value) {	//LR rotation
            	vf.addFirst(21);
                node.left = rotateLeft(node.left);
            }
            else	//RR rotation
            	vf.addFirst(11);
            
            node = rotateRight(node);
        } else if (balance < -1) {
        	nr.addFirst(new Node(1));
            if (value < node.right.value) {	//RL rotation
            	vf.addFirst(12);
                node.right = rotateRight(node.right);
            }
            else	//LL rotation
            	vf.addFirst(22);
            
            node = rotateLeft(node);
        }

        return node;
    }
    
	// You may add additional code to the next two functions.
    protected Node rotateRight(Node y) {
    	nr.addLast(y);
        Node x = y.left;
        Node T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;
        
        //Update parents
        if (T2 != null) {
        	T2.parent = y;
        	y.leftSubTreeSize =T2.leftSubTreeSize+T2.rightSubTreeSize+1; //our line
        }
        else
        	y.leftSubTreeSize = 0;
       
        
        //update x.rightSubTreeSize
        x.rightSubTreeSize = y.rightSubTreeSize+y.leftSubTreeSize+1;
        
        x.parent = y.parent;
        y.parent = x;
        
        y.updateHeight();
        x.updateHeight();

        // Return new root
        return x;
    }

    protected Node rotateLeft(Node x) {
    	nr.addLast(x);
        Node y = x.right;
        Node T2 = y.left;
        
        // Perform rotation
        y.left = x;
        x.right = T2;
        
        
        
        //Update parents
        if (T2 != null) {
        	T2.parent = x;
        	x.rightSubTreeSize=T2.rightSubTreeSize+T2.leftSubTreeSize+1; //our line
        }
        else //our lines
        	x.rightSubTreeSize = 0;
        
        y.leftSubTreeSize = x.leftSubTreeSize+x.rightSubTreeSize+1; 
        
        
        y.parent = x.parent;
        x.parent = y;
        
        x.updateHeight();
        y.updateHeight();

        // Return new root
        return y;
    }
    
    public void printTree() {
    	TreePrinter.print(this.root);
    }

    /***
     * A Printer for the AVL-Tree. Helper Class for the method printTree().
     * Not relevant to the assignment.
     */
    private static class TreePrinter {
        private static void print(Node root) {
            if(root == null) {
                System.out.println("(XXXXXX)");
            } else {    
                final int height = root.height + 1;
                final int halfValueWidth = 4;
                int elements = 1;
                
                List<Node> currentLevel = new ArrayList<Node>(1);
                List<Node> nextLevel    = new ArrayList<Node>(2);
                currentLevel.add(root);
                
                // Iterating through the tree by level
                for(int i = 0; i < height; i++) {
                    String textBuffer = createSpaceBuffer(halfValueWidth * ((int)Math.pow(2, height-1-i) - 1));
        
                    // Print tree node elements
                    for(Node n : currentLevel) {
                        System.out.print(textBuffer);
        
                        if(n == null) {
                            System.out.print("        ");
                            nextLevel.add(null);
                            nextLevel.add(null);
                        } else {
                            System.out.printf("(%6d)", n.value);
                            nextLevel.add(n.left);
                            nextLevel.add(n.right);
                        }
                        
                        System.out.print(textBuffer);
                    }
        
                    System.out.println();
                    
                    if(i < height - 1) {
                        printNodeConnectors(currentLevel, textBuffer);
                    }
        
                    elements *= 2;
                    currentLevel = nextLevel;
                    nextLevel = new ArrayList<Node>(elements);
                }
            }
        }
        
        private static String createSpaceBuffer(int size) {
            char[] buff = new char[size];
            Arrays.fill(buff, ' ');
            
            return new String(buff);
        }
        
        private static void printNodeConnectors(List<Node> current, String textBuffer) {
            for(Node n : current) {
                System.out.print(textBuffer);
                if(n == null) {
                    System.out.print("        ");
                } else {
                    System.out.printf("%s      %s",
                            n.left == null ? " " : "/", n.right == null ? " " : "\\");
                }
    
                System.out.print(textBuffer);
            }
    
            System.out.println();
        }
    }

    /***
     * A base class for any Iterator over Binary-Search Tree.
     * Not relevant to the assignment, but may be interesting to read!
     * DO NOT WRITE CODE IN THE ITERATORS, THIS MAY FAIL THE AUTOMATIC TESTS!!!
     */
    private abstract class BaseBSTIterator implements Iterator<Integer> {
        private List<Integer> values;
        private int index;
        public BaseBSTIterator(Node root) {
            values = new ArrayList<>();
            addValues(root);
            
            index = 0;
        }
        
        @Override
        public boolean hasNext() {
            return index < values.size();
        }

        @Override
        public Integer next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            
            return values.get(index++);
        }
        
        protected void addNode(Node node) {
            values.add(node.value);
        }
        
        abstract protected void addValues(Node node);
    }
    
    public class InorderIterator extends BaseBSTIterator {
        public InorderIterator(Node root) {
            super(root);
        }

        @Override
        protected void addValues(Node node) {
            if (node != null) {
                addValues(node.left);
                addNode(node);
                addValues(node.right);
            }
        }    
      
    }
    
    public class PreorderIterator extends BaseBSTIterator {

        public PreorderIterator(Node root) {
            super(root);
        }

        @Override
        protected void addValues(AVLTree.Node node) {
            if (node != null) {
                addNode(node);
                addValues(node.left);
                addValues(node.right);
            }
        }        
    }
    
    @Override
    public Iterator<Integer> iterator() {
        return getInorderIterator();
    }
    
    public Iterator<Integer> getInorderIterator() {
        return new InorderIterator(this.root);
    }
    
    public Iterator<Integer> getPreorderIterator() {
        return new PreorderIterator(this.root);
    }
    
    
}
