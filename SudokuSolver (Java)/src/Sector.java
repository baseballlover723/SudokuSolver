import java.util.ArrayList;

public class Sector extends AbstractStructure{
	private int minX;
	private int maxX;
	private int minY;
	private int maxY;


	public Sector(SudokuSolver solver, int minX, int maxX, int minY, int maxY) {
		super(solver);
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.type = "sector";
	}


}
