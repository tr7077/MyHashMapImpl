import java.util.Random;

public class UniversalHashFunction {
	
	public static final Long DEFAULT_FIXED_SEED = 17l;
	public static final int DEFAULT_HASHCODE_BITS = 32;
	
	private Random random;
	
	private int b, u;
	private int[][] arrayF;
	
	public UniversalHashFunction(int arrayLength, int hashCodeBits, Long seed){
		if(arrayLength <= 0 || hashCodeBits <= 0 || log2(arrayLength) != Math.floor(log2(arrayLength))) {
			throw new IllegalArgumentException("ArrayLength must be positive and power of 2. hashCodeBits must be positive.");
		}
		
		this.b = (int)log2(arrayLength);
		this.u = hashCodeBits;
		
		this.random = seed == null ? new Random() : new Random(seed);
		
		arrayF = new int[b][u];
		
		for(int i=0; i<b; i++) {
			for(int j=0; j<u; j++) {
				arrayF[i][j] = random.nextInt(2); 
			}
		}
	}
	public UniversalHashFunction(int arrayLength) {
		this(arrayLength, DEFAULT_HASHCODE_BITS, null);
	}
	public UniversalHashFunction(int arrayLength, int hashCodeBits) {
		this(arrayLength, hashCodeBits, null);
	}
	public UniversalHashFunction(int arrayLength, Long seed) {
		this(arrayLength, DEFAULT_HASHCODE_BITS, seed);
	}
	
	public int hash(int hashCode) {

		int[] hashCodeBits = new int[u];
		int[] result = new int[b];
		int mappedIndex = 0;
		
		for(int i=0; i<u; i++) {
			hashCodeBits[i] = (hashCode & (1<<i)) == 0 ? 0 : 1;
		}
		
		for(int i=0; i<b; i++) {
			int temp = 0;
			for(int j=0; j<u; j++) {
				temp += arrayF[i][j] * hashCodeBits[u-j-1] % 2;
			}
			result[b-i-1] = temp % 2;
			
			// could be done without holding a result array:
			// mappedIndex += (temp % 2)*(int)Math.pow(2, i);
		}
		
		for(int i=0; i<b; i++) {
			mappedIndex += result[i]*(int)Math.pow(2, i);
		}
		
		return mappedIndex;
	}
	
	private static double log2(int x) {
        return Math.log(x) / Math.log(2);
    }
}
