package calculus.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountMap <T>{

	private Map<T, Integer> counts;
	
	public CountMap(){
		counts = new HashMap<T, Integer>();
	}
	
	public int increment(T t){
		if (counts.containsKey(t)){
			int newCount = counts.get(t) + 1;
			counts.put(t, newCount);
			return newCount;
		} else {
			counts.put(t, 1);
			return 1;
		}
	}
	
	public List<T> getAscendingList(){
		List<T> keys = new ArrayList<T>(counts.keySet());
		Collections.sort(keys, new MapCountComparator(counts));
		return keys;
	}
	
	public List<T> getAscendingList(int n){
		List<T> ascList = getAscendingList();
		if (n >= ascList.size()){
			return ascList;
		}
		List<T> result = new ArrayList<T>(ascList.subList(0, n));
		return result;
	}
	
	public List<T> getDescendingList(){
		List<T> asc = getAscendingList();
		Collections.reverse(asc);
		return asc;
	}
	
	public List<T> getDescendingList(int n){
		List<T> descList = getDescendingList();
		if (n >= descList.size()){
			return descList;
		}
		List<T> result = new ArrayList<T>(descList.subList(0, n));
		return result;
	}
	
	private class MapCountComparator implements Comparator<T>{
		
		private Map<T, Integer> counts;
		
		public MapCountComparator(Map<T, Integer> counts){
			this.counts = counts;
		}

		@Override
		public int compare(T o1, T o2) {
			if (!counts.containsKey(o1) && !counts.containsKey(o2)){
				return 0;
			} else if (!counts.containsKey(o1)){
				return 1;
			} else if (!counts.containsKey(o2)){
				return -1;
			}
			return counts.get(o1).compareTo(counts.get(o2));
		}
	}
}
