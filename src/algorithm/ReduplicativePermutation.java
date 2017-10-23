package algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
 
public class ReduplicativePermutation {
 
    private int n;
    private int r;
    private ArrayList<String>result = new ArrayList<>();
    public HashSet<String>set = new HashSet<>();
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
    	set.add(str);
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
    
    public ArrayList<String> getResults() {
    	result.addAll(set);
    	result.sort(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
    	});
    	return result;
    }
    /*
     *  배열의 길이를 초과하는 가짓수에 대한 해결법 
     * 	u, o 일 경우 초과하는 가짓수에 대해서 같은 String 하나를 더 추가한 배열을 선언하여 
     * ex) 2개중에 3개를 중복하여 순열을 만들경우
     * 	String[] uo = {"언", "옵", "옵"}; 
     * 에서 uo를 갖고 perm(2,3)을 진행한다. 그것의 결과를
     * hashset에 담아서 재출력하면 중복된것들이 제거되고 2PI3의 결과값이 나오게 된다.
     * 굉장히 위험하고 메모리 누수가 걱정되며 알고리즘 적으로 굉장히 비효율적이지만
     * 현재 상황에서 간단하게 해결할 수 있는 방법 중 하나이다.
    */
}
 
