import java.util.Comparator;

public class UnsolvedComp implements  Comparator<Unsolved> {
	@Override
	public int compare(Unsolved u1, Unsolved u2) {
		return Integer.compare(u1.getNumbPossible(), u2.getNumbPossible());
	}

}
