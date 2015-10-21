/* Author: Shikhar Murty 
 * SuffixSort algorithm using Manber Myers in O(n*logn)
 * Also implements kasai's algorithm for fast lcp queries in O(n)
 */

public class SuffixSort {
	private int[] cnt;
	private boolean[] bh;
	private boolean[] b2h;
	private int[] p2r;
	private int[] r2p;
	public int N;
	private char[] text;
	private String x;
	private int[] height;
	public SuffixSort(String input) {
		N = input.length();
		text = input.toCharArray();
		x = input;
		cnt = new int[N];
		p2r = new int[N];
		r2p = new int[N];
		bh = new boolean[N];
		b2h = new boolean[N];
		height = new int[N];
		/* perform counting sort on the first characters */
		int R = 256;
		int[] aux = new int[N];
		int[] count = new int[R+1];
		for (int i = 0; i < N; i++)
			count[input.charAt(i)+1]++;
		for (int r = 0; r < R; r++)
			count[r+1] += count[r];
		for (int i = 0; i < N; i++)
			aux[count[input.charAt(i)]++] = i;  // if the first character is say d,then aux[5] = 1 which means position of 5th element is 1
		for (int i = 0; i < N; i++) {
			r2p[i] = aux[i]; // then aux[i] is the rank to position.
			
		}
			

		/* done with counting sort */

		for (int i = 0; i < N; i++){
			bh[i] = i == 0 || (text[r2p[i]] != text[r2p[i-1]]); //bh[i] is true if it is the start of a bucket
			b2h[i] = false; // initialized this way
		}

		for (int i = 0; i < N; i++)
			p2r[r2p[i]] = i;
		
		Manber();
		calculate_height();
	}

	private void Manber() {

		int[] next = new int[N];
		for (int h = 1; h < N; h *= 1) {
			int buckets = 0;
			int i = 0;
			int j;
			while (i < N) {
				j = i+1;
				while (j < N && !bh[j]) j++;
				next[i] = j; // next[i] is the rank of the next bucket
				buckets++;
				i = j;
			}
			/* at this point,all the buckets are organized such that the first h elements of each suffix in each bucket are equal..
			 * the buckets start at l and end at next[l] -1;
			 */
			if (buckets == N) return; //done
			for (i = 0; i < N; i = next[i]) {
				cnt[i] = 0; // set the count value for each left hand boundary of a bucket to 0
				for (j = i; j < next[i]; j++)
					// since the buckets are organized by rank,first find out the position of this element by r2p[j]
					// then set its rank to i
					// now all the elements in the bucket have the same rank which is equal to the rank to the first element in
					// the bucket. therefore for any element, p2r[i] is the rank of the first element in the bucket for that element
					p2r[r2p[j]] = i; 
			}

			/* this part is necessary because r2p[j] goes from 0 to N-1 only */
			/* therefore the positions that are changed are from 0 to  N-1-h and less */
			/* the ones with position N-h+1 to N-1  (h - 1) elements are suffixes of length h-1 or less so they are ok*/
			/* but the element N - h is of size h so it'll come first in its bucket. So place it there. */
			int d = N - h;
			int head = p2r[d];
			p2r[d] = head;
			cnt[head]++;
			b2h[p2r[d]] = true;

			/* now go through all the buckets  */
			/* the next few lines are for suffixes of size N to h+1 */
			/* clearly assuming we have sorted in 2h order till bucket i,for bucket i+1,let l and r be the left and right bounds */
			/* for all of thse calculate pos[j] - h,and place that element to the first available place in its bucket */
			/* if multiple elements from the same bucket have been moved we have some additional work to do*/
			for (i = 0; i < N; i = next[i]) {
				for (j = i; j < next[i]; j++) {
					assert (r2p[j] < N);
					int position_of_suffix = r2p[j] - h; // find the position of the suffix that deserves to be first in its bucket
					if (position_of_suffix >= 0){

						int bucket_head = p2r[position_of_suffix]; // find the bucket head corresponding to this 
						p2r[position_of_suffix] = bucket_head + cnt[bucket_head]; // set the rank of this suffix to be the first available position
						cnt[bucket_head]++; //increment the count
						assert (p2r[position_of_suffix] < N);
						b2h[p2r[position_of_suffix]] = true; // this is a possible start of a bucket
					}
				}
				// now,you need to make sure that only the start is a bucket head
				for (j = i; j < next[i]; j++) {
					int position_of_suffix = r2p[j] - h;
					if (position_of_suffix >= 0 && b2h[p2r[position_of_suffix]]) {
						for (int k = p2r[position_of_suffix]+1; k < N && !bh[k] && b2h[k]; k++) 
							b2h[k] = false;
					}
				}
			}

			for (i = 0; i < N; i++) {
				r2p[p2r[i]] = i;
				bh[i] |= b2h[i];
			}

		}
	}

	// returns the index into the string of the ith smallest suffix
	public int index(int i) {
		if (i < 0 || i >= text.length) throw new IndexOutOfBoundsException();
		return r2p[i];
	}

	public String select(int i) {
        if (i < 0 || i >= text.length) throw new IndexOutOfBoundsException();
        return x.substring(r2p[i]);
    }

    /* O(N) algorithm for calculating lcp information */
    /* height[i] stores the lcp of elements whose rank is i and i-1  */
    /* now the lcp of any 2 suffixes say i and j, whose ranks are l and m is just the minimum of 
     * heights of l+1...m
     */
    private void calculate_height() {
    	int h = 0;
    	height[0] = 0;
    	for (int i = 0; i < N; i++) {
    		if (p2r[i] > 0) {
    			int k = r2p[p2r[i] - 1];
    			while (i+h < N && k+h < N && text[i+h] == text[k+h]) h++;
    			height[p2r[i]] = h;
    			if (h > 0) h--;
    		}
    	}
    }


	public void show() {

        StdOut.println("  i ind - rank select");
        StdOut.println("---------------------------");

        for (int i = 0; i < x.length(); i++) {
            int index = index(i);
            String ith = "\"" + x.substring(index, Math.min(index + 50, x.length())) + "\"";
            assert x.substring(index).equals(select(i));
            int rank = i;
            if (i == 0) {
                StdOut.printf("%3d %3d %3s %3d %s\n", i, index, "-", rank, ith);
            }
            else {
                StdOut.printf("%3d %3d %3s %3d %s\n", i, index, "-", rank, ith);
            }
        }
    }

    public int lcp(int i) {
    	if (i < 1 || i >= x.length()) throw new IndexOutOfBoundsException();
    	return height[i];
    }

    public static String lrs(String text) {
        int N = text.length();
        SuffixSort sa = new SuffixSort(text);
        String lrs = "";
        for (int i = 1; i < N; i++) {
            int length = sa.lcp(i);
            if (length > lrs.length()) {
                // lrs = sa.select(i).substring(0, length);
                lrs = text.substring(sa.index(i), sa.index(i) + length);
            }
        }
        return lrs;
    }

    // test client
    public static void main(String[] args) {
    	String s = StdIn.readAll().replaceAll("\\s+", " ").trim();
        SuffixSort m = new SuffixSort(s);
        m.show();
    }

}