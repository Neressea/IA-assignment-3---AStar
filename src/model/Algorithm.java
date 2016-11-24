package model;

/**
 * 
 * Interface designing the basic methods of all algorithms in the project.
 *
 */
public interface Algorithm {

	/**
	 * Execute the next step of the algorithm.
	 * During a step, a node is chosen in the open list, put in the closed list and its child are put
	 * in the open list.
	 * 
	 * @throws NoPathFoundException If the open list is empty
	 * @throws PathFoundException If we found the solution, to get out of the loop.
	 */
	public void nextStep() throws NoPathFoundException, PathFoundException;
	
	/**
	 * Execute a infinite loop calling nextStep().
	 * Is stopped when an exception is throwed, whether a solution is found or not.
	 * 
	 * @throws NoPathFoundException
	 * @throws PathFoundException
	 */
	public void finalStep() throws NoPathFoundException, PathFoundException;
	
}
