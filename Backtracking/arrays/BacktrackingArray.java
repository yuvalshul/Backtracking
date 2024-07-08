

public class BacktrackingArray implements Array<Integer>, Backtrack {
    private Stack stack;
    private int[] arr;
    private int i;
    
    // TODO: implement your code here

    // Do not change the constructor's signature
    public BacktrackingArray(Stack stack, int size) {
        this.stack = stack;
        arr = new int[size];
        i=0;
    }

    @Override
    public Integer get(int index){
        // TODO: implement your code here
    	if(index<i)
    		return arr[index];
    	else
    		throw new IllegalArgumentException();
    }

    @Override
    public Integer search(int k) {
        for(int j =0;j<i;j++)
        	if(arr[j]==k) return j;
        return -1;
    }

    @Override
    public void insert(Integer x) {
    	if(i==arr.length) throw new IllegalArgumentException();
    	arr[i]=x;
    	i++;
    	stack.push(-1);
    }

    @Override
	public void delete(Integer index) {
		if (i == 0 || index >= i)
			throw new IllegalArgumentException();
		stack.push(arr[index]);
		stack.push(index);
		arr[index] = arr[i - 1];
		i--;
	}

    @Override
	public Integer minimum() {
    	if(i==0)
			throw new IllegalArgumentException();
		int min = 0;
		for (int j = 1; j < i; j++)
			if (arr[j] < arr[min])
				min = j;
		return min;
	}

	@Override
	public Integer maximum() {
		if(i==0)
			throw new IllegalArgumentException();
		int max = 0;
		for (int j = 1; j < i; j++)
			if (arr[j] > arr[max])
				max = j;
		return max;
	}

    @Override
	public Integer successor(Integer index) {
		int suc = index;
		for (int j = 0; j < i; j++) {
			if (arr[j] > arr[index])
				if (suc == index || arr[j] < arr[suc])
					suc = j;
		}
		if (suc == index)
			throw new IllegalArgumentException();
		return suc;
	}

	@Override
	public Integer predecessor(Integer index) {
		int pre = index;
		for (int j = 0; j < i; j++) {
			if (arr[j] < arr[index])
				if (pre == index || arr[j] > arr[pre])
					pre = j;
		}
		if (pre == index)
			throw new IllegalArgumentException();
		return pre;
	}

    @Override
    public void backtrack() {
        // TODO: implement your code here
    	if(!stack.isEmpty()) {
    		int flag = (int) stack.pop();
    		if(flag==-1) i--;
    		else {
    			int key = (int)stack.pop();
    			arr[i]=(arr[flag]);
    			i++;
    			arr[flag]=key;
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
			if (j != i)
				output = output + arr[j] + " ";
			else
				output = output + arr[j];
		}
		System.out.println(output);
	}
}
