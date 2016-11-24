package model;

import java.io.IOException;
import java.util.ArrayList;

public class PathfindingEnv extends Environment {
	
	protected CellType cells[][];
	protected Cell arrival, beginning;
	
	public PathfindingEnv(String filepath) throws IOException, UnknownCellTypeException{
		this.fill(filepath);
		this.setAlgo();
	}
	
	public void setAlgo(){
		super.algo = new AStar(this);
	}
	
	public Cell getBeg(){
		return beginning;
	}
	
	public Cell getArr(){
		return arrival;
	}
	
	public void finalStep() throws NoPathFoundException, PathFoundException{
		((AStar) super.algo).init();
		algo.finalStep();
	}
	
	/**
	 * Return the type of the cell in the case of coordinates (x, y).
	 * @param x
	 * @param y
	 * @return
	 */
	public Colored getElement(int x, int y){
		return cells[y][x];
	}
	
	/**
	 * Get all the neighbours from the map.
	 * Max. 4, minus 2. We check the constraints.
	 * @param a
	 * @return
	 */
	public ArrayList<Cell> getNeighbors(Cell a){
		
		ArrayList<Cell> neighbors = new ArrayList<Cell>();
		
		//Left
		if(a.getX() > 0 && ((CellType) getElement(a.getX()-1, a.getY())).getCost() < Integer.MAX_VALUE)
			neighbors.add(new Cell(a.getX()-1, a.getY(), (CellType) getElement(a.getX()-1, a.getY())));
		
		//Up
		if(a.getY() > 0 && ((CellType) getElement(a.getX(), a.getY()-1)).getCost() < Integer.MAX_VALUE)
			neighbors.add(new Cell(a.getX(), a.getY()-1, (CellType) getElement(a.getX(), a.getY()-1)));
		
		//Down
		if(a.getY() < getHeight()-1 && ((CellType) getElement(a.getX(), a.getY()+1)).getCost() < Integer.MAX_VALUE)
			neighbors.add(new Cell(a.getX(), a.getY()+1, (CellType) getElement(a.getX(), a.getY()+1)));
		
		//Right
		if(a.getX() < getWidth()-1 && ((CellType) getElement(a.getX()+1, a.getY())).getCost() < Integer.MAX_VALUE)
			neighbors.add(new Cell(a.getX()+1, a.getY(), (CellType) getElement(a.getX()+1, a.getY())));
		
		

		return neighbors;
		
	}
	
	/**
	 * Parse a file into a map.
	 * @param filepath
	 * @throws IOException
	 * @throws UnknownCellTypeException
	 */
	public void fill(String filepath) throws IOException, UnknownCellTypeException{
		
		ArrayList<ArrayList<Character>> parsed = loadFile(filepath);
		
		//We create the double array with the right sizes
		cells = new CellType[parsed.size()][parsed.get(0).size()];		
		int beg = 0, arr = 0;
		int x= 0;
		
		//We go through all the characters
		for (ArrayList<Character> list : parsed) {
			
			int y = 0;
			
			for (Character chara : list) {
								
				//We transform the character into a CellType
				CellType type = CellType.getType(chara);
				
				switch(type){
				
					//We count the number of arrival and beginning. If at the end it is not 1 for each, there is a problem.
					case Beginning:
						beg++;
						cells[x][y] = type;
						beginning = new Cell(y, x, CellType.Beginning);
						break;
						
					case Arrival:
						arr++;
						cells[x][y] = type;
						arrival = new Cell(y, x, CellType.Arrival);
						break;
						
					//If it is something else, we check that the cell type is authorized for this environmeent type
					default:
						cells[x][y] = type;
				}
				
				y++;
			}
			
			x++;
		}
		
		if(arr != 1 || beg != 1){
			throw new UnknownCellTypeException("There must be only 1 beginning and arrival!");
		}
		
	}
	
	public int getWidth(){
		return cells[0].length;
	}
	
	public int getHeight(){
		return cells.length;
	}
	
}
