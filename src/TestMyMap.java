import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.Test;

class TestMyMap {

	@Test
void test() {
		
		for(int k=0; k<100; k++) {
			
			HashTable<Integer, Integer> hashMap = new HashTable<>();
			int r = new Random().nextInt();
			
			int SIZE = Math.abs(new Random().nextInt(1, 1001));
			
			assertEquals(hashMap.size(), 0);
			assertTrue(hashMap.isEmpty());
			
			for (int i=1;i<=SIZE;i++) {
				hashMap.put(r+i, i);
				assertTrue(hashMap.contains(r+i));
			}
			assertEquals(hashMap.size(), SIZE);
			assertFalse(hashMap.isEmpty());
			
			for (int i=1;i<=SIZE;i++) {
				assertTrue(hashMap.contains(r+i));
				assertEquals(hashMap.get(r+i), i);
			}
			
			for (int i=1;i<=SIZE;i++) {
				assertTrue(hashMap.contains(r+i));
				hashMap.remove(r+i);
			}
			assertTrue(hashMap.isEmpty());
			assertEquals(hashMap.size(), 0);
			
		}
	}
}

