package model;

import java.util.ArrayList;

/**
 * 
 * A cell in the shortest path problem.
 *
 */
public class Cell {
	
	/**
	 * The position of the cell
	 */
	private int x, y;
	
	/**
	 * Its H and G costs.
	 */
	private double g, h;
	
	/**
	 * Its parent.
	 */
	private Cell parent;
	
	/**
	 * Its type, i.e. road, forest, ...
	 * Used to compute costs and for the display.
	 */
	private CellType type;
	
	/**
	 * Its kids.
	 */
	private ArrayList<Cell> kids;
	
	public Cell(int x, int y, CellType type){
		this.x = x;
		this.y = y;
		this.g = Integer.MAX_VALUE;
		this.h = Integer.MAX_VALUE;
		this.type = type;
		this.kids = new ArrayList<Cell>();
	}
	
	/**
	 * A cell is equals to another one if their position is the same.
	 * (Costs can be updated)
	 */
	@Override
	public boolean equals(Object o){
		Cell c = (Cell) o;
		return this.x == c.x && this.y == c.y;
	}
	
	//// Setters and getters after that /////
	
	public CellType getType(){
		return type;
	}
	
	public void setParent(Cell c){
		parent=c;
	}
	
	public boolean isKid(Cell c){
		return kids.contains(c);
	}
	
	public void addKid(Cell c){
		kids.add(c);
	}
	
	public Cell getKid(Cell c){
		return kids.get(kids.indexOf(c));
	}
	
	public ArrayList<Cell> getKids(){return kids;}
	public Cell getKid(int i){return kids.get(i);}
	public Cell getParent(){return parent;}
	
	//F is automatically computed
	public void setH(double h){this.h=h;}
	public void setG(double g){this.g=g;}
	
	public double getF(){return h+g;}
	public double getH(){return h;}
	public double getG(){return g;}
	
	public int getX(){return x;}
	public int getY(){return y;}
	
	public String toString(){
		return "(" + x + "," + y + ")" + " c = " + getF();
	}
}
