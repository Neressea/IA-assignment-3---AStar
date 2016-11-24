package view;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import model.Cell;
import model.SolutionPath;

public class PathView implements Observer{
	
	/**
	 * For the path
	 */
	protected ArrayList<Rectangle> rects;
	
	/**
	 * For the closed nodes
	 */
	protected ArrayList<PositionnedLabel> crosses;
	
	/**
	 * For the open nodes
	 */
	protected ArrayList<PositionnedLabel> stars;
		
	public PathView(){
		rects = new ArrayList<Rectangle>();
		crosses = new ArrayList<PositionnedLabel>();
		stars = new ArrayList<PositionnedLabel>();
	}
	
	@Override
	/**
	 * We get all data: the shortest path, the opened nodes and the closed nodes.
	 * SimplePathView and ComplexPathView will manage the display.
	 */
	public void update(Observable o, Object arg) {
		
		SolutionPath solution = (SolutionPath) o;
		int w = EnvironmentView.case_width;
		int h = EnvironmentView.case_height;
		
		rects = new ArrayList<Rectangle>();
		crosses = new ArrayList<PositionnedLabel>();
		stars = new ArrayList<PositionnedLabel>();
		
		for (int i = 0; i < solution.sizePath(); i++) {
			Cell c = solution.getPath(i);
			Rectangle r = new Rectangle(c.getX() * w + w/4, c.getY() * h + h/4, w / 2, h / 2);
			rects.add(r);
		}
		
		for (int i = 0; i < solution.sizeClosed(); i++) {
			Cell c = solution.getClosed(i);
			PositionnedLabel pl = new PositionnedLabel("x", c.getX() * w, c.getY() * h);
			crosses.add(pl);
		}
		
		for (int i = 0; i < solution.sizeOpen(); i++) {
			Cell c = solution.getOpen(i);
			PositionnedLabel pl = new PositionnedLabel("*", c.getX() * w, c.getY() * h);
			stars.add(pl);
		}
		
		UIView.getEnvView().repaint();
		UIView.getInstance().setCost(solution.getCost());
	}
	
	public void printRects(Graphics g){
		for (int i = 0; i < rects.size(); i++) {
			Rectangle r = rects.get(i);
			g.fillRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(), (int) r.getHeight());
		}
	}
	
	public void printAll(Graphics g){
				
		printRects(g);
		
		for (int i = 0; i < crosses.size(); i++) {
			PositionnedLabel r = crosses.get(i);
			g.drawString(r.getText(), r.getX() + 8, r.getY() + EnvironmentView.case_height - 6);
		}
		
		for (int i = 0; i < stars.size(); i++) {
			PositionnedLabel r = stars.get(i);
			g.drawString(r.getText(), r.getX()+9, r.getY() + EnvironmentView.case_height + 3);
		}
	}
	
	protected class PositionnedLabel{
		
		private int x, y;
		private String label;
		
		public PositionnedLabel(String label, int x, int y){
			this.label = label;
			this.x = x;
			this.y = y;
		}
		
		public int getX(){return x;}
		public int getY(){return y;}
		public String getText(){return label;}
	}
	
}
