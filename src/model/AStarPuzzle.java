package model;

import java.util.ArrayList;
import java.util.HashMap;

import view.UIView;

/**
 * 
 * The algorithm for the puzzle problem.
 * We didn't use the same models for the two problems, and because we didn't
 * know at the beginning how we would modelize the puzzle prob, we had to make another algo.
 *
 */
public class AStarPuzzle implements Algorithm{
	
	/**
	 * The open nodes. We can explore them. The list is sorted on F cost.
	 */
	ArrayList<PuzzleEnv> open;
	
	/**
	 * The closed nodes. We already explored them.
	 */
	ArrayList<PuzzleEnv> closed;
	
	/**
	 * Already generated nodes. The string is a unique identifier. If two nodes has the same string, they are exactly the same.
	 * We don't recreate nodes we already created. This force the model to behave like a graph, and not a tree.
	 * It avoids infinite loop on similar states.
	 */
	HashMap<String, PuzzleEnv> generated;
	
	public AStarPuzzle(PuzzleEnv env){
		
		generated = new HashMap<String, PuzzleEnv>();
		open = new ArrayList<PuzzleEnv>();
		closed = new ArrayList<PuzzleEnv>();
		
		// The first environment is the first node of the graph.
		env.g = 0;
		env.h = env.heuristic();
		env.f = env.h;
		
		//We update the list
		generated.put(env.computeID(), env);
		open.add(env);
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
	 * During a step, the best node of the open list is explored: put in the closed list, and its children are put 
	 * in the open list after we computed their G and H parameters.
	 * 
	 * Children are computed at the beginning of the loop, and depending on the current state.
	 */
	@Override
	public void nextStep() throws NoPathFoundException, PathFoundException{
		
		//If we have no node in the open list, it is the end: we failed.
		if(open.size() == 0) 
			throw new NoPathFoundException("No path was found.");
		
		//The list is sorted, and the best one is always the first.
		//We remove it from the open list and we add it to the closed.
		PuzzleEnv x = open.remove(0);
		closed.add(x);
		
		//We check if the current state is complete, i.e. if we can go to the exit.
		if(x.isComplete()){
			throw new PathFoundException(this.buildSolution(x));
		}
		
		//We compute all successors of the current state.
		//A successor has one and only one different move done compared to the parent.
		ArrayList<PuzzleEnv> succs = x.getSuccessors();
		
		for (int i = 0; i < succs.size(); i++) {
			PuzzleEnv s = succs.get(i);
			
			//If we already computed this node, we don't check it again.
			if(generated.containsKey(s.computeID())){
				continue;
			}else{
				//If it is the first time, we put it in the generated map.
				generated.put(s.computeID(), s);
			}
			
			//We update the parent's kids.
			if(s.parent == null || !s.parent.equals(x)){
				x.kids.add(s);
			}
			
			//If the child is not in the closed list nor in the open, we set up its parent and add it to the open.
			if(!open.contains(s) && !closed.contains(s)){
				
				//We set the parent and compute G and H for the child.
				attachAndEval(s, x);
				
				//We put it in the open list, so we can explore it later on. 
				//During this operation, we set the node at the right place so the list stay sorted on F cost.
				addOpen(s);
				
			}else if(x.g + 1 < s.g){ //There is always one more move between a child and its parent, so we add 1 to the cost
				
				//If it's in one of the previous list, and if the G cost is now better with this parent, we update the situation.
				attachAndEval(s, x);
				
				if(closed.contains(s)){
					//In addition if we already explored the node, we also update its children.
					propagatePathImprovement(s);
				}
				
			}
		}
	}
	
	/**
	 * Build all the steps to resolve the problem.
	 * @param x The solved environment.
	 * @return
	 */
	public String buildSolution(PuzzleEnv x){
		
		PuzzleEnv current = x;
		ArrayList<String> s = new ArrayList<String>();
		int cost = 0;
		
		/**
		 * We go through all the parents. We know that the original node don't have any, so we are sure this loop will stop.
		 */
		while(current.parent != null) {
			
			//We compute the difference there is between the child and its parent, and determine the move that has be done.
			//Then we add it to the list.
			s.add(current.diff(current.parent));
			
			//We recurse on the parent.
			current = current.parent;
			
			//And each step is one move, so we update the total cost
			cost++;
		}
		
		String sol = "";
		
		//At the end, we reverse all the steps (because we started whith the last node, but we want to know the steps in the correct order.
		for (int i = s.size()-1; i >= 0; i--) {
			sol+=s.get(i);
		}
		
		//And finally we set the cost in the view and we add it at the end of the steps string.
		sol+="\nCost: " + cost+" moves";
		UIView.getInstance().setCost(cost);
		
		return sol;
	}
	
	/**
	 * Add a node to the open list. The list is sorted by F cost.
	 * The sort is ascending.
	 * @param env
	 */
	public void addOpen(PuzzleEnv env){
		
		//A boolean to be sure that the node is inserted.
		boolean inserted = false;
		int i = 0;
		
		//We stop when we inserted the node or when we are at the end of the list.
		//We can go through the whole list and not insert node if it is the worst.
		while(!inserted && i < open.size()){
			
			//We check if the the current node is worst than the one we insert
			if(env.f < open.get(i).f){
				//If it's the case, we put the new node at this position and shift the rest on the left. After that we go out.
				open.add(i, env);
				return;
			}
			
			i++;
		}
		
		//if the node is not inserted, it is worst than current the worst node of the list.
		//So we add it at the end of the list.
		if(!inserted)
			open.add(env);
		
	}
	
	/**
	 * Set the parent of the node, and compute G and H costs.
	 * @param c Child
	 * @param p Parent
	 */
	public void attachAndEval(PuzzleEnv c, PuzzleEnv p){
		
		c.parent = p;
		c.g = p.g + 1;
		c.h = c.heuristic();
		c.f = c.g + c.h;
		
	}
	
	/**
	 * Update the children of the node if it was already explored and updated.
	 * @param p The node whose nodes we need to update.
	 */
	public void propagatePathImprovement(PuzzleEnv p){
		
		//We go through all children
		for(PuzzleEnv c : p.kids){
			
			//If the new situation is better than the last one, we update it.
			if(p.g + 1 < c.g){
				
				c.parent = p;
				c.g = p.g + 1; //We add 1 move to the G cost, because we know a child has one more move than its parent.
				c.f = c.g + c.h;
				
				//We recurse
				propagatePathImprovement(c);
			}
		}
		
	}

}
