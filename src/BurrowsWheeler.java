import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import edu.princeton.cs.algs4.Queue;
public class BurrowsWheeler{
	private static final int CHAR_BITS = 8;


	public static void encode(){
		String input = BinaryStdIn.readString() + '$';
		CircularSuffixArray c = new CircularSuffixArray(input);
		for (int i = 0; i < c.length(); i++)
			if (c.index(i) == 0)
			{
				BinaryStdOut.write(i);
				break;
			}

		for (int i = 0; i < c.length(); ++i){
			int index = c.index(i);
			if (index == 0)
			{
				BinaryStdOut.write(input.charAt(input.length() -1), CHAR_BITS);
				continue;
			}
			BinaryStdOut.write(input.charAt(index-1), CHAR_BITS);
		}

		BinaryStdOut.close();
	}


	public static void decode(){
		int first = BinaryStdIn.readInt();
		String chars = BinaryStdIn.readString();
		char[] t = chars.toCharArray();
		chars = null;
		int next[] = new int[t.length];
		Map<Character, Queue<Integer> > positions = new HashMap<Character, Queue<Integer> > ();
		for (int i = 0; i < t.length; i++){
			if (!positions.containsKey(t[i]))
				positions.put(t[i], new Queue<Integer>());
			positions.get(t[i]).enqueue(i);
		}

		Arrays.sort(t);
		for (int i = 0; i < t.length; i++)
			next[i] = positions.get(t[i]).dequeue();
		for (int i =0, currRow = first; i < t.length; i++, currRow = next[currRow])
		{
			if (t[currRow] == '$') break;
			BinaryStdOut.write(t[currRow]);
		}

		BinaryStdOut.close();
	}

	public static void main(String[] args) {
      if (args.length == 0) 
         throw new java.lang.IllegalArgumentException("Usage: input '+' for encoding or '-' for decoding");
      if (args[0].equals("-")) 
         encode();
      else if (args[0].equals("+")) 
         decode();
      else 
         throw new java.lang.IllegalArgumentException("Usage: input '+' for encoding or '-' for decoding");
   }
}
