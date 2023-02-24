import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class HashTable<K, V> implements Dictionary<K, V>{
	
	public static int DEFAULT_LENGTH = 16;
	private int size;
	private Entry<K, V>[] array;
	private UniversalHashFunction hashF;
	
	public HashTable(int length) {
		if(length <= 0) throw new IllegalArgumentException("Length must be positive.");
		hashF = new UniversalHashFunction(length);
		this.size = 0;
		this.array = (Entry<K, V>[]) Array.newInstance(EntryImpl.class, length);
	}
	public HashTable() {
		this(DEFAULT_LENGTH);
	}

	@Override
	public void put(K key, V value) {
		rehash();
		insert(key, value);
	}

	@Override
	public V remove(K key) {
		if (!this.contains(key)) {
			throw new NoSuchElementException("The key "+key+" doesn't exist.");
		}
		rehash();
		
		int cur = hashF.hash(key.hashCode());
		V ret = null;
		
		while(!array[cur].getKey().equals(key)) {
			cur = (cur+1) % array.length;
		}
		// delete the element if found
		ret = array[cur].getValue();
		array[cur] = null;
		size--;
		
		//fix the position of null if needed
		int next = (cur+1) % array.length;
		while(array[next] != null) {
			int defIdx = hashF.hash(array[next].getKey().hashCode());
            
            while(array[defIdx] != null) {
            	defIdx = (defIdx + 1) % array.length; 
            }
            if(defIdx != next) {
                array[defIdx] = array[next];
                array[next] = null;	     
            }       
			
			next = (next + 1) % array.length;
		}

		return ret;
	}

	@Override
	public V get(K key) {
		int index = hashF.hash(key.hashCode());
		
		while(array[index] != null) {
			if(array[index].getKey().equals(key)) {
				return array[index].getValue();
			}
			index = (index+1) % array.length;
		}
		
		return null;
	}

	@Override
	public boolean contains(K key) {
		int index = hashF.hash(key.hashCode());
		
		while(array[index] != null) {
			if(array[index].getKey().equals(key)) {
				return true;
			}
			index = (index+1) % array.length;
		}
		
		return false;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public void clear() {
		hashF = new UniversalHashFunction(DEFAULT_LENGTH);
		this.size = 0;
		this.array = (Entry<K, V>[]) Array.newInstance(EntryImpl.class, DEFAULT_LENGTH);
	}

	@Override
	public Iterator<Entry<K, V>> iterator() {
		return new HashIterator();
	}
	
	private void rehash() {
		int newLength;
		if((double)size/array.length >= 0.5) {
			newLength = array.length * 2;
		}
		else if((double)size/array.length <= 0.25 && array.length >= 2*DEFAULT_LENGTH) {
			newLength = array.length / 2;
		}
		else return;
		
		HashTable<K, V> newTable = new HashTable<K, V>(newLength);
		
		for(Entry<K, V> e: this.array) {
			if(e != null) newTable.insert(e.getKey(), e.getValue());
		}
		
		this.hashF = newTable.hashF;
		this.array = newTable.array;
		// size must be the same
	}
	private void insert(K key, V value) {
		if (value == null) {
			throw new IllegalArgumentException("Value can not be null.");
		}
		
		int index = hashF.hash(key.hashCode());
		
		while(array[index] != null) {
			if (array[index].getKey().equals(key)) {
				array[index] = new EntryImpl<K, V>(key, value);
				return;
			}
			index = (index + 1) % array.length;
		}
		
		array[index] = new EntryImpl<K, V>(key, value);
		size++;
	}
	
	private static class EntryImpl<K, V> implements Dictionary.Entry<K, V>{

		private K key;
		private V value;
		
		private EntryImpl(K key, V value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}
	}
	
	private class HashIterator implements Iterator<Dictionary.Entry<K, V>> {
		
		private int cur;
		
		private HashIterator() {this.cur = 0;}

		@Override
		public boolean hasNext() {
			while(cur < array.length) {
				if(array[cur] != null) return true;
				cur++;
			}
			return false;
		}

		@Override
		public Dictionary.Entry<K, V> next() {
			if(!hasNext()) {
				throw new NoSuchElementException();
			}
			return array[cur++];
		}
		
	}

}
