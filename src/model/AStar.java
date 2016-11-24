package model;

import java.util.ArrayList;

/**
 * 
 * AStar algorithm for the shortest path problem. Is also used for BFS and Dijkstra (the heuristic used is just changed).
 * The algo for the puzzle is named AStarPuzzle and is slighty different, because the models used are different.
 *
 */
public class AStar implements Algorithm{
	
	/**
	 * Represents the solution of the problem.
	 * Is constructed at the end of the execution.
	 */
	private SolutionPath path;
	
	/**
	 * The environment on which the algo is executed.
	 */
	private PathfindingEnv environment;
	
	public AStar(PathfindingEnv env) {
		
		//We create a nice, empty solution
		path = new SolutionPath();
		environment = env;
		
		//We set the parameters of the beginning: no G and the initial H, i.e. euclidian distance between beginning and arrival.
		Cell beginning = environment.getBeg();
		beginning.setH(heuristic(beginning, environment.getArr()));
		beginning.setG(0);
		
		//We add the first node to the open list.
		path.addCellOpen(beginning);
	}
	
	/**
	 * This function can switch the mode of the algo. 
	 * We can switch between AStar, BFS and Dijkstra.
	 * @param mode A*, BFS or Dijkstra
	 */
	public void setMode(String mode){
		path.setMode(mode);
	}
	
	/**
	 * Initialize the algo if it was already used.
	 */
	public void init(){
		
		//We clear the solution, i.e. we empty all its lists.
		path.clear();
		
		//We parameter the beg point on more time.
		Cell beginning = environment.getBeg();
		beginning.setH(heuristic(beginning, environment.getArr()));
		beginning.setG(0);
		
		//And we add it to the open list.
		path.addCellOpen(beginning);
	}
	
	
	/**
	 * Loop to the final state, whether a solution is found or not.
	 * In the two cases, an exception is thrown.
	 */
	@Override
	public void finalStep() throws NoPathFoundException, PathFoundException {
				
		//We loop on next while there is no exception (if the arrival is found or of there is no path).
		while(true){
			
			nextStep();

		}
		
	}

	/**
	 * During a step, the best node of the open list is explored: put in the closed list, andits child are put 
	 * in the open list after we computed their G and H parameters.
	 */
	@Override
	public void nextStep() throws NoPathFoundException, PathFoundException{
		
		//If we have no node in the open list, it is the end: we failed.
		if(path.open.size() == 0) 
			throw new NoPathFoundException("No path was found.");
		
		//The list is sorted, and the best one is always the first.
		//We remove it from the open list and we add it to the closed.
		Cell x = path.open.remove(0);
		path.closed.add(x);
		
		//We check if we are at the arrival.
		if(x.equals(environment.arrival)){
			
			//If it is the case, we build the solution by recursing on parents node and we display it.
			path.createSolution();
			
			//A little exception to get out of there.
			throw new PathFoundException("A path was found.");
		}
		
		//If we don't have the solution and we still have nodes in the open list, we continue our search.
		
		//We get all neighbors of the current node.
		ArrayList<Cell> succs = environment.getNeighbors(x);
		
		for (int i = 0; i < succs.size(); i++) {
			
			Cell s = succs.get(i);
			
			//If the child is not in the closed list nor in the open, we set up its parent and add it to the open.
			if(!path.closed.contains(s) && !path.open.contains(s)){
				
				//We set the parent and compute G and H for the child.
				attachAndEval(s, x);
				
				//We put it in the open list, so we can explore it later on. 
				//During this operation, we set the node at the right place so the list stay sorted on F cost.
				path.addCellOpen(s);
				
			}else if(x.getG() + s.getType().getCost() < s.getG()){
				
				//If it's in one of the previous list, and if the G cost is now better with this parent, we update the situation.
				attachAndEval(s, x);
				
				if(path.closed.contains(s)){
					//In addition if we already explored the node, we also update its children.
					propagatePathImprovement(s);
				}
				
			}
			
			//We also change the parent status.
			if(s.getParent() == null || !s.getParent().equals(x)){
				x.addKid(s);
			}
		}
	}
	
	/**
	 * Set the parent of the node, and compute G and H costs.
	 * @param c Child
	 * @param p Parent
	 */
	public void attachAndEval(Cell c, Cell p){
		
		c.setParent(p);
		c.setG(p.getG() + c.getType().getCost());
		c.setH(heuristic(c, environment.getArr()));
		
		//We don't need to compute F, because it is always computed based on G and H in getF().
		
	}
	
	/**
	 * Update the children of the node if it was already explored and updated.
	 * @param p The node whose nodes we need to update.
	 */
	public void propagatePathImprovement(Cell p){
		
		//We go through all children
		for(Cell c : p.getKids()){
			
			//If the new situation is better than the last one, we update it.
			if(p.getG() + c.getType().getCost() < c.getG()){
				
				c.setParent(p);
				c.setG(p.getG() + c.getType().getCost());
				
				//H cost doesn't depend on the parent, so we don't need to compute it again.
				//In addition, F cost is automatically computed, so we don't have to change anything else.
				
				//We recurse on the child's children
				propagatePathImprovement(c);
			}
		}
		
	}
	
	public SolutionPath getPath(){
		return path;
	}
	
	/**
	 * The heuristic is always euclidian distance between two nodes, for H cost.
	 * The algo mode impacts the SolutionPath object, not the heuristic.
	 * 
	 * If we are in Dijkstra mode, only G will be used and heuristic will be ignored.
	 * If we are in BFS, both will be ignored.
	 * 
	 * @param a Cell 1
	 * @param b Cell 2
	 * @return
	 */
	public double heuristic(Cell a, Cell b){
		return euclidianDistance(a, b);
	}
	
	/**
	 * Compute the euclidian distance between two nodes.
	 * @param a
	 * @param b
	 * @return
	 */
	public double euclidianDistance(Cell a, Cell b){
		return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
	}

}
