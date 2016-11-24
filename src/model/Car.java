package model;

import java.awt.Color;

/**
 * 
 * A car in the rush hour puzzle.
 *
 */
public class Car implements Colored{
	
	/**
	 * Every car has a number.
	 * We wanted to display it in the view, but didn't have the time to.
	 */
	private static int last_num = 0;
	
	/**
	 * All the colors cars can have.
	 */
	private final static Color[] colors = new Color[]{Color.RED, Color.BLUE, Color.CYAN, Color.DARK_GRAY, 
			Color.GREEN, Color.MAGENTA, Color.LIGHT_GRAY, Color.ORANGE, Color.PINK, Color.YELLOW, 
			new Color(76, 153, 0), new Color(204, 0, 102), new Color(204, 102, 0)};
	
	/**
	 * The color of the car.
	 */
	private Color color;
	
	/**
	 * Position and size of the car
	 */
	private int num, x, y, size;
	
	/**
	 * A boolean to know if it is horizontally or vertically.
	 */
	private boolean horizontally;
	
	public Car(int orientation, int x, int y, int size){
		this.color = colors[last_num];
		this.num = last_num++;
		
		this.horizontally = (orientation == 0);
		this.x = x;
		this.y = y;
		this.size = size;
	}
	
	/**
	 * Duplicates a car (used to generate successor states).
	 * @param car
	 */
	public Car(Car car) {
		this.color = car.color;
		this.num = car.num;
		
		this.x = car.x;
		this.y = car.y;
		this.size = car.size;
		
		this.horizontally = car.horizontally;
	}
	
	///// After that, we have getters and setters, so it is not interesting /////

	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public static void init(){ last_num = 0; }

	@Override
	public Color getColor() {
		return color;
	}
	
	public String toString(){
		return "(" + this.horizontally + "," + x + "," + y + "," + size +")";
	}
	
	public int getX(){return x;}
	public int getY(){return y;}
	public int getSize(){return size;}
	public int getNum(){return num;}
	public boolean isHorizontally(){return horizontally;}
	
}
