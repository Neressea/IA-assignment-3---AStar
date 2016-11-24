package model;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;

public class PuzzleEnv extends Environment{
	
	private AStarPuzzle algo;
	
	/**
	 * THe list of cars of the environment.
	 */
	protected Car[] cars;
	private Point exit;
	
	/**
	 * The parent is unique. If there is another previous equals state which has another parent,
	 * then we only change the parent if the F cost is lower.
	 */
	PuzzleEnv parent;
	protected double h, g, f;
	protected String ID;
	protected ArrayList<PuzzleEnv> kids;

	public PuzzleEnv(String filepath) throws IOException {
		
		ArrayList<ArrayList<Character>> parsed = loadFile(filepath);
		exit = new Point(5, 2);
		
		Car.init();
		
		//We create the array with the right sizes
		cars = new Car[parsed.size()];
		
		//We fill it
		for (int i = 0; i < parsed.size(); i++) {
			ArrayList<Character> line = parsed.get(i);
			cars[i] = new Car(Integer.parseInt(line.get(0).toString()), Integer.parseInt(line.get(1).toString()), 
					Integer.parseInt(line.get(2).toString()), Integer.parseInt(line.get(3).toString()));
		}
		
		kids = new ArrayList<PuzzleEnv>();
		
		this.computeID();
		this.setAlgo();
	}
	
	public AStarPuzzle getAlgo(){
		return algo;
	}
	
	public String diff(PuzzleEnv parent){
		
		for (int i = 0; i < cars.length; i++) {
			//There can bew max one move between a father and a child, so we get out when we found one.
			if(cars[i].getX() != parent.cars[i].getX() || cars[i].getY() != parent.cars[i].getY()){
				return "(" + parent.cars[i].getX() + ","+ parent.cars[i].getY() + ") ==> (" + this.cars[i].getX() + ", " + this.cars[i].getY() +")\n";
			}
		}
		
		return "";
	}
	
	public void setAlgo(){
		algo = new AStarPuzzle(this);
	}
	
	public void finalStep() throws NoPathFoundException, PathFoundException{
		algo.finalStep();
	}
	
	public PuzzleEnv() {
		kids = new ArrayList<PuzzleEnv>();
	}

	/**
	 * Returns true if the red car is near the exit.
	 * In any other case, returns false.
	 * 
	 * @return
	 */
	public boolean isComplete(){
		//If the right part is on 4, the left is on 5 and the car can go out.
		return cars[0].getX() == 4;
	}
	
	/**
	 * Compute the ID of the current step.
	 * It must be unique, so we can know which states are identical.
	 * We get the relevant position (i.e. the position that can change depending on their direction, horizontal or vertical) of all cars
	 * and we create a string. In every step the order of the car is the same.
	 * 
	 * @return
	 */
	public String computeID(){
		
		this.ID = "";
		
		for (int i = 0; i < cars.length; i++) {
			
			Car c = cars[i];
			int index = 0;
			
			//We get the relevant position, i.e. the position that can move.
			if(c.isHorizontally())
				index = c.getX();
			else
				index = c.getY();
			
			this.ID += index;
			
		}
		
		return this.ID;
	}
	
	/**
	 * The heuristic used. We just compute the number of cars on the way of the red car.
	 * @return
	 */
	public double heuristic(){
		
		double h = 0;
		
		for (int i = 0; i < 6; i++) {
			if(getCar(i, 2) != null && getCar(i, 2).getNum() != 0)
				h+=1;
		}
		
		if(h != 0) h++;
		
		return h;
	}
	
	/**
	 * Create all possible successors if the current state.
	 * @return
	 */
	public ArrayList<PuzzleEnv> getSuccessors(){
		
		ArrayList<PuzzleEnv> successors = new ArrayList<PuzzleEnv>();
		
		//For each car, we check if there is a move possible. And for each possible move, we create a child.
		for (int i = 0; i < cars.length; i++) {
			Car c = cars[i];
			
			//If the car is horizontal, we check if it can be moved by one case on the left or on the right.
			//We can create two distinct state for this case: 
			//	- one in which we move the car on the left
			//	- one in which we move the car on the right
			if(c.isHorizontally()){
				
				//It must be inferior to the limit of the map (we must also consider its size) on the left
				if(this.getCar(c.getX() + c.getSize(), c.getY()) == null && c.getX() + c.getSize() < 6){
					PuzzleEnv copy = this.duplicate(); //W e create a new fresh state based on this one
					copy.cars[i].setX(c.getX() + 1); //We ove the corresponding car
					
					//We update all costs: G gains 1 move.
					copy.g = this.g + 1;
					copy.h = copy.heuristic();
					copy.f = copy.g + copy.h;
					
					//We compute the ID of this state. If it was already generated, it will be igniored later on.
					copy.computeID();
					successors.add(copy);
				}
				
				//And it also must be superior or equals to 0 on the right
				if(this.getCar(c.getX() - 1, c.getY()) == null && c.getX() -1 >= 0){
					PuzzleEnv copy = this.duplicate();
					copy.cars[i].setX(c.getX() - 1);
					
					copy.g = this.g + 1;
					copy.h = copy.heuristic();
					copy.f = copy.g + copy.h;
					
					copy.computeID();
					successors.add(copy);
				}
				
			}else{
				
				//If the car is vertical, we check if it can be moved by up or down.
				//We can create two distinct state for this case: 
				//	- one in which we move the car up
				//	- one in which we move the car down
				
				//It must be inferior to the limit of the map (we must also consider its size) dpwn
				if(this.getCar(c.getX(), c.getY() + c.getSize()) == null && c.getY() + c.getSize() < 6){
					PuzzleEnv copy = this.duplicate();
					copy.cars[i].setY(c.getY() + 1);
					
					copy.g = this.g + 1;
					copy.h = copy.heuristic();
					copy.f = copy.g + copy.h;
					
					copy.computeID();
					successors.add(copy);
				}
				
				//And it also must be superior or equals to 0 up
				if(this.getCar(c.getX(), c.getY() - 1) == null && c.getY() - 1 >= 0){
					PuzzleEnv copy = this.duplicate();
					copy.cars[i].setY(c.getY() - 1);
					
					copy.g = this.g + 1;
					copy.h = copy.heuristic();
					copy.f = copy.g + copy.h;
					
					copy.computeID();
					successors.add(copy);
				}
				
			}
		}
		
		return successors;
	}
	
	/**
	 * Creates a deep clone of the environment. Used to generates successors.
	 * @return
	 */
	public PuzzleEnv duplicate(){
		PuzzleEnv env = new PuzzleEnv();
		env.parent = this;
		env.exit = this.exit;
		
		env.cars = new Car[this.cars.length];
		
		for (int i = 0; i < this.cars.length; i++) {
			env.cars[i] = new Car(this.cars[i]);
		}
		
		return env;
	}
	
	public Point getExit(){
		return exit;
	}

	@Override
	public int getHeight() {
		return 6;
	}

	@Override
	public int getWidth() {
		return 6;
	}
	
	/**
	 * Get the car on the case (x,y).
	 * If it is empty, return null.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public Car getCar(int x, int y){
		
		for (int i = 0; i < cars.length; i++) {
			
			Car c = cars[i];
			int w = (c.isHorizontally()) ? c.getSize()-1 : 0;
			
			if(x >= c.getX() && x <= c.getX()+w){
				
				int h = (!c.isHorizontally()) ? c.getSize()-1 : 0;
				
				if(y >= c.getY() && y <= c.getY()+h){
					return c;
				}
			}
		}
		
		return null;
	}

	@Override
	public Colored getElement(int x, int y) {
		Car car;
		return ((car = getCar(x, y)) != null) ? car : CellType.Empty;
	}
	
	@Override
	public boolean equals(Object obj) {
		return ((PuzzleEnv) obj).ID == this.ID;
	}

}
