

public class Warmup {
    public static int backtrackingSearch(int[] arr, int x, int forward, int back, Stack myStack) {
        int i =0;
        while (i<arr.length) {
        	for(int j=0;j<forward&i<arr.length;j++) {
        		if(arr[i]==x) return i;
        		else {
        			myStack.push(arr[i]);
        			i++;
        			if(i==arr.length) return -1;
        		}
        	}
        	for(int j=0;j<back&i>0;j++) {
        		i--;
        		if(arr[i] != (Integer)myStack.pop()) return -1;
        	}						
        }
        return -1;
    }

	public static int consistentBinSearch(int[] arr, int x, Stack myStack) {
		if(arr.length==0) return -1;
		int[] highs = new int[arr.length];
		int[] lows = new int[arr.length];
		int highsIndex=0;
		int lowsIndex = 0;
    	int low = 0, high = arr.length-1;
    	highs[highsIndex] = high;
		highsIndex++;
		lows[lowsIndex] = low;
		lowsIndex++;
    	while (low <= high){
    		int middle = (low+high)/2;
    		myStack.push(middle);
    		if(arr[middle] == x){
    			return middle; 
    			}
    		else if (x < arr[middle]){
    			if(highsIndex==highs.length) return -1;
    			high = middle-1;
    			highs[highsIndex] = high;
    			highsIndex++;
    			}
    		else{
    			if(lowsIndex==lows.length) return -1;
    			low = middle+1;
    			lows[highsIndex] = low;
    			lowsIndex++;
    		}
    		int inconsistencies = Consistency.isConsistent(arr);
	    		for(int j=0; j<inconsistencies;j++){		//if inconsistencies>0
	    			if(!myStack.isEmpty()) {
		    			if((Integer)myStack.pop()<middle) {
			    			low=lows[lowsIndex-1];
			    			lowsIndex--;
		    			}
		    			else {
		    				high=highs[highsIndex-1];
		    				highsIndex--;
		    			}
	    			}
	    		}
    		
    	
    	}
    	return -987; // temporal return command to prevent compilation error
    }

		
	
	
	public static void main(String[] args) {
		int[] arr ={1,2,3,5,6,7};
		Stack myStack = new Stack();
		System.out.println(consistentBinSearch(arr, 4, myStack));
	}
}







