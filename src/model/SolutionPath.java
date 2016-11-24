package model;

import java.util.ArrayList;
import java.util.Observable;

/**
 * 
 * Solutio for the shortest path problem
 *
 */
public class SolutionPath extends Observable{
	
	//Mode used. Influence the open list sorting.
	protected boolean dijkstra_mode, bfs_mode;
	protected ArrayList<Cell> path, open, closed;
	//Total cost of the solution
	private int cost;
	
	public SolutionPath(){
		path = new ArrayList<Cell>();
		open = new ArrayList<Cell>();
		closed = new ArrayList<Cell>();
		
		dijkstra_mode = false;
		bfs_mode = false;
	}
	
	/**
	 * Changes the mode of the sorting for the open list.
	 * Changes the algorithm used.
	 * @param mode
	 */
	public void setMode(String mode){
		switch(mode){
			case "A*":
				bfs_mode = dijkstra_mode = false;
				break;
			case "BFS":
				bfs_mode = true;
				dijkstra_mode = false;
				break;
			case "Dijkstra":
				bfs_mode = false;
				dijkstra_mode = true;
				break;
		}
	}
	
	public void clear(){
		path.clear();
		open.clear();
		closed.clear();
	}
	
	/**
	 * Create a solution and display it on the view (MVC used).
	 */
	public void createSolution(){
		
		cost = 0;
		
		//The last element of the closed list is the arrival.
		Cell arrival = closed.get(closed.size()-1);
		
		//We add it to the path
		path.add(arrival);
		
		//Then we loop on the parents
		Cell current = arrival;
		while(current.getParent().getParent() != null){
			closed.remove(current);
			addCellSolution(current.getParent());
			cost+=current.getType().getCost();
			current = current.getParent();
		}
		
		closed.remove(current);
		closed.remove(current.getParent());
		
		setChanged();
		notifyObservers();
	}
	
	public int getCost(){return cost;}
	
	public void addCellSolution(Cell c){
		path.add(c);
	}
	
	/**
	 * We add a cell. The method depends on the mode.
	 * @param cell
	 */
	public void addCellOpen(Cell cell){
		
		if(open.size() == 0){
			open.add(cell);
		}else{
			
			if(dijkstra_mode){
				addDijkstra(cell);
			}else if(bfs_mode){
				addBFS(cell);
			}else{
				addAStar(cell);
			}
			
		}
	}
	
	/**
	 * Dijkstra add only considering G cost.
	 * @param cell
	 */
	public void addDijkstra(Cell cell){
		
		boolean found = false;
		int i = 0;
		
		while(!found && i<open.size()){
			
			Cell curr = open.get(i);
			if(curr.getG() > cell.getG()){
				open.add(i, cell);
				found = true;
			}
			
			i++;
		}
		
		if(!found)
			open.add(cell);
		
	}
	
	/**
	 * For A*, the open list is sorted on F.
	 * @param cell
	 */
	public void addAStar(Cell cell){
		
			boolean found = false;
			int i = 0;
			
			while(!found && i<open.size()){
				
				Cell curr = open.get(i);
				if(curr.getF() > cell.getF()){
					open.add(i, cell);
					found = true;
				}
				
				i++;
			}
			
			if(!found)
				open.add(cell);
	}
	
	/**
	 * And for BFS, we check nothing, the list isn't sorted.
	 * @param cell
	 */
	public void addBFS(Cell cell){
		open.add(cell);
	}
	
	public void addCellClosed(Cell c){
		closed.add(c);
	}
	
	public ArrayList<Cell> getPath(){
		return path;
	}
	
	public ArrayList<Cell> getOpen(){
		return open;
	}
	
	public ArrayList<Cell> getClosed(){
		return closed;
	}
	
	public Cell getPath(int index){
		return path.get(index);
	}
	
	public Cell getClosed(int index){
		return closed.get(index);
	}
	
	public Cell getOpen(int index){
		return open.get(index);
	}
	
	public int sizePath(){
		return path.size();
	}
	
	public int sizeOpen(){
		return open.size();
	}
	
	public int sizeClosed(){
		return closed.size();
	}
	
}
