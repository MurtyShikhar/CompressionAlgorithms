public class MoveToFront{
	private static final int R = 256;
	
	public static void encode() {
		char[] seq = new char[R];
		for (int i = 0; i < R; i++)
			seq[i] = (char) i;

		while (!BinaryStdIn.isEmpty()) {
			char current = BinaryStdIn.readChar();

			for (int i = 0; i < R; i++) {
				if (current == seq[i]) {
					BinaryStdOut.write(i,8);
					if (i != 0)
						move(i,seq);
				}
			}
		}

		BinaryStdOut.close();
	}

	public static void decode() {
		char[] seq = new char[R];
		for (int i = 0; i < R; i++)
			seq[i] = (char) i;
		while (!BinaryStdIn.isEmpty()) {
			int current = BinaryStdIn.readChar(8);
			char wrt = seq[current];
			BinaryStdOut.write(wrt);
			if (current != 0)
				move(current,seq);
		}

		BinaryStdOut.close();
	}

	public static void move(int i,char[] seq) {
		while (i != 0){
			exch(i,i-1,seq);
			i--;
		}
		return;
	}

	private static void exch(int i,int j, char[] a) {
		char temp = a[i];
		a[i] = a[j];
		a[j] = temp;
		return; 
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