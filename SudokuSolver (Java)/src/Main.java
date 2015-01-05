import java.io.IOException;

/**
 * @author rosspa
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SudokuSolver solver;
		try {
			solver = new SudokuSolver("src/1.txt");
			System.out.println();
			solver.solve();
			System.out.println("done");
			System.out.println(solver.output());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
