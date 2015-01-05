public class Unsolved {

	private int x;
	private int y;
	private boolean[] possible;
	private int numbPossible;
	private Sector sector;
	private Row row;
	private Column column;

	public Unsolved(int x, int y, Sector sector, Row row, Column column) {
		this.x = x;
		this.y = y;
		this.possible = new boolean[] { true, true, true, true, true, true, true, true, true };
		this.numbPossible = 9;
		this.sector = sector;
		this.row = row;
		this.column = column;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}
	
	public Sector getSector() {
		return this.sector;
	}

	public boolean inRowOrColumnOrSector(int x, int y, Sector sector) {
		return x == this.x || y == this.y || sector == this.sector;
	}

	public int getNumbPossible() {
		return this.numbPossible;
	}

	public boolean remove(int numb) {
		if (this.possible[numb - 1]) {
			this.possible[numb - 1] = false;
			this.numbPossible--;
			this.sector.remove(this, numb);
			this.row.remove(this, numb);
			this.column.remove(this, numb);
			return true;
		}
		return false;
	}

	public boolean removePossibities(int numb) {
		if (this.possible[numb - 1]) {
			this.possible[numb - 1] = false;
			this.numbPossible--;
			return true;
		}
		return false;
	}
	
	public boolean checkPossibility(int numb) {
		return this.possible[numb-1];
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int k = 0; k < 9; k++) {
			if (this.possible[k]) {
				sb.append(k + 1);
			}
		}
		return "x = " + (this.x + 1) + ", y = " + (this.y + 1) + "; possible numbers: " + sb.toString();
	}

	public int getLastNumb() {
		int numb = 0;
		for (int k = 0; k < 9; k++) {
			if (this.possible[k]) {
				if (numb != 0) {
					System.out.println("too many numbers");
					return 0;
				}
				numb = k+1;
			}
		}
		return numb;

	}
}