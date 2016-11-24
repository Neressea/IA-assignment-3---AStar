package model;

import java.awt.Color;

/**
 * 
 * This enumeration defines all the possible types of cells for the pathfinding problem.
 *
 */
public enum CellType implements Colored{
	
	//Costs for each kind of cells. For the basic problem, it 1 or MAX_VALUE (for obstacle, so we are sure to never pass by this one).
	Beginning(0, Color.CYAN), Arrival(1, Color.RED), Obstacle(Integer.MAX_VALUE, Color.GRAY), Empty(1, Color.WHITE), Water(100, Color.BLUE), 
	Mountains(50, Color.LIGHT_GRAY), Forests(10, new Color(0, 204, 0)), Grasslands(5, new Color(153, 255, 153)), Roads(1, new Color(204, 102, 0));
	
	/**
	 * Cost of the cell type.
	 */
	private int cost;
	
	/**
	 * Its associated color.
	 */
	private Color color;
	
	/**
	 * Translate a character into a cell type.
	 * Used during the parsing of a file.
	 * 
	 * @param c
	 * @return
	 * @throws UnknownCellTypeException If the character isn't authorized.
	 */
	public static CellType getType(Character c) throws UnknownCellTypeException{
		
		switch(c){
			case 'A':
				return Arrival;
			case 'B':
				return Beginning;
			case '.':
				return Empty; 
			case '#':
				return Obstacle;
			case 'g':
				return Grasslands;
			case 'w':
				return Water;
			case 'r':
				return Roads;
			case 'm':
				return Mountains;
			case 'f':
				return Forests;
			default:
				throw new UnknownCellTypeException("The file contains unknow characters : " + c);
		}
	}
	
	CellType(int cost, Color color){
		this.cost = cost;
		this.color = color;
	}
	
	public int getCost(){
		return this.cost;
	}
	
	public Color getColor(){
		return this.color;
	}
	
}
