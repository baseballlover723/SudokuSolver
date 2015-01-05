import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * @author rosspa
 *
 */

// DONE initialize, abstract data structure, adding new solved squares,
// TODO fix solving (not sure), add solving by sector, row, column
public class SudokuSolver {
	private int[][] board;
	private PriorityQueue<Unsolved> unsolved;
	private ArrayList<Sector> sectors;
	private ArrayList<Row> rows;
	private ArrayList<Column> columns;
	private static final UnsolvedComp comp = new UnsolvedComp();

	public SudokuSolver(String string) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(new File(string)));
		this.board = new int[9][9];
		this.unsolved = new PriorityQueue<Unsolved>(10, comp);
		this.sectors = new ArrayList<Sector>();
		for (int bigY = 0; bigY < 3; bigY++) {
			for (int bigX = 0; bigX < 3; bigX++) {
				this.sectors.add(new Sector(this, bigX * 3, bigX * 3 + 3, bigY * 3, bigY * 3 + 3));
			}
		}
		this.rows = new ArrayList<Row>();
		for (int y = 0; y < 9; y++) {
			this.rows.add(new Row(this, y));
		}
		this.columns = new ArrayList<Column>();
		for (int x = 0; x < 9; x++) {
			this.columns.add(new Column(this, x));
		}
		// reads txt file
		for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 9; x++) {
				int numb = reader.read();
				// parses periods
				if (numb == 46) {
					Sector sector = this.getSector(x, y);
					Row row = this.rows.get(y);
					Column column = this.columns.get(x);

					// adds to queue of unsolved squares
					Unsolved u = new Unsolved(x, y, sector, row, column);
					this.unsolved.offer(u);
					// adds into different board data structures
					sector.addUnsolved(u);
					row.addUnsolved(u);
					column.addUnsolved(u);

				} else {
					// sets the board to the given number
					this.board[x][y] = numb - 48;
				}
			}
			// parse new lines
			reader.read();
			reader.read();
		}
		reader.close();
		System.out.println(this.toString());
		for (Sector sector : this.sectors) {
			System.out.println(sector);
		}
		this.initialPass();
		System.out.println(this.outputUnsolved());
		System.out.println(this.unsolved.peek().toString());
		System.out.println(this);
//		System.exit(0);
	}

	private void initialPass() {
		for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 9; x++) {
				if (this.board[x][y] != 0) {
					int givenNumber = this.board[x][y];
					// this.accountForGiven(x, y, givenNumber);
					this.getSector(x, y).removePossiblities(givenNumber);
					this.rows.get(y).removePossiblities(givenNumber);
					this.columns.get(x).removePossiblities(givenNumber);
				}
			}
		}
		this.refreshPQ();
		System.out.println(this.unsolved.toString());

	}

	private void accountForGiven(int x, int y, int numb) {
		for (Unsolved square : this.unsolved) {
			String str = square.toString();
			if (square.inRowOrColumnOrSector(x, y, this.getSector(x, y))) {
				square.removePossibities(numb);
			}
		}

	}

	private void newSolvedNumber(Unsolved solvedSquare, int newNumb) {
		Iterator<Unsolved> itr = this.unsolved.iterator();
		while (itr.hasNext()) {
			Unsolved square = itr.next();
			if (square == solvedSquare) {
				itr.remove();
				solvedSquare.remove(newNumb);
				return;
			}
		}
	}

	private Sector getSector(int x, int y) {
		int numb = 1;
		numb += 3 * (y / 3);
		numb += x / 3;
		return this.sectors.get(numb - 1);
	}

	public void refreshPQ() {
		Unsolved[] temp = new Unsolved[this.unsolved.size()];
		this.unsolved.toArray(temp);
		this.unsolved = new PriorityQueue<Unsolved>(temp.length, comp);
		this.unsolved.addAll(Arrays.asList(temp));
	}

	public void solve() {
		while (!this.unsolved.isEmpty()) {
			while (this.unsolved.peek().getNumbPossible() == 1) {
				Unsolved u = this.unsolved.poll();
				int x = u.getX();
				int y = u.getY();
				int newNumb = u.getLastNumb();
				System.out.println("x = " + (x + 1) + ", y = " + (y + 1) + " = " + this.board[x][y]);
				this.board[x][y] = newNumb;
				this.newSolvedNumber(u, newNumb);
				System.out.println(" = " + this.board[x][y]);
				System.out.println(this.output());
				System.out.println();
			}
			this.refreshPQ();
			if (this.unsolved.peek().getNumbPossible() == 1) {
				// if there is at least 1 unsolved space that has only 1 possible numb to be placed there
				continue;
			}
			System.out.println("solve by sector");
			boolean boo1 = this.solveBySector();
			boolean boo2 = this.solveByRow();
			boolean boo3 = this.solveByColumn();

			if (!(boo1 || boo2 || boo3)) {
				System.out.println("couldn't solve");
				System.out.println(this.unsolved);
				break;
			}
		}
		System.out.println("end");
		System.out.println(this.outputUnsolved());
	}

	/**
	 * solves the puzzle by looking at 3 by 3 sectors
	 */
	private boolean solveBySector() {
		boolean changed = false;
		for (Sector sector : this.sectors) {
			if (sector.getUnsolved().isEmpty()) {
				continue;
			}
			changed = sector.solve() ? true : changed;
		}
		return changed;
	}

	private boolean solveByRow() {
		boolean changed = false;
		for (Row row : this.rows) {
			if (row.getUnsolved().isEmpty()) {
				continue;
			}
			changed = row.solve() ? true: changed;
		}
		return changed;
	}

	private boolean solveByColumn() {
		boolean changed = false;
		for (Column column : this.columns) {
			if (column.getUnsolved().isEmpty()) {
				continue;
			}
			changed = column.solve() ? true : changed;
		}
		return changed;
	}

	public boolean removeUnsolved(Unsolved u) {
		return this.unsolved.remove(u);
	}
	/**
	 * only marks the given number at the given 0 based coordinate
	 * 
	 * @param numb
	 * @param x
	 * @param y
	 */
	public void markNumber(int numb, int x, int y) {
		this.board[x][y] = numb;
	}

	public String output() {
		return "start output\n" + this.toString();
	}

	public String outputUnsolved() {
		StringBuilder sb = new StringBuilder();
		sb.append('\n');
		for (Unsolved square : this.unsolved) {
			sb.append(square.toString());
			sb.append('\n');
		}
		return sb.toString();

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int y = 0; y < 9; y++) {
			if (y == 3 || y == 6) {
				sb.append("---+---+---\n");
			}
			for (int x = 0; x < 9; x++) {
				if (x == 3 || x == 6) {
					sb.append('|');
				}
				sb.append(this.board[x][y]);
			}
			sb.append("\n");
		}
		return sb.toString();
	}

}
