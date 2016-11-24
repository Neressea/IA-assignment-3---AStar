package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Point;

import model.AStar;
import model.CellType;
import model.Colored;
import model.Environment;
import model.PathfindingEnv;
import model.PuzzleEnv;
import model.SolutionPath;

public class EnvironmentView extends JPanel{
	
	protected Environment environment;
	protected PathView solution;
	
	public static int case_width, case_height;
	private boolean display_all;
	
	public void setEnv(Environment env){
		environment = env;
		
		if(env instanceof PuzzleEnv){
			case_width = case_height = 60;
		}else{
			case_width = case_height = 30;
			SolutionPath path = ((AStar)env.getAlgo()).getPath();
			solution = new PathView();

			path.addObserver(solution);
		}
		
		this.setPreferredSize(new Dimension(env.getWidth() * case_width, env.getHeight() * case_height));
		this.repaint();
	}
	
	/**
	 * Changes the display.
	 * True: we show the open and closed lists.
	 * False: we only show the shortest path.
	 * @param display
	 */
	public void setComplexDisplay(boolean display){
		display_all = display;
	}
	
	public boolean isComplexDisplay(){
		return display_all;
	}

	@Override
	public void paintComponent(Graphics g) {
		//We clean everything
		g.setColor(Color.WHITE);
		g.clearRect(0, 0, environment.getWidth() * case_width, environment.getHeight() * case_height);
		
		for (int i = 0; i < environment.getWidth(); i++) {
			for (int j = 0; j < environment.getHeight(); j++) {
				Colored ct = environment.getElement(i, j);
				Color c = ct.getColor();
				g.setColor(c);
				g.fillRect(i * case_width, j * case_height, case_width, case_height);
				
				g.setColor(Color.BLACK);
				g.drawRect(i * case_width, j * case_height, case_width, case_height);
				
				g.setFont(new Font("Times New Roman", Font.BOLD, case_width));
				if(ct == CellType.Arrival){
					g.drawString("A", i * case_width + case_width/6, (j + 1) * case_height- case_height/6);
				}else if(ct == CellType.Beginning){
					g.drawString("B", i * case_width + case_width/6, (j + 1) * case_height- case_height/6);
				}
			}
		}
		
		//Finally, we draw the current state of the solution
		g.setFont(new Font("Arial", 0, 30));
		if(environment instanceof PathfindingEnv && display_all){

			solution.printAll(g);
			
		}else if(environment instanceof PathfindingEnv){
			
			solution.printRects(g);
			
		}else if(environment instanceof PuzzleEnv){
			
			PuzzleEnv pe = (PuzzleEnv) environment;
			Point pexit = pe.getExit();
			
			g.setColor(Color.RED);
			g.fillRect((int)pexit.getX() * case_width + case_width, (int)pexit.getY() * case_height, 8, case_height);
			
		}

    }
	
}
