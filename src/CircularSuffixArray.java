
public class CircularSuffixArray{
	private SuffixSort sa;
	public CircularSuffixArray(String s)
	{
		sa = new SuffixSort(s);

	}
	// return index of ith sorted suffix
	public int index(int i) {
		return sa.index(i);
	}

	public int length() {
		return sa.N;
	}
	
}