package view;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import model.Environment;
import model.PathfindingEnv;
import model.PuzzleEnv;
import model.UnknownCellTypeException;

public class UIView extends JFrame{
	
	private static UIView singleton;
	private EnvironmentView view;
	private ControlBar bar;
	private JTextField cost;
	
	private UIView(){
		super();
		
		setTitle("Algo tester");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setLayout(new BorderLayout());
		
		bar = new ControlBar();
		
		this.add(bar, BorderLayout.NORTH);
		
		view = new EnvironmentView();
		this.add(view, BorderLayout.CENTER);
		
		cost = new JTextField();
		cost.setEditable(false);
		cost.setText("Cost of the shortest path: ");
		this.add(cost, BorderLayout.SOUTH);
		
		try {
			this.changeView("Problem A.1", "board-1-1");
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(view, "There was a IOException during the file parsing.", "IOException", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch (UnknownCellTypeException e1) {
			JOptionPane.showMessageDialog(view, e1.getMessage(), "Cell Type Error", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
		
		pack();
	    setVisible(true);
	}
	
	public void setCost(int cost){
		this.cost.setText("Cost of the shortest path: " + cost);
	}
	
	public void changeView(String type, String filepath) throws IOException, UnknownCellTypeException{
		
		Environment env = null;
		
		switch(type){
			case "Problem A.1":
			case "Problem A.2":
				env = new PathfindingEnv(filepath);
				view.setComplexDisplay(false);
				break;
			case "Problem A.3":
				env = new PathfindingEnv(filepath);
				view.setComplexDisplay(true);
				break;
			case "Problem B":
				env = new PuzzleEnv(filepath);
				view.setComplexDisplay(false);
				break;
		}
		
		view.setEnv(env);
		this.pack();
	}
	
	public static UIView getInstance(){
		return singleton;
	}
	
	public static EnvironmentView getEnvView(){
		return singleton.view;
	}
	
	public static void launch(){
		singleton = new UIView();
	}
	
	public static void main(String[] args) {
		UIView.launch();
	}

}
