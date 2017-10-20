package algorithm;

import java.util.ArrayList;
 
public class ReduplicativePermutation {
 
    private int n;
    private int r;
    public ArrayList<String>result = new ArrayList<>();
    private String[] res;
    
    public ReduplicativePermutation(int n, int r){
        this.n = n;
        this.r = r;
        res = new String[r];
    }
    
    public void swap(String[] arr, int i, int j) {
        String temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    public void save(String[] arr) {
    	String str = "";
    	for(String a : arr)
    		str+=a;
    	result.add(str);
    }
 
    public void perm(String[] arr, int depth) {
        
        if (depth == r) {
        	save(res);
            return;
        }
        
        for (int i = 0; i < n; i++) {
            swap(arr, depth, i);
            res[depth] = arr[depth];
            perm(arr, depth +1);
            swap(arr, depth, i);
        }
    }
}
 
