

public class BacktrackingSortedArray implements Array<Integer>, Backtrack {
    private Stack stack;
    public int[] arr; // This field is public for grading purposes. By coding conventions and best practice it should be private.
    private int i;
    // TODO: implement your code here

    // Do not change the constructor's signature
    public BacktrackingSortedArray(Stack stack, int size) {
        this.stack = stack;
        arr = new int[size];
        i=0;
    }
    
    @Override
    public Integer get(int index){
        if(index<i&&index>=0)
        	return arr[index];
    	else
    		throw new IllegalArgumentException();
    }

    @Override
    public Integer search(int k) {
		int output = -1; 
		boolean found = false;
		int low = 0, high = i-1;
		while (low <= high & !found) {
			int middle = (low + high) / 2;
			if (arr[middle] == k) {
				output = middle;
				found = true;
			} else if (k < arr[middle]) {
				high = middle - 1;
			} else
				low = middle + 1;
		}
		return output;
	}


    @Override
    public void insert(Integer x) {
    	if(search(x)== -1) {
	        if(i==arr.length) throw new IllegalArgumentException();
	        boolean shifted =false;
	        for(int j=0;j<i&!shifted;j++) {
	        	if(arr[j]>x) {
	        		for(int h=i;h>j;h--) 
	        			arr[h]=arr[h-1];
	        		shifted=true;
	        		arr[j]=x;
	        		stack.push(j);
	        	}
	        }
	        if(!shifted) { 
	        	arr[i]=x;
	        	stack.push(i);
	        }
	        i++;
	        stack.push(-1);
    	}
    }

    @Override
    public void delete(Integer index) {
    	if(i==0|index>i-1) throw new IllegalArgumentException();
    	stack.push(arr[index]);
    	 stack.push(1);
    		for(int j=index;j<i;j++) 
    		 arr[j]=arr[j+1];
    	 i--;
    	 
    }

    @Override
    public Integer minimum() {
        if(i>0)
        	return 0;
        else throw new IllegalArgumentException();
    }

    @Override
    public Integer maximum() {
    	if (i>0)
    		return i-1;
    	else throw new IllegalArgumentException();
    }

    @Override
    public Integer successor(Integer index) {
        if(index>=i-1|index<0) throw new IllegalArgumentException();
        return index+1;
        		
    }

    @Override
    public Integer predecessor(Integer index) {
    	 if (index>i-1|index<=0) throw new IllegalArgumentException();
         return index-1;
    }

    @Override
    public void backtrack() {
    	if(!stack.isEmpty()) {
    		if((int)stack.pop()==-1) { //last function was insert
    			delete((int)stack.pop());
    			stack.pop();
    			stack.pop();
    		}
    		else { //last function was delete
    			insert((int)stack.pop());
    			stack.pop();
    			stack.pop();
    		}
    	}
    }

    @Override
    public void retrack() {
		/////////////////////////////////////
		// Do not implement anything here! //
		/////////////////////////////////////
    }

    @Override
    public void print() {
    	String output = "";
		for (int j = 0; j < i; j++) {
			if (j != i-1)
				output = output + arr[j] + " ";
			else
				output = output + arr[j];
		}
		System.out.println(output);
    }
    
    
    
}
