import java.util.ArrayList;
import java.util.Arrays;

public class AbstractStructure {
	protected ArrayList<Unsolved> unsolved;
	protected int id;
	protected String type = "none";
	protected ArrayList<Integer> remainingPossibilities;
	private static int count = 0;
	// TODO add reference to sudoku solver so i can print out board after solving thing
	private SudokuSolver solver;

	public AbstractStructure(SudokuSolver solver) {
		this.solver = solver;
		count++;
		this.id = count;
		this.unsolved = new ArrayList<Unsolved>();
		this.remainingPossibilities = new ArrayList<Integer>();
		this.remainingPossibilities.addAll(Arrays.asList(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }));
	}

	protected ArrayList<Unsolved> getUnsolved() {
		return this.unsolved;
	}

	public void addUnsolved(Unsolved u) {
		this.unsolved.add(u);
	}

	public void remove(Unsolved u, int numb) {
		this.unsolved.remove(u);
		this.solver.removeUnsolved(u);
		this.removePossiblities(numb);
	}

	public void removePossiblities(int numb) {
		this.remainingPossibilities.remove((Integer) numb);
		for (Unsolved u : this.unsolved) {
			u.removePossibities(numb);
		}
	}
	
	public ArrayList<Integer> getRemainingPossibilities() {
		return this.remainingPossibilities;
	}

	/**
	 * solves as much of the structure as possible
	 * 
	 * @return true if it found a solution and false if no possible solutions could be found
	 */
	public boolean solve() {
		boolean foundASolution = false;
		// check all remaining possibilities
		// possibilityLoop: for (Integer possibility : this.remainingPossibilities) {
		possibilityLoop: for (int k = 0; k < this.remainingPossibilities.size(); k++) {
			int possibility = this.remainingPossibilities.get(k);
			Unsolved onlyPossibility = null;
			// check if there is only 1 possible spot for this possibility
			for (Unsolved unsolved : this.unsolved) {
				if (unsolved.checkPossibility(possibility)) {
					if (onlyPossibility != null) {
						// more then 1 possibility
						// next possibility
						continue possibilityLoop;
					}
					// first possibility so far
					onlyPossibility = unsolved;
				}
			}
			// there is only 1 possible spot for this possibility
			if (onlyPossibility == null) {
				System.err.println(possibility + " is not a valid possibility for " + this.type + " " + this.id
						+ "\nwith unsolved " + this.unsolved.toString());
				throw new RuntimeException(possibility + " is not a valid possibility for " + this.type + " " + this.id
						+ "\nwith unsolved " + this.unsolved.toString());
			}
			onlyPossibility.remove(possibility);
			k--;
			foundASolution = true;
			this.solver.markNumber(possibility, onlyPossibility.getX(), onlyPossibility.getY());
			System.out.println(this.solver.output());
			int a = 1;
		}
		return foundASolution;
	}

	@Override
	public String toString() {
		return this.id + ", " + this.type + ", unsolved = " + this.unsolved.toString();
	}

}
